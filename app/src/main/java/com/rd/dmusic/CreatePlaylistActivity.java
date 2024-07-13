package com.rd.dmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;

public class CreatePlaylistActivity extends AppCompatActivity {

    private TextView playlistNameView;
    private TextView songsCountView;
    private CardView createdPlaylist;
    private ArrayList<Song> playlistSongsList;
    private ImageButton playAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        playlistNameView = findViewById(R.id.playlistNameView);
        songsCountView = findViewById(R.id.songsCountView);
        createdPlaylist = findViewById(R.id.playlistCard);
        playAllButton = findViewById(R.id.playAllButton); // Reference to the play all button

        // Extract data from the intent
        String playlistName = getIntent().getStringExtra("playlistName");
        int selectedSongsCount = getIntent().getIntExtra("selectedSongsCount", 0);
        playlistSongsList = (ArrayList<Song>) getIntent().getSerializableExtra("playlistSongsList");

        // Set text views
        playlistNameView.setText(playlistName);
        songsCountView.setText("Songs: " + selectedSongsCount);

        // Set onClickListener for the playAllButton to start playing all songs
        playAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playlistSongsList != null && !playlistSongsList.isEmpty()) {
                    Intent intent = new Intent(CreatePlaylistActivity.this, PlaySongActivity.class);
                    intent.putExtra("songsList", playlistSongsList);  // This should match the key expected in PlaySongActivity
                    intent.putExtra("songIndex", 0);  // Optional: Start playing from the first song
                    startActivity(intent);
                } else {
                    Toast.makeText(CreatePlaylistActivity.this, "No songs in the playlist to play.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
