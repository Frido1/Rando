package com.example.frido.rando;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frido.rando.Objects.RandoPicture;
import com.example.frido.rando.Utilities.CustomImageViewAdapater;
import com.example.frido.rando.Utilities.saveBitmap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    private SwipeCardView mContentView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


                //save thumbnail file
                saveBitmap = new saveBitmap(thumbnailName,bitmap,getApplicationContext());
                saveBitmap.save();

                //setup FireBase DB
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference();
                RandoPicture randoPicture = new RandoPicture(dataObject.toString(),thumbnailName);
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference.child("users").child(userID).push().setValue(randoPicture);




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

}
