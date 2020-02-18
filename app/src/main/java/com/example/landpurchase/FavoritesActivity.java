package com.example.landpurchase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Database.Database;
import com.example.landpurchase.Helper.RecyclerItemTouchHelper;
import com.example.landpurchase.Interface.RecyclerItemTouchHelperListener;
import com.example.landpurchase.Models.Favorites;
import com.example.landpurchase.ViewHolder.FavoritesAdapter;
import com.example.landpurchase.ViewHolder.FavoritesViewHolder;
import com.google.android.material.snackbar.Snackbar;

public class FavoritesActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FavoritesAdapter adapter;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


        rootLayout = (RelativeLayout)findViewById(R.id.root_layout);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_fav);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_from_left);
        recyclerView.setLayoutAnimation(controller);

        /**swipe to delete**/
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
        LoadFavorites();
    }

    private void LoadFavorites() {
        adapter = new FavoritesAdapter(this,new Database(this).getAllFavorites(Common.currentUser.getPhone()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavoritesViewHolder)
        {
            String name = ((FavoritesAdapter)recyclerView.getAdapter()).getItem(position).getLandLocationName();

            final Favorites deleteItem = ((FavoritesAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition());
            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).removeFromFavorites(deleteItem.getLand(), Common.currentUser.getPhone());

            /**Make Snackbar**/
            Snackbar snackbar = Snackbar.make(rootLayout,name + " removed from favorites!",Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    new Database(getBaseContext()).addFavorites(deleteItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }

    }
}

