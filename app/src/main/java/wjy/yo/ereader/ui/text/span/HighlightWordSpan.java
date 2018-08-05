package wjy.yo.ereader.ui.text.span;

import android.content.res.Resources;
import android.text.style.ForegroundColorSpan;

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public class HighlightWordSpan extends ToggleStyleSpan {

    public HighlightWordSpan(ParaTextView textView, SpanLocation location) {
        super(textView, location);
    }

    protected Object createStyleSpan() {
        Resources res = textView.getResources();
        int color = res.getColor(R.color.text_highlight_word);
        return new HighlightWordStyle(color);
    }

    static class HighlightWordStyle extends ForegroundColorSpan {

        HighlightWordStyle(int color) {
            super(color);
        }
    }
}
