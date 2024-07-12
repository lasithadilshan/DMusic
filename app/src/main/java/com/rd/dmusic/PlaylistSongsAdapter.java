package com.rd.dmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PlaylistSongsAdapter extends RecyclerView.Adapter<PlaylistSongsAdapter.SongViewHolder> {
    private ArrayList<Song> songsList;
    private OnSongClickListener listener;

    public PlaylistSongsAdapter(ArrayList<Song> songsList, OnSongClickListener listener) {
        this.songsList = songsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_all_songs, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songsList.get(position);
        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getArtist());
        holder.songDuration.setText(song.getDuration());
        holder.playButton.setOnClickListener(v -> listener.onSongClick(position));
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public interface OnSongClickListener {
        void onSongClick(int position);
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtist, songDuration;
        ImageButton playButton;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtist = itemView.findViewById(R.id.songArtist);
            songDuration = itemView.findViewById(R.id.songDuration);
            playButton = itemView.findViewById(R.id.playButton);
        }
    }
}
