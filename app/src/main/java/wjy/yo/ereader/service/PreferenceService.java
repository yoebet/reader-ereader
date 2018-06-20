package wjy.yo.ereader.service;

import java.util.List;

import io.reactivex.Flowable;
import wjy.yo.ereader.entity.userdata.UserWordTag;

public interface PreferenceService {

    String getBaseVocabulary();

    void setBaseVocabulary(String categoryCode);

    Flowable<List<UserWordTag>> getUserWordTags();
}
