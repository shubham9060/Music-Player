package com.example.musicplayer.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.musicplayer.R;
import com.example.musicplayer.roomDb.MusicPlayed;
import com.example.musicplayer.roomDb.MusicPlayedDatabase;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btnPlay, btnNext, btnPrevious;
    TextView tvSongName;
    ImageView imageView;
    String songName;
    static MediaPlayer mediaPlayer;
    int position = 0;
   MusicPlayedDatabase database = Room.databaseBuilder(getApplicationContext(),
    MusicPlayedDatabase.class, "database-name").build();
    ArrayList<File> mySongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        btnPlay = findViewById(R.id.btnPlay);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        tvSongName = findViewById(R.id.tvSongs);
        imageView = findViewById(R.id.musicImage);
        if (mediaPlayer != null) {
            mediaPlayer.start();
            mediaPlayer.release();
        }

       createMusicPlayer();
    }
    private void createMusicPlayer()
    {
        Intent intent = getIntent();
        database.musicPlayedDao().insert(new MusicPlayed());
        position = intent.getIntExtra(Constant.POSITION, 0);
        tvSongName.setSelected(true);
        Uri uri = Uri.parse(mySongs.get(position).toString());
        songName = mySongs.get(position).getName();
        tvSongName.setText(songName);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();
        playMusic();
        mediaPlayer.setOnCompletionListener(mediaPlayer -> btnNext.performClick());
        moveToNextSongs();
        moveToPreviousMusic();
    }

    @SuppressLint("SimpleDateFormat")
    private void playMusic()
    {
        btnPlay.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                new SimpleDateFormat(Constant.DATE_AND_TIME_FORMAT);
                btnPlay.setBackgroundResource(R.drawable.play_song_icon);
                mediaPlayer.pause();
            } else {
                btnPlay.setBackgroundResource(R.drawable.pause_song_icon);
                mediaPlayer.start();
            }
        });
    }
    private void moveToNextSongs()
    {
        btnNext.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position + 1) % mySongs.size());
            Uri uri1 = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            songName = mySongs.get(position).getName();
            tvSongName.setText(songName);
            mediaPlayer.start();
        });
    }
    private void moveToPreviousMusic()
    {
        btnPrevious.setOnClickListener(view -> {
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position - 1) % mySongs.size());
            if (position < 0)
                position = mySongs.size() - 1;
            Uri uri1 = Uri.parse(mySongs.get(position).toString());
            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
            songName = mySongs.get(position).getName();
            tvSongName.setText(songName);
            mediaPlayer.start();
        });
    }
}