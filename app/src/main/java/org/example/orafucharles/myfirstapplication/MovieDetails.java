package org.example.orafucharles.myfirstapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.squareup.picasso.Picasso;
import java.util.Objects;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetails extends AppCompatActivity {
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    @BindView(R.id.thumbnail_image_header)
     ImageView imageHeader;
    @BindView(R.id.plot_synopsis)
     TextView plotSynopsis;
    @BindView(R.id.title)
     TextView title;
    @BindView(R.id.user_rating)
     TextView userRating;
    @BindView(R.id.release_date)
     TextView releaseDate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(ORIGINAL_TITLE)) {

            //get all needed extras intent
            String thumbnail = Objects.requireNonNull(getIntent().getExtras()).getString(POSTER_PATH);
            String movieName = getIntent().getExtras().getString(ORIGINAL_TITLE);
            String synopsis = getIntent().getExtras().getString(OVERVIEW);
            String rating = getIntent().getExtras().getString(VOTE_AVERAGE);
            String dateOfRelease = getIntent().getExtras().getString(RELEASE_DATE);

            //setting data to appropriate views
            Picasso.with(this)
                    .load(thumbnail)
                    .placeholder(R.drawable.ic_loading)
                    .error(R.drawable.ic_loading)
                    .into(imageHeader);

            title.setText(movieName);
            plotSynopsis.setText(synopsis);
            userRating.setText(rating);
            releaseDate.setText(dateOfRelease);
        }else{

            Toast.makeText(this, R.string.no_api_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void initCollapsingToolbar() {
//        final CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
//        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar_pref expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.movie_details));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }
}
