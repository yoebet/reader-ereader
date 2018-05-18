package wjy.yo.ereader.ui.reader;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


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


    private String getTheWord(CharSequence cs, final int offset) {

        char c = cs.charAt(offset);
        if (!Character.isLetter(c)) {
            return null;
        }
        int length = cs.length();
        int start = offset;
        while (start > 0) {
            c = cs.charAt(start - 1);
            if (Character.isLetter(c)) {
                start--;
            } else {
                break;
            }
        }
        int end = offset;
        while (end + 1 < length) {
            c = cs.charAt(end + 1);
            if (Character.isLetter(c)) {
                end++;
            } else {
                break;
            }
        }
        if (start == end) {
            return null;
        }
        int wordLen = end - start + 1;
        if (wordLen < 3 || wordLen > 20) {
            return null;
        }
//        System.out.println(cs.getClass());
//        if (cs instanceof Spannable) {
//            Selection.setSelection((Spannable) cs, start, end + 1);
//        }
        return cs.subSequence(start, end + 1).toString();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
//            int start = getSelectionStart();
//            int end = getSelectionEnd();
//            if (start > end) {
//                int tmp = start;
//                start = end;
//                end = tmp;
//            }
            float x = event.getX();
            float y = event.getY();
//            System.out.println("Event: " + action + " " + x + "," + y);
//            System.out.println("Selection: " + start + " <> " + end);
            int offset = getOffsetForPosition(x, y);
            if (offset >= 0) {
                String word = getTheWord(getText(), offset);
                if (word != null) {
                    System.out.println("offset: " + offset + ", " + word);
                }
            }
//            performClick();
        }
//        return true;
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onTextContextMenuItem(int id) {
//        boolean result = super.onTextContextMenuItem(id);
//
        System.out.println("textContextMenuItem: " + id);
//        System.out.println("result: " + result);

        return false;
    }


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
        System.out.println("selection: " + selection);
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
