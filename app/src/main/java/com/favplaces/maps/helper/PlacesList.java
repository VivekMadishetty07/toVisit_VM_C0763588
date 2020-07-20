package com.favplaces.maps.helper;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.favplaces.R;

import java.util.ArrayList;
import java.util.List;


public class PlacesList extends AppCompatActivity {


     List<PlacesModel> listPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_list_saved_places);
        listPlace = new ArrayList<>();

        loadPlaces();





    }




    private void loadPlaces(){

       /* Cursor cursor = mDatabase.getPlaces();
        if(cursor.moveToFirst()){

            do{


                listPlace.add(new PlacesModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),cursor.getDouble(5)
                ));

            }while (cursor.moveToNext());

            cursor.close();
        }*/



    }

}
