package wjy.yo.ereader.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import wjy.yo.ereader.R;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(BookDetailFragment.ARG_BOOK_ID,
                    getIntent().getStringExtra(BookDetailFragment.ARG_BOOK_ID));
            BookDetailFragment fragment = new BookDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.book_detail_container, fragment)
                    .commit();
        }
//        startActionMode(new ActionMode.Callback() {
//            @Override
//            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//                return false;
//            }
//
//            @Override
//            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//                return false;
//            }
//
//            @Override
//            public void onDestroyActionMode(ActionMode mode) {
//
//            }
//        });


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
}
