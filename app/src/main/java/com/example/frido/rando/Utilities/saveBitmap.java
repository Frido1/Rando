package com.example.frido.rando.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fjmar on 11/19/2016.
 */

public class saveBitmap {
    private String filename;
    private Bitmap bitmap;
    private  Context context;
    private final int MODE_PRIVATE =0;
    public saveBitmap() {
    }
    public saveBitmap(String filename, Bitmap bitmap, Context context){
        this.filename = filename;
        this.bitmap = bitmap;
        this.context = context;
    }


    public void save(){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        try {
            String fileName = filename;
            FileOutputStream fileOutStream = context.openFileOutput(fileName, MODE_PRIVATE);
            fileOutStream.write(b);  //b is byte array
            //(used if you have your picture downloaded
            // from the *Web* or got it from the *devices camera*)
            //otherwise this technique is useless
            fileOutStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static String createFilename(String url){
        //http://fatpita.net/images/image%20(4378).jpg
        String temp = url;
        int start = temp.lastIndexOf("(");
        temp = temp.substring(start);
        temp = temp.replace("(","");
        temp = temp.replace(")","");
        return  temp;
    }

}
