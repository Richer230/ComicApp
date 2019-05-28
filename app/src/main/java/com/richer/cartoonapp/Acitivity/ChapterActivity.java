package com.richer.cartoonapp.Acitivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.richer.cartoonapp.Adapter.ChapterAdapter;
import com.richer.cartoonapp.Adapter.ComicAdapter;
import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;
import com.richer.cartoonapp.Util.ZoomRecyclerView;

import org.litepal.LitePal;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChapterActivity extends AppCompatActivity {

    private int comicId;
    private long id;
    private String comicName;
    private String comicAuthor;
    private String comicTags;
    private String comicCover;
    private String comicDescription;

    private ImageView img_comicCover;
    private TextView tv_comicName;
    private TextView tv_comicAuthor;
    private TextView tv_comicTags;
    private TextView tv_comicDescription;
    private List<Chapters> chaptersList;
    private Button btSubscribe;
    private Button btCancelSubscribe;

    private long lastUpdateTime;

    private ChapterAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        Intent intent = getIntent();
        id = intent.getLongExtra("id",0);
        System.out.println("id : "+ id);
        comicName = intent.getStringExtra("comic_name");
        comicAuthor = intent.getStringExtra("comic_author");
        comicTags = intent.getStringExtra("comic_tags");
        comicCover = intent.getStringExtra("comic_cover");
        comicId = intent.getIntExtra("comic_id",0);
        comicDescription = intent.getStringExtra("comic_description");

        getSupportActionBar().setTitle(comicName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initChapters();
        recyclerView = findViewById(R.id.chapter_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(ChapterActivity.this,1);
        recyclerView.setLayoutManager(layoutManager);

        img_comicCover = findViewById(R.id.comic_chapter_image);
        tv_comicName = findViewById(R.id.comic_chapter_name);
        tv_comicAuthor = findViewById(R.id.comic_chapter_author);
        tv_comicTags = findViewById(R.id.comic_chapter_tags);
        tv_comicDescription = findViewById(R.id.comic_chapter_description);
        Glide.with(ChapterActivity.this).load(comicCover).into(img_comicCover);
        tv_comicName.setText(comicName);
        tv_comicAuthor.setText(comicAuthor);
        tv_comicTags.setText(comicTags);
        tv_comicDescription.setText(comicDescription);

        btSubscribe = findViewById(R.id.bt_subscibe);
        btSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("isSubscribed",true);

                LitePal.updateAll(Comics.class,values,"comicId = ?",String.valueOf(comicId));
                Toast.makeText(ChapterActivity.this,"订阅成功",Toast.LENGTH_SHORT).show();
            }
        });
        btCancelSubscribe = findViewById(R.id.cancel_subscribe);
        btCancelSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("isSubscribed",false);

                LitePal.updateAll(Comics.class,values,"comicId = ?",String.valueOf(comicId));
                Toast.makeText(ChapterActivity.this,"已取消订阅",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initChapters() {
        String address = "http://app.u17.com/v3/appV3_3/" +
                "android/phone/comic/detail_static_new?" +
                "come_from=xiaomi&comicid="+comicId+"&serialNumber" +
                "=7de42d2e&v=4500102&model=MI+6&android_id" +
                "=f5c9b6c9284551ad";

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ChapterActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                System.out.println("#############"+responseText);
                chaptersList = Utility.handleChapterResponse(responseText);
                lastUpdateTime = Utility.getLastUpdateTime(responseText,comicId);
                SharedPreferences.Editor editor = getSharedPreferences("update",MODE_PRIVATE).edit();
                editor.putLong(String.valueOf(comicId),lastUpdateTime);
                editor.apply();
                ChapterActivity.this.runOnUiThread(()->{
                    adapter = new ChapterAdapter(chaptersList);
                    recyclerView.setAdapter(adapter);
                });
                System.out.println(chaptersList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

}
