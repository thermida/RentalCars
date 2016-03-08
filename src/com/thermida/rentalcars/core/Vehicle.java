package com.thermida.rentalcars.core;

/**
 * Created by thermida on 05/03/2016.
 */
public class Vehicle implements Cloneable{

    String name;
    private String sipp;
    private String supplier;
    private double price;
    private double rating;
    private int score;

    public Vehicle(String name, String sipp, double price, String supplier, double rating){
        this.name = name;
        this.sipp = sipp;
        this.price = price;
        this.supplier = supplier;
        this.rating = rating;
    }

    // Getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSipp() {
        return sipp;
    }

    public void setSipp(String sipp) {
        this.sipp = sipp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String toString() {
        return  "{" +
                "name='" + name + "'" +
                ", sipp='" + sipp + "'" +
                ", price=" + price +
                ", supplier='" + supplier + "'" +
                ", rating=" + rating +
                "}";
    }
}
