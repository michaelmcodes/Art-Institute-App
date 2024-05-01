package com.application.artworkapp.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;

import com.application.artworkapp.util.Constants;
import com.application.artworkapp.util.Utils;
import com.application.artworkapp.databinding.ActivityCopyrightBinding;

public class CopyrightActivity extends AppCompatActivity {

    private ActivityCopyrightBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCopyrightBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SpannableString firstLinkContent = new SpannableString(Constants.ARTIC_API);
        firstLinkContent.setSpan(new UnderlineSpan(), 0, firstLinkContent.length(), 0);
        binding.tvFirstLink.setText(firstLinkContent);
        binding.tvFirstLink.setOnClickListener(v -> Utils.openLinkInBrowser(CopyrightActivity.this, Constants.ARTIC_API));

        SpannableString secondLinkContent = new SpannableString(Constants.CINZEL_FONT_API);
        secondLinkContent.setSpan(new UnderlineSpan(), 0, secondLinkContent.length(), 0);
        binding.tvSecondLink.setText(secondLinkContent);
        binding.tvSecondLink.setOnClickListener(v -> Utils.openLinkInBrowser(CopyrightActivity.this, Constants.CINZEL_FONT_API));

        binding.ivLogo.setOnClickListener(view -> {
            finish();
        });
    }
}