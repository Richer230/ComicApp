package com.richer.cartoonapp.Acitivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.richer.cartoonapp.Adapter.ContentAdapter;
import com.richer.cartoonapp.Beans.Content;
import com.richer.cartoonapp.Service.DownloadService;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;
import com.richer.cartoonapp.Util.ZoomRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ContentActivity extends AppCompatActivity {

    private List<Content> contentList = new ArrayList<>();

    private boolean orientation = true;

    private ContentAdapter adapter;
    private int chapterId;
    private String chapterName;
    private ZoomRecyclerView recyclerView;

    private String url;
    private Map<String,String> downloadMap;

    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SharedPreferences spf = getSharedPreferences("download",MODE_PRIVATE);
            downloadMap = (Map<String, String>) spf.getAll();
            downloadBinder = (DownloadService.DownloadBinder) service;
            downloadBinder.startDownload(downloadMap,chapterName);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        chapterId = intent.getIntExtra("chapter_id",0);
        chapterName = intent.getStringExtra("chapter_name");

        initContent();

        recyclerView = findViewById(R.id.content_zoomrecyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(ContentActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setEnableScale(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(orientation){
            ((LinearLayoutManager)recyclerView.getLayoutManager()).setOrientation(LinearLayoutManager.VERTICAL);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(chapterName);
    }

    private void initContent() {

        String address = "http://app.u17.com/v3/appV3_3/android/phone/comic/" +
                "chapterNew?come_from=xiaomi&serialNumber=7de42d2e&v=4500102" +
                "&model=MI+6&chapter_id="+chapterId+"&android_id=f5c9b6c9284551ad";

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ContentActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                System.out.println("#############"+responseText);
                contentList = Utility.handleContentResponse(responseText);
                url = Utility.getDownloadUrl(responseText);
                ContentActivity.this.runOnUiThread(()->{
                    adapter = new ContentAdapter(contentList);
                    recyclerView.setAdapter(adapter);
                });
                System.out.println(contentList);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_change:
                if(orientation){
                    Intent intent = new Intent(ContentActivity.this,HorizontalContentAcitivity.class);
                    intent.putExtra("chapter_id",chapterId);
                    intent.putExtra("chapter_name",chapterName);
                    startActivity(intent);
                    orientation = false;
                    finish();
                }else{
                }
                break;
            case R.id.action_download:
                SharedPreferences spf = getSharedPreferences("download",MODE_PRIVATE);
                String Url = spf.getString(String.valueOf(chapterId),null);
                if(Url == null){
                    SharedPreferences.Editor editor = getSharedPreferences("download",MODE_PRIVATE).edit();
                    editor.putString(String.valueOf(chapterId),url);
                    editor.apply();
                }
                if(ContextCompat.checkSelfPermission(ContentActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(ContentActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    Intent bindIntent = new Intent(this,DownloadService.class);
                    bindService(bindIntent,connection,BIND_AUTO_CREATE);
                }
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_content_menu, menu);
        return true;
    }
}
