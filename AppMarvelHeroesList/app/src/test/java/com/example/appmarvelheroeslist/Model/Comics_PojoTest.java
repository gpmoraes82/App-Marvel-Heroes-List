package com.example.appmarvelheroeslist.Model;

import org.junit.Test;

import static org.junit.Assert.*;

public class Comics_PojoTest {

    String inputComicTitle = "1";
    String inputComicImageUrl = "url";

    boolean expctedComicTitle = true;
    boolean expctedComicImageUrl = true;

    boolean output;

    Comics_Pojo comicsPojo = new Comics_Pojo( inputComicTitle, inputComicImageUrl);

    @Test
    public void getComicTitle() {

        output = comicsPojo.getComicTitle().getClass().equals(inputComicTitle.getClass());

        assertEquals(expctedComicTitle,output);

    }

    @Test
    public void getComicImageUrl() {

        output = comicsPojo.getComicTitle().getClass().equals(inputComicTitle.getClass());

        assertEquals(expctedComicImageUrl,output);

    }
}