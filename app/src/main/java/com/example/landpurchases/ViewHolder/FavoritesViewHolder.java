package com.example.landpurchases.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.landpurchases.Interface.ItemClickListener;
import com.example.landpurchases.R;


public class FavoritesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView land_name,land_price;
public ImageView land_image,fav_image,share_image,quick_cart;
private ItemClickListener itemClickListener;

public RelativeLayout view_background;
public LinearLayout view_forebackground;

public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;


        }

public FavoritesViewHolder(@NonNull View itemView) {
        super(itemView);

        land_name = itemView.findViewById(R.id.land_name);
        land_image = itemView.findViewById(R.id.land_image);
        fav_image = itemView.findViewById(R.id.fav);
        share_image = itemView.findViewById(R.id.btnShare);
        land_price = itemView.findViewById(R.id.land_price);
        quick_cart = itemView.findViewById(R.id.btn_quick_cart);

        view_background = itemView.findViewById(R.id.view_background);
        view_forebackground = itemView.findViewById(R.id.view_foreground);

        itemView.setOnClickListener(this);
        }

@Override
public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(),false);

        }
        }

