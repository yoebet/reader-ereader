package wjy.yo.ereader.service;

import wjy.yo.ereader.model.DictEntry;

public interface DictService {

    DictEntry lookup(String word);

}
