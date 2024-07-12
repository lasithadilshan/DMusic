package com.rd.dmusic;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class CreatePlaylistDetailActivity extends AppCompatActivity {

    private EditText playlistName;
    private RecyclerView songsRecyclerView;
    private CreatePlaylistSongsAdapter songsAdapter;
    private ArrayList<Song> songsList = new ArrayList<>();
    private ArrayList<Song> selectedSongsList = new ArrayList<>();
    private ImageView tickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist_detail);

        playlistName = findViewById(R.id.playlistName);
        songsRecyclerView = findViewById(R.id.songsRecyclerView);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        tickButton = findViewById(R.id.tickButton);
        tickButton.setOnClickListener(v -> {
            selectedSongsList.clear();
            for (int position : songsAdapter.getSelectedPositions()) {
                selectedSongsList.add(songsList.get(position));
            }
            Intent intent = new Intent(CreatePlaylistDetailActivity.this, CreatePlaylistActivity.class);
            intent.putExtra("playlistName", playlistName.getText().toString());
            intent.putExtra("selectedSongsCount", selectedSongsList.size());
            startActivity(intent);
        });

        songsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new CreatePlaylistSongsAdapter(songsList, this::onAddButtonClick);
        songsRecyclerView.setAdapter(songsAdapter);

        loadSongs();  // Method to load songs from the device
    }

    private void loadSongs() {
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                songsList.add(new Song(title, artist, duration, path, albumId));
            } while (cursor.moveToNext());
            cursor.close();
        }
        songsAdapter.notifyDataSetChanged();
    }

    private void onAddButtonClick(int position) {
        // No action needed for now
    }
}
