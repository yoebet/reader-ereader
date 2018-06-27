package wjy.yo.ereader.serviceimpl;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.dict.DictDao;
import wjy.yo.ereader.db.dict.MeaningItemDao;
import wjy.yo.ereader.db.dict.WordRankDao;
import wjy.yo.ereader.entity.Ordered;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.remote.DictAPI;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.LocalSettingService;

@Singleton
public class DictServiceImpl implements DictService {

    @Inject
    Context context;

    @Inject
    DictAPI dictAPI;

    @Inject
    LocalSettingService settingService;

    private DB db;
    private DictDao dictDao;
    private MeaningItemDao meaningItemDao;
    private WordRankDao wordRankDao;


    private Map<String, String> baseFormMap;
    private Single<Map<String, String>> baseFormMapObs;

    @Inject
    public DictServiceImpl(DB db) {
        this.db = db;
        this.dictDao = db.dictDao();
        this.meaningItemDao = db.meaningItemDao();
        this.wordRankDao = db.wordRankDao();
    }

    private void saveDictEntry(DictEntry entry, DictEntry localEntry) {

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

    private Maybe<DictEntry> loadFromDB(String word) {
        return dictDao.load(word).map(entry -> {
            List<MeaningItem> items = entry.getMeaningItems();
            if (items != null && items.size() > 1) {
                Collections.sort(items, Ordered.Comparator);
            }
            return entry;
        });
    }

    public Maybe<DictEntry> lookup(String word) {

        Maybe<DictEntry> dbSource = loadFromDB(word);
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

    public synchronized Single<Map<String, String>> loadBaseForms() {
        if (baseFormMapObs != null) {
            return baseFormMapObs;
        }
        if (baseFormMap != null) {
            return Single.just(baseFormMap);
        }

        String fileName = "wl-base-forms";
        Maybe<Map<String, String>> fileSource = Maybe.create(emitter -> {
            File base = context.getFilesDir();
            File catFile = new File(base, fileName);
            if (!catFile.exists() || !catFile.isFile()) {
                emitter.onComplete();
                return;
            }

            Map<String, String> bfm = new HashMap<>();
            try (BufferedReader reader
                         = new BufferedReader(new FileReader(catFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] ws = line.split(",");
                    for (String wf : ws) {
                        String[] wordForm = wf.split(":");
                        String word = wordForm[0];
                        String baseForm = wordForm[1];
                        bfm.put(word, baseForm);
                    }
                }
            }
            emitter.onSuccess(bfm);
        });

        Single<Map<String, String>> result;
        if (settingService.isOffline()) {
            result = fileSource.toSingle(new HashMap<>());
        } else {
            Single<Map<String, String>> netSource = dictAPI.loadBaseForms()
                    .map((List<String[]> words) -> {
                        Map<String, String> bfm = new HashMap<>();
                        try (
                                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

                            int count = 0;
                            for (String[] wordForm : words) {
                                if (count % 10 == 0) {
                                    if (count > 0) {
                                        writer.newLine();
                                    }
                                } else {
                                    writer.write(',');
                                }
                                String word = wordForm[0];
                                String baseForm = wordForm[1];
                                bfm.put(word, baseForm);

                                writer.write(word + ":" + baseForm);
                                count++;
                            }

                            System.out.println(fileName + " saved.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return bfm;
                    });

            result = fileSource.switchIfEmpty(netSource);
        }

        result = result.map(bfm -> {
            baseFormMap = bfm;
            baseFormMapObs = null;
            return bfm;
        }).cache();

        baseFormMapObs = result;
        return result;
    }

}
