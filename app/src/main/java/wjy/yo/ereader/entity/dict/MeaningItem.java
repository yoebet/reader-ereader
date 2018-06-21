package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "dict_meaning_item", indices = {@Index(value = {"word", "pos"}, unique = true)})
public class MeaningItem implements Ordered {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ForeignKey(entity = Dict.class, parentColumns = "word", childColumns = "word", onDelete = ForeignKey.CASCADE)
    private String word;

    private String pos;

    private String exp;

    private long no;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

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

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }
}
