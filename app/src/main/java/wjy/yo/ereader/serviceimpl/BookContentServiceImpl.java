package wjy.yo.ereader.serviceimpl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.serviceimpl.common.ModelChanges;
import wjy.yo.ereader.serviceimpl.common.NetworkBoundResource;
import wjy.yo.ereader.serviceimpl.common.RateLimiter;

import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_CHAP_PARAS;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;

@Singleton
public class BookContentServiceImpl extends UserDataService implements BookContentService {

    private static final String CHAP_KEY_PREFIX = "CHAP_";

    private ChapDao chapDao;
    private ParaDao paraDao;

    @Inject
    BookAPI bookAPI;

    private RateLimiter<String> bookRateLimit = new RateLimiter<>(1, TimeUnit.MINUTES);

    @Inject
    BookContentServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.chapDao = db.chapDao();
        this.paraDao = db.paraDao();
        System.out.println("new BookContentServiceImpl");
    }

    public Flowable<ChapDetail> loadChapDetail(String chapId) {
        return new NetworkBoundResource<ChapDetail>() {

            @Override
            protected Flowable<ChapDetail> loadFromDb() {
                System.out.println("3 loadFromDb ...");
                return chapDao.loadDetail(chapId);
            }

            @Override
            protected boolean shouldFetch(@Nullable ChapDetail chap) {
                if (offline) {
                    return false;
                }
                if (!accountService.isLogin()) {
                    return false;
                }
                if (chap == null || chap.getParas() == null || chap.getParas().size() == 0) {
                    String key = CHAP_KEY_PREFIX + chapId;
                    return bookRateLimit.shouldFetch(key);
                }

                DataSyncRecord dsr = dataSyncService.getCommonDataSyncRecord(
                        DSR_CATEGORY_CHAP_PARAS, DSR_DIRECTION_DOWN);
                Date pslf = chap.getParasLastFetchAt();
                return dataSyncService.checkTimeout(dsr, pslf);
            }

            @NonNull
            @Override
            protected Flowable<ChapDetail> createCall() {
                return bookAPI.getChapDetail(chapId);
            }

            @Override
            protected void saveCallResult(ChapDetail chap, ChapDetail localChap) {
                System.out.println("3 saveCallResult ...");

                Date now = new Date();
                chap.setLastFetchAt(now);
                chap.setParasLastFetchAt(now);
                if (localChap == null) {
                    chapDao.insert(chap);
                } else {
                    chapDao.update(chap);
                }
                System.out.println((localChap == null) ? "inserted: " : "updated: " + chap);

                List<Para> paras = chap.getParas();
                if (paras == null) {
                    return;
                }

                paraDao.deleteChapParas(chapId);
                for (Para para : paras) {
                    para.setBookId(chap.getBookId());
                    para.setChapId(chapId);
                    para.setLastFetchAt(now);
                    paraDao.insert(para);
                }

//                List<Para> localParas = null;
//                if (localChap != null) {
//                    localParas = localChap.getParas();
//                }
//
//                ModelChanges.Changes changes = ModelChanges.evaluateChanges(
//                        (List<FetchedData>) (List<?>) paras,
//                        (List<FetchedData>) (List<?>) localParas);
//                ModelChanges.applyChanges(changes, paraDao, true);
            }

        }.asFlowable();
    }
}
