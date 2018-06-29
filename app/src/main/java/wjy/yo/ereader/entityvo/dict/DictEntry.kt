package wjy.yo.ereader.entityvo.dict

import android.arch.persistence.room.Ignore
import android.arch.persistence.room.Relation

import com.google.gson.annotations.SerializedName

import wjy.yo.ereader.entity.dict.MeaningItem
import wjy.yo.ereader.entity.dict.WordRank
import wjy.yo.ereader.entity.dict.Dict

class DictEntry : Dict() {

    @SerializedName("simple")
    @Relation(parentColumn = "word", entityColumn = "word")
    var meaningItems: List<MeaningItem>? = null

    @Relation(parentColumn = "word", entityColumn = "word")
    var wordRanks: List<WordRank>? = null

    @Ignore
    var categories: Map<String, Int>? = null
    //    private List<PosCompleteMeaning> completeMeanings;

}

