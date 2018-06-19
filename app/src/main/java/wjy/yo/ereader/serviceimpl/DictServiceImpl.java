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
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.dict.DictDao;
import wjy.yo.ereader.db.dict.MeaningItemDao;
import wjy.yo.ereader.db.dict.WordRankDao;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.remote.DictAPI;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.serviceimpl.common.NetworkBoundResource2;

@Singleton
public class DictServiceImpl implements DictService {

    @Inject
    DictAPI dictAPI;

    protected boolean offline = false;

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

    public Flowable<DictEntry> lookup(String word) {

        return new NetworkBoundResource2<DictEntry>("DictEntry") {

            @Override
            protected Maybe<DictEntry> loadFromDb() {
                System.out.println("D loadFromDb ...");
                return dictDao.load(word);
            }

            @Override
            protected boolean shouldFetch(@Nullable DictEntry entry) {
                if (offline) {
                    return false;
                }
                return entry == null;
            }

            @NonNull
            @Override
            protected Maybe<DictEntry> createCall() {
                return dictAPI.dictLookup(word);
            }

            @Override
            protected void saveCallResult(DictEntry entry, DictEntry localEntry) {
                System.out.println("D saveCallResult ...");
                System.out.println(entry.getCategories());

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
        }.asFlowable();
    }

}
