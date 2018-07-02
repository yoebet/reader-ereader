package wjy.yo.ereader.ui.vocabulary;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import org.joda.time.Period;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.service.UserWordService;
import wjy.yo.ereader.ui.booklist.BookRecyclerViewAdapter;
import wjy.yo.ereader.vo.GroupedUserWords;
import wjy.yo.ereader.vo.VocabularyFilter;

import static wjy.yo.ereader.vo.VocabularyFilter.*;

public class VocabularyActivity extends AppCompatActivity {

    @Inject
    UserWordService userWordService;

    private VocabularyFilter filter = new VocabularyFilter();

    private Disposable filterDisp;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupFilterForms();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//                Fragment fragment = new CursorLoaderListFragment();
//                Fragment fragment = ConsListDialogFragment.newInstance(30);
//                fragment.setArguments(arguments);
//                getSupportFragmentManager().beginTransaction()
//                        .add(R.id.voca_fragment_container, fragment)
//                        .commit();

            BottomSheetDialogFragment bsdf = new DictBottomSheetDialogFragment();
            bsdf.show(getSupportFragmentManager(), bsdf.getTag());

        });

    }


    private void setupFamilarityFilter() {

        CheckBox all = findViewById(R.id.familarity_all);
        CheckBox f1 = findViewById(R.id.familarity_1);
        CheckBox f2 = findViewById(R.id.familarity_2);
        CheckBox f3 = findViewById(R.id.familarity_3);

        all.setOnCheckedChangeListener(
                (CompoundButton buttonView, boolean isChecked) -> {
                    System.out.println(buttonView + " checked: " + isChecked);
                    f1.setEnabled(!isChecked);
                    f2.setEnabled(!isChecked);
                    f3.setEnabled(!isChecked);
                    filter.setFamiliarityAll(isChecked);
                });

        f1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filter.setFamiliarity1(isChecked);
        });
        f2.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filter.setFamiliarity2(isChecked);
        });
        f3.setOnCheckedChangeListener((buttonView, isChecked) -> {
            filter.setFamiliarity3(isChecked);
        });

        filter.setFamiliarityAll(all.isChecked());
        filter.setFamiliarity1(f1.isChecked());
        filter.setFamiliarity2(f2.isChecked());
        filter.setFamiliarity3(f3.isChecked());
    }

    private void setupAddDateFilter() {

        RadioGroup.OnCheckedChangeListener dsHandler = (RadioGroup group, @IdRes int checkedId) -> {
            switch (checkedId) {
                case R.id.add_date_all:
                    filter.setPeriod(null);
                    break;
                case R.id.add_date_1w:
                    filter.setPeriod(Period.weeks(1));
                    break;
                case R.id.add_date_1m:
                    filter.setPeriod(Period.months(1));
                    break;
            }
        };

        RadioGroup dateScope = findViewById(R.id.add_date_scope);
        dateScope.setOnCheckedChangeListener(dsHandler);

        dsHandler.onCheckedChanged(dateScope, dateScope.getCheckedRadioButtonId());
    }


    private void setupGroupBy() {

        RadioGroup.OnCheckedChangeListener gbHandler = (RadioGroup group, @IdRes int checkedId) -> {
            switch (checkedId) {
                case R.id.group_by_none:
                    filter.setGroupBy(GroupBy.NONE);
                    break;
                case R.id.group_by_add_date:
                    filter.setGroupBy(GroupBy.AddDate);
                    break;
                case R.id.group_by_chapter:
                    filter.setGroupBy(GroupBy.Chapter);
                    break;
            }
        };

        RadioGroup groupBy = findViewById(R.id.group_by_method);
        groupBy.setOnCheckedChangeListener(gbHandler);

        gbHandler.onCheckedChanged(groupBy, groupBy.getCheckedRadioButtonId());
    }


    private void setupFilterForms() {


        setupFamilarityFilter();

        setupAddDateFilter();

        setupGroupBy();


        final RecyclerView recyclerView = findViewById(R.id.vocabulary_groups);
        final GroupedUserWordsRecyclerViewAdapter adapter = new GroupedUserWordsRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        findViewById(R.id.refresh).setOnClickListener(v -> {
            System.out.println(filter);
            Single<List<GroupedUserWords>> gws = userWordService.filterAndGroup(filter);
            if (filterDisp != null) {
                mDisposable.remove(filterDisp);
            }
            filterDisp = gws
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            (List<GroupedUserWords> list) -> {
                                System.out.println("Group By: " + filter.getGroupBy());
                                for (GroupedUserWords guw : list) {
                                    System.out.println(guw);
                                }

                                adapter.resetList(list);
                            },
                            Throwable::printStackTrace);
            mDisposable.add(filterDisp);
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }

}
