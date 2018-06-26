package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "dict_word_category", indices = {@Index(value = "code", unique = true)})
@ForeignKey(entity = WordCategory.class, parentColumns = "code", childColumns = "extendTo", onDelete = ForeignKey.CASCADE)
@Data
@EqualsAndHashCode(callSuper = true)
public class WordCategory extends FetchedData {

    private String code;

    private String name;

    private String dictKey;

    private Integer dictValue;

    private String dictOperator;

    private String description;

    private Integer wordCount;

    private String extendTo;

    private Integer extendedWordCount;

    private long no;

    @Ignore
    @EqualsAndHashCode.Exclude
    private WordCategory extend;
}
