package com.example.rentatent;
public class RAT {
    private String address,available,city,description,name,link,mobile;
    private int price;
    private double latitude,longitude;

    public RAT(String address, String available, String city, String description, String name, String link,
               String mobile, int price, double latitude, double longitude)
    {
        this.address = address;
        this.available = available;
        this.city = city;
        this.description = description;
        this.name = name;
        this.link = link;
        this.mobile = mobile;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return this.mobile;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
