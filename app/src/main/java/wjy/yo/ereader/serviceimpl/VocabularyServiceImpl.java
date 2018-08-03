package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;
import android.util.ArrayMap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.PreferenceService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.WordCategoryService;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.VocabularyStatistic;

import static wjy.yo.ereader.util.Constants.RX_STRING_ELEMENT_NULL;
import static wjy.yo.ereader.util.EnglishForms.guestBaseForms;
import static wjy.yo.ereader.util.EnglishForms.guestStem;

@Singleton
public class VocabularyServiceImpl implements VocabularyService {

    private PreferenceService preferenceService;

    @Inject
    DictService dictService;

    @Inject
    UserWordService userWordService;

    @Inject
    WordCategoryService wordCategoryService;

    private String baseVocabulary;
    private Map<String, String> baseVocabularyMap = new ConcurrentHashMap<>();
    private Single<Map<String, String>> baseVocabularyMapObs;

    private Disposable bvChangeDisp;

    private Disposable bvmDisp;

    private Subject<UserVocabularyMap> userVocabularyChangeSubject;

    private boolean needRebuildUserVocabularyMap;

    @Inject
    VocabularyServiceImpl(PreferenceService preferenceService) {

        userVocabularyChangeSubject = BehaviorSubject.create();

        this.preferenceService = preferenceService;
        if (bvChangeDisp != null) {
            bvChangeDisp.dispose();
        }
        bvChangeDisp = preferenceService.getBaseVocabularyChangeObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(bv -> {
                    if (RX_STRING_ELEMENT_NULL.equals(bv)) {
                        this.baseVocabulary = null;
                        if (baseVocabularyMap == null) {
                            baseVocabularyMap = new ConcurrentHashMap<>();
                        } else {
                            baseVocabularyMap.clear();
                        }
                        userVocabularyChangeSubject.onNext(UserVocabularyMap.InvalidMap);
                    } else {
                        baseVocabulary = bv;
                        baseVocabularyMap = null;
                        if (userVocabularyChangeSubject.hasObservers()) {
                            buildUserVocabularyMap();
                        } else {
                            needRebuildUserVocabularyMap = true;
                        }
                    }
                }, ExceptionHandlers::handle);
    }

    private static class CategoryAndWords {
        String code;
        List<String> words;

        CategoryAndWords(String code, List<String> words) {
            this.code = code;
            this.words = words;
        }
    }

    @SuppressLint("CheckResult")
    public Single<Map<String, String>> getBaseVocabularyMap() {
        if (baseVocabularyMapObs != null) {
            return baseVocabularyMapObs;
        }
        if (baseVocabularyMap != null) {
            return Single.just(baseVocabularyMap);
        }

        final Single<Map<String, String>> result = Single.<Map<String, String>>create(emitter -> {
            baseVocabularyMap = new ConcurrentHashMap<>();
            wordCategoryService.getWordCategory(baseVocabulary).toObservable()
                    .flatMapIterable((WordCategory wc) -> {
                        Set<String> codes = new HashSet<>();
                        while (wc != null) {
                            String code = wc.getCode();
                            if (codes.contains(code)) {
                                System.out.println("CIRCULAR " + codes);
                                break;
                            }
                            codes.add(code);
                            WordCategory extend = wc.getExtend();
                            if (extend != null) {
                                wc = extend;
                            } else {
                                break;
                            }
                        }
                        return codes;
                    })
                    .flatMap(
                            code -> wordCategoryService.getCategoryAllWords(code)
                                    .toObservable()
                                    .map(list -> new CategoryAndWords(code, list)))
                    .subscribe(
                            (CategoryAndWords cl) -> {
                                for (String word : cl.words) {
                                    if (word.indexOf(' ') == -1) {
                                        baseVocabularyMap.put(word, cl.code);
                                    }
                                }
                            },
                            ExceptionHandlers::handle,
                            () -> {
                                emitter.onSuccess(baseVocabularyMap);
                                baseVocabularyMapObs = null;
                            });
        }).cache();

        baseVocabularyMapObs = result;
        return result;
    }

