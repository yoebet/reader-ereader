package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.databinding.Observable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.widget.PopupWindow;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.BR;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.VocabularyService.UserVocabularyMap;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.SpanLocation;
import wjy.yo.ereader.ui.text.TextSetting;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.span.AnnotationSpan;
import wjy.yo.ereader.ui.text.span.NewWordSpan;
import wjy.yo.ereader.ui.text.taghandler.ContentTagHandler;
import wjy.yo.ereader.ui.text.taghandler.ParaTagHandler;
import wjy.yo.ereader.util.Action;
import wjy.yo.ereader.util.Consumer;
import wjy.yo.ereader.util.Offset;
import wjy.yo.ereader.util.Utils;
import wjy.yo.ereader.util.ViewUtils;
import wjy.yo.ereader.util.WordAndPosition;
import wjy.yo.ereader.vo.WordContext;


public class ParaContentTextView extends ParaTextView {

    private ParaTransTextView peer;

    private Disposable uvmDisp;

    private Set<String> wordSet;

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


    private void handleTouchUp(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();
        int offset = getOffsetForPosition(x, y);
        if (offset >= 0) {
            WordAndPosition wp = ViewUtils.getTheWord(getText(), offset);
            if (wp != null) {
                String word = wp.word;
                int start = wp.start;
                int end = wp.stop;

                DictAgent dictAgent = settings.getDictAgent();
                TextSetting textSetting = settings.getTextSetting();

                if (textSetting.isLookupDict()) {
                    int dictMode = settings.getDictMode();
                    if (dictMode == Settings.DICT_MODE_BOTTOM_SHEET) {

                        WordContext wc = null;
                        Para para = getPara();
                        if (para != null) {
                            wc = para.getWordContext();
                        }
                        Action onOpen = () -> setSelection(start, end);
                        Action onClose = this::removeSelection;

                        dictAgent.requestDict(word, wc, onOpen, onClose);
                    } else if (dictMode == Settings.DICT_MODE_SIMPLE_POPUP) {

                        Consumer<PopupWindow> onPopup = (PopupWindow pw) -> setSelection(start, end);
                        PopupWindow.OnDismissListener onDismissListener = this::removeSelection;

                        Offset o = ViewUtils.calculateOffset(this, start, end);
                        PopupWindowManager pwm = settings.getPopupWindowManager();

                        dictAgent.requestDictPopup(word,
                                this,
                                o,
                                pwm,
                                onPopup,
                                onDismissListener);
                    }
                }
                if (textSetting.isHighlightSentence()) {
                    String sid = highlightTheSentence(start, end);
                    if (sid != null && peer != null) {
                        peer.highlightTheSentence(sid);
                    }
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {
            TextSetting ts = settings.getTextSetting();
            if (ts.isLookupDict() || ts.isHighlightSentence()) {
                handleTouchUp(event);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
//        boolean result = super.onTextContextMenuItem(id);
//
        System.out.println("textContextMenuItem: " + id);
//        System.out.println("result: " + result);

        return false;
    }


    @Override
    public void createContextMenu(ContextMenu menu) {
//        System.out.println("createContextMenu, menu.size: " + menu.size());
//        CharSequence selected = "";
//        int start = getSelectionStart();
//        if (start >= 0) {
//            int end = getSelectionEnd();
//            if (end >= 0) {
//                selected = getText().subSequence(start, end);
//            }
//        }
//        String selection = selected.toString();
//        System.out.println("selection: " + selection);
//        if (selection.indexOf(' ') >= 0) {
//            super.createContextMenu(menu);
//        } else {
//            Toast.makeText(getContext(), "Option zz: " + selected, Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {

        System.out.println("onCreateContextMenu, menu.size: " + menu.size());

    }

    protected ParaTagHandler newTagHandler() {
        return new ContentTagHandler(this, settings);
    }

    private void doBuildNewWords() {

        wordSet = new HashSet<>();

        String text = getText().toString();
        int textLen = text.length();

        List<NewWordSpan> spans = spansHolder.getSpansForPush(NewWordSpan.class);
        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {
            String word = matcher.group();
            wordSet.add(word.toLowerCase());

            int start = matcher.start();
            int end = matcher.end();
            if (end - start <= 2) {
                continue;
            }

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
            span.setEnabled(true);
            spans.add(span);
        }
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

        resetSpans(NewWordSpan.class, false);
        spansHolder.removeSpans(NewWordSpan.class);

        doBuildNewWords();

        newWordsBuilt = true;
    }

    private void processNewWords() {
        VocabularyService vocabularyService = settings.getVocabularyService();
        Flowable<UserVocabularyMap> uvmFlowable = vocabularyService.getUserVocabularyMap();
        if (uvmDisp != null) {
            uvmDisp.dispose();
        }
        uvmDisp = uvmFlowable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userVocabularyMap -> {
                    this.userVocabularyMap = userVocabularyMap;
                    newWordsBuilt = false;
                    tryBuildNewWords();
                });
    }

    private void buildWordSet() {
    }

    @Override
    public void setRawText(String content) {
        super.setRawText(content);

        if (settings.isHandleNewWords()) {
            buildWordSet();
            processNewWords();
        }
    }

//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        System.out.println("onDetachedFromWindow");
//        if (uvmDisp != null && !uvmDisp.isDisposed()) {
//            uvmDisp.dispose();
//            System.out.println("onDetachedFromWindow ..dispose");
//        }
//    }
}
