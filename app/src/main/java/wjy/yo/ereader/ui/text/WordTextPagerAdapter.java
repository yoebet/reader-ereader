package wjy.yo.ereader.ui.text;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjy.yo.ereader.databinding.WordTextBinding;
import wjy.yo.ereader.vo.TextSearchResult;
import wjy.yo.ereader.vo.TextSearchResult.ResultItem;

public class WordTextPagerAdapter extends PagerAdapter {

    private TextSearchResult searchResult;

    public void setSearchResult(TextSearchResult searchResult) {
        this.searchResult = searchResult;
    }


    public int getItemPosition(@NonNull Object object) {
        System.out.println("getItemPosition: " + object);
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (searchResult == null || searchResult.getResultItems() == null) {
            return 0;
        }
        return searchResult.getResultItems().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        WordTextBinding binding = WordTextBinding.inflate(inflater, container, false);

        System.out.println("getItem: " + position);

        binding.setWord(searchResult.getKeyword());
        ResultItem resultItem = searchResult.getResultItems().get(position);
        if (resultItem != null) {
            binding.setPara(resultItem.getPara());
//            System.out.println("text: " + resultItem.getPara().getContent());
        }

        View view = binding.getRoot();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
