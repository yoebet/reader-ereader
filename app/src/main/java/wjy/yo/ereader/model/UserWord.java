package wjy.yo.ereader.model;

import java.util.Date;

public class UserWord {
    private String word;
    private WordFamiliarity familiarity;
    private Date addedOn;
    private WordOrigin origin;
    private String memo;

    public UserWord(String word){
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public WordFamiliarity getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(WordFamiliarity familiarity) {
        this.familiarity = familiarity;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public WordOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(WordOrigin origin) {
        this.origin = origin;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
