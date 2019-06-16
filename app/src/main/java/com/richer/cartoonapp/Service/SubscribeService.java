package com.richer.cartoonapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.richer.cartoonapp.Acitivity.MainActivity.CHANNEL_ID;

public class SubscribeService extends Service {

    private List<Comics> subscribeList = new ArrayList<>();
    private List<Comics> comicsList = new ArrayList<>();

    private boolean runService = false;

    @Override
    public void onCreate() {
        super.onCreate();

        comicsList = LitePal.findAll(Comics.class);

        for(int i=0;i<comicsList.size();i++){
            Comics comic;
            comic = comicsList.get(i);
            if(comic.isSubscribed()){
                subscribeList.add(comic);
            }
        }
        subscribeNotify();

    }

    public SubscribeService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
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
                                        new NotificationCompat.Builder(SubscribeService.this,CHANNEL_ID)
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
                    Thread.sleep(900*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }
}
