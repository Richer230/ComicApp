package com.richer.cartoonapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.richer.cartoonapp.Acitivity.ChapterActivity;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder> {

    private Context mContext;

    private List<Comics> mComicList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if(mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.comic_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Comics comic = mComicList.get(i);
        viewHolder.comicName.setText(comic.getName());
        viewHolder.comicAuthor.setText(comic.getAuthor());
        viewHolder.comicTags.setText(comic.getTags());
        viewHolder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ChapterActivity.class);
            intent.putExtra("comic_name",comic.getName());
            intent.putExtra("comic_author",comic.getAuthor());
            intent.putExtra("comic_tags",comic.getTags());
            intent.putExtra("comic_cover",comic.getCover());
            intent.putExtra("comic_id",comic.getComicId());
            intent.putExtra("comic_description",comic.getDescription());
            mContext.startActivity(intent);
        });
        Glide.with(mContext).load(comic.getCover()).into(viewHolder.comicImage);
    }

    @Override
    public int getItemCount() {
        return mComicList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView comicImage;
        TextView comicName;
        TextView comicAuthor;
        TextView comicTags;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            comicImage = view.findViewById(R.id.comic_image);
            comicName = view.findViewById(R.id.comic_name);
            comicAuthor = view.findViewById(R.id.comic_author);
            comicTags = view.findViewById(R.id.comic_tags);
        }
    }

    public ComicAdapter(List<Comics> comicsList){
        mComicList = comicsList;
    }



}
