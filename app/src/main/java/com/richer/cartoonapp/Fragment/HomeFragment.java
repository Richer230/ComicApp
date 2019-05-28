package com.richer.cartoonapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.richer.cartoonapp.Acitivity.MainActivity;
import com.richer.cartoonapp.Acitivity.SearchActivity;
import com.richer.cartoonapp.Adapter.ComicAdapter;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.HttpUtil;
import com.richer.cartoonapp.Util.Utility;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.richer.cartoonapp.Util.HttpUtil.sendOkHttpRequest;

public class HomeFragment extends Fragment {

    private int page=1;
    private List<Comics> comicsList = new ArrayList<>();
    private ComicAdapter adapter;
    private RecyclerView recyclerView;
    private Button nextPage;
    private Button previousPage;
    private EditText editSearch;
    private Button buttonSearch;

    private SwipeRefreshLayout swipeRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,container,false);

        editSearch = view.findViewById(R.id.et_search);
        buttonSearch = view.findViewById(R.id.bt_search);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                String searchText = editSearch.getText().toString();
                intent.putExtra("searchText",searchText);
                Log.d("tag", "onClick: "+searchText);
                getActivity().startActivity(intent);
            }
        });

        previousPage = view.findViewById(R.id.bt_formal);
        nextPage = view.findViewById(R.id.bt_next);
        previousPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toPreviousPage();
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextPage();
            }
        });
        initComics();
        recyclerView = view.findViewById(R.id.home_recyclerview);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefresh = view.findViewById(R.id.sr_home);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshComics();
            }
        });

        return view;
    }

    private void toNextPage() {
        page++;
        refreshComics();
    }

    private void toPreviousPage() {
        if(page==1){
            Toast.makeText(getContext(),"已经是第一页啦",Toast.LENGTH_SHORT).show();
        }
        page--;
        refreshComics();
    }

    private void refreshComics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initComics();
                        if(adapter!=null)adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initComics() {

        String address = "http://app.u17.com/v3/appV3_3/android/phone/list/getRankComicList?" +
                "period=total&type=2&page="+page+"&come_from=xiaomi" +
                "&serialNumber=7de42d2e" +
                "&v=450010" +
                "&model=MI+6" +
                "&android_id=f5c9b6c9284551ad";
        System.out.println("*********"+address);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               //  Toast.makeText(,"加载失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                System.out.println("#############"+responseText);
                comicsList = Utility.handleComicsResponse(responseText);
                getActivity().runOnUiThread(()->{
                    adapter = new ComicAdapter(comicsList);
                    recyclerView.setAdapter(adapter);
                });
                System.out.println(comicsList);
            }
        });

    }


}
