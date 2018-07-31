package wjy.yo.ereader.entityvo.dict;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entity.dict.Dict;

public class DictEntry extends Dict {

    @SerializedName("simple")
    @Relation(parentColumn = "word", entityColumn = "word")
    private List<MeaningItem> meaningItems;

    @Relation(parentColumn = "word", entityColumn = "word")
    private List<WordRank> wordRanks;

    @Ignore
    private Map<String, Integer> categories;

    @Ignore
    private String[] forms;

    public List<MeaningItem> getMeaningItems() {
        return meaningItems;
    }

    public void setMeaningItems(List<MeaningItem> meaningItems) {
        this.meaningItems = meaningItems;
    }

    public List<WordRank> getWordRanks() {
        return wordRanks;
    }

    public void setWordRanks(List<WordRank> wordRanks) {
        this.wordRanks = wordRanks;
    }

    public Map<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Integer> categories) {
        this.categories = categories;
    }

    public String[] getForms() {
        if (forms == null) {
            String formsCsv = getFormsCsv();
            if (formsCsv != null && !formsCsv.equals("")) {
                forms = getFormsCsv().split(",");
            }
        }
        return forms;
    }

    public void setForms(String[] forms) {
        this.forms = forms;
    }

    //    private List<PosCompleteMeaning> completeMeanings;

}

