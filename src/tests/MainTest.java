package tests;

import com.thermida.rentalcars.Main;
import com.thermida.rentalcars.core.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by thermida on 08/03/2016.
 */
public class MainTest {


    Vehicle vehicle1;
    Vehicle vehicle2;
    Vehicle vehicle3;
    Vehicle vehicle4;


    @Before
    public void setup(){
        Main.setupMaps();

        vehicle1 = new Vehicle("Vehicle 1", "IWMR", 111.11, "Ford", 9.5f);
        vehicle2 = new Vehicle("Vehicle 2", "CCAR", 222.22, "Audi", 7.8f);
        vehicle3 = new Vehicle("Vehicle 3", "MBMN", 123.45, "BMW", 8.6f);
        vehicle4 = new Vehicle("Vehicle 4", "CXMN", 100.00, "Hertz", 8.2f);

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
        vehicles.add(vehicle4);
        Main.setVehicles(vehicles);
    }

    @Test
    public void testGetListOfVehiclesByPrice() throws Exception {
        ArrayList<VehicleByPrice> results = Main.getListOfVehiclesByPrice();
        assertEquals(results.get(0).getName(), vehicle4.getName());
        assertEquals(results.get(1).getName(), vehicle1.getName());
        assertEquals(results.get(2).getName(), vehicle3.getName());
        assertEquals(results.get(3).getName(), vehicle2.getName());

    }

    @Test
    public void testGetSpecForVehicle() throws Exception {
        VehicleTypeBySIPP result = Main.getSpecForVehicle(vehicle1.getName(), vehicle1.getSipp());
        VehicleTypeBySIPP expected = new VehicleTypeBySIPP("Vehicle 1", "IWMR", "Intermediate", "Estate", "Manual", "Petrol", "AC");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetSpecForVehicleWithUnknown() throws Exception {
        vehicle1.setSipp("IXMR");
        VehicleTypeBySIPP result = Main.getSpecForVehicle(vehicle1.getName(), vehicle1.getSipp());
        VehicleTypeBySIPP expected = new VehicleTypeBySIPP("Vehicle 1", "IXMR", "Intermediate", "Unknown", "Manual", "Petrol", "AC");
        assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetListOfVehiclesBySIPP() throws Exception {
        ArrayList<VehicleTypeBySIPP> results = Main.getListOfVehiclesBySIPP();
        VehicleTypeBySIPP expected = new VehicleTypeBySIPP("Vehicle 1", "IWMR", "Intermediate", "Estate", "Manual", "Petrol", "AC");
        VehicleTypeBySIPP expected2 = new VehicleTypeBySIPP("Vehicle 2", "CCAR", "Compact", "4 doors", "Automatic", "Petrol", "AC");

        assertEquals(results.get(0).toString(), expected.toString());
        assertEquals(results.get(1).toString(), expected2.toString());

    }


    @Test
    public void testGetListOfVehiclesByRating() throws Exception {
        ArrayList<VehicleByRating> results = Main.getListOfVehiclesByRating();
        assertEquals(results.get(0).getName(), vehicle4.getName());
        assertEquals(results.get(1).getName(), vehicle1.getName());
        assertEquals(results.get(2).getName(), vehicle3.getName());
    }


    @Test
    public void testGetVehiclesForCarType() throws Exception {
        ArrayList<Vehicle> results = Main.getVehiclesForCarType("C");
        assertEquals(results.get(0).getName(), vehicle2.getName());
        assertEquals(results.get(1).getName(), vehicle4.getName());
    }

    @Test
    public void testSetScores() throws Exception {
        Main.setScores();
        ArrayList<Vehicle> results = Main.getVehicles();
        assertEquals(3, results.get(0).getScore(), 0);
        assertEquals(7, results.get(1).getScore(), 0);
        assertEquals(1, results.get(2).getScore(), 0);
        assertEquals(1, results.get(3).getScore(), 0);

    }

    @Test
    public void testGetListOfVehiclesByScore() throws Exception {
        Main.setScores();
        ArrayList<VehicleByScore> results = Main.getListOfVehiclesByScore();
        assertEquals(results.get(0).getName(), vehicle2.getName());
        assertEquals(results.get(1).getName(), vehicle1.getName());
        assertEquals(results.get(2).getName(), vehicle3.getName());
        assertEquals(results.get(3).getName(), vehicle4.getName());

    }

}