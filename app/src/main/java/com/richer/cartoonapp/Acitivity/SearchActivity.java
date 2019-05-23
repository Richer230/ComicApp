package com.richer.cartoonapp.Acitivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.richer.cartoonapp.Adapter.ComicAdapter;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private List<Comics> mComicList = new ArrayList<>();
    private ComicAdapter adapter;
    private RecyclerView recyclerView;

    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        searchText = intent.getStringExtra("searchText");

        initComics();
        recyclerView = findViewById(R.id.search_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);

    }

    private void initComics() {

        String address =  "http://app.u17.com/v3/appV3_3/android/phone/search/searchResult?" +
                "q="+searchText+"&come_from=xiaomi" +
                "&serialNumber=7de42d2e" +
                "&v=4500102" +
                "&model=MI+6" +
                "&android_id=f5c9b6c9284551ad";
        System.out.println("*********"+address);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this,"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                System.out.println("#############"+responseText);
                mComicList = Utility.handleComicsResponse(responseText);
                runOnUiThread(()->{
                    adapter = new ComicAdapter(mComicList);
                    recyclerView.setAdapter(adapter);
                });
                System.out.println(mComicList);
            }
        });

    }
}
