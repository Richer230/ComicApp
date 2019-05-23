package com.richer.cartoonapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.richer.cartoonapp.Acitivity.ContentActivity;
import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.R;
import com.richer.cartoonapp.Util.DateUtil;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private Context mContext;

    private List<Chapters> mChapterList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if(mContext==null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.chapter_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Chapters chapter = mChapterList.get(i);
        viewHolder.chapterName.setText(chapter.getName());
        DateUtil myDate = new DateUtil();
        long time = chapter.getPublishTime()*1000;
        viewHolder.chapterPublishTime.setText(myDate.getYear(time)+"-"+myDate.getMonth(time)+"-"+myDate.getDay(time));
        Glide.with(mContext).load(chapter.getSmallPlaceCover()).into(viewHolder.chapterImg);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ContentActivity.class);
                intent.putExtra("chapter_id",chapter.getChapterId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mChapterList.size();
    }

    public ChapterAdapter(List<Chapters> chaptersList){
        mChapterList = chaptersList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        ImageView chapterImg;
        TextView chapterName;
        TextView chapterPublishTime;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            chapterImg = view.findViewById(R.id.chapter_image);
            chapterName = view.findViewById(R.id.chapter_name);
            chapterPublishTime = view.findViewById(R.id.chapter_publishTime);
        }

    }

}
