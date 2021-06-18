package com.example.awesomeapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.awesomeapp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String image;
    private String name;
    private String url;

    @BindView(R.id.ll_toolbar)
    LinearLayout back;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.name)
    TextView nameText;
    @BindView(R.id.url)
    TextView stringUrl;
    @BindView(R.id.title)
    TextView titleText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getExtrasIntent();
        setUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void getExtrasIntent() {
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        url = getIntent().getStringExtra("url");
    }

    public void setUI() {
        back.setOnClickListener(this);
        try {
            titleText.setText("Detail "+name);
            Picasso.get().load(image).into(imageView);
            nameText.setText(name);
            stringUrl.setText(url);
        } catch (Exception e) {
            Picasso.get().load("https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg").into(imageView);
            nameText.setText(R.string.none);
            stringUrl.setText(R.string.none);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_toolbar:
                onBackPressed();
                break;
        }
    }
}
