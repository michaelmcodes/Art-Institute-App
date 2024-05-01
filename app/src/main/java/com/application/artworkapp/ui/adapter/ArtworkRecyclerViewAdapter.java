package com.application.artworkapp.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.artworkapp.R;
import com.application.artworkapp.databinding.LayoutArtworkRecyclerViewBinding;
import com.application.artworkapp.model.Artwork;
import com.application.artworkapp.util.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ArtworkRecyclerViewAdapter extends RecyclerView.Adapter<ArtworkRecyclerViewAdapter.MyViewHolder> {

    private List<Artwork> artworks = new ArrayList<>();

    public void setArtworks(List<Artwork> artworks) {
        this.artworks = artworks;
        notifyDataSetChanged();
    }

    public Artwork getArtwork(int position) {
        return artworks.get(position);
    }

    public void clearData() {
        artworks.clear();
        notifyDataSetChanged();
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArtworkRecyclerViewBinding binding = LayoutArtworkRecyclerViewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Artwork artwork = artworks.get(position);
        holder.bind(artwork);
    }

    @Override
    public int getItemCount() {
        return artworks.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final LayoutArtworkRecyclerViewBinding binding;

        public MyViewHolder(@NonNull LayoutArtworkRecyclerViewBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;

            if (onItemClickListener != null)
                itemView.setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
        }

        public void bind(Artwork artwork) {
            Utils.loadImageWithPicasso(Utils.getThumbnailUrl(artwork.getImageId()), binding.ivArtwork);
            binding.tvArtworkTitle.setText(artwork.getTitle());
        }
    }
}
