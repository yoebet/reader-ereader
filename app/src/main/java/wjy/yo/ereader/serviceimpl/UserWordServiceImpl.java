package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
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
import wjy.yo.ereader.remotevo.UserWordForSync;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.remotevo.UserWordForAdd;

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

    private List<UserWord> filterDeleted(List<UserWord> all) {
        List<UserWord> normal = new ArrayList<>();
        for (UserWord uw : all) {
            if (!UserWord.ChangeFlagDelete.equals(uw.getChangeFlag())) {
                normal.add(uw);
            }
        }
        return normal;
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

    public Maybe<UserWord> getOne(String word) {
        if (wordsMap != null) {
            UserWord uw = wordsMap.get(word);
            if (uw == null || UserWord.ChangeFlagDelete.equals(uw.getChangeFlag())) {
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
            if (cf == null || UserWord.ChangeFlagDelete.equals(cf)) {
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
            userWord.setChangeFlag(UserWord.ChangeFlagDelete);

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
                if (UserWord.ChangeFlagDelete.equals(changeFlag)) {
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
