package wjy.yo.ereader.reader;

import android.widget.PopupWindow;

public class PopupWindowManager {

    PopupWindow currentPopup;

    public void clear() {
        if (currentPopup != null) {
            currentPopup.dismiss();
            currentPopup = null;
        }
    }
}
