package com.example.frido.rando;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.frido.rando.Database.RandoDatabaseContract;
import com.example.frido.rando.Database.RandoDbHelper;
import com.example.frido.rando.Utilities.CustomArrayAdapter;

import java.util.ArrayList;

import quatja.com.vorolay.VoronoiView;

public class HistoryGallery extends Activity {
    private  LayoutInflater layoutInflater;
    private ArrayList<String> listUrls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_gallery);
        ListView listView = (ListView) findViewById(R.id.vorolayListView);
        listUrls = getURLSFromDatabase();
        listView.setAdapter(new CustomArrayAdapter(getApplicationContext(),listUrls));

    }

    private ArrayList<String> getURLSFromDatabase() {
        ArrayList<String> listUrls = new ArrayList<String>();
        RandoDbHelper dbHelper = new RandoDbHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        String[] projection = {
                RandoDatabaseContract.RandoDatabase.COLUMN_THUMBNAIL_ID,
        };
        Cursor cursor = sqLiteDatabase.query(
                RandoDatabaseContract.RandoDatabase.TABLE_NAME,projection,null,null,null,null,null
        );
        while (cursor.moveToNext()){
           String temp =  cursor.getString(cursor.getColumnIndex(RandoDatabaseContract.RandoDatabase.COLUMN_THUMBNAIL_ID));
           listUrls.add(temp);

        }
        return listUrls;
    }
}
