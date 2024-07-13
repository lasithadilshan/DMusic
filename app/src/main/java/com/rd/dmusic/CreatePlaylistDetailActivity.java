package com.rd.dmusic;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreatePlaylistDetailActivity extends AppCompatActivity {

    private EditText playlistName;
    private RecyclerView songsRecyclerView;
    private CreatePlaylistSongsAdapter songsAdapter;
    private ArrayList<Song> songsList = new ArrayList<>();
    private Set<Integer> selectedSongsList = new HashSet<>();
    private ImageView tickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist_detail);

        playlistName = findViewById(R.id.playlistName);
        songsRecyclerView = findViewById(R.id.songsRecyclerView);
        tickButton = findViewById(R.id.tickButton);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        tickButton.setOnClickListener(v -> {
            ArrayList<Song> selectedSongs = new ArrayList<>();
            for (int position : selectedSongsList) {
                selectedSongs.add(songsList.get(position));
            }
            Intent intent = new Intent(CreatePlaylistDetailActivity.this, CreatePlaylistActivity.class);
            intent.putExtra("playlistName", playlistName.getText().toString());
            intent.putExtra("selectedSongsCount", selectedSongs.size());
            intent.putExtra("playlistSongsList", selectedSongs);
            startActivity(intent);
        });

        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new CreatePlaylistSongsAdapter(songsList, position -> {
            if (selectedSongsList.contains(position)) {
                selectedSongsList.remove(position);
            } else {
                selectedSongsList.add(position);
            }
            Toast.makeText(this, "Song " + (selectedSongsList.contains(position) ? "added" : "removed"), Toast.LENGTH_SHORT).show();
        });
        songsRecyclerView.setAdapter(songsAdapter);

        loadSongs();
    }

    private void loadSongs() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            int albumIdIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);

            do {
                String title = cursor.getString(titleIndex);
                String artist = cursor.getString(artistIndex);
                String duration = cursor.getString(durationIndex);
                String data = cursor.getString(dataIndex);
                long albumId = cursor.getLong(albumIdIndex);

                Song song = new Song(title, artist, duration, data, albumId);
                songsList.add(song);
            } while (cursor.moveToNext());
            cursor.close();
        }
        songsAdapter.notifyDataSetChanged();
    }
}
