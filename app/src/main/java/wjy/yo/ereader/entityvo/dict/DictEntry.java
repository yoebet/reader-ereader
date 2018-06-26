package wjy.yo.ereader.entityvo.dict;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entity.dict.Dict;

public class DictEntry extends Dict {

    @Setter
    @Getter
    @SerializedName("simple")
    @Relation(parentColumn = "word", entityColumn = "word")
    private List<MeaningItem> meaningItems;

    @Setter
    @Getter
    @Relation(parentColumn = "word", entityColumn = "word")
    private List<WordRank> wordRanks;

    @Setter
    @Getter
    @Ignore
    private Map<String, Integer> categories;

//    private List<PosCompleteMeaning> completeMeanings;

}

