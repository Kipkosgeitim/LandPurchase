package com.example.landpurchase;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Models.Rating;
import com.example.landpurchase.ViewHolder.ShowCommentViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ShowComment extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference ratingTbl;

    SwipeRefreshLayout mSwipeRefreshLayout;


    FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder> adapter;

    String  landId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_comment);

        /**
         * FireBase
         *
         */
        database = FirebaseDatabase.getInstance();
        ratingTbl = database.getReference("Rating");

        recyclerView = (RecyclerView)findViewById(R.id.recycleComment);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        /**
         * Swipe layout
         *
         */

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onRefresh() {
                if (getIntent() !=null)
                    landId =getIntent().getStringExtra(Common.INTENT_LAND_ID);
                if (!landId.isEmpty() && landId !=null)
                {
                    /**
                     * create request query
                     *
                     */
                    Query query = ratingTbl.orderByChild("landId").equalTo(landId);
                    FirebaseRecyclerOptions<Rating> options =new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(ShowCommentViewHolder holder, int position, Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserPhone.setText(model.getUserPhone());

                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view = LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.show_comment_layout,viewGroup,false);
                            return new ShowCommentViewHolder(view);
                        }
                    };

                    loadComment(landId);
                }
            }
        });

        /**
         * Thread to load on first launch
         */
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);

                if (getIntent() !=null)
                    landId =getIntent().getStringExtra(Common.INTENT_LAND_ID);
                if (!landId.isEmpty() && landId !=null)
                {
                    /**
                     * create request query
                     */
                    Query query = ratingTbl.orderByChild("landId").equalTo(landId);
                    FirebaseRecyclerOptions<Rating> options =new FirebaseRecyclerOptions.Builder<Rating>()
                            .setQuery(query,Rating.class)
                            .build();

                    adapter = new FirebaseRecyclerAdapter<Rating, ShowCommentViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(ShowCommentViewHolder holder, int position, Rating model) {
                            holder.ratingBar.setRating(Float.parseFloat(model.getRateValue()));
                            holder.txtComment.setText(model.getComment());
                            holder.txtUserPhone.setText(model.getUserPhone());

                        }

                        @NonNull
                        @Override
                        public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                            View view =LayoutInflater.from(viewGroup.getContext())
                                    .inflate(R.layout.show_comment_layout,viewGroup,false);
                            return new ShowCommentViewHolder(view);
                        }
                    };

                    loadComment(landId);
                }
            }
        });

    }

    private void loadComment(String landId) {
        adapter.startListening();

        recyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
