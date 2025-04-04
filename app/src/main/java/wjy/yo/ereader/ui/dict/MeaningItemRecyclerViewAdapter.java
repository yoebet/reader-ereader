package wjy.yo.ereader.ui.dict;

import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.DictMeaningItemBinding;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.ui.common.DataBoundRecyclerViewAdapter;

public class MeaningItemRecyclerViewAdapter
        extends DataBoundRecyclerViewAdapter<MeaningItem, DictMeaningItemBinding> {

    public MeaningItemRecyclerViewAdapter() {
        super(R.layout.dict_meaning_item, DictMeaningItemBinding::setItem);
    }

}
