package com.application.artworkapp.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.widget.ImageView;

import com.application.artworkapp.R;
import com.squareup.picasso.Picasso;

public class Utils {

    public static void openLinkInBrowser(Activity activity, String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(browserIntent);
    }

    public static boolean isNetworkAvailable(Application application) {
        ConnectivityManager connectivityManager = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network nw = connectivityManager.getActiveNetwork();
        if (nw == null) return false;
        NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
        return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));

    }

    public static void loadImageWithPicasso(String url, ImageView imageView) {
        Picasso.get().load(url).error(R.drawable.not_available).into(imageView);
    }

    public static String getThumbnailUrl(String imageId) {
        return String.format("https://www.artic.edu/iiif/2/%s/full/200,/0/default.jpg", imageId);
    }

    public static String getFullImageUrl(String imageId) {
        return String.format("https://www.artic.edu/iiif/2/%s/full/843,/0/default.jpg", imageId);
    }
    public static String getGalleryUrl(String galleryId) {
        return String.format("https://www.artic.edu/galleries/%s", galleryId);
    }

}
