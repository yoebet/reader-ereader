package wjy.yo.ereader.ui.text.taghandler;

import android.text.Editable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;

import org.xml.sax.Attributes;

import wjy.yo.ereader.ui.text.Environment;
import wjy.yo.ereader.ui.text.Settings;

public class TransTagHandler extends ParaTagHandler {

    public TransTagHandler(Environment environment, Settings settings) {
        super(environment, settings);
    }

    @Override
    public boolean handleTagOpen(String tag, Editable output, Attributes attributes) {
        if (!canHandle(tag)) {
            return false;
        }
        starts.put(tag, output.length());
        return true;
    }

    @Override
    protected void doHandleTagEnd(String tag, int start, int end, Editable output) {

        if (tag.equals("s-st")) {
            output.setSpan(new Object(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                output.setSpan(new ClickableWordSpan(this.popupWindowManager), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

}
