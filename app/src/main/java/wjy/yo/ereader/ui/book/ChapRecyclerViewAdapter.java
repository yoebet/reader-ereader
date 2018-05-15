package wjy.yo.ereader.ui.book;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ChapListContentBinding;
import wjy.yo.ereader.ui.reader.ReaderActivity;
import wjy.yo.ereader.model.Chap;


public class ChapRecyclerViewAdapter
        extends RecyclerView.Adapter<ChapRecyclerViewAdapter.ViewHolder> {

    private List<Chap> mValues;
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

    public List<Chap> getValues() {
        return mValues;
    }

    public void setValues(List<Chap> mValues) {
        this.mValues = mValues;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.chap_list_content, parent, false);
//        return new ViewHolder(view);

        ChapListContentBinding dataBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.chap_list_content, parent, false);
//        dataBinding.
        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Chap chap = mValues.get(position);
//        holder.mIdView.setText(chap.getId());
//        holder.mNameView.setText(chap.getName());

        ChapListContentBinding binding = (ChapListContentBinding) holder.binding;
        binding.setChap(chap);

        holder.itemView.setTag(chap);
        holder.itemView.setOnClickListener(mOnClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        //        final TextView mIdView;
//        final TextView mNameView;
        ViewDataBinding binding;

        ViewHolder(/*View view*/ViewDataBinding binding) {
            super(binding.getRoot());
//            mIdView = (TextView) view.findViewById(R.id.id_text);
//            mNameView = (TextView) view.findViewById(R.id.name);

            this.binding = binding;
            binding.executePendingBindings();

            binding.getRoot().setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, final View v,
                                        ContextMenu.ContextMenuInfo menuInfo) {

            final Context context = v.getContext();
            System.out.println("OnCreateContextMenuListener, menuInfo: " + menuInfo);
            menu.setHeaderTitle("Select The Action");
            MenuItem mi1 = menu.add(0, v.getId(), 10, "Call");
            mi1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Toast.makeText(context, "OICL: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
//            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View actionView = li.inflate(R.layout.dict_action_view, null);
//            mi1.setActionView(actionView);
            menu.add(0, v.getId(), 0, "SMS");

        }
    }
}