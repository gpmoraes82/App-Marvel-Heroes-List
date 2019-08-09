package com.example.appmarvelheroeslist.Presenter;

import com.example.appmarvelheroeslist.MVP.HeroDetailActivity;
import com.example.appmarvelheroeslist.Model.Comics_Pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


//---- PRESENTER CONTRACT

//---- Instantiates a new HeroDetailActivity Presenter

public class HeroDetailActivityPresenter {

    //---- Attributes declaration
    private ArrayList<Comics_Pojo> comicsPojoArrayList;
    private HeroDetailActivity heroDetailActivity;


    //---- Instantiates a new HeroDetailActivity Presenter
    public HeroDetailActivityPresenter(HeroDetailActivity heroDetailActivity) {
        this.heroDetailActivity = heroDetailActivity;
        this.comicsPojoArrayList = new ArrayList<>();
    }

    //---- Parses and get Api requested values
    public void comicsJsonParser(JSONObject response, int pageIndex) {

        try {

            //---- Enforce the api coding to get clear data and possible errors to be handled
            String code = response.getString("code");
            if (code.equals("200")) {

                if (pageIndex == 0) {
                    comicsPojoArrayList.clear();
                }

                JSONObject dataObj = response.getJSONObject("data");
                JSONArray resultsArray = dataObj.getJSONArray("results");

                //---- Parses response data to to be added on Comics ArraysList from an specific Character
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject results = resultsArray.getJSONObject(i);

                    String comicTitle = results.getString("title");

                    //---- Construct the url to get thumb data
                    JSONObject thumbnailObj = results.getJSONObject("thumbnail");
                    String comicImageUrl = thumbnailObj.getString("path") + "." + thumbnailObj.getString("extension");

                    comicsPojoArrayList.add(new Comics_Pojo(comicTitle, comicImageUrl));

                }

                // ---- Put the date on the view ----
                heroDetailActivity.updateData(comicsPojoArrayList);

            }


        } catch (JSONException e) {

            e.printStackTrace();

        }
    }
}