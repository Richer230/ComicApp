package com.richer.cartoonapp.Acitivity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.richer.cartoonapp.Adapter.ChapterAdapter;
import com.richer.cartoonapp.Adapter.ComicAdapter;
import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;
import com.richer.cartoonapp.Util.ZoomRecyclerView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChapterActivity extends AppCompatActivity {

    private int comicId;
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


    private ChapterAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        Intent intent = getIntent();
        comicName = intent.getStringExtra("comic_name");
        comicAuthor = intent.getStringExtra("comic_author");
        comicTags = intent.getStringExtra("comic_tags");
        comicCover = intent.getStringExtra("comic_cover");
        comicId = intent.getIntExtra("comic_id",0);
        comicDescription = intent.getStringExtra("comic_description");
//        ZoomRecyclerView r = findViewById(R.id.chapter_recyclerview);
//        r.setEnableScale(true);
//        r.setLayoutManager(new LinearLayoutManager(this));
//        ((LinearLayoutManager)r.getLayoutManager()).setOrientation(LinearLayoutManager.VERTICAL);
        System.out.println("000000: "+comicTags);

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
                ChapterActivity.this.runOnUiThread(()->{
                    adapter = new ChapterAdapter(chaptersList);
                    recyclerView.setAdapter(adapter);
                });
                System.out.println(chaptersList);
            }
        });
    }
}
