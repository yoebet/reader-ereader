package wjy.yo.ereader.ui.dict;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.View;

import wjy.yo.ereader.databinding.DictCenterBinding;
import wjy.yo.ereader.ui.common.PopupWindowAwareBottomSheetDialog;
import wjy.yo.ereader.ui.text.PopupWindowManager;

public class DictBottomSheetDialogFragment extends AppCompatDialogFragment {

    private PopupWindowAwareBottomSheetDialog dialog;
    private DictCenterBinding binding;
    private Context context;

    private PopupWindowManager popupWindowManager;

    DictView dictView;

    private DictRequest pendingRequest;

    private BottomSheetBehavior bottomSheetBehavior;

    private BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            System.out.println("newState " + newState);
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            System.out.println("slideOffset " + slideOffset);
        }
    };

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

        setBehaviorCallback();

        if (pendingRequest != null) {
            dictView.renderDict(pendingRequest);
            pendingRequest = null;
        }

        return dialog;
    }

    public void setDictRequest(DictRequest request) {
        if (binding == null || context == null) {
            pendingRequest = request;
            return;
        }
        dictView.renderDict(request);
    }

    private void setBehaviorCallback() {
        View rootView = binding.getRoot();
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) rootView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        System.out.println("behavior " + behavior);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = (BottomSheetBehavior) behavior;
//            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public int getBottomSheetState() {
        if (bottomSheetBehavior == null) {
            System.out.println("bottomSheetBehavior is null.");
            return BottomSheetBehavior.STATE_HIDDEN;
        }
        return bottomSheetBehavior.getState();
    }

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
    }
}
