package com.richer.cartoonapp.Beans;

import org.litepal.crud.LitePalSupport;

public class Chapters extends LitePalSupport {

    private String name;
    private int imageTotle;
    private int type;
    private int chapterId;
    private long publishTime;
    private int chapterIndex;
    private String smallPlaceCover;
    private int index;


    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageTotle() {
        return imageTotle;
    }

    public void setImageTotle(int imageTotle) {
        this.imageTotle = imageTotle;
    }

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public void setChapterIndex(int chapterIndex) {
        this.chapterIndex = chapterIndex;
    }

    public String getSmallPlaceCover() {
        return smallPlaceCover;
    }

    public void setSmallPlaceCover(String smallPlaceCover) {
        this.smallPlaceCover = smallPlaceCover;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
