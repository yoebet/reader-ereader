package wjy.yo.ereader.ui.text.span;

import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

abstract class ToggleStyleSpan extends SemanticSpan {

    private Object styleSpan;

    ToggleStyleSpan(ParaTextView textView, SpanLocation location) {
        super(textView, location);
    }

    protected abstract Object createStyleSpan();

    protected void resetStyle(boolean addStyle) {
        if (addStyle) {
            if (styleSpan == null) {
                styleSpan = createStyleSpan();
            }
            setSpan(styleSpan);
        } else {
            removeSpan(styleSpan);
        }
    }

}
