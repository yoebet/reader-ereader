package wjy.yo.ereader.serviceimpl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.serviceimpl.common.ModelChanges;
import wjy.yo.ereader.serviceimpl.common.NetworkBoundResource;
import wjy.yo.ereader.serviceimpl.common.RateLimiter;

@Singleton
public class BookContentServiceImpl implements BookContentService {

    private static final String CHAP_KEY_PREFIX = "CHAP_";

    private ChapDao chapDao;
    private ParaDao paraDao;
    private BookAPI bookAPI;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookContentServiceImpl(DB db, BookAPI bookAPI) {
        this.chapDao = db.chapDao();
        this.paraDao = db.paraDao();
        this.bookAPI = bookAPI;
        System.out.println("new BookContentServiceImpl");
    }


    public Flowable<ChapDetail> loadChapDetail(String chapId) {
        return new NetworkBoundResource<ChapDetail>() {
            @Override
            protected void saveCallResult(ChapDetail chap, ChapDetail localChap) {
                System.out.println("3 saveCallResult ...");

                ModelChanges.saveIfNeeded(chap, localChap, chapDao);

                List<Para> paras = chap.getParas();
                if (paras == null) {
                    return;
                }

                String chapId = chap.getId();
                for (Para para : paras) {
                    para.setBookId(chap.getBookId());
                    para.setChapId(chapId);
                }

                List<Para> localParas = null;
                if (localChap != null) {
                    localParas = localChap.getParas();
                }

                ModelChanges.Changes changes = ModelChanges.evaluateChanges(
                        (List<FetchedData>) (List<?>) paras,
                        (List<FetchedData>) (List<?>) localParas);
                ModelChanges.applyChanges(changes, paraDao, true);
            }

            @Override
            protected boolean shouldFetch(@Nullable ChapDetail chap) {
                String key = CHAP_KEY_PREFIX + chapId;
                return bookRateLimit.shouldFetch(key);
            }

            @Override
            protected Flowable<ChapDetail> loadFromDb() {
                System.out.println("3 loadFromDb ...");
                return chapDao.loadDetail(chapId);
            }

            @NonNull
            @Override
            protected Flowable<ChapDetail> createCall() {
                return bookAPI.getChapDetail(chapId);
            }
        }.asFlowable();
    }
}
