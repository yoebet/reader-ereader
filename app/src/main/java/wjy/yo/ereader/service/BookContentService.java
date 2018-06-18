package wjy.yo.ereader.service;

import io.reactivex.Flowable;
import wjy.yo.ereader.entityvo.book.ChapDetail;

public interface BookContentService {

    Flowable<ChapDetail> loadChapDetail(String chapId);
}
