package wjy.yo.ereader.ui.text.span;

import android.text.Spannable;
import android.text.Spanned;
import android.text.style.UnderlineSpan;

import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public class SemanticSpan {

    protected ParaTextView textView;

    protected SpanLocation location;

    private boolean enabled;

    public SemanticSpan(ParaTextView textView, SpanLocation location) {
        this.textView = textView;
        this.location = location;
    }

    public SpanLocation getLocation() {
        return location;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        this.enabled = enabled;
        resetStyle(enabled);
    }

    protected Class styleSpanClass() {
        return UnderlineSpan.class;
    }

    protected Object newStyleSpan() {
        return new UnderlineSpan();
    }

    protected void resetStyle(boolean addStyle) {
        int start = location.getStart();
        int end = location.getEnd();

        Spannable sp = textView.getTextSpannable();
        Object[] spans = sp.getSpans(start, end, styleSpanClass());
        if (addStyle) {
            if (spans.length == 0) {
                Object styleSpan = newStyleSpan();
                sp.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            if (spans.length > 0) {
                for (Object span : spans) {
                    sp.removeSpan(span);
                }
            }
        }
    }

}
