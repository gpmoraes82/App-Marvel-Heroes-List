package com.example.appmarvelheroeslist.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmarvelheroeslist.Model.Comics_Pojo;
import com.example.appmarvelheroeslist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ComicViewHolder> {

    // ---- Attributes declaration ----
    private ArrayList<Comics_Pojo> comicPojoArrayList;
    private Context mContext;

    private LayoutInflater layoutInflater;


    public ComicsAdapter(Context mContext, ArrayList<Comics_Pojo> comicPojoArrayList) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;

        this.comicPojoArrayList = comicPojoArrayList;

    }


    //---- Inflate the cell with a Comic data ----
    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.item_comic, viewGroup, false);
        return new ComicViewHolder(view);

    }

    //---- Add Comic data to the cell ----
    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder comicViewHolder, int i) {

        Comics_Pojo current  = comicPojoArrayList.get(i);

        String comicImageUrl = current.getComicImageUrl();
        String comicTitle = current.getComicTitle();
        comicViewHolder.comicNameTextView.setText(comicTitle);

        Picasso.with(mContext).load(comicImageUrl).fit().centerInside().into(comicViewHolder.comicImageView);

    }

    //---- Get Comic list size ----
    @Override
    public int getItemCount() {
        return comicPojoArrayList.size();
    }

    //---- Set the data on the layout ----
    public class ComicViewHolder extends RecyclerView.ViewHolder{

        public ImageView comicImageView;
        public TextView comicNameTextView;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);

            comicImageView = itemView.findViewById(R.id.imageView_comicThumb);
            comicNameTextView = itemView.findViewById(R.id.textView_comicTitle);

            itemView.setTag(itemView);

        }

    }
}
