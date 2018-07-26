package wjy.yo.ereader.ui.dict.support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Selection;
import android.text.Spannable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.dict.DictRequest;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.WordContext;

import static wjy.yo.ereader.util.Constants.DICT_CURRENT_WORD;

public abstract class DictAgentActivity extends AppCompatActivity implements DictAgent {

    @Inject
    protected DictService dictService;

    @Inject
    protected UserWordService userWordService;

    @Inject
    protected VocabularyService vocabularyService;

    protected PopupWindowManager popupWindowManager;

    protected String currentWord;
    private DictRequest currentDictRequest;

    private Disposable dictDisp;


//    public String getCurrentWord() {
//        return currentWord;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popupWindowManager = new PopupWindowManager();
    }

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


    // TODO: return Maybe<PopupWindow>
    public void requestDictPopup(String word, View anchor, int offsetX, int offsetY, PopupWindowManager pwm) {

        // TODO: lookup word, dict view
        LayoutInflater li = getLayoutInflater();

        View contentView = li.inflate(R.layout.popup_window, null);
        TextView titleView = contentView.findViewById(R.id.cword);
        titleView.setText(word);

        popupWindow(contentView, anchor, offsetX, offsetY, pwm);
    }

    private void popupWindow(View contentView, View anchor, int offsetX, int offsetY, PopupWindowManager pwm) {

        if (pwm.getCurrentPopup() != null) {
            pwm.getCurrentPopup().dismiss();
        }

        PopupWindow pw = new PopupWindow(contentView);
        pwm.setCurrentPopup(pw);

        pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchor, offsetX, offsetY);

        pw.setOnDismissListener(() -> {
            if (anchor instanceof TextView) {
                TextView tv = (TextView) anchor;
                CharSequence cs = tv.getText();
                if (cs instanceof Spannable) {
                    Spannable sp = (Spannable) cs;
                    Selection.removeSelection(sp);
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (currentWord != null) {
            outState.putString(DICT_CURRENT_WORD, currentWord);
        }
    }

    @Override
    protected void onDestroy() {
//        System.out.println("onDestroy");
        if (popupWindowManager != null) {
            popupWindowManager.clear();
            popupWindowManager = null;
        }
        super.onDestroy();
    }

    protected boolean closePopupIfAny() {
        if (popupWindowManager != null && popupWindowManager.anyPopup()) {
            popupWindowManager.clear();
            return true;
        }
        return false;
    }

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
