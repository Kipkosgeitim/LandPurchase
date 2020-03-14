package com.example.landpurchases.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.landpurchases.Cart;
import com.example.landpurchases.Common.Common;
import com.example.landpurchases.Database.Database;
import com.example.landpurchases.Models.LandOrder;
import com.example.landpurchases.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{
    private List<LandOrder> listData =new ArrayList<>();
    private Cart cart;
    private ViewGroup parent;
    private int position;


    public CartAdapter(List<LandOrder> listdata, Cart cart) {
        this.listData = listdata;
        this.cart = cart;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return  new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder cartViewHolder, int i) {
      //  TextDrawable drawable =TextDrawable.builder()
        //        .buildRound(""+listData.get(position).getQuantity(), Color.RED);
        //cartViewHolder.img_cart_count.setImageDrawable(drawable);

        Picasso.with(cart.getBaseContext())
                .load(listData.get(position).getImage())
                .resize(70,70)
                .centerCrop()
                .into(cartViewHolder.cart_image);
        cartViewHolder.btn_SizeOfLand.setNumber(listData.get(position).getSizeOfLand());

        cartViewHolder.btn_SizeOfLand.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                LandOrder order = listData.get(position);
                order.setSizeOfLand(String.valueOf(newValue));
                new Database(cart).updateCart(order);

                int total =0;
                List<LandOrder> orders = new Database(cart).getCarts(Common.currentUser.getPhone());
                for (LandOrder item:orders)
                    total+=(Integer.parseInt(order.getPrice()))*(Integer.parseInt(item.getSizeOfLand()));
                Locale locale = new Locale("en","US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
                cart.txtTotalPrice.setText(fmt.format(total));
            }
        });
        Locale locale = new Locale("en","US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()))*(Integer.parseInt(listData.get(position).getSizeOfLand()));
        cartViewHolder.txt_price.setText(fmt.format(price));
        cartViewHolder.txt_cart_name.setText(listData.get(position).getLandNameLocation());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public LandOrder getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position)
    {
        listData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(LandOrder item,int position)
    {
        listData.add(position,item);
        notifyItemInserted(position);
    }
}
