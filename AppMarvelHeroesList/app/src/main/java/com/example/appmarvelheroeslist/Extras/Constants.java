package com.example.appmarvelheroeslist.Extras;


//---- Class of constants used in the App ----
public class Constants {

    //---- API Keys ----
    public static final String PUBLIC_KEY = "Change Me";
    public static final String PRIVATE_KEY = "Change Me";

    //---- Url parts ----
    public static final String BASE_URL = "http://gateway.marvel.com";
    public static final String CHARACTERS = "/v1/public/characters";

    //---- Url parts ----
    public static String getHeroComics(String heroId) {

        return CHARACTERS + "/"+ heroId + "/comics";
    }
}


