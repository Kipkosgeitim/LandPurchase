package com.example.landpurchase.Models;

public class LandOrder {
    private String UserPhone,LandOrderId,LandNameLocation,SizeOfLand,Price,LandTitle,Image;

    public LandOrder() {
    }

    public LandOrder(String userPhone, String landOrderId, String landNameLocation, String sizeOfLand, String price, String landTitle, String image) {
        UserPhone = userPhone;
        LandOrderId = landOrderId;
        LandNameLocation = landNameLocation;
        SizeOfLand = sizeOfLand;
        Price = price;
        LandTitle = landTitle;
        Image = image;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getLandOrderId() {
        return LandOrderId;
    }

    public void setLandOrderId(String landOrderId) {
        LandOrderId = landOrderId;
    }

    public String getLandNameLocation() {
        return LandNameLocation;
    }

    public void setLandNameLocation(String landNameLocation) {
        LandNameLocation = landNameLocation;
    }

    public String getSizeOfLand() {
        return SizeOfLand;
    }

    public void setSizeOfLand(String sizeOfLand) {
        SizeOfLand = sizeOfLand;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getLandTitle() {
        return LandTitle;
    }

    public void setLandTitle(String landTitle) {
        LandTitle = landTitle;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
