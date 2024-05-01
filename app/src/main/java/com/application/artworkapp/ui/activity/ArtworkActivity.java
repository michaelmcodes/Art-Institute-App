package com.application.artworkapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import com.application.artworkapp.R;
import com.application.artworkapp.databinding.ActivityArtworkBinding;
import com.application.artworkapp.model.Artwork;
import com.application.artworkapp.util.Constants;
import com.application.artworkapp.util.Utils;
import com.google.gson.Gson;

public class ArtworkActivity extends AppCompatActivity {

    private ActivityArtworkBinding binding;

    private Artwork artwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtworkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent() != null) {
            String jsonArtwork = getIntent().getStringExtra(Constants.ARTWORK_BUNDLE);
            artwork = new Gson().fromJson(jsonArtwork, Artwork.class);
            initializeViews();

            if (artwork.getGalleryTitle() != null) {
                binding.tvGalleryTitle.setOnClickListener(v -> Utils.openLinkInBrowser(ArtworkActivity.this, Utils.getGalleryUrl(String.valueOf(artwork.getGalleryId()))));
            }

            binding.ivImage.setOnClickListener(v -> {
                Intent intent = new Intent(ArtworkActivity.this, ImageActivity.class);
                intent.putExtra(Constants.ARTWORK_BUNDLE, jsonArtwork);
                startActivity(intent);
            });
        }

        binding.ivLogo.setOnClickListener(view -> {
            finish();
        });
    }

    private void initializeViews() {
        if (artwork != null) {
            binding.tvTitle.setText(artwork.getTitle());
            binding.tvDate.setText(artwork.getDateDisplay());

            if (artwork.getArtistDisplay().contains("\n")) {
                String[] splitArtistDisplay = artwork.getArtistDisplay().split("\n");
                if (splitArtistDisplay.length == 2) {
                    binding.tvArtistName.setText(splitArtistDisplay[0]);
                    binding.tvArtistCountryDOB.setText(splitArtistDisplay[1]);
                }
            } else {
                binding.tvArtistName.setText(artwork.getArtistDisplay());
                binding.tvArtistCountryDOB.setText("");

            }
            binding.tvDepartmentName.setText(artwork.getDepartmentTitle());
            if (artwork.getGalleryTitle() != null) {

                SpannableString content = new SpannableString(artwork.getGalleryTitle());
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                binding.tvGalleryTitle.setText(content);
            } else {
                binding.tvGalleryTitle.setText(getString(R.string.not_on_display));
            }
            binding.tvLocation.setText(artwork.getPlaceOfOrigin());
            binding.tvTypeMedium.setText(artwork.getArtworkTypeTitle() + " - " + artwork.getMediumDisplay());
            binding.tvDimensions.setText(artwork.getDimensions());
            binding.tvCredit.setText(artwork.getCreditLine());

            Utils.loadImageWithPicasso(Utils.getFullImageUrl(artwork.getImageId()), binding.ivImage);
        }
    }
}