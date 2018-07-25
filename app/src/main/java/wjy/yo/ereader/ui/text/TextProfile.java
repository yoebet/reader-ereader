package wjy.yo.ereader.ui.text;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wjy.yo.ereader.BR;

public class TextProfile extends BaseObservable {

    private boolean showTitles;

    private boolean showTrans;

    @Bindable
    public boolean isShowTitles() {
        return showTitles;
    }

    public void setShowTitles(boolean showTitles) {
        this.showTitles = showTitles;
        notifyPropertyChanged(BR.showTitles);
    }

    @Bindable
    public boolean isShowTrans() {
        return showTrans;
    }

    public void setShowTrans(boolean showTrans) {
        this.showTrans = showTrans;
        notifyPropertyChanged(BR.showTrans);
    }
}
