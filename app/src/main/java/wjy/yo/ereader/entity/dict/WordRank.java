package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.FetchedData;


@Entity(tableName = "dict_word_rank", indices = {@Index(value = {"word", "name"}, unique = true)})
@Data
@NoArgsConstructor
public class WordRank {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ForeignKey(entity = Dict.class, parentColumns = "word", childColumns = "word", onDelete = ForeignKey.CASCADE)
    private String word;

    private String name;

    private Integer rank;

}
