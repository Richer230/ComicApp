package com.richer.cartoonapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.richer.cartoonapp.Acitivity.ContentActivity;
import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.DownloadContentActivity;
import com.richer.cartoonapp.R;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private Context mContext;

    private List<String> mChapterList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView chapterName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            chapterName = itemView.findViewById(R.id.download_chapter_name);
        }
    }

    public DownloadAdapter(List<String> chaptersList){
        mChapterList = chaptersList;
    }

    @NonNull
    @Override
    public DownloadAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.download_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadAdapter.ViewHolder viewHolder, int i) {
        String id = mChapterList.get(i);

        SharedPreferences spf = mContext.getSharedPreferences("downloadMessage",Context.MODE_PRIVATE);
        String name = spf.getString(id,"当前无已下载项");

        viewHolder.chapterName.setText(name);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DownloadContentActivity.class);
                intent.putExtra("fileId",id);
                intent.putExtra("fileName",name);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mChapterList.size();
    }
}
