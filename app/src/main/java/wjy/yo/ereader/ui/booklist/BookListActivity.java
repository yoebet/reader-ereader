package wjy.yo.ereader.ui.booklist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.R;
import wjy.yo.ereader.service.BookListService;
import wjy.yo.ereader.ui.vocabulary.VocabularyActivity;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.remotevo.OpResult;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.remotevo.UserInfo;

public class BookListActivity extends AppCompatActivity {

    @Inject
    AccountService accountService;

    @Inject
    BookListService bookListService;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> {
//            Context context = view.getContext();
//            Intent intent = new Intent(context, MainActivity.class);
//            context.startActivity(intent);
//        });

        final RecyclerView recyclerView = findViewById(R.id.book_list);
        BookRecyclerViewAdapter adapter = new BookRecyclerViewAdapter();
        recyclerView.setAdapter(adapter);

        Disposable disposable = bookListService.loadBooksWithUserBook()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (List<Book> books) -> {
                            System.out.println("111 " + books);
                            adapter.resetList(books);
                        },
                        Throwable::printStackTrace);
        mDisposable.add(disposable);

    }


    private void login() {
        final String userName = "aaaaaa";
        Disposable disposable = accountService.login(userName, "aaaaaa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((UserInfo ui) -> {
                    Toast.makeText(BookListActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }, (t) -> {
                    Toast.makeText(BookListActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                });
        mDisposable.add(disposable);
    }

    private void logout() {
        Disposable disposable = accountService.logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((OpResult opr) -> {
                    Toast.makeText(BookListActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
                }, (t) -> {
                    Toast.makeText(BookListActivity.this, "退出登录失败", Toast.LENGTH_SHORT).show();
                });
        mDisposable.add(disposable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vocabulary:
                Intent intent = new Intent(this, VocabularyActivity.class);
                startActivity(intent);
                return true;
            case R.id.account:
                Disposable disposable = accountService.checkNeedLogin()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((Boolean need) -> {
                            if (need == null || need) {
                                login();
                            } else {
                                logout();
                            }
                        }, Throwable::printStackTrace);
                mDisposable.add(disposable);
                return true;
            case R.id.all_books:
                Toast.makeText(this, "All ...", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.my_books:
                Toast.makeText(this, "my_books", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }

}
