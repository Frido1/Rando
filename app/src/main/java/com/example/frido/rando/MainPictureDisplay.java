package com.example.frido.rando;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frido.rando.Database.RandoDatabaseContract;
import com.example.frido.rando.Database.RandoDbHelper;
import com.example.frido.rando.Objects.RandoPicture;
import com.example.frido.rando.Utilities.CustomImageViewAdapater;
import com.example.frido.rando.Utilities.saveBitmap;



import java.util.ArrayList;
import java.util.Random;

import in.arjsna.swipecardlib.SwipeCardView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainPictureDisplay extends Activity {

    private  String imageURLFatPita = "http://fatpita.net/images/image%20";
    private final int Total_FATPITA_Images = 20240;
    private ArrayList<String> imagesToLoad = new ArrayList<String>();
    private saveBitmap saveBitmap;
    private String thumbnailName;
    private SQLiteDatabase db;
    private SwipeCardView mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: 1/20/2017 Because they can be long-running, be sure that you call getWritableDatabase() or getReadableDatabase() in a background thread, such as with AsyncTask or IntentService.
        RandoDbHelper dbHelper = new RandoDbHelper(getApplicationContext());
        db = dbHelper.getWritableDatabase();

        setContentView(R.layout.activity_main_picture_display);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mContentView = (SwipeCardView) findViewById(R.id.flingContainerFrame);


        final ThumbnailUtils thumbnailUtils = new ThumbnailUtils();

        //load image into main content view
        imagesToLoad = getImageURLS();
        final CustomImageViewAdapater adapater = new CustomImageViewAdapater(getApplicationContext(),imagesToLoad);
        mContentView.setAdapter(adapater);
        mContentView.setFlingListener(new SwipeCardView.OnCardFlingListener() {
            @Override
            public void onCardExitLeft(Object dataObject) {
                makeToast(getApplicationContext(),"Left");
            }

            @Override
            public void onCardExitRight(Object dataObject) {
                makeToast(getApplicationContext(),"Right");
                ImageView tempView = (ImageView) mContentView.getChildAt((mContentView.getChildCount()-1));
                Bitmap bi = ((BitmapDrawable) tempView.getDrawable()).getBitmap();
                Bitmap  bitmap =thumbnailUtils.extractThumbnail(bi,250,250);
                thumbnailName = saveBitmap.createThumbnailFileName(dataObject.toString());

                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(RandoDatabaseContract.RandoDatabase.COLUMN_URL, dataObject.toString());
                values.put(RandoDatabaseContract.RandoDatabase.COLUMN_THUMBNAIL_ID,thumbnailName);
                db.insert(RandoDatabaseContract.RandoDatabase.TABLE_NAME,null,values);

                //save thumbnail file
                saveBitmap = new saveBitmap(thumbnailName,bitmap,getApplicationContext());
                saveBitmap.save();

                //setup FireBase DB


            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                imagesToLoad .add(getOneMoreImage());
                adapater.notifyDataSetChanged();
            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }

            @Override
            public void onCardExitTop(Object dataObject) {
                makeToast(getApplicationContext(),"Top");

            }

            @Override
            public void onCardExitBottom(Object dataObject) {
                makeToast(getApplicationContext(),"Bottom");

            }
        });


        mContentView.setOnItemClickListener(new SwipeCardView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Intent intent = new Intent(getApplicationContext(),ImageViewFullscreen.class);
                intent.putExtra("imageToView", ""+dataObject);
                String filename =  saveBitmap.createFilename((String) dataObject);
                intent.putExtra("fileName",filename);
                ImageView tempView = (ImageView) mContentView.getSelectedView();
                Bitmap bi = ((BitmapDrawable) tempView.getDrawable()).getBitmap();
                saveBitmap = new saveBitmap(filename,bi,getApplicationContext());
                saveBitmap.save();

                startActivity(intent);

            }
        });



    }

    private void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    private String getOneMoreImage() {
        Random rand = new Random();
        String placeHolder = imageURLFatPita;
        int fatPitaImage =  rand.nextInt(Total_FATPITA_Images)+1;
        placeHolder = placeHolder+"("+fatPitaImage+").jpg";
        return placeHolder ;
    }

    private ArrayList<String> getImageURLS() {
        Random rand = new Random();
        ArrayList<String> temp = new ArrayList<String>();
        String tempURL;
        for (int i = 0; i < 10 ; i++) {
            int fatPitaImage =  rand.nextInt(Total_FATPITA_Images)+1;
            tempURL = imageURLFatPita+"("+fatPitaImage+").jpg";
            temp.add(tempURL);
        }
        return temp;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
