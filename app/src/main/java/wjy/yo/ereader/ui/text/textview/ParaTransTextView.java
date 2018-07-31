package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import wjy.yo.ereader.ui.text.OnTouchBehavior;
import wjy.yo.ereader.ui.text.taghandler.ParaTagHandler;
import wjy.yo.ereader.ui.text.taghandler.TransTagHandler;


public class ParaTransTextView extends ParaTextView {

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
        OnTouchBehavior onTouchBehavior = settings.getOnTouchBehavior();
        if (onTouchBehavior == null) {
            return super.onTouchEvent(event);
        }

        boolean handled = false;
        if (onTouchBehavior.isDefaultBehaviorFirst()) {
            handled = super.onTouchEvent(event);
        }
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

//            if (environment.getDictAgent() != null &&
//                    onTouchBehavior.isShowDict() &&
//                    onTouchBehavior.selectTargetContains(OnTouchBehavior.SELECT_TARGET_WORD)) {
//            }
            if (onTouchBehavior.isPerformClick()) {
                boolean clickResult = performClick();
                System.out.println("clickResult: " + clickResult);
//                handled = handled || clickResult;
            }
        }
        if (!onTouchBehavior.isDefaultBehaviorFirst()) {
            if (onTouchBehavior.isDefaultBehaviorAnyway() || !handled) {
                return super.onTouchEvent(event);
            }
        }
        return true;
    }


    protected ParaTagHandler newTagHandler() {
        return new TransTagHandler(this, settings);
    }

}
