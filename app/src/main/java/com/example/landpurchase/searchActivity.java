package com.example.landpurchase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Database.Database;
import com.example.landpurchase.Interface.ItemClickListener;
import com.example.landpurchase.Models.Favorites;
import com.example.landpurchase.Models.Land;
import com.example.landpurchase.Models.LandOrder;
import com.example.landpurchase.ViewHolder.LandViewHolder;
import com.facebook.CallbackManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class searchActivity extends AppCompatActivity {

    /**
     * search functionality**/

    FirebaseRecyclerAdapter<Land, LandViewHolder> adapter;
    FirebaseRecyclerAdapter<Land, LandViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference landList;

    /**Favorites**/
    Database localDB;

    /**facebook share**/
    CallbackManager callbackManager;
    ShareDialog shareDialog;


    /**create target from picasso**/
    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(bitmap)
                    .build();
            if (ShareDialog.canShow(SharePhotoContent.class))
            {
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
            }
        }


        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        /**init Facebook**/
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);


        /**Firebase**/
        database = FirebaseDatabase.getInstance();
        landList = database.getReference("Counties").child(Common.countySelected)
                .child("detail").child("Lands");

        /**local DB**/
        localDB =new Database(this);


        recyclerView = findViewById(R.id.recycler_search);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);



        /** search**/
        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter Constituency");
        /**materialSearchBar.setSpeechMode(false);no need because we had defined in xml**/
        loadSuggest();/**write function to load suggest from firebase**/
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /**Then user can type their text**/

                List<String>suggest = new ArrayList<>();
                for (String search:suggestList)
                {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean b) {
                /**when search bar is closed
                 * restore original adapter
                 */
                if (!b)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence charSequence) {

                /**
                 * when search finishes
                 * show result of search adapter
                 *
                 */

                startSearch(charSequence);
            }

            @Override
            public void onButtonClicked(int i) {

            }
        });


        /**load all foods**/
        loadAllLands();

    }

    private void loadAllLands() {
        Query searchByName = landList;

        /**create options with query**/

        FirebaseRecyclerOptions<Land> options = new FirebaseRecyclerOptions.Builder<Land>()
                .setQuery(searchByName,Land.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Land, LandViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final LandViewHolder viewHolder, final int position, @NonNull final Land model) {

                viewHolder.land_name.setText(model.getName());
                //viewHolder.food_price.setText(String.format("$ %s", model.getPrice().toString()));
                Picasso.with(getBaseContext()).load(model.getLandImage())
                        .into(viewHolder.land_image);

                /**quick cart**/

                viewHolder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean isExists = new Database(getBaseContext()).checkLandExists(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
                        if (!isExists) {
                            new Database(getBaseContext()).addToCart(new LandOrder(

                                    Common.currentUser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    model.getName(),
                                    "1",
                                    model.getLandPrice(),
                                    model.getSizeOfLand(),
                                    model.getLandImage()
                            ));
                        }

                        else
                        {
                            new Database(getBaseContext()).increaseCart(Common.currentUser.getPhone(),adapter.getRef(position).getKey());

                        }
                        Toast.makeText(searchActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();

                    }

                });

                /**Add Favorites**/
                if (localDB.isFavorite(adapter.getRef(position).getKey(),Common.currentUser.getPhone()))
                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);

                /**click to share**/
                viewHolder.share_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Picasso.with(getApplicationContext())
                                .load(model.getLandImage())
                                .into(target);
                    }
                });

                /**Click to change state of favorite**/
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Favorites favorites = new Favorites();
                        favorites.setLand(adapter.getRef(position).getKey());
                        favorites.setLandLocationName(model.getName());
                        favorites.setLandDescription(model.getLandDescription());
                        favorites.setSizeOfLand(model.getSizeOfLand());
                        favorites.setLandImage(model.getLandImage());
                        favorites.setLandId(model.getLandMenuId());
                        favorites.setUserPhone(Common.currentUser.getPhone());
                        favorites.setLandPrice(model.getLandPrice());

                        if (!localDB.isFavorite(adapter.getRef(position).getKey(),Common.currentUser.getPhone()))
                        {
                            localDB.addFavorites(favorites);
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(searchActivity.this, ""+model.getName()+"was added to Favourites", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            localDB.removeFromFavorites(adapter.getRef(position).getKey(),Common.currentUser.getPhone());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(searchActivity.this, ""+model.getName()+"was removed from Favourites", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                final Land local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent landDetail = new Intent(searchActivity.this, LandDetails.class);
                        landDetail.putExtra("LandId", adapter.getRef(position).getKey());
                        startActivity(landDetail);


                    }
                });

            }



            @NonNull
            @Override
            public LandViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.land_item,viewGroup,false);
                return new LandViewHolder(itemView);
            }
        };
        recyclerView = findViewById(R.id.recycler_search);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(recyclerView.getContext(),
                R.anim.layout_from_left);
        recyclerView.setLayoutAnimation(controller);


        /**set adapter**/
        adapter.startListening();

        recyclerView.setAdapter(adapter);


        /**Animation**/
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();

    }

    private void startSearch(CharSequence charSequence) {
        /**create query by name
         *
         */
        Query searchByName = landList.orderByChild("name").equalTo(charSequence.toString());
        /**
         * create options with query
         */
        FirebaseRecyclerOptions<Land> landOptions = new FirebaseRecyclerOptions.Builder<Land>()
                .setQuery(searchByName,Land.class)
                .build();
        searchAdapter = new FirebaseRecyclerAdapter<Land, LandViewHolder>(landOptions){
            @Override
            protected void onBindViewHolder(@NonNull LandViewHolder viewHolder, int i, @NonNull Land model) {

                viewHolder.land_name.setText(model.getName());
                //viewHolder.food_price.setText(String.format("$ %s", model.getPrice().toString()));
                Picasso.with(getBaseContext()).load(model.getLandImage())
                        .into(viewHolder.land_image);

                final Land local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //start new activity
                        Intent landDetail = new Intent(searchActivity.this, LandDetails.class);
                        landDetail.putExtra("LandId", searchAdapter.getRef(position).getKey());
                        startActivity(landDetail);


                    }
                });
                recyclerView.setAdapter(searchAdapter); /**set adapter for recycler view in search results**/
            }


            @NonNull
            @Override
            public LandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
    }

    private void loadSuggest() {
        landList.addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren()
                )
                {
                    Land  item = postSnapshot.getValue(Land.class);
                    suggestList.add(item.getName());/**Add name of food to suggest list**/

                }
                materialSearchBar.setLastSuggestions(suggestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
