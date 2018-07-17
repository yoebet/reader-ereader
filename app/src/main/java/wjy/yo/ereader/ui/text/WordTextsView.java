package wjy.yo.ereader.ui.text;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.databinding.WordTextBinding;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.service.TextSearchService;
import wjy.yo.ereader.util.ExceptionHandlers;

public class WordTextsView {

    private TextSearchService textSearchService;

    private WordTextBinding binding;

    private String word;

    private List<Para> paras;

    private int currentIndex;

    public WordTextsView(TextSearchService textSearchService, WordTextBinding binding) {
        this.textSearchService = textSearchService;
        this.binding = binding;
    }

    public void resetWord(final String word) {
        this.word = word;
        this.paras = null;
        binding.setWord(word);
        binding.setContent(null);

        Disposable disp = textSearchService.search(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((List<Para> paras) -> {
                    if (!word.equals(this.word)) {
                        return;
                    }
                    this.paras = paras;
                    setCurrentText(0);
                }, ExceptionHandlers::handle);
    }

    private void setCurrentText(int index) {
        if (paras == null) {
            return;
        }
        if (index < 0 || index >= paras.size()) {
            return;
        }
        currentIndex = index;
        Para currentPara = paras.get(currentIndex);
        binding.setContent(currentPara.getContent());
    }

    private void nextText() {
        setCurrentText(currentIndex + 1);
    }

    private void previousText() {
        setCurrentText(currentIndex - 1);
    }

}
