package com.example.frido.rando;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

/**
 * Created by fjmar on 11/18/2016.
 */

public class TouchZoomSwipeFlingAdapterView extends SwipeFlingAdapterView {
    public TouchZoomSwipeFlingAdapterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public TouchZoomSwipeFlingAdapterView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(event.getPointerCount() == 1) {
            onTouchEvent(event);
        }
        return false;
    }
}

/*public MyImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
}

public MyImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
}*/