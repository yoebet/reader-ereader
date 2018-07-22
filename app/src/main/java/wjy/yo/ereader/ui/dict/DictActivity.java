package wjy.yo.ereader.ui.dict;

import android.os.Bundle;

import wjy.yo.ereader.R;

public class DictActivity extends DictInlineSheetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict);

        setupDictSheet(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (currentWord != null) {
//            requestDict(currentWord, null);
//        }
        requestDict("prince", null);
    }

}
