package wjy.yo.ereader.ui.text;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class ParaTransTextView extends AppCompatTextView {

    public ParaTransTextView(Context context) {
        super(context);
    }

    public ParaTransTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaTransTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
//            System.out.println("Selection: " + start + " <> " + end);
            float x = event.getX();
            float y = event.getY();
//            System.out.println("Event: " + action + " " + x + "," + y);
            int offset = getOffsetForPosition(x, y);
            if (offset >= 0) {
                //
            }
//            boolean clickResult = performClick();
//            System.out.println("clickResult: " + clickResult);
        }
//        return true;
        return super.onTouchEvent(event);
    }

}
