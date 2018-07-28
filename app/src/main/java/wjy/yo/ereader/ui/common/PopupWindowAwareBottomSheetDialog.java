package wjy.yo.ereader.ui.common;

import android.content.Context;
import android.support.annotation.NonNull;

import wjy.yo.ereader.ui.text.PopupWindowManager;

public class PopupWindowAwareBottomSheetDialog extends CustomBottomSheetDialog {

    private PopupWindowManager popupWindowManager;

    public PopupWindowAwareBottomSheetDialog(@NonNull Context context) {
        super(context);
    }

    public PopupWindowAwareBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected PopupWindowAwareBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void setPopupWindowManager(PopupWindowManager popupWindowManager) {
        this.popupWindowManager = popupWindowManager;
    }

    @Override
    public void onBackPressed() {
        if (popupWindowManager != null && popupWindowManager.anyPopup()) {
            popupWindowManager.clear();
            return;
        }
        super.onBackPressed();
    }
}
