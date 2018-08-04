package wjy.yo.ereader.ui.text.span;

import android.text.style.UnderlineSpan;
import android.util.ArrayMap;

import java.util.Map;

import wjy.yo.ereader.entity.anno.Anno;
import wjy.yo.ereader.entityvo.anno.AnnotationFamily;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.textview.ParaTextView;
import wjy.yo.ereader.ui.text.TextAnnos;

public class AnnotationSpan extends SemanticSpan {

    private Map<String, String> dataMap;

    private TextAnnos textAnnos;

    public AnnotationSpan(ParaTextView textView, SpanLocation location, Map<String, String> attributeMap) {
        super(textView, location);

        dataMap = new ArrayMap<>();

        for (Map.Entry<String, String> entry : attributeMap.entrySet()) {
            String attrName = entry.getKey();
            if (!attrName.startsWith("data-")) {
                continue;
            }
            String dataName = attrName.substring(5);
            String dataValue = entry.getValue();
            dataMap.put(dataName, dataValue);
        }
    }

    public static boolean validate(Map<String, String> attributeMap) {
        return attributeMap != null;
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
            if (dataName.equals("phra")) {
                TextAnnos.Phrase phrase = new TextAnnos.Phrase();
                phrase.setGroup(dataValue);
                //...
                textAnnos.setPhrase(phrase);
                textAnnos.pushAnnoLabel("Phrase ...");
            } else if (dataName.equals("mid") || dataName.equals("meaning")) {
                TextAnnos.SelectedMeaning sm = textAnnos.getSelectedMeaning();
                if (sm == null) {
                    sm = new TextAnnos.SelectedMeaning();
                    textAnnos.setSelectedMeaning(sm);
                }
                if (dataName.equals("mid")) {
                    sm.setMid(dataValue);
                } else {
                    sm.setMeaning(dataValue);
                }
            } else if (dataName.equals("note")) {
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

    public void setTextAnnos(TextAnnos textAnnos) {
        this.textAnnos = textAnnos;
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
