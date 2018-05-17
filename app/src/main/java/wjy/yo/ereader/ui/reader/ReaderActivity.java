package wjy.yo.ereader.ui.reader;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ActivityReaderBinding;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.model.Para;
import wjy.yo.ereader.service.ChapService;
import wjy.yo.ereader.service.VocabularyService;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;
import static wjy.yo.ereader.util.Constants.CHAP_ID_KEY;

public class ReaderActivity extends AppCompatActivity {

    private String chapId;

    PopupWindowManager pwm;

    @Inject
    VocabularyService vocabularyService;

    @Inject
    ChapService chapService;

    @Inject
    ReaderViewModel readerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        chapId = getIntent().getStringExtra(CHAP_ID_KEY);
        if (chapId == null && savedInstanceState != null) {
            chapId = savedInstanceState.getString(CHAP_ID_KEY);
        }

        readerViewModel.setChapId(chapId);

        setContentView(R.layout.activity_reader);


        ActivityReaderBinding binding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.activity_reader,
                        null, false, null);

        pwm = new PopupWindowManager();

        RecyclerView recyclerView = findViewById(R.id.para_list);
        ParaRecyclerViewAdapter adapter = new ParaRecyclerViewAdapter(pwm, vocabularyService);
        recyclerView.setAdapter(adapter);

        LiveData<Chap> chapWithParas = readerViewModel.getChapWithParas();
        chapWithParas.observe(this, (Chap chap) -> {

            if (chap == null) {
                return;
            }
            if (chapId != null && !chapId.equals(chap.getId())) {
                return;
            }
            System.out.println("chap detail: " + chap);

            binding.setChap(chap);
            binding.executePendingBindings();

            List<Para> paras = chap.getParas();
            if (paras != null) {
                adapter.resetList(paras);
            }
        });

//        View pv=getLayoutInflater().inflate(R.layout.popup_window,null);
//        registerForContextMenu(pv);

        final Button button = findViewById(R.id.button_opt);

        final PopupMenu pm = new PopupMenu(button.getContext(), button, Gravity.CENTER);
        pm.getMenuInflater().inflate(R.menu.popup, pm.getMenu());
        pm.setOnMenuItemClickListener(item -> {
            System.out.println(item.getTitle());
            Toast.makeText(button.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });

        button.setOnClickListener(v -> pm.show());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CHAP_ID_KEY, chapId);
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        if (pwm != null) {
            pwm.clear();
            pwm = null;
        }
        super.onDestroy();
    }

//    @Override
//    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
//        return dispatchingAndroidInjector;
//    }

//    @Override
//    public AndroidInjector<Activity> activityInjector(){
//        return dispatchingAndroidInjector;
//    }

}
