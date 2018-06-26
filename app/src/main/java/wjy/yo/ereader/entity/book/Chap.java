package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;
import wjy.yo.ereader.entity.userdata.UserChap;

/**
 * Created by wsx on 2018/1/30.
 */

@Entity(tableName = "book_chap")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Chap extends FetchedData implements Ordered {

    @NonNull
    @ForeignKey(entity = Book.class, parentColumns = "id", childColumns = "bookId")
    private String bookId;

    private String name;

    private String zhName;

    private long no;

    @EqualsAndHashCode.Exclude
    private Date parasLastFetchAt;

    @Ignore
    @EqualsAndHashCode.Exclude
    private UserChap userChap;

    @Override
    public String toString() {
        return id + " " + name;
    }
}
