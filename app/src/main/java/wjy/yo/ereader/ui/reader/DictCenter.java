package wjy.yo.ereader.ui.reader;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.UserWordService;

public class DictCenter {

    private DictService dictService;

    private UserWordService userWordService;

    private String currentWord;

    DictCenter(DictService dictService, UserWordService userWordService) {
        this.dictService = dictService;
        this.userWordService = userWordService;
    }

    void requestDict(String word) {
        if (word == null || "".equals(word.trim())) {
            return;
        }
        if (word.equals(currentWord)) {
            return;
        }
        currentWord = word;

        Schedulers.io().scheduleDirect(() -> {
            Disposable disp = userWordService.getOne(word).subscribe(
                    userWord -> {
                        int fami = userWord.getFamiliarity();
                        System.out.println("Familiarity: " + fami);
                        if (fami == UserWord.FamiliarityHighest) {
                            userWordService.remove(word);
                        } else {
                            fami++;
                            userWordService.update(word, fami);
                        }
                    },
                    Throwable::printStackTrace,
                    () -> {
                        System.out.println("Not User Word");
                        UserWord uw=new UserWord(word);
                        uw.setParaId("zzzzz");
                        userWordService.add(uw);
                    });
        });

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
