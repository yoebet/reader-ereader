package wjy.yo.ereader.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Method;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wjy.yo.ereader.BuildConfig;
import wjy.yo.ereader.R;
import wjy.yo.ereader.service.LocalSettingService;
import wjy.yo.ereader.ui.booklist.BookListActivity;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.remotevo.UserInfo;
import wjy.yo.ereader.ui.vocabulary.VocabularyActivity;
import wjy.yo.ereader.util.ExceptionHandlers;

public class LaunchScreenActivity extends AppCompatActivity {

    private static final int MIN_WAITING_TIME = 2000;
    private static final int MAX_WAITING_TIME = 6000;

    @Inject
    AccountService accountService;

    @Inject
    LocalSettingService localSettingService;

    private AsyncTask<Object, Object, Object> waitingTask;

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_launch_screen);

        waitingTask = new BackgroundTask();
        waitingTask.execute();

        ensureLogin();

        if (BuildConfig.DEBUG) {
            System.out.println("SDK_INT: " + android.os.Build.VERSION.SDK_INT);
            showDebugDBAddressLogToast();
        }
    }

    void showDebugDBAddressLogToast() {
        try {
            Class<?> debugDB = Class.forName("com.amitshekhar.DebugDB");
            Method getAddressLog = debugDB.getMethod("getAddressLog");
            Object value = getAddressLog.invoke(null);
            Toast.makeText(this, (String) value, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureLogin() {
        Disposable disposable = accountService.checkNeedLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Boolean need) -> {
                    if (need == null || need) {
                        login();
                    }
                }, ExceptionHandlers::handle);
        mDisposable.add(disposable);
    }

    private void login() {
        final String userName = "aaaaaa";
        Disposable disposable = accountService.login(userName, "aaaaaa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (UserInfo ui) -> {
                            Toast.makeText(LaunchScreenActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            waitingTask.cancel(true);
                        },
                        (t) -> ExceptionHandlers.handle(t, "登录失败"));
        mDisposable.add(disposable);
    }


    private class BackgroundTask extends AsyncTask {

        private void navigate() {
            Intent intent = new Intent(LaunchScreenActivity.this, VocabularyActivity.class);
//            intent = new Intent(LaunchScreenActivity.this, BookListActivity.class);

            startActivity(intent);
            finish();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            long begin = System.currentTimeMillis();
            try {
                Thread.sleep(MAX_WAITING_TIME);
            } catch (InterruptedException e) {
                System.out.println("be interrupted 1");
                long end = System.currentTimeMillis();
                long elapse = end - begin;
                System.out.println("been waited: " + elapse);
                long remain = MIN_WAITING_TIME - elapse;
                if (remain <= 50) {
                    return null;
                }
                try {
                    System.out.println("continue waiting: " + remain);
                    Thread.sleep(remain);
                } catch (InterruptedException e2) {
                    System.out.println("be interrupted 2");
                }
            }

            return null;
        }

        @Override
        protected void onCancelled(Object o) {
            System.out.println("onCancelled");
            navigate();
        }

        @Override
        protected void onPostExecute(Object o) {
            navigate();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }
}
