package com.example.landpurchases;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.landpurchases.Common.Common;
import com.example.landpurchases.Interface.ItemClickListener;
import com.example.landpurchases.Models.Requests;
import com.example.landpurchases.ViewHolder.LandOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class LandStatus extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Requests, LandOrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_status);

        /**Firebase**/
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Counties").child(Common.countySelected).child("Requests");

        recyclerView = findViewById(R.id.listLands);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent()==null)
            loadOrders(Common.currentUser.getPhone());
        else
        {
            if (getIntent().getStringExtra("userPhone") ==null)
                loadOrders(Common.currentUser.getPhone());
            else
                loadOrders(getIntent().getStringExtra("userPhone"));

        }
    }

    private void loadOrders(final String phone) {

        Query getOrderByUser = requests.orderByChild("phone")
                .equalTo(phone);

        FirebaseRecyclerOptions<Requests> orderOptions = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(getOrderByUser,Requests.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Requests, LandOrderViewHolder>(orderOptions) {
            @Override
            protected void onBindViewHolder(@NonNull LandOrderViewHolder viewHolder, final int position, @NonNull Requests model) {

                viewHolder.txtLandId.setText(adapter.getRef(position).getKey());
                viewHolder.txtLandStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtLandAddress.setText(model.getAddress());
                viewHolder.txtLandPhone.setText(model.getPhone());
                viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (adapter.getItem(position).getStatus().equals("0"))

                            deleteOrder(adapter.getRef(position).getKey());
                        else
                            Toast.makeText(LandStatus.this, "You cannot delete this order", Toast.LENGTH_SHORT).show();


                    }
                });

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Common.currentKey = adapter.getRef(position).getKey();
                        startActivity(new Intent(LandStatus.this,TrackingLand.class));
                    }
                });
            }

            @NonNull
            @Override
            public LandOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.land_layout,viewGroup,false);
                return new LandOrderViewHolder(itemView);
            }
        };
        adapter.startListening();

        recyclerView.setAdapter(adapter);
    }

    private void deleteOrder(final String key) {

        requests.child(key)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LandStatus.this, new StringBuilder("Order ")
                        .append(key)
                        .append("has been deleted!").toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LandStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
