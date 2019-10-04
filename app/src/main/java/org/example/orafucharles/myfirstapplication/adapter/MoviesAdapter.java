package org.example.orafucharles.myfirstapplication.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.example.orafucharles.myfirstapplication.MovieDetails;
import org.example.orafucharles.myfirstapplication.R;
import org.example.orafucharles.myfirstapplication.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {
    private final Context mContext;
    private final List<Movie> movieList;
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";

    public MoviesAdapter(Context mContext, List<Movie> movieList){
        this.mContext = mContext;
        this.movieList = movieList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, int i){
        viewHolder.title.setText(movieList.get(i).getOriginalTitle());
        String vote = Double.toString(movieList.get(i).getVoteAverage());
        viewHolder.userRating.setText(vote);

        //load poster image with picasso
        Picasso.with(mContext)
                .load(movieList.get(i).getPosterPath())
                .placeholder(R.drawable.ic_loading)
                .error(R.drawable.ic_loading)
                .into(viewHolder.thumbnail);

    }

    @Override
    public int getItemCount(){
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title) TextView title;
        @BindView(R.id.user_rating) TextView userRating;
        @BindView(R.id.thumbnail) ImageView thumbnail;

        MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);

            //on item click
            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        Intent intent = new Intent(mContext, MovieDetails.class);
                        intent.putExtra(ORIGINAL_TITLE, movieList.get(pos).getOriginalTitle());
                        intent.putExtra(POSTER_PATH, movieList.get(pos).getPosterPath());
                        intent.putExtra(OVERVIEW, movieList.get(pos).getOverview());
                        intent.putExtra(VOTE_AVERAGE, Double.toString(movieList.get(pos).getVoteAverage()));
                        intent.putExtra(RELEASE_DATE, movieList.get(pos).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                    }
                }
            });
        }

    }


}
