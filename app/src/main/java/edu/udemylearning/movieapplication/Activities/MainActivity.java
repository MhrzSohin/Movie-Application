package edu.udemylearning.movieapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import edu.udemylearning.movieapplication.Adapter.CategoryListAdapter;
import edu.udemylearning.movieapplication.Adapter.FilmListAdapter;
import edu.udemylearning.movieapplication.Adapter.SliderAdapter;
import edu.udemylearning.movieapplication.Domain.GenresItem;
import edu.udemylearning.movieapplication.Domain.ListFilm;
import edu.udemylearning.movieapplication.Domain.SliderItems;
import edu.udemylearning.movieapplication.R;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private RecyclerView.Adapter bestMoviesAdapter, categoryMoviesAdapter, upcomingMoviesAdapter;
    private RecyclerView bestMovies, categoryMovies, upcomingMovies;
    private RequestQueue mRequestQueue;
    private StringRequest mStringRequestQueue,mStringRequestQueue2,mStringRequestQueue3;
    private ProgressBar loading1,loading2,loading3;
    private ImageView profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        banners();
        sendRequestBestMovies();
        sendRequestCategoryMovies();
        sendRequestUpcomingMovies();
        profileBtn.setOnClickListener(V->showMenu());

    }
    void showMenu(){
        PopupMenu popupMenu = new PopupMenu(MainActivity.this,profileBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, SplashActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    private void sendRequestBestMovies(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading1.setVisibility(View.VISIBLE);
        mStringRequestQueue = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=1", response -> {
            Gson gson = new Gson();
            loading1.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response,ListFilm.class);
            bestMoviesAdapter = new FilmListAdapter(items);
            bestMovies.setAdapter(bestMoviesAdapter);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading1.setVisibility(View.GONE);
                Log.i("UI","onErrorResponse"+error.toString());
            }
        });
        mRequestQueue.add(mStringRequestQueue);
    }

    private void sendRequestCategoryMovies(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading2.setVisibility(View.VISIBLE);
        mStringRequestQueue2 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/genres", response -> {
            Gson gson = new Gson();
            loading2.setVisibility(View.GONE);
            ArrayList<GenresItem> categoryList = gson.fromJson(response, new TypeToken<ArrayList<GenresItem>>(){
            }.getType());
            categoryMoviesAdapter = new CategoryListAdapter(categoryList);
            categoryMovies.setAdapter(categoryMoviesAdapter);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading2.setVisibility(View.GONE);
                Log.i("UI","onErrorResponse"+error.toString());
            }
        });
        mRequestQueue.add(mStringRequestQueue2);
    }

    private void sendRequestUpcomingMovies(){
        mRequestQueue = Volley.newRequestQueue(this);
        loading3.setVisibility(View.VISIBLE);
        mStringRequestQueue3 = new StringRequest(Request.Method.GET, "https://moviesapi.ir/api/v1/movies?page=3", response -> {
            Gson gson = new Gson();
            loading3.setVisibility(View.GONE);
            ListFilm items = gson.fromJson(response, ListFilm.class);
            upcomingMoviesAdapter = new FilmListAdapter(items);
            upcomingMovies.setAdapter(upcomingMoviesAdapter);
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading3.setVisibility(View.GONE);
                Log.i("UI","onErrorResponse"+error.toString());
            }
        });
        mRequestQueue.add(mStringRequestQueue3);
    }
    private void initView(){
        viewPager2 = findViewById(R.id.viewpaggerSlider);
        bestMovies = findViewById(R.id.view1);
        bestMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        categoryMovies =findViewById(R.id.view2);
        categoryMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        upcomingMovies = findViewById(R.id.view3);
        upcomingMovies.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        loading1 = findViewById(R.id.progressBar1);
        loading2 = findViewById(R.id.progressBar2);
        loading3 = findViewById(R.id.progressBar3);
        profileBtn = findViewById(R.id.profileBtn);


    }

    private  void banners(){
        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.wide));
        sliderItems.add(new SliderItems(R.drawable.wide1));
        sliderItems.add(new SliderItems(R.drawable.wide3));

        //Customize viewpage2
//       Disables clipping to padding and children, allowing adjacent images to be partially visible.
//       Sets OffscreenPageLimit(3), ensuring up to three pages are preloaded on either side for smoother transitions.
//       Disables overscroll mode to prevent edge glow effects.
        viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        //Margin Transformer: Adds a 40-pixel margin between pages.
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        //Custom Scale Transformer: Scales each page vertically based on its position, creating a zoom-in effect on the focused image and a slight reduction for side images.
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r=1-Math.abs(position);
                page.setScaleY(0.85f+r*0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setCurrentItem(1);
        //This callback is triggered whenever a new page is selected.
        // It removes any scheduled (sliderRunnable) tasks to prevent unintended page advances.
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                //When a new page is selected, this line removes any previously scheduled tasks of sliderRunnable that are still in the queue of slideHandler.
                //It prevents multiple instances of sliderRunnable from being executed at once, which could cause the ViewPager2 to advance pages too quickly or unexpectedly.
            }
        });
    }
//onPause: Removes sliderRunnable callbacks when the activity is paused.
//onResume: Re-posts sliderRunnable with a 2-second delay when the activity resumes, creating a sliding effect.
    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        slideHandler.postDelayed(sliderRunnable,2000);
    }

    //Purpose: Moves the ViewPager2 to the next item automatically.
    //Effect: The Runnable increments the current item, creating an automatic slide transition.
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

}