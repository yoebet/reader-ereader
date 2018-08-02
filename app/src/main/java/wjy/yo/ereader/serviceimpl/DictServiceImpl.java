package wjy.yo.ereader.serviceimpl;

import android.content.Context;
import android.util.LruCache;

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
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.dict.DictDao;
import wjy.yo.ereader.db.dict.MeaningItemDao;
import wjy.yo.ereader.db.dict.WordRankDao;
import wjy.yo.ereader.entity.Ordered;
import wjy.yo.ereader.entity.dict.Dict;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.remote.DictAPI;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.util.Utils;

import static wjy.yo.ereader.util.EnglishForms.guestBaseForms;
import static wjy.yo.ereader.util.EnglishForms.guestStem;

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

    private LruCache<String, DictEntry> dictCache;

    @Inject
    DictServiceImpl(DB db) {
        this.db = db;
        this.dictDao = db.dictDao();
        this.meaningItemDao = db.meaningItemDao();
        this.wordRankDao = db.wordRankDao();
        dictCache = new LruCache<>(200);
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

    private Maybe<DictEntry> lookupLocally(final String word0) {

        return Maybe.<Dict>create(emitter -> {

            String word = word0;
            Dict dict = dictDao.loadBasicSync(word);
            if (dict != null) {
                emitter.onSuccess(dict);
                return;
            }
            Pattern ulp = Pattern.compile("[A-Z]");
            if (ulp.matcher(word).matches()) {
                word = word.toLowerCase();
                dict = dictDao.loadBasicSync(word);
                if (dict != null) {
                    emitter.onSuccess(dict);
                    return;
                }
            }
            List<String> forms = guestBaseForms(word);
            for (String form : forms) {
                dict = dictDao.loadBasicSync(form);
                if (dict != null) {
                    emitter.onSuccess(dict);
                    return;
                }
            }
            String stem = guestStem(word);
            if (stem != null) {
                dict = dictDao.loadBasicSync(stem);
                if (dict != null) {
                    emitter.onSuccess(dict);
                    return;
                }
            }
            emitter.onComplete();

        }).flatMap(dict -> loadFromDB(dict.getWord()));
    }

    private void setupWordRanks(DictEntry entry) {

        Map<String, Integer> categories = entry.getCategories();
        if (categories == null || categories.size() == 0) {
            return;
        }

        String word = entry.getWord();
        List<WordRank> wordRanks = new ArrayList<>();
        for (Map.Entry<String, Integer> e : categories.entrySet()) {
            WordRank wr = new WordRank();
            wr.setWord(word);
            wr.setName(e.getKey());
            wr.setRank(e.getValue());
            wordRanks.add(wr);
        }
        entry.setWordRanks(wordRanks);
    }

    private void saveDictEntry(DictEntry entry) {

        db.runInTransaction(() -> {

            setupWordRanks(entry);

            String[] forms = entry.getForms();
            if (forms == null) {
                entry.setFormsCsv(null);
            } else {
                entry.setFormsCsv(Utils.join(forms));
            }

            final String word = entry.getWord();
            Dict dict = dictDao.loadBasicSync(word);
            if (dict == null) {
                dictDao.insert(entry);
            } else {
                if (dict.equals(entry)) {
                    return;
                }
                dictDao.update(entry);
            }

            if (dict != null) {
                meaningItemDao.deleteMeaningItems(word);
            }
            List<MeaningItem> meaningItems = entry.getMeaningItems();
            if (meaningItems != null && meaningItems.size() > 0) {
                int no = 1;
                for (MeaningItem mi : meaningItems) {
                    mi.setWord(word);
                    mi.setNo(no++);
                }
                meaningItemDao.insert(meaningItems);
            }

            if (dict != null) {
                wordRankDao.deleteRanks(word);
            }
            List<WordRank> wordRanks = entry.getWordRanks();
            if (wordRanks != null && wordRanks.size() > 0) {
                wordRankDao.insert(wordRanks);
            }
        });
    }

    public Maybe<DictEntry> lookup(final String word) {

        DictEntry cached = dictCache.get(word);
        if (cached != null) {
            System.out.println(word + ", cache hit.");
            return Maybe.just(cached);
        }

        Function<DictEntry, DictEntry> cacheIt = e -> {
            dictCache.put(e.getWord(), e);
            if (!word.equals(e.getWord())) {
                dictCache.put(word, e);
            }
            return e;
        };

        if (settingService.isOffline()) {
            return lookupLocally(word).map(cacheIt);
        }
        Maybe<DictEntry> dbSource = loadFromDB(word);
        Maybe<DictEntry> netSource = dictAPI.dictLookup(word)
                .map(entry -> {
                    System.out.println(word + ", Received From Network ...");
                    saveDictEntry(entry);
                    return entry;
                });

        return dbSource.concatWith(netSource).firstElement().map(cacheIt);
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
