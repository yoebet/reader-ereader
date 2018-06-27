package wjy.yo.ereader.entity;

import java.util.Comparator;

public interface Ordered {

    Comparator<Ordered> Comparator = (o1, o2) -> (int) (o1.getNo() - o2.getNo());

    long getNo();

    void setNo(long no);
}
