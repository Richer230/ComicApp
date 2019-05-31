package com.richer.cartoonapp.Acitivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.Fragment.DownloadFragment;
import com.richer.cartoonapp.Fragment.HomeFragment;
import com.richer.cartoonapp.Fragment.MeFragment;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private List<Comics> subscribeList = new ArrayList<>();
    private List<Comics> comicsList = new ArrayList<>();

    private boolean runService = false;
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

        comicsList = LitePal.findAll(Comics.class);

        for(int i=0;i<comicsList.size();i++){
            Comics comic;
            comic = comicsList.get(i);
            if(comic.isSubscribed()){
                subscribeList.add(comic);
            }
        }

        createChannel();

        subscribeNotify();

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

    private void subscribeNotify() {
        runService = true;
        new Thread(()->{
            while(runService){
                String address;
                for(int i=0;i<subscribeList.size();i++){
                    Comics comic = subscribeList.get(i);
                    Log.d("Id", "subscribeNotify: "+ comic.getComicId());
                    address = "http://app.u17.com/v3/appV3_3/" +
                            "android/phone/comic/detail_static_new?" +
                            "come_from=xiaomi&comicid="+comic.getComicId()+"&serialNumber" +
                            "=7de42d2e&v=4500102&model=MI+6&android_id" +
                            "=f5c9b6c9284551ad";
                    HttpUtil.sendOkHttpRequest(address, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            SharedPreferences spf = getSharedPreferences("update",MODE_PRIVATE);
                            long lastUpdateTime1 = spf.getLong(String.valueOf(comic.getComicId()),0);
                            long lastUpdateTime2=0;

                            String responseText = response.body().string();
                            if(!TextUtils.isEmpty(responseText)){
                                try{
                                    JSONObject messages = new JSONObject(responseText);
                                    JSONObject datas = messages.getJSONObject("data");
                                    JSONObject returnData = datas.getJSONObject("returnData");
                                    JSONObject comic = returnData.getJSONObject("comic");
                                    lastUpdateTime2 = comic.getLong("last_update_time");
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                            }
                            Log.d("comic", "onResponse: "+ lastUpdateTime1);
                            Log.d("tag", "onResponse: "+ lastUpdateTime2);
                            if(lastUpdateTime1 != lastUpdateTime2){

                                SharedPreferences.Editor editor = getSharedPreferences("update",MODE_PRIVATE).edit();
                                editor.remove(String.valueOf(comic.getComicId()));
                                editor.putLong(String.valueOf(comic.getComicId()),lastUpdateTime2);

                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                                Notification notification =
                                        new NotificationCompat.Builder(MainActivity.this,CHANNEL_ID)
                                                .setSmallIcon(R.drawable.ic_launcher_background)
                                                .setContentText("您订阅的漫画更新了！")
                                                .setContentTitle(comic.getName())
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .build();

                                notificationManager.notify(1,notification);

                            }

                        }
                    });
                }
                try {
                    Thread.sleep(600*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
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
