package wjy.yo.ereader.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import wjy.yo.ereader.model.WordFamiliarity;
import wjy.yo.ereader.service.VocabularyService;

@Singleton
public class VocabularyServiceImpl implements VocabularyService {

//    static String[] myBaseVocabularyGroup=new String[]{"aa","bb"};

    static Map<String, Boolean> myBaseVocabularyMap = new HashMap<>();

    static Map<String, WordFamiliarity> myWordsMap = new HashMap<>();

    @Inject
    public VocabularyServiceImpl(){
        System.out.println("new VocabularyServiceImpl");
    }

    static {
        String[] bvs = new String[]{"a", "of", "the", "they"};
        for (String bv : bvs) {
            myBaseVocabularyMap.put(bv, true);
        }


        String[] myWords = new String[]{"Valley", "Silicon", "pioneer", "Technology", "company", "Stanford", "instance"};
        for (String w : myWords) {
            myWordsMap.put(w, WordFamiliarity.Strange);
        }
    }

    public Map<String, WordFamiliarity> getMyWordsMap(){
        return myWordsMap;
    }

}
