package wjy.yo.ereader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.graphics.Color;
import android.widget.Toast;

import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Para;


public class ParaRecyclerViewAdapter
        extends RecyclerView.Adapter<ParaRecyclerViewAdapter.ViewHolder> {

    private final List<Para> mValues;
    private int clickCounter = 0;

    private void switchStyle(TextView mContentView, Para para) {

        CharSequence cs = mContentView.getText();
        if (!(cs instanceof SpannableString)) {
            return;
        }
        SpannableString ss = (SpannableString) cs;
        clickCounter++;
        String content = para.getContent();
        String word = "Valley";
        int wi = content.indexOf(word);
        if (wi >= 0) {
            StyleSpan spans[] = ss.getSpans(wi, wi + word.length(), StyleSpan.class);
            for (StyleSpan span : spans) {
                ss.removeSpan(span);
                System.out.println("remove SPAN: " + span);
            }
            int tf = (clickCounter % 2 == 0) ? Typeface.BOLD : Typeface.ITALIC;
            ss.setSpan(new StyleSpan(tf), wi, wi + word.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        word = "generation";
        wi = content.indexOf(word);
        if (wi >= 0) {
            ForegroundColorSpan spans[] = ss.getSpans(wi, wi + word.length(), ForegroundColorSpan.class);
            for (ForegroundColorSpan span : spans) {
                ss.removeSpan(span);
                System.out.println("remove SPAN: " + span);
            }
            int fc = (clickCounter % 2 == 0) ? Color.BLUE : Color.GREEN;
            ss.setSpan(new ForegroundColorSpan(fc), wi, wi + word.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        }
        word = "pioneer";
        wi = content.indexOf(word);
        if (wi >= 0) {
            ForegroundColorSpan spans[] = ss.getSpans(wi, wi + word.length(), ForegroundColorSpan.class);
            for (ForegroundColorSpan span : spans) {
                ss.removeSpan(span);
                System.out.println("remove SPAN: " + span);
            }
            int fc = (clickCounter % 2 == 0) ? Color.RED : Color.CYAN;
            ss.setSpan(new ForegroundColorSpan(fc), wi, wi + word.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        System.out.println(content);

        mContentView.setText(ss);
    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView mContentView = view.findViewById(R.id.content);
            Para para = (Para) view.getTag();
            switchStyle(mContentView, para);
            //...
        }
    };

    private final View.OnClickListener mOnClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Para para = (Para) view.getTag();
            switchStyle((TextView) view, para);
            //...
        }
    };

    private final View.OnLongClickListener mOnLOngClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Para para = (Para) view.getTag();
            switchStyle((TextView) view, para);
            //...
            return true;
        }
    };

//    private final View.OnTouchListener mOnTouchListener=new View.OnTouchListener(){
//        @Override
//        public boolean onTouch(View v, MotionEvent event){
////            event.getAction()
//            return true;
//        }
//    };

    private class NewWord extends ClickableSpan {
        String word;
        int start;
        int end;

        NewWord(String word, int start, int end) {
            this.word = word;
            this.start = start;
            this.end = end;
        }

        @Override
        public void onClick(View widget) {
            System.out.println(word);
            TextView tv = (TextView) widget;
            CharSequence cs = tv.getText();
            if (!(cs instanceof SpannableString)) {
                return;
            }
            Toast.makeText(widget.getContext().getApplicationContext(), cs, Toast.LENGTH_SHORT).show();

            SpannableString ss = (SpannableString) cs;
            ForegroundColorSpan spans[] = ss.getSpans(start, end, ForegroundColorSpan.class);
            int color = Color.RED;
            for (ForegroundColorSpan span : spans) {
                color = span.getForegroundColor();
                ss.removeSpan(span);
                System.out.println("remove SPAN: " + span);
            }
            int fc = (color == Color.RED) ? Color.GREEN : Color.RED;
            ss.setSpan(new ForegroundColorSpan(fc), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
//        @Override
//        public void updateDrawState(TextPaint ds) {
////            ds.setColor(ds.linkColor);
//            ds.setUnderlineText(true);
//        }
    }

    public ParaRecyclerViewAdapter(List<Para> paras) {
        mValues = paras;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.para_list_content, parent, false);
        return new ViewHolder(view);
    }

//    @Override
//    public boolean onTextContextMenuItem(int id) {
//        boolean result=super.onTextContextMenuItem(id);
//
//        if (id==android.R.id.cut || id==android.R.id.paste) {
//        }
//
//        return(result);
//    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Para para = mValues.get(position);
        TextView mContentView = holder.mContentView;
        String content = para.getContent();
        SpannableString ss = new SpannableString(para.getContent());
        String[] words = new String[]{"Valley", "Silicon", "pioneer"};

        for (String word : words) {
            int wi = content.indexOf(word);

            if (wi >= 0) {
                int end = wi + word.length();
                ss.setSpan(new ForegroundColorSpan(Color.BLUE), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ss.setSpan(new NewWord(word, wi, end), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        mContentView.setText(ss, TextView.BufferType.SPANNABLE);
        mContentView.setTextIsSelectable(true);
        mContentView.setMovementMethod(LinkMovementMethod.getInstance());

        holder.itemView.setTag(para);
        mContentView.setTag(para);
        mContentView.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                System.out.println("onCreateActionMode");
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                System.out.println("onPrepareActionMode");
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                System.out.println("onActionItemClicked");
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                System.out.println("onDestroyActionMode");

            }
        });
//        holder.itemView.setOnClickListener(mOnClickListener);
//        mContentView.setOnClickListener(mOnClickListener2);
//        mContentView.setOnLongClickListener(mOnLOngClickListener);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //        final TextView mIdView;
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
//            mIdView = (TextView) view.findViewById(R.id.id_text);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}