package wjy.yo.ereader.ui.dict.support;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.view.View;
import android.widget.FrameLayout;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;
import wjy.yo.ereader.ui.dict.DictRequest;
import wjy.yo.ereader.ui.dict.DictView;

import static wjy.yo.ereader.util.Utils.dpToPx;

public abstract class DictBottomSheetActivity extends DictAgentActivity {

    private DictView dictView;

    protected BottomSheetBehavior dictSheetBehavior;

    private BottomSheetCallback mBehaviorCallback = new BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            System.out.println("newState " + newState);
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                if (currentDictRequest != null) {
                    currentDictRequest.callCloseAction();
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//            System.out.println("slideOffset " + slideOffset);
        }
    };

    protected void setupDictSheet(@Nullable Bundle savedInstanceState) {

        dictView = new DictView(popupWindowManager);
        DictCenterBinding binding = dictView.build(this);
        View dictCenter = binding.getRoot();

        FrameLayout dictSheet = findViewById(R.id.dict_sheet);
        dictSheetBehavior = BottomSheetBehavior.from(dictSheet);
        dictSheetBehavior.setHideable(true);
        int px = dpToPx(200, getResources());
        dictSheetBehavior.setPeekHeight(px);
        dictSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        dictSheetBehavior.setBottomSheetCallback(mBehaviorCallback);

        dictSheet.addView(dictCenter);
//        dictSheet.setVisibility(View.VISIBLE);
    }

    @Override
    protected void showDict(DictRequest request) {
        dictView.renderDict(request);

        if (dictSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            dictSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            System.out.println("setState STATE_COLLAPSED ");
        }
    }

    @Override
    public void onBackPressed() {
        if (closePopupIfAny()) {
            return;
        }
        if (dictSheetBehavior != null &&
                dictSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            dictSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }

}
