package wjy.yo.ereader.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
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

    private Activity context;
    private final List<Para> mValues;
    private int clickCounter = 0;
//    private RecyclerView recyclerView;

//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        this.recyclerView=recyclerView;
//    }

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

    PopupWindow currentPopup;

    private class NewWord extends ClickableSpan {
        String word;
        int start;
        int end;
        PopupWindow pw;
        int offsetY = 0;
        int offsetX = 0;

        NewWord(String word, int start, int end) {
            this.word = word;
            this.start = start;
            this.end = end;
        }

        private void evaluatePosition(TextView textView) {

            Log.d("evaluatePosition", "------");

            Layout textLayout = textView.getLayout();

//            SpannableString ss = (SpannableString) textView.getText();
//            int startOffset = ss.getSpanStart(this);
//            int endOffset = ss.getSpanEnd(this);
            int startOffset = start;
            int endOffset = end;
            float startXCoordinate = textLayout.getPrimaryHorizontal(startOffset);
            float endXCoordinate = textLayout.getPrimaryHorizontal(endOffset);
            Log.d("XCoordinates", "" + startXCoordinate + "-" + endXCoordinate);

            int startLine = textLayout.getLineForOffset(startOffset);
            int endLine = textLayout.getLineForOffset(endOffset);
            Log.d("LineSpan", "" + startLine + "-" + endLine);

//            int wh=textView.getWidth();
            if (startLine == endLine) {
                this.offsetX = (int) startXCoordinate;
            }

            int th = textView.getHeight();
            Log.d("getHeight", "" + th);
            Rect rect = new Rect();
            int baseLine2 = textLayout.getLineBounds(endLine, rect);
            int dy = th - rect.bottom;
            this.offsetY = -dy;

            Log.d("offset", "offset:" + offsetY + "," + offsetY);
        }

        @Override
        public void onClick(View widget) {
            System.out.println(word);
            TextView tv = (TextView) widget;
            CharSequence cs = tv.getText();
            if (!(cs instanceof SpannableString)) {
                return;
            }
//            Toast.makeText(widget.getContext().getApplicationContext(), cs, Toast.LENGTH_SHORT).show();

            SpannableString ss = (SpannableString) cs;
            ForegroundColorSpan spans[] = ss.getSpans(start, end, ForegroundColorSpan.class);
            int color = Color.BLUE;
            for (ForegroundColorSpan span : spans) {
                color = span.getForegroundColor();
                ss.removeSpan(span);
                System.out.println("remove SPAN: " + span);
            }
            int fc = (color == Color.BLUE) ? Color.GREEN : Color.BLUE;
            ss.setSpan(new ForegroundColorSpan(fc), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

/*            PopupMenu pm = new PopupMenu(ParaRecyclerViewAdapter.this.context, widget, Gravity.CENTER);
            pm.getMenuInflater().inflate(R.menu.popup, pm.getMenu());
            pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    System.out.println(item.getTitle());
                    return true;
                }
            });
            pm.show();*/

//            Layout layout=tv.getLayout();
//            int lineStart=layout.getLineForOffset(start);
//            int lineEnd=layout.getLineForOffset(end);
            evaluatePosition(tv);


            if (pw == null || !pw.isShowing()) {
                if (currentPopup != null) {
                    currentPopup.dismiss();
                    currentPopup = null;
                }
//            LayoutInflater li = LayoutInflater.from(ParaRecyclerViewAdapter.this.context);
                LayoutInflater li = context.getLayoutInflater();
//            ViewGroup vg=(ViewGroup) context.findViewById(R.id.para_list);
//            Context context = ParaRecyclerViewAdapter.this.context;

                View v = li.inflate(R.layout.popup_window, null);
                TextView titleView = v.findViewById(R.id.cword);
                titleView.setText(word);

//            View vt=context.findViewById(R.id.annot);
                System.out.println("contextView: " + v);
                pw = new PopupWindow(v);
                pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                pw.setOutsideTouchable(true);
                pw.showAsDropDown(widget, offsetX, offsetY);
                currentPopup = pw;
            } else {
                pw.dismiss();
                if (currentPopup == pw) {
                    currentPopup = null;
                }
                pw = null;
            }
        }
//        @Override
//        public void updateDrawState(TextPaint ds) {
////            ds.setColor(ds.linkColor);
//            ds.setUnderlineText(true);
//        }
    }

    public ParaRecyclerViewAdapter(List<Para> paras, Activity context) {
        mValues = paras;
        this.context = context;
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

    class ActionModeCallbackImpl implements ActionMode.Callback {
        private int option3Id;
        TextView tv;

        ActionModeCallbackImpl(TextView tv) {
            this.tv = tv;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            System.out.println("onCreateActionMode");
//            MenuInflater inflater = mode.getMenuInflater();
//            inflater.inflate(R.menu.text_context, menu);

            MenuItem m = menu.add("Option 3");
            option3Id = m.getItemId();
            System.out.println("added MenuItem: " + option3Id);
            System.out.println("MenuItem Size: " + menu.size());
//            m.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
//
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    System.out.println("MenuItem clicked: " + item.getItemId());
//                    return true;
//                }
//            });

//            if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//                System.out.println("Type : " + mode.getType());
//            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            System.out.println("onPrepareActionMode");
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            System.out.println("onActionItemClicked");
            Context context = ParaRecyclerViewAdapter.this.context;
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.option1:
                    Toast.makeText(context, "Option 1", Toast.LENGTH_SHORT).show();
                    mode.getMenu().close();
//                        mode.finish();
                    return true;
                case R.id.option2:
                    Toast.makeText(context, "Option 2", Toast.LENGTH_SHORT).show();
                    mode.finish();
                    return true;
                default:
                    if (itemId == option3Id) {
                        CharSequence selected = "";
                        int start = tv.getSelectionStart();
                        if (start >= 0) {
                            int end = tv.getSelectionEnd();
                            if (end >= 0) {
                                selected = tv.getText().subSequence(start, end);
                            }
                        }
                        Toast.makeText(context, "Option 3: " + selected, Toast.LENGTH_SHORT).show();
//                        mode.getMenu().close();
                        mode.finish();
//                        ((Activity)context).closeOptionsMenu();
                        return true;

                    }
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            System.out.println("onDestroyActionMode");
//            mode.finish();

        }
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Para para = mValues.get(position);
        final TextView mContentView = holder.mContentView;
        String content = para.getContent();
        SpannableString ss = new SpannableString(para.getContent());
        String[] words = new String[]{"Valley", "Silicon", "pioneer", "Technology"};

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

        mContentView.setCustomSelectionActionModeCallback(new ActionModeCallbackImpl(mContentView));

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

    public void onDestroy() {
        if (currentPopup != null) {
            currentPopup.dismiss();
            currentPopup = null;
        }
    }
}