package wjy.yo.ereader.ui.vocabulary;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wjy.yo.ereader.R;

public class VocabularyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Fragment fragment = new CursorLoaderListFragment();
//                Fragment fragment = ConsListDialogFragment.newInstance(30);
//                fragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.voca_fragment_container, fragment)
//                        .commit();

            BottomSheetDialogFragment bsdf = new CustomBottomSheetDialogFragment();
            bsdf.show(getSupportFragmentManager(), bsdf.getTag());

        });

    }

}
