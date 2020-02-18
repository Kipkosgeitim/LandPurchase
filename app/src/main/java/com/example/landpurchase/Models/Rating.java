package com.example.landpurchase.Models;

public class Rating {
    private String userPhone; //both key and value
    private String landId;
    private String rateValue;
    private String comment;

    public Rating() {
    }

    public Rating(String userPhone, String landId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.landId = landId;
        this.rateValue = rateValue;
        this.comment = comment;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getLandId() {
        return landId;
    }

    public void setLandId(String landId) {
        this.landId = landId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
