package com.mahfuz.movietune.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.mahfuz.movietune.activity.MainActivity;
import com.mahfuz.movietune.R;
import com.mahfuz.movietune.adapter.RecyclerAdapter;
import com.mahfuz.movietune.apiRelated.ApiInterface;
import com.mahfuz.movietune.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class Upcoming extends Fragment {

    RecyclerView mRecyclerView;
    List<String> id = new ArrayList<>();
    List<String> poster_path = new ArrayList<>();
    ProgressBar progressBar;


    public Upcoming() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        iniView(view);

        Log.d(MainActivity.sTAG, "onCreateView: ");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.sBASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<ApiResponse> call = apiInterface.getUpcomingData();
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                progressBar.setVisibility(View.GONE);
                ApiResponse apiResponse = response.body();
                for (int i=0; i<apiResponse.getResult().size(); i++){
                    String list_id = apiResponse.getResult().get(i).getId();
                    String path = "http://image.tmdb.org/t/p/w500/"
                            +apiResponse.getResult().get(i).getPoster_path()
                            +"?api_key="+MainActivity.sAPI_KEY;
                    id.add(list_id);
                    poster_path.add(path);
                    Log.d(MainActivity.sTAG, "onResponse: "+list_id);
                }
                mRecyclerView.setAdapter(new RecyclerAdapter(getContext(),id,poster_path,true));
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(MainActivity.sTAG, "onFailure: ");
            }
        });

        return view;
    }

    private void iniView(View view) {

        mRecyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.loadingBar);
    }
}
