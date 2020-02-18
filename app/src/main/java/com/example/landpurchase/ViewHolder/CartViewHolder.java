package com.example.landpurchase.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Interface.ItemClickListener;
import com.example.landpurchase.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {

    public TextView txt_cart_name,txt_price;
    public ElegantNumberButton btn_SizeOfLand;
    public ImageView cart_image;

    public RelativeLayout view_background;
    public LinearLayout view_forebackground;

    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name){
        this.txt_cart_name=txt_cart_name;
    }
    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = (TextView)itemView.findViewById(R.id.cart_item_name);
        txt_price = (TextView)itemView.findViewById(R.id.cart_item_price);
        btn_SizeOfLand = (ElegantNumberButton) itemView.findViewById(R.id.btn_sizeOfLand);
        cart_image = (ImageView) itemView.findViewById(R.id.cart_image);
        view_background =(RelativeLayout)itemView.findViewById(R.id.view_background);
        view_forebackground = (LinearLayout)itemView.findViewById(R.id.view_foreground);

        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select Action");
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}
