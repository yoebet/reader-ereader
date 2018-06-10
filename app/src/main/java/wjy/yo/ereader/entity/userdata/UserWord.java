package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import java.util.Date;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "user_word", indices = {@Index(value = {"userName", "word"}, unique = true)})
public class UserWord extends BaseModel {

    private String userName;

    private String word;

    private int familiarity;

    private Date addedOn;

    @Embedded
    private WordOrigin origin;


    public static final int FamiliarityLowest = 1;

    public static final int FamiliarityHighest = 3;


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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
