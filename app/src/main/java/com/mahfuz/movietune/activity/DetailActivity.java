package com.mahfuz.movietune.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mahfuz.movietune.R;
import com.mahfuz.movietune.model.SpecificData;
import com.mahfuz.movietune.adapter.RecyclerAdapter;
import com.mahfuz.movietune.apiRelated.ApiInterface;
import com.mahfuz.movietune.model.ApiResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    ImageView mImageView;
    TextView mTitleView,
            mGenresView,
            mVoteAvgView,
            mPopularityView,
            mDescriptionView,
            mProductionCompany,
            mProductionCountry,
            mBudgetView,
            mLanguage;
    RecyclerView mRecyclerView;

    List<String> genresList = new ArrayList<>();
    List<String> id = new ArrayList<>();
    List<String> poster_path = new ArrayList<>();

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        iniView();
        hideVisibility();
        progressBar.setVisibility(View.VISIBLE);
        String movieId = getIntent().getStringExtra("id");
        Log.d(MainActivity.sTAG, "onCreate: " + movieId);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.sBASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<SpecificData> call = apiInterface.getSpecificData(movieId);
        call.enqueue(new Callback<SpecificData>() {
            @Override
            public void onResponse(Call<SpecificData> call, Response<SpecificData> response) {
                showVisibility();
                progressBar.setVisibility(View.GONE);
                SpecificData result = response.body();
                StringBuilder genres = new StringBuilder();
                String imagePath = "http://image.tmdb.org/t/p/w500/"
                        + result.getBackdrop_path()
                        + "?api_key=" + MainActivity.sAPI_KEY;

                if (result.getGenres().size() < 3) {
                    for (int i = 0; i < result.getGenres().size(); i++) {
                        genres.append(result.getGenres().get(i).getName() + ",");
                        //Log.d(MainActivity.sTAG, "onResponse: Genres: "+genres);
                    }
                } else {
                    for (int i = 0; i < 3; i++) {
                        genres.append(result.getGenres().get(i).getName() + ",");
                        //Log.d(MainActivity.sTAG, "onResponse: Genres: "+genres);
                    }
                }
                try {
                    double popularity = result.getPopularity();
                    Picasso.with(getApplicationContext())
                            .load(imagePath).into(mImageView);
                    mDescriptionView.setText(result.getOverview());
                    mPopularityView.setText("" + Math.ceil(popularity) + "%");
                    mTitleView.setText(result.getOriginal_title());
                    mLanguage.setText(result.getSpoken_languages().get(0).getName());
                    mGenresView.setText(genres);
                    mBudgetView.setText("$" + result.getBudget());
                    mVoteAvgView.setText("" + Math.ceil(result.getVote_average()));
                    mProductionCompany.setText(result.getProduction_companies().get(0).getName());
                    if (result.getProduction_countries().get(0).getName().equals("United States of America")) {
                        mProductionCountry.setText("USA");
                    } else {
                        mProductionCountry.setText(result.getProduction_countries().get(0).getName());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<SpecificData> call, Throwable t) {

            }
        });

        Call<ApiResponse> call2 = apiInterface.getSimilarMovieData(movieId);
        call2.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.body();
                for (int i = 0; i < apiResponse.getResult().size(); i++) {
                    String list_id = apiResponse.getResult().get(i).getId();
                    String path = "http://image.tmdb.org/t/p/w500/"
                            + apiResponse.getResult().get(i).getPoster_path()
                            + "?api_key=" + MainActivity.sAPI_KEY;
                    id.add(list_id);
                    poster_path.add(path);
                    Log.d(MainActivity.sTAG, "onResponse: " + list_id);
                }
                mRecyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), id, poster_path, false));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL, false));

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(MainActivity.sTAG, "onFailure: ");
            }
        });
    }

    private void hideVisibility() {
        mImageView.setVisibility(View.INVISIBLE);
        mTitleView.setVisibility(View.INVISIBLE);
        mGenresView.setVisibility(View.INVISIBLE);
        mVoteAvgView.setVisibility(View.INVISIBLE);
        mPopularityView.setVisibility(View.INVISIBLE);
        mDescriptionView.setVisibility(View.INVISIBLE);
        mProductionCompany.setVisibility(View.INVISIBLE);
        mProductionCountry.setVisibility(View.INVISIBLE);
        mBudgetView.setVisibility(View.INVISIBLE);
        mLanguage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

    }

    public void showVisibility() {
        mImageView.setVisibility(View.VISIBLE);
        mTitleView.setVisibility(View.VISIBLE);
        mGenresView.setVisibility(View.VISIBLE);
        mVoteAvgView.setVisibility(View.VISIBLE);
        mPopularityView.setVisibility(View.VISIBLE);
        mDescriptionView.setVisibility(View.VISIBLE);
        mProductionCompany.setVisibility(View.VISIBLE);
        mProductionCountry.setVisibility(View.VISIBLE);
        mBudgetView.setVisibility(View.VISIBLE);
        mLanguage.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void iniView() {

        mImageView = findViewById(R.id.mainImage);
        mTitleView = findViewById(R.id.title);
        mGenresView = findViewById(R.id.genres);
        mVoteAvgView = findViewById(R.id.voteAvg);
        mPopularityView = findViewById(R.id.popularity);
        mDescriptionView = findViewById(R.id.description);
        mProductionCompany = findViewById(R.id.productionCompany);
        mProductionCountry = findViewById(R.id.productionCountry);
        mBudgetView = findViewById(R.id.budget);
        mLanguage = findViewById(R.id.language);
        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.loadingBar);
        mDescriptionView.setMovementMethod(new ScrollingMovementMethod());
    }
}
