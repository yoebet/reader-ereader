package wjy.yo.ereader.ui.text;

import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import wjy.yo.ereader.R;
import wjy.yo.ereader.BR;
import wjy.yo.ereader.databinding.WordTextBinding;
import wjy.yo.ereader.vo.TextSearchResult;
import wjy.yo.ereader.vo.TextSearchResult.ResultItem;


public class WordTextPagerAdapter extends PagerAdapter {

    private ViewPager viewPager;

    private TextProfile textProfile;

    private TextSearchResult searchResult;

    private int count;

    private WordTextBinding[] bindings;

    public WordTextPagerAdapter(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public void setTextProfile(TextProfile textProfile) {
        this.textProfile = textProfile;

        textProfile.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                System.out.println(sender + " change -> " + propertyId);

                if (propertyId == BR.showTrans) {
                    System.out.println("isShowTrans -> " + textProfile.isShowTrans());
                } else if (propertyId == BR.showChapTitle) {
                    System.out.println("isShowChapTitle -> " + textProfile.isShowChapTitle());
                }
            }
        });
    }

    private void onTextProfileChanged(Observable sender, int propertyId) {

    }


    public void setSearchResult(TextSearchResult searchResult) {
        this.searchResult = searchResult;
        if (searchResult == null || searchResult.getResultItems() == null) {
            count = 0;
        } else {
            count = searchResult.getResultItems().size();
        }
        bindings = new WordTextBinding[count];
    }


    public int getItemPosition(@NonNull Object object) {
//        System.out.println("getItemPosition: " + object);
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return object == view;
    }

    private void setupEvent(WordTextBinding binding, ResultItem resultItem, int position) {
        View view = binding.getRoot();
        view.findViewById(R.id.next_text).setOnClickListener(v -> {
            if (position < count - 1) {
                viewPager.setCurrentItem(position + 1);
            }
        });
        view.findViewById(R.id.previous_text).setOnClickListener(v -> {
            if (position > 0) {
                viewPager.setCurrentItem(position - 1);
            }
        });

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        System.out.println("getItem: " + position);
        WordTextBinding binding = bindings[position];
        if (binding != null) {
//            System.out.println("getItem: " + position + ", hit.");
            container.addView(binding.getRoot());
            return binding.getRoot();
        }

        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        binding = WordTextBinding.inflate(inflater, container, false);

        bindings[position] = binding;

//        String word = searchResult.getKeyword();
//        binding.setWord(word);
        binding.setCurrentIndex(position);
        binding.setTotal(count);
        binding.setTextProfile(textProfile);

        ResultItem resultItem = searchResult.getResultItems().get(position);
        binding.setPara(resultItem.getPara());
//      System.out.println("text: " + resultItem.getPara().getContent());
        setupEvent(binding, resultItem, position);

        View view = binding.getRoot();
//        view.setTag(word);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        System.out.println("destroyItem: " + position);
        View view = (View) object;
        container.removeView(view);
    }
}
