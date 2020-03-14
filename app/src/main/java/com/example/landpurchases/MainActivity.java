package com.example.landpurchases;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.landpurchases.Common.Common;
import com.example.landpurchases.Models.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 7171;
    Button btn_continue;
    TextView txtSlogan;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private List<AuthUI.IdpConfig>providers;

    FirebaseDatabase database;
    DatabaseReference users;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener !=null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        listener = firebaseAuth ->{
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null)
                checkUserFromFirebase(user);

        };
        setContentView(R.layout.activity_main);

        /**init firebase**/
        database = FirebaseDatabase.getInstance();
        users = database.getReference("user");

        btn_continue= (Button)findViewById(R.id.btn_continue);

        txtSlogan = (TextView)findViewById(R.id.slogan);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");
        txtSlogan.setTypeface(face);



        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startLoginSystem();

            }
        });

    }

    private void checkUserFromFirebase(FirebaseUser user) {
        /**show dialog**/
        final AlertDialog waitingDialog = new SpotsDialog(this);
        waitingDialog.show();
        waitingDialog.setMessage("Please wait");
        waitingDialog.setCancelable(false);


        /**check if exists on firebase Users**/
        users.orderByKey().equalTo(user.getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child(user.getPhoneNumber()).exists())/**if not exists**/
                        {
                            User newUser = new User();
                            newUser.setPhone(user.getPhoneNumber());
                            newUser.setName("");
                            newUser.setBalance(String.valueOf(0.0));

                            /**Add to Firebase**/
                            users.child(user.getPhoneNumber())
                                    .setValue(newUser)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(MainActivity.this, "User Register Successful!", Toast.LENGTH_SHORT).show();

                                                /**Login**/
                                                users.child(user.getPhoneNumber())
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                User localUser = dataSnapshot.getValue(User.class);

                                                                Intent homeItent = new Intent(MainActivity.this, CountiesList.class);
                                                                Common.currentUser = localUser;
                                                                startActivity(homeItent);
                                                                finish();

                                                                /**dismiss dialog**/
                                                                waitingDialog.dismiss();
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                waitingDialog.dismiss();
                                                                Toast.makeText(MainActivity.this , ""+databaseError.getMessage() , Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                        else/**if exists**/
                        {
                            /**we will just login
                             Login**/
                            users.child(user.getPhoneNumber() )
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            User localUser = dataSnapshot.getValue(User.class);

                                            Intent homeItent = new Intent(MainActivity.this, CountiesList.class);
                                            Common.currentUser = localUser;
                                            startActivity(homeItent);
                                            finish();

                                            /**dismiss dialog**/
                                            waitingDialog.dismiss();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            waitingDialog.dismiss();
                                            Toast.makeText(MainActivity.this , ""+databaseError.getMessage() , Toast.LENGTH_SHORT).show();


                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        waitingDialog.dismiss();
                        Toast.makeText(MainActivity.this , ""+databaseError.getMessage() , Toast.LENGTH_SHORT).show();


                    }
                });
    }

    private void startLoginSystem() {

        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),REQUEST_CODE);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (requestCode == RESULT_OK)
            {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            }
            else
                Toast.makeText(this , "Failed to sign in " , Toast.LENGTH_SHORT).show();
        }
    }
}
