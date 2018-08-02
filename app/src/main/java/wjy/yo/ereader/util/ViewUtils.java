package wjy.yo.ereader.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import static android.view.View.LAYER_TYPE_SOFTWARE;


public class ViewUtils {

    public static WordAndPosition getTheWord(CharSequence cs, int offset) {

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
        WordAndPosition wp = new WordAndPosition();
        wp.word = cs.subSequence(start, end + 1).toString();
        wp.start = start;
        wp.stop = end + 1;

        return wp;
    }

    public static Offset calculateOffset(TextView textView, int start, int end) {

        Layout textLayout = textView.getLayout();

        int startLine = textLayout.getLineForOffset(start);
        int endLine = textLayout.getLineForOffset(end);

        int offsetX = (startLine == endLine) ? (int) textLayout.getPrimaryHorizontal(start) : 0;

        Rect rect = new Rect();
        textLayout.getLineBounds(endLine, rect);
        int offsetY = rect.bottom - textView.getHeight();

        return new Offset(offsetX, offsetY);
    }


    public static int dpToPx(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }


    public static Drawable generateBackgroundWithShadow(View view,
                                                        int backgroundColor,
                                                        float cornerRadius,
                                                        int shadowColor,
                                                        int elevation,
                                                        int shadowGravity) {
        float[] outerRadius = new float[8];
        Arrays.fill(outerRadius, cornerRadius);

        Rect shapeDrawablePadding = new Rect();
        shapeDrawablePadding.left = elevation;
        shapeDrawablePadding.right = elevation;

        int DY;
        switch (shadowGravity) {
            case Gravity.CENTER:
                shapeDrawablePadding.top = elevation;
                shapeDrawablePadding.bottom = elevation;
                DY = 0;
                break;
            case Gravity.TOP:
                shapeDrawablePadding.top = elevation * 2;
                shapeDrawablePadding.bottom = elevation;
                DY = -1 * elevation / 3;
                break;
            default:
            case Gravity.BOTTOM:
                shapeDrawablePadding.top = elevation;
                shapeDrawablePadding.bottom = elevation * 2;
                DY = elevation / 3;
                break;
        }

        ShapeDrawable shapeDrawable = new ShapeDrawable();
        shapeDrawable.setPadding(shapeDrawablePadding);

        shapeDrawable.getPaint().setColor(backgroundColor);
        shapeDrawable.getPaint().setShadowLayer(cornerRadius / 3, 0, DY, shadowColor);

        view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());

        shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

        LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
        drawable.setLayerInset(0, elevation, elevation * 2, elevation, elevation * 2);

        return drawable;

    }
}
