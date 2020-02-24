package com.example.landpurchase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Database.Database;
import com.example.landpurchase.Models.Land;
import com.example.landpurchase.Models.LandOrder;
import com.example.landpurchase.Models.Rating;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;


public class LandDetails extends AppCompatActivity implements RatingDialogListener {

    TextView land_name,land_price,land_description;
    ImageView land_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnRating;
    CounterFab btnCart;
    ElegantNumberButton numberButton;
    RatingBar ratingBar;

    String landId="";

    FirebaseDatabase database;
    DatabaseReference lands;
    DatabaseReference ratingTbl;
    Land currentLand;

    Button btnShowComment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_land_details);

        btnShowComment = findViewById(R.id.btnShowComment);
        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandDetails.this,ShowComment.class);
                intent.putExtra(Common.INTENT_LAND_ID,landId);
                startActivity(intent);
            }
        });

        /**firebase**/
        database = FirebaseDatabase.getInstance();
        lands=database.getReference("Counties").child(Common.countySelected)
                .child("detail").child("Lands");
        ratingTbl=database.getReference("Rating");


        /**init view**/
        numberButton = findViewById(R.id.number_button);
        btnCart= findViewById(R.id.btnCart);
        btnRating= findViewById(R.id.btn_rating);
        ratingBar= findViewById(R.id.ratingBar);

        btnRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRatingDialog();
            }
        });
        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new LandOrder(

                        Common.currentUser.getPhone(),
                        landId,
                        currentLand.getName(),
                        numberButton.getNumber(),
                        currentLand.getLandPrice(),
                        currentLand.getSizeOfLand(),
                        currentLand.getLandImage()
                ));
                Toast.makeText(LandDetails.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        btnCart.setCount(new Database(this).getCountCart(Common.currentUser.getPhone()));


        land_description = findViewById(R.id.land_description);
        land_name = findViewById(R.id.land_name);
        land_price = findViewById(R.id.land_price);
        land_image = findViewById(R.id.img_land);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        /**Get Land id from intent**/
        if(getIntent() !=null)
            landId =getIntent().getStringExtra("LandId");
            if (!landId.isEmpty()) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    getDetailLand(landId);
                    getRatingLand(landId);

                } else {
                    Toast.makeText(LandDetails.this, "Please check your connection!!", Toast.LENGTH_SHORT).show();
                }
            }


    }

    private void getRatingLand(String landId) {
        com.google.firebase.database.Query LandRating =ratingTbl.orderByChild("landId").equalTo(landId);
        LandRating.addValueEventListener(new ValueEventListener() {
            int count=0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count !=0)
                {
                    float average =sum/count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate this Land")
                .setDescription("Please select some stars and give your feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your hint here")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(android.R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(LandDetails.this)
                .show();
    }

    private void getDetailLand(String landId) {
        lands.child(landId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentLand = dataSnapshot.getValue(Land.class);

                /**Set image**/
                Picasso.with(getBaseContext()).load(currentLand.getLandImage())
                        .into(land_image);

                collapsingToolbarLayout.setTitle(currentLand.getName());

                land_price.setText(currentLand.getLandPrice());

                land_name.setText(currentLand.getName());

                land_description.setText(currentLand.getLandDescription());



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NotNull String comments) {
        /**Get Rating and and Upload to firebase**/
        final Rating rating = new Rating (Common.currentUser.getPhone(),
                landId,
                String.valueOf(value),comments);

        /**fix user can rate multiple times**/
        ratingTbl.push()
                .setValue(rating)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(LandDetails.this, "Thank you, for submitting your rating ", Toast.LENGTH_SHORT).show();

                    }
                });

    }
}
