package wjy.yo.ereader.entityvo.book;

import android.arch.persistence.room.Relation;

import java.util.List;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;

public class BookDetail extends Book {

    @Setter
    @Getter
    @Relation(parentColumn = "id", entityColumn = "bookId")
    private List<Chap> chaps;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BookDetail bd = (BookDetail) o;
        return Objects.equals(chaps, bd.chaps);
    }
}
