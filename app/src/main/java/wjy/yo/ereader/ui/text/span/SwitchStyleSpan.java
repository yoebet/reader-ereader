package wjy.yo.ereader.ui.text.span;

import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

abstract class SwitchStyleSpan extends SemanticSpan {

    protected Object disabledStyle;

    protected Object enabledStyle;

    SwitchStyleSpan(ParaTextView textView, SpanLocation location) {
        super(textView, location);
    }

    protected abstract Object createDisabledStyleSpan();

    protected abstract Object createEnabledStyleSpan();

    @Override
    protected void resetStyle(boolean addStyle) {
        if (addStyle) {
            if (enabledStyle == null) {
                enabledStyle = createEnabledStyleSpan();
            }
            setSpan(enabledStyle);
            removeSpan(disabledStyle);
        } else {
            if (disabledStyle == null) {
                disabledStyle = createDisabledStyleSpan();
            }
            setSpan(disabledStyle);
            removeSpan(enabledStyle);
        }
    }

}
