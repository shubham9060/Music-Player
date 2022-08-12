package com.example.musicplayer.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicMainActivity extends AppCompatActivity {
    private ListView musicList;
    private String[] songDetailList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicList = findViewById(R.id.listViewSongs);
        runTimePermission();
    }

    private void runTimePermission() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        displaySong();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list,
                                                                   PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private ArrayList<File> findSong(File file) {
        ArrayList<File> songList = new ArrayList<>();
        File[] files = file.listFiles();
        assert files != null;
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                songList.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(Constant.MP3_EXTENSION) || singleFile.getName().
                        endsWith(Constant.WAV_EXTENSION)) {
                    songList.add(singleFile);
                }
            }
        }
        return songList;
    }

    private void displaySong() {
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        songDetailList = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++) {
            songDetailList[i] = mySongs.get(i).getName().replace(Constant.MP3_EXTENSION, "")
                    .replace(Constant.WAV_EXTENSION, "");
        }
        moveToPlayerActivity(mySongs);
    }

    private void moveToPlayerActivity(ArrayList<File> mySongs) {
        CustomAdapter customAdapter = new CustomAdapter();

        musicList.setAdapter(customAdapter);
        musicList.setOnItemClickListener((adapterView, view, i, l) -> {
            String songName = (String) musicList.getItemAtPosition(i);
            Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
            intent.putExtra(Constant.SONGS, mySongs);
            intent.putExtra(Constant.SONGS_NAME, songName);
            intent.putExtra(Constant.POSITION, i);
            startActivity(intent);

        });

    }

    public class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return songDetailList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            @SuppressLint({Constant.VIEW_HOLDER, Constant.INFLATE_PARAMETER, "InflateParams", "ViewHolder"}) View view = getLayoutInflater()
                    .inflate(R.layout.song_name_layout, null);
            TextView txtSong = view.findViewById(R.id.tvSongName);
            txtSong.setSelected(true);
            txtSong.setText(songDetailList[position]);
            return view;
        }
    }
}
