package wjy.yo.ereader.ui.reader;

import android.view.ActionMode;
import android.text.method.LinkMovementMethod;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ParaContentBinding;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.OnTouchBehavior;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.textview.ParaContentTextView;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.Environment;


public class ParaRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Para, ParaContentBinding> {

    private Environment environment;

    private Settings contentSettings;

//    private Settings transSettings;

    ParaRecyclerViewAdapter(PopupWindowManager pwm,
                            DictAgent dictAgent) {
        super(R.layout.para_content, ParaContentBinding::setPara);

        environment = new Environment();
        environment.setPopupWindowManager(pwm);
        environment.setDictAgent(dictAgent);

        contentSettings = new Settings();

        OnTouchBehavior onTouchBehavior = new OnTouchBehavior();
//        onTouchBehavior.setDefaultBehaviorAnyway(true);
        contentSettings.setOnTouchBehavior(onTouchBehavior);
    }

    @Override
    protected void doOnCreateViewHolder(ParaContentBinding binding) {

        System.out.println("doOnCreateViewHolder ...");
        final ParaContentTextView contentView = binding.content;

        contentView.setEnvironment(environment);
        contentView.setSettings(contentSettings);

//        contentView.setTextIsSelectable(true);
        contentView.setMovementMethod(LinkMovementMethod.getInstance());

        ActionMode.Callback ac = new SelectionActionModeCallback(contentView);
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
            Para para = (Para) view.getTag();
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

        final ParaContentTextView contentView = binding.content;

        Para para = binding.getPara();
        contentView.setTag(para);

        String content = para.getContent();

/*        SpannableString ss = new SpannableString(para.getContent());
        Set<String> words = vocabularyService.getMyWordsMap().keySet();

        for (String word : words) {
            int wi = content.indexOf(word);

            if (wi >= 0) {
                int end = wi + word.length();
                ss.setSpan(new ForegroundColorSpan(Color.BLUE), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ss.setSpan(new ClickableWordSpan(this.popupWindowManager), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        contentView.setText(ss, TextView.BufferType.SPANNABLE);*/


        contentView.setRawText(content);

    }


    @Override
    public void resetList(List<Para> update) {
        if (update != null) {
            for (int i = 0; i < update.size(); i++) {
                update.get(i).setNo(i + 1);
            }
        }
        super.resetList(update);
    }

}
