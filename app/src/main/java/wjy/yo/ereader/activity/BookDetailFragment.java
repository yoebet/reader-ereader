package wjy.yo.ereader.activity;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import wjy.yo.ereader.R;
import wjy.yo.ereader.di.Injectable;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.model.Chap;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.viewmodel.BooksViewModel;
import wjy.yo.ereader.databinding.BookDetailBinding;

public class BookDetailFragment extends Fragment implements Injectable {

    public static final String ARG_BOOK_ID = "book_id";

    private Book book;

    @Inject
    BookService bookService;

    @Inject
    BooksViewModel booksViewModel;

    public BookDetailFragment() {
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.book_detail, container, false);

        BookDetailBinding binding = DataBindingUtil
                .inflate(getLayoutInflater(),
                        R.layout.book_detail, container, false);

        LiveData<Book> bookWithChaps = booksViewModel.getBookWithChaps();

        Bundle args = getArguments();
        if (args != null && args.containsKey(ARG_BOOK_ID)) {
            String bookId = getArguments().getString(ARG_BOOK_ID);
            booksViewModel.setBookId(bookId);

            RecyclerView recyclerView = rootView.findViewById(R.id.chap_list);
            ChapRecyclerViewAdapter adapter = new ChapRecyclerViewAdapter(Collections.emptyList());
            recyclerView.setAdapter(adapter);

            bookWithChaps.observe(this, (Book book) -> {
                if (book == null) {
                    return;
                }

                binding.setBook(book);

                Activity activity = this.getActivity();
                if (activity != null) {
                    CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(book.getName());
                    }
                }

                System.out.println(book);
                List<Chap> chaps = book.getChaps();
                if (chaps != null) {
                    adapter.setValues(chaps);
                    adapter.notifyDataSetChanged();
                }
            });

/*            bookService.getBook(bookId, new Callback<Book>() {

                @Override
                public void onResponse(Call<Book> call, Response<Book> response) {
                    book = response.body();
                    if (book == null) {
                        Toast.makeText(activity, "book not exists", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(book.getName());
                    }

                    ((TextView) rootView.findViewById(R.id.book_name)).setText(book.getName());
                    ((TextView) rootView.findViewById(R.id.book_author)).setText(book.getAuthor());
                    ((TextView) rootView.findViewById(R.id.book_zh_name)).setText(book.getZhName());
                    ((TextView) rootView.findViewById(R.id.book_zh_author)).setText(book.getZhAuthor());

                    RecyclerView recyclerView = rootView.findViewById(R.id.chap_list);
                    recyclerView.setAdapter(new ChapRecyclerViewAdapter(book.getChaps()));
                }

                @Override
                public void onFailure(Call<Book> call, Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(activity, "Failed to fetch the book", Toast.LENGTH_SHORT).show();
                }
            });*/

        }

//        registerForContextMenu(rootView);

        return rootView;
    }

}
