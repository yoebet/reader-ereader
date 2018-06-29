package wjy.yo.ereader.entityvo.book;

import android.arch.persistence.room.Relation;

import java.util.List;
import java.util.Objects;

import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;

public class ChapDetail extends Chap {

    @Relation(parentColumn = "id", entityColumn = "chapId")
    private List<Para> paras;

    public List<Para> getParas() {
        return paras;
    }

    public void setParas(List<Para> paras) {
        this.paras = paras;
    }

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
