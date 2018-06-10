package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

import wjy.yo.ereader.entity.BaseModel;

@Entity(tableName = "dict_meaning_item", indices = {@Index(value = {"word", "pos"}, unique = true)})
public class MeaningItem extends BaseModel {

    @ForeignKey(entity = Dict.class, parentColumns = "word", childColumns = "word", onDelete = ForeignKey.CASCADE)
    private String word;

    private String pos;

    private String exp;

    private Integer no;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPos() {
        return pos;
    }

    public String getExp() {
        return exp;
    }


    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }
}
