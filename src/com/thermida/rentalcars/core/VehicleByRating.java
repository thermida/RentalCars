package com.thermida.rentalcars.core;

/**
 * Created by thermida on 06/03/2016.
 */
public class VehicleByRating {
    private String name;
    private String carType;
    private String supplier;
    private double rating;

    public VehicleByRating(String name, String carType, String supplier, double rating) {
        this.name = name;
        this.carType = carType;
        this.supplier = supplier;
        this.rating = rating;
    }

    public String getName(){ return name;}

    @Override
    public String toString() {
        return name + " - " + carType + " - " + supplier + " - " + rating;
    }
}

