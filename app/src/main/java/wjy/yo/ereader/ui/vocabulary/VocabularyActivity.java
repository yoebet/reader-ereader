package wjy.yo.ereader.ui.vocabulary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import org.joda.time.Period;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.booklist.BookListActivity;
import wjy.yo.ereader.ui.dict.DictBottomSheetActivity;
import wjy.yo.ereader.util.ExceptionHandlers;
import wjy.yo.ereader.vo.GroupedUserWords;
import wjy.yo.ereader.vo.VocabularyFilter;

import static wjy.yo.ereader.vo.VocabularyFilter.*;

public class VocabularyActivity extends DictBottomSheetActivity {

    private Disposable filterDisp;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private VocabularyFilter filter = new VocabularyFilter();
    private WordsGroupRecyclerViewAdapter wordsGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RecyclerView recyclerView = findViewById(R.id.vocabulary_groups);
        wordsGroupAdapter = new WordsGroupRecyclerViewAdapter(this, this);
        recyclerView.setAdapter(wordsGroupAdapter);

        setupFilterForms();

    }

    private void setupFamiliarityFilter() {

        CheckBox all = findViewById(R.id.familiarity_all);
        CheckBox f1 = findViewById(R.id.familiarity_1);
        CheckBox f2 = findViewById(R.id.familiarity_2);
        CheckBox f3 = findViewById(R.id.familiarity_3);

        all.setOnCheckedChangeListener(
                (CompoundButton buttonView, boolean isChecked) -> {
                    System.out.println(buttonView + " checked: " + isChecked);
                    f1.setEnabled(!isChecked);
                    f2.setEnabled(!isChecked);
                    f3.setEnabled(!isChecked);
                    filter.setFamiliarityAll(isChecked);
                });

        f1.setOnCheckedChangeListener((v, isChecked) -> filter.setFamiliarity1(isChecked));
        f2.setOnCheckedChangeListener((v, isChecked) -> filter.setFamiliarity2(isChecked));
        f3.setOnCheckedChangeListener((v, isChecked) -> filter.setFamiliarity3(isChecked));

        filter.setFamiliarityAll(all.isChecked());
        filter.setFamiliarity1(f1.isChecked());
        filter.setFamiliarity2(f2.isChecked());
        filter.setFamiliarity3(f3.isChecked());
    }

    static class AddDateOption {
        String label;
        Period period;

        AddDateOption(String label, Period period) {
            this.label = label;
            this.period = period;
        }

        public String toString() {
            return label;
        }
    }

    private void setupAddDateFilter() {

        AddDateOption[] options = new AddDateOption[]{
                new AddDateOption(this.getString(R.string.vocabulary_add_date_all), null),
                new AddDateOption(this.getString(R.string.vocabulary_add_date_1m), Period.months(1)),
                new AddDateOption(this.getString(R.string.vocabulary_add_date_1w), Period.weeks(1)),
                new AddDateOption(this.getString(R.string.vocabulary_add_date_1d), Period.days(1))
        };

        Spinner spinner = findViewById(R.id.spinner_add_date);
        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                AddDateOption ado = (AddDateOption) parent.getItemAtPosition(position);
                filter.setPeriod(ado.period);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    static class GroupByOption {
        String label;
        GroupBy groupBy;

        GroupByOption(String label, GroupBy groupBy) {
            this.label = label;
            this.groupBy = groupBy;
        }

        public String toString() {
            return label;
        }
    }


    private void setupGroupBy() {

        GroupByOption[] options = new GroupByOption[]{
                new GroupByOption(this.getString(R.string.vocabulary_group_by_none), GroupBy.NONE),
                new GroupByOption(this.getString(R.string.vocabulary_group_by_familiar), GroupBy.Familiarity),
                new GroupByOption(this.getString(R.string.vocabulary_group_by_date), GroupBy.AddDate),
                new GroupByOption(this.getString(R.string.vocabulary_group_by_chap), GroupBy.Chapter)
        };

        Spinner spinner = findViewById(R.id.spinner_group_by);
        SpinnerAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_item, options);
        spinner.setAdapter(adapter);

        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                GroupByOption gbo = (GroupByOption) parent.getItemAtPosition(position);
                filter.setGroupBy(gbo.groupBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    private void setupFilterForms() {

        setupFamiliarityFilter();

        setupAddDateFilter();

        setupGroupBy();

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
//                                System.out.println("Size: " + list.size());

                                wordsGroupAdapter.resetList(list);
                            },
                            ExceptionHandlers::handle);
            mDisposable.add(filterDisp);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vocabulary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all_books:
                Intent intent = new Intent(this, BookListActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (dictSheetBehavior != null &&
                dictSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            dictSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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
