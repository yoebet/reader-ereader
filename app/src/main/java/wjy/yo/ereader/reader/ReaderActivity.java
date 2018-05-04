package wjy.yo.ereader.reader;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.ChapService;
import wjy.yo.ereader.service.VocabularyService;

public class ReaderActivity extends AppCompatActivity /*implements HasActivityInjector*/ {
    private Chap chap;
    private RecyclerView recyclerView;
    private ParaRecyclerViewAdapter paraRecyclerViewAdapter;

    PopupWindowManager pwm;

//    @Inject
//    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
//    @Inject
//    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    VocabularyService vocabularyService;
    @Inject
    ChapService chapService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String chapId = getIntent().getStringExtra("chap_id");
        chap = chapService.getChap(chapId);
//            paras = ChapService.PARAS_MAP.get(getArguments().getString(ARG_CHAP_ID));

        setContentView(R.layout.activity_reader);

        TextView chapNameView = (TextView) findViewById(R.id.chap_name);
        chapNameView.setText(chap.getName());

        pwm = new PopupWindowManager();

        recyclerView = findViewById(R.id.para_list);
        paraRecyclerViewAdapter = new ParaRecyclerViewAdapter(chap.getParas(), pwm, vocabularyService);
        recyclerView.setAdapter(paraRecyclerViewAdapter);

//        View pv=getLayoutInflater().inflate(R.layout.popup_window,null);
//        registerForContextMenu(pv);

        final Button button = findViewById(R.id.button_opt);

        final PopupMenu pm = new PopupMenu(button.getContext(), button, Gravity.CENTER);
        pm.getMenuInflater().inflate(R.menu.popup, pm.getMenu());
        pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                System.out.println(item.getTitle());
                Toast.makeText(button.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pm.show();
            }
        });

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
