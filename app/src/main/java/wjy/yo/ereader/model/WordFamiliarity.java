package wjy.yo.ereader.model;

public enum WordFamiliarity {
    Ignored(0), Strange(1), InProgress(2), Familiar(3);
    private int value;

    WordFamiliarity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
