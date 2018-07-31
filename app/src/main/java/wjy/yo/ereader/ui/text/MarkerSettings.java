package wjy.yo.ereader.ui.text;

public class MarkerSettings {

    public static final int YES = 1;

    public static final int NO = 2;

    public static final int ON_FOCUS = 3;

    private int markAnnotations = YES;

    private int markUserWords = YES;

    public int getMarkAnnotations() {
        return markAnnotations;
    }

    public void setMarkAnnotations(int markAnnotations) {
        this.markAnnotations = markAnnotations;
    }

    public int getMarkUserWords() {
        return markUserWords;
    }

    public void setMarkUserWords(int markUserWords) {
        this.markUserWords = markUserWords;
    }
}
