package com.richer.cartoonapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.R;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private Context mContext;

    private List<Chapters> mChapterList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView downloadImage;
        TextView chapterName;
        TextView status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (CardView) itemView;
            downloadImage = itemView.findViewById(R.id.download_image);
            chapterName = itemView.findViewById(R.id.download_chapter_name);
            status = itemView.findViewById(R.id.download_status);
        }
    }

    public DownloadAdapter(List<Chapters> chaptersList){
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
        Chapters chapter = mChapterList.get(i);

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
