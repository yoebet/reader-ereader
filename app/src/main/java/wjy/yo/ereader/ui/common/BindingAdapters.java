package wjy.yo.ereader.ui.common;

import android.databinding.BindingAdapter;
import android.view.View;

public class BindingAdapters {

    @BindingAdapter("goneUnless")
    public static void goneUnless(View view, Boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @BindingAdapter("goneIfNull")
    public static void goneIfNull(View view, Object obj) {
        view.setVisibility(obj == null ? View.GONE : View.VISIBLE);
    }
}
