package wjy.yo.ereader.ui.dict;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;
import wjy.yo.ereader.entity.dict.WordCategory;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.ui.common.FlowLayout;
import wjy.yo.ereader.vo.OperationResult;
import wjy.yo.ereader.vo.WordContext;

public class DictView {

    private Context context;

    private UserWordService userWordService;

    private DictCenterBinding binding;

    private MeaningItemRecyclerViewAdapter meaningItemAdapter;

    private DictRequest dictRequest;

    private Disposable userWordDisp;
    private Disposable rankLabelsDisp;
    private Disposable bvcDisp;

    public DictCenterBinding build(Context context) {
        this.context = context;
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.dict_center,
                        null, false);

        RecyclerView meaningItemsRecycle = binding.meaningItems;
        meaningItemAdapter = new MeaningItemRecyclerViewAdapter();
        meaningItemsRecycle.setAdapter(meaningItemAdapter);

        setupEvents();

        return binding;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUserWordService(UserWordService userWordService) {
        this.userWordService = userWordService;
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
                });
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
                });
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
                });
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

    private void setupEvents() {
        binding.addToVocabulary.setOnClickListener(this::addToVocabulary);
        binding.familiarityDecrease.setOnClickListener(this::decreaseFamiliarity);
        binding.familiarityIncrease.setOnClickListener(this::increaseFamiliarity);
        binding.removeFromVocabulary.setOnClickListener(this::clickRemoveFromVocabulary);
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
                        Throwable::printStackTrace);
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
        if (refWords == null || refWords.size() == 0) {
            layout.setVisibility(View.GONE);
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
        layout.setVisibility(View.VISIBLE);
    }

    private void resetBaseVocabularyCategory(Maybe<WordCategory> uv) {
        if (uv == null) {
            binding.setBvCategory(null);
            return;
        }
        ensureDispose(bvcDisp);
        bvcDisp = uv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding::setBvCategory);
    }

    public void renderDict(DictRequest request) {
        dictRequest = request;
        DictEntry entry = request.entry;
        binding.setEntry(entry);
        resetRankLabels(request.getRankLabels());
        resetRefWords(request.getRefWords());
        resetUserWord(request.getUserWord());
        resetBaseVocabularyCategory(request.getBaseVocabularyCategory());
        meaningItemAdapter.resetList(entry.getMeaningItems());
    }

    private void ensureDispose(Disposable disp) {
        if (disp != null) {
            disp.dispose();
        }
    }

    public void clear() {
        ensureDispose(userWordDisp);
        ensureDispose(rankLabelsDisp);
        ensureDispose(bvcDisp);
    }
}
