package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.databinding.Observable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.PopupWindow;

import com.annimon.stream.Stream;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.BR;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.VocabularyService.UserVocabularyMap;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.TextAnnos;
import wjy.yo.ereader.ui.text.TextSetting;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.span.AnnotationSpan;
import wjy.yo.ereader.ui.text.span.NewWordSpan;
import wjy.yo.ereader.ui.text.taghandler.ContentTagHandler;
import wjy.yo.ereader.ui.text.taghandler.ParaTagHandler;
import wjy.yo.ereader.util.Action;
import wjy.yo.ereader.util.Consumer;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.util.Offset;
import wjy.yo.ereader.util.ViewUtils;
import wjy.yo.ereader.util.WordAndPosition;
import wjy.yo.ereader.vo.WordContext;


public class ParaContentTextView extends ParaTextView {

    private ParaTransTextView peer;

    private Disposable uvmDisp;

    private Disposable uwcDisp;

    private boolean newWordsBuilt;

    private UserVocabularyMap userVocabularyMap = null;

    private static Pattern wordPattern = Pattern.compile("[a-zA-Z]+", Pattern.CASE_INSENSITIVE);

    private static Pattern upperLetterPattern = Pattern.compile("[A-Z]");

    private static Pattern nstPattern = Pattern.compile("n[’']t");

    public ParaContentTextView(Context context) {
        super(context);
    }

    public ParaContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPeer(ParaTransTextView peer) {
        this.peer = peer;
    }

