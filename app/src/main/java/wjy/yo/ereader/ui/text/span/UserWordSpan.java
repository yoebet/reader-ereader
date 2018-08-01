package wjy.yo.ereader.ui.text.span;

import android.content.res.Resources;
import android.text.style.ForegroundColorSpan;

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public class UserWordSpan extends SemanticSpan {

    private int familiarity;

    public UserWordSpan(ParaTextView textView, SpanLocation location) {
        super(textView, location);
    }

    public int getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(int familiarity) {
        this.familiarity = familiarity;
    }

    @Override
    protected Class styleSpanClass() {
        return ForegroundColorSpan.class;
    }

    @Override
    protected Object newStyleSpan() {
        Resources res = textView.getResources();
        int colorRes = R.color.word_familiarity_1;
        if (familiarity == 2) {
            colorRes = R.color.word_familiarity_2;
        }
        int color = res.getColor(colorRes);
        return new ForegroundColorSpan(color);
    }
}
