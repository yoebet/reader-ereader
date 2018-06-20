package wjy.yo.ereader.ui.reader;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;

public class DictCenter {

    private DictService dictService;

    private String currentWord;

    DictCenter(DictService dictService) {
        this.dictService = dictService;
    }

    void requestDict(String word) {
        if (word == null || "".equals(word.trim())) {
            return;
        }
        if (word.equals(currentWord)) {
            return;
        }
        currentWord = word;
        Maybe<DictEntry> mde = dictService.lookup(word);
        mde.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DictEntryObserver(currentWord));
    }

    void hideDict() {
        currentWord = null;
        // ...
    }

    static class DictEntryObserver implements MaybeObserver<DictEntry> {

        private String word;

        DictEntryObserver(String word) {
            this.word = word;
        }

        @Override
        public void onSubscribe(Disposable d) {
        }

        @Override
        public void onSuccess(DictEntry e) {
            System.out.println("got: " + e.getWord());
            List<MeaningItem> meaningItems = e.getMeaningItems();
            for (MeaningItem mi : meaningItems) {
                System.out.println(mi.getPos() + " " + mi.getExp());
            }
            // show ...
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onComplete() {

            System.out.println("Not Found: " + word);
        }
    }

}
