package wjy.yo.ereader.entityvo.book;

import android.arch.persistence.room.Relation;

import java.util.List;
import java.util.Objects;

import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;

public class BookDetail extends Book {

    @Relation(parentColumn = "_id", entityColumn = "bookId")
    private List<Chap> chaps;

    public List<Chap> getChaps() {
        return chaps;
    }

    public void setChaps(List<Chap> chaps) {
        this.chaps = chaps;
    }

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
