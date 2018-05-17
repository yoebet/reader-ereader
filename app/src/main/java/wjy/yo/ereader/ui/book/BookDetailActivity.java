package wjy.yo.ereader.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.booklist.BookListActivity;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;

public class BookDetailActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle arguments = new Bundle();
        bookId = getIntent().getStringExtra(BOOK_ID_KEY);
        if (bookId == null && savedInstanceState != null) {
            bookId = savedInstanceState.getString(BOOK_ID_KEY);
        }

        arguments.putString(BOOK_ID_KEY, bookId);
        BookDetailFragment fragment = new BookDetailFragment();
        fragment.setArguments(arguments);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.book_detail_container, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BOOK_ID_KEY, bookId);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, BookListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        System.out.println("onCreateContextMenu");
//        System.out.println("menuInfo: "+menuInfo);
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.book_context, menu);
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.bc1:
//            case R.id.bc2:
//                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                Toast.makeText(this, item.getTitle(), Toast.LENGTH_SHORT).show();
//                return super.onContextItemSelected(item);
//        }
//    }


    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        System.out.println("dispatchingAndroidInjector: " + dispatchingAndroidInjector);
        return dispatchingAndroidInjector;
    }
}
