package wjy.yo.ereader.ui.text;

import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.BR;
import wjy.yo.ereader.databinding.WordTextBinding;
import wjy.yo.ereader.di.AppInjector;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.entity.book.Chap;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.TextSearchResult;
import wjy.yo.ereader.vo.TextSearchResult.ResultItem;


public class WordTextPagerAdapter extends PagerAdapter {

    private WordTextViewPager viewPager;

    @Inject
    BookService bookService;

    @Inject
    BookContentService bookContentService;

    private TextProfile textProfile;

    private TextSearchResult searchResult;

    private int count;

    private WordTextBinding[] bindings;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private Map<String, Maybe<Book>> bookMaybeMap = new ArrayMap<>();

    private Map<String, Maybe<Chap>> chapMaybeMap = new ArrayMap<>();


    public WordTextPagerAdapter(WordTextViewPager viewPager) {
        this.viewPager = viewPager;

        AppInjector.getAppComponent().inject(this);

        /*viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                System.out.println("onPageSelected -> " + position);
            }
        });*/
    }

    public void setTextProfile(TextProfile textProfile) {
        this.textProfile = textProfile;

        textProfile.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (count == 0) {
                    return;
                }
                if (bindings == null) {
                    return;
                }

                if (propertyId == BR.showTrans) {
                    if (textProfile.isShowTrans()) {
                        for (int i = 0; i < bindings.length; i++) {
                            WordTextBinding binding = bindings[i];
                            if (binding == null) {
                                continue;
                            }
                            ResultItem resultItem = searchResult.getResultItems().get(i);
                            ensureTrans(binding, resultItem);
                        }
                    }
                } else if (propertyId == BR.showTitles) {
                    if (textProfile.isShowTitles()) {
                        for (int i = 0; i < bindings.length; i++) {
                            WordTextBinding binding = bindings[i];
                            if (binding == null) {
                                continue;
                            }
                            ResultItem resultItem = searchResult.getResultItems().get(i);
                            ensureBookTitle(binding, resultItem);
                            ensureChapTitle(binding, resultItem);
                        }
                    }
                }
            }
        });
    }

    public void setSearchResult(TextSearchResult searchResult) {
        this.searchResult = searchResult;

        if (searchResult == null || searchResult.getResultItems() == null) {
            count = 0;
        } else {
            count = searchResult.getResultItems().size();
        }

        String currentWord = null;
        if (searchResult != null) {
            currentWord = searchResult.getKeyword();
        }
        viewPager.setCurrentWord(currentWord);

        bindings = new WordTextBinding[count];
        mDisposable.clear();
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

    private void setupEvent(WordTextBinding binding, int position) {
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

    private void ensureTrans(WordTextBinding binding, ResultItem resultItem) {
        if (resultItem.isParaLoaded()) {
            return;
        }

        Para para = resultItem.getPara();
        String paraId = para.getId();
        Disposable disp = bookContentService.loadPara(paraId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        para1 -> {
                            resultItem.setPara(para1);
                            resultItem.setParaLoaded(true);
                            binding.setPara(para1);
                        },
                        ExceptionHandlers::handle,
                        () -> System.out.println("Para Not Found: " + paraId));
        mDisposable.add(disp);
    }

    private void ensureBookTitle(WordTextBinding binding, ResultItem resultItem) {
        if (resultItem.getBook() != null) {
            return;
        }

        Para para = resultItem.getPara();
        String bookId = para.getBookId();

        Maybe<Book> bookMaybe = bookMaybeMap.get(bookId);
        if (bookMaybe == null) {
            bookMaybe = bookService.loadBook(bookId).cache()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            bookMaybeMap.put(bookId, bookMaybe);
        }

        Disposable disp = bookMaybe
                .subscribe(
                        book -> {
                            resultItem.setBook(book);
                            binding.setBook(book);
                        },
                        ExceptionHandlers::handle,
                        () -> System.out.println("Book Not Found: " + bookId));
        mDisposable.add(disp);
    }

    private void ensureChapTitle(WordTextBinding binding, ResultItem resultItem) {
        if (resultItem.getChap() != null) {
            return;
        }

        Para para = resultItem.getPara();
        String chapId = para.getChapId();

        Maybe<Chap> chapMaybe = chapMaybeMap.get(chapId);
        if (chapMaybe == null) {
            chapMaybe = bookService.loadChap(chapId).cache()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            chapMaybeMap.put(chapId, chapMaybe);
        }
        Disposable disp = chapMaybe
                .subscribe(
                        chap -> {
                            resultItem.setChap(chap);
                            binding.setChap(chap);
                        },
                        ExceptionHandlers::handle,
                        () -> System.out.println("Chap Not Found: " + chapId));
        mDisposable.add(disp);
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
        binding.setCurrent(position);
        binding.setTotal(count);
        binding.setTextProfile(textProfile);

        ResultItem resultItem = searchResult.getResultItems().get(position);
        binding.setPara(resultItem.getPara());
        if (resultItem.getBook() != null) {
            binding.setBook(resultItem.getBook());
        }
        if (resultItem.getChap() != null) {
            binding.setChap(resultItem.getChap());
        }

        if (textProfile.isShowTrans()) {
            ensureTrans(binding, resultItem);
        }
        if (textProfile.isShowTitles()) {
            ensureBookTitle(binding, resultItem);
            ensureChapTitle(binding, resultItem);
        }

        setupEvent(binding, position);

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


    public void clear() {
        mDisposable.clear();
    }
}
