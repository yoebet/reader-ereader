package wjy.yo.ereader.ui.dict;

import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;

public class DictBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Dialog dialog;
    private DictCenterBinding binding;

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

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (dialog != null) {
            return dialog;
        }

        dialog = super.onCreateDialog(savedInstanceState);

        binding = DataBindingUtil
                .inflate(LayoutInflater.from(getContext()), R.layout.dict_center,
                        null, false);
        System.out.println("On onCreateDialog ...");

//        View contentView = View.inflate(getContext(), R.layout.dict_center, null);
        View rootView = binding.getRoot();
        dialog.setContentView(rootView);

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) rootView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        return dialog;
    }

    public void setDictUIRequest(DictUIRequest dictUIRequest) {
        if (binding != null) {
            binding.setEntry(dictUIRequest.entry);
            binding.setUserWord(dictUIRequest.userWord);
            // binding.executePendingBindings();

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
