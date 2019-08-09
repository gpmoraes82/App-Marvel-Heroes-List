package com.example.appmarvelheroeslist.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appmarvelheroeslist.Model.Heroes_Pojo;
import com.example.appmarvelheroeslist.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//---- HeroesAdapter will handle the data in cell of its correspondent RecyclerView ----
public class HeroesAdapter extends RecyclerView.Adapter<HeroesAdapter.HeroViewHolder>{

    // ---- Atributes declaration ----
    private ArrayList<Heroes_Pojo> heroesPojoArrayList;
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    private LayoutInflater layoutInflater;


    public HeroesAdapter(Context mContext, ArrayList<Heroes_Pojo> heroesArrayList, OnItemClickListener onItemClickListener) {
        layoutInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;

        this.heroesPojoArrayList = heroesArrayList;

        mOnItemClickListener = onItemClickListener;

    }

    //---- Inflate the cell with a Hero data ----
    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = layoutInflater.inflate(R.layout.item_hero, viewGroup, false);
        return new HeroViewHolder(view, mOnItemClickListener);

    }

    //---- Add Hero data to the cell ----
    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder heroListViewHolder, int i) {

        Heroes_Pojo currentHero = heroesPojoArrayList.get(i);

        String heroName = currentHero.getHeroName();
        String heroDescription = currentHero.getHeroDescription();
        String heroImageUrl = currentHero.getHeroImageUrl();

        heroListViewHolder.heroNameTextView.setText(heroName);
        heroListViewHolder.heroDescriptionTextView.setText(heroDescription);

        Picasso.with(mContext).load(heroImageUrl).fit().centerInside().into(heroListViewHolder.heroImageView);

    }

    //---- Get Hero list size ----
    @Override
    public int getItemCount() {
        return heroesPojoArrayList.size();
    }


    //---- Set the data on the layout ----
    public class HeroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView heroNameTextView;
        TextView heroDescriptionTextView;
        ImageView heroImageView;

        OnItemClickListener onItemClickListener;

        public HeroViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            heroNameTextView = itemView.findViewById(R.id.textView_heroName);
            heroDescriptionTextView = itemView.findViewById(R.id.textView_hero_description);
            heroImageView = itemView.findViewById(R.id.imageView_heroThumb);

            this.onItemClickListener = onItemClickListener;

            //--- Add the click component to send data to HeroDetailActivity ----
            itemView.setOnClickListener(this);

            itemView.setTag(itemView);

        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    //---- Keep thq selected item track on click ----
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
