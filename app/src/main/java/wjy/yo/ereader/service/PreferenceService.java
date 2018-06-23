package wjy.yo.ereader.service;

import java.util.List;

import io.reactivex.Flowable;

public interface PreferenceService {

    String getBaseVocabulary();

    void setBaseVocabulary(String categoryCode);

    String[] getUserWordTags();
}
