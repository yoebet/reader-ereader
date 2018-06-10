package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.BaseModel;


@Entity(tableName = "dict_word_rank", indices = {@Index(value = {"word", "name"}, unique = true)})
public class WordRank extends BaseModel {

    @ForeignKey(entity = Dict.class, parentColumns = "word", childColumns = "word", onDelete = ForeignKey.CASCADE)
    private String word;

    private String name;

    private Integer rank;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
