package com.example.landpurchase;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.landpurchase.Common.Common;
import com.example.landpurchase.Helper.DirectionJSONParser;
import com.example.landpurchase.Models.LandInformation;
import com.example.landpurchase.Models.Requests;
import com.example.landpurchase.Remote.IGoogleService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressWarnings({"ConstantConditions" , "NullableProblems"})
public class TrackingLand extends FragmentActivity implements OnMapReadyCallback,ValueEventListener{

    private GoogleMap mMap;
    FirebaseDatabase database;
    DatabaseReference requests,landOrder;
    Requests currentLand;

    IGoogleService mService;

    Marker landMaker;
    Polyline polyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_land);
        /** Obtain the SupportMapFragment and get notified when the map is ready to be used.**/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        database =FirebaseDatabase.getInstance();
        requests = database.getReference("Counties").child(Common.countySelected)
                .child("Requests");
        landOrder = database.getReference("ShippingOrders");
        landOrder.addValueEventListener(this);


        mService = Common.getGoogleMapAPI();
    }

    @Override
    protected void onStop() {
        landOrder.removeEventListener(this);
        super.onStop();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        trackingLocation();
    }

    private void trackingLocation() {


        requests.child(Common.currentKey);
        requests.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentLand = dataSnapshot.getValue(Requests.class);

                /**if class has address**/
                if (currentLand.getAddress() != null && !currentLand.getAddress().isEmpty()) {
                    //noinspection StringBufferReplaceableByString
                    mService.getLocationFromAddress(new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?address=")
                            .append(currentLand.getAddress()).toString())
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call , Response<String> response) {
                                    try {
                                        //noinspection ConstantConditions
                                        JSONObject jsonObject = new JSONObject(response.body());

                                        String lat = ((JSONArray) jsonObject.get("results"))
                                                .getJSONObject(0)
                                                .getJSONObject("geometry")
                                                .getJSONObject("location")
                                                .get("lat").toString();

                                        String lng = ((JSONArray) jsonObject.get("results"))
                                                .getJSONObject(0)
                                                .getJSONObject("geometry")
                                                .getJSONObject("location")
                                                .get("lng").toString();

                                        LatLng location = new LatLng(Double.parseDouble(lat) ,
                                                Double.parseDouble(lng));

                                        mMap.addMarker(new MarkerOptions().position(location)
                                                .title("Land Location destination")
                                                .icon(BitmapDescriptorFactory.defaultMarker()));

                                        /**Set land location**/
                                        landOrder.child(Common.currentKey)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        LandInformation landInformation = dataSnapshot.getValue(LandInformation.class);

                                                        @SuppressWarnings("ConstantConditions") LatLng landLocation = new LatLng(landInformation.getLat() , landInformation.getLng());
                                                        if (landMaker == null) {
                                                            landMaker = mMap.addMarker(
                                                                    new MarkerOptions()
                                                                            .position(landLocation)
                                                                            .title("Land Location # " + landInformation.getLandId())
                                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                                            );
                                                        } else {
                                                            landMaker.setPosition(landLocation);
                                                        }
                                                        /**Update Camera**/
                                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                                .target(landLocation)
                                                                .zoom(16)
                                                                .bearing(0)
                                                                .tilt(45)
                                                                .build();
                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                                        /**draw routes**/
                                                        if (polyline != null)
                                                            polyline.remove();
                                                        mService.getDirections(landLocation.latitude + "," + landLocation.longitude ,
                                                                currentLand.getAddress())
                                                                .enqueue(new Callback<String>() {
                                                                    @Override
                                                                    public void onResponse(Call<String> call , Response<String> response) {
                                                                        //noinspection ResultOfMethodCallIgnored
                                                                        new ParserTask().execute(response.body()).toString();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<String> call , Throwable t) {

                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call , Throwable t) {

                                }
                            });

                }
                /**If land has latlng**/
                else if (currentLand.getLatLng() != null && !currentLand.getLatLng().isEmpty()) {

                    mService.getLocationFromAddress(new StringBuilder("https://maps.googleapis.com/maps/api/geocode/json?latlng=")
                            .append(currentLand.getLatLng()).toString())
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call , Response<String> response) {
                                    try {
                                        @SuppressWarnings("ConstantConditions") JSONObject jsonObject = new JSONObject(response.body());

                                        String lat = ((JSONArray) jsonObject.get("results"))
                                                .getJSONObject(0)
                                                .getJSONObject("geometry")
                                                .getJSONObject("location")
                                                .get("lat").toString();

                                        String lng = ((JSONArray) jsonObject.get("results"))
                                                .getJSONObject(0)
                                                .getJSONObject("geometry")
                                                .getJSONObject("location")
                                                .get("lng").toString();

                                        LatLng location = new LatLng(Double.parseDouble(lat) ,
                                                Double.parseDouble(lng));

                                        mMap.addMarker(new MarkerOptions().position(location)
                                                .title("Land Location destination")
                                                .icon(BitmapDescriptorFactory.defaultMarker()));

                                        /**Set Shipper location**/
                                        landOrder.child(Common.currentKey)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        LandInformation landInformation = dataSnapshot.getValue(LandInformation.class);

                                                        @SuppressWarnings("ConstantConditions") LatLng landLocation = new LatLng(landInformation.getLat() , landInformation.getLng());
                                                        if (landMaker == null) {
                                                            landMaker = mMap.addMarker(
                                                                    new MarkerOptions()
                                                                            .position(landLocation)
                                                                            .title("Land location # " + landInformation.getLandId())
                                                                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                                                            );
                                                        } else {
                                                            landMaker.setPosition(landLocation);
                                                        }
                                                        /**Update Camera**/
                                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                                .target(landLocation)
                                                                .zoom(16)
                                                                .bearing(0)
                                                                .tilt(45)
                                                                .build();
                                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                                                        /**draw routes**/
                                                        if (polyline != null)
                                                            polyline.remove();
                                                        mService.getDirections(landLocation.latitude + "," + landLocation.longitude ,
                                                                currentLand.getLatLng())
                                                                .enqueue(new Callback<String>() {
                                                                    @SuppressWarnings("ResultOfMethodCallIgnored")
                                                                    @Override
                                                                    public void onResponse(Call<String> call , Response<String> response) {
                                                                        new
                                                                                ParserTask().execute(response.body()).toString();
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<String> call , Throwable t) {

                                                                    }
                                                                });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Call<String> call , Throwable t) {

                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        trackingLocation();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    private class ParserTask extends AsyncTask<String,Integer, List<List<HashMap<String,String>>>> {
        AlertDialog mDialog = new SpotsDialog(TrackingLand.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog.show();
            mDialog.setMessage("Please waiting...");
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes=null;
            try{
                jObject = new JSONObject(strings[0]);
                DirectionJSONParser parser = new DirectionJSONParser();

                routes = parser.parse(jObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            mDialog.dismiss();
            @SuppressWarnings("UnusedAssignment") ArrayList points = null;
            PolylineOptions lineOptions = null;

            for (int i = 0 ;i<lists.size();i++){
                points = new ArrayList();
                lineOptions = new PolylineOptions();

                List<HashMap<String,String>> path = lists.get(i);

                for (int j=0;j<path.size();j++){
                    HashMap<String,String >point = path.get(j);

                    @SuppressWarnings("ConstantConditions") double lat = Double.parseDouble(point.get("lat"));
                    @SuppressWarnings("ConstantConditions") double lng = Double.parseDouble(point.get("lng"));

                    LatLng position = new LatLng(lat,lng);

                    points.add(position);

                }
                lineOptions.addAll(points);
                lineOptions.width(12);
                lineOptions.color(Color.BLUE);
                lineOptions.geodesic(true);

            }
            polyline=mMap.addPolyline(lineOptions);

        }
    }

}