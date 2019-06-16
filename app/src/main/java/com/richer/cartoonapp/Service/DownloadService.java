package com.richer.cartoonapp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.richer.cartoonapp.Acitivity.MainActivity;
import com.richer.cartoonapp.Util.DownloadTask;
import com.richer.cartoonapp.R;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.util.Map;
import java.util.Set;

public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;
    private String id;
    private String name;

    private DownloadBinder mBinder = new DownloadBinder();

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("Downloading...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Success",-1));
            Toast.makeText(DownloadService.this,"Download Success",Toast.LENGTH_SHORT).show();
            initZip();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("Download Failed",-1));
            Toast.makeText(DownloadService.this,"Download Failed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this,"Paused",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"Canceled", Toast.LENGTH_SHORT).show();
        }
    };


    private void initZip() {

        File f = Environment.getExternalStorageDirectory();
        String path = f.getPath()+"/downloadImages";
        File downloadFile = new File(path);
        downloadFile.mkdirs();
        String d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
        try {
            unZip(d+"/"+id,downloadFile.getPath()+"/"+id+"/");
        } catch (ZipException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = getSharedPreferences("downloadMessage",MODE_PRIVATE).edit();
        editor.putString(id,name);
        editor.apply();

    }

    public void unZip(String zipfile, String dest) throws ZipException
    {
        System.out.println(zipfile);
        ZipFile zfile = new ZipFile(zipfile);
        zfile.setFileNameCharset("UTF-8");
        if (!zfile.isValidZipFile())
        {
            throw new ZipException("压缩文件不合法，可能已经损坏！");
        }
        File allFile = new File(dest);
        allFile.mkdirs();
        zfile.extractAll(dest);

    }

    public class DownloadBinder extends Binder{
        public void startDownload(Map<String,String> map,String chapterName){
            name = chapterName;
            Set<String> idSet = map.keySet();
            for(String chapterId:idSet){
                String url = map.get(chapterId);
                if(downloadTask == null){
                    downloadUrl = url;
                    downloadTask = new DownloadTask(listener);
                    System.out.println(downloadUrl);
                    downloadTask.execute(downloadUrl,chapterId);
                    id = chapterId;
                    startForeground(1,getNotification("Downloading...",0));
                    Toast.makeText(DownloadService.this,"Downloading...",Toast.LENGTH_SHORT).show();
                }
            }
            SharedPreferences.Editor editor = getSharedPreferences("download",MODE_PRIVATE).edit();
            editor.clear();
            editor.apply();
        }

        public void pauseDownload(){
            if(downloadTask != null){
                downloadTask.pauseDownload();
            }
        }

        public void cancelDownload(){
            if(downloadTask != null){
                downloadTask.cancelDownload();
            }else{
                if(downloadUrl != null){
                    String fileName = id;
                    String directory = Environment.getExternalStoragePublicDirectory
                            (Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"Canceled",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,int progress){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,MainActivity.CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress > 0){
            builder.setContentText(progress + "%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}
