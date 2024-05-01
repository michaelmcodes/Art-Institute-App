package com.application.artworkapp.model;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.annotations.SerializedName;

public class Artwork {
    @SerializedName("medium_display")
    private String mediumDisplay;

    @SerializedName("artist_display")
    private String artistDisplay;

    @SerializedName("title")
    private String title;

    @SerializedName("gallery_title")
    private String galleryTitle;

    @SerializedName("place_of_origin")
    private String placeOfOrigin;

    @SerializedName("credit_line")
    private String creditLine;

    @SerializedName("artwork_type_title")
    private String artworkTypeTitle;

    @SerializedName("department_title")
    private String departmentTitle;

    @SerializedName("api_link")
    private String apiLink;

    @SerializedName("date_display")
    private String dateDisplay;

    @SerializedName("gallery_id")
    private long galleryId;

    @SerializedName("id")
    private int id;

    @SerializedName("image_id")
    private String imageId;

    @SerializedName("dimensions")
    private String dimensions;

    public int getId() {
        return id;
    }

    public long getGalleryId() {
        return galleryId;
    }

    public String getApiLink() {
        return apiLink;
    }

    public String getArtistDisplay() {
        return artistDisplay;
    }

    public String getArtworkTypeTitle() {
        return artworkTypeTitle;
    }

    public String getCreditLine() {
        return creditLine;
    }

    public String getDateDisplay() {
        return dateDisplay;
    }

    public String getDepartmentTitle() {
        return departmentTitle;
    }

    public String getDimensions() {
        return dimensions;
    }

    public String getGalleryTitle() {
        return galleryTitle;
    }

    public String getImageId() {
        return imageId;
    }

    public String getMediumDisplay() {
        return mediumDisplay;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public String getTitle() {
        return title;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
