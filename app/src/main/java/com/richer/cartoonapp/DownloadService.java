package com.richer.cartoonapp;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import com.liulishuo.okdownload.DownloadContext;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;

import java.io.File;
import java.util.List;
import java.util.Map;



public class DownloadService extends Service {

    private static String TAG = "TAG";

    private String url = "http://zip6.u17i.com/62/10662_crop.zip?update_time=1515652521";

    private String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
    private String fileName = "comic";
    File parentFile = new File(directory);

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void taskStart(@NonNull DownloadTask task) {
            Log.d(TAG, "taskStart: ");
        }

        @Override
        public void connectTrialStart(@NonNull DownloadTask task, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectTrialEnd(@NonNull DownloadTask task, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

        }

        @Override
        public void downloadFromBeginning(@NonNull DownloadTask task, @NonNull BreakpointInfo info, @NonNull ResumeFailedCause cause) {

        }

        @Override
        public void downloadFromBreakpoint(@NonNull DownloadTask task, @NonNull BreakpointInfo info) {

        }

        @Override
        public void connectStart(@NonNull DownloadTask task, int blockIndex, @NonNull Map<String, List<String>> requestHeaderFields) {

        }

        @Override
        public void connectEnd(@NonNull DownloadTask task, int blockIndex, int responseCode, @NonNull Map<String, List<String>> responseHeaderFields) {

        }

        @Override
        public void fetchStart(@NonNull DownloadTask task, int blockIndex, long contentLength) {

        }

        @Override
        public void fetchProgress(@NonNull DownloadTask task, int blockIndex, long increaseBytes) {

        }

        @Override
        public void fetchEnd(@NonNull DownloadTask task, int blockIndex, long contentLength) {

        }

        @Override
        public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause) {

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(()->{
            DownloadTask task = new DownloadTask.Builder(url,parentFile)
                    .setFilename(fileName)
                    .setMinIntervalMillisCallbackProcess(30)
                    .setPassIfAlreadyCompleted(false)
                    .build();
            task.enqueue(listener);
            //task.cancel();
            task.execute(listener);
        }).start();

    }

    public DownloadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
