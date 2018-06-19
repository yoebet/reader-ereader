package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import wjy.yo.ereader.entityvo.dict.DictEntry;

public interface DictService {

    Flowable<DictEntry> lookup(String word);

}
