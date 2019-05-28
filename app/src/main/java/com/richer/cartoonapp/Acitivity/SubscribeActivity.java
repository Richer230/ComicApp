package com.richer.cartoonapp.Acitivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.richer.cartoonapp.Adapter.ComicAdapter;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.R;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class SubscribeActivity extends AppCompatActivity {

    private List<Comics> comicsList = new ArrayList<>();
    private List<Comics> subscribeList = new ArrayList<>();

    private ComicAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        comicsList = LitePal.findAll(Comics.class);

        for(int i=0;i<comicsList.size();i++){
            Comics comic;
            comic = comicsList.get(i);
            if(comic.isSubscribed()){
                subscribeList.add(comic);
            }
        }

        getSupportActionBar().setTitle("订阅列表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.subscribe_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ComicAdapter(subscribeList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
