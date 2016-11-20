package com.example.frido.rando;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.frido.rando.Utilities.saveBitmap;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;


import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

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
    private SwipeFlingAdapterView mContentView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_picture_display);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = (SwipeFlingAdapterView) findViewById(R.id.flingContainerFrame);



        // Set up the user interaction to manually show or hide the system UI.



        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        //load image into main content view
        imagesToLoad = getImageURLS();
        final CustomImageViewAdapater adapater = new CustomImageViewAdapater(getApplicationContext(),imagesToLoad);
        mContentView.setAdapter(adapater);
        mContentView.setFlingListener
                (new SwipeFlingAdapterView.onFlingListener() {
                    @Override
                    public void removeFirstObjectInAdapter() {
                        // this is the simplest way to delete an object from the Adapter (/AdapterView)
                        Log.d("LIST", "removed object!");
                        imagesToLoad.remove(0);
                        adapater.notifyDataSetChanged();
                    }





                    @Override
                    public void onLeftCardExit(Object dataObject) {
                        //Do something on the left!
                        //You also have access to the original object.
                        //If you want to use it just cast it (String) dataObject
                        Toast.makeText(MainPictureDisplay.this, "Left!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRightCardExit(Object dataObject) {
                        Toast.makeText(MainPictureDisplay.this, "Right!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdapterAboutToEmpty(int i) {
                        imagesToLoad.add(getOneMoreImage());
                        adapater.notifyDataSetChanged();
                        Log.d("LIST", "notified");
                    }


                    @Override
                    public void onScroll(float v) {

                    }

                });


        mContentView.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int i, Object o) {
                Intent intent = new Intent(getApplicationContext(),ImageViewFullscreen.class);
                intent.putExtra("imageToView", ""+o);
                String filename =  saveBitmap.createFilename((String) o);
                intent.putExtra("fileName",filename);
                ImageView tempView = (ImageView) mContentView.getSelectedView();
                Bitmap bi = ((BitmapDrawable) tempView.getDrawable()).getBitmap();
                saveBitmap = new saveBitmap(filename,bi,getApplicationContext());
                saveBitmap.save();

                startActivity(intent);


            }
        });




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
}
