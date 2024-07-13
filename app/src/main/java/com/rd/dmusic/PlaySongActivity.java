package com.rd.dmusic;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.InputStream;
import java.util.ArrayList;

public class PlaySongActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 1;
    private TextView songNameView, artistNameView;
    private ImageView albumCoverView;
    private SeekBar seekBar;
    private ImageButton playButton, prevButton, nextButton;
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> playlistSongsList;
    private int currentSongIndex = 0;
    private static final String TAG = "PlaySongActivity";
    private Handler seekBarHandler;
    private Runnable updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        initializeViews();
        checkStoragePermission();
    }

    private void initializeViews() {
        songNameView = findViewById(R.id.songName);
        artistNameView = findViewById(R.id.artistName);
        albumCoverView = findViewById(R.id.albumCover);
        seekBar = findViewById(R.id.seekBar);
        playButton = findViewById(R.id.playButton);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            initializeMediaPlayer();
        }
    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> playNextSong());
        setupMediaPlayerListeners();
        setupSeekBar();

        // Retrieve the playlist safely
        playlistSongsList = (ArrayList<Song>) getIntent().getSerializableExtra("songsList");
        currentSongIndex = getIntent().getIntExtra("songIndex", 0);

        // Check if the playlist is null or empty
        if (playlistSongsList == null || playlistSongsList.isEmpty()) {
            Log.e(TAG, "Playlist is empty or not loaded.");
            // Use runOnUiThread to ensure the Toast is shown on the UI thread
            runOnUiThread(() -> {
                Toast.makeText(this, "The playlist is empty or cannot be loaded.", Toast.LENGTH_LONG).show();
            });
            finish(); // Close this activity as there's nothing to play
        } else {
            playSong(currentSongIndex); // Play the song if the list is valid
        }
    }



    private void setupMediaPlayerListeners() {
        playButton.setOnClickListener(v -> playPauseSong());
        prevButton.setOnClickListener(v -> playPreviousSong());
        nextButton.setOnClickListener(v -> playNextSong());
    }

    private void setupSeekBar() {
        seekBarHandler = new Handler();
        updateSeekBar = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    seekBarHandler.postDelayed(this, 1000);
                }
            }
        };

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void playSong(int index) {
        currentSongIndex = index;
        Song song = playlistSongsList.get(index);
        Log.d(TAG, "Playing song: " + song.getTitle() + " by " + song.getArtist());
        songNameView.setText(song.getTitle());
        artistNameView.setText(song.getArtist());

        // Set album cover image
        Bitmap albumArt = getAlbumArt(song.getAlbumId());
        if (albumArt != null) {
            albumCoverView.setImageBitmap(albumArt);
        } else {
            albumCoverView.setImageResource(R.drawable.default_album_cover); // Default album cover
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(song.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.ic_pause);
            seekBar.setMax(mediaPlayer.getDuration());
            seekBarHandler.postDelayed(updateSeekBar, 0);
        } catch (Exception e) {
            Log.e(TAG, "Error playing song: " + e.getMessage(), e);
        }
    }

    private Bitmap getAlbumArt(long albumId) {
        Uri albumArtUri = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
        try (InputStream inputStream = getContentResolver().openInputStream(albumArtUri)) {
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving album art", e);
            return null;
        }
    }

    private void playPauseSong() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.ic_play);
        } else {
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.ic_pause);
        }
    }

    private void playPreviousSong() {
        if (currentSongIndex > 0) {
            playSong(currentSongIndex - 1);
        } else {
            Log.d(TAG, "No previous song to play.");
        }
    }

    private void playNextSong() {
        if (currentSongIndex < playlistSongsList.size() - 1) {
            playSong(currentSongIndex + 1);
        } else {
            Log.d(TAG, "No more songs to play. Stopping playback.");
            mediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            seekBarHandler.removeCallbacks(updateSeekBar);
            mediaPlayer = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initializeMediaPlayer();
        } else {
            Toast.makeText(this, "Permission denied to read external storage", Toast.LENGTH_SHORT).show();
        }
    }
}
