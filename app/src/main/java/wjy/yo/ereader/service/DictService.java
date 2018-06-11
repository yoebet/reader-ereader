package wjy.yo.ereader.service;

import wjy.yo.ereader.entityvo.dict.DictEntry;

public interface DictService {

    DictEntry lookup(String word);

}
