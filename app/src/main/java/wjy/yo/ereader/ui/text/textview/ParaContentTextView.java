package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.databinding.Observable;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.widget.PopupWindow;

import wjy.yo.ereader.BR;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.TextSetting;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.span.AnnotationSpan;
import wjy.yo.ereader.ui.text.span.SentenceSpan;
import wjy.yo.ereader.ui.text.span.UserWordSpan;
import wjy.yo.ereader.ui.text.taghandler.ContentTagHandler;
import wjy.yo.ereader.ui.text.taghandler.ParaTagHandler;
import wjy.yo.ereader.util.Action;
import wjy.yo.ereader.util.Consumer;
import wjy.yo.ereader.util.Offset;
import wjy.yo.ereader.util.ViewUtils;
import wjy.yo.ereader.vo.WordContext;


public class ParaContentTextView extends ParaTextView {

    private ParaTransTextView peer;

    private static ParaContentTextView currentHighlight;

    public ParaContentTextView(Context context) {
        super(context);
    }

    public ParaContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    static class WordAndPosition {
        String word;
        int start;
        int stop;
    }

    public void setPeer(ParaTransTextView peer) {
        this.peer = peer;
    }

    private static Observable.OnPropertyChangedCallback commonTextSettingChangedCallback;

    @Override
    public void setSettings(Settings settings) {
        super.setSettings(settings);
        if (this.settings != null) {
            TextSetting ts = this.settings.getTextSetting();
            ts.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(Observable sender, int propertyId) {
                    if (propertyId == BR.showAnnotations) {
                        resetSpans(AnnotationSpan.class, ts.isShowAnnotations());
                    } else if (propertyId == BR.showUserWords) {
                        resetSpans(UserWordSpan.class, ts.isShowUserWords());
                    }
                }
            });
            if (commonTextSettingChangedCallback == null) {
                commonTextSettingChangedCallback = new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable sender, int propertyId) {
                        if (propertyId == BR.highlightSentence) {
                            if (!ts.isHighlightSentence()) {
                                clearCurrentHighlight();
                                ParaTransTextView.clearCurrentHighlight();
                            }
                        }
                    }
                };
                ts.addOnPropertyChangedCallback(commonTextSettingChangedCallback);
            }
        }
    }

    static void clearCurrentHighlight() {
        if (currentHighlight != null) {
            currentHighlight.resetSpans(SentenceSpan.class, false);
            currentHighlight = null;
        }
    }

    @Override
    protected String highlightTheSentence(int start, int end) {
        clearCurrentHighlight();
        String sid = super.highlightTheSentence(start, end);
        if (sid != null) {
            currentHighlight = this;
        }
        return sid;
    }

    @Override
    protected void highlightTheSentence(String sid) {
        clearCurrentHighlight();
        super.highlightTheSentence(sid);
        currentHighlight = this;
    }

    private WordAndPosition getTheWord(CharSequence cs, int offset) {

        int length = cs.length();
        if (offset >= length) {
            offset = length - 1;
        }
        char c = cs.charAt(offset);
        if (!Character.isLetter(c)) {
            return null;
        }
        int start = offset;
        while (start > 0) {
            c = cs.charAt(start - 1);
            if (Character.isLetter(c)) {
                start--;
            } else {
                break;
            }
        }
        int end = offset;
        while (end + 1 < length) {
            c = cs.charAt(end + 1);
            if (Character.isLetter(c)) {
                end++;
            } else {
                break;
            }
        }
        if (start == end) {
            return null;
        }
        int wordLen = end - start + 1;
        if (wordLen < 3 || wordLen > 20) {
            return null;
        }
//        System.out.println(cs.getClass());
//        if (cs instanceof Spannable) {
//            Selection.setSelection((Spannable) cs, start, end + 1);
//        }
        WordAndPosition wp = new WordAndPosition();
        wp.word = cs.subSequence(start, end + 1).toString();
        wp.start = start;
        wp.stop = end + 1;

        return wp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

//            int start = getSelectionStart();
//            int end = getSelectionEnd();
//            if (start >= 0 && end >= 0) {
//                if (start > end) {
//                    int tmp = start;
//                    start = end;
//                    end = tmp;
//                }
//                System.out.println("Selection: " + start + " <> " + end);
//            }

            float x = event.getX();
            float y = event.getY();
            int offset = getOffsetForPosition(x, y);
            if (offset >= 0) {
                WordAndPosition wp = getTheWord(getText(), offset);
                if (wp != null) {
                    String word = wp.word;
                    int start = wp.start;
                    int end = wp.stop;

                    DictAgent dictAgent = settings.getDictAgent();
                    TextSetting textSetting = settings.getTextSetting();

                    if (dictAgent != null && textSetting.isLookupDict()) {
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

}
