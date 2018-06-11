package wjy.yo.ereader.entityvo.dict;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entity.dict.Dict;

public class DictEntry {

    @Embedded
    private Dict entry;

    @Relation(parentColumn = "_id", entityColumn = "word")
    private List<WordRank> wordRanks;

    @Relation(parentColumn = "_id", entityColumn = "word")
    private List<MeaningItem> meaningItems;

//    private List<PosCompleteMeaning> completeMeanings;

    public Dict getEntry() {
        return entry;
    }

    public void setEntry(Dict entry) {
        this.entry = entry;
    }

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
}

