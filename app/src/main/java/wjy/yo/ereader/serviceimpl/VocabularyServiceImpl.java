package wjy.yo.ereader.serviceimpl;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.PreferenceService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.service.WordCategoryService;

@Singleton
public class VocabularyServiceImpl implements VocabularyService {

    @Inject
    PreferenceService preferenceService;

    @Inject
    DictService dictService;

    @Inject
    UserWordService userWordService;

    @Inject
    WordCategoryService wordCategoryService;

    @Inject
    public VocabularyServiceImpl() {

    }


    public Map<String, Integer> getMyWordsMap() {

        /*Schedulers.io().scheduleDirect(() -> {
            Disposable d0 = dictService.loadBaseForms().subscribe(System.out::println);
            Disposable d1 = wordCategoryService.getCategoriesMap().subscribe(System.out::println);
            Disposable d2 = wordCategoryService.getCategoryAllWords("cet4").subscribe(System.out::println, System.out::println);
            Disposable d3 = preferenceService.getUserWordTags().subscribe(System.out::println);

            String bv = preferenceService.getBaseVocabulary();
            System.out.println("getBaseVocabulary " + bv);
        });*/

        return null;
    }

}
