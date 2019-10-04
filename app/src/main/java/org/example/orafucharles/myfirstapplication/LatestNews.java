package org.example.orafucharles.myfirstapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.example.orafucharles.myfirstapplication.model.News;
import org.example.orafucharles.myfirstapplication.model.NewsAdapter;
import org.example.orafucharles.myfirstapplication.utilities.NewsLoader;

import java.util.ArrayList;
import java.util.List;


public class LatestNews  extends AppCompatActivity implements NewsAdapter.ItemClickListener,
        LoaderManager.LoaderCallbacks<List<News>> {

    //UI Elements
    private RecyclerView mRecyclerView;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageTextView;

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.latest_news);

        mRecyclerView = findViewById(R.id.recycler);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        mErrorMessageTextView = findViewById(R.id.tv_error_message);

        List<News> mList = new ArrayList<>();

        mAdapter = new NewsAdapter(mList, this);

        mRecyclerView.setAdapter(mAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        startLoading();

    }

    private void startLoading() {
        //Start loading
        if (isNetworkAvailable()) {
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            mLoadingIndicator.setVisibility(View.GONE);
            showError();
            mErrorMessageTextView.setText(R.string.error_internet_connection);
        }
    }

    //Checks for internet connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void showData() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setVisibility(View.GONE);
    }

    private void showError() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClickListener(News newsItem) {
        String webUrl = newsItem.getWebUrl();
        openNewsWebPage(webUrl);

    }

    private void openNewsWebPage(String url) {
        Uri webPage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webPage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @NonNull
    @Override
    public Loader<List<News>> onCreateLoader(int id, @Nullable Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<News>> loader, List<News> data) {
        mLoadingIndicator.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mAdapter = new NewsAdapter(data, this);
            mRecyclerView.setAdapter(mAdapter);
            showData();
        } else {
            showError();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<News>> loader) {

    }


}
