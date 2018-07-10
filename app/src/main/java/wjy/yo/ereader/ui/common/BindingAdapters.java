package wjy.yo.ereader.ui.common;

import android.databinding.BindingAdapter;
import android.view.View;

import java.util.Collection;

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
}
