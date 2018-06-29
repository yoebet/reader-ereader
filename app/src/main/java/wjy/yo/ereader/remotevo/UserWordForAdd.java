package wjy.yo.ereader.remotevo;

import wjy.yo.ereader.entity.userdata.UserWord;

public class UserWordForAdd {

    private String word;

    private int familiarity;

    private String bookId;

    private String chapId;

    private String paraId;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFamiliarity() {
        return familiarity;
    }

    public void setFamiliarity(int familiarity) {
        this.familiarity = familiarity;
    }

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

    public UserWordForAdd(String word) {
        this.word = word;
    }

    public static UserWordForAdd from(UserWord uw) {
        UserWordForAdd vo = new UserWordForAdd(uw.getWord());
        vo.setFamiliarity(uw.getFamiliarity());
        vo.setBookId(uw.getBookId());
        vo.setChapId(uw.getChapId());
        vo.setParaId(uw.getParaId());
        return vo;
    }

}
