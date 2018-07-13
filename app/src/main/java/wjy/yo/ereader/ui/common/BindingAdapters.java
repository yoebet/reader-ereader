package wjy.yo.ereader.ui.common;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import wjy.yo.ereader.R;
import wjy.yo.ereader.entity.userdata.UserWord;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BindingAdapters {

    @BindingAdapter("goneUnless")
    public static void goneUnless(View view, Boolean visible) {
        view.setVisibility(visible ? VISIBLE : GONE);
    }

    @BindingAdapter("showIfNull")
    public static void showIfNull(View view, Object obj) {
        view.setVisibility(obj == null ? VISIBLE : GONE);
    }

    @BindingAdapter("goneIfNull")
    public static void goneIfNull(View view, Object obj) {
        view.setVisibility(obj == null ? GONE : VISIBLE);
    }

    @BindingAdapter("goneIfEmpty")
    public static void goneIfEmpty(View view, Object obj) {
        if (obj == null) {
            view.setVisibility(GONE);
            return;
        }
        if (obj instanceof String) {
            String s = (String) obj;
            if (s.trim().equals("")) {
                view.setVisibility(GONE);
                return;
            }
        }
        if (obj instanceof Collection) {
            Collection c = (Collection) obj;
            if (c.size() == 0) {
                view.setVisibility(GONE);
                return;
            }
        }
        view.setVisibility(VISIBLE);
    }

    @BindingAdapter("familiarityName")
    public static void familiarityName(TextView text, int familiarity) {
        if (familiarity < UserWord.FamiliarityLowest || familiarity > UserWord.FamiliarityHighest) {
            String name = "熟悉度 " + familiarity;
            text.setText(name);
            return;
        }

        Resources res = text.getResources();
        int textRes = 0;
        int colorRes = 0;
        switch (familiarity) {
            case 1:
                textRes = R.string.word_familiarity_1;
                colorRes = R.color.word_familiarity_1;
                break;
            case 2:
                textRes = R.string.word_familiarity_2;
                colorRes = R.color.word_familiarity_2;
                break;
            case 3:
                textRes = R.string.word_familiarity_3;
                colorRes = R.color.word_familiarity_3;
                break;
        }

        text.setTextColor(res.getColor(colorRes));
        text.setText(textRes);
    }


    @BindingAdapter("familiarityIcon")
    public static void familiarityIcon(IconTextView iconText, int familiarity) {
        if (familiarity < UserWord.FamiliarityLowest || familiarity > UserWord.FamiliarityHighest) {
            iconText.setText("");
            return;
        }

        Resources res = iconText.getResources();
        int textRes = 0;
        int colorRes = 0;

        switch (familiarity) {
            case 1:
                textRes = R.string.word_familiarity_1_icon;
                colorRes = R.color.word_familiarity_1;
                break;
            case 2:
                textRes = R.string.word_familiarity_2_icon;
                colorRes = R.color.word_familiarity_2;
                break;
            case 3:
                textRes = R.string.word_familiarity_3_icon;
                colorRes = R.color.word_familiarity_3;
                break;
        }

        iconText.setTextColor(res.getColor(colorRes));
        iconText.setText(textRes);
    }

    @BindingAdapter("phonetic")
    public static void phonetic(TextView text, String phonetic) {
        if (phonetic != null
                && phonetic.startsWith("[")
                && phonetic.endsWith("]")) {
            phonetic = phonetic.substring(1, phonetic.length() - 1);
        }
        text.setText(phonetic);
    }

    private static final String datePattern = "yyyy-M-d";
    private static SimpleDateFormat sdf = new SimpleDateFormat(datePattern, Locale.CHINA);

    @BindingAdapter("dateText")
    public static void dateText(TextView text, Date date) {
        String dateStr;
        if (date == null) {
            dateStr = "";
        } else {
            dateStr = sdf.format(date);
        }
        text.setText(dateStr);
    }
}
