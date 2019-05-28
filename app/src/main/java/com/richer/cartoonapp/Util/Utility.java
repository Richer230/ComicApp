package com.richer.cartoonapp.Util;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.richer.cartoonapp.Beans.Chapters;
import com.richer.cartoonapp.Beans.Comics;
import com.richer.cartoonapp.Beans.Content;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Utility {

    public static long getLastUpdateTime(String response,int comicId){
        long lastUpdateTime = 0;
        if(!TextUtils.isEmpty(response)){
            try{
                JSONObject messages = new JSONObject(response);
                JSONObject datas = messages.getJSONObject("data");
                JSONObject returnData = datas.getJSONObject("returnData");
                JSONObject comic = returnData.getJSONObject("comic");
                lastUpdateTime = comic.getLong("last_update_time");
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return lastUpdateTime;
    }

    public static List<Comics> handleComicsResponse(String response){
        String address;
        List<Comics> comicsList = new ArrayList<>();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONObject messages = new JSONObject(response);
                JSONObject datas = messages.getJSONObject("data");
                JSONObject returnData = datas.getJSONObject("returnData");
                JSONArray allcomics = returnData.getJSONArray("comics");
                for(int i=0;i<allcomics.length();i++){
                    JSONObject comicObject = allcomics.getJSONObject(i);
                    Comics comic = new Comics();
                    comic.setCover(comicObject.getString("cover"));
                    comic.setName(comicObject.getString("name"));
                    comic.setComicId(comicObject.getInt("comicId"));
                    comic.setDescription(comicObject.getString("description"));
                    comic.setAuthor(comicObject.getString("author"));
                    JSONArray tagArray = comicObject.getJSONArray("tags");
                    String tags= tagArray.get(0).toString();
                    for(int j=1;j<tagArray.length();j++){
                        tags = tags+"/"+tagArray.get(j).toString();
                        comic.setTags(tags);
                    }
                    List<Comics> list = LitePal.where("comicId = ?",String.valueOf(comic.getComicId())).find(Comics.class);
                    if(list.size()!=0){
                        for(int k=0;k<list.size();k++){
                            comic.setId(list.get(k).getId());
                            LitePal.delete(Comics.class,list.get(k).getId());
                            comic.setSubscribed(list.get(k).isSubscribed());
                        }
                    }
                    comic.save();
                    comicsList.add(comic);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return comicsList;
    }

    public static List<Chapters> handleChapterResponse(String response){
        List<Chapters> chaptersList = new ArrayList<>();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONObject messages = new JSONObject(response);
                JSONObject datas = messages.getJSONObject("data");
                JSONObject returnData = datas.getJSONObject("returnData");
                JSONArray allchapters = returnData.getJSONArray("chapter_list");
                for(int i=0;i<allchapters.length();i++){
                    JSONObject chapterObject = allchapters.getJSONObject(i);
                    Chapters chapter = new Chapters();
                    chapter.setName(chapterObject.getString("name"));
                    chapter.setImageTotle(chapterObject.getInt("image_total"));
                    chapter.setChapterId(chapterObject.getInt("chapter_id"));
                    chapter.setPublishTime(chapterObject.getLong("publish_time"));
                    chapter.setSmallPlaceCover(chapterObject.getString("smallPlaceCover"));
                    chapter.setChapterIndex(chapterObject.getInt("chapterIndex"));
                    chapter.setIndex(chapterObject.getInt("index"));
                    chapter.setType(chapterObject.getInt("type"));
                    if(chapter.getType()==0){
                        chapter.save();
                        chaptersList.add(chapter);
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return chaptersList;
    }

    public static List<Content> handleContentResponse(String response){
        List<Content> contentList = new ArrayList<>();
        if(!TextUtils.isEmpty(response)){
            try{
                JSONObject messages = new JSONObject(response);
                JSONObject datas = messages.getJSONObject("data");
                JSONObject returnData = datas.getJSONObject("returnData");
                JSONArray allContent = returnData.getJSONArray("image_list");
                for(int i=0;i<allContent.length();i++){
                    JSONObject contentObject = allContent.getJSONObject(i);
                    Content content = new Content();
                    content.setLocation(contentObject.getString("location"));
                    content.setImage_id(contentObject.getInt("image_id"));
                    content.setImg05(contentObject.getString("img05"));
                    content.setImg50(contentObject.getString("img50"));
                    content.setType(contentObject.getInt("type"));
                    content.save();
                    contentList.add(content);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return contentList;
    }

}
