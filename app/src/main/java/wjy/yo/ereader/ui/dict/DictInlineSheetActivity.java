package wjy.yo.ereader.ui.dict;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;

import static wjy.yo.ereader.util.Constants.DICT_CURRENT_WORD;

public abstract class DictInlineSheetActivity extends DictAgentActivity {

    private DictView dictView;

    private DictCenterBinding dictCenterBinding;

    protected void setupDictSheet(@Nullable Bundle savedInstanceState) {

        dictView = new DictView();
        dictCenterBinding = dictView.build(this);
        View dictCenter = dictCenterBinding.getRoot();
        dictCenter.setVisibility(View.GONE);

        FrameLayout dictSheet = findViewById(R.id.dict_sheet);
        dictSheet.addView(dictCenter);

        String word = getIntent().getStringExtra(DICT_CURRENT_WORD);
        if (word == null && savedInstanceState != null) {
            word = savedInstanceState.getString(DICT_CURRENT_WORD);
        }
        if (word != null) {
            currentWord = word;
        }
    }

    protected void hideDict() {
        if (dictCenterBinding != null) {
            View dictCenter = dictCenterBinding.getRoot();
            dictCenter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void showDict(DictRequest request) {
        dictView.renderDict(request);
        View dictCenter = dictCenterBinding.getRoot();
        dictCenter.setVisibility(View.VISIBLE);
    }

}
