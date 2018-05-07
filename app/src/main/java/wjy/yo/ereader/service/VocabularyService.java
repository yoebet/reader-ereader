package wjy.yo.ereader.service;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

import wjy.yo.ereader.model.UserWord;


public interface VocabularyService {

    Map<String, UserWord.Familiarity> getMyWordsMap();

}
