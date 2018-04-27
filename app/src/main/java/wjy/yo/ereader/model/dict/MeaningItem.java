package wjy.yo.ereader.model.dict;

public class MeaningItem {
    private int id;
    private String exp;
    private String[] tags;

    public MeaningItem(int id, String exp) {
        this.id = id;
        this.exp = exp;
    }

    public MeaningItem(int id, String exp, String[] tags) {
        this.id = id;
        this.exp = exp;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String[] getTags() {
        return tags;
    }

    public String getExp() {
        return exp;
    }
}
