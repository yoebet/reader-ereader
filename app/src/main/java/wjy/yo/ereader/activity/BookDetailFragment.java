package wjy.yo.ereader.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import wjy.yo.ereader.R;
import wjy.yo.ereader.di.Injectable;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.service.BookService;

public class BookDetailFragment extends Fragment implements Injectable {

    public static final String ARG_BOOK_ID = "book_id";

    private Book book;

    @Inject
    BookService bookService;

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
        final Activity activity = this.getActivity();

        if (getArguments().containsKey(ARG_BOOK_ID)) {
            String bookId = getArguments().getString(ARG_BOOK_ID);
            bookService.getBook(bookId, new Callback<Book>() {

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
            });

        }

//        registerForContextMenu(rootView);

        return rootView;
    }

}
