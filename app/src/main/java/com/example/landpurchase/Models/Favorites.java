package com.example.landpurchase.Models;

public class Favorites {
    private String Land,LandLocationName,LandPrice,LandId,LandImage,SizeOfLand,LandDescription,UserPhone;


    public Favorites() {
    }

    public Favorites(String land , String landLocationName , String landPrice , String landId , String landImage , String sizeOfLand , String landDescription , String userPhone) {
        Land = land;
        LandLocationName = landLocationName;
        LandPrice = landPrice;
        LandId = landId;
        LandImage = landImage;
        SizeOfLand = sizeOfLand;
        LandDescription = landDescription;
        UserPhone = userPhone;
    }

    public String getLand() {
        return Land;
    }

    public void setLand(String land) {
        Land = land;
    }

    public String getLandLocationName() {
        return LandLocationName;
    }

    public void setLandLocationName(String landLocationName) {
        LandLocationName = landLocationName;
    }

    public String getLandPrice() {
        return LandPrice;
    }

    public void setLandPrice(String landPrice) {
        LandPrice = landPrice;
    }

    public String getLandId() {
        return LandId;
    }

    public void setLandId(String landId) {
        LandId = landId;
    }

    public String getLandImage() {
        return LandImage;
    }

    public void setLandImage(String landImage) {
        LandImage = landImage;
    }

    public String getSizeOfLand() {
        return SizeOfLand;
    }

    public void setSizeOfLand(String sizeOfLand) {
        SizeOfLand = sizeOfLand;
    }

    public String getLandDescription() {
        return LandDescription;
    }

    public void setLandDescription(String landDescription) {
        LandDescription = landDescription;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }
}
