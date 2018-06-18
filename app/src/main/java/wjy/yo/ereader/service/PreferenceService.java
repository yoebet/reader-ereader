package wjy.yo.ereader.service;

import io.reactivex.Flowable;

public interface PreferenceService {
    Flowable<String> getPreference(String code);
}
