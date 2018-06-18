package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.userdata.PreferenceDao;
import wjy.yo.ereader.db.userdata.UserWordTagDao;
import wjy.yo.ereader.entity.userdata.Preference;
import wjy.yo.ereader.entity.userdata.User;
import wjy.yo.ereader.entity.userdata.UserWordTag;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.PreferenceService;

import static wjy.yo.ereader.util.Constants.PreferenceCode_BaseVocabulary;

@Singleton
public class PreferenceServiceImpl implements PreferenceService {

    private PreferenceDao preferenceDao;

    private UserWordTagDao userWordTagDao;

    private AccountService accountService;

    private Map<String, String> preferenceMap;

    private List<UserWordTag> userWordTags;

    private String userName;

    //TODO:
    private List<Preference> unsavedPreference;

    @Inject
    @SuppressLint("CheckResult")
    public PreferenceServiceImpl(DB db, AccountService accountService) {
        this.preferenceDao = db.preferenceDao();
        this.userWordTagDao = db.userWordTagDao();

        this.accountService = accountService;

        this.unsavedPreference = new ArrayList<>();

        accountService.getUserChangeObservable().subscribe((User user) -> {
            userName = user.getName();
            userWordTags = null;
            this.preferenceMap = null;
        });
    }

    private Flowable<Map<String, String>> getPreferenceMap() {
        if (preferenceMap != null) {
            return Flowable.just(preferenceMap);
        }
        return preferenceDao.loadUserPreferences(userName)
                .map((List<Preference> pl) -> {
                    if (preferenceMap == null) {
                        preferenceMap = new HashMap<>();
                    } else {
                        preferenceMap.clear();
                    }
                    for (Preference p : pl) {
                        preferenceMap.put(p.getCode(), p.getValue());
                    }

                    return preferenceMap;
                });
    }


    public Flowable<String> getPreference(String code) {
        return getPreferenceMap().map(pm -> pm.get(code));
    }

    public Flowable<String> getBaseVocabulary() {
        return getPreference(PreferenceCode_BaseVocabulary);
    }


    public void setBaseVocabulary(String categoryCode) {
        setPreference(PreferenceCode_BaseVocabulary, categoryCode);
    }


    public void setPreference(String code, String value) {
        if (preferenceMap != null) {
            preferenceMap.put(code, value);
        }
        Preference p = new Preference();
        p.setUserName(userName);
        p.setCode(code);
        p.setValue(value);
        p.setLocal(true);
        unsavedPreference.add(p);
    }


    public Flowable<List<UserWordTag>> getUserWordTags() {
        if (userWordTags != null) {
            return Flowable.just(userWordTags);
        }
        return userWordTagDao.loadUserWordTags(userName)
                .map((List<UserWordTag> ts) -> {
                    this.userWordTags = ts;
                    return ts;
                });
    }

}
