package wjy.yo.ereader.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import wjy.yo.ereader.R;
import wjy.yo.ereader.model.Failure;
import wjy.yo.ereader.model.OpResult;
import wjy.yo.ereader.model.UserInfo;
import wjy.yo.ereader.service.AccountService;
import wjy.yo.ereader.service.ServiceCallback;

public class LaunchScreenActivity extends AppCompatActivity {

    private static final int SPLASH_TIME = 3000;

    @Inject
    AccountService accountService;

    UserInfo userInfo;

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

        getUserInfo();
    }

    private void getUserInfo() {
        accountService.getUserInfo(new ServiceCallback<UserInfo>() {
            @Override
            public void onComplete(UserInfo ui) {
                if (ui == null) {
                    login();
                    return;
                }
                userInfo = ui;
                String text = "user: " + ui.getName() + ", login: " + ui.isLogin();
                Toast.makeText(LaunchScreenActivity.this, text, Toast.LENGTH_SHORT).show();
                if (ui.isLogin()) {
//                    logout();
                } else {
                    login();
                }
            }

            @Override
            public void onFailure(Failure f) {
                Toast.makeText(LaunchScreenActivity.this, f.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login() {
        final String userName = "aaaaaa";
        accountService.login(userName, "aaaaaa", new ServiceCallback<OpResult>() {
            @Override
            public void onComplete(OpResult opResult) {
                userInfo = new UserInfo();
                userInfo.setLogin(true);
                userInfo.setName(userName);
                Toast.makeText(LaunchScreenActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Failure f) {
                Toast.makeText(LaunchScreenActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void logout() {
        accountService.logout(new ServiceCallback<OpResult>() {
            @Override
            public void onComplete(OpResult opResult) {
                System.out.println(opResult);
                userInfo = null;
                Toast.makeText(LaunchScreenActivity.this, "已退出登录", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Failure f) {
                Toast.makeText(LaunchScreenActivity.this, "退出登录失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class BackgroundTask extends AsyncTask {
        Intent intent;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            intent = new Intent(LaunchScreenActivity.this, BookListActivity.class);
        }

        @Override
        protected Object doInBackground(Object[] params) {
/*            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (userInfo != null) {
                String text = "user: " + userInfo.getName() + ", login: " + userInfo.isLogin();
                Toast.makeText(LaunchScreenActivity.this, text, Toast.LENGTH_SHORT).show();
            }*/

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
}
