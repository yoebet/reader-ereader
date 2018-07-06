package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Maybe;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.book.ChapDao;
import wjy.yo.ereader.db.book.ParaDao;
import wjy.yo.ereader.entity.DataSyncRecord;
import wjy.yo.ereader.entity.FetchedData;
import wjy.yo.ereader.entity.Ordered;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.remote.BookAPI;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.service.DataSyncService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.util.Utils;

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

    private void saveChapParas(ChapDetail chap, ChapDetail localChap) {
        System.out.println("saveChapParas ...");

        if (Objects.equals(chap, localChap)) {
            return;
        }
        Date now = new Date();
        chap.setLastFetchAt(now);
        chap.setParasLastFetchAt(now);
        if (localChap == null) {
            chapDao.insert(chap);
            System.out.println("inserted: " + chap);
        } else {
            chapDao.update(chap);
            System.out.println("updated: " + chap);
        }

        List<Para> paras = (chap.getParas() == null) ? new ArrayList<>(0) : chap.getParas();

        for (Para para : paras) {
            para.setBookId(chap.getBookId());
            para.setChapId(chap.getId());
        }

        List<Para> localParas;
        if (localChap == null || localChap.getParas() == null) {
            localParas = new ArrayList<>(0);
        } else {
            localParas = localChap.getParas();
        }

        db.runInTransaction(() ->
                Utils.updateData(paras, localParas, paraDao, true)
        );
    }


    @SuppressLint("CheckResult")
    private void doFetchChapDetail(FlowableEmitter<ChapDetail> emitter, String chapId, ChapDetail localChap) {

        bookAPI.getChapDetail(chapId).subscribe(
                (ChapDetail chap) -> {
                    emitter.onNext(chap);
                    saveChapParas(chap, localChap);
                },
                e -> {
                    e.printStackTrace();
                    if (localChap != null) {
                        emitter.onNext(localChap);
                    }
                },
                () -> {
                    if (localChap != null) {
                        emitter.onNext(localChap);
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void fetchChapDetail(FlowableEmitter<ChapDetail> emitter, String chapId, ChapDetail localChap) {
        if (localChap == null) {
            doFetchChapDetail(emitter, chapId, null);
            return;
        }
        bookAPI.getParaVersions(chapId).subscribe(
                (List<FetchedData> fds) -> {
                    List<Para> paras = localChap.getParas();
                    if (Utils.versionEquals(fds, paras)) {
                        System.out.println("Paras Version Not Change.");
                        emitter.onNext(localChap);

                        Date now = new Date();
                        localChap.setParasLastFetchAt(now);
                        chapDao.update(localChap);

                        return;
                    }
                    doFetchChapDetail(emitter, chapId, localChap);
                },
                e -> {
                    e.printStackTrace();
                    emitter.onNext(localChap);
                });
    }

    private Maybe<ChapDetail> loadChapDetailFromDB(String chapId) {
        return chapDao.loadDetail(chapId).map(
                (ChapDetail chap) -> {
                    List<Para> paras = chap.getParas();
                    if (paras != null && paras.size() > 1) {
                        Collections.sort(paras, Ordered.Comparator);
                    }
                    return chap;
                });
    }

    public Flowable<ChapDetail> loadChapDetail(String chapId) {

        return Flowable.create(emitter -> {
            loadChapDetailFromDB(chapId).subscribe(
                    (ChapDetail localChap) -> {
                        if (settingService.isOffline()) {
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

                        fetchChapDetail(emitter, chapId, localChap);
                    },
                    Throwable::printStackTrace,
                    () -> fetchChapDetail(emitter, chapId, null));
        }, BackpressureStrategy.LATEST);
    }
}
