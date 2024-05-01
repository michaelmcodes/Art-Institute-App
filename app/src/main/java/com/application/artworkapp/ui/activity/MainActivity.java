package com.application.artworkapp.ui.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.application.artworkapp.R;
import com.application.artworkapp.model.Artwork;
import com.application.artworkapp.model.Gallery;
import com.application.artworkapp.model.GalleryData;
import com.application.artworkapp.ui.adapter.ArtworkRecyclerViewAdapter;
import com.application.artworkapp.util.Constants;
import com.application.artworkapp.util.Utils;
import com.application.artworkapp.databinding.ActivityMainBinding;
import com.application.artworkapp.model.ArtworkData;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding binding;

    private final ArtworkRecyclerViewAdapter artworksAdapter = new ArtworkRecyclerViewAdapter();
    private boolean isLoading = false;

    private final List<Gallery> galleriesIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvCopyright.setOnClickListener(this);
        binding.btnSearch.setOnClickListener(this);
        binding.ivClearSearch.setOnClickListener(this);
        binding.btnRandom.setOnClickListener(this);

        binding.rvArtworks.setAdapter(artworksAdapter);
        binding.rvArtworks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        artworksAdapter.setOnItemClickListener(position -> {
            Artwork artwork = artworksAdapter.getArtwork(position);
            Intent intent = new Intent(MainActivity.this, ArtworkActivity.class);
            intent.putExtra(Constants.ARTWORK_BUNDLE, new Gson().toJson(artwork));
            startActivity(intent);
        });

        binding.ivLogo.setOnClickListener(view -> artworksAdapter.clearData());
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSearch) {
            search();
        } else if (view.getId() == R.id.tvCopyright) {
            startActivity(new Intent(MainActivity.this, CopyrightActivity.class));
        } else if (view.getId() == R.id.ivClearSearch) {
            binding.etSearch.getText().clear();
        } else if (view.getId() == R.id.btnRandom) {
            randomize();
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.logo);

        builder.setPositiveButton(R.string.ok, (dialog, id) -> {
            //actions
            dialog.cancel();
        });
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView titleTextView = dialog.findViewById(androidx.appcompat.R.id.alertTitle);
        if (titleTextView != null)
            titleTextView.setTypeface(null, Typeface.BOLD);

        TextView messageTextView = dialog.findViewById(android.R.id.message);
        if (messageTextView != null)
            messageTextView.setTypeface(null, Typeface.NORMAL);

        TextView closeTextView = dialog.findViewById(android.R.id.button1);
        if (closeTextView != null)
            closeTextView.setTypeface(null, Typeface.NORMAL);
    }

    private void search() {
        String searchedText = binding.etSearch.getText().toString().trim();

        if (isLoading) {
            return;
        }
        if (searchedText.length() == 0) {
            return;
        }
        if (searchedText.length() < 3) {
            String title = getString(R.string.search_string_too_short);
            String message = getString(R.string.try_longer_search);
            showAlertDialog(title, message);
            return;
        }
        if (!Utils.isNetworkAvailable(getApplication())) {
            String title = getString(R.string.no_connection);
            String message = getString(R.string.no_network);
            showAlertDialog(title, message);
            return;
        }

        getArtworksFromAPI(searchedText);

    }

    private void randomize() {
        isLoading = true;
        binding.pvLoadingArtwork.setVisibility(View.VISIBLE);
        binding.rvArtworks.setVisibility(View.GONE);
        binding.ivMainImage.setVisibility(View.GONE);
        galleriesIds.clear();

        RequestQueue queue = Volley.newRequestQueue(this);
        getFirstPageOfGalleries(queue);


    }

    private void getFirstPageOfGalleries(RequestQueue queue) {
        String galleriesUrl = "https://api.artic.edu/api/v1/galleries?limit=100&fields=id&page=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, galleriesUrl,
                response -> {
                    Log.v("volley response", response);

                    GalleryData galleryData = new Gson().fromJson(response, GalleryData.class);
                    Log.v("number of data page1", galleryData.getData().size() + "");

                    galleriesIds.addAll(galleryData.getData());

                    getSecondPageOfGalleries(queue);


                }, error -> {
            if (error.getLocalizedMessage() != null)
                Log.e("volley error", error.getLocalizedMessage());
            isLoading = false;
            runOnUiThread(() -> {
                binding.pvLoadingArtwork.setVisibility(View.GONE);
                binding.ivMainImage.setVisibility(View.VISIBLE);
            });
        });

        queue.add(stringRequest);
    }

    private void getSecondPageOfGalleries(RequestQueue queue) {
        String galleriesUrl2 = "https://api.artic.edu/api/v1/galleries?limit=100&fields=id&page=2";

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, galleriesUrl2,
                response -> {
                    Log.v("volley response", response);

                    GalleryData galleryData = new Gson().fromJson(response, GalleryData.class);
                    Log.v("number of data page2", galleryData.getData().size() + "");

                    galleriesIds.addAll(galleryData.getData());

                    Log.v("size", galleriesIds.size() + "");

                    getRandomArtwork(queue, galleriesIds.get(getRandomIndex(galleriesIds.size())).getId());

                }, error -> {
            if (error.getLocalizedMessage() != null)
                Log.e("volley error", error.getLocalizedMessage());
            isLoading = false;
            runOnUiThread(() -> {
                binding.pvLoadingArtwork.setVisibility(View.GONE);
                binding.ivMainImage.setVisibility(View.VISIBLE);
            });
        });
        queue.add(stringRequest2);
    }

    private void getRandomArtwork(RequestQueue queue, int galleryId) {
        String url = String.format("https://api.artic.edu/api/v1/artworks/search?query[term][gallery_id]=%s&limit=100&fields=title,date_display,artist_display,medium_display,artwork_type_title,image_id,dimensions,provenance_text ,department_title,credit_line,place_of_origin,gallery_title,gallery_id,id,api_link,thumbnail", galleryId + "");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    isLoading = false;
                    Log.v("volley response", response);

                    ArtworkData artworkData = new Gson().fromJson(response, ArtworkData.class);

                    if (artworkData.getData().size() > 0) {
                        int randomArtworkIndex = getRandomIndex(artworkData.getData().size());
                        Artwork artwork = artworkData.getData().get(randomArtworkIndex);
                        List<Artwork> artworkList = new ArrayList<>();
                        artworkList.add(artwork);
                        runOnUiThread(() -> {
                            artworksAdapter.setArtworks(artworkList);
                            binding.rvArtworks.setVisibility(View.VISIBLE);
                            binding.pvLoadingArtwork.setVisibility(View.GONE);
                        });
                    } else {
                        randomize();
                    }

                }, error -> {
            if (error.getLocalizedMessage() != null)
                Log.e("volley error", error.getLocalizedMessage());
            isLoading = false;
            runOnUiThread(() -> {
                binding.pvLoadingArtwork.setVisibility(View.GONE);
                binding.ivMainImage.setVisibility(View.VISIBLE);
            });
        });

        queue.add(stringRequest);
    }

    private int getRandomIndex(int size) {
        int randomIndex = new Random().nextInt(size);
        return randomIndex;
    }

    private void getArtworksFromAPI(String searchedText) {
        isLoading = true;
        binding.pvLoadingArtwork.setVisibility(View.VISIBLE);
        binding.rvArtworks.setVisibility(View.GONE);
        binding.ivMainImage.setVisibility(View.GONE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = String.format("https://api.artic.edu/api/v1/artworks/search?q=%s&limit=15&page=1&fields=title, date_display, artist_display, medium_display, artwork_type_title, image_id, dimensions, department_title, credit_line, place_of_origin, gallery_title, gallery_id, id, api_link", searchedText);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    isLoading = false;
                    Log.v("volley response", response);

                    ArtworkData artworkData = new Gson().fromJson(response, ArtworkData.class);

                    runOnUiThread(() -> {
                        if (artworkData.getData().size() > 0) {
                            artworksAdapter.setArtworks(artworkData.getData());

                        } else {
                            String title = getString(R.string.no_search_result_found);
                            String message = String.format(getString(R.string.no_result_found_for), searchedText.toUpperCase());
                            showAlertDialog(title, message);

                        }

                        binding.rvArtworks.setVisibility(View.VISIBLE);
                        binding.pvLoadingArtwork.setVisibility(View.GONE);
                    });

                }, error -> {
            if (error.getLocalizedMessage() != null)
                Log.e("volley error", error.getLocalizedMessage());
            isLoading = false;
            runOnUiThread(() -> {
                binding.pvLoadingArtwork.setVisibility(View.GONE);
                binding.ivMainImage.setVisibility(View.VISIBLE);
            });
        });

        queue.add(stringRequest);
    }
}