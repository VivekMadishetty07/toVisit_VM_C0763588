package com.favplaces.maps;

import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.favplaces.R;
import com.favplaces.roomDb.AppDatabase;
import com.favplaces.roomDb.DatabaseClient;
import com.favplaces.roomDb.FavouritePlacesBean;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class UpdateFavoritePlace extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int favId;
    String address;
    AppDatabase databaseClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_favorite_place);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseClient = new DatabaseClient(this).getAppDatabase();
        if (getIntent().hasExtra("id")){
            favId=getIntent().getIntExtra("id",0);


        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<FavouritePlacesBean> list=databaseClient.favouritePlacesDao().getAll();
        LatLng fav = new LatLng(list.get(favId).getLatitude(), list.get(favId).getLongitude());
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(fav)
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        mMap.addMarker(new MarkerOptions().position(fav).draggable(true).title(list.get(favId).getName()));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }
    private void setMarker(LatLng latLng){
        MarkerOptions options = new MarkerOptions().position(latLng).title("Address:"+address)
                .snippet("Your Destination").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mMap.addMarker(options);


    }
    private void getAddress(LatLng latLng){
        List<Address> addressList;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String dateTime=dateFormatter();
        try{
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
            if(!addressList.isEmpty()){
                address = addressList.get(0).getLocality() + " " + addressList.get(0).getAddressLine(0);
                System.out.println(addressList.get(0).getAddressLine(0));
                FavouritePlacesBean favouritePlacesBean = new FavouritePlacesBean();
                assert favouritePlacesBean != null;
                favouritePlacesBean.setName(addressList.get(0).getLocality());
                favouritePlacesBean.setAddress(addressList.get(0).getAddressLine(0));
                favouritePlacesBean.setLatitude(latLng.latitude);
                favouritePlacesBean.setLongitude(latLng.longitude);
                favouritePlacesBean.setDate(dateTime);
                databaseClient.favouritePlacesDao().insert(favouritePlacesBean);
                Toast.makeText(UpdateFavoritePlace.this, "Address: "+addressList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();

            }
        }catch (IOException e){
            e.printStackTrace();

        }

    }

    public String dateFormatter(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        String date = simpleDateFormat.format(calendar.getTime());
        return date;
    }
}