package wjy.yo.ereader.service;

import io.reactivex.Observable;

public interface PreferenceService {

    String getBaseVocabulary();

    void setBaseVocabulary(String categoryCode);

    Observable<String> getBaseVocabularyChangeObservable();

    String[] getUserWordTags();
}
