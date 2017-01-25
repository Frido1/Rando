package com.example.frido.rando.Utilities;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.frido.rando.R;

import java.util.ArrayList;
import java.util.Random;

import quatja.com.vorolay.VoronoiView;

/**
 * Created by fjmar on 1/22/2017.
 */

public class CustomArrayAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<String> imageUrls = new ArrayList<String>();
    private ViewHolderGallery viewHolderGallery;


    public CustomArrayAdapter (Context context, ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolderGallery  holder;
        if (null == convertView) {
            convertView = layoutInflater.inflate(R.layout.item_voronoi, parent, false);

            holder = new ViewHolderGallery();
            convertView.setTag(viewHolderGallery);
            holder.imageVoroni = (VoronoiView) convertView.findViewById(R.id.imageVoroni);
            for (int i = 0; i < 15; i++) {
                View view = layoutInflater.inflate(R.layout.item_list_voronoi, null, false);
                holder.imageVoroni.addView(view);

                View actualImage = view.findViewById(R.id.layoutVornoni);
                int randomColor = getRandomColor();
                actualImage.setBackgroundColor(randomColor);
            }
        }
        return convertView;
    }

    static class ViewHolderGallery {
        private VoronoiView imageVoroni;
        private TextView textview;

    }

    public int getRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
