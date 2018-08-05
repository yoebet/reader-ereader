package wjy.yo.ereader.ui.text.span;

import android.text.Spannable;
import android.text.Spanned;

import java.util.List;

import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public abstract class SemanticSpan {

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

    protected void setSpan(Object style) {
        Spannable sp = textView.getTextSpannable();
        int start = location.getStart();
        int end = location.getEnd();
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        if (style instanceof List) {
            List styles = (List) style;
            for (Object st : styles) {
                sp.setSpan(st, start, end, flag);
            }
        } else {
            sp.setSpan(style, start, end, flag);
        }
    }

    protected void removeSpan(Object style) {
        if (style == null) {
            return;
        }
        Spannable sp = textView.getTextSpannable();
        if (style instanceof List) {
            List styles = (List) style;
            for (Object st : styles) {
                sp.removeSpan(st);
            }
        } else {
            sp.removeSpan(style);
        }
    }

    protected abstract void resetStyle(boolean addStyle);

    public boolean spanContains(int offset) {
        return location.contains(offset);
    }

    public int spanLength() {
        return location.length();
    }

}
