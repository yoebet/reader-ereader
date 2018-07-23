package wjy.yo.ereader.ui.text;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wjy.yo.ereader.BR;

public class TextProfile extends BaseObservable {

    private boolean showChapTitle;

    private boolean showTrans;

    @Bindable
    public boolean isShowChapTitle() {
        return showChapTitle;
    }

    public void setShowChapTitle(boolean showChapTitle) {
        this.showChapTitle = showChapTitle;
        notifyPropertyChanged(BR.showChapTitle);
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
