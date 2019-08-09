package com.example.appmarvelheroeslist.MVP;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.appmarvelheroeslist.Adapter.ComicsAdapter;
import com.example.appmarvelheroeslist.Extras.Constants;
import com.example.appmarvelheroeslist.Extras.Network.VolleySingleton;
import com.example.appmarvelheroeslist.Extras.Tools;
import com.example.appmarvelheroeslist.Model.Comics_Pojo;
import com.example.appmarvelheroeslist.Presenter.HeroDetailActivityPresenter;
import com.example.appmarvelheroeslist.R;
import com.example.appmarvelheroeslist.View.HeroDetailActivityView;

import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.appmarvelheroeslist.MVP.MainActivity.EXTRA_heroId;
import static com.example.appmarvelheroeslist.MVP.MainActivity.EXTRA_heroName;



//---- HeroDetailActivity will handle the comics list from a character ----
public class HeroDetailActivity extends AppCompatActivity implements HeroDetailActivityView {

    // ---- Atributes declaration ----
    private String heroName;
    private String heroId;

    private ComicsAdapter comicsAdapter;
    private ArrayList<Comics_Pojo> comicsPojoArrayList;
    private RecyclerView comicsRecyclerView;


    private HeroDetailActivityPresenter heroDetailActivityPresenter;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading = false;
    private int pageIndex = 0;
    private final int offset = 20; // Offset sets the size of the page request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);

        //---- Sets the received date to class attributes ----
        Intent intent = getIntent();
        heroName = intent.getStringExtra(EXTRA_heroName);
        heroId = intent.getStringExtra(EXTRA_heroId);

        //---- Preper and sets the toolbar ----
        Toolbar toolbar = findViewById(R.id.toolbar_hero_detail);
        toolbar.setSubtitle("Comics that appers:");
        toolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(heroName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //---- Presenter initialization ----
        heroDetailActivityPresenter = new HeroDetailActivityPresenter(HeroDetailActivity.this);

        //---- Progress bar initialization ----
        progressBar = findViewById(R.id.progressBar_heroes_detail);

        //---- Heroes Recycler initialization ----
        comicsRecyclerView = findViewById(R.id.recycler_hero_comics);

        comicsPojoArrayList = new ArrayList<>();

        comicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        comicsPojoArrayList = new ArrayList<>();

        //---- Get timestamp for Url construction ----
        String timestamp = Tools.getTimestamp();
        //---- Set md5 for timestamp and add the keys, private and public
        String hash = Tools.md5(timestamp + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY);
        String url = Constants.BASE_URL + Constants.getHeroComics(heroId) + "?ts=" + timestamp + "&apikey=" + Constants.PUBLIC_KEY + "&hash=" + hash;

        //---- Get the data of request for paging ----
        getComicData(url);

        //---- Set up the infinite scroll lister for the adapter ----
        comicsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        comicsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                        isLoading = true;

                        pageIndex += offset;

                        //---- Get timestamp for Url construction -----------
                        String timestamp = Tools.getTimestamp();
                        //---- Set md5 for timestamp and add the keys, private and public
                        String hash = Tools.md5(timestamp + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY);

                        String url = Constants.BASE_URL + Constants.getHeroComics(heroId) + "?ts=" + timestamp + "&apikey=" + Constants.PUBLIC_KEY + "&hash=" + hash + "&offset=" + pageIndex;

                        getComicData(url);
                    }
                }
            }
        });

    }

    //---- Method to set up paging for the API requests ----
    private void getComicData(String url) {
        progressBar.setVisibility(View.VISIBLE);

        final int pageNumber = pageIndex;

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() >= 1) {
                            heroDetailActivityPresenter.comicsJsonParser(response, pageIndex);

                        } else {
                            if (pageNumber != 0) {
                                pageIndex = pageNumber - 1;
                            }
                        }

                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        /*
         * The singleton here help to consist the date on response, if the response don't  came in 30 secunds, is restart
         * and didn't overload the server.
         */
        VolleySingleton.getInstance(HeroDetailActivity.this).addToRequestQueue(mJsonObjectRequest);
    }


    //---- Method to notify the adapter after having changes in RecyclerView ----
    @Override
    public void updateData(ArrayList<Comics_Pojo> comicsPojoArrayList) {

        this.comicsPojoArrayList = comicsPojoArrayList;

        if (pageIndex == 0) {
            comicsAdapter = new ComicsAdapter(HeroDetailActivity.this, this.comicsPojoArrayList);

            comicsRecyclerView.setAdapter(comicsAdapter);

            linearLayoutManager = new LinearLayoutManager(HeroDetailActivity.this);
            comicsRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            isLoading = false;
            if (comicsAdapter != null) {

                // notifyDataSetChanged is used to notify the adapter after having changes in recyclerView
                comicsAdapter.notifyDataSetChanged();
            }
        }
    }

}
