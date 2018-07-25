package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookContentService {

    Flowable<ChapDetail> loadChapDetail(String chapId);

    Maybe<Para> loadPara(String id);
}
