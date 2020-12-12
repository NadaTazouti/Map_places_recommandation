package com.example.map1;


import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.Status;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;


    Intent intent;
    ImageView Gps;
    Button btn;
    static double latitude = 0.0, longitude = 0.0;


    DB_Sqlit db = new DB_Sqlit(this);
    ArrayList<Place> myData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        new AddPlace.GetDataFromServer(this).execute(AddPlace.HttpURLGet);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        Gps = (ImageView) findViewById(R.id.gps);
        btn =(Button) findViewById(R.id.btn);

        intent = getIntent();

        Places.initialize(getApplicationContext(), "AIzaSyCBErcW5_RDLPWjwGeUWp_LwrHa0AvsmOc");

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 14));

                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title("new location ")).showInfoWindow();

                Log.i("", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {

                Log.i("", "An error occurred: " + status);
            }
        });

    }

    private void init() {

        Gps.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }

        });

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), AfficheFavoris.class);
                startActivity(intent);
                finish();

            }

        });

    }







    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (intent.hasExtra("latitude")) {


            String name = intent.getStringExtra("name");

            String lat = intent.getStringExtra("latitude");
            String lon = intent.getStringExtra("longitude");

            Double latit = (lat == null || lat.equals("") ? 0.0 : Double.parseDouble(lat));

            Double longi = (lon == null || lon.equals("") ? 0.0 : Double.parseDouble(lon));

            LatLng myPosition = new LatLng(longi, latit);

            mMap.addMarker(new MarkerOptions().position(myPosition).title(name)).showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 14));

            Toast.makeText(MapsActivity.this, "", Toast.LENGTH_SHORT).show();


        } else {

            LatLng Rabat = new LatLng(34.0218267, -6.8404208);

            mMap.addMarker(new MarkerOptions().position(Rabat).title("Agdal")).showInfoWindow();

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Rabat, 14));

        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {

                latitude = latLng.latitude;
                longitude = latLng.longitude;

                Toast.makeText(MapsActivity.this, latitude + "," + longitude, Toast.LENGTH_SHORT).show();

                mMap.clear();

                mMap.addMarker(new MarkerOptions().position(latLng)).showInfoWindow();


                String s1 = Double.toString(latitude);
                String s2 = Double.toString(longitude);

                Intent intent = new Intent(getApplicationContext(), AddPlace.class);

                intent.putExtra("first", s1);
                intent.putExtra("second", s2);

                startActivity(intent);
                finish();


            }
        });

        init();

    }

}