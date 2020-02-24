package com.example.landpurchase.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landpurchase.Interface.ItemClickListener;
import com.example.landpurchase.R;

public class LandViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView land_name,land_price;
    public ImageView land_image,fav_image,share_image,quick_cart;
    private ItemClickListener itemClickListener;

    public LandViewHolder(@NonNull View itemView) {
        super(itemView);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

        land_name = itemView.findViewById(R.id.land_address_name);
        land_image = itemView.findViewById(R.id.land_image);
        fav_image = itemView.findViewById(R.id.fav);
        share_image = itemView.findViewById(R.id.btnShare);
        land_price = itemView.findViewById(R.id.land_price);
        quick_cart = itemView.findViewById(R.id.btn_quick_cart);

        itemView.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

    }
}
