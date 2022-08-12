package com.example.musicplayer.roomDb;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "LastPlayedMusic")
@Dao
public class MusicPlayed {
    @PrimaryKey()
    @NonNull
    public String title;
    @ColumnInfo(name = "time")
    public  String  time;
}
