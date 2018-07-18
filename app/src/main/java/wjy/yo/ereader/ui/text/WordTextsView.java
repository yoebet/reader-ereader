package wjy.yo.ereader.ui.text;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.databinding.WordTextBinding;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.service.TextSearchService;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.TextSearchResult;
import wjy.yo.ereader.vo.TextSearchResult.ResultItem;

public class WordTextsView {

    private TextSearchService textSearchService;

    private WordTextBinding binding;

    private String word;

    private List<ResultItem> resultItems;

    private int currentIndex;

    public WordTextsView(TextSearchService textSearchService, WordTextBinding binding) {
        this.textSearchService = textSearchService;
        this.binding = binding;
    }

    public void resetWord(final String word) {
        this.word = word;
        this.resultItems = null;

        binding.setWord(word);
        binding.setPara(null);
        binding.setChap(null);
        binding.setBook(null);

        Disposable disp = textSearchService.search(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((TextSearchResult searchResult) -> {
                    if (!word.equals(this.word)) {
                        return;
                    }
                    this.resultItems = searchResult.getResultItems();
                    setCurrentText(0);
                }, ExceptionHandlers::handle);
    }

    private void setCurrentText(int index) {
        if (resultItems == null) {
            return;
        }
        if (index < 0 || index >= resultItems.size()) {
            return;
        }
        currentIndex = index;
        binding.setHasNext(currentIndex < resultItems.size() - 1);
        binding.setHasPrevious(currentIndex > 0);
        ResultItem resultItem = resultItems.get(currentIndex);
        Para para = resultItem.getPara();
        binding.setPara(para);
    }

    private void nextText() {
        setCurrentText(currentIndex + 1);
    }

    private void previousText() {
        setCurrentText(currentIndex - 1);
    }

}
