package wjy.yo.ereader.ui.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.ArrayMap;
import android.util.AttributeSet;

import java.util.Map;

import wjy.yo.ereader.R;

public class IconTextView extends AppCompatTextView {

    private final static String TYPEFACE_DEFAULT = "fa-regular";

    private static Map<String, Typeface> typefaceMap = new ArrayMap<>();

    public IconTextView(Context context) {
        super(context);

        Typeface tf = getTypeface(context, null);
        setTypeface(tf);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.IconText, 0, 0);
        String font = a.getString(R.styleable.IconText_iconFont);

        Typeface tf = getTypeface(context, font);
        setTypeface(tf);
    }

    private synchronized Typeface getTypeface(Context context, String fontName) {
        if (fontName == null) {
            fontName = TYPEFACE_DEFAULT;
        }
        Typeface typeface = typefaceMap.get(fontName);
        if (typeface != null) {
            return typeface;
        }
        String path;
        path = "fonts/icons-" + fontName;
        if (fontName.indexOf('.') == -1) {
            path += ".ttf";
        }
        typeface = Typeface.createFromAsset(context.getAssets(), path);
        typefaceMap.put(fontName, typeface);
        return typeface;
    }

}
