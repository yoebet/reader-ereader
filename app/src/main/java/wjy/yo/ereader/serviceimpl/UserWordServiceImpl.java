package wjy.yo.ereader.serviceimpl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.UserWordDao;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.remote.UserWordAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.serviceimpl.common.RateLimiter;

@Singleton
public class UserWordServiceImpl extends UserDataService implements UserWordService {

    @Inject
    UserWordAPI userWordAPI;

    @Inject
    LocalSettingService settingService;

//    @Inject
//    PreferenceService preferenceService;

    private DB db;
    private UserWordDao userWordDao;

    private List<UserWord> allWords;

    private Map<String, UserWord> wordsMap;

    private RateLimiter<String> fetchAllWordsRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    public UserWordServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.userWordDao = db.userWordDao();
    }

    protected void onUserChanged() {
        allWords = null;
        wordsMap = null;
    }

    private void saveUserWords(List<UserWord> uws) {
        db.runInTransaction(() -> {
            for (UserWord uw : uws) {
                userWordDao.insert(uw);
            }
        });
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

    public Flowable<Map<String, UserWord>> getWordsMap() {
        if (wordsMap != null) {
            return Flowable.just(wordsMap);
        }
        return getAll().map(l -> wordsMap);
    }

    public Flowable<List<UserWord>> getAll() {
        if (allWords != null) {
            return Flowable.just(allWords);
        }

        return Flowable.create(emitter -> {
            if (userName == null) {
                List<UserWord> uws = Collections.emptyList();
                setAllWords(uws);
                emitter.onNext(uws);
                return;
            }
            userWordDao.loadUserWords(userName)
                    .subscribe(wl -> {
                        if (wl.size() > 0) {
                            setAllWords(wl);
                            emitter.onNext(wl);
                            return;
                        }

                        String key = "USER_WORDS_" + userName;
                        boolean fetch = fetchAllWordsRateLimit.shouldFetch(key);
                        if (!fetch) {
                            setAllWords(wl);
                            emitter.onNext(wl);
                            return;
                        }

                        userWordAPI.getAll().subscribe(wl2 -> {
                            setAllWords(wl2);
                            emitter.onNext(wl2);

                            Schedulers.io().scheduleDirect(() -> {
                                saveUserWords(wl2);
                            });
                        });
                    });
        }, BackpressureStrategy.LATEST);
    }

    public Maybe<UserWord> getOne(String word) {
        if (wordsMap != null) {
            return Maybe.just(wordsMap.get(word));
        }
        return getWordsMap().map(m -> m.get(word)).firstElement();
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
