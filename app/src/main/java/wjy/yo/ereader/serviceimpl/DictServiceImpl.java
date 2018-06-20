package wjy.yo.ereader.serviceimpl;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.dict.DictDao;
import wjy.yo.ereader.db.dict.MeaningItemDao;
import wjy.yo.ereader.db.dict.WordRankDao;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.remote.DictAPI;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.LocalSettingService;

@Singleton
public class DictServiceImpl implements DictService {

    @Inject
    DictAPI dictAPI;

    @Inject
    LocalSettingService settingService;

    private DB db;
    private DictDao dictDao;
    private MeaningItemDao meaningItemDao;
    private WordRankDao wordRankDao;

    @Inject
    public DictServiceImpl(DB db) {
        System.out.println("new DictServiceImpl");

        this.db = db;
        this.dictDao = db.dictDao();
        this.meaningItemDao = db.meaningItemDao();
        this.wordRankDao = db.wordRankDao();
    }

    private void saveDictEntry(DictEntry entry, DictEntry localEntry) {
        System.out.println("saveDictEntry ...");
        System.out.println(entry.getCategories());

        String word = entry.getWord();

        db.runInTransaction(() -> {
            if (localEntry == null) {
                dictDao.insert(entry);
            } else {
                dictDao.update(entry);
            }

            meaningItemDao.deleteMeaningItems(word);
            List<MeaningItem> meaningItems = entry.getMeaningItems();
            int no = 1;
            for (MeaningItem mi : meaningItems) {
                mi.setWord(word);
                mi.setNo(no++);
                long id = meaningItemDao.insert(mi);
                mi.setId((int) id);
            }

            wordRankDao.deleteRanks(word);

            List<WordRank> wordRanks = new ArrayList<>();
            Map<String, Integer> categories = entry.getCategories();
            if (categories != null) {
                for (Map.Entry<String, Integer> e : categories.entrySet()) {
                    WordRank wr = new WordRank();
                    wr.setWord(word);
                    wr.setName(e.getKey());
                    wr.setRank(e.getValue());
                    long id = wordRankDao.insert(wr);
                    wr.setId((int) id);
                    wordRanks.add(wr);
                }
            }
            entry.setWordRanks(wordRanks);
        });
    }

    public Maybe<DictEntry> lookup(String word) {

        Maybe<DictEntry> dbSource = dictDao.load(word);
        if (settingService.isOffline()) {
            return dbSource;
        }
        Maybe<DictEntry> netSource = dictAPI.dictLookup(word)
                .map(entry -> {
                    System.out.println("DictEntry  Received From Network ...");

                    Schedulers.io().scheduleDirect(() -> {
                        saveDictEntry(entry, null);
                    });
                    return entry;
                });

        return dbSource.concatWith(netSource).firstElement();
    }

}
