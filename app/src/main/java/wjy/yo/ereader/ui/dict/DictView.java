package wjy.yo.ereader.ui.dict;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;
import wjy.yo.ereader.di.AppInjector;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.TextSearchService;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.ui.common.FlowLayout;
import wjy.yo.ereader.ui.text.PopupWindowManager;
import wjy.yo.ereader.ui.text.TextProfile;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.OperationResult;
import wjy.yo.ereader.vo.TextSearchResult;
import wjy.yo.ereader.vo.WordContext;

public class DictView {

    private Context context;

    private PopupWindowManager popupWindowManager;

    @Inject
    UserWordService userWordService;

    @Inject
    TextSearchService textSearchService;

    private DictCenterBinding binding;

    private MeaningItemRecyclerViewAdapter meaningItemAdapter;

    private WordTextPagerAdapter pagerAdapter;

    private DictRequest dictRequest;

    private Stack<String> dictWordStack = new Stack<>();
    private boolean gobackFlag;

    private Disposable userWordDisp;
    private Disposable rankLabelsDisp;
    private Disposable bvcDisp;
    private Disposable textSearchDisp;

    public DictView(PopupWindowManager pwm) {
        this.popupWindowManager = pwm;
        AppInjector.getAppComponent().inject(this);
    }

    public DictCenterBinding build(Context context) {
        DictCenterBinding binding = DictCenterBinding.inflate(LayoutInflater.from(context), null, false);
        build(context, binding);
        return binding;
    }

