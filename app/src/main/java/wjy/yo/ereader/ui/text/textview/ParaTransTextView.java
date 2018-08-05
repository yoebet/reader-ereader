package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import wjy.yo.ereader.ui.text.TextSetting;
import wjy.yo.ereader.ui.text.span.SentenceSpan;


public class ParaTransTextView extends ParaTextView {

    private ParaContentTextView peer;

    public ParaTransTextView(Context context) {
        super(context);
    }

    public ParaTransTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaTransTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPeer(ParaContentTextView peer) {
        this.peer = peer;
    }


    void clearCurrentHighlight() {
        TextStatusHolder tsh = settings.getTextStatusHolder();
        if (tsh.transCurrentHighlight != null) {
            tsh.transCurrentHighlight.resetSpans(SentenceSpan.class, false);
            tsh.transCurrentHighlight = null;
        }
    }

    @Override
    protected String highlightTheSentence(int start, int end) {
        clearCurrentHighlight();
        String sid = super.highlightTheSentence(start, end);
        if (sid != null) {
            TextStatusHolder tsh = settings.getTextStatusHolder();
            tsh.transCurrentHighlight = this;
        }
        return sid;
    }

    @Override
    protected void highlightTheSentence(String sid) {
        clearCurrentHighlight();
        super.highlightTheSentence(sid);
        TextStatusHolder tsh = settings.getTextStatusHolder();
        tsh.transCurrentHighlight = this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            int offset = getOffsetForPosition(x, y);
            if (offset >= 0) {
                TextSetting textSetting = settings.getTextSetting();
                if (textSetting.isHighlightSentence()) {
                    String sid = highlightTheSentence(offset, offset);
                    if (sid != null && peer != null) {
                        peer.highlightTheSentence(sid);
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

}
