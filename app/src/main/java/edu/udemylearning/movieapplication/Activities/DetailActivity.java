package edu.udemylearning.movieapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import edu.udemylearning.movieapplication.Adapter.ActorListAdapter;
import edu.udemylearning.movieapplication.Adapter.CategoryEachFilmAdapter;
import edu.udemylearning.movieapplication.Domain.FIlmItem;
import edu.udemylearning.movieapplication.R;

public class DetailActivity extends AppCompatActivity {
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private ProgressBar myLoading;
    private TextView movieTitle, rating, movieTime, actorInfo, summaryInfo;
    private int idFilm;
    private NestedScrollView myScrollView;
    private RecyclerView recyclerViewActors, recyclerViewCategory;
    private RecyclerView.Adapter adpaterActorList, adapterViewCategory;
    private ImageView backBtn, posterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        idFilm = getIntent().getIntExtra("id",0);
        initView();
        sendRequest();

    }
    private void sendRequest(){
        mRequestQueue = Volley.newRequestQueue(this);
        myLoading.setVisibility(View.VISIBLE);
        myScrollView.setVisibility(View.GONE);

        mStringRequest = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies/" + idFilm, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                myLoading.setVisibility(View.GONE);
                myScrollView.setVisibility(View.VISIBLE);

                FIlmItem item = gson.fromJson(response, FIlmItem.class);

                Glide.with(DetailActivity.this)
                        .load(item.getPoster())
                        .into(posterView);

                movieTitle.setText(item.getTitle());
                movieTime.setText(item.getRuntime());
                rating.setText(item.getImdbRating());
                summaryInfo.setText(item.getPlot());
                actorInfo.setText(item.getActors());
                if(item.getImages()!=null){
                    adpaterActorList = new ActorListAdapter(item.getImages());
                    recyclerViewActors.setAdapter(adpaterActorList);
                }
                if (item.getGenres() != null){
                    adapterViewCategory = new CategoryEachFilmAdapter(item.getGenres());
                    recyclerViewCategory.setAdapter(adapterViewCategory);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myLoading.setVisibility(View.GONE);
                Log.i("UI","Error Occures"+error.toString());
            }
        });
        mRequestQueue.add(mStringRequest);

    }

    private void initView() {
        posterView = findViewById(R.id.posterDetail);
        myLoading = findViewById(R.id.progressBarDetail);
        movieTime = findViewById(R.id.movieTime);
        movieTitle = findViewById(R.id.movieNameTxt);
        rating = findViewById(R.id.movieStar);
        actorInfo = findViewById(R.id.movieActorInfo);
        summaryInfo = findViewById(R.id.movieSummary);
        myScrollView = findViewById(R.id.scrollViewDetailActivity);
        backBtn = findViewById(R.id.backBtn);
        recyclerViewCategory = findViewById(R.id.genreView);
        recyclerViewActors = findViewById(R.id.imagesRecycler);
        recyclerViewActors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        backBtn.setOnClickListener(V->finish());

    }
}