package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.AttributeSet;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.common.HtmlParser;
import wjy.yo.ereader.ui.text.Environment;
import wjy.yo.ereader.ui.text.MarkerSettings;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.SpansHolder;
import wjy.yo.ereader.ui.text.span.AnnotationSpan;
import wjy.yo.ereader.ui.text.span.HighlightWordSpan;
import wjy.yo.ereader.ui.text.span.SemanticSpan;
import wjy.yo.ereader.ui.text.span.SentenceSpan;
import wjy.yo.ereader.ui.text.taghandler.ParaTagHandler;
import wjy.yo.ereader.util.Utils;


public abstract class ParaTextView extends AppCompatTextView {

    protected String rawText;

    protected boolean textSetted;

    protected Environment environment;

    protected Settings settings;

    protected SpansHolder<SemanticSpan> spansHolder;

    public ParaTextView(Context context) {
        super(context);
    }

    public ParaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isTextSetted() {
        return textSetted;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }


    public Spannable getTextSpannable() {
        CharSequence cs = getText();
        if (cs instanceof Spannable) {
            return (Spannable) cs;
        }
        Spannable sp = new SpannableStringBuilder(cs);
        setText(sp);
        return sp;
    }

    protected void setSelection(int start, int stop) {
        Spannable sp = getTextSpannable();
        Selection.setSelection(sp, start, stop);
    }

    protected void removeSelection() {
        Spannable sp = getTextSpannable();
        Selection.removeSelection(sp);
    }

    protected Para getPara() {
        return (Para) getTag();
    }


    protected abstract ParaTagHandler newTagHandler();

    private String commonPrefix(String[] strings) {
        String first = strings[0];
        int commonLength = first.length();
        for (int i = 1; i < strings.length; ++i) {
            String si = strings[i];
            commonLength = Math.min(commonLength, si.length());
            for (int j = 0; j < commonLength; ++j) {
                if (si.charAt(j) != first.charAt(j)) {
                    commonLength = j;
                    break;
                }
            }
        }
        return first.substring(0, commonLength);
    }

    public void highlightWords(List<String> words) {
//        System.out.println("words " + words);

        if (words == null || words.size() == 0) {
            return;
        }

        String[] wordsArray = words.toArray(new String[words.size()]);

        String prefix = commonPrefix(wordsArray);
        String patternString;
//        System.out.println("prefix " + prefix);

        int prefixLength = prefix.length();
        if (prefixLength == 0) {
            patternString = Utils.join(wordsArray, "|");
        } else {
            StringBuilder wsb = new StringBuilder();
            int wc = 0;
            for (String word : wordsArray) {
                word = word.substring(prefixLength);
                if (wc > 0) {
                    wsb.append("|");
                }
                wsb.append(word);
                wc++;
            }
            StringBuilder psb = new StringBuilder(prefix);
            if (wsb.length() > 0) {
                psb.append("(");
                psb.append(wsb);
                psb.append(")");
            }
            patternString = psb.toString();
        }

        Pattern pattern = Pattern.compile("\\b" + patternString + "\\b", Pattern.CASE_INSENSITIVE);
//        System.out.println("pattern " + pattern.pattern());

        String text = getText().toString();
        Matcher matcher = pattern.matcher(text);

        List<HighlightWordSpan> wordSpans = spansHolder.getSpansForPush(HighlightWordSpan.class);
        List<SentenceSpan> sentenceSpans = spansHolder.getSpans(SentenceSpan.class);

        clearSentenceHighlight();

        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (start == end) {
                System.out.println("start == end " + start);
                continue;
            }

//            System.out.println("found " + text.substring(start, end));
            SpanLocation location = new SpanLocation(start, end);

            HighlightWordSpan wordSpan = new HighlightWordSpan(this, location);
            wordSpans.add(wordSpan);

            if (sentenceSpans != null) {
                for (SentenceSpan sentenceSpan : sentenceSpans) {
                    SpanLocation sl = sentenceSpan.getLocation();
                    boolean highlight = sl.contains(start, end);
                    if (highlight) {
                        sentenceSpan.setHighlight(true);
//                        System.out.println("highlight sentence: " + sentenceSpan.getSid());
                    }
                }
            }
        }

        for (HighlightWordSpan wordSpan : wordSpans) {
            wordSpan.setEnabled(true);
        }
    }

    public List<String> getHighlightSentences() {
        List<SentenceSpan> spans = spansHolder.getSpans(SentenceSpan.class);
        if (spans == null) {
            return new ArrayList<>();
        }
        return Stream.of(spans)
                .filter(SentenceSpan::isHighlight)
                .map(SentenceSpan::getSid)
                .toList();
    }

    public void highlightSentences(List<String> sids) {
        if (sids == null) {
            resetSpans(SentenceSpan.class, false);
            return;
        }
        List<SentenceSpan> sentenceSpans = spansHolder.getSpans(SentenceSpan.class);
        if (sentenceSpans == null) {
            return;
        }
        for (SentenceSpan sentenceSpan : sentenceSpans) {
            boolean hl = sids.contains(sentenceSpan.getSid());
            sentenceSpan.setHighlight(hl);
        }
    }

    protected void resetSpans(Class clazz, boolean enable) {
        List<SemanticSpan> spans = spansHolder.getSpans(clazz);
        if (spans != null) {
            for (SemanticSpan span : spans) {
                span.setEnabled(enable);
            }
        }
    }

    protected void clearSentenceHighlight() {
        resetSpans(SentenceSpan.class, false);
    }

    protected void clearWordHighlight() {
        List<HighlightWordSpan> spans = spansHolder.removeSpans(HighlightWordSpan.class);
        if (spans != null) {
            for (HighlightWordSpan span : spans) {
                span.destroy();
            }
        }
    }

    protected void resetSpanStates() {
        MarkerSettings ms = settings.getMarkerSettings();
        if (ms == null) {
            return;
        }

        Map<Class<? extends SemanticSpan>, List<? extends SemanticSpan>> spansMap = spansHolder.getSpansMap();
        for (Map.Entry typeSpans : spansMap.entrySet()) {
            Class type = (Class) typeSpans.getKey();
            List<SemanticSpan> spans = (List) typeSpans.getValue();
            if (type == AnnotationSpan.class) {
                boolean mark = ms.getMarkAnnotations() == MarkerSettings.YES;
                for (SemanticSpan ss : spans) {
                    ss.setEnabled(mark);
                }
            }
        }
    }

    public void setRawText(String content) {
        if (content == null) {
            content = "";
        }
        this.rawText = content;

        if (content.indexOf('<') == -1) {
            Spannable sp = new SpannableString(content);
            setText(sp);
            spansHolder = new SpansHolder<>();
            return;
        }

        ParaTagHandler th = newTagHandler();
        Spanned spanned = HtmlParser.buildSpannedText(content, th);
        setText(spanned);

        textSetted = true;

        spansHolder = th.getSpansHolder();
        resetSpanStates();
    }
}
