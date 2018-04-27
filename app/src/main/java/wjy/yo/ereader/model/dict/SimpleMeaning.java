package wjy.yo.ereader.model.dict;

public class SimpleMeaning {
    private String pos;
    private String exp;

    public SimpleMeaning(String pos, String exp) {
        this.pos = pos;
        this.exp = exp;
    }

    public String getPos() {
        return pos;
    }

    public String getExp() {
        return exp;
    }
}
