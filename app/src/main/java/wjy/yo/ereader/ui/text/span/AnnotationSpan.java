package wjy.yo.ereader.ui.text.span;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;
import wjy.yo.ereader.ui.text.TextAnnos;
import wjy.yo.ereader.util.Constants;
import wjy.yo.ereader.util.Constants.Annotations;

public class AnnotationSpan extends SemanticSpan {

    private Map<String, String> dataMap;

    private TextAnnos textAnnos;

    private List styleSpans;

    private NoteAfterSpan noteAfterSpan;

    public AnnotationSpan(ParaTextView textView, SpanLocation location,
                          Map<String, String> attributeMap, Editable output) {
        super(textView, location);

        dataMap = new ArrayMap<>();

        if (attributeMap == null) {
            return;
        }
        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            String attrName = entry.getKey();
            if (!attrName.startsWith("data-")) {
                continue;
            }
            String dataName = attrName.substring(5);
            String dataValue = entry.getValue();
            dataMap.put(dataName, dataValue);
        }

        if (dataMap.containsKey(Constants.Annotations.ATTR_NOTE)) {
            String nfCharacter = "注";
            output.append(nfCharacter);
            int end = location.getEnd();
            int newEnd = end + nfCharacter.length();
            location.setEnd(newEnd);
            SpanLocation noteAfterLocation = new SpanLocation(end, newEnd);
            noteAfterSpan = new NoteAfterSpan(textView, noteAfterLocation, output);
        }
    }

    public TextAnnos buildAnnos(AnnotationFamily af) {
        if (af == null) {
            return null;
        }
        Map<String, Anno> annosMap = af.getAnnosMap();
        if (annosMap == null) {
            return null;
        }
        TextAnnos textAnnos = new TextAnnos();

        for (Map.Entry<String, String> attr : dataMap.entrySet()) {
            String dataName = attr.getKey();
            String dataValue = attr.getValue();
            if (dataName.equals(Annotations.ATTR_PHRASE)) {
                TextAnnos.Phrase phrase = new TextAnnos.Phrase();
                phrase.setGroup(dataValue);
                //...
                textAnnos.setPhrase(phrase);
                textAnnos.pushAnnoLabel("Phrase ...");
            } else if (dataName.equals(Annotations.ATTR_MEANING_ID)
                    || dataName.equals(Annotations.ATTR_MEANING_TEXT)) {
                TextAnnos.SelectedMeaning sm = textAnnos.getSelectedMeaning();
                if (sm == null) {
                    sm = new TextAnnos.SelectedMeaning();
                    textAnnos.setSelectedMeaning(sm);
                }
                if (dataName.equals(Annotations.ATTR_MEANING_ID)) {
                    sm.setMid(dataValue);
                } else {
                    sm.setMeaning(dataValue);
                }
            } else if (dataName.equals(Annotations.ATTR_NOTE)) {
                textAnnos.setNote(dataValue);
            } else {
                Anno anno = af.findAnno(dataName, dataValue);
                if (anno != null) {
                    textAnnos.pushAnnoLabel(anno.getName());
                }
            }
        }

        this.textAnnos = textAnnos;
        return textAnnos;
    }

    public TextAnnos getTextAnnos() {
        return textAnnos;
    }

    protected List createStyleSpans() {
        List spans = new ArrayList();
        if (dataMap.get(Annotations.ATTR_PHRASE) != null) {
            spans.add(new PhraseStyle());
            spans.add(new AnnotationStyle());
        }
        if (dataMap.get(Annotations.ATTR_MEANING_ID) != null
                || dataMap.get(Annotations.ATTR_MEANING_TEXT) != null) {
            spans.add(new MeaningStyle());
        }
        if (spans.isEmpty()) {
            spans.add(new AnnotationStyle());
        }
        return spans;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (noteAfterSpan != null) {
            noteAfterSpan.setEnabled(enabled);
        }
    }

    protected void resetStyle(boolean addStyle) {

        Spannable sp = textView.getTextSpannable();
        if (addStyle) {
            if (styleSpans == null) {
                styleSpans = createStyleSpans();
            }
            int start = location.getStart();
            int end = location.getEnd();
            if (noteAfterSpan != null) {
                end -= noteAfterSpan.spanLength();
            }
            for (Object span : styleSpans) {
                sp.setSpan(span, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } else {
            removeSpan(styleSpans);
        }
    }

    static class AnnotationStyle extends UnderlineSpan {

    }

    static class PhraseStyle extends StyleSpan {

        PhraseStyle() {
            super(Typeface.ITALIC);
        }
    }

    static class MeaningStyle extends StyleSpan {

        MeaningStyle() {
            super(Typeface.BOLD);
        }
    }
}
