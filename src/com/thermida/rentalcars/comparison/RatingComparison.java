package com.thermida.rentalcars.comparison;

import com.thermida.rentalcars.core.Vehicle;

import java.util.Comparator;

/**
 * Created by thermida on 06/03/2016.
 */
public class RatingComparison implements Comparator<Vehicle> {

    @Override
    public int compare(Vehicle car_1, Vehicle car_2) {
        double car_1_rating = car_1.getRating();
        double car_2_rating = car_2.getRating();

        if (car_1_rating > car_2_rating)
            return -1;
        else if (car_1_rating == car_2_rating)
            return 0;
        else if (car_1_rating < car_2_rating)
            return 1;
        return 0;
    }
}