    private void buildUserVocabularyMap() {
        needRebuildUserVocabularyMap = false;
        if (this.baseVocabulary == null) {
            return;
        }
        System.out.println(">>> do Build UserVocabularyMap");
        if (bvmDisp != null) {
            bvmDisp.dispose();
        }
        bvmDisp = Observable
                .combineLatest(
                        getBaseVocabularyMap().toObservable(),
                        dictService.loadBaseForms().toObservable(),
                        userWordService.getWordsMap().toObservable(),
                        CombinedUserVocabularyMap::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        userVocabularyChangeSubject::onNext,
                        ExceptionHandlers::handle);
    }

    public Flowable<UserVocabularyMap> getUserVocabularyMap() {

        if (needRebuildUserVocabularyMap) {
            buildUserVocabularyMap();
        }
        return userVocabularyChangeSubject.toFlowable(BackpressureStrategy.LATEST);
    }

    public Maybe<WordCategory> inBaseVocabulary(final String word) {
        return getBaseVocabularyMap()
                .filter(m -> m.get(word) != null)
                .flatMap(m -> wordCategoryService.getWordCategory(m.get(word)));
    }

    static class CombinedUserVocabularyMap implements UserVocabularyMap {

        private Map<String, String> baseVocabularyMap;
        private Map<String, String> baseFormsMap;
        private Map<String, UserWord> userWordsMap;

        private static Pattern upperLetter = Pattern.compile("[A-Z]");

        CombinedUserVocabularyMap(Map<String, String> baseVocabularyMap,
                                  Map<String, String> baseFormsMap,
                                  Map<String, UserWord> userWordsMap) {
            this.baseVocabularyMap = baseVocabularyMap;
            this.baseFormsMap = baseFormsMap;
            this.userWordsMap = userWordsMap;
            System.out.println("baseVocabularyMap, Count:" + baseVocabularyMap.size());
            System.out.println("baseFormsMap, Count:" + baseFormsMap.size());
            System.out.println("userWordsMap, Count:" + userWordsMap.size());
        }

        private Object _get(String word) {

            UserWord userWord = userWordsMap.get(word);
            if (userWord != null &&
                    !UserWord.ChangeFlagDelete.equals(userWord.getChangeFlag())) {
                return userWord;
            }
            return baseVocabularyMap.get(word);
        }

        public Object get(String word) {

            Object codeOrUW = this._get(word);

            if (codeOrUW != null) {
                return codeOrUW;
            }
            if (upperLetter.matcher(word).find()) {
                word = word.toLowerCase();
                codeOrUW = this._get(word);
                if (codeOrUW != null) {
                    return codeOrUW;
                }
            }

            String base = this.baseFormsMap.get(word);
            if (base != null) {
                codeOrUW = this._get(base);
                if (codeOrUW != null) {
//                    System.out.println("hit, " + word + " -> " + base);
                    return codeOrUW;
                }
            }

            List<String> forms = guestBaseForms(word);
            for (String form : forms) {
                codeOrUW = this._get(form);
                if (codeOrUW != null) {
//                    System.out.println("baseForms hit, " + word + " -> " + form);
                    return codeOrUW;
                }
            }

            String stem = guestStem(word);
            if (stem != null) {
                codeOrUW = this._get(stem);
//                if (codeOrUW != null) {
//                    System.out.println("guestStem hit, " + word + " -> " + stem);
//                }
            }

            return codeOrUW;
        }
    }


