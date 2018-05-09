package wjy.yo.ereader.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.viewmodel.JsonViewModel;

public class VocabularyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Fragment fragment = new CursorLoaderListFragment();
                Fragment fragment = ConsListDialogFragment.newInstance(30);
//                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.voca_fragment_container, fragment)
                        .commit();
            }
        });

        JsonViewModel model =
                ViewModelProviders.of(this).get(JsonViewModel.class);
        model.getData().observe(this, data -> {
            // update UI
        });
    }

}
