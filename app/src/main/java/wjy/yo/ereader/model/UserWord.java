package wjy.yo.ereader.model;

import java.util.Date;

public class UserWord {
    private String word;
    private Familiarity familiarity;
    private Date addedOn;
    private WordOrigin origin;
    private String memo;

    public UserWord(String word){
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public Familiarity getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(Familiarity familiarity) {
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


    public static enum Familiarity {
        Ignored(0), Strange(1), InProgress(2), Familiar(3);
        private int value;

        Familiarity(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static class WordOrigin {
        private String bookId;
        private String chapId;
        private String paraId;

        public String getBookId() {
            return bookId;
        }

        public void setBookId(String bookId) {
            this.bookId = bookId;
        }

        public String getChapId() {
            return chapId;
        }

        public void setChapId(String chapId) {
            this.chapId = chapId;
        }

        public String getParaId() {
            return paraId;
        }

        public void setParaId(String paraId) {
            this.paraId = paraId;
        }
    }

}