    public void build(Context context, DictCenterBinding binding) {
        this.context = context;
        this.binding = binding;

        RecyclerView meaningItemsRecycle = binding.meaningItems;
        meaningItemAdapter = new MeaningItemRecyclerViewAdapter();
        meaningItemsRecycle.setAdapter(meaningItemAdapter);

        WordTextViewPager textViewPager = binding.textViewPager;
        TextProfile textProfile = new TextProfile();
        binding.setTextProfile(textProfile);
        pagerAdapter = new WordTextPagerAdapter(textViewPager, popupWindowManager);
//        textProfile.setShowTrans(true);
        pagerAdapter.setTextProfile(textProfile);

        textViewPager.setOffscreenPageLimit(2);
        textViewPager.setAdapter(pagerAdapter);

        setupEvents();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @SuppressLint("CheckResult")
    private void addToVocabulary(View v) {

        DictEntry entry = binding.getEntry();
        final String word = entry.getWord();

        UserWord uw = new UserWord(word);
        WordContext wc = dictRequest.getWordContext();
        uw.setWordContext(wc);

        userWordService.add(uw)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((OperationResult result) -> {
                    if (binding.getEntry() != entry) {
                        return;
                    }

                    if (result.isSuccess()) {
                        binding.setUserWord(uw);
                    }
                }, ExceptionHandlers::handle);
    }

    @SuppressLint("CheckResult")
    private void doSetFamiliarity(UserWord uw, int setTo) {
        userWordService.update(uw.getWord(), setTo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((OperationResult result) -> {
                    if (binding.getUserWord() != uw) {
                        return;
                    }

                    if (result.isSuccess()) {
                        uw.setFamiliarity(setTo);
                        binding.setUserWord(uw);
                    }
                }, ExceptionHandlers::handle);
    }

    private void decreaseFamiliarity(View v) {
        UserWord uw = binding.getUserWord();
        if (uw == null) {
            return;
        }

        int familiarity = uw.getFamiliarity();
        if (familiarity <= UserWord.FamiliarityLowest) {
            return;
        }

        doSetFamiliarity(uw, familiarity - 1);
    }

    private void increaseFamiliarity(View v) {
        UserWord uw = binding.getUserWord();
        if (uw == null) {
            return;
        }

        int familiarity = uw.getFamiliarity();
        if (familiarity >= UserWord.FamiliarityHighest) {
            return;
        }

        doSetFamiliarity(uw, familiarity + 1);
    }

    @SuppressLint("CheckResult")
    private void removeFromVocabulary(UserWord uw) {

        userWordService.remove(uw.getWord())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((OperationResult result) -> {
                    if (binding.getUserWord() != uw) {
                        return;
                    }

                    if (result.isSuccess()) {
                        binding.setUserWord(null);
                    }
                }, ExceptionHandlers::handle);
    }

    @SuppressLint("CheckResult")
    private void clickRemoveFromVocabulary(View v) {

        UserWord uw = binding.getUserWord();
        if (uw == null) {
            return;
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle(uw.getWord())
                .setMessage("要从我的词汇中移除吗？")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    removeFromVocabulary(uw);
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> {
                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void goback(View v) {
        if (dictWordStack.empty()) {
            return;
        }
        if (dictRequest == null) {
            return;
        }
        String lastWord = dictWordStack.pop();
        gobackFlag = true;
        dictRequest.agent.requestDict(lastWord, dictRequest.getWordContext());
    }

    private void setupEvents() {
        binding.addToVocabulary.setOnClickListener(this::addToVocabulary);
        binding.familiarityDecrease.setOnClickListener(this::decreaseFamiliarity);
        binding.familiarityIncrease.setOnClickListener(this::increaseFamiliarity);
        binding.removeFromVocabulary.setOnClickListener(this::clickRemoveFromVocabulary);

        binding.goback.setOnClickListener(this::goback);
    }

    private void resetUserWord(Maybe<UserWord> userWordMaybe) {
        binding.setUserWord(null);
        if (userWordMaybe == null) {
            return;
        }
        ensureDispose(userWordDisp);
        userWordDisp = userWordMaybe
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        binding::setUserWord,
                        ExceptionHandlers::handle);
    }

    private void resetRankLabels(Single<List<String>> labelsSingle) {
        FlowLayout layout = binding.categories;
        layout.removeAllViews();

        ensureDispose(rankLabelsDisp);
        rankLabelsDisp = labelsSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(labels -> {
                    if (labels == null || labels.size() == 0) {
                        layout.setVisibility(View.GONE);
                        return;
                    }

                    LayoutInflater inflater = LayoutInflater.from(context);
                    for (String label : labels) {
                        TextView tv = (TextView) inflater.inflate(R.layout.dict_word_rank, null);
                        tv.setText(label);
                        tv.setTag(label);
                        layout.addView(tv);
                    }
                    layout.setVisibility(View.VISIBLE);
                });
    }

    private void onRefWordClick(View v) {
        if (!(v instanceof TextView)) {
            return;
        }
        String word = (String) v.getTag();
        dictRequest.agent.requestDict(word, dictRequest.getWordContext());
    }

    private void resetRefWords(List<String> refWords) {
        FlowLayout layout = binding.refWords;
        layout.removeAllViews();
        boolean hasRefWords = refWords != null && refWords.size() > 0;
        binding.setHasRefWords(hasRefWords);
        if (!hasRefWords) {
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        for (String word : refWords) {
            TextView tv = (TextView) inflater.inflate(R.layout.dict_ref_word, null);
            tv.setText(word);
            tv.setTag(word);
            layout.addView(tv);

            tv.setOnClickListener(this::onRefWordClick);
        }
    }

    private void resetBaseVocabularyCategory(Maybe<WordCategory> uv) {
        binding.setBvCategory(null);
        if (uv == null) {
            return;
        }
        ensureDispose(bvcDisp);
        bvcDisp = uv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding::setBvCategory, ExceptionHandlers::handle);
    }

    private void resetTextViewPager() {

        pagerAdapter.setSearchResult(null);
        pagerAdapter.notifyDataSetChanged();

        final String word = binding.getEntry().getWord();
        ensureDispose(textSearchDisp);
        textSearchDisp = textSearchService.search(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((TextSearchResult searchResult) -> {
                    DictEntry entry = binding.getEntry();
                    if (entry == null | !word.equals(entry.getWord())) {
                        return;
                    }

                    List<String> words = new ArrayList<>();
                    words.add(entry.getWord());
                    String[] forms = entry.getForms();
                    if (forms != null) {
                        for (String form : forms) {
                            if ("".equals(form)) {
                                continue;
                            }
                            words.add(form);
                        }
                    }
                    searchResult.setHighlightWords(words);

                    pagerAdapter.setDictAgent(dictRequest.agent);
                    pagerAdapter.setSearchResult(searchResult);
                    pagerAdapter.notifyDataSetChanged();
                }, ExceptionHandlers::handle);
    }

    public void renderDict(DictRequest request) {
        if (dictRequest != null) {
            dictRequest.callCloseAction();
            if (!gobackFlag) {
                String lastWord = dictRequest.entry.getWord();
                if (!lastWord.equals(request.entry.getWord())) {
                    dictWordStack.push(lastWord);
                    binding.setHasPrevious(true);
                }
            }
        }
        if (gobackFlag) {
            binding.setHasPrevious(!dictWordStack.empty());
            gobackFlag = false;
        }
        dictRequest = request;
        DictEntry entry = request.entry;
        binding.setEntry(entry);
        resetRankLabels(request.getRankLabels());
        resetRefWords(request.getRefWords());
        resetUserWord(request.getUserWord());
        resetBaseVocabularyCategory(request.getBaseVocabularyCategory());
        meaningItemAdapter.resetList(entry.getMeaningItems());
        resetTextViewPager();

        request.callOnOpenAction();
    }

    public void clear() {
        ensureDispose(userWordDisp);
        ensureDispose(rankLabelsDisp);
        ensureDispose(bvcDisp);
        ensureDispose(textSearchDisp);
        if (pagerAdapter != null) {
            pagerAdapter.clear();
        }
        dictRequest = null;
        dictWordStack.clear();
        binding.setHasPrevious(false);
    }

    private void ensureDispose(Disposable disp) {
        if (disp != null && !disp.isDisposed()) {
            disp.dispose();
        }
    }
}
