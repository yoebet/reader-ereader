package wjy.yo.ereader.ui.dict;

import android.view.View;

import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.vo.WordContext;

public interface DictAgent {

//    String getCurrentWord();

    void requestDict(String word, WordContext wordContext);

    void requestDictPopup(String word, View anchor, int offsetX, int offsetY, PopupWindowManager pwm);
}
