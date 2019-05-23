package com.richer.cartoonapp.Acitivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.richer.cartoonapp.Adapter.ChapterAdapter;
import com.richer.cartoonapp.Adapter.ContentAdapter;
import com.richer.cartoonapp.Beans.Content;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;
import com.richer.cartoonapp.Util.ZoomRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ContentActivity extends AppCompatActivity {

    private List<Content> contentList = new ArrayList<>();

    private boolean orientation = true;

    private ContentAdapter adapter;
    private int chapterId;
    private ZoomRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        Intent intent = getIntent();
        chapterId = intent.getIntExtra("chapter_id",0);

        initContent();

        recyclerView = findViewById(R.id.content_zoomrecyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(ContentActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setEnableScale(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(orientation){
            ((LinearLayoutManager)recyclerView.getLayoutManager()).setOrientation(LinearLayoutManager.VERTICAL);
        }

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
        getMenuInflater().inflate(R.menu.activity_content_menu, menu);
        return true;
    }
}
