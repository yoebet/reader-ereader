package wjy.yo.ereader.entity.dict;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "dict", indices = {@Index(value = "word", unique = true)})
public class Dict extends FetchedData {

    private String word;

    private String phoneticUK;

    private String phoneticUS;

    private boolean isPhrase;

    private String baseForm;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPhoneticUK() {
        return phoneticUK;
    }

    public void setPhoneticUK(String phoneticUK) {
        this.phoneticUK = phoneticUK;
    }

    public String getPhoneticUS() {
        return phoneticUS;
    }

    public void setPhoneticUS(String phoneticUS) {
        this.phoneticUS = phoneticUS;
    }

    public boolean isPhrase() {
        return isPhrase;
    }

    public void setPhrase(boolean phrase) {
        isPhrase = phrase;
    }

    public String getBaseForm() {
        return baseForm;
    }

    public void setBaseForm(String baseForm) {
        this.baseForm = baseForm;
    }
}

