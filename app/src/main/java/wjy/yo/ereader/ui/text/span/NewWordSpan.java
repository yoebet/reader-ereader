package wjy.yo.ereader.ui.text.span;

import android.content.res.Resources;
import android.text.style.ForegroundColorSpan;

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public class NewWordSpan extends ToggleStyleSpan {

    private boolean inUserWord;

    private int familiarity;

    public NewWordSpan(ParaTextView textView, SpanLocation location) {
        super(textView, location);
    }

    public boolean isInUserWord() {
        return inUserWord;
    }

    public void setInUserWord(boolean inUserWord) {
        this.inUserWord = inUserWord;
    }

    public int getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(int familiarity) {
        this.familiarity = familiarity;
    }

    @Override
    protected Object createStyleSpan() {
        Resources res = textView.getResources();
        int colorRes = R.color.word_out_of_band;
        if (inUserWord) {
            if (familiarity == 1) {
                colorRes = R.color.word_familiarity_1;
            } else if (familiarity == 2) {
                colorRes = R.color.word_familiarity_2;
            }
        }
        int color = res.getColor(colorRes);
        return new NewWordStyle(color);
    }


    static class NewWordStyle extends ForegroundColorSpan {

        NewWordStyle(int color) {
            super(color);
        }
    }
}
