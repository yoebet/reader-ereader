package wjy.yo.ereader.ui.booklist;

import android.arch.lifecycle.LiveData;
import android.content.Context;
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

import wjy.yo.ereader.R;
import wjy.yo.ereader.ui.vocabulary.VocabularyActivity;
import wjy.yo.ereader.entity.book.Book;
import wjy.yo.ereader.model.Failure;
import wjy.yo.ereader.model.OpResult;
import wjy.yo.ereader.model.UserInfo;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.BookService;
import wjy.yo.ereader.service.ServiceCallback;

public class BookListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Inject
    AccountService accountService;

    @Inject
    BookService bookService;

    @Inject
    BookListViewModel bookListViewModel;

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

        if (findViewById(R.id.book_detail_container) != null) {
            mTwoPane = true;
        }

        final RecyclerView recyclerView = findViewById(R.id.book_list);
        BookRecyclerViewAdapter adapter = new BookRecyclerViewAdapter(getSupportFragmentManager(), mTwoPane);
        recyclerView.setAdapter(adapter);

        LiveData<List<Book>> ld = bookListViewModel.getBooks();
        ld.observe(this, (List<Book> books) -> {
            System.out.println("111 " + books);
            if (books != null) {
                adapter.resetList(books);
            }
        });

    }


    private void login() {
        final String userName = "aaaaaa";
        accountService.login(userName, "aaaaaa", new ServiceCallback<OpResult>() {
            @Override
            public void onComplete(OpResult opResult) {
                Toast.makeText(BookListActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Failure f) {
                Toast.makeText(BookListActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        accountService.logout(new ServiceCallback<OpResult>() {
            @Override
            public void onComplete(OpResult opResult) {
                Toast.makeText(BookListActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Failure f) {
                Toast.makeText(BookListActivity.this, "退出登录失败", Toast.LENGTH_SHORT).show();
            }
        });
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
                UserInfo userInfo = accountService.getUserInfo();
                if (userInfo == null || !userInfo.isLogin()) {
                    login();
                } else {
                    logout();
                }
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

}
