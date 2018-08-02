package wjy.yo.ereader.service;

import io.reactivex.Flowable;

public interface PreferenceService {

    String getBaseVocabulary();

    void setBaseVocabulary(String categoryCode);

    Flowable<String> getBaseVocabularyChangeObservable();

    String[] getUserWordTags();
}
