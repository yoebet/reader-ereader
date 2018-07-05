package wjy.yo.ereader.serviceimpl;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.PreferenceService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.WordCategoryService;
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
    private UserVocabularyMap userVocabularyMap;
    private Single<UserVocabularyMap> userVocabularyMapObs;


    @Inject
    public VocabularyServiceImpl(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
        Disposable disp = preferenceService.getBaseVocabularyChangeObservable()
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
                    } else {
                        baseVocabulary = bv;
                        baseVocabularyMap = null;
                        userVocabularyMap = null;
                    }
                });
    }


    @SuppressLint("CheckResult")
    public Map<String, Integer> getMyWordsMap() {

        Schedulers.io().scheduleDirect(() -> {
//            Disposable d0 = dictService.loadBaseForms().subscribe(System.out::println);
//            Disposable d1 = wordCategoryService.getCategoriesMap().subscribe(System.out::println);
//            Disposable d2 = wordCategoryService.getCategoryAllWords("cet4").subscribe(System.out::println, System.out::println);

//            String bv = preferenceService.getBaseVocabulary();
//            System.out.println("getBaseVocabulary " + bv);
//
//            String[] words = {"help", "service", "basic", "grammar", "interrupt", "jade"};
//            getBaseVocabularyMap().subscribe(map -> {
//                for (String word : words) {
//                    System.out.println("BaseVocabularyMap, " + word + ": " + map.get(word));
//                }
//            });
//            getUserVocabularyMap().subscribe((UserVocabularyMap uvm) -> {
//                for (String word : words) {
//                    System.out.println("UserVocabularyMap, " + word + ": " + uvm.get(word));
//                }
//            });
//            for (String word : words) {
//                inBaseVocabulary(word).subscribe(
//                        o -> System.out.println("inBaseVocabulary, " + word + ": " + o),
//                        Throwable::printStackTrace,
//                        () -> System.out.println("inBaseVocabulary, " + word + ": -"));
//            }

//            statistic().subscribe(System.out::println);
        });

        return null;
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
                            Throwable::printStackTrace,
                            () -> {
                                emitter.onSuccess(baseVocabularyMap);
                                baseVocabularyMapObs = null;
                            });
        }).cache();

        baseVocabularyMapObs = result;
        return result;
    }

    public Single<UserVocabularyMap> getUserVocabularyMap() {
        if (userVocabularyMapObs != null) {
            return userVocabularyMapObs;
        }
        if (userVocabularyMap != null) {
            return Single.just(userVocabularyMap);
        }

        Single<UserVocabularyMap> result = Observable
                .combineLatest(
                        getBaseVocabularyMap().toObservable(),
                        dictService.loadBaseForms().toObservable(),
                        userWordService.getWordsMap().toObservable(),
                        CombinedUserVocabularyMap::new)
                .map((UserVocabularyMap vm) -> {
                    userVocabularyMap = vm;
                    userVocabularyMapObs = null;
                    return vm;
                })
                .first(UserVocabularyMap.EmptyMap())
                .cache();

        userVocabularyMapObs = result;
        return result;
    }

    /*public Maybe<String> inBaseVocabulary(String word) {
        return getBaseVocabularyMap()
                .filter(m -> m.get(word) != null)
                .map(m -> m.get(word));
    }*/

    static class CombinedUserVocabularyMap implements UserVocabularyMap {

        private Map<String, String> baseVocabularyMap;
        private Map<String, String> baseFormsMap;
        private Map<String, UserWord> userWordsMap;

        public CombinedUserVocabularyMap(Map<String, String> baseVocabularyMap, Map<String, String> baseFormsMap, Map<String, UserWord> userWordsMap) {
            this.baseVocabularyMap = baseVocabularyMap;
            this.baseFormsMap = baseFormsMap;
            this.userWordsMap = userWordsMap;
            System.out.println("new CombinedUserVocabularyMap()");
            System.out.println("baseVocabularyMap, Count:" + baseVocabularyMap.size());
            System.out.println("baseFormsMap, Count:" + baseFormsMap.size());
            System.out.println("userWordsMap, Count:" + userWordsMap.size());
        }

        private Object _get(String word) {

            UserWord userWord = userWordsMap.get(word);
            if (userWord != null) {
                return userWord;
            }
            return baseVocabularyMap.get(word);
        }


        private Pattern upperLetter = Pattern.compile("[A-Z]");

        public Object get(String word) {

            Object codeOrUW = this._get(word);

            if (codeOrUW != null) {
                return codeOrUW;
            }
            if (upperLetter.matcher(word).matches()) {
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
                    System.out.println("hit, " + word + " -> " + base);
                    return codeOrUW;
                }
            }

            List<String> forms = guestBaseForms(word);
            for (String form : forms) {
                codeOrUW = this._get(form);
                if (codeOrUW != null) {
                    System.out.println("guestBaseForms hit, " + word + " -> " + form);
                    return codeOrUW;
                }
            }

            String stem = guestStem(word);
            if (stem != null) {
                codeOrUW = this._get(stem);
                if (codeOrUW != null) {
                    System.out.println("guestStem hit, " + word + " -> " + stem);
                }
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

}
