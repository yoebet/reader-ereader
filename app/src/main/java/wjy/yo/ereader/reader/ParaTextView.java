package wjy.yo.ereader.reader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class ParaTextView extends AppCompatTextView {
    public ParaTextView(Context context) {
        super(context);
    }

    public ParaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean result = super.onTextContextMenuItem(id);

        System.out.println("textContextMenuItem: " + id);
        System.out.println("result: " + result);

        return (result);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        System.out.println("onTouchEvent");
//        return super.onTouchEvent(event);
//    }

//    @Override
//    public void setOnContextClickListener(@Nullable OnContextClickListener l) {
//        super.setOnContextClickListener(l);
//    }


//    @Override
//    public boolean performClick() {
//        return super.performClick();
//    }


    @Override
    public void createContextMenu(ContextMenu menu) {
        System.out.println("createContextMenu, menu.size: " + menu.size());
        CharSequence selected = "";
        int start = getSelectionStart();
        if (start >= 0) {
            int end = getSelectionEnd();
            if (end >= 0) {
                selected = getText().subSequence(start, end);
            }
        }
        String selection = selected.toString();
        if (selection.indexOf(' ') >= 0) {
            super.createContextMenu(menu);
        } else {
            Toast.makeText(getContext(), "Option zz: " + selected, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {

        System.out.println("onCreateContextMenu, menu.size: " + menu.size());

    }

}
