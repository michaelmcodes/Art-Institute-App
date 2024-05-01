package com.application.artworkapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.application.artworkapp.databinding.ActivityImageBinding;
import com.application.artworkapp.model.Artwork;
import com.application.artworkapp.util.Constants;
import com.application.artworkapp.util.Utils;
import com.google.gson.Gson;

public class ImageActivity extends AppCompatActivity {

    private ActivityImageBinding binding;

    private Artwork artwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            String jsonArtwork = getIntent().getStringExtra(Constants.ARTWORK_BUNDLE);
            artwork = new Gson().fromJson(jsonArtwork, Artwork.class);
            initializeViews();
        }

        binding.ivLogo.setOnClickListener(view -> {
            Intent intent = new Intent(ImageActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });
    }

    private void initializeViews() {
        if (artwork != null) {
            binding.tvTitle.setText(artwork.getTitle());
            String[] splitArtistDisplay = artwork.getArtistDisplay().split("\n");
            if (splitArtistDisplay.length == 2) {
                binding.tvArtistName.setText(splitArtistDisplay[0]);
                binding.tvArtistCountryDOB.setText(splitArtistDisplay[1]);
            }

            Utils.loadImageWithPicasso(Utils.getFullImageUrl(artwork.getImageId()), binding.pvImage);
        }
    }
}