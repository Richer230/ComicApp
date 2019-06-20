package com.richer.cartoonapp.Acitivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.richer.cartoonapp.Adapter.ContentAdapter;
import com.richer.cartoonapp.Beans.Content;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Service.DownloadService;
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

public class HorizontalContentAcitivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

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

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private List<Content> contentList = new ArrayList<>();

    private boolean orientation = false;

    private ContentAdapter adapter;
    private int chapterId;
    private String chapterName;
    private String url;

    private void initContent() {

        String address = "http://app.u17.com/v3/appV3_3/android/phone/comic/" +
                "chapterNew?come_from=xiaomi&serialNumber=7de42d2e&v=4500102" +
                "&model=MI+6&chapter_id="+chapterId+"&android_id=f5c9b6c9284551ad";

        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                System.out.println("#############"+responseText);
                contentList = Utility.handleContentResponse(responseText);
                url = Utility.getDownloadUrl(responseText);
//                ContentActivity.this.runOnUiThread(()->{
//                    adapter = new ContentAdapter(contentList);
//                    recyclerView.setAdapter(adapter);
//                });
                System.out.println(contentList);
                for(int i=0;i<contentList.size();i++) {
                    Fragment f = PlaceholderFragment.newInstance(contentList.get(i).getImg05());
                    fragmentList.add(f);
                }
                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                // Set up the ViewPager with the sections adapter.
                mViewPager = (ViewPager) findViewById(R.id.container);
                runOnUiThread(()->{
                    mViewPager.setAdapter(mSectionsPagerAdapter);
                });

            }
        });

    }
    private List<Fragment> fragmentList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_content_acitivity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        chapterId = intent.getIntExtra("chapter_id",0);
        chapterName = intent.getStringExtra("chapter_name");

        initContent();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_content_menu, menu);
        return true;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String imageURL) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString("URL", imageURL);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_horizontal_content_acitivity, container, false);
            ImageView imageView = (ImageView) rootView.findViewById(R.id.image);
            Glide.with(getContext()).load(getArguments().getString("URL")).into(imageView);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragmentList.size();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_change:
                if(!orientation){
                    Intent intent = new Intent(HorizontalContentAcitivity.this,ContentActivity.class);
                    intent.putExtra("chapter_id",chapterId);
                    intent.putExtra("chapter_name",chapterName);
                    startActivity(intent);
                    orientation = true;
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
                if(ContextCompat.checkSelfPermission(HorizontalContentAcitivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(HorizontalContentAcitivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
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
}
