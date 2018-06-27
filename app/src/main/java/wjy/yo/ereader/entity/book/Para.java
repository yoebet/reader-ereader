package wjy.yo.ereader.entity.book;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;

/**
 * Created by wsx on 2018/4/19.
 */

@Entity(tableName = "book_para")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Para extends FetchedData implements Ordered {

    @NonNull
    @ForeignKey(entity = Book.class, parentColumns = "id", childColumns = "bookId")
    private String bookId;

    @NonNull
    @ForeignKey(entity = Chap.class, parentColumns = "id", childColumns = "chapId")
    private String chapId;

    private String content;

    private String trans;

    private long no;

}
