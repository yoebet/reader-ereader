package wjy.yo.ereader.ui.text.taghandler;

import android.text.Editable;

import org.xml.sax.Attributes;

import java.util.HashMap;
import java.util.Map;

import wjy.yo.ereader.ui.common.HtmlParser;
import wjy.yo.ereader.ui.text.Environment;
import wjy.yo.ereader.ui.text.Settings;

public abstract class ParaTagHandler implements HtmlParser.TagHandler {

    protected Map<String, Integer> starts = new HashMap<>();

    protected Environment environment;

    protected Settings settings;

    public ParaTagHandler(Environment environment, Settings settings) {
        this.environment = environment;
        this.settings = settings;
    }

    protected boolean canHandle(String tag) {
        return tag.indexOf('-') > 0;
    }

    @Override
    public boolean handleTagOpen(String tag, Editable output, Attributes attributes) {
        if (!canHandle(tag)) {
            return false;
        }
        starts.put(tag, output.length());
//        System.out.println("<" + tag + ">");
        for (int i = 0, n = attributes.getLength(); i < n; i++) {
            String localName = attributes.getLocalName(i);
            String value = attributes.getValue(i);
//            System.out.println("    " + localName + ": " + value);
        }
        return true;
    }

    protected abstract void doHandleTagEnd(String tag, int start, int end, Editable output);

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

        doHandleTagEnd(tag, start, end, output);
    }
}
