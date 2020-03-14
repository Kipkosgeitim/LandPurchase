package com.example.landpurchases.Models;

public class Banner {
    private String id,landNameLocation,landImage;

    public Banner() {
    }

    public Banner(String id, String landNameLocation, String landImage) {
        this.id = id;
        this.landNameLocation = landNameLocation;
        this.landImage = landImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLandNameLocation() {
        return landNameLocation;
    }

    public void setLandNameLocation(String landNameLocation) {
        this.landNameLocation = landNameLocation;
    }

    public String getLandImage() {
        return landImage;
    }

    public void setLandImage(String landImage) {
        this.landImage = landImage;
    }
}
