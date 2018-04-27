package wjy.yo.ereader.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import wjy.yo.ereader.MainActivity;
import wjy.yo.ereader.R;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.adapter.BookRecyclerViewAdapter;

public class BookListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
        });

        if (findViewById(R.id.book_detail_container) != null) {
            mTwoPane = true;
        }

        RecyclerView recyclerView = findViewById(R.id.book_list);
        recyclerView.setAdapter(new BookRecyclerViewAdapter(this, BookService.BOOKS, mTwoPane));

//        View dv=getWindow().getDecorView();
//        dv.setClickable(true);
//        dv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(BookListActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_books:
                Toast.makeText(this, "All ...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.my_books:
                Toast.makeText(this, "my_books", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
