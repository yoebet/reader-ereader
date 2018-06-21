package wjy.yo.ereader.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.UserWordDao;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.remote.UserWordAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.UserWordService;

import static wjy.yo.ereader.serviceimpl.common.RateLimiter.RequestFailOrNoDataRetryRateLimit;

@Singleton
public class UserWordServiceImpl extends UserDataService implements UserWordService {

    @Inject
    UserWordAPI userWordAPI;

    @Inject
    LocalSettingService settingService;

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


    private Single<List<UserWord>> loadAll() {

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

    public Single<List<UserWord>> getAll() {
        if (allWords != null) {
            return Single.just(allWords);
        }

        return loadAll().map(wl -> {
            setAllWords(wl);
            return wl;
        });
    }

    public Maybe<UserWord> getOne(String word) {
        if (wordsMap != null) {
            UserWord uw = wordsMap.get(word);
            if (uw == null) {
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
            //TODO:
            return;
        }
        setupNewUserData(userWord);
        userWord.setUserName(userName);
        Schedulers.io().scheduleDirect(() -> {
            userWordDao.insert(userWord);
            if (allWords != null) {
                allWords.add(userWord);
            }
            if (wordsMap != null) {
                wordsMap.put(userWord.getWord(), userWord);
            }
            userWordAPI.addAWord(userWord).subscribe(opr -> {
                if (opr.isOk()) {
                    userWord.setLocal(false);
                    userWordDao.update(userWord);
                    System.out.println("post: " + userWord.getWord());
                }
            });
        });
    }

    public void update(String word, int familiarity) {
        if (userName == null) {
            System.out.println("no user.");
            //TODO:
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
            updateUserData(userWord);
            userWordDao.update(userWord);

            if (settingService.isOffline()) {
                return;
            }
            userWordAPI.updateAWord(word, familiarity).subscribe(opr -> {
                if (opr.isOk()) {
                    userWord.setLocal(true);
                    userWordDao.update(userWord);
                }
            });
        });

    }

    public void remove(String word) {
        if (userName == null) {
            System.out.println("no user.");
            //TODO:
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
            //TODO:
            userWordDao.delete(userWord);

            if (settingService.isOffline()) {
                return;
            }
            userWordAPI.removeAWord(word).subscribe(opr -> {
//                if (opr.isOk()) {
//                }
            });
        });
    }
}
