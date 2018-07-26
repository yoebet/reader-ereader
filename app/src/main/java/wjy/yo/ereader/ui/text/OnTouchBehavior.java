package wjy.yo.ereader.ui.text;

public class OnTouchBehavior implements Cloneable {

    private boolean defaultBehaviorFirst = false;

    private boolean defaultBehaviorAnyway = false;

    private boolean showDict = true;

    private boolean performClick = true;

    public static final int SELECT_TARGET_WORD = 1;
    public static final int SELECT_TARGET_PHRASE = 2;
    public static final int SELECT_TARGET_SENTENCE = 4;
    public static final int SELECT_TARGET_WHOLE_TEXT = 8;

    private int selectTarget = SELECT_TARGET_WORD;

    public int getSelectTarget() {
        return selectTarget;
    }

    public void setSelectTarget(int selectTarget) {
        this.selectTarget = selectTarget;
    }

    public void addSelectTarget(int selectTarget) {
        this.selectTarget |= selectTarget;
    }

    public void removeSelectTarget(int selectTarget) {
        this.selectTarget &= ~selectTarget;
    }

    public boolean selectTargetContains(int selectTarget) {
        return (this.selectTarget & selectTarget) == selectTarget;
    }

    public boolean isDefaultBehaviorFirst() {
        return defaultBehaviorFirst;
    }

    public void setDefaultBehaviorFirst(boolean defaultBehaviorFirst) {
        this.defaultBehaviorFirst = defaultBehaviorFirst;
    }

    public boolean isDefaultBehaviorAnyway() {
        return defaultBehaviorAnyway;
    }

    public void setDefaultBehaviorAnyway(boolean defaultBehaviorAnyway) {
        this.defaultBehaviorAnyway = defaultBehaviorAnyway;
    }

    public boolean isShowDict() {
        return showDict;
    }

    public void setShowDict(boolean showDict) {
        this.showDict = showDict;
    }

    public boolean isPerformClick() {
        return performClick;
    }

    public void setPerformClick(boolean performClick) {
        this.performClick = performClick;
    }
}
