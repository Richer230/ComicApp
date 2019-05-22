package com.richer.cartoonapp.Beans;

import org.litepal.crud.LitePalSupport;

public class Content extends LitePalSupport {

    private String location;
    private long image_id;
    private int type;
    private String img05;
    private String img50;


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getImage_id() {
        return image_id;
    }

    public void setImage_id(long image_id) {
        this.image_id = image_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImg05() {
        return img05;
    }

    public void setImg05(String img05) {
        this.img05 = img05;
    }

    public String getImg50() {
        return img50;
    }

    public void setImg50(String img50) {
        this.img50 = img50;
    }
}
