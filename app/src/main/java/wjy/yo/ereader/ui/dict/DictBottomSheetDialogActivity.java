package wjy.yo.ereader.ui.dict;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public abstract class DictBottomSheetDialogActivity extends DictAgentActivity {

    private DictBottomSheetDialogFragment dictFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dictFragment = new DictBottomSheetDialogFragment();
        dictFragment.setUserWordService(this.userWordService);
    }

    @Override
    protected void showDict(DictRequest request) {
        dictFragment.setDictRequest(request);
        if (!dictFragment.isAdded()) {
            FragmentManager fm = getSupportFragmentManager();
            dictFragment.show(fm, "DictBottomSheetDialog");
        }
    }

}
