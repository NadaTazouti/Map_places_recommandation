package com.example.map1;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AfficheFavoris extends Activity {

    DB_Sqlit db = new DB_Sqlit(this);

    RecyclerView recyclerView;

    PlaceAdapter placeAdapter;

    ArrayList<Place> Data = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_affichefavoris);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Data = db.getAll();

        placeAdapter = new PlaceAdapter(this, Data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(placeAdapter);


    }


    public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

        Context context;
        ArrayList<Place> Data;
        double lat = 0.0, lon = 0.0;


        public PlaceAdapter(Context c, ArrayList<Place> d) {
            this.context = c;
            this.Data = d;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView name, latlng;


            public ViewHolder(View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.name);
                latlng = (TextView) itemView.findViewById(R.id.latlng);


            }

        }


        @Override
        public PlaceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView;

            itemView = LayoutInflater.from(context).inflate(R.layout.cardview, parent, false);

            return new ViewHolder(itemView);

        }


        @Override
        public void onBindViewHolder(final PlaceAdapter.ViewHolder holder, final int position) {


            holder.name.setText("Name : " + Data.get(position).getName());


            holder.latlng.setText(Data.get(position).getLatitude() + "," + Data.get(position).getLongitude());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

                    intent.putExtra("latitude",Data.get(position).getLatitude());
                    intent.putExtra("longitude", Data.get(position).getLongitude());
                    intent.putExtra("name", Data.get(position).getName());

                    startActivity(intent);
                    finish();

                }
            });
        }


        @Override
        public int getItemCount() {

            return Data.size();

        }

    }

}



