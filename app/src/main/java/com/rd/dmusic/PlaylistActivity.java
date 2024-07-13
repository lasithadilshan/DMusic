package com.rd.dmusic;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PlaylistActivity extends AppCompatActivity {

    private TextView playlistNameView, nowPlayingSongTitle;
    private RecyclerView playlistRecyclerView;
    private PlaylistSongsAdapter songsAdapter;
    private ArrayList<Song> playlistSongsList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private int currentSongIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        playlistNameView = findViewById(R.id.playlistNameView);
        nowPlayingSongTitle = findViewById(R.id.nowPlayingSongTitle); // TextView for displaying now playing song
        playlistRecyclerView = findViewById(R.id.playlistRecyclerView);
        ImageButton playAllButton = findViewById(R.id.playAllButton);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        Intent intent = getIntent();
        String playlistName = intent.getStringExtra("playlistName");
        playlistSongsList = (ArrayList<Song>) intent.getSerializableExtra("playlistSongsList");
        playlistNameView.setText(playlistName);

        playlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        songsAdapter = new PlaylistSongsAdapter(playlistSongsList, this::onSongClick);
        playlistRecyclerView.setAdapter(songsAdapter);

        playAllButton.setOnClickListener(v -> playAllSongs());

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> playNextSong());
    }

    private void onSongClick(int position) {
        playSong(position);
    }

    private void playAllSongs() {
        if (!playlistSongsList.isEmpty()) {
            playSong(0);
        }
    }

    private void playSong(int position) {
        currentSongIndex = position;
        Song song = playlistSongsList.get(position);
        nowPlayingSongTitle.setText("Now Playing: " + song.getTitle()); // Update the now playing text
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error playing song", Toast.LENGTH_SHORT).show(); // Show error message
        }
    }

    private void playNextSong() {
        if (currentSongIndex < playlistSongsList.size() - 1) {
            playSong(currentSongIndex + 1);
        } else {
            mediaPlayer.stop();
            nowPlayingSongTitle.setText("Playlist ended."); // Update when playlist ends
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}
