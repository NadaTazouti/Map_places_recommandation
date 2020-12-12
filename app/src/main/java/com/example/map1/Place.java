package com.example.map1;

public class Place {

    private String name;
    private String longitude;
    private String latitude;

    public Place(String name, String longitude, String latitude) {

        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;

    }

    public String getName() {
        return name;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return "[ name : " + name + ", latitude : " + latitude + ", longitude : " + longitude + " ]";
    }

}