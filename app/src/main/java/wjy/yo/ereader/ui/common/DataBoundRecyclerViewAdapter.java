package wjy.yo.ereader.ui.common;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import wjy.yo.ereader.model.BaseModel;
import wjy.yo.ereader.util.BiFunction;

public abstract class DataBoundRecyclerViewAdapter<M extends BaseModel, B extends ViewDataBinding>
        extends RecyclerView.Adapter<DataBoundRecyclerViewAdapter.DataBoundViewHolder<B>> {

    @Nullable
    protected List<M> items;

    private int dataVersion = 0;

    @LayoutRes
    private int listItemLayoutId;


    private BiFunction<B, M> setter;

    public DataBoundRecyclerViewAdapter(@LayoutRes int listItemLayoutId, BiFunction<B, M> setter) {
        this.listItemLayoutId = listItemLayoutId;
        this.setter = setter;

        System.out.println("new RecyclerViewAdapter: " + this);
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


    @MainThread
    @SuppressLint("StaticFieldLeak")
    public void resetList(List<M> update) {
        dataVersion++;
        if (items == null) {
            if (update == null) {
                return;
            }
            items = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = items.size();
            items = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult doInBackground(Void... voids) {

                    final List<M> oldItems = items;
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {

                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oi, int ni) {
                            M oldItem = oldItems.get(oi);
                            M newItem = update.get(ni);
                            if (oldItem == null) {
                                return newItem == null;
                            }
                            return Objects.equals(oldItem.getId(), newItem.getId());
                        }

                        @Override
                        public boolean areContentsTheSame(int oi, int ni) {
                            return Objects.equals(oldItems.get(oi), update.get(ni));
                        }
                    }, false);
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    items = update;
                    diffResult.dispatchUpdatesTo(DataBoundRecyclerViewAdapter.this);
                }
            }.execute();
        }
    }

    static class DataBoundViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        final B binding;

        DataBoundViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
