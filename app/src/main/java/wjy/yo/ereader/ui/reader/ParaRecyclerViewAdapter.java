package wjy.yo.ereader.ui.reader;

import android.text.method.LinkMovementMethod;
import android.view.ActionMode;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ParaContentBinding;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.ui.text.SelectionActionMode;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.textview.ParaContentTextView;
import wjy.yo.ereader.ui.text.textview.ParaTransTextView;


public class ParaRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Para, ParaContentBinding> {

    private Settings settings;

    ParaRecyclerViewAdapter(Settings settings) {
        super(R.layout.para_content, ParaContentBinding::setPara);
        this.settings = settings;
    }

    @Override
    protected void doOnCreateViewHolder(ParaContentBinding binding) {

        System.out.println("doOnCreateViewHolder ...");

        binding.setTextSetting(settings.getTextSetting());

        final ParaContentTextView contentView = binding.paraContent;
        final ParaTransTextView transView = binding.paraTrans;
        contentView.setSettings(settings);
        transView.setSettings(settings);

//        contentView.setTextIsSelectable(true);
        contentView.setMovementMethod(LinkMovementMethod.getInstance());

        ActionMode.Callback ac = new SelectionActionMode(contentView, settings.getDictAgent());
//        contentView.startActionMode(ac,ActionMode.TYPE_FLOATING);
//        contentView.startActionMode(ac);

        contentView.setCustomSelectionActionModeCallback(ac);

//        contentView.setOnCreateContextMenuListener();

//        binding.getRoot().setOnClickListener(view -> {
//            Para para = (Para) view.getTag();
//            System.out.println("OnClickListener 1");
//            //...
//        });
        contentView.setOnClickListener(view -> {
//            view.setSelected();
//            Para para = (Para) view.getTag();
            System.out.println("OnClickListener 2");
            //...
        });
//        contentView.setOnLongClickListener(view -> {
//            Para para = (Para) view.getTag();
//            System.out.println("OnLongClickListener 1");
//            //...
//            return true;
//        });

    }

    @Override
    protected void doOnBindViewHolder(ParaContentBinding binding) {

        final ParaContentTextView contentView = binding.paraContent;
        final ParaTransTextView transView = binding.paraTrans;

        contentView.setPeer(transView);
        transView.setPeer(contentView);

        Para para = binding.getPara();
        contentView.setTag(para);
        contentView.setRawText(para.getContent());

        transView.setTag(para);
        transView.setRawText(para.getTrans());
    }


    @Override
    public void resetList(List<Para> update) {
        if (update != null) {
            for (int i = 0; i < update.size(); i++) {
                update.get(i).setSeq(i + 1);
            }
        }
        super.resetList(update);
    }

}
