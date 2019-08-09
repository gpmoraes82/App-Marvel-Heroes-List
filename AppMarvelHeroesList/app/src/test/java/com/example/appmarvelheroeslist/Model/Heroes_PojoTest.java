package com.example.appmarvelheroeslist.Model;

import org.junit.Test;

import java.lang.reflect.Type;

import static org.junit.Assert.*;

public class Heroes_PojoTest {

    String inputHeroName = "1";
    String inputHeroImageUrl = "url";
    String inputHeroId = "id";
    String inputHeroDescription = "1";

    boolean expctedHeroName = true;
    boolean expctedHeroImageUrl = true;
    boolean expctedHeroId = true;
    boolean expctedHeroDescription = true;

    boolean output;

    Heroes_Pojo heroesPojo = new Heroes_Pojo(
            inputHeroName,
            inputHeroImageUrl,
            inputHeroId,
            inputHeroDescription);

    @Test
    public void getHeroName() {

        output = heroesPojo.getHeroName().getClass().equals(inputHeroName.getClass());

        assertEquals(expctedHeroName,output);

    }

    @Test
    public void getHeroImageUrl() {

        output = heroesPojo.getHeroImageUrl().getClass().equals(inputHeroImageUrl.getClass());

        assertEquals(expctedHeroImageUrl,output);

    }

    @Test
    public void getHeroId() {

        output = heroesPojo.getHeroId().getClass().equals(inputHeroId.getClass());

        assertEquals(expctedHeroId,output);

    }

    @Test
    public void getHeroDescription() {

        output = heroesPojo.getHeroDescription().getClass().equals(inputHeroDescription.getClass());

        assertEquals(expctedHeroDescription,output);

    }
}