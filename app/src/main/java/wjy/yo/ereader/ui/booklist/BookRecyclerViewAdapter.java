package wjy.yo.ereader.ui.booklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.BookListContentBinding;
import wjy.yo.ereader.model.Book;
import wjy.yo.ereader.ui.book.BookDetailActivity;
import wjy.yo.ereader.ui.book.BookDetailFragment;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;


public class BookRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Book, BookListContentBinding> {

    private final FragmentManager fragmentManager;
    private final boolean mTwoPane;


    @Override
    protected void setupEventHandlers(View root) {
        root.setOnClickListener((View view) -> {
            Book book = (Book) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(BookDetailFragment.ARG_BOOK_ID, book.getId());
                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                fragmentManager.beginTransaction()
                        .replace(R.id.book_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailFragment.ARG_BOOK_ID, book.getId());

                context.startActivity(intent);
            }
        });
    }

    BookRecyclerViewAdapter(FragmentManager fragmentManager,
                            boolean twoPane) {
        super(R.layout.book_list_content, BookListContentBinding::setBook);
        this.fragmentManager = fragmentManager;
        mTwoPane = twoPane;
    }

}
