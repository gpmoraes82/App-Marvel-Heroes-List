package com.example.appmarvelheroeslist.MVP;

import android.content.Intent;
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
import com.example.appmarvelheroeslist.Adapter.HeroesAdapter;
import com.example.appmarvelheroeslist.Extras.Constants;
import com.example.appmarvelheroeslist.Extras.Network.VolleySingleton;
import com.example.appmarvelheroeslist.Extras.Tools;
import com.example.appmarvelheroeslist.Model.Heroes_Pojo;
import com.example.appmarvelheroeslist.Presenter.MainActivityPresenter;
import com.example.appmarvelheroeslist.R;
import com.example.appmarvelheroeslist.View.MainActivityView;

import org.json.JSONObject;

import java.util.ArrayList;

/*
*
* MainActivity will handle the main view, in order to
* do that it must implement the Heroes Adapter methods
* and the click interface to transmit the data to HeroDetailActivity
*
*/

public class MainActivity extends AppCompatActivity implements MainActivityView, HeroesAdapter.OnItemClickListener {

    // ---- Intent date receiver ----
    public static final String EXTRA_heroName = "HERO_NAME";
    public static final String EXTRA_heroId = "HERO_ID";

    // ---- Atributes declaration ----
    private HeroesAdapter heroesAdapter;
    private ArrayList<Heroes_Pojo> heroesPojoArrayList;
    private RecyclerView heroesRecyclerView;

    private MainActivityPresenter mainActivityPresenter;
    private ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;

    private boolean isLoading = false;
    private int pageIndex = 0;
    private final int offset = 20; // Offset sets the size of the page request

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //---- Preper and sets the toolbar ----
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainActivityPresenter = new MainActivityPresenter(MainActivity.this);

        //---- Progress bar initialization ----
        progressBar = findViewById(R.id.progressBar_heroes);

        //---- Heroes Recycler initialization ----
        heroesRecyclerView = findViewById(R.id.recycler_heroes);

        heroesPojoArrayList = new ArrayList<>();

        //---- Get timestamp for Url construction -----------
        String timestamp = Tools.getTimestamp();
        //---- Set md5 for timestamp and add the keys, private and public
        String hash = Tools.md5(timestamp + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY);
        String url = Constants.BASE_URL + Constants.CHARACTERS + "?ts=" + timestamp + "&apikey=" + Constants.PUBLIC_KEY + "&hash=" + hash;

        //---- Get the data of request for paging
        getHeroData(url);

        //---- Set up the infinite scroll lister for the adapter ----
        heroesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        heroesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                        String url = Constants.BASE_URL + Constants.CHARACTERS + "?ts=" + timestamp + "&apikey=" + Constants.PUBLIC_KEY + "&hash=" + hash + "&offset=" + pageIndex;

                        getHeroData(url);
                    }
                }
            }
        });


    }

    //---- Method to set up paging for the API requests ----
    private void getHeroData(String url) {
        progressBar.setVisibility(View.VISIBLE);

        final int pageNumber = pageIndex;

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response.length() >= 1) {
                            mainActivityPresenter.heroesJsonParser(response, pageIndex);

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
        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(mJsonObjectRequest);

    }


    //---- Method to notify the adapter after having changes in RecyclerView ----
    @Override
    public void updateData(ArrayList<Heroes_Pojo> heroesPojoArrayList) {

        this.heroesPojoArrayList = heroesPojoArrayList;

        if (pageIndex == 0) {
            heroesAdapter = new HeroesAdapter(MainActivity.this, this.heroesPojoArrayList, this);

            heroesRecyclerView.setAdapter(heroesAdapter);

            linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            heroesRecyclerView.setLayoutManager(linearLayoutManager);
        } else {
            isLoading = false;
            if (heroesAdapter != null) {
                heroesAdapter.notifyDataSetChanged();
            }
        }

    }

    //---- Method to handle and send data to HeroDetailActivity intent ----
    @Override
    public void onItemClick(int position) {

        Intent heroDetailIntent = new Intent(MainActivity.this, HeroDetailActivity.class);

        Heroes_Pojo clickedItem = heroesPojoArrayList.get(position);

        heroDetailIntent.putExtra(EXTRA_heroName, clickedItem.getHeroName());
        heroDetailIntent.putExtra(EXTRA_heroId, clickedItem.getHeroId());

        startActivity(heroDetailIntent);

    }

}
