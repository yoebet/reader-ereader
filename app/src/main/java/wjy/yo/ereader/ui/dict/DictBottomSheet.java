package wjy.yo.ereader.ui.dict;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.entity.dict.MeaningItem;
import wjy.yo.ereader.entity.userdata.UserWord;
import wjy.yo.ereader.entityvo.dict.DictEntry;
import wjy.yo.ereader.service.DictService;
import wjy.yo.ereader.service.UserWordService;

public class DictBottomSheet implements DictUI {

    private DictService dictService;

    private UserWordService userWordService;

    private AppCompatActivity activity;

    private DictBottomSheetDialogFragment fragment;

    private String currentWord;

    private Disposable disp;

    private final static DictEntry DictEntryNotFound = new DictEntry();
    private final static UserWord UserWordNotFound = new UserWord("");

    public DictBottomSheet(DictService dictService, UserWordService userWordService,
                           AppCompatActivity activity, DictBottomSheetDialogFragment fragment) {
        this.dictService = dictService;
        this.userWordService = userWordService;
        this.activity = activity;
        this.fragment = fragment;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void requestDict(String word) {
        if (word == null || "".equals(word.trim())) {
            return;
        }
        if (word.equals(currentWord)) {
            return;
        }
        currentWord = word;

        dispose();

        Single<DictEntry> sde = dictService.lookup(word).toSingle(DictEntryNotFound);
        Single<UserWord> suw = userWordService.getOne(word).toSingle(UserWordNotFound);
        disp = Observable.combineLatest(
                sde.toObservable(),
                suw.toObservable(),
                (de, uw) -> {
                    if (de == DictEntryNotFound) {
                        de = null;
                    } else {
                        System.out.println("got: " + de.getWord());
                        List<MeaningItem> meaningItems = de.getMeaningItems();
                        for (MeaningItem mi : meaningItems) {
                            System.out.println(mi.getPos() + " " + mi.getExp());
                        }
                    }
                    if (uw == UserWordNotFound) {
                        uw = null;
                    } else {
                        int fami = uw.getFamiliarity();
                        System.out.println("Familiarity: " + fami);
                    }
                    return new DictUIRequest(de, uw);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        request -> {
                            if (!word.equals(currentWord)) {
                                return;
                            }
                            if (request.entry == null) {
                                Toast.makeText(activity, "Not Found", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            fragment.setDictUIRequest(request);
                            FragmentManager fm = activity.getSupportFragmentManager();
                            fragment.show(fm, word);
                        },
                        e -> Toast.makeText(activity, "Fail.", Toast.LENGTH_SHORT).show());

    }

    void dispose() {
        if (disp != null) {
            disp.dispose();
            disp = null;
        }
    }

    void hideDict() {
//        currentWord = null;
        // ...
    }

}
