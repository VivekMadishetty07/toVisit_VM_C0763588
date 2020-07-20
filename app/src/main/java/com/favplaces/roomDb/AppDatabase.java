package com.favplaces.roomDb;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FavouritePlacesBean.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavouritePlacesDao favouritePlacesDao();
}
