package com.favplaces.maps.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.favplaces.maps.MapsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NearByPlaces extends AsyncTask<Object,String,String > {


    String near_by_places;
    String loc_url;
    GoogleMap mMap;
    Context mCtx;

    public NearByPlaces(Context applicationContext) {
        mCtx=applicationContext;
    }

    public NearByPlaces() {

    }


    @Override
    protected String doInBackground(Object... objects) {
        mMap = (GoogleMap) objects[0];
        loc_url = (String) objects[1];

        UrlConnector url = new UrlConnector();
        try {
            near_by_places = url.readUrl(loc_url);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return near_by_places;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> list_places = null;
        JSONDataParser dataParser = new JSONDataParser();
        list_places = dataParser.parse(s);

        nearByPlaces(list_places);


    }


    private void nearByPlaces(List<HashMap<String,String>> places_list){
        if (places_list.size()==0){
            Toast.makeText(mCtx, "Nothing Found", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0;i<places_list.size();i++) {
            HashMap<String, String> placeDetails = places_list.get(i);
            String name = placeDetails.get("placeName");
            double latitude = Double.parseDouble(placeDetails.get("lat"));
            double longitude = Double.parseDouble(placeDetails.get("lng"));
            String vicinity = placeDetails.get("vicinity");
            if (name==null){
                name="";
            }if (vicinity==null){
                vicinity="";
            }
            LatLng latLng = new LatLng(latitude, longitude);
            MarkerOptions options=new MarkerOptions().position(latLng)
                    .title(name + ":" + vicinity);
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET));
            mMap.addMarker(options);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        }

    }
}


