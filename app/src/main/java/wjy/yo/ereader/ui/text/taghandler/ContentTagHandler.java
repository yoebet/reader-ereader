package wjy.yo.ereader.ui.text.taghandler;

import android.text.Editable;
import android.text.Spanned;

import java.util.Map;

import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.span.AnnotationSpan;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.textview.ParaContentTextView;

public class ContentTagHandler extends ParaTagHandler {

    public ContentTagHandler(ParaContentTextView textView, Settings settings) {
        super(textView, settings);
    }

    protected void handleAnnotation(TagInfo tagInfo, Editable output) {

        SpanLocation location = tagInfo.getLocation();
        int start = location.getStart();
        int end = location.getEnd();

        Map<String, String> attributeMap = tagInfo.getAttributeMap();
        if (!AnnotationSpan.validate(attributeMap)) {
            return;
        }

        AnnotationSpan annotationSpan = new AnnotationSpan(textView, location, attributeMap);
        output.setSpan(annotationSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spansHolder.push(annotationSpan);

//        PopupWindowManager pwm = environment.getPopupWindowManager();
//        output.setSpan(new ForegroundColorSpan(Color.BLUE), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        output.setSpan(new ClickableWordSpan(pwm), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
