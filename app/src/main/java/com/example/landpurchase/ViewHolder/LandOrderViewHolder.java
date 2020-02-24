package com.example.landpurchase.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landpurchase.Interface.ItemClickListener;
import com.example.landpurchase.R;

public class LandOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtLandId,txtLandPhone,txtLandStatus,txtLandAddress;

    private ItemClickListener itemClickListener;
    public ImageView btn_delete;

    public LandOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtLandId = itemView.findViewById(R.id.land_id);
        txtLandPhone= itemView.findViewById(R.id.land_phone);
        txtLandStatus= itemView.findViewById(R.id.land_status);
        txtLandAddress= itemView.findViewById(R.id.land_address);
        btn_delete = itemView.findViewById(R.id.btn_delete);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
