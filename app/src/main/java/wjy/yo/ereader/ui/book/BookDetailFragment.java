package wjy.yo.ereader.ui.book;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import wjy.yo.ereader.R;
import wjy.yo.ereader.di.Injectable;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entityvo.book.BookDetail;
import wjy.yo.ereader.service.BookService;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;

public class BookDetailFragment extends Fragment implements Injectable {

    @Inject
    BookService bookService;

    @Inject
    BookViewModel bookViewModel;

    public BookDetailFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.book_detail, container, false);


        Bundle args = getArguments();
        if (args == null || !args.containsKey(BOOK_ID_KEY)) {
            return rootView;
        }

        final String bookId = getArguments().getString(BOOK_ID_KEY);
        if (bookId == null) {
            return rootView;
        }

        bookViewModel.setBookId(bookId);

        RecyclerView recyclerView = rootView.findViewById(R.id.chap_list);
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

            Activity activity = this.getActivity();
            if (activity != null) {
                CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(book.getName());
                }
            }

            List<Chap> chaps = book.getChaps();
            if (chaps != null) {
                adapter.resetList(chaps);
            }
        });

//        registerForContextMenu(rootView);

        return rootView;
    }

}
