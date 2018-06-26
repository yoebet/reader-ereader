package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

@Entity(tableName = "dict_meaning_item", indices = {@Index(value = {"word", "pos"}, unique = true)})
@Data
@NoArgsConstructor
public class MeaningItem implements Ordered {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ForeignKey(entity = Dict.class, parentColumns = "word", childColumns = "word", onDelete = ForeignKey.CASCADE)
    private String word;

    private String pos;

    private String exp;

    private long no;

}
