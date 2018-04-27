package wjy.yo.ereader.reader;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.ChapService;

public class ReaderActivity extends AppCompatActivity {
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

        setContentView(R.layout.activity_reader);

        TextView chapNameView = (TextView) findViewById(R.id.chap_name);
        chapNameView.setText(chap.getName());

        pwm = new PopupWindowManager();

        recyclerView = (RecyclerView) findViewById(R.id.para_list);
        paraRecyclerViewAdapter = new ParaRecyclerViewAdapter(chap.getParas(), pwm);
        recyclerView.setAdapter(paraRecyclerViewAdapter);

        View pv=getLayoutInflater().inflate(R.layout.popup_window,null);

        registerForContextMenu(pv);

        final Button button=(Button)findViewById(R.id.button_opt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ReaderActivity.this.openContextMenu(button);
//                startActionMode()
            }
        });

    }


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
