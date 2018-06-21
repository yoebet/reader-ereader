package wjy.yo.ereader.service;

import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;
import wjy.yo.ereader.entityvo.dict.DictEntry;

public interface DictService {

    Maybe<DictEntry> lookup(String word);

    Single<Map<String, String>> loadBaseForms();

}
