package com.rd.dmusic;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    private String artist;
    private String duration;
    private String path;
    private long albumId;

    public Song(String title, String artist, String duration, String path, long albumId) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public long getAlbumId() {
        return albumId;
    }
}