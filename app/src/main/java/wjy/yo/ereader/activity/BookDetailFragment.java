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

import wjy.yo.ereader.R;
import wjy.yo.ereader.adapter.ChapRecyclerViewAdapter;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.service.BookService;

public class BookDetailFragment extends Fragment {

    public static final String ARG_BOOK_ID = "book_id";

    private Book book;

    public BookDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_BOOK_ID)) {
            book = BookService.BOOK_MAP.get(getArguments().getString(ARG_BOOK_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(book.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.book_detail, container, false);

        if (book != null) {
            ((TextView) rootView.findViewById(R.id.book_name)).setText(book.getName());
            ((TextView) rootView.findViewById(R.id.book_author)).setText(book.getAuthor());
            ((TextView) rootView.findViewById(R.id.book_zh_name)).setText(book.getZhName());
            ((TextView) rootView.findViewById(R.id.book_zh_author)).setText(book.getZhAuthor());

            RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.chap_list);
            assert recyclerView != null;
            recyclerView.setAdapter(new ChapRecyclerViewAdapter(book.getChaps()));
        }

        return rootView;
    }
}
