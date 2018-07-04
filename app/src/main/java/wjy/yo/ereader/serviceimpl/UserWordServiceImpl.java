package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.annimon.stream.Stream;

import org.joda.time.LocalDate;
import org.joda.time.Period;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.UserWordDao;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.remote.UserWordAPI;
import wjy.yo.ereader.remotevo.UserWordForSync;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.remotevo.UserWordForAdd;
import wjy.yo.ereader.vo.GroupedUserWords;
import wjy.yo.ereader.vo.GroupedUserWords.ChapterGroup;
import wjy.yo.ereader.vo.GroupedUserWords.CreatedDateGroup;
import wjy.yo.ereader.vo.GroupedUserWords.Group;
import wjy.yo.ereader.vo.VocabularyFilter;
import wjy.yo.ereader.vo.VocabularyFilter.GroupBy;

import static wjy.yo.ereader.entity.userdata.UserWord.ChangeFlagDelete;
import static wjy.yo.ereader.util.RateLimiter.RequestFailOrNoDataRetryRateLimit;

@Singleton
public class UserWordServiceImpl extends UserDataService implements UserWordService {

    @Inject
    UserWordAPI userWordAPI;

    @Inject
    LocalSettingService settingService;

    @Inject
    BookService bookService;

    private DB db;
    private UserWordDao userWordDao;

    private List<UserWord> allWords;

    private Map<String, UserWord> wordsMap;

