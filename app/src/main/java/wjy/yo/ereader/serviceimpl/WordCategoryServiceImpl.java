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

    private Map<String, List<String>> categoryAllWordsMap = new ArrayMap<>();

    @Inject
    public WordCategoryServiceImpl(DB db) {
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
                    if (cl.size() > 0) {
                        Schedulers.io().scheduleDirect(() -> {
                            db.runInTransaction(() -> {
                                wordCategoryDao.deleteAll();
                                wordCategoryDao.insert(cl);
                            });
                        });
                    }
                    return cl;
                });

        return dbSource.filter(cl -> cl.size() > 0).concatWith(netSource.toMaybe())
                .firstElement().toSingle(new ArrayList<>());
    }


    public Single<List<WordCategory>> getCategories() {
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


    public Single<List<String>> getCategoryAllWords(String code) {

        List<String> allWords0 = categoryAllWordsMap.get(code);
        if (allWords0 != null) {
            return Single.just(allWords0);
        }

        String fileName = "wl-" + code;
        Maybe<List<String>> fileSource = Maybe.create(emitter -> {
            File base = context.getFilesDir();
            File catFile = new File(base, fileName);
            if (!catFile.exists() || !catFile.isFile()) {
                emitter.onComplete();
                return;
            }

            List<String> allWords = new ArrayList<>(256);
            categoryAllWordsMap.put(code, allWords);

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
        if (settingService.isOffline()) {
            return fileSource.toSingle(new ArrayList<>());
        }

        Single<List<String>> netSource = dictAPI.loadCategoryAllWords(code)
                .map((List<String> words) -> {

                    categoryAllWordsMap.put(code, words);

                    Schedulers.io().scheduleDirect(() -> {
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
                    });

                    return words;
                });

        return fileSource.switchIfEmpty(netSource);
    }
}
