package wjy.yo.ereader.ui.common;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import wjy.yo.ereader.model.BaseModel;
import wjy.yo.ereader.util.BiFunction;

public abstract class DataBoundRecyclerViewAdapter<M extends BaseModel, B extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundRecyclerViewAdapter.DataBoundViewHolder<B>> {

    @Nullable
    protected List<M> items;

    @LayoutRes
    private int listItemLayoutId;


    private BiFunction<B, M> setter;

    public DataBoundRecyclerViewAdapter(@LayoutRes int listItemLayoutId, BiFunction<B, M> setter) {
        this.listItemLayoutId = listItemLayoutId;
        this.setter = setter;
    }

    @NonNull
    @Override
    public DataBoundViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = createBinding(parent);
        setupEventHandlers(binding.getRoot());
        return new DataBoundViewHolder<>(binding);
    }

    protected B createBinding(ViewGroup parent) {
        B binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), listItemLayoutId,
                        parent, false, null);
        return binding;
    }

    protected void setupEventHandlers(View root) {
    }

    protected void bind(B binding, M m) {
        this.setter.apply(binding, m);
        binding.getRoot().setTag(m);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<B> holder, int position) {
        if (items == null) {
            return;
        }
        M m = items.get(position);
        bind(holder.binding, m);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void resetList(List<M> items) {
        //TODO:
        this.items = items;
        notifyDataSetChanged();
    }

    static class DataBoundViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        final B binding;

        DataBoundViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
