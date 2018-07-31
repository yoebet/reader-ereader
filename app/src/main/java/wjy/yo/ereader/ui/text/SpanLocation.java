package wjy.yo.ereader.ui.text;

public class SpanLocation {
    private int start;
    private int end;

    public SpanLocation() {
    }

    public SpanLocation(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public boolean contains(int start, int end) {
        return this.start <= start && this.end >= end;
    }
}
