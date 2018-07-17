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

    private static final int SPLASH_TIME = 2000;

    @Inject
    AccountService accountService;

    @Inject
    LocalSettingService localSettingService;

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

        AsyncTask<Object, Object, Object> task = new BackgroundTask();
        task.execute();

        Disposable disposable = accountService.checkNeedLogin()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Boolean need) -> {
                    if (need == null || need) {
                        login();
                    }
                }, ExceptionHandlers::handle);
        mDisposable.add(disposable);

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

    private void login() {
        final String userName = "aaaaaa";
        Disposable disposable = accountService.login(userName, "aaaaaa")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((UserInfo ui) -> {
                    Toast.makeText(LaunchScreenActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                }, (t) -> {
                    Toast.makeText(LaunchScreenActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                });
        mDisposable.add(disposable);
    }


    private class BackgroundTask extends AsyncTask {
        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intent = new Intent(LaunchScreenActivity.this, VocabularyActivity.class);
//            intent = new Intent(LaunchScreenActivity.this, BookListActivity.class);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mDisposable.clear();
    }
}
