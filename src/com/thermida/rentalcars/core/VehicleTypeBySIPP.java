package com.thermida.rentalcars.core;

/**
 * Created by thermida on 06/03/2016.
 */
public class VehicleTypeBySIPP {
    private String name;
    private String sipp;
    private String carType;
    private String doorsType;
    private String transmission;
    private String fuel;
    private String aircon;

    public VehicleTypeBySIPP(String name, String sipp, String carType, String doorsType, String transmission, String fuel, String ac) {
        this.name = name;
        this.sipp = sipp;
        this.carType = carType;
        this.doorsType = doorsType;
        this.transmission = transmission;
        this.fuel = fuel;
        this.aircon = ac;
    }

    public String getName(){ return name; }

    public String toString() {
        return name + " - " + sipp + " - " + carType + " - " + doorsType + " - " + transmission + " - " + fuel + " - " + aircon;
    }
}
