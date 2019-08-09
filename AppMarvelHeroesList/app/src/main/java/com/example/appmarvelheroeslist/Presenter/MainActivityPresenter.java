package com.example.appmarvelheroeslist.Presenter;

import com.example.appmarvelheroeslist.MVP.MainActivity;
import com.example.appmarvelheroeslist.Model.Heroes_Pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


//---- PRESENTER CONTRACT

//---- Instantiates a new MainActivity Presenter

public class MainActivityPresenter {

    //---- Attributes declaration
    private ArrayList<Heroes_Pojo> heroesArrayList;
    private MainActivity mainActivityView;


    //---- Instantiates a new MainActivity Presenter
    public MainActivityPresenter(MainActivity mainActivityView) {
        this.mainActivityView = mainActivityView;
        this.heroesArrayList = new ArrayList<>();
    }

    //---- Parses and get Api requested values
    public void heroesJsonParser(JSONObject response, int pageIndex) {

        try {

            //---- Enforce the api coding to get clear data and possible errors to be handled
            String code = response.getString("code");
            if (code.equals("200")) {

                if (pageIndex == 0) {
                    heroesArrayList.clear();
                }

                JSONObject dataObj = response.getJSONObject("data");
                JSONArray resultsArray = dataObj.getJSONArray("results");

                //---- Parses response data to to be added on Heroes ArraysList
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject results = resultsArray.getJSONObject(i);

                    String heroName = results.getString("name");
                    String heroId = results.getString("id");
                    String heroDescription = results.getString("description");
                    if (heroDescription.isEmpty()) {
                        heroDescription = "Top Secret or N/A";
                    }

                    //---- Construct the url to get thumb data
                    JSONObject thumbnailObj = results.getJSONObject("thumbnail");
                    String heroImageUrl = thumbnailObj.getString("path") + "." + thumbnailObj.getString("extension");

                    heroesArrayList.add(new Heroes_Pojo(heroName, heroImageUrl, heroId, heroDescription));

                }

                // ---- Put the date on the view ----
                mainActivityView.updateData(heroesArrayList);

            }


        } catch (JSONException e) {

            e.printStackTrace();

        }
    }
}