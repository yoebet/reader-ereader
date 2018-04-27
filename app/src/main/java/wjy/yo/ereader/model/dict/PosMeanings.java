package wjy.yo.ereader.model.dict;


public class PosMeanings {
    private String pos;
    private MeaningItem[] items;

    public PosMeanings(String pos, MeaningItem[] items) {
        this.pos = pos;
        this.items = items;
    }

    public String getPos() {
        return pos;
    }

    public MeaningItem[] getItems() {
        return items;
    }
}
