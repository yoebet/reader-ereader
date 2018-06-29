package wjy.yo.ereader.ui.book;

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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ActivityBookDetailBinding;
import wjy.yo.ereader.databinding.BookInfoBinding;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.service.AnnotationService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.ui.booklist.BookListActivity;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;

public class BookDetailActivity extends AppCompatActivity {

    private String bookId;

    @Inject
    BookService bookService;

    @Inject
    AnnotationService annotationService;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookId = getIntent().getStringExtra(BOOK_ID_KEY);
        if (bookId == null && savedInstanceState != null) {
            bookId = savedInstanceState.getString(BOOK_ID_KEY);
        }

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


        Disposable disp = bookService.loadBookWithUserChaps(bookId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((BookDetail book) -> {
                    if (book == null) {
                        return;
                    }
                    if (!bookId.equals(book.getId())) {
                        return;
                    }
                    System.out.println("book: " + book);

//                    annotationService.loadAnnotations(book.getAnnotationFamilyId())
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(Schedulers.io())
//                            .subscribe(System.out::println);

                    binding.setBook(book);
                    BookInfoBinding bib = binding.bookInfo;
                    bib.setBook(book);

                    List<Chap> chaps = book.getChaps();
                    if (chaps != null) {
//                        System.out.println("chaps: " + chaps);
                        adapter.resetList(chaps);
                    }
                }, Throwable::printStackTrace);
        mDisposable.add(disp);
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
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }
}
