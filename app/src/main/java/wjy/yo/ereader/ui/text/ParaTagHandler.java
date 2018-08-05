package wjy.yo.ereader.ui.text;

import android.text.Editable;
import android.util.ArrayMap;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

import wjy.yo.ereader.ui.common.HtmlParser;
import wjy.yo.ereader.ui.text.span.AnnotationSpan;
import wjy.yo.ereader.ui.text.span.SentenceSpan;
import wjy.yo.ereader.ui.text.span.SemanticSpan;
import wjy.yo.ereader.ui.text.textview.ParaTextView;
import wjy.yo.ereader.util.Constants;

public class ParaTagHandler implements HtmlParser.TagHandler {

    protected ParaTextView textView;

    protected Settings settings;

    protected Map<String, TagInfo> tagsMap = new HashMap<>();

    protected SpansHolder<SemanticSpan> spansHolder;

    public ParaTagHandler(ParaTextView textView, Settings settings) {
        this.textView = textView;
        this.settings = settings;
        spansHolder = new SpansHolder<>();
    }

    public SpansHolder<SemanticSpan> getSpansHolder() {
        return spansHolder;
    }

    protected boolean canHandle(String tag) {
        return tag.indexOf('-') > 0;
    }

    @Override
    public boolean handleTagOpen(String tag, Editable output, Attributes attributes) {
        if (!canHandle(tag)) {
            return false;
        }

        int attrLen = attributes.getLength();
        Map<String, String> attributeMap = null;
        if (attrLen > 0) {
            attributeMap = new ArrayMap<>();
        }
//        System.out.println("<" + tag + ">");
        for (int i = 0; i < attrLen; i++) {
            String localName = attributes.getLocalName(i);
            String value = attributes.getValue(i);
            attributeMap.put(localName, value);
        }

        TagInfo tagInfo = new TagInfo(tag);
        tagInfo.setLocationStart(output.length());
        tagInfo.attributeMap = attributeMap;

        tagsMap.put(tag, tagInfo);

        return true;
    }

    @Override
    public void handleTagEnd(String tag, Editable output) {
        if (!canHandle(tag)) {
            return;
        }

        TagInfo tagInfo = tagsMap.remove(tag);
        if (tagInfo == null) {
            System.out.println("Missing Start: " + tag);
            return;
        }

        int end = output.length();
        SpanLocation location = tagInfo.location;
        int start = location.getStart();
        if (start == end) {
            return;
        }

        location.setEnd(end);
        Map<String, String> attributeMap = tagInfo.attributeMap;

        if (Constants.TEXT_TAG_SENTENCE.equals(tag)) {
            if (SentenceSpan.validate(attributeMap)) {
                SentenceSpan sentenceSpan = new SentenceSpan(textView, location, attributeMap);
                spansHolder.push(sentenceSpan);
            }
        } else if (Constants.TEXT_TAG_ANNOTATION.equals(tag)) {
            if (settings.isHandleAnnotations()) {
                AnnotationSpan annotationSpan = new AnnotationSpan(textView, location, attributeMap, output);
                spansHolder.push(annotationSpan);
            }
        }
    }


    static class TagInfo {

        String name;

        SpanLocation location;

        Map<String, String> attributeMap;

        TagInfo(String name) {
            this.name = name;
            this.location = new SpanLocation(-1, -1);
        }

        void setLocationStart(int start) {
            location.setStart(start);
        }

    }

}
