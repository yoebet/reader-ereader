package wjy.yo.ereader.serviceimpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;

import static wjy.yo.ereader.util.RateLimiter.RequestFailOrNoDataRetryRateLimit;
import static wjy.yo.ereader.util.Constants.DSR_CATEGORY_CHAP_PARAS;
import static wjy.yo.ereader.util.Constants.DSR_DIRECTION_DOWN;

@Singleton
public class BookContentServiceImpl extends UserDataService implements BookContentService {

    private static final String CHAP_KEY_PREFIX = "CHAP_";

    private DB db;
    private ChapDao chapDao;
    private ParaDao paraDao;

    @Inject
    BookAPI bookAPI;

    @Inject
    LocalSettingService settingService;

    @Inject
    BookContentServiceImpl(DB db, AccountService accountService, DataSyncService dataSyncService) {
        super(accountService, dataSyncService);
        this.db = db;
        this.chapDao = db.chapDao();
        this.paraDao = db.paraDao();
        observeUserChange();
    }


    private void saveChap(ChapDetail chap, ChapDetail localChap) {
        System.out.println("3 saveCallResult ...");

        if (Objects.equals(chap, localChap)) {
            return;
        }
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

        paraDao.deleteChapParas(chap.getId());
        for (Para para : paras) {
            para.setBookId(chap.getBookId());
            para.setChapId(chap.getId());
            para.setLastFetchAt(now);
            paraDao.insert(para);
        }
    }

    public Flowable<ChapDetail> loadChapDetail(String chapId) {

        return Flowable.create(emitter -> {
            chapDao.loadDetail(chapId).subscribe((ChapDetail localChap) -> {
                if (settingService.isOffline()) {
                    emitter.onNext(localChap);
                    return;
                }
                if (!accountService.isLogin()) {
                    emitter.onNext(localChap);
                    return;
                }
                if (localChap.getParas() == null || localChap.getParas().size() == 0) {
                    String key = CHAP_KEY_PREFIX + chapId;
                    if (!RequestFailOrNoDataRetryRateLimit.shouldFetch(key)) {
                        emitter.onNext(localChap);
                        return;
                    }
                }
                DataSyncRecord dsr = dataSyncService.getCommonDataSyncRecord(
                        DSR_CATEGORY_CHAP_PARAS, DSR_DIRECTION_DOWN);
                Date pslf = localChap.getParasLastFetchAt();
                if (!dataSyncService.checkTimeout(dsr, pslf)) {
                    emitter.onNext(localChap);
                    return;
                }

                bookAPI.getChapDetail(chapId).subscribe(
                        (ChapDetail chap) -> {
                            emitter.onNext(chap);
                            saveChap(chap, localChap);
                        },
                        e -> {
                            e.printStackTrace();
                            emitter.onNext(localChap);
                        });
            });
        }, BackpressureStrategy.LATEST);
    }
}
