package wjy.yo.ereader.ui.booklist;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.BookListContentBinding;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.ui.book.BookDetailActivity;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;

import static wjy.yo.ereader.util.Constants.BOOK_ID_KEY;

public class BookRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Book, BookListContentBinding> {


    @Override
    protected void doOnCreateViewHolder(BookListContentBinding binding) {
        binding.getRoot().setOnClickListener((View view) -> {
            Book book = (Book) view.getTag();
            if (book == null) {
                return;
            }
            String bookId = book.getId();
            Context context = view.getContext();
            Intent intent = new Intent(context, BookDetailActivity.class);
            intent.putExtra(BOOK_ID_KEY, bookId);

            context.startActivity(intent);
        });
    }

    BookRecyclerViewAdapter() {
        super(R.layout.book_list_content, BookListContentBinding::setBook);
    }

}
