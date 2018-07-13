package wjy.yo.ereader.ui.dict;

import wjy.yo.ereader.vo.WordContext;

public interface DictAgent {

//    String getCurrentWord();

    void requestDict(String word, WordContext wordContext);
}
