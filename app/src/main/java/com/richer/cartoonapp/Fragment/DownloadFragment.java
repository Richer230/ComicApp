package com.richer.cartoonapp.Fragment;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.richer.cartoonapp.Adapter.DownloadAdapter;
import com.richer.cartoonapp.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends Fragment {

    private File file;
    private File[] fileList;
    private List<String> fileNames = new ArrayList<>();

    private DownloadAdapter adapter;
    private RecyclerView recyclerView;

    private Button deleteButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.download_fragment,container,false);

        String d = Environment.getExternalStorageDirectory().getPath();
        file = new File(d+"/downloadImages");
        fileList = file.listFiles();
        String[] names = file.list();
        if(names != null){
            int len = names.length;
            for(int i=0;i<len;i++){
                fileNames.add(names[i]);
            }
            adapter = new DownloadAdapter(fileNames);
            recyclerView = view.findViewById(R.id.download_recyclerview);
            recyclerView.setAdapter(adapter);

            GridLayoutManager layoutManager = new GridLayoutManager(getContext(),1);
            recyclerView.setLayoutManager(layoutManager);
        }else{
        }

        return view;

    }
}
