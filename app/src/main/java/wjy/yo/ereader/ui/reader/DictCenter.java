package wjy.yo.ereader.ui.reader;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;

public class DictCenter {

    private DictService dictService;

    private Disposable lastDisp;

    private String word;

    DictCenter(DictService dictService) {
        this.dictService = dictService;
    }

    void requestDict(String word) {
        if (word == null || "".equals(word.trim())) {
            return;
        }
        if (word.equals(this.word)) {
            return;
        }
        this.word = word;
        if (lastDisp != null && !lastDisp.isDisposed()) {
            lastDisp.dispose();
        }
        lastDisp = dictService.lookup(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((DictEntry e) -> {
                    System.out.println("got: " + e.getWord());
                    List<MeaningItem> meaningItems = e.getMeaningItems();
                    for (MeaningItem mi : meaningItems) {
                        System.out.println(mi.getPos() + " " + mi.getExp());
                    }
                    // show ...
                });
    }

    void hideDict() {
        // ...
        if (lastDisp != null && !lastDisp.isDisposed()) {
            lastDisp.dispose();
        }
    }

}
