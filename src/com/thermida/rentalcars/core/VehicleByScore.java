package com.thermida.rentalcars.core;

/**
 * Created by thermida on 06/03/2016.
 */
public class VehicleByScore {

    private String name;
    private double score;
    private double rating;
    private double sum;

    public VehicleByScore (String name, double score, double rating) {
        this.name = name;
        this.score = score;
        this.rating = rating;
        this.sum = score + rating;
    }

    public String getName(){ return name; }


    @Override
    public String toString() {
        return name + " - " + score + " - " + rating + " - " + sum;
    }

}
