package com.example.stormcore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.DirectAction;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

    /*
    9/13, 5:30 - 8:30, Ankur, 3 hours
    9/14, 1:00 - 3:00, Ankur's apt, Walter and Ankur, 2 hours
    9/14, 4:00 - 8:00, Ankur's apt, Walter and Ankur, 4 hours
    9/15, 8:00 - 9:30, PCL 5th Floor, Walter and Ankur, 3 hours
    9/16, 4:00 - 7:00, PCL 5th Floor, Walter and Ankur, 3 hours
    total time 15 hours, 12 hours of pair programming
    */

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Created here to be able to access in all future methods
    Button btnShowCoord;
    Button btnWeather;
    EditText edtAddress;
    TextView txtCoord;
    EditText loc2;
    private MapView mapView;
    private GoogleMap gmap;
    TextView tv;
    static SupportMapFragment mf;
    LatLng ll;
    Boolean def = false;
    Button btnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Begin Ankur Driving, Walter Supervising


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //will be used to determine what the coordinates are
        btnShowCoord = (Button)findViewById(R.id.btnShowCoordinates);
        //type address here
        edtAddress = (EditText)findViewById(R.id.edtAddress);
        //txtCoord = (TextView)findViewById(R.id.txtCoordinates);
        //where the coordinates show up
        loc2 = (EditText)findViewById(R.id.loc);
        //click to display weather info
        btnWeather = findViewById(R.id.buttonWeather);
        //shows weather info
        tv = findViewById(R.id.textView);
        ll = new LatLng(30.257153, -97.7430608);
        mf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mf.getMapAsync(this);
        btnMap = findViewById(R.id.btnMap);
        //mapView = findViewById(R.id.map_view);
        //mapView.onCreate(mapViewBundle);
        //mapView.getMapAsync(this);


        //listeners for both buttons
        btnShowCoord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCoordinates().execute(edtAddress.getText().toString().replace(" ","+"));
            }
        });
        btnWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = loc2.getText().toString().substring(14, loc2.length()-1);

                new weatherInfo().execute(str);
            }
        });


    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        final LatLng ll2 = new LatLng(-8.783195, 34.508523);
        googleMap.clear();
        if(def)
        {
            googleMap.addMarker(new MarkerOptions().position(ll).title("M2"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        }
        googleMap.addMarker(new MarkerOptions().position(ll)
                .title("Marker"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMap.addMarker(new MarkerOptions().position(ll)
                        .title("Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
            }
        });

    }
    //end Ankur Driving


    //Begin Ankur driving, Walter supporting
    private class weatherInfo extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String response;

            try {
                String url2 = String.format("https://api.darksky.net/forecast/c3b0b883b5b88cd92595e20d0515562e/%s,%s\n", "30.257153", "-97.7430608");


                String str = strings[0];
                String[] strArry = str.split(" / ");
                HttpDataHandler http = new HttpDataHandler();
                //String url = String.format("https://api.darksky.net/forecast/c3b0b883b5b88cd92595e20d0515562e/%s,%s\n", strArry[0], strArry[1]);

                response = http.getHTTPData(url2);

                return response;

            }catch (Exception e){}


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject current =  jsonObject.getJSONObject("currently");
                //JSONArray current = jsonObject.getJSONArray("currently");
                //JSONArray currArry = current.getJSONArray();
                String temp = current.getString("temperature");
                String windSpeed = current.getString("windSpeed");
                String humidity = current.getString("humidity");
                String precip = current.getString("precipIntensity");


                tv.setText(String.format("Current Weather:\nTemperature: %s\nWind Speed: %s\nHumidity: %s\nPrecipitation Intensity: %s", temp, windSpeed, humidity, precip));
            }
            catch (Exception e){}
        }
    }
    //end Ankur Driving

    //Walter driving here, Ankur Supervising and offering assistance
    private class GetCoordinates extends AsyncTask<String,Void,String> {
        ProgressDialog dialog = new ProgressDialog(MainActivity.this);


        @Override
        protected String doInBackground(String... strings) {
            String response;
            try{
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=AIzaSyBN21SuE0mMAVrbSLU2ctbIwKE49GR83EY\n",address);
                response = http.getHTTPData(url);


                return response;

            }
            catch (Exception ex)
            {


            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lat").toString();
                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry").getJSONObject("location").get("lng").toString();

                Double latD = Double.valueOf(lat);
                Double lngD = Double.valueOf(lng);

                ll = new LatLng(latD,lngD);
                def = true;
                //mf.getFragmentManager().beginTransaction().remove(mf).commit();
                mf = SupportMapFragment.newInstance();


                //gmap.addMarker(new MarkerOptions().position(ll).title("curLoc"));

                loc2.setText(String.format("Coordinates : %s / %s ", lat,lng));

                dialog.dismiss();
                dialog.cancel();

            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }
    //end Walter driving
}
