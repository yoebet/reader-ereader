package wjy.yo.ereader.reader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import wjy.yo.ereader.R;

public class ClickableWordSpan extends ClickableSpan {

    PopupWindowManager pwm;

    ClickableWordSpan(PopupWindowManager pwm) {
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

        ForegroundColorSpan spans[] = ss.getSpans(start, end, ForegroundColorSpan.class);
        int color = Color.BLUE;
        for (ForegroundColorSpan span : spans) {
            color = span.getForegroundColor();
            ss.removeSpan(span);
            System.out.println("remove SPAN: " + span);
        }
        int fc = (color == Color.BLUE) ? Color.GREEN : Color.BLUE;
        ss.setSpan(new ForegroundColorSpan(fc), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        Log.d("evaluatePosition", "------");

        Layout textLayout = textView.getLayout();


        float startXCoordinate = textLayout.getPrimaryHorizontal(start);
        float endXCoordinate = textLayout.getPrimaryHorizontal(end);
        Log.d("XCoordinates", "" + startXCoordinate + "-" + endXCoordinate);

        int startLine = textLayout.getLineForOffset(start);
        int endLine = textLayout.getLineForOffset(end);
        Log.d("LineSpan", "" + startLine + "-" + endLine);

        int offsetY = 0;
        int offsetX = 0;

//            int wh=textView.getWidth();
        if (startLine == endLine) {
            offsetX = (int) startXCoordinate;
        }

        int th = textView.getHeight();
        Log.d("getHeight", "" + th);
        Rect rect = new Rect();
        int baseLine2 = textLayout.getLineBounds(endLine, rect);
        int dy = th - rect.bottom;
        offsetY = -dy;

        Log.d("offset", "offset:" + offsetY + "," + offsetY);


//            if (pw == null || !pw.isShowing()) {
        if (pwm.currentPopup != null) {
            pwm.currentPopup.dismiss();
            pwm.currentPopup = null;
        }
        LayoutInflater li = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View v = li.inflate(R.layout.popup_window, null);
        TextView titleView = v.findViewById(R.id.cword);
        titleView.setText(word.toString());

//            View vt=context.findViewById(R.id.annot);
        System.out.println("contextView: " + v);
        PopupWindow pw = new PopupWindow(v);
        pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(textView, offsetX, offsetY);
        pwm.currentPopup = pw;
    }
}
