package wjy.yo.ereader.service;

import wjy.yo.ereader.entityvo.DictEntry;

public interface DictService {

    DictEntry lookup(String word);

}
