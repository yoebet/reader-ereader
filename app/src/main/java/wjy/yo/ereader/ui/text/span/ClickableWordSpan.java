package wjy.yo.ereader.ui.text.span;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.util.Offset;

import static wjy.yo.ereader.util.Utils.calculateOffset;

public class ClickableWordSpan extends ClickableSpan {

    private PopupWindowManager pwm;

    public ClickableWordSpan(PopupWindowManager pwm) {
        this.pwm = pwm;
    }

    @Override
    public void onClick(View view) {
        TextView textView = (TextView) view;
        CharSequence cs = textView.getText();
        if (!(cs instanceof SpannableString)) {
            return;
        }

        SpannableString ss = (SpannableString) cs;

        int start = ss.getSpanStart(this);
        int end = ss.getSpanEnd(this);
        CharSequence word = ss.subSequence(start, end);
        System.out.println(word);

        toggleSpan(ss, start, end);

        LayoutInflater li = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View contentView = li.inflate(R.layout.popup_window, null);
        TextView titleView = contentView.findViewById(R.id.cword);
        titleView.setText(word.toString());

        Offset offset = calculateOffset(textView, start, end);
        popup(contentView, textView, offset.x, offset.y);
    }


    void toggleSpan(SpannableString ss, int start, int end) {
        ForegroundColorSpan spans[] = ss.getSpans(start, end, ForegroundColorSpan.class);
        int color = Color.BLUE;
        for (ForegroundColorSpan span : spans) {
            color = span.getForegroundColor();
            ss.removeSpan(span);
            System.out.println("remove SPAN: " + span);
        }
        int fc = (color == Color.BLUE) ? Color.GREEN : Color.BLUE;
        ss.setSpan(new ForegroundColorSpan(fc), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    void popup(View contentView, View anchor, int offsetX, int offsetY) {

        if (pwm.getCurrentPopup() != null) {
            pwm.getCurrentPopup().dismiss();
        }

        PopupWindow pw = new PopupWindow(contentView);
        pwm.setCurrentPopup(pw);

        pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchor, offsetX, offsetY);
    }
}
