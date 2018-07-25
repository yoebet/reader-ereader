package wjy.yo.ereader.ui.dict;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class WordTextViewPager extends ViewPager {

    private String currentWord;

    private Map<String, Integer> textHeightMap = new HashMap<>();

    public WordTextViewPager(@NonNull Context context) {
        super(context);
    }

    public WordTextViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        if (currentWord != null) {
            Integer stored = textHeightMap.get(currentWord);
            if (stored == null) {
                textHeightMap.put(currentWord, height);
            } else {
                if (stored < height) {
                    textHeightMap.put(currentWord, height);
                } else {
                    height = stored;
//                    System.out.println("use stored: " + height);
                }
            }
        }

//        System.out.println("height: " + height);
        float dp = 300;
        int px = (int) dpToPx(dp);
        if (height < px) {
            height = px;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
