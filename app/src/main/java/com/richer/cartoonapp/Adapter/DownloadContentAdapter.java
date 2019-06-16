package com.richer.cartoonapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.richer.cartoonapp.Beans.Content;
import com.richer.cartoonapp.R;

import java.io.File;
import java.util.List;

public class DownloadContentAdapter extends RecyclerView.Adapter<DownloadContentAdapter.ViewHolder> {

    private Context mContext;
    private List<File> mContentList;

    @NonNull
    @Override
    public DownloadContentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.download_content_item,viewGroup,false);
        return new DownloadContentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        File file = mContentList.get(i);
        Glide.with(mContext).load(file).into(viewHolder.contentImage);
    }

    @Override
    public int getItemCount() {
        return mContentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView contentImage;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            contentImage = view.findViewById(R.id.download_content_image);
        }
    }
    public DownloadContentAdapter(List<File> contentList){
        mContentList = contentList;
    }
}
