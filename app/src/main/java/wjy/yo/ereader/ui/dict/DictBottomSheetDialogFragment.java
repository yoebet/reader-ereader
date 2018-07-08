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

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictCenterBinding;
import wjy.yo.ereader.entity.dict.WordRank;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.ui.common.FlowLayout;

public class DictBottomSheetDialogFragment extends BottomSheetDialogFragment {

    private Dialog dialog;
    private DictCenterBinding binding;
    private Context context;

    private DictUIRequest dictUIRequest;

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

    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("On onCreateDialog " + dialog);
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

    private void resetWordTags(DictEntry entry) {
        FlowLayout categories = binding.categories;
        categories.removeAllViews();

        LayoutInflater inflater = LayoutInflater.from(context);
        List<WordRank> wordRanks = entry.getWordRanks();
        if (wordRanks != null) {
            for (WordRank wr : wordRanks) {
                TextView tv = (TextView) inflater.inflate(R.layout.dict_word_rank, null);
                tv.setText(wr.getName() + "-" + wr.getRank());
                tv.setTag(wr);
                categories.addView(tv);
            }
        }
    }

    public void setDictUIRequest(DictUIRequest dictUIRequest) {
        if (binding == null) {
            this.dictUIRequest = dictUIRequest;
            return;
        }
        resetDictUI(dictUIRequest);
    }

    private void resetDictUI(DictUIRequest dictUIRequest) {
        DictEntry entry = dictUIRequest.entry;
        binding.setEntry(entry);
        resetWordTags(entry);
        binding.setUserWord(dictUIRequest.userWord);
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
