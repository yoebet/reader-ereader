package wjy.yo.ereader.ui.booklist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.BookListContentBinding;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.ui.book.BookDetailActivity;
import wjy.yo.ereader.ui.book.BookDetailFragment;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;

public class BookRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Book, BookListContentBinding> {

    private final FragmentManager fragmentManager;
    private final boolean mTwoPane;


    @Override
    protected void doOnCreateViewHolder(BookListContentBinding binding) {
        binding.getRoot().setOnClickListener((View view) -> {
            Book book = (Book) view.getTag();
            if (book == null) {
                return;
            }
            String bookId = book.getId();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(BOOK_ID_KEY, bookId);
                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                fragmentManager.beginTransaction()
                        .replace(R.id.book_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BOOK_ID_KEY, bookId);

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
