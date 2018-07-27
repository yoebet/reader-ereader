package wjy.yo.ereader.ui.text.textview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.text.Selection;
import android.text.Spannable;
import android.util.AttributeSet;

import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.ui.text.Environment;
import wjy.yo.ereader.ui.text.Settings;


public abstract class ParaTextView extends AppCompatTextView {

    protected String rawText;

    protected Environment environment;

    protected Settings settings;

    public ParaTextView(Context context) {
        super(context);
    }

    public ParaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


    protected void setSelection(int start, int stop) {
        CharSequence cs = getText();
        if (cs instanceof Spannable) {
            Spannable sp = (Spannable) cs;
            Selection.setSelection(sp, start, stop);
        }
    }

    protected void removeSelection() {
        CharSequence cs = getText();
        if (cs instanceof Spannable) {
            Spannable sp = (Spannable) cs;
            Selection.removeSelection(sp);
        }
    }

    protected Para getPara() {
        return (Para) getTag();
    }

    public abstract void setRawText(String content);

}
