package wjy.yo.ereader.ui.text;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import wjy.yo.ereader.BR;

public class TextSetting extends BaseObservable {

    private boolean lookupDict = true;

    private boolean highlightSentence;

    private boolean showAnnotations;

    private boolean showUserWords;

    private boolean showTrans;

    private boolean contrastHorizontal;

    @Bindable
    public boolean isLookupDict() {
        return lookupDict;
    }

    public void setLookupDict(boolean lookupDict) {
        this.lookupDict = lookupDict;
        notifyPropertyChanged(BR.lookupDict);
    }

    @Bindable
    public boolean isHighlightSentence() {
        return highlightSentence;
    }

    public void setHighlightSentence(boolean highlightSentence) {
        this.highlightSentence = highlightSentence;
        notifyPropertyChanged(BR.highlightSentence);
    }

    @Bindable
    public boolean isShowAnnotations() {
        return showAnnotations;
    }

    public void setShowAnnotations(boolean showAnnotations) {
        this.showAnnotations = showAnnotations;
        notifyPropertyChanged(BR.showAnnotations);
    }

    @Bindable
    public boolean isShowUserWords() {
        return showUserWords;
    }

    public void setShowUserWords(boolean showUserWords) {
        this.showUserWords = showUserWords;
        notifyPropertyChanged(BR.showUserWords);
    }

    @Bindable
    public boolean isShowTrans() {
        return showTrans;
    }

    public void setShowTrans(boolean showTrans) {
        this.showTrans = showTrans;
        notifyPropertyChanged(BR.showTrans);
    }

    @Bindable
    public boolean isContrastHorizontal() {
        return contrastHorizontal;
    }

    public void setContrastHorizontal(boolean contrastHorizontal) {
        this.contrastHorizontal = contrastHorizontal;
        notifyPropertyChanged(BR.contrastHorizontal);
    }
}
