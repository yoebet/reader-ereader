package wjy.yo.ereader.ui.dict;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

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
import wjy.yo.ereader.ui.common.FlowLayout;

public class DictView {
    private Context context;

    private DictCenterBinding binding;

    private MeaningItemRecyclerViewAdapter meaningItemAdapter;

    private DictRequest dictUIRequest;

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

        return binding;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private void resetUserWord(Maybe<UserWord> userWordMaybe) {
        binding.setUserWord(null);
        if (userWordMaybe == null) {
            return;
        }
        if (userWordDisp != null) {
            userWordDisp.dispose();
        }
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

        if (rankLabelsDisp != null) {
            rankLabelsDisp.dispose();
        }
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
        dictUIRequest.agent.requestDict(word);
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
        if (bvcDisp != null) {
            bvcDisp.dispose();
        }
        bvcDisp = uv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(binding::setBvCategory);
    }

    public void renderDict(DictRequest request) {
        dictUIRequest = request;
        DictEntry entry = request.entry;
        binding.setEntry(entry);
        resetRankLabels(request.getRankLabels());
        resetRefWords(request.getRefWords());
        resetUserWord(request.getUserWord());
        resetBaseVocabularyCategory(request.getBaseVocabularyCategory());
        meaningItemAdapter.resetList(entry.getMeaningItems());
    }

    public void clear() {
        if (userWordDisp != null) {
            userWordDisp.dispose();
        }
        if (rankLabelsDisp != null) {
            rankLabelsDisp.dispose();
        }
        if (bvcDisp != null) {
            bvcDisp.dispose();
        }
    }
}