    @Inject
    public UserWordServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.userWordDao = db.userWordDao();
        observeUserChange();
    }

    protected void onUserChanged() {
        allWords = null;
        wordsMap = null;
    }

    private void setAllWords(List<UserWord> allWords) {
        this.allWords = allWords;
        if (wordsMap == null) {
            wordsMap = new HashMap<>();
        } else {
            wordsMap.clear();
        }
        for (UserWord uw : allWords) {
            wordsMap.put(uw.getWord(), uw);
        }
    }

    public Single<Map<String, UserWord>> getWordsMap() {
        if (wordsMap != null) {
            return Single.just(wordsMap);
        }
        return getAll().map(l -> wordsMap);
    }


    private synchronized Single<List<UserWord>> loadAll() {

        final String userName = this.userName;
        Single<List<UserWord>> dbSource = userWordDao.loadUserWords(userName);

        if (settingService.isOffline()) {
            return dbSource;
        }

        String key = "USER_WORDS_" + userName;
        boolean fetch = RequestFailOrNoDataRetryRateLimit.shouldFetch(key);
        if (!fetch) {
            return dbSource;
        }

        Single<List<UserWord>> netSource = userWordAPI.getAll()
                .map(wl -> {
                    Schedulers.io().scheduleDirect(() -> {
                        db.runInTransaction(() -> {
                            for (UserWord uw : wl) {
                                uw.setUserName(userName);
                                userWordDao.insert(uw);
                            }
                        });
                    });
                    return wl;
                });

        return dbSource.filter(wl -> wl.size() > 0).switchIfEmpty(netSource);
    }

    private List<UserWord> filterDeleted(List<UserWord> all) {
        return Stream.of(all)
                .filter(uw -> uw.getWord() != null
                        && !ChangeFlagDelete.equals(uw.getChangeFlag()))
                .toList();
    }

    public Single<List<UserWord>> getAll() {
        if (allWords != null) {
            return Single.just(allWords).map(this::filterDeleted);
        }

        return loadAll().map(wl -> {
            setAllWords(wl);
            wl = filterDeleted(wl);
            return wl;
        });
    }


    private Stream<UserWord> filterFamiliarity(Stream<UserWord> stream, VocabularyFilter filter) {
        if (filter.isFamiliarityAll()) {
            return stream;
        }
        return stream.filter(uw -> {
            int f = uw.getFamiliarity();
            return filter.isFamiliarity1() && f == 1
                    || filter.isFamiliarity2() && f == 2
                    || filter.isFamiliarity3() && f == 3;
        });
    }

    private Stream<UserWord> filterAddDate(Stream<UserWord> stream, VocabularyFilter filter) {
        return stream.filter(uw -> {
            Period period = filter.getPeriod();
            if (period == null) {
                return true;
            }
            Date createdAt = uw.getCreatedAt();
            if (createdAt == null) {
                System.out.println("createdAt is null: " + uw.getWord());
                return false;
            }

            LocalDate createdOn = new LocalDate(createdAt);
            LocalDate createFrom = LocalDate.now().minus(period);
            return !createdOn.isBefore(createFrom);
        });
    }

    private Stream<Map.Entry<Group, List<UserWord>>> groupUserWords(Stream<UserWord> stream, VocabularyFilter filter) {

        GroupBy groupBy = filter.getGroupBy();
        return stream.groupBy(uw -> {
            if (groupBy == GroupBy.AddDate) {
                Date createdAt = uw.getCreatedAt();
                if (createdAt == null) {
                    return new Group("?");
                }
                LocalDate createdOn = new LocalDate(createdAt);
                return new CreatedDateGroup(createdOn);
            } else if (groupBy == GroupBy.Chapter) {
                String chapId = uw.getChapId();
                if (chapId == null) {
                    return new Group("?");
                }
                return new ChapterGroup(chapId, chapId);
            }
            return new Group("All");
        });
    }

    public Single<List<GroupedUserWords>> filterAndGroup(VocabularyFilter filter) {

        Single<List<GroupedUserWords>> gs = getAll().map(userWords -> {
            Stream<UserWord> stream = Stream.of(userWords);
            stream = filterFamiliarity(stream, filter);
            stream = filterAddDate(stream, filter);
            stream = stream.sortBy(userWord -> {
                Date createdAt = userWord.getCreatedAt();
                if (createdAt == null) {
                    return 0;
                }
                return (int) createdAt.getTime() / 1000;
            });

            Stream<Map.Entry<Group, List<UserWord>>> groups = groupUserWords(stream, filter);
            return groups.map(entry -> {
                Group group = entry.getKey();
                List<UserWord> uws = entry.getValue();
                return new GroupedUserWords(group, uws);
            }).toList();
        });

        GroupBy groupBy = filter.getGroupBy();
        if (groupBy == GroupBy.Chapter) {
            Chap NotFound = new Chap();
            NotFound.setName("?");
            return gs.flattenAsObservable(gl -> gl)
                    .flatMap(grouped -> {
                        Group group = grouped.getGroup();
                        if (!(group instanceof ChapterGroup)) {
                            return Observable.just(grouped);
                        }
                        ChapterGroup cg = (ChapterGroup) group;
                        String chapId = cg.getChapId();
                        return bookService.loadChap(chapId)
                                .toSingle(NotFound)
                                .map(chap -> {
                                    cg.setChap(chap);
                                    cg.setTitle(chap.getName());
                                    return grouped;
                                }).toObservable();
                    }).toList();
        } else if (groupBy == GroupBy.AddDate) {
            return gs.map((List<GroupedUserWords> list) -> {
                Collections.sort(list, (o1, o2) -> o1.getGroup().compareTo(o2.getGroup()));
                return list;
            });
        }

        return gs;
    }


    public Maybe<UserWord> getOne(String word) {
        if (wordsMap != null) {
            UserWord uw = wordsMap.get(word);
            if (uw == null || ChangeFlagDelete.equals(uw.getChangeFlag())) {
                return Maybe.empty();
            }
            return Maybe.just(uw);
        }
        return getWordsMap().filter(m -> m.get(word) != null)
                .map(m -> m.get(word));
    }


    public void add(UserWord userWord) {
        if (userName == null) {
            System.out.println("no user.");
            return;
        }
        setupNewLocal(userWord);
        userWord.setChangeFlag(UserWord.ChangeFlagCreate);
        Schedulers.io().scheduleDirect(() -> {
            userWordDao.insert(userWord);
            if (wordsMap != null) {
                wordsMap.put(userWord.getWord(), userWord);
                allWords.add(userWord);
            }
            UserWordForAdd forAdd = UserWordForAdd.from(userWord);
            userWordAPI.addAWord(forAdd).subscribe(opr -> {
                if (opr.isOk()) {
                    userWord.setLocal(false);
                    userWord.setChangeFlag(null);
                    userWordDao.update(userWord);
                    System.out.println("post: " + userWord.getWord());
                }
            });
        });
    }

    public void update(String word, int familiarity) {
        if (userName == null) {
            System.out.println("no user.");
            return;
        }
        Schedulers.io().scheduleDirect(() -> {
            if (wordsMap == null) {
                return;
            }
            UserWord userWord = wordsMap.get(word);
            if (userWord == null) {
                System.out.println("not found uw: " + word);
                return;
            }
            userWord.setFamiliarity(familiarity);
            updateLocal(userWord);
            String cf = userWord.getChangeFlag();
            if (cf == null || ChangeFlagDelete.equals(cf)) {
                userWord.setChangeFlag(UserWord.ChangeFlagFamiliarity);
            }
            userWordDao.update(userWord);

            if (settingService.isOffline()) {
                return;
            }
            userWordAPI.updateAWord(word, familiarity).subscribe(opr -> {
                if (opr.isOk()) {
                    userWord.setLocal(false);
                    userWord.setChangeFlag(null);
                    userWordDao.update(userWord);
                }
            });
        });

    }

    public void remove(String word) {
        if (userName == null) {
            System.out.println("no user.");
            return;
        }
        Schedulers.io().scheduleDirect(() -> {
            if (wordsMap == null) {
                return;
            }
            UserWord userWord = wordsMap.remove(word);
            if (userWord == null) {
                System.out.println("not found uw: " + word);
                return;
            }
            userWord.setLocal(true);
            userWord.setChangeFlag(ChangeFlagDelete);

            if (settingService.isOffline()) {
                return;
            }
            userWordAPI.removeAWord(word).subscribe(opr -> {
                if (opr.isOk()) {
                    if (wordsMap != null) {
                        wordsMap.remove(word);
                        allWords.remove(userWord);
                    }
                    userWordDao.delete(userWord);
                }
            });
        });
    }

    @SuppressLint("CheckResult")
    void syncWords() {
        if (allWords == null || allWords.size() == 0) {
            return;
        }
        List<UserWord> userWordsToSync = new ArrayList<>();
        for (UserWord uw : allWords) {
            if (uw.isLocal()) {
                userWordsToSync.add(uw);
            }
        }
        List<UserWordForSync> syncList = UserWordForSync.fromUserWords(userWordsToSync);
        userWordAPI.syncWords(syncList).subscribe(opResult -> {
            for (UserWord uw : userWordsToSync) {
                String changeFlag = uw.getChangeFlag();
                uw.setLocal(false);
                uw.setChangeFlag(null);
                if (ChangeFlagDelete.equals(changeFlag)) {
                    if (wordsMap != null) {
                        wordsMap.remove(uw.getWord());
                        allWords.remove(uw);
                    }
                    userWordDao.delete(uw);
                    continue;
                }
                userWordDao.update(uw);
            }
        });
    }
}
