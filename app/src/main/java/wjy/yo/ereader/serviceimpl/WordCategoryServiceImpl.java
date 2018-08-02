package wjy.yo.ereader.serviceimpl;

import android.content.Context;
import android.util.ArrayMap;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.db.DB;
import wjy.yo.ereader.db.dict.WordCategoryDao;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.remote.DictAPI;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.service.WordCategoryService;

@Singleton
public class WordCategoryServiceImpl implements WordCategoryService {

    @Inject
    Context context;

    @Inject
    DictAPI dictAPI;

    @Inject
    LocalSettingService settingService;

    private DB db;

    private WordCategoryDao wordCategoryDao;


    private List<WordCategory> categories;

    private Map<String, WordCategory> categoriesMap;

    private Map<String, List<String>> categoryAllWordsMap = new ConcurrentHashMap<>();

    private Map<String, Single<List<String>>> categoryAllWordsObsMap = new ConcurrentHashMap<>();

    @Inject
    WordCategoryServiceImpl(DB db) {
        this.db = db;
        wordCategoryDao = db.wordCategoryDao();
    }

    private Single<List<WordCategory>> loadAllCategories() {

        Single<List<WordCategory>> dbSource = wordCategoryDao.loadAll();
        if (settingService.isOffline()) {
            return dbSource;
        }
        Single<List<WordCategory>> netSource = dictAPI.getAllCategories()
                .map(cl -> {
                    if (cl.size() == 0) {
                        return cl;
                    }
                    Schedulers.io().scheduleDirect(() -> {
                        db.runInTransaction(() -> {
                            wordCategoryDao.deleteAll();
                            wordCategoryDao.insert(cl);
                        });
                    });
                    return cl;
                });

        return dbSource.filter(cl -> cl.size() > 0).switchIfEmpty(netSource);
    }


    Single<List<WordCategory>> getCategories() {
        if (categories != null) {
            return Single.just(categories);
        }

        return loadAllCategories().map(wl -> {
            categories = wl;
            categoriesMap = new HashMap<>();
            for (WordCategory wc : wl) {
                categoriesMap.put(wc.getCode(), wc);
            }
            for (WordCategory wc : wl) {
                String extendTo = wc.getExtendTo();
                WordCategory extend = categoriesMap.get(extendTo);
                if (extend != null) {
                    wc.setExtend(extend);
                }
            }
            return wl;
        });
    }


    public Single<Map<String, WordCategory>> getCategoriesMap() {
        if (categoriesMap != null) {
            return Single.just(categoriesMap);
        }
        return getCategories().map(l -> categoriesMap);
    }

    public Maybe<WordCategory> getWordCategory(String code) {
        return getCategoriesMap()
                .filter(map -> categoriesMap.get(code) != null)
                .map(map -> categoriesMap.get(code));
    }

    private final Map<String, Object> getCategoryAllWordsLocks = new ConcurrentHashMap<>();

    public Single<List<String>> getCategoryAllWords(String code) {
        Object lock;
        synchronized (getCategoryAllWordsLocks) {
            lock = getCategoryAllWordsLocks.get(code);
            if (lock == null) {
                lock = new Object();
                getCategoryAllWordsLocks.put(code, lock);
            }
        }
        synchronized (lock) {
            return doGetCategoryAllWords(code);
        }
    }

    private Single<List<String>> doGetCategoryAllWords(String code) {
        Single<List<String>> categoryAllWordsObs = categoryAllWordsObsMap.get(code);
        if (categoryAllWordsObs != null) {
            return categoryAllWordsObs;
        }
        List<String> allWords0 = categoryAllWordsMap.get(code);
        if (allWords0 != null) {
            return Single.just(allWords0);
        }

        String fileName = "wl-" + code;
        Maybe<List<String>> fileSource = Maybe.create(emitter -> {
            File base = context.getFilesDir();
            File catFile = new File(base, fileName);
            if (!catFile.exists() || !catFile.isFile()) {
                System.out.println("File Not Exists: " + fileName);
                emitter.onComplete();
                return;
            }

            List<String> allWords = new ArrayList<>(256);

            try (BufferedReader reader
                         = new BufferedReader(new FileReader(catFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] ws = line.split(",");
                    for (String w : ws) {
                        allWords.add(w);
                    }
                }
            }
            emitter.onSuccess(allWords);
        });


        Single<List<String>> result;
        if (settingService.isOffline()) {
            result = fileSource
                    .toSingle(new ArrayList<>());
        } else {

            Single<List<String>> netSource = dictAPI.loadCategoryAllWords(code)
                    .map((List<String> words) -> {
                        try (
                                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {
                            int count = 0;
                            for (String word : words) {
                                if (count % 20 == 0) {
                                    if (count > 0) {
                                        writer.newLine();
                                    }
                                } else {
                                    writer.write(',');
                                }
                                writer.write(word);
                                count++;
                            }

                            System.out.println(fileName + " saved.");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        return words;
                    });

            result = fileSource
                    .switchIfEmpty(netSource);
        }

        result = result
                .map(words -> {
                    categoryAllWordsMap.put(code, words);
                    categoryAllWordsObsMap.remove(code);
                    return words;
                })
                .cache();
        categoryAllWordsObsMap.put(code, result);

        return result;
    }
}
