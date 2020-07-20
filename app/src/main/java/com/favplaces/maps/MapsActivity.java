package com.favplaces.maps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.favplaces.R;
import com.favplaces.maps.helper.GetDirections;
import com.favplaces.maps.helper.NearByPlaces;
import com.favplaces.roomDb.AppDatabase;
import com.favplaces.roomDb.DatabaseClient;
import com.favplaces.roomDb.FavouritePlacesBean;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final int REQUEST_CODE = 1;
    LocationCallback locationCallback;
    LocationRequest locationRequest;
    LatLng currentLocation;
    String address;
    final int RADIUS = 2000;
    LatLng customMarker;
    TextView viewMore;
    AppDatabase databaseClient;
    FloatingActionButton direction;
    FloatingActionButton minSheet;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    String url;
    Object[] objects = new Object[2];
    NearByPlaces getNearbyPlaceData;
    Button viewFavoritePlaces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        databaseClient = new DatabaseClient(this).getAppDatabase();
        getNearbyPlaceData=new NearByPlaces(getApplicationContext());
        if (!checkPermission()) {
            requestPermission();
        } else {
            getUserLocation();
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        }
        bottom_sheet = findViewById(R.id.bottom_sheet);
        direction = findViewById(R.id.fbDirection);
        viewMore = findViewById(R.id.tvMore);
        viewFavoritePlaces = findViewById(R.id.btnFavoritePlaces);
        minSheet = findViewById(R.id.btn_bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        viewFavoritePlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FavoritePlaces.class));
            }
        });
        direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customMarker!=null){
                    setUpDirection();
                }else {
                    Toast.makeText(MapsActivity.this, "Please mark destination", Toast.LENGTH_SHORT).show();
                }
            }
        });
        minSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
viewMore.setText("More Functionality");
                } else {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    viewMore.setText("Press more to get more info");
                }
            }
        });
        Spinner spinner = (Spinner)findViewById(R.id.maptype);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){

                    case 0:
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        break;

                    case 1:
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

                        break;

                    case 2:
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

                        break;
                    case 3:
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;

                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Spinner nearby = (Spinner)findViewById(R.id.spNearby);



        nearby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:

                        break;
                    case 1:
                        url = getUrl(currentLocation.latitude, currentLocation.longitude,"restaurant");

                        objects[0] = mMap;
                        objects[1] = url;
                        Log.d("", "btnClick: " + url);

                        getNearbyPlaceData.execute(objects);
                        Toast.makeText(MapsActivity.this,"Restaurants", Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        url = getUrl(currentLocation.latitude, currentLocation.longitude, "museum");

                        objects[0] = mMap;
                        objects[1] = url;

                        getNearbyPlaceData.execute(objects);
                        Toast.makeText(MapsActivity.this, "Museums", Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        url = getUrl(currentLocation.latitude, currentLocation.longitude, "cafe");
                        objects = new Object[2];
                        objects[0] = mMap;
                        objects[1] = url;
                        getNearbyPlaceData.execute(objects);
                        Toast.makeText(MapsActivity.this, "Cafe'", Toast.LENGTH_SHORT).show();
                        break;


                    default:
                        Toast.makeText(MapsActivity.this, "Nothing Selected View ", Toast.LENGTH_SHORT).show();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
// callback for do something

    }

    private void gtMyFavoriteLocations() {
        List<FavouritePlacesBean> favouritePlacesBean=databaseClient.favouritePlacesDao().getAll();
        for (int i=0;i<favouritePlacesBean.size();i++){
            setMarkerSaved(favouritePlacesBean.get(i));
        }
    }
private void setMarkerSaved(FavouritePlacesBean favouritePlacesBean){
    LatLng fav = new LatLng(favouritePlacesBean.getLatitude(), favouritePlacesBean.getLongitude());
    CameraPosition cameraPosition = CameraPosition.builder()
            .target(fav)
            .zoom(15)
            .bearing(0)
            .tilt(45)
            .build();
    mMap.addMarker(new MarkerOptions().position(fav).draggable(true).title(favouritePlacesBean.getName()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
}
    private String getDirectionUrl() {
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        urlBuilder.append("origin="+currentLocation.latitude+","+currentLocation.longitude);
        urlBuilder.append("&destination="+customMarker.latitude+","+customMarker.longitude);
        urlBuilder.append("&key="+getString(R.string.google_maps_key));
        Log.d("", "getDirectionUrl: "+urlBuilder);
        return urlBuilder.toString();
    }


    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        StringBuilder placeUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        placeUrl.append("location="+latitude+","+longitude);
        placeUrl.append("&radius="+RADIUS);
        placeUrl.append("&type="+nearbyPlace);
        placeUrl.append("&key="+getString(R.string.google_maps_key));
        System.out.println(placeUrl.toString());
        return placeUrl.toString();
    }

    private void getUserLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setSmallestDisplacement(10);
        setHomeMarker();
    }

    private void setHomeMarker() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                    currentLocation = userLocation;

                    CameraPosition cameraPosition = CameraPosition.builder()
                            .target(userLocation)
                            .zoom(15)
                            .bearing(0)
                            .tilt(45)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    mMap.addMarker(new MarkerOptions().position(userLocation)
                            .title("Your Location"));
                    // mDatabase.addFavrtPlaces()
                    //.icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.icon_loc)));
                }
            }
        };
    }
    private void setUpDirection(){
        objects = new Object[4];
        url = getDirectionUrl();
        objects[0] = mMap;
        objects[1] = url;
        objects[2] = customMarker;
        objects[3] = new LatLng(currentLocation.latitude,currentLocation.longitude);
        GetDirections getDirectionsData = new GetDirections();

        getDirectionsData.execute(objects);
    }

    private boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setHomeMarker();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        }
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
                Toast.makeText(MapsActivity.this, "Address: "+addressList.get(0).getAddressLine(0), Toast.LENGTH_SHORT).show();

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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gtMyFavoriteLocations();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                customMarker = latLng;
                setMarker(latLng);


                getAddress(customMarker);
            }
        });
    }
}