package wjy.yo.ereader.ui.vocabulary;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import wjy.yo.ereader.R;

public class DictBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private TextView mOffsetText;
    private TextView mStateText;
    private BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            setStateText(newState);
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            setOffsetText(slideOffset);
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        View contentView = View.inflate(getContext(), R.layout.dict_entry, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        mOffsetText = (TextView) contentView.findViewById(R.id.offsetText);
        mStateText = (TextView) contentView.findViewById(R.id.stateText);

        return dialog;
    }

    @Override
    public void onViewCreated(View contentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(contentView, savedInstanceState);
//
//        recyclerView.setLayoutManager(mLinearLayoutManager);
//        recyclerView.setAdapter(mAdapter);
    }


    private void setOffsetText(final float slideOffset) {
        ViewCompat.postOnAnimation(mOffsetText, () -> mOffsetText.setText(getString(R.string.offset, slideOffset)));
    }

    private void setStateText(int newState) {
//        mStateText.setText(BottomSheetActivity.getStateAsString(newState));
    }
}
