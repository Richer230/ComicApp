package com.richer.cartoonapp.Acitivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.richer.cartoonapp.Fragment.DownloadFragment;
import com.richer.cartoonapp.Fragment.HomeFragment;
import com.richer.cartoonapp.Fragment.MeFragment;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Service.SubscribeService;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    public static final String CHANNEL_ID = "Subscribe";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    getSupportActionBar().setTitle("漫画列表");
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(new DownloadFragment());
                    getSupportActionBar().setTitle("下载列表");
                    return true;
                case R.id.navigation_notifications:
                    replaceFragment(new MeFragment());
                    getSupportActionBar().setTitle("我");
                    return true;
            }
            return false;
        }
    };

    boolean init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("漫画列表");

        replaceFragment(new HomeFragment());
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        createChannel();

        Intent intent = new Intent(this, SubscribeService.class);
        startService(intent);

    }

    private void createChannel() {
        String name = "通知";
        String text = "更新通知";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,name,importance);
            channel.setDescription(text);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }

    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (init) {
            transaction.add(R.id.frameLayout,fragment);
            init=false;
        } else {
            transaction.replace(R.id.frameLayout,fragment);
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

}
