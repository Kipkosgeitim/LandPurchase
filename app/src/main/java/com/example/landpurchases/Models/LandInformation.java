package com.example.landpurchases.Models;

public class LandInformation {
    private String landId,sellerPhone;
    private Double lat,lng;

    public LandInformation() {
    }

    public LandInformation(String landId, String sellerPhone, Double lat, Double lng) {
        this.landId = landId;
        this.sellerPhone = sellerPhone;
        this.lat = lat;
        this.lng = lng;
    }

    public String getLandId() {
        return landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    public String getSellerPhone() {
        return sellerPhone;
    }

    public void setSellerPhone(String sellerPhone) {
        this.sellerPhone = sellerPhone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
