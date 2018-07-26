package wjy.yo.ereader.ui.text.taghandler;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import wjy.yo.ereader.ui.text.span.ClickableWordSpan;
import wjy.yo.ereader.ui.text.Environment;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.Settings;

public class ContentTagHandler extends ParaTagHandler {

    public ContentTagHandler(Environment environment, Settings settings) {
        super(environment, settings);
    }

    protected void doHandleTagEnd(String tag, int start, int end, Editable output) {
        PopupWindowManager pwm = environment.getPopupWindowManager();

        if (tag.equals("y-o")) {
            if (settings.isHandleAnnotations()) {
                output.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                output.setSpan(new ClickableWordSpan(pwm), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else if (tag.equals("s-st")) {
            output.setSpan(new Object(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                output.setSpan(new ClickableWordSpan(this.popupWindowManager), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
