package wjy.yo.ereader.ui.dict.support;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.AnnosPopupBinding;
import wjy.yo.ereader.databinding.DictPopupBinding;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.ui.dict.DictAgent;
import wjy.yo.ereader.ui.dict.DictRequest;
import wjy.yo.ereader.ui.dict.MeaningItemRecyclerViewAdapter;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.TextAnnos;
import wjy.yo.ereader.util.Action;
import wjy.yo.ereader.util.Consumer;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.util.Offset;
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
    protected DictRequest currentDictRequest;

    private Disposable dictDisp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        popupWindowManager = new PopupWindowManager();
    }

    public void requestDict(final String word,
                            WordContext wordContext) {
        requestDict(word, wordContext, null, null);
    }

    public void requestDict(final String word,
                            WordContext wordContext,
                            Action onOpen,
                            Action onClose) {
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
                    req.setOnOpen(onOpen);
                    req.setOnClose(onClose);
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


    public void requestDictPopup(String word,
                                 WordContext wordContext,
                                 View anchor,
                                 Offset offset,
                                 PopupWindowManager pwm,
                                 Consumer<PopupWindow> onPopup,
                                 PopupWindow.OnDismissListener onDismiss) {

        PopupWindowManager pwm2 = (pwm == null) ? popupWindowManager : pwm;
        clear();
        dictDisp = dictService.lookup(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        entry -> {
                            LayoutInflater li = getLayoutInflater();
                            DictPopupBinding binding = DictPopupBinding.inflate(li, null, false);
                            binding.setEntry(entry);

                            RecyclerView meaningItemsRecycle = binding.meaningItems;
                            MeaningItemRecyclerViewAdapter adapter = new MeaningItemRecyclerViewAdapter();
                            meaningItemsRecycle.setAdapter(adapter);
                            adapter.resetList(entry.getMeaningItems());

                            PopupWindow pw = new PopupWindow(binding.getRoot());
                            showPopup(pw, anchor, offset, pwm2, onPopup, onDismiss);

                            binding.lookup.setOnClickListener(v -> {
                                requestDict(word, wordContext, pwm2::clear, null);
                            });
                        },
                        ExceptionHandlers::handle,
                        () -> Toast.makeText(this, "Not Found", Toast.LENGTH_SHORT).show());

    }

    public void requestAnnosPopup(TextAnnos textAnnos,
                                  View anchor,
                                  Offset offset,
                                  PopupWindowManager pwm,
                                  Consumer<PopupWindow> onPopup,
                                  PopupWindow.OnDismissListener onDismiss) {

        LayoutInflater li = getLayoutInflater();
        AnnosPopupBinding binding = AnnosPopupBinding.inflate(li, null, false);
        binding.setAnnos(textAnnos);

        ViewGroup als = binding.annoLabels;

        List<String> labels = textAnnos.getAnnoLabels();
        if (labels == null || labels.size() == 0) {
            als.setVisibility(View.GONE);
        } else {
            LayoutInflater inflater = getLayoutInflater();
            for (String label : labels) {
                TextView tv = (TextView) inflater.inflate(R.layout.anno_label, null);
                tv.setText(label);
                als.addView(tv);
            }
        }

        AndroidSchedulers.mainThread().scheduleDirect(() -> {
            PopupWindow pw = new PopupWindow(binding.getRoot());
            showPopup(pw, anchor, offset, pwm, onPopup, onDismiss);
        }, 1L, TimeUnit.MICROSECONDS);

    }

    private void showPopup(PopupWindow pw,
                           View anchor,
                           Offset offset,
                           PopupWindowManager pwm,
                           Consumer<PopupWindow> onPopup,
                           PopupWindow.OnDismissListener onDismiss) {
        if (pwm == null) {
            pwm = popupWindowManager;
        }
        pwm.clear();
        pwm.setCurrentPopup(pw);
        pw.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(anchor, offset.x, offset.y);
        if (onPopup != null) {
            onPopup.accept(pw);
        }
        if (onDismiss != null) {
            pw.setOnDismissListener(onDismiss);
        }
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
