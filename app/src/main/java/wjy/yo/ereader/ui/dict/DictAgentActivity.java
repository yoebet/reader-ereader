package wjy.yo.ereader.ui.dict;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.TextSearchService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.WordContext;

public abstract class DictAgentActivity extends AppCompatActivity implements DictAgent {

    @Inject
    protected DictService dictService;

    @Inject
    protected UserWordService userWordService;

    @Inject
    protected VocabularyService vocabularyService;

    @Inject
    protected TextSearchService textSearchService;

    private String currentWord;
    private DictRequest currentDictRequest;

    private Disposable dictDisp;


//    public String getCurrentWord() {
//        return currentWord;
//    }

    public void requestDict(final String word, WordContext wordContext) {
        if (word == null || "".equals(word.trim())) {
            return;
        }

        if (word.equals(currentWord) && currentDictRequest != null) {
            showDict(currentDictRequest);
            return;
        }

        currentDictRequest = null;
        currentWord = word;

        clear();

        dictDisp = dictService.lookup(word)
                .map(entry -> {
                    DictRequest req = new DictRequest(this, word, entry);
                    req.setWordContext(wordContext);
                    return req;
                })
                .map(this::prepareRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        req -> {
                            if (!word.equals(currentWord)) {
                                return;
                            }

                            currentDictRequest = req;

                            DictEntry entry = req.entry;
                            String entryWord = entry.getWord();

                            Maybe<UserWord> uwm = userWordService.getOne(entryWord);
                            Single<List<String>> labels = vocabularyService.evaluateWordRankLabels(entry.getWordRanks());
                            Maybe<WordCategory> bvc = vocabularyService.inBaseVocabulary(entryWord);
                            req.setUserWord(uwm);
                            req.setRankLabels(labels);
                            req.setBaseVocabularyCategory(bvc);

                            showDict(req);
                        },
                        ExceptionHandlers::handle,
                        () -> Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show());

    }

    protected DictRequest prepareRequest(DictRequest request) {

        DictEntry entry = request.entry;
        System.out.println("got: " + entry.getWord());
        List<MeaningItem> meaningItems = entry.getMeaningItems();
        for (MeaningItem mi : meaningItems) {
            System.out.println(mi.getPos() + " " + mi.getExp());
        }

        String baseForm = entry.getBaseForm();
        if (baseForm != null) {
            List<String> refWords = new ArrayList<>();
            refWords.add(baseForm);
            request.setRefWords(refWords);
        }

        return request;
    }

    protected abstract void showDict(DictRequest request);

    private void clear() {
        if (dictDisp != null) {
            dictDisp.dispose();
            dictDisp = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        clear();
    }
}
