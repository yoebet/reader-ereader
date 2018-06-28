package wjy.yo.ereader.entityvo.book;

import android.arch.persistence.room.Relation;

import java.util.List;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;

public class ChapDetail extends Chap {

    @Setter
    @Getter
    @Relation(parentColumn = "id", entityColumn = "chapId")
    private List<Para> paras;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ChapDetail cd = (ChapDetail) o;
        return Objects.equals(paras, cd.paras);
    }

}
