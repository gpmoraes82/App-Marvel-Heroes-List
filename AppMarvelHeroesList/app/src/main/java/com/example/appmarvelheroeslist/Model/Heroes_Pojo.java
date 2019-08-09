package com.example.appmarvelheroeslist.Model;

public class Heroes_Pojo {
    private String heroName;
    private String heroImageUrl;
    private String heroId;
    private String heroDescription;

    public Heroes_Pojo(String heroName, String heroImageUrl, String heroId, String heroDescription) {
        this.heroName = heroName;
        this.heroImageUrl = heroImageUrl;
        this.heroId = heroId;
        this.heroDescription = heroDescription;
    }

    public String getHeroName() {
        return heroName;
    }

    public String getHeroImageUrl() {
        return heroImageUrl;
    }

    public String getHeroId() {
        return heroId;
    }

    public String getHeroDescription() { return heroDescription; }


}
