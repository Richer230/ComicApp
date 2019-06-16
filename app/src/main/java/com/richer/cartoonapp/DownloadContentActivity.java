package com.richer.cartoonapp;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.richer.cartoonapp.Acitivity.ContentActivity;
import com.richer.cartoonapp.Adapter.ContentAdapter;
import com.richer.cartoonapp.Adapter.DownloadContentAdapter;
import com.richer.cartoonapp.Beans.Content;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;
import com.richer.cartoonapp.Util.ZoomRecyclerView;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class DownloadContentActivity extends AppCompatActivity {

    private List<File> contentList = new ArrayList<>();

    private boolean orientation = true;

    private DownloadContentAdapter adapter;
    private String chapterId;
    private String chapterName;
    private ZoomRecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_content);

        recyclerView = findViewById(R.id.download_content_zoomrecyclerview);
        Intent intent = getIntent();
        chapterId = intent.getStringExtra("fileId");
        chapterName = intent.getStringExtra("fileName");

        getSupportActionBar().setTitle(chapterName);

        initDownloadContent();

        adapter = new DownloadContentAdapter(contentList);
        System.out.println(contentList);
        recyclerView.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(DownloadContentActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setEnableScale(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(orientation){
            ((LinearLayoutManager)recyclerView.getLayoutManager()).setOrientation(LinearLayoutManager.VERTICAL);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initDownloadContent() {

        File f = Environment.getExternalStorageDirectory();
        String path = f.getPath()+"/downloadImages";
        File file = new File(path+"/"+chapterId);
        System.out.println("((("+file.getPath());
        File[] files = file.listFiles();
        if(files != null){
            for(int i=0;i<files.length;i++){
                contentList.add(files[i]);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            case R.id.action_change:
                if(orientation){
                    ((LinearLayoutManager)recyclerView.getLayoutManager()).setOrientation(LinearLayoutManager.HORIZONTAL);
                    orientation = false;
                }else{
                    ((LinearLayoutManager)recyclerView.getLayoutManager()).setOrientation(LinearLayoutManager.VERTICAL);
                    orientation = true;
                }
                adapter.notifyDataSetChanged();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_download_content_menu, menu);
        return true;
    }
}
