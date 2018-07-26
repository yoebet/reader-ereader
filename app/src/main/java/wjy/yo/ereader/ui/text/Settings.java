package wjy.yo.ereader.ui.text;

public class Settings implements Cloneable {

    public static final int DICT_MODE_SIMPLE_POPUP = 1;

    public static final int DICT_MODE_BOTTOM_SHEET = 2;

    private int dictMode = DICT_MODE_BOTTOM_SHEET;

    private boolean handleAnnotations = true;

    private OnTouchBehavior onTouchBehavior;

    public int getDictMode() {
        return dictMode;
    }

    public void setDictMode(int dictMode) {
        this.dictMode = dictMode;
    }

    public boolean isHandleAnnotations() {
        return handleAnnotations;
    }

    public void setHandleAnnotations(boolean handleAnnotations) {
        this.handleAnnotations = handleAnnotations;
    }

    public OnTouchBehavior getOnTouchBehavior() {
        return onTouchBehavior;
    }

    public void setOnTouchBehavior(OnTouchBehavior onTouchBehavior) {
        this.onTouchBehavior = onTouchBehavior;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
