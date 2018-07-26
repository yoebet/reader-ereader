package wjy.yo.ereader.ui.text;

import android.widget.PopupWindow;

public class PopupWindowManager {

    private PopupWindow currentPopup;

    public PopupWindow getCurrentPopup() {
        return currentPopup;
    }

    public void setCurrentPopup(PopupWindow currentPopup) {
        this.currentPopup = currentPopup;
    }

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
