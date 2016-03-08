package com.thermida.rentalcars.core;

/**
 * Created by thermida on 05/03/2016.
 */
public class VehicleByPrice {
    private String name;
    private double price;

    public VehicleByPrice(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName(){ return name; }

    public String toString() {return name + " - "+ price; }

}
