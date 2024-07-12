package com.rd.dmusic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class AllSongsActivity extends AppCompatActivity {
    private static final int STORAGE_PERMISSION_CODE = 1;
    private ArrayList<Song> songsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private FloatingActionButton fabAddPlaylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new SongsAdapter(songsList, this::onSongClick);
        recyclerView.setAdapter(songsAdapter);

        fabAddPlaylist = findViewById(R.id.fabAddPlaylist);
        fabAddPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(AllSongsActivity.this, CreatePlaylistDetailActivity.class);
            startActivity(intent);
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            loadSongs();
        }
    }

    private void loadSongs() {
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            try {
                do {
                    String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                    songsList.add(new Song(title, artist, duration, path, albumId));
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        songsAdapter.notifyDataSetChanged();
    }

    private void onSongClick(int position) {
        Intent intent = new Intent(AllSongsActivity.this, PlaySongActivity.class);
        intent.putExtra("songsList", songsList);
        intent.putExtra("songIndex", position);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            loadSongs();
        }
    }
}
