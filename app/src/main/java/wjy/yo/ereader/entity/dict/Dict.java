package wjy.yo.ereader.entity.dict;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.FetchedData;

@Entity(tableName = "dict", indices = {@Index(value = "word", unique = true)})
@Data
@EqualsAndHashCode(callSuper = true)
public class Dict extends FetchedData {

    private String word;

    private boolean isPhrase;

    private String baseForm;

    @Embedded
    private Phonetics phonetics;


    @Data
    public static class Phonetics {

        @SerializedName("UK")
        private String phoneticUK;

        @SerializedName("US")
        private String phoneticUS;
    }
}

