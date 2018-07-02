package wjy.yo.ereader.ui.vocabulary;

import android.view.View;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.VocabularyGroupedBinding;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;
import wjy.yo.ereader.vo.GroupedUserWords;

public class GroupedUserWordsRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<GroupedUserWords, VocabularyGroupedBinding> {


    @Override
    protected void doOnCreateViewHolder(VocabularyGroupedBinding binding) {
        binding.getRoot().setOnClickListener((View view) -> {
            GroupedUserWords grouped = (GroupedUserWords) view.getTag();
            if (grouped == null) {
                return;
            }
            //
        });
    }

    GroupedUserWordsRecyclerViewAdapter() {
        super(R.layout.vocabulary_grouped, VocabularyGroupedBinding::setGrouped);
    }

}
