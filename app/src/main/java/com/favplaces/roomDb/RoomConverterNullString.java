package com.favplaces.roomDb;

import androidx.room.TypeConverter;

public class RoomConverterNullString {
    @TypeConverter
    public static String fromNullToString(String value) {
        return (value == null) ? "" : value;
    }
}
