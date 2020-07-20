package com.favplaces.maps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.favplaces.R;
import com.favplaces.roomDb.AppDatabase;
import com.favplaces.roomDb.DatabaseClient;
import com.favplaces.roomDb.FavouritePlacesBean;

import java.util.ArrayList;
import java.util.List;

public class FavoritePlaces extends AppCompatActivity {
    RecyclerView favrecycleview;
    TextView noFavoritePlaces;
    AppDatabase database;
    ImageView back;
    ArrayList<FavouritePlacesBean> favList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_places);
        noFavoritePlaces=findViewById(R.id.tvNofavPlaces);
        favrecycleview=findViewById(R.id.rvFavPlaces);
        back=findViewById(R.id.ivBack);
        database= DatabaseClient.getInstance(getApplicationContext()).getAppDatabase();
        favList= (ArrayList<FavouritePlacesBean>) database.favouritePlacesDao().getAll();
        if (favList.size()==0){
            noFavoritePlaces.setVisibility(View.VISIBLE);
        }else {

            favrecycleview.setAdapter(new FavoritePlacesAdapter(getApplicationContext(),favList));
        }

    }
}