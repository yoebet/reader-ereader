package wjy.yo.ereader.ui.text.span;

import android.content.res.Resources;
import android.text.style.BackgroundColorSpan;

import java.util.Map;

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public class SentenceSpan extends SemanticSpan {

    private final static String SID_ATTR_NAME = "data-sid";

    private String sid;

    public SentenceSpan(ParaTextView textView, SpanLocation location, Map<String, String> attributeMap) {
        super(textView, location);
        this.sid = attributeMap.get(SID_ATTR_NAME);
    }

    public static boolean validate(Map<String, String> attributeMap) {
        return attributeMap != null && attributeMap.get(SID_ATTR_NAME) != null;
    }

    public String getSid() {
        return sid;
    }

    public boolean isHighlight() {
        return isEnabled();
    }

    public void setHighlight(boolean highlight) {
        setEnabled(highlight);
    }

    @Override
    protected Class styleSpanClass() {
        return BackgroundColorSpan.class;
    }

    @Override
    protected Object newStyleSpan() {
        Resources res = textView.getResources();
        int color = res.getColor(R.color.text_highlight_sentence);
        return new BackgroundColorSpan(color);
    }
}
