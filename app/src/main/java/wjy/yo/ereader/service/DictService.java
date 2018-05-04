package wjy.yo.ereader.service;

import java.util.HashMap;
import java.util.Map;

import wjy.yo.ereader.model.DictEntry;
import wjy.yo.ereader.model.dict.MeaningItem;
import wjy.yo.ereader.model.dict.PosMeanings;
import wjy.yo.ereader.model.dict.SimpleMeaning;

public interface DictService {

    DictEntry lookup(String word);

}
