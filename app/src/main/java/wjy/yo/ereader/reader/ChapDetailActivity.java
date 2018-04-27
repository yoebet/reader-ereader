package wjy.yo.ereader.reader;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.ChapService;

public class ChapDetailActivity extends AppCompatActivity {
    private Chap chap;
    private RecyclerView recyclerView;
    private ParaRecyclerViewAdapter paraRecyclerViewAdapter;

    PopupWindowManager pwm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        String chapId = getIntent().getStringExtra("chap_id");
        chap = ChapService.CHAP_MAP.get(chapId);
//            paras = ChapService.PARAS_MAP.get(getArguments().getString(ARG_CHAP_ID));

        CollapsingToolbarLayout appBarLayout = findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(chap.getName());
        }

        setContentView(R.layout.activity_chap_detail);

        pwm = new PopupWindowManager();

        recyclerView = (RecyclerView) findViewById(R.id.para_list);
        paraRecyclerViewAdapter = new ParaRecyclerViewAdapter(chap.getParas(), pwm);
        recyclerView.setAdapter(paraRecyclerViewAdapter);

//        View rv= getWindow().getDecorView();
//        System.out.println("decorView.isClickable: "+rv.isClickable());
//        rv.setClickable(true);
//        rv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ChapDetailActivity.this, "decorView", Toast.LENGTH_SHORT).show();
//            }
//        });

//        System.out.println("recyclerView.isClickable: "+recyclerView.isClickable());
//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(ChapDetailActivity.this, "recyclerView", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            Intent intent = new Intent(this, BookDetailActivity.class);
//            intent.putExtra(BookDetailFragment.ARG_BOOK_ID, chap.getBookId());
//            navigateUpTo(intent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("onCreateContextMenu");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        System.out.println("onCreateContextMenu");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        System.out.println("onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
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
}