    @Override
    public void setSettings(Settings settings) {
        super.setSettings(settings);

        TextSetting textSetting = settings.getTextSetting();
        if (settings.isHandleAnnotations() || settings.isHandleNewWords()) {
            textSetting.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    TextSetting ts = settings.getTextSetting();
                    if (propertyId == BR.showAnnotations) {
                        if (!settings.isHandleAnnotations()) {
                            return;
                        }
                        resetSpans(AnnotationSpan.class, ts.isShowAnnotations());
                        return;
                    }
                    if (propertyId == BR.markNewWords) {
                        if (!settings.isHandleNewWords()) {
                            return;
                        }
                        boolean mark = ts.isMarkNewWords();
                        if (mark && !newWordsBuilt) {
                            tryBuildNewWords();
                        } else {
                            resetSpans(NewWordSpan.class, mark);
                        }
                    }
                }
            });
        }
        TextStatusHolder tsh = settings.getTextStatusHolder();
        if (tsh.textSettingChangedCallback == null) {
            tsh.textSettingChangedCallback = new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    TextSetting ts = settings.getTextSetting();
                    if (propertyId == BR.highlightSentence) {
                        if (!ts.isHighlightSentence()) {
                            clearCurrentHighlight();
                            ParaTransTextView.clearCurrentHighlight();
                        }
                    }
                }
            };
            textSetting.addOnPropertyChangedCallback(tsh.textSettingChangedCallback);
        }
    }

    void clearCurrentHighlight() {
        TextStatusHolder tsh = settings.getTextStatusHolder();
        if (tsh.currentHighlight != null) {
            tsh.currentHighlight.clearSentenceHighlight();
            tsh.currentHighlight = null;
        }
    }

    @Override
    protected String highlightTheSentence(int start, int end) {
        clearCurrentHighlight();
        String sid = super.highlightTheSentence(start, end);
        if (sid != null) {
            TextStatusHolder tsh = settings.getTextStatusHolder();
            tsh.currentHighlight = this;
        }
        return sid;
    }

    @Override
    protected void highlightTheSentence(String sid) {
        clearCurrentHighlight();
        super.highlightTheSentence(sid);
        TextStatusHolder tsh = settings.getTextStatusHolder();
        tsh.currentHighlight = this;
    }

    private AnnotationSpan findAnnotationSpan(int offset) {

        List<AnnotationSpan> annoSpans = spansHolder.getSpans(AnnotationSpan.class);
        return Stream.of(annoSpans)
                .filter(span -> span.isEnabled() && span.spanContains(offset))
                .min((s1, s2) -> s1.spanLength() - s2.spanLength())
                .orElse(null);
    }

    private boolean checkAnnosPopup(int offset) {

        if (!settings.isHandleAnnotations()) {
            return false;
        }

        AnnotationSpan annoSpan = findAnnotationSpan(offset);
        if (annoSpan == null) {
            return false;
        }

        TextAnnos textAnnos = annoSpan.getTextAnnos();
        if (textAnnos == null) {
            textAnnos = annoSpan.buildAnnos(settings.getAnnotationFamily());
            if (textAnnos == null || textAnnos.isEmpty()) {
                return false;
            }
        }

        SpanLocation location = annoSpan.getLocation();

        Offset o = ViewUtils.calculateOffset(this, location.getStart(), location.getEnd());
        PopupWindowManager pwm = settings.getPopupWindowManager();

        Consumer<PopupWindow> onPopup = (PopupWindow pw) -> setSelection(location);
        PopupWindow.OnDismissListener onDismiss = this::removeSelection;

        DictAgent dictAgent = settings.getDictAgent();
        dictAgent.requestAnnosPopup(textAnnos,
                this,
                o,
                pwm,
                onPopup,
                onDismiss);

        return true;
    }

    private void handleTouchUp(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int offset = getOffsetForPosition(x, y);
        if (offset < 0) {
            return;
        }

        TextSetting textSetting = settings.getTextSetting();

        if (textSetting.isHighlightSentence()) {
            String sid = highlightTheSentence(offset, offset);
            if (sid != null && peer != null) {
                peer.highlightTheSentence(sid);
            }
        }

        if (textSetting.isShowAnnotations()) {
            boolean annosPopup = checkAnnosPopup(offset);
            if (annosPopup) {
                return;
            }
        }

        WordAndPosition wp = ViewUtils.getTheWord(getText(), offset);
        if (wp == null) {
            return;
        }
        String word = wp.word;
        int start = wp.start;
        int end = wp.stop;

        if (textSetting.isLookupDict()) {
            DictAgent dictAgent = settings.getDictAgent();
            WordContext wordContext = null;
            Para para = getPara();
            if (para != null) {
                wordContext = para.getWordContext();
            }
            int dictMode = settings.getDictMode();
            if (dictMode == Settings.DICT_MODE_BOTTOM_SHEET) {

                Action onOpen = () -> setSelection(start, end);
                Action onClose = this::removeSelection;

                dictAgent.requestDict(word, wordContext, onOpen, onClose);
            } else if (dictMode == Settings.DICT_MODE_SIMPLE_POPUP) {

                Consumer<PopupWindow> onPopup = (PopupWindow pw) -> setSelection(start, end);
                PopupWindow.OnDismissListener onDismiss = this::removeSelection;

                Offset o = ViewUtils.calculateOffset(this, start, end);
                PopupWindowManager pwm = settings.getPopupWindowManager();

                dictAgent.requestDictPopup(word,
                        wordContext,
                        this,
                        o,
                        pwm,
                        onPopup,
                        onDismiss);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            TextSetting ts = settings.getTextSetting();
            if (ts.isLookupDict() || ts.isShowAnnotations() || ts.isHighlightSentence()) {
                handleTouchUp(event);
            }
        }
        return super.onTouchEvent(event);
    }

//    @Override
//    public boolean onTextContextMenuItem(int id) {
//        return super.onTextContextMenuItem(id);
//    }

    protected ParaTagHandler newTagHandler() {
        return new ContentTagHandler(this, settings);
    }

    private void doBuildNewWords() {
//        System.out.println("doBuildNewWords: " + getPara().getSeq());

        destroySpans(NewWordSpan.class);
        newWordsBuilt = true;

        TextSetting ts = settings.getTextSetting();
        boolean enabled = ts.isMarkNewWords();

        String text = getText().toString();
        int textLen = text.length();

        List<NewWordSpan> spans = spansHolder.getSpansForPush(NewWordSpan.class);
        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (end - start <= 2) {
                continue;
            }

            String word = matcher.group();
            Object codeOrUW = userVocabularyMap.get(word);

            if (codeOrUW instanceof String) {
                // in base vocabulary
                continue;
            }

            if (codeOrUW == null) {
                if (upperLetterPattern.matcher(word).find()) {
                    continue;
                }
                if (end + 2 <= textLen) {
                    // don't, wasn't, couldn't, haven't, ...
                    String c3 = text.substring(end - 1, end + 2);
                    if (nstPattern.matcher(c3).matches()) {
                        continue;
                    }
                }
                if (end < textLen && text.charAt(end) == '-') {
                    continue;
                }
                if (start > 0 && text.charAt(start - 1) == '-') {
                    continue;
                }
            }

            NewWordSpan span = new NewWordSpan(this, new SpanLocation(start, end));

            if (codeOrUW == null) {
                span.setInUserWord(false);
            } else if (codeOrUW instanceof UserWord) {
                UserWord uw = (UserWord) codeOrUW;
                int familiarity = uw.getFamiliarity();
                if (familiarity == UserWord.FamiliarityHighest) {
                    continue;
                }
                span.setInUserWord(true);
                span.setFamiliarity(familiarity);
            }
            span.setEnabled(enabled);
            spans.add(span);
        }
    }

    private void observeUserWordChange() {

        if (uwcDisp != null && !uwcDisp.isDisposed()) {
            return;
        }
        UserWordService userWordService = settings.getUserWordService();
        if (userWordService == null) {
            return;
        }
        uwcDisp = userWordService.observeUserWordChange()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (String word) -> {
//                            System.out.println("receive change: " + word);
                            if (!newWordsBuilt) {
                                return;
                            }

                            doBuildNewWords();
                        },
                        ExceptionHandlers::handle);
    }

    private void tryBuildNewWords() {

        if (newWordsBuilt) {
            return;
        }
        if (userVocabularyMap == null ||
                userVocabularyMap == UserVocabularyMap.InvalidMap) {
            return;
        }

        TextSetting ts = settings.getTextSetting();
        if (!ts.isMarkNewWords()) {
            return;
        }

        doBuildNewWords();
        observeUserWordChange();
    }

    private void processNewWords() {
        if (uvmDisp != null && !uvmDisp.isDisposed()) {
            newWordsBuilt = false;
            tryBuildNewWords();
            return;
        }
        VocabularyService vocabularyService = settings.getVocabularyService();
        Flowable<UserVocabularyMap> uvm = vocabularyService.getUserVocabularyMap();
        uvmDisp = uvm
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userVocabularyMap -> {
                    this.userVocabularyMap = userVocabularyMap;
                    newWordsBuilt = false;
                    tryBuildNewWords();
                }, ExceptionHandlers::handle);
    }

    @Override
    public void setRawText(String content) {
        super.setRawText(content);

        if (settings.isHandleNewWords()) {
            processNewWords();
        }
    }
}
