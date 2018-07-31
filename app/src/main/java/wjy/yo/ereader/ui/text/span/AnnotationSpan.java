package wjy.yo.ereader.ui.text.span;

import android.text.style.UnderlineSpan;

import java.util.Map;

import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

public class AnnotationSpan extends SemanticSpan {

    private String dataName;

    private String dataValue;

    // annotations


    public AnnotationSpan(ParaTextView textView, SpanLocation location, Map<String, String> attributeMap) {
        super(textView, location);

        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            String attrName = entry.getKey();
            if (!attrName.startsWith("data-")) {
                continue;
            }
            this.dataName = attrName.substring(5);
            this.dataValue = entry.getValue();
        }
    }

    public static boolean validate(Map<String, String> attributeMap) {
        return attributeMap != null;
    }

    public String getDataName() {
        return dataName;
    }

    public String getDataValue() {
        return dataValue;
    }

    @Override
    protected Class styleSpanClass() {
        return UnderlineSpan.class;
    }

    @Override
    protected Object newStyleSpan() {
        return new UnderlineSpan();
    }
}
