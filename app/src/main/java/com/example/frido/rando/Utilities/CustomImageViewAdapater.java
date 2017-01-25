package com.example.frido.rando.Utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.frido.rando.R;
import com.example.frido.rando.Utilities.CroppingTransformation;

import java.util.ArrayList;

/**
 * Created by fjmar on 11/16/2016.
 */

public class CustomImageViewAdapater extends ArrayAdapter {
     private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imageUrls;
    private ImageView imageViewTouch;
    private   ViewHolder holder;

    public CustomImageViewAdapater(Context context, ArrayList<String> imageUrls) {
        super(context, R.layout.pictures, imageUrls);

        this.context = context;
        this.imageUrls = imageUrls;

        inflater = LayoutInflater.from(context);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.pictures, parent, false);
        }

        imageViewTouch = (ImageView) convertView.findViewById(R.id.fullscreen_content);

        //store the view to access in other classes
        holder = new ViewHolder();
        holder.imageUrl = imageUrls.get(position);
        holder.imageview = imageViewTouch;



        Glide.with(context)
                .load(imageUrls.get(position))
                .asBitmap()
              //  .placeholder(R.mipmap.ic_launcher)
                .thumbnail(0.1f)
                .transform(new CroppingTransformation(context))
                .into(imageViewTouch);





        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public String getItem(int position) {
        return imageUrls.get(position);
    }
    public void setImageUrls(ArrayList<String> imageUrls){
        this.imageUrls= imageUrls;
    }

    static  class  ViewHolder{
    ImageView imageview;
    String imageUrl;
}

}
