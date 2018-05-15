package wjy.yo.ereader;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bfc = (Button) findViewById(R.id.buttonFullscreen);
        bfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, FullscreenActivity.class);
                context.startActivity(intent);
            }
        });
        Button bb = (Button) findViewById(R.id.buttonBasic);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BasicActivity.class);
                context.startActivity(intent);
            }
        });
        Button bli = (Button) findViewById(R.id.buttonLogin);
        bli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        Button bbn = (Button) findViewById(R.id.buttonBottomNav);
        bbn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, BottomNavigationActivity.class);
                context.startActivity(intent);
            }
        });
        Button bnd = (Button) findViewById(R.id.buttonNavDrawer);
        bnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, NavigationDrawerActivity.class);
                context.startActivity(intent);
            }
        });
        Button bs = (Button) findViewById(R.id.buttonScrolling);
        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, ScrollingActivity.class);
                context.startActivity(intent);
            }
        });
        Button bss = (Button) findViewById(R.id.buttonSettings);
        bss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
            }
        });
        Button bsp = (Button) findViewById(R.id.buttonSpinner);
        bsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SpinnerTabActivity.class);
                context.startActivity(intent);
            }
        });
        Button bsw = (Button) findViewById(R.id.buttonSwipe);
        bsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, SwipeTabActivity.class);
                context.startActivity(intent);
            }
        });
        Button bt = (Button) findViewById(R.id.buttonTabs);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, TabsActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
