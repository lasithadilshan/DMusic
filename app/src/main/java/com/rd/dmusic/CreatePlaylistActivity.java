package com.rd.dmusic;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;

public class CreatePlaylistActivity extends AppCompatActivity {

    private TextView playlistNameView;
    private TextView songsCountView;
    private LinearLayout createdPlaylist;
    private ArrayList<Song> playlistSongsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        playlistNameView = findViewById(R.id.playlistNameView);
        songsCountView = findViewById(R.id.songsCountView);
        createdPlaylist = findViewById(R.id.createdPlaylist);

        String playlistName = getIntent().getStringExtra("playlistName");
        int selectedSongsCount = getIntent().getIntExtra("selectedSongsCount", 0);
        playlistSongsList = (ArrayList<Song>) getIntent().getSerializableExtra("playlistSongsList");

        playlistNameView.setText(playlistName);
        songsCountView.setText("Songs: " + selectedSongsCount);

        createdPlaylist.setOnClickListener(v -> {
            Intent intent = new Intent(CreatePlaylistActivity.this, PlaySongActivity.class);
            intent.putExtra("playlistName", playlistName);
            intent.putExtra("playlistSongsList", playlistSongsList);
            startActivity(intent);
        });
    }
}
