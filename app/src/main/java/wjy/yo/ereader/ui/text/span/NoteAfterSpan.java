package wjy.yo.ereader.ui.text.span;

import android.content.res.Resources;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;

import java.util.ArrayList;
import java.util.List;

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

class NoteAfterSpan extends SwitchStyleSpan {

    NoteAfterSpan(ParaTextView textView, SpanLocation location, Editable output) {
        super(textView, location);
        disabledStyle = new DummyReplacementSpan();
        output.setSpan(disabledStyle, location.getStart(), location.getEnd(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    protected Object createDisabledStyleSpan() {
        return new DummyReplacementSpan();
    }

    @Override
    protected Object createEnabledStyleSpan() {
        Resources res = textView.getResources();
        int color = res.getColor(R.color.anno_note_after);
        List styles = new ArrayList();
        styles.add(new SuperscriptSpan());
        styles.add(new ForegroundColorSpan(color));
        styles.add(new RelativeSizeSpan(0.7f));
        return styles;
    }

}
