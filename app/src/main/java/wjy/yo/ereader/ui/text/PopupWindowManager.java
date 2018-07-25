package wjy.yo.ereader.ui.text;

import android.widget.PopupWindow;

public class PopupWindowManager {

    PopupWindow currentPopup;


    public boolean anyPopup() {
        return currentPopup != null;
    }

    public void clear() {
        if (currentPopup != null) {
            currentPopup.dismiss();
            currentPopup = null;
        }
    }
}
