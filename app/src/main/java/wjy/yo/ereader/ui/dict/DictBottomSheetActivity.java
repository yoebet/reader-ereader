package wjy.yo.ereader.ui.dict;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.view.View;
import android.widget.FrameLayout;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;

public abstract class DictBottomSheetActivity extends DictAgentActivity {

    private DictView dictView;

    protected BottomSheetBehavior dictSheetBehavior;


    protected void setupDictSheet(@Nullable Bundle savedInstanceState) {

        dictView = new DictView(userWordService, textSearchService);
        DictCenterBinding binding = dictView.build(this);
        View dictCenter = binding.getRoot();

        FrameLayout dictSheet = findViewById(R.id.dict_sheet);
        dictSheetBehavior = BottomSheetBehavior.from(dictSheet);
        dictSheetBehavior.setHideable(true);
        dictSheetBehavior.setPeekHeight(600);
        dictSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        dictSheet.addView(dictCenter);
    }

    @Override
    protected void showDict(DictRequest request) {
        dictView.renderDict(request);

        if (dictSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            dictSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

    @Override
    public void onBackPressed() {
        if (dictSheetBehavior != null &&
                dictSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            dictSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return;
        }
        super.onBackPressed();
    }

}
