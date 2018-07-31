package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "dict", indices = {@Index(value = "word", unique = true)})
public class Dict extends FetchedData {

    private String word;

    private boolean isPhrase;

    private String baseForm;

    private String formsCsv;

    @Embedded
    private Phonetics phonetics;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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

    public String getFormsCsv() {
        return formsCsv;
    }

    public void setFormsCsv(String formsCsv) {
        this.formsCsv = formsCsv;
    }

    public Phonetics getPhonetics() {
        return phonetics;
    }

    public void setPhonetics(Phonetics phonetics) {
        this.phonetics = phonetics;
    }

    public static class Phonetics {

        @SerializedName("UK")
        private String phoneticUK;

        @SerializedName("US")
        private String phoneticUS;

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
    }
}

