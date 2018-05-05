package wjy.yo.ereader.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Book;


public class BookRecyclerViewAdapter
        extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private final BookListActivity mParentActivity;
    private final List<Book> mValues;
    private final boolean mTwoPane;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Book item = (Book) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putString(BookDetailFragment.ARG_BOOK_ID, item.getId());
                BookDetailFragment fragment = new BookDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.book_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, BookDetailActivity.class);
                intent.putExtra(BookDetailFragment.ARG_BOOK_ID, item.getId());

                context.startActivity(intent);
            }
        }
    };

    BookRecyclerViewAdapter(BookListActivity parent,
                            List<Book> books,
                            boolean twoPane) {
        mValues = books;
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
//        holder.mIdView.setText(mValues.get(position).getId());
        holder.mNameView.setText(mValues.get(position).getName());

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
//        final TextView mIdView;
        final TextView mNameView;

        ViewHolder(View view) {
            super(view);
//            mIdView = (TextView) view.findViewById(R.id.id_text);
            mNameView = (TextView) view.findViewById(R.id.name);
        }
    }
}
