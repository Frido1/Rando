package com.example.frido.rando.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.frido.rando.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by fjmar on 1/31/2017.
 */

public class RecycleViewerAdapter extends RecyclerView.Adapter<RecycleViewerAdapter.ViewHolder> {
    private ArrayList<String> thumbnails;
    private File filePath;
    private Context context;

    public RecycleViewerAdapter(ArrayList<String> thumbnails,Context context) {
        this.thumbnails = thumbnails;
        this.context = context;
    }



    @Override
    public RecycleViewerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staggeredgrid_item,parent,false);
        RecyclerView.ViewHolder vh = new ViewHolder(v);
        return (ViewHolder) vh;
    }

    @Override
    public void onBindViewHolder(RecycleViewerAdapter.ViewHolder holder, int position) {
        final String thumbnailLocation = thumbnails.get(position);
        filePath = context.getFileStreamPath(thumbnailLocation);

       Glide.with(context).load(filePath).into(holder.gridItem);
    }

    @Override
    public int getItemCount() {
        return thumbnails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.straggeredgrid_image)
        ImageView gridItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
