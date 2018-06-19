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

    @Relation(parentColumn = "word", entityColumn = "word")
    private List<WordRank> wordRanks;

    //TODO: order
    @SerializedName("simple")
    @Relation(parentColumn = "word", entityColumn = "word")
    private List<MeaningItem> meaningItems;

    @Ignore
    private Map<String, Integer> categories;

//    private List<PosCompleteMeaning> completeMeanings;

    public List<WordRank> getWordRanks() {
        return wordRanks;
    }

    public void setWordRanks(List<WordRank> wordRanks) {
        this.wordRanks = wordRanks;
    }

    public List<MeaningItem> getMeaningItems() {
        return meaningItems;
    }

    public void setMeaningItems(List<MeaningItem> meaningItems) {
        this.meaningItems = meaningItems;
    }

    public Map<String, Integer> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, Integer> categories) {
        System.out.println("CAT: " + categories);
        this.categories = categories;
    }
}

