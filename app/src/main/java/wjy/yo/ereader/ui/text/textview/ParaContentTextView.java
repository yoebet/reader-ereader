package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.common.HtmlParser;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.text.OnTouchBehavior;
import wjy.yo.ereader.ui.text.Settings;
import wjy.yo.ereader.ui.text.taghandler.ContentTagHandler;
import wjy.yo.ereader.util.Offset;
import wjy.yo.ereader.vo.WordContext;

import static wjy.yo.ereader.util.Utils.calculateOffset;


public class ParaContentTextView extends ParaTextView {

    public ParaContentTextView(Context context) {
        super(context);
    }

    public ParaContentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaContentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    class WordAndPosition {
        String word;
        int start;
        int end;
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
        wp.end = end;

        return wp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        OnTouchBehavior onTouchBehavior = settings.getOnTouchBehavior();
        if (onTouchBehavior == null) {
            return super.onTouchEvent(event);
        }

        boolean handled = false;
        if (onTouchBehavior.isDefaultBehaviorFirst()) {
            handled = super.onTouchEvent(event);
        }
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            DictAgent dictAgent = environment.getDictAgent();
            if (dictAgent != null &&
                    onTouchBehavior.isShowDict() &&
                    onTouchBehavior.selectTargetContains(OnTouchBehavior.SELECT_TARGET_WORD)) {

//                int start = getSelectionStart();
//                int end = getSelectionEnd();
//                if (start >= 0 && end >= 0) {
//                    if (start > end) {
//                        int tmp = start;
//                        start = end;
//                        end = tmp;
//                    }
//                    System.out.println("Selection: " + start + " <> " + end);
//                }

                float x = event.getX();
                float y = event.getY();
//                System.out.println("Event: " + action + " " + x + "," + y);
                int offset = getOffsetForPosition(x, y);
                if (offset >= 0) {
                    WordAndPosition wp = getTheWord(getText(), offset);
                    if (wp != null) {
                        String word = wp.word;
//                        System.out.println("offset: " + offset + ", " + word);
//                        System.out.println("Position: " + wp.start + " <> " + wp.end);

                        int dictMode = settings.getDictMode();
                        if (dictMode == Settings.DICT_MODE_BOTTOM_SHEET) {
                            WordContext wc = null;
                            Para para = getPara();
                            if (para == null) {
//                            wc = new WordContext();
                            } else {
                                wc = para.getWordContext();
                            }
                            dictAgent.requestDict(word, wc);
                            handled = true;
                        } else if (dictMode == Settings.DICT_MODE_SIMPLE_POPUP) {
                            Offset o = calculateOffset(this, wp.start, wp.end);
                            dictAgent.requestDictPopup(word, this, o.x, o.y,
                                    environment.getPopupWindowManager());
                            handled = true;
                        }
                    }
                }
            }
            if (onTouchBehavior.isPerformClick()) {
                boolean clickResult = performClick();
                System.out.println("clickResult: " + clickResult);
//                handled = handled || clickResult;
            }
        }
        if (!onTouchBehavior.isDefaultBehaviorFirst()) {
            if (onTouchBehavior.isDefaultBehaviorAnyway() || !handled) {
                return super.onTouchEvent(event);
            }
        }
        return true;
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
        System.out.println("createContextMenu, menu.size: " + menu.size());
        CharSequence selected = "";
        int start = getSelectionStart();
        if (start >= 0) {
            int end = getSelectionEnd();
            if (end >= 0) {
                selected = getText().subSequence(start, end);
            }
        }
        String selection = selected.toString();
        System.out.println("selection: " + selection);
        if (selection.indexOf(' ') >= 0) {
            super.createContextMenu(menu);
        } else {
            Toast.makeText(getContext(), "Option zz: " + selected, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreateContextMenu(ContextMenu menu) {

        System.out.println("onCreateContextMenu, menu.size: " + menu.size());

    }


    public void setRawText(String content) {
        this.rawText = content;

        if (content.indexOf('<') == -1) {
            setText(content);
            return;
        }

        ContentTagHandler th = new ContentTagHandler(environment, settings);
        Spanned spanned = HtmlParser.buildSpannedText(content, th);
        setText(spanned);
    }

}
