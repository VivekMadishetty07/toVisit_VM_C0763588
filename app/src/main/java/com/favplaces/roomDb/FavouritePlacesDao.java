package com.favplaces.roomDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavouritePlacesDao {

    @Query("SELECT * FROM favorite_places")
    List<FavouritePlacesBean> getAll();

    @Insert
    void insert(FavouritePlacesBean contacts);

    @Delete
    void delete(FavouritePlacesBean contacts);

    @Update
    void update(FavouritePlacesBean contacts);

}