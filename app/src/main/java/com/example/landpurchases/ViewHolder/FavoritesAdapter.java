package com.example.landpurchases.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.landpurchases.Common.Common;
import com.example.landpurchases.Database.Database;
import com.example.landpurchases.Interface.ItemClickListener;
import com.example.landpurchases.LandDetails;
import com.example.landpurchases.Models.Favorites;
import com.example.landpurchases.Models.LandOrder;
import com.example.landpurchases.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Favorites> favoritesList;

    public FavoritesAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favorites_item,viewGroup,false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder viewHolder, final int i) {


        viewHolder.land_name.setText(favoritesList.get(i).getLandLocationName());
//        viewHolder.food_price.setText(String.format("$ %s", favoritesList.get(i).getFoodPrice().toString()));
        Picasso.with(context).load(favoritesList.get(i).getLandImage())
                .into(viewHolder.land_image);

        /**quick cart**/

        viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isExists = new Database(context).checkLandExists(favoritesList.get(i).getLand(), Common.currentUser.getPhone());
                if (!isExists) {
                    new Database(context).addToCart(new LandOrder(

                            Common.currentUser.getPhone(),
                            favoritesList.get(i).getLand(),
                            favoritesList.get(i).getLandLocationName(),
                            "1",
                            favoritesList.get(i).getLandPrice(),
                            favoritesList.get(i).getSizeOfLand(),
                            favoritesList.get(i).getLandImage()
                    ));
                }

                else
                {
                    new Database(context).increaseCart(Common.currentUser.getPhone(),
                            favoritesList.get(i).getLand());

                }
                Toast.makeText(context, "Added to Cart", Toast.LENGTH_SHORT).show();

            }

        });

        final Favorites local = favoritesList.get(i);
        viewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                /**start new activity**/
                Intent foodDetail = new Intent(context, LandDetails.class);
                foodDetail.putExtra("LandId", favoritesList.get(i).getLand());
                context.startActivity(foodDetail);


            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void removeItem(int position) {
        favoritesList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Favorites item,int position) {
        favoritesList.add(position,item);
        notifyItemInserted(position);
    }

    public Favorites getItem(int position){
        return favoritesList.get(position);
    }

}
