package wjy.yo.ereader.ui.dict.support;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialogFragment;

import wjy.yo.ereader.databinding.DictCenterBinding;
import wjy.yo.ereader.ui.common.PopupWindowAwareBottomSheetDialog;
import wjy.yo.ereader.ui.dict.DictRequest;
import wjy.yo.ereader.ui.dict.DictView;
import wjy.yo.ereader.ui.text.PopupWindowManager;

public class DictBottomSheetDialogFragment extends AppCompatDialogFragment {

    private PopupWindowAwareBottomSheetDialog dialog;
    private DictCenterBinding binding;
    private Context context;

    private PopupWindowManager popupWindowManager;

    DictView dictView;

    private DictRequest currentRequest;

    private DictRequest pendingRequest;

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (dictView != null) {
            dictView.setContext(context);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        System.out.println("onCreateDialog " + dialog);
        if (dialog != null) {
            return dialog;
        }

        if (popupWindowManager == null) {
            popupWindowManager = new PopupWindowManager();
        }

        context = getContext();
        dialog = new PopupWindowAwareBottomSheetDialog(context, getTheme());
        dialog.setPopupWindowManager(popupWindowManager);

        dictView = new DictView(popupWindowManager);
        binding = dictView.build(context);

        dialog.setContentView(binding.getRoot());

        if (pendingRequest != null) {
            dictView.renderDict(pendingRequest);
            currentRequest = pendingRequest;
            pendingRequest = null;
        }

        return dialog;
    }

    public void setDictRequest(DictRequest request) {
        if (binding == null || context == null) {
            pendingRequest = request;
            return;
        }
        currentRequest = request;
        dictView.renderDict(request);
    }

    /*public int getBottomSheetState() {

        if (dialog == null) {
            System.out.println("dialog is null.");
            return BottomSheetBehavior.STATE_HIDDEN;
        }
        BottomSheetBehavior behavior = dialog.getBottomSheetBehavior();
        if (behavior == null) {
            System.out.println("bottomSheetBehavior is null.");
            return BottomSheetBehavior.STATE_HIDDEN;
        }
        return behavior.getState();
    }*/

    @Override
    public void onDetach() {
        if (popupWindowManager != null && popupWindowManager.anyPopup()) {
            popupWindowManager.clear();
        }
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dictView != null) {
            dictView.clear();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (currentRequest != null) {
            currentRequest.callCloseAction();
        }
    }
}
