package wjy.yo.ereader.reader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
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
    private PopupWindowManager pwm;
    private int clickCounter = 0;
//    private RecyclerView recyclerView;

//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        this.recyclerView=recyclerView;
//    }

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            TextView mContentView = view.findViewById(R.id.content);
            Para para = (Para) view.getTag();
            //...
        }
    };

    private final View.OnClickListener mOnClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Para para = (Para) view.getTag();
            //...
        }
    };

    private final View.OnLongClickListener mOnLOngClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            Para para = (Para) view.getTag();
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


    ParaRecyclerViewAdapter(List<Para> paras, PopupWindowManager pwm) {
        mValues = paras;
        this.pwm = pwm;
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
        final TextView mContentView = holder.mContentView;
        String content = para.getContent();
        SpannableString ss = new SpannableString(para.getContent());
        String[] words = new String[]{"Valley", "Silicon", "pioneer", "Technology", "company", "Stanford"};

        for (String word : words) {
            int wi = content.indexOf(word);

            if (wi >= 0) {
                int end = wi + word.length();
                ss.setSpan(new ForegroundColorSpan(Color.BLUE), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                ss.setSpan(new ClickableWordSpan(this.pwm), wi, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }

        mContentView.setText(ss, TextView.BufferType.SPANNABLE);
        mContentView.setTextIsSelectable(true);
        mContentView.setMovementMethod(LinkMovementMethod.getInstance());

        holder.itemView.setTag(para);
        mContentView.setTag(para);

        mContentView.setCustomSelectionActionModeCallback(new SelectionActionModeCallback(mContentView));

//        mContentView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return false;
//            }
//        });

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
