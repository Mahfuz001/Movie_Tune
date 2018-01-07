package com.mahfuz.movietune;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by mahfuz on 7/1/18.
 */

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    Context mContext;
    List<String> imageUrls = Collections.emptyList();
    List<Integer> ids = Collections.emptyList();

    public RecyclerAdapter(Context context, List<Integer> id, List<String> poster_path) {
        this.mContext = context;
        this.ids = id;
        this.imageUrls = poster_path;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mContext);

        return new MyViewHolder(inflater.inflate(R.layout.custom_row,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Picasso.with(mContext).load(imageUrls.get(position)).into(holder.mImageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageView);
        }
    }
}