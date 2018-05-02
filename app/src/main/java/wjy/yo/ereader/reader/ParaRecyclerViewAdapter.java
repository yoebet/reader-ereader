package wjy.yo.ereader.reader;

import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.method.LinkMovementMethod;
import android.graphics.Color;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Para;
import wjy.yo.ereader.service.VocabularyService;


public class ParaRecyclerViewAdapter
        extends RecyclerView.Adapter<ParaRecyclerViewAdapter.ViewHolder> {

    private final List<Para> mValues;
    private PopupWindowManager pwm;
    private VocabularyService vocabularyService;
//    private RecyclerView recyclerView;

//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        this.recyclerView=recyclerView;
//    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView mContentView = view.findViewById(R.id.content);
            Para para = (Para) view.getTag();
            System.out.println("OnClickListener 1");
            //...
        }
    };

    private final View.OnClickListener mOnClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Para para = (Para) view.getTag();
            System.out.println("OnClickListener 2");
            //...
        }
    };

    private final View.OnLongClickListener mOnLOngClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Para para = (Para) view.getTag();
            System.out.println("OnLongClickListener 1");
            //...
            return true;
        }
    };


    ParaRecyclerViewAdapter(List<Para> paras, PopupWindowManager pwm, VocabularyService vocabularyService) {
        mValues = paras;
        this.pwm = pwm;
        this.vocabularyService = vocabularyService;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.para_content, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Para para = mValues.get(position);
        final TextView lineNoView = holder.lineNoView;
        final TextView contentView = holder.contentView;
        String content = para.getContent();
        SpannableString ss = new SpannableString(para.getContent());
        Set<String> words = vocabularyService.getMyWordsMap().keySet();

        for (String word : words) {
            int wi = content.indexOf(word);

            if (wi >= 0) {
                int end = wi + word.length();
                ss.setSpan(new ForegroundColorSpan(Color.BLUE), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ss.setSpan(new ClickableWordSpan(this.pwm), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        contentView.setText(ss, TextView.BufferType.SPANNABLE);
//        contentView.setTextIsSelectable(true);
        contentView.setMovementMethod(LinkMovementMethod.getInstance());

        holder.itemView.setTag(para);
        contentView.setTag(para);

        lineNoView.setText("" + position);

        ActionMode.Callback ac = new SelectionActionModeCallback(contentView);
//        contentView.startActionMode(ac,ActionMode.TYPE_FLOATING);
//        contentView.startActionMode(ac);

        contentView.setCustomSelectionActionModeCallback(ac);

//        contentView.setOnCreateContextMenuListener();

//        contentView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                System.out.println("onTouch 1");
//                return true;
//            }
//        });

//        holder.itemView.setOnClickListener(mOnClickListener);
//        contentView.setOnClickListener(mOnClickListener2);
//        contentView.setOnLongClickListener(mOnLOngClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        final TextView mIdView;
        final TextView lineNoView;
        final TextView contentView;

        ViewHolder(View view) {
            super(view);
            lineNoView = (TextView) view.findViewById(R.id.line_no);
            contentView = (TextView) view.findViewById(R.id.content);
        }
    }

}