    public Single<VocabularyStatistic> statistic() {

        VocabularyStatistic vs = new VocabularyStatistic();
        return Observable.combineLatest(
                getBaseVocabularyMap().toObservable(),
                userWordService.getAll().toObservable(),
                (Map<String, String> bvm, List<UserWord> uws) -> {
                    List<UserWord> familiarity1Words = new ArrayList<>();
                    List<UserWord> familiarity2Words = new ArrayList<>();
                    List<UserWord> familiarity3Words = new ArrayList<>();
                    for (UserWord uw : uws) {
                        if (uw.getWord().indexOf(' ') >= 0) {
                            continue;
                        }
                        if (uw.getFamiliarity() == 1) {
                            familiarity1Words.add(uw);
                        } else if (uw.getFamiliarity() == 2) {
                            familiarity2Words.add(uw);
                        } else if (uw.getFamiliarity() == 3) {
                            familiarity3Words.add(uw);
                        }
                    }

                    int familiarity1Count = familiarity1Words.size();
                    int familiarity2Count = familiarity2Words.size();
                    int familiarity3Count = familiarity3Words.size();

                    int baseVocabularyCount = bvm.size();
                    int userWordsCount = familiarity1Count + familiarity2Count + familiarity3Count;

                    int unfamiliarCountInBV = 0;
                    int familiarCountNotInBV = 0;

                    for (UserWord uw : familiarity1Words) {
                        if (bvm.get(uw.getWord()) != null) {
                            unfamiliarCountInBV++;
                        }
                    }
                    for (UserWord uw : familiarity2Words) {
                        if (bvm.get(uw.getWord()) != null) {
                            unfamiliarCountInBV++;
                        }
                    }

                    for (UserWord uw : familiarity3Words) {
                        if (bvm.get(uw.getWord()) == null) {
                            familiarCountNotInBV++;
                        }
                    }

                    int graspedCount = baseVocabularyCount - unfamiliarCountInBV + familiarCountNotInBV;

                    vs.setBaseVocabularyCount(baseVocabularyCount);
                    vs.setUserWordsCount(userWordsCount);
                    vs.setFamiliarity1Count(familiarity1Count);
                    vs.setFamiliarity2Count(familiarity2Count);
                    vs.setFamiliarity3Count(familiarity3Count);
                    vs.setUnfamiliarCountInBV(unfamiliarCountInBV);
                    vs.setFamiliarCountNotInBV(familiarCountNotInBV);
                    vs.setGraspedCount(graspedCount);

                    return vs;
                }
        ).first(vs);

    }

    public Single<List<String>> evaluateWordRankLabels(List<WordRank> wordRanks) {

        if (wordRanks == null || wordRanks.size() == 0) {
            return Single.just(new ArrayList<>());
        }

        Map<String, Integer> categories = new ArrayMap<>();
        for (WordRank wr : wordRanks) {
            categories.put(wr.getName(), wr.getRank());
        }

        return wordCategoryService.getCategoriesMap()
                .map(categoriesMap -> {
                    List<String> labels = new ArrayList<>();

                    String[] userWordTags = preferenceService.getUserWordTags();
                    for (String code : userWordTags) {
                        WordCategory wordCategory = categoriesMap.get(code);
                        if (wordCategory == null) {
                            System.out.println("Not Found: " + code);
                            continue;
                        }

                        String key = wordCategory.getDictKey();
                        Integer rank = categories.get(key);
                        if (rank == null) {
                            continue;
                        }
                        String op = wordCategory.getDictOperator();
                        Integer val = wordCategory.getDictValue();

                        if (op == null) {
                            if (!rank.equals(val)) {
                                continue;
                            }
                        } else if ("lt".equals(op)) {
                            if (rank >= val) {
                                continue;
                            }
                        } else if ("gt".equals(op)) {
                            if (rank <= val) {
                                continue;
                            }
                        } else if ("ne".equals(op)) {
                            if (rank.equals(val)) {
                                continue;
                            }
                        } else {
                            continue;
                        }

                        String label = wordCategory.getName();

                        if ("haici".equals(code)) {
                            label = "海词" + rank + "星";
                        } else if ("coca".equals(code)
                                || "bnc".equals(code)
                                || "anc".equals(code)) {
                            int align3 = rank + (3 - rank % 3);
                            label = code.toUpperCase() + " " + align3 + "000";
                        }

                        labels.add(label);
                    }

                    return labels;
                });

    }

}
