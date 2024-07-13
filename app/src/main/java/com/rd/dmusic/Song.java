package com.rd.dmusic;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;   // Title of the song
    private String artist;  // Artist of the song
    private String duration;// Duration of the song
    private String path;    // File path to the song
    private long albumId;   // Database ID for the album cover

    /**
     * Constructs a new Song instance.
     *
     * @param title The title of the song
     * @param artist The artist of the song
     * @param duration The duration of the song
     * @param path The file system path to the song
     * @param albumId The album ID associated with the song
     */

    public Song(String title, String artist, String duration, String path, long albumId) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.albumId = albumId;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}