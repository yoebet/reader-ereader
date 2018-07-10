package wjy.yo.ereader.ui.reader;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.databinding.ActivityReaderBinding;
import wjy.yo.ereader.databinding.ReaderDrawerHeaderBinding;
import wjy.yo.ereader.entity.book.Para;
import wjy.yo.ereader.entityvo.book.ChapDetail;
import wjy.yo.ereader.service.AnnotationService;
import wjy.yo.ereader.service.BookContentService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.VocabularyService;
import wjy.yo.ereader.ui.dict.DictBottomSheetActivity;

import static wjy.yo.ereader.util.Constants.CHAP_ID_KEY;

public class ReaderActivity extends DictBottomSheetActivity {

    @Inject
    VocabularyService vocabularyService;

    @Inject
    BookService bookService;

    @Inject
    BookContentService bookContentService;

    @Inject
    AnnotationService annotationService;

    private String chapId;

    private PopupWindowManager pwm;

    private DrawerLayout drawerLayout;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chapId = getIntent().getStringExtra(CHAP_ID_KEY);
        if (chapId == null && savedInstanceState != null) {
            chapId = savedInstanceState.getString(CHAP_ID_KEY);
        }

        ActivityReaderBinding binding = DataBindingUtil
                .inflate(getLayoutInflater(), R.layout.activity_reader,
                        null, false);

        drawerLayout = binding.drawerLayout;

        NavigationView nv = binding.navView;
        ReaderDrawerHeaderBinding drawerBinding = ReaderDrawerHeaderBinding.bind(nv.getHeaderView(0));

        setContentView(binding.getRoot());

        pwm = new PopupWindowManager();

        RecyclerView recyclerView = binding.paraList;
        ParaRecyclerViewAdapter adapter = new ParaRecyclerViewAdapter(dictService, userWordService,
                vocabularyService, pwm, this);
        recyclerView.setAdapter(adapter);

        Disposable disposable = bookContentService.loadChapDetail(chapId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((ChapDetail chap) -> {

                    if (chap == null) {
                        return;
                    }
                    if (chapId != null && !chapId.equals(chap.getId())) {
                        return;
                    }
                    System.out.println("chap: " + chap);

                    bookService.loadBook(chap.getBookId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(book -> {
                                System.out.println("book: " + book);
                                /*annotationService.loadAnnotations(book.getAnnotationFamilyId())
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(System.out::println);*/
                            });

                    drawerBinding.setChap(chap);

                    List<Para> paras = chap.getParas();
                    if (paras != null) {
                        adapter.resetList(paras);
                    }
                }, Throwable::printStackTrace);
        mDisposable.add(disposable);

//        View pv=getLayoutInflater().inflate(R.layout.popup_window,null);
//        registerForContextMenu(pv);

        final Button button = drawerBinding.buttonOpt;

        final PopupMenu pm = new PopupMenu(button.getContext(), button, Gravity.CENTER);
        pm.getMenuInflater().inflate(R.menu.popup, pm.getMenu());
        pm.setOnMenuItemClickListener(item -> {
            System.out.println(item.getTitle());
            Toast.makeText(button.getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            return true;
        });

        button.setOnClickListener(v -> pm.show());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CHAP_ID_KEY, chapId);
    }

    @Override
    protected void onDestroy() {
        System.out.println("onDestroy");
        if (pwm != null) {
            pwm.clear();
            pwm = null;
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if (pwm != null && pwm.anyPopup()) {
            pwm.clear();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }

}
