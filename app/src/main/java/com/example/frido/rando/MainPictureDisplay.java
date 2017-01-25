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
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.frido.rando.Database.RandoDatabaseContract;
import com.example.frido.rando.Database.RandoDbHelper;
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
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private SwipeCardView mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.

            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };
    private  String imageURLFatPita = "http://fatpita.net/images/image%20";
    private final int Total_FATPITA_Images = 20240;
    private ArrayList<String> imagesToLoad = new ArrayList<String>();
    private saveBitmap saveBitmap;
    private String thumbnailName;
    private SQLiteDatabase db;

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

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (SwipeCardView) findViewById(R.id.flingContainerFrame);


        final ThumbnailUtils thumbnailUtils = new ThumbnailUtils();




        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
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

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }
}
