package wjy.yo.ereader.ui.book;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ActivityBookDetailBinding;
import wjy.yo.ereader.databinding.BookInfoBinding;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.ui.booklist.BookListActivity;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;

public class BookDetailActivity extends AppCompatActivity {

    private String bookId;
    @Inject
    BookViewModel bookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookId = getIntent().getStringExtra(BOOK_ID_KEY);
        if (bookId == null && savedInstanceState != null) {
            bookId = savedInstanceState.getString(BOOK_ID_KEY);
        }
        bookViewModel.setBookId(bookId);

        ActivityBookDetailBinding binding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.activity_book_detail,
                        null, false);

        setContentView(binding.getRoot());

        setSupportActionBar(binding.detailToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        binding.fab.setOnClickListener(view -> Snackbar.make(view,
                "Replace with your own detail action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        RecyclerView recyclerView = binding.chapList;
        ChapRecyclerViewAdapter adapter = new ChapRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);


        LiveData<BookDetail> bookWithChaps = bookViewModel.getBookWithChaps();
        bookWithChaps.observe(this, (BookDetail book) -> {

            if (book == null) {
                return;
            }
            if (!bookId.equals(book.getId())) {
                return;
            }
            System.out.println("book detail: " + book);

            binding.setBook(book);
            binding.bookInfo.setBook(book);

            List<Chap> chaps = book.getChaps();
            if (chaps != null) {
                adapter.resetList(chaps);
            }
        });
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

}
