package com.example.musicplayer.roomDb;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {MusicPlayed.class}, version = 1, exportSchema = false)
public abstract class MusicPlayedDatabase extends RoomDatabase {
    public abstract MusicPlayedDao musicPlayedDao();
}
