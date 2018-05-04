package wjy.yo.ereader.activity;

import android.app.Activity;
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

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import wjy.yo.ereader.MainActivity;
import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.adapter.BookRecyclerViewAdapter;

public class BookListActivity extends AppCompatActivity/*  implements HasActivityInjector*/ {

    private boolean mTwoPane;

//    @Inject
//    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    BookService bookService;

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
        List<Book> books=bookService.listAllBooks();
        recyclerView.setAdapter(new BookRecyclerViewAdapter(this, books, mTwoPane));

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

//    @Override
//    public AndroidInjector<Activity> activityInjector(){
//        return dispatchingAndroidInjector;
//    }

}
