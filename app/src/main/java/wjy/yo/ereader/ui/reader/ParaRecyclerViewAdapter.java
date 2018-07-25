package wjy.yo.ereader.ui.reader;

import android.view.ActionMode;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ParaContentBinding;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.ui.dict.DictAgent;


public class ParaRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<Para, ParaContentBinding> {

    private PopupWindowManager pwm;

    private DictAgent dictAgent;


    ParaRecyclerViewAdapter(PopupWindowManager pwm,
                            DictAgent dictAgent) {
        super(R.layout.para_content, ParaContentBinding::setPara);
        this.pwm = pwm;

        this.dictAgent = dictAgent;
    }

    @Override
    protected void doOnCreateViewHolder(ParaContentBinding binding) {

        System.out.println("doOnCreateViewHolder ...");
        final ParaTextView contentView = binding.content;

        contentView.setDictAgent(dictAgent);

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

        final ParaTextView contentView = binding.content;

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
                ss.setSpan(new ClickableWordSpan(this.pwm), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        contentView.setText(ss, TextView.BufferType.SPANNABLE);*/

        if (content.indexOf('<') == -1) {
            contentView.setText(content);
            return;
        }


        TagHandler th = new TagHandler(pwm);
        Spanned spanned = HtmlParser.buildSpannedText(content, th);
//        System.out.println(para.getNo() + ": " + spanned);
//        String htmlText=Html.toHtml(spanned);
//        System.out.println(para.getNo() + ": " + htmlText);
        contentView.setText(spanned);

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
