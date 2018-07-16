package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.PreferenceDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.userdata.Preference;
import wjy.yo.ereader.remotevo.UserPreference;
import wjy.yo.ereader.remote.PreferenceAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.PreferenceService;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.util.Utils;

import static wjy.yo.ereader.util.RateLimiter.RequestFailOrNoDataRetryRateLimit;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_PREFERENCES;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;
import static wjy.yo.ereader.util.Constants.PREF_BASE_VOCABULARY;
import static wjy.yo.ereader.util.Constants.PREF_WORD_TAGS;
import static wjy.yo.ereader.util.Constants.RX_STRING_ELEMENT_NULL;

@Singleton
public class PreferenceServiceImpl extends UserDataService implements PreferenceService {

    @Inject
    PreferenceAPI preferenceAPI;

    private LocalSettingService settingService;

    private DB db;
    private PreferenceDao preferenceDao;

    private Map<String, Preference> preferencesMap;

    private Subject<String> baseVocabularyChangeSubject;

    private String baseVocabulary;

    @Inject
    @SuppressLint("CheckResult")
    public PreferenceServiceImpl(DB db, AccountService accountService,
                                 DataSyncService dataSyncService, LocalSettingService settingService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.preferenceDao = db.preferenceDao();
        this.settingService = settingService;

        baseVocabularyChangeSubject = BehaviorSubject.create();

        observeUserChange();
    }

    protected void onUserChanged() {
        preferencesMap = null;
        loadUserPreferences();
    }

    @SuppressLint("CheckResult")
    private void loadUserPreferences() {
        if (userName == null) {
            return;
        }
        preferenceDao.loadUserPreferences(userName)
                .subscribe(
                        (List<Preference> pl) -> {
                            if (preferencesMap == null) {
                                preferencesMap = new HashMap<>();
                            } else {
                                preferencesMap.clear();
                            }
                            if (pl.size() == 0) {
                                String key = "USER_PREFERENCES_" + userName;
                                boolean fetch = RequestFailOrNoDataRetryRateLimit.shouldFetch(key);
                                if (!fetch) {
                                    return;
                                }
                            }
                            DataSyncRecord dsr = dataSyncService.getUserDataSyncRecord(userName,
                                    DSR_CATEGORY_PREFERENCES, DSR_DIRECTION_DOWN);
                            if (pl.size() > 0) {
                                for (Preference p : pl) {
                                    String code = p.getCode();
                                    preferencesMap.put(code, p);

                                    if (PREF_BASE_VOCABULARY.equals(code)) {
                                        notifyBaseVocabulary(p.getValue());
                                    }
                                }
                                if (!dsr.isStale() && !dataSyncService.checkTimeout(dsr)) {
                                    return;
                                }
                            }
                            if (settingService.isOffline()) {
                                return;
                            }

                            preferenceAPI.get().subscribe((UserPreference up) -> {
                                dsr.setDataVersion(up.getVersion());
                                dataSyncService.renewSyncRecord(dsr);

                                savePreference(up);
                            });
                        },
                        ExceptionHandlers::handle);
    }

    private void notifyBaseVocabulary(String baseVocabulary) {
        if (Objects.equals(this.baseVocabulary, baseVocabulary)) {
            return;
        }
        this.baseVocabulary = baseVocabulary;
        String element = (baseVocabulary == null) ? RX_STRING_ELEMENT_NULL : baseVocabulary;
        baseVocabularyChangeSubject.onNext(element);
    }

    public Observable<String> getBaseVocabularyChangeObservable() {
        return baseVocabularyChangeSubject;
    }

    private void savePreference(UserPreference up) {

        String bv = up.getBaseVocabulary();
        String[] wordTags = up.getWordTags();
        String prefWordTags = Utils.join(wordTags);

        db.runInTransaction(() -> {
            setPreference(PREF_BASE_VOCABULARY, bv, false);
            setPreference(PREF_WORD_TAGS, prefWordTags, false);
        });
    }

    private void setPreference(String code, String value, boolean local) {
        if (preferencesMap == null) {
            return;
        }
        final Preference existed = preferencesMap.get(code);
        if (existed != null) {
            if (Objects.equals(existed.getValue(), value) && existed.isLocal() == local) {
                return;
            }
        }
        if (existed != null) {
            existed.setValue(value);
            existed.setVersion(existed.getVersion() + 1);
            existed.setLocal(local);
            existed.setUpdatedAt(new Date());
            if (!local) {
                existed.setLastFetchAt(new Date());
            }
            preferenceDao.update(existed);
            return;
        }
        Preference pref = new Preference();
        setupNewLocal(pref);
        pref.setLocal(local);
        if (!local) {
            pref.setLastFetchAt(new Date());
        }
        pref.setCode(code);
        pref.setValue(value);
        preferencesMap.put(code, pref);
        preferenceDao.insert(pref);
    }

    private void setPreferenceAsync(String code, String value) {
        Schedulers.io().scheduleDirect(() -> setPreference(code, value, true));
    }


    private String getPreference(String code) {
        if (preferencesMap == null) {
            return null;
        }
        Preference p = preferencesMap.get(code);
        if (p == null) {
            return null;
        }
        return p.getValue();
    }

    public String getBaseVocabulary() {
        return getPreference(PREF_BASE_VOCABULARY);
    }


    public void setBaseVocabulary(String categoryCode) {
        setPreferenceAsync(PREF_BASE_VOCABULARY, categoryCode);
        notifyBaseVocabulary(categoryCode);
    }


    public String[] getUserWordTags() {
        String tagsString = getPreference(PREF_WORD_TAGS);
        if (tagsString == null) {
            return new String[0];
        }
        return tagsString.split(",");
    }

}
