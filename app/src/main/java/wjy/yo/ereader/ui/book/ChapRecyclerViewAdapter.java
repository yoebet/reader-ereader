package wjy.yo.ereader.ui.book;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ChapListContentBinding;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.ui.reader.ReaderActivity;

import static wjy.yo.ereader.util.Constants.CHAP_ID_KEY;

public class ChapRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Chap, ChapListContentBinding> implements View.OnCreateContextMenuListener {

    public ChapRecyclerViewAdapter() {
        super(R.layout.chap_list_content, ChapListContentBinding::setChap);
    }

    @Override
    protected void doOnCreateViewHolder(ChapListContentBinding binding) {
        View root = binding.getRoot();
        root.setOnClickListener((View view) -> {
            Chap item = (Chap) view.getTag();
            Context context = view.getContext();
            Intent intent = new Intent(context, ReaderActivity.class);
            intent.putExtra(CHAP_ID_KEY, item.getId());

            context.startActivity(intent);
        });

        root.setOnCreateContextMenuListener(this);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, final View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        final Context context = v.getContext();
        System.out.println("OnCreateContextMenuListener, menuInfo: " + menuInfo);
        menu.setHeaderTitle("Select The Action");
        MenuItem mi1 = menu.add(0, v.getId(), 10, "Call");
        mi1.setOnMenuItemClickListener(item -> {
            Toast.makeText(context, "OICL: " + item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });
//            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View actionView = li.inflate(R.layout.dict_action_view, null);
//            mi1.setActionView(actionView);
        menu.add(0, v.getId(), 0, "SMS");

    }
}
