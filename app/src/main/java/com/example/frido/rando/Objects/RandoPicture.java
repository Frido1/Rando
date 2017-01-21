package com.example.frido.rando.Objects;


/**
 * Created by fjmar on 1/20/2017.
 */


public class RandoPicture  {

    private String  url;
    private String thumbnail_ID;

    public RandoPicture(){

    }

    public RandoPicture(String url, String thumbnail_ID) {
        this.url = url;
        this.thumbnail_ID = thumbnail_ID;
    }
}
