package wjy.yo.ereader.ui.dict;

import android.view.View;
import android.widget.PopupWindow;

import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.util.Action;
import wjy.yo.ereader.util.Consumer;
import wjy.yo.ereader.util.Offset;
import wjy.yo.ereader.vo.WordContext;

public interface DictAgent {

//    String getCurrentWord();

    void requestDict(String word, WordContext wordContext);

    void requestDict(String word,
                     WordContext wordContext,
                     Action onOpen,
                     Action onClose);

    void requestDictPopup(String word,
                          View anchor,
                          Offset offset,
                          PopupWindowManager pwm,
                          Consumer<PopupWindow> onPopup,
                          PopupWindow.OnDismissListener onDismissListener);
}
