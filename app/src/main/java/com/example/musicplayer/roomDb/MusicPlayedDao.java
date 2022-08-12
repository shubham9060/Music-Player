package com.example.musicplayer.roomDb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MusicPlayedDao {
    @Query("SELECT * FROM LastPlayedMusic")
    List<MusicPlayed> getAll();
    @Insert
    void insert(MusicPlayed musicPlayed);
}
