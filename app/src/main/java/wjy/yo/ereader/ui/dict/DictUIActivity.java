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
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;

public abstract class DictUIActivity extends AppCompatActivity implements DictUI {

    @Inject
    protected DictService dictService;

    @Inject
    protected UserWordService userWordService;

    @Inject
    protected VocabularyService vocabularyService;

    private String currentWord;
    private DictUIRequest currentDictUIRequest;

    private Disposable dictDisp;


    public String getCurrentWord() {
        return currentWord;
    }

    public void requestDict(final String word) {
        if (word == null || "".equals(word.trim())) {
            return;
        }

        if (word.equals(currentWord) && currentDictUIRequest != null) {
            showDict(currentDictUIRequest);
            return;
        }

        currentDictUIRequest = null;
        currentWord = word;

        clear();

        dictDisp = dictService.lookup(word)
                .map(entry -> new DictUIRequest(word, entry))
                .map(this::prepareUIRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        req -> {
                            if (!word.equals(currentWord)) {
                                return;
                            }

                            currentDictUIRequest = req;

                            DictEntry entry = req.entry;
                            Maybe<UserWord> userWordMaybe = userWordService.getOne(entry.getWord());
                            Single<List<String>> labels = vocabularyService.evaluateWordRankLabels(entry.getWordRanks());
                            req.setUserWord(userWordMaybe);
                            req.setRankLabels(labels);

                            showDict(req);
                        },
                        e -> {
                            e.printStackTrace();
                            Toast.makeText(this, "Fail.", Toast.LENGTH_SHORT).show();
                        },
                        () -> Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show());

    }

    protected DictUIRequest prepareUIRequest(DictUIRequest request) {

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

    protected abstract void showDict(DictUIRequest request);

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
