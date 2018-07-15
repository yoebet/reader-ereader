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

    protected DictCenterBinding dictCenterBinding;

    protected BottomSheetBehavior dictSheetBehavior;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        dictView = new DictView();
        dictView.setUserWordService(userWordService);
        dictCenterBinding = dictView.build(this);
        View dictCenter = dictCenterBinding.getRoot();

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

        dictSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}
