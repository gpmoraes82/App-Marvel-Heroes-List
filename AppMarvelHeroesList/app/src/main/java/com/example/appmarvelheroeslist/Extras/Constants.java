package com.example.appmarvelheroeslist.Extras;


//---- Class of constants used in the App ----
public class Constants {

    //---- API Keys ----
    public static final String PUBLIC_KEY = "3043d757764057aee3fd1a7b23153899";
    public static final String PRIVATE_KEY = "bcbb0f6a64d55948cc81d41b9124f2592f32de1f";

    //---- Url parts ----
    public static final String BASE_URL = "http://gateway.marvel.com";
    public static final String CHARACTERS = "/v1/public/characters";

    //---- Url parts ----
    public static String getHeroComics(String heroId) {

        return CHARACTERS + "/"+ heroId + "/comics";
    }
}


