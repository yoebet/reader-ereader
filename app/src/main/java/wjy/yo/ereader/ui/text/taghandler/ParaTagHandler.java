package wjy.yo.ereader.ui.text.taghandler;

import android.text.Editable;
import android.text.Spanned;
import android.util.ArrayMap;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

import wjy.yo.ereader.ui.common.HtmlParser;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.SpansHolder;
import wjy.yo.ereader.ui.text.span.SentenceSpan;
import wjy.yo.ereader.ui.text.span.SemanticSpan;
import wjy.yo.ereader.ui.text.textview.ParaTextView;

import static wjy.yo.ereader.util.Constants.TEXT_TAG_NAME_ANNOTATION;
import static wjy.yo.ereader.util.Constants.TEXT_TAG_NAME_SENTENCE;

public abstract class ParaTagHandler implements HtmlParser.TagHandler {

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
        tagInfo.setAttributeMap(attributeMap);

        tagsMap.put(tag, tagInfo);

        return true;
    }

    protected void handleAnnotation(TagInfo tagInfo, Editable output) {

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
        SpanLocation location = tagInfo.getLocation();
        int start = location.getStart();
        if (start == end) {
            return;
        }

        location.setEnd(end);
        Map<String, String> attributeMap = tagInfo.getAttributeMap();

        if (TEXT_TAG_NAME_SENTENCE.equals(tag)) {
            if (SentenceSpan.validate(attributeMap)) {
                SentenceSpan sentenceSpan = new SentenceSpan(textView, location, attributeMap);
//                output.setSpan(sentenceSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                spansHolder.push(sentenceSpan);
            }
        } else if (TEXT_TAG_NAME_ANNOTATION.equals(tag)) {
            if (settings.isHandleAnnotations()) {
                handleAnnotation(tagInfo, output);
            }
        }
    }
}
