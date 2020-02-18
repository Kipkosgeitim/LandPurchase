package com.example.landpurchase.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landpurchase.Interface.ItemClickListener;
import com.example.landpurchase.R;


public class CountyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txt_county_name;
    public ImageView img_county;
    private ItemClickListener itemClickListener;

    public CountyViewHolder(@NonNull View itemView) {
        super(itemView);

        txt_county_name = (TextView)itemView.findViewById(R.id.county_name);
        img_county = (ImageView)itemView.findViewById(R.id.county_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
}
