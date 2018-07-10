package wjy.yo.ereader.ui.dict;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import wjy.yo.ereader.databinding.DictCenterBinding;

public class DictBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Dialog dialog;
    private DictCenterBinding binding;
    private Context context;

    DictView dictView;

    private DictRequest pendingRequest;

    private BottomSheetBehavior bottomSheetBehavior;

    private BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };


    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (dictView != null) {
            dictView.setContext(context);
        }
        System.out.println("onAttach " + context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("onCreateDialog " + dialog);
        if (dialog != null) {
            return dialog;
        }

        dialog = super.onCreateDialog(savedInstanceState);
        context = getContext();
        dictView = new DictView();
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
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    public int getBottomSheetState() {
        if (bottomSheetBehavior == null) {
            System.out.println("bottomSheetBehavior is null.");
            return BottomSheetBehavior.STATE_HIDDEN;
        }
        return bottomSheetBehavior.getState();
    }
}
