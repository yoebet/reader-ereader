package wjy.yo.ereader.entity.userdata;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import wjy.yo.ereader.entity.UserData;

@Entity(tableName = "user_word", indices = {@Index(value = {"userName", "word"}, unique = true)})
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UserWord extends UserData {

    @EqualsAndHashCode.Include
    private String word;

    @EqualsAndHashCode.Include
    private int familiarity = FamiliarityLowest;

    private String bookId;

    private String chapId;

    @EqualsAndHashCode.Include
    private String paraId;

    @EqualsAndHashCode.Include
    private String changeFlag;

    public static final int FamiliarityLowest = 1;

    public static final int FamiliarityHighest = 3;

    public static final String ChangeFlagAll = "A";

    public static final String ChangeFlagCreate = "C";

    public static final String ChangeFlagDelete = "D";

    public static final String ChangeFlagFamiliarity = "F";


    public UserWord(String word) {
        this.word = word;
    }

}
