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
import android.view.ViewGroup;

import java.util.List;
import java.util.Objects;

import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.util.BiFunction;

public abstract class DataBoundRecyclerViewAdapter<M, B extends ViewDataBinding>
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

//        System.out.println("new RecyclerViewAdapter: " + this);
    }

    @NonNull
    @Override
    public DataBoundViewHolder<B> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        B binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), listItemLayoutId,
                        parent, false, null);
        doOnCreateViewHolder(binding);
        return new DataBoundViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DataBoundViewHolder<B> holder, int position) {
        if (items == null) {
            return;
        }
        M m = items.get(position);
        B binding = holder.binding;
        binding.getRoot().setTag(m);
        this.setter.apply(binding, m);
        doOnBindViewHolder(binding);
        binding.executePendingBindings();
    }

    protected void doOnCreateViewHolder(B binding) {
    }

    protected void doOnBindViewHolder(B binding) {
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public boolean areItemsTheSame(M oo, M no) {
        if (oo == null || no == null) {
            return oo == no;
        }
        if (oo instanceof FetchedData) {
            FetchedData ofd = (FetchedData) oo;
            FetchedData nfd = (FetchedData) no;
            return Objects.equals(ofd.getId(), nfd.getId());
        }
        return Objects.equals(oo, no);
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
                            M oo = oldItems.get(oi);
                            M no = update.get(ni);
                            return DataBoundRecyclerViewAdapter.this.areItemsTheSame(oo, no);
                        }

                        @Override
                        public boolean areContentsTheSame(int oi, int ni) {
                            M oo = oldItems.get(oi);
                            M no = update.get(ni);
                            boolean eq = Objects.equals(oo, no);
//                            if (eq) {
//                                System.out.println("Content Equals:\n" + oo + " " + no);
//                            }
                            return eq;
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

    public static class DataBoundViewHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

        final B binding;

        DataBoundViewHolder(B binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
