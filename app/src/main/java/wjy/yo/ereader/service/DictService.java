package wjy.yo.ereader.service;

import io.reactivex.Maybe;
import wjy.yo.ereader.entityvo.dict.DictEntry;

public interface DictService {

    Maybe<DictEntry> lookup(String word);

}
