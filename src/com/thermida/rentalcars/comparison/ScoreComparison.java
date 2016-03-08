package com.thermida.rentalcars.comparison;

import com.thermida.rentalcars.core.Vehicle;

import java.util.Comparator;

/**
 * Created by thermida on 06/03/2016.
 */
public class ScoreComparison implements Comparator<Vehicle> {
    @Override
    public int compare(Vehicle car_1, Vehicle car_2) {
        double car_1_sum = car_1.getScore() + car_1.getRating();
        double car_2_sum = car_2.getScore() + car_2.getRating();

        if (car_1_sum > car_2_sum)
            return -1;
        else if (car_1_sum == car_2_sum)
            return 0;
        else if (car_1_sum < car_2_sum)
            return 1;
        return 0;
    }
}
