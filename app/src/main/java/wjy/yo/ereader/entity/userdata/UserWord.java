package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.UserData;
import wjy.yo.ereader.vo.WordContext;

@Entity(tableName = "user_word", indices = {@Index(value = {"userName", "word"}, unique = true)})
public class UserWord extends UserData {

    private String word;

    private int familiarity = FamiliarityLowest;

    private String bookId;

    private String chapId;

    private String paraId;

    private String changeFlag;

    public static final int FamiliarityLowest = 1;

    public static final int FamiliarityHighest = 3;

    public static final String ChangeFlagAll = "A";

    public static final String ChangeFlagCreate = "C";

    public static final String ChangeFlagDelete = "D";

    public static final String ChangeFlagFamiliarity = "F";

    public static final String[] FamiliarityNames = new String[]{"", "很陌生", "熟悉中", "已掌握"};


    public UserWord(String word) {
        this.word = word;
    }


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

    public String getChangeFlag() {
        return changeFlag;
    }

    public void setChangeFlag(String changeFlag) {
        this.changeFlag = changeFlag;
    }

    public WordContext getWordContext() {
        if (this.paraId == null) {
            return null;
        }
        WordContext wc = new WordContext();
        wc.setParaId(this.paraId);
        wc.setChapId(this.chapId);
        wc.setBookId(this.bookId);
        return wc;
    }

    public void setWordContext(WordContext wc) {
        if (wc == null) {
            this.paraId = null;
            this.chapId = null;
            this.bookId = null;
        } else {
            this.paraId = wc.getParaId();
            this.chapId = wc.getChapId();
            this.bookId = wc.getBookId();
        }
    }

    public void setWordContextIfExists(WordContext wc) {
        if (wc == null || wc.getParaId() == null) {
            return;
        }
        setWordContext(wc);
    }

    public String toString() {
        return word + " " + familiarity + (changeFlag == null ? "" : " " + changeFlag);
    }
}
