package wjy.yo.ereader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import wjy.yo.ereader.R;
import wjy.yo.ereader.adapter.ParaRecyclerViewAdapter;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.ChapService;

public class ChapDetailActivity extends AppCompatActivity {
    private Chap chap;

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

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.para_list);
        recyclerView.setAdapter(new ParaRecyclerViewAdapter(chap.getParas()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra(BookDetailFragment.ARG_BOOK_ID, chap.getBookId());
            navigateUpTo(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
