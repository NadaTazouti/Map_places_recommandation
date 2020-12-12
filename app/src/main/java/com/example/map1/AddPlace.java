package com.example.map1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.DeniedByServerException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AddPlace extends Activity {

    DB_Sqlit db = new DB_Sqlit(this);

    EditText editText;
    TextView textView, textView2;
    Button add;


    String name = "", latitude = "", longitude = "";

    String HttpURLInsert = "https://murmuring-felt.000webhostapp.com/insert_data.php";

    static String HttpURLGet = "https://murmuring-felt.000webhostapp.com/get_data.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_addplace);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        add = (Button) findViewById(R.id.addPlace);


        latitude = getIntent().getStringExtra("first");
        longitude = getIntent().getStringExtra("second");

        textView.setText(latitude);
        textView2.setText(longitude);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                insertData(editText.getText().toString(), latitude, longitude);

                boolean result = db.insertData(editText.getText().toString(), latitude, longitude);

                if (result == true) {

                    Toast.makeText(AddPlace.this, "Ok", Toast.LENGTH_SHORT).show();

                } else

                    Toast.makeText(AddPlace.this, "NO", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();

            }
        });


    }

    public void insertData(final String name, final String latitude, final String longitude) {

        new AsyncTask<String, Void, String>() {

            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                String lat = latitude;
                String lon = longitude;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("latitude", lat));
                nameValuePairs.add(new BasicNameValuePair("longitude", lon));

                try {

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(HttpURLInsert);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {
                    Log.e("error", e.getMessage());

                } catch (IOException e) {
                    Log.e("error", e.getMessage());
                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

            }
        }.execute(name, latitude, longitude);
    }


    public static class GetDataFromServer extends AsyncTask<String, Void, Void> {

        Context context;
        private String jsonResponse;
        DB_Sqlit db ;
        private ProgressDialog dialog;

        public GetDataFromServer(Context c) {
            this.context = c;
            dialog = new ProgressDialog(context);
            db = new DB_Sqlit(context);
        }

        protected void onPreExecute() {
            dialog.show();
        }


        protected Void doInBackground(String... urls) {
            try {

                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream isResponse = urlConnection.getInputStream();
                BufferedReader responseBuffer = new BufferedReader(new InputStreamReader(isResponse));

                String myLine = "";

                StringBuilder strBuilder = new StringBuilder();

                while ((myLine = responseBuffer.readLine()) != null) {
                    strBuilder.append(myLine);
                }

                jsonResponse = strBuilder.toString();
                Log.e("RESPONSE", jsonResponse);
            } catch (Exception e) {
                Log.e("RESPONSE Error", e.getMessage());
            }
            return null;
        }

        protected void onPostExecute(Void unused) {

            try {

                dialog.dismiss();

                Gson gson = new Gson();

                Log.e("PostExecute", "content: " + jsonResponse);

                Type listType = new TypeToken<ArrayList<Place>>() {
                }.getType();

                Log.e("PostExecute", "arrayType: " + listType.toString());

                ArrayList<Place> places = gson.fromJson(jsonResponse, listType);

                Log.e("PostExecute", "OutputData: " + places.toString());


                db.drop();

                for (Place p : places) {

                    db.insertData(p.getName(), p.getLatitude(), p.getLongitude());

                }
            } catch (JsonSyntaxException e) {

                Log.e("POST-Execute", e.getMessage());
            }
        }
    }
}


