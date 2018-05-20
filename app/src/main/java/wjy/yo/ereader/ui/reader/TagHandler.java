package wjy.yo.ereader.ui.reader;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

public class TagHandler implements HtmlParser.TagHandler {

    private Map<String, Integer> starts = new HashMap<>();

    private PopupWindowManager pwm;

    public TagHandler(PopupWindowManager pwm) {
        this.pwm = pwm;
    }

    private boolean canHandle(String tag) {
        return tag.indexOf('-') > 0;
    }

    @Override
    public boolean handleTagOpen(String tag, Editable output, Attributes attributes) {
        if (!canHandle(tag)) {
            return false;
        }
        starts.put(tag, output.length());
        System.out.println("<" + tag + ">");
        for (int i = 0, n = attributes.getLength(); i < n; i++) {
            String localName = attributes.getLocalName(i);
            String value = attributes.getValue(i);
            System.out.println("    " + localName + ": " + value);
        }
        return true;
    }

    @Override
    public void handleTagEnd(String tag, Editable output) {
        if (!canHandle(tag)) {
            return;
        }
        Integer start = starts.remove(tag);
        if (start == null) {
            System.out.println("Missing Start: " + tag);
            return;
        }
        int end = output.length();
        if (start == end) {
            return;
        }

        if (tag.equals("y-o")) {
            output.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            output.setSpan(new ClickableWordSpan(pwm), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (tag.startsWith("sp-")) {
            output.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            output.setSpan(new ClickableWordSpan(pwm), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (tag.equals("s-st")) {
            output.setSpan(new BackgroundColorSpan(Color.LTGRAY), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                output.setSpan(new ClickableWordSpan(this.pwm), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }
}
