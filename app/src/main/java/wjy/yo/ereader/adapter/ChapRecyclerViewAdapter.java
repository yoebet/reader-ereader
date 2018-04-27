package wjy.yo.ereader.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.reader.ReaderActivity;
import wjy.yo.ereader.model.Chap;


public class ChapRecyclerViewAdapter
        extends RecyclerView.Adapter<ChapRecyclerViewAdapter.ViewHolder> {

    private final List<Chap> mValues;
    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Chap item = (Chap) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, ReaderActivity.class);
            intent.putExtra("chap_id", item.getId());

            context.startActivity(intent);
        }
    };

    public ChapRecyclerViewAdapter(List<Chap> chaps) {
        mValues = chaps;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chap_list_content, parent, false);
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