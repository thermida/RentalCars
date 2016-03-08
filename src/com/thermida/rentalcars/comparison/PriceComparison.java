package com.thermida.rentalcars.comparison;
import com.thermida.rentalcars.core.Vehicle;

import java.util.Comparator;

/**
 * Created by thermida on 05/03/2016.
 */
public class PriceComparison implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle car_1, Vehicle car_2){
        double car_1_price = car_1.getPrice();
        double car_2_price = car_2.getPrice();

        if (car_1_price > car_2_price)
            return 1;
        else if (car_1_price == car_2_price)
            return 0;
        else if (car_1_price < car_2_price)
            return -1;
        return 0;
    }
}
