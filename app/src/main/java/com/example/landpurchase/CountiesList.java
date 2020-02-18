package com.example.landpurchase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Interface.ItemClickListener;
import com.example.landpurchase.Models.County;
import com.example.landpurchase.ViewHolder.CountyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CountiesList extends AppCompatActivity {

    AlertDialog waitingDialog;
    RecyclerView recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;



    FirebaseRecyclerOptions<County> options = new FirebaseRecyclerOptions.Builder<County>()
            .setQuery(FirebaseDatabase.getInstance()
                            .getReference()
                            .child("Counties")
                    ,County.class)
            .build();

    FirebaseRecyclerAdapter<County, CountyViewHolder> adapter = new FirebaseRecyclerAdapter<County, CountyViewHolder>(options) {

        @NonNull
        @Override
        public CountyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.county_item,viewGroup,false);
            return new CountyViewHolder(itemView);

        }
        @Override
        protected void onBindViewHolder(@NonNull CountyViewHolder holder, int position, @NonNull County model) {

            holder.txt_county_name.setText(model.getName());
            Picasso.with(getBaseContext()).load(model.getImage())
                    .into(holder.img_county);

            final County clickItem =model;
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {


                    Intent landList = new Intent(CountiesList.this,Home.class);

                    /**When user select restaurant id to select category of this restaurant**/
                    Common.countySelected = adapter.getRef(position).getKey();
                    startActivity(landList);

                }
            });
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counties_list);

        /**View**/
        mSwipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext()))
                    loadRestaurant();

                else{
                    Toast.makeText(getBaseContext(), "Please check your connection!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        /**Default ,load for first time**/
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

                if (Common.isConnectedToInternet(getBaseContext()))
                    loadRestaurant();

                else{
                    Toast.makeText(getBaseContext(), "Please check your connection!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
        recyclerView =(RecyclerView)findViewById(R.id.recycler_county);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadRestaurant() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);

        /**Animation**/
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
