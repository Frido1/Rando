package com.example.frido.rando.Objects;


/**
 * Created by fjmar on 1/20/2017.
 */


public class RandoPicture  {

    private String  url;
    private String thumbnail_ID;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_ID() {
        return thumbnail_ID;
    }

    public void setThumbnail_ID(String thumbnail_ID) {
        this.thumbnail_ID = thumbnail_ID;
    }

    public RandoPicture(){


    }

    public RandoPicture(String url, String thumbnail_ID) {
        this.url = url;
        this.thumbnail_ID = thumbnail_ID;
    }
}
