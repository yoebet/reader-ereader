package wjy.yo.ereader.ui.common;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.TextView;

import java.util.Collection;

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
        if (UserWord.FamiliarityLowest <= familiarity
                && familiarity <= UserWord.FamiliarityHighest) {
            String name = UserWord.FamiliarityNames[familiarity];
            text.setText(name);

            Resources res = text.getResources();
            switch (familiarity) {
                case 1:
                    text.setTextColor(res.getColor(R.color.vocabulary_familiarity_1));
                    break;
                case 2:
                    text.setTextColor(res.getColor(R.color.vocabulary_familiarity_2));
                    break;
                case 3:
                    text.setTextColor(res.getColor(R.color.vocabulary_familiarity_3));
                    break;
            }
        } else {
            String name = "熟悉度 " + familiarity;
            text.setText(name);
        }
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
}
