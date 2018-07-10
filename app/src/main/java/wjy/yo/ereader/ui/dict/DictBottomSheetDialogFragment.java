package wjy.yo.ereader.ui.dict;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetBehavior.BottomSheetCallback;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
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
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.ui.common.FlowLayout;

public class DictBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Dialog dialog;
    private DictCenterBinding binding;
    private Context context;

    private BottomSheetBehavior bottomSheetBehavior;

    private MeaningItemRecyclerViewAdapter meaningItemAdapter;

    private BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    private DictUIRequest dictUIRequest;

    private Disposable userWordDisp;
    private Disposable rankLabelsDisp;


    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        System.out.println("onAttach " + context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("onCreateDialog " + dialog);
        if (dialog != null) {
            return dialog;
        }

        dialog = super.onCreateDialog(savedInstanceState);

        context = getContext();
        binding = DataBindingUtil
                .inflate(LayoutInflater.from(context), R.layout.dict_center,
                        null, false);

        View rootView = binding.getRoot();
        dialog.setContentView(rootView);

        RecyclerView meaningItemsRecycle = binding.meaningItems;
        meaningItemAdapter = new MeaningItemRecyclerViewAdapter();
        meaningItemsRecycle.setAdapter(meaningItemAdapter);

        setBehaviorCallback();

        if (dictUIRequest != null) {
            resetDictUI(dictUIRequest);
            dictUIRequest = null;
        }

        return dialog;
    }

    private void setBehaviorCallback() {
        View rootView = binding.getRoot();
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) rootView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            bottomSheetBehavior = (BottomSheetBehavior) behavior;
            bottomSheetBehavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
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
        }
        layout.setVisibility(View.VISIBLE);
    }

    public void setDictUIRequest(DictUIRequest request) {
        if (binding == null) {
            this.dictUIRequest = request;
            return;
        }
        resetDictUI(request);
    }

    private void resetDictUI(DictUIRequest request) {
        DictEntry entry = request.entry;
        binding.setEntry(entry);
        resetRankLabels(request.getRankLabels());
        resetRefWords(request.getRefWords());
        resetUserWord(request.getUserWord());
        meaningItemAdapter.resetList(entry.getMeaningItems());
    }

    public int getBottomSheetState() {
        if (bottomSheetBehavior == null) {
            System.out.println("bottomSheetBehavior is null.");
            return BottomSheetBehavior.STATE_HIDDEN;
        }
        return bottomSheetBehavior.getState();
    }
}
