package com.rd.dmusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CreatePlaylistSongsAdapter extends RecyclerView.Adapter<CreatePlaylistSongsAdapter.SongViewHolder> {
    private ArrayList<Song> songsList;
    private Set<Integer> selectedPositions = new HashSet<>();
    private OnSongClickListener listener;

    public CreatePlaylistSongsAdapter(ArrayList<Song> songsList, OnSongClickListener listener) {
        this.songsList = songsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_select, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songsList.get(position);
        holder.songTitle.setText(song.getTitle());
        holder.songArtist.setText(song.getArtist());
        holder.songDuration.setText(song.getDuration());
        holder.itemView.setBackgroundColor(selectedPositions.contains(position) ? 0xFFFAFAFA : 0xFFFFFFFF);

        holder.addButton.setOnClickListener(v -> {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position);
            } else {
                selectedPositions.add(position);
            }
            notifyItemChanged(position);
            listener.onSongClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public Set<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    public interface OnSongClickListener {
        void onSongClick(int position);
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle, songArtist, songDuration;
        ImageButton addButton;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtist = itemView.findViewById(R.id.songArtist);
            songDuration = itemView.findViewById(R.id.songDuration);
            addButton = itemView.findViewById(R.id.addButton);
        }
    }
}
