package tests;

import com.thermida.rentalcars.core.Vehicle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by thermida on 08/03/2016.
 */
public class VehicleTest {

    Vehicle vehicle1;
    Vehicle vehicle2;
    Vehicle vehicle3;

    @Before
    public void setup(){
        vehicle1 = new Vehicle("Vehicle 1", "IWMR", 111.11f, "Ford", 9.5f);
        vehicle2 = new Vehicle("Vehicle 2", "CCAR", 222.22f, "Audi", 7.8f);
        vehicle3 = new Vehicle("Vehicle 3", "MBMN", 123.45f, "BMW", 8.6f);
    }

    @Test
    public void testConstructor1() throws Exception {
        String name = "Vehicle 1";
        String sipp = "IWMR";
        double price = 111.11f;
        String supplier = "Ford";
        double rating = 9.5f;
        String result = "{" +
                "name='" + name + "'" +
                ", sipp='" + sipp + "'" +
                ", price=" + price +
                ", supplier='" + supplier + "'" +
                ", rating=" + rating +
                "}";

        assertEquals(result, vehicle1.toString());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Vehicle 1", vehicle1.getName());
        assertEquals("Vehicle 2", vehicle2.getName());
        assertEquals("Vehicle 3", vehicle3.getName());

    }

    @Test
    public void testSetName() throws Exception {
        vehicle1.setName("Vehicle 1.1");
        assertEquals("Vehicle 1.1", vehicle1.getName());

    }

    @Test
    public void testGetSipp() throws Exception {
        assertEquals("IWMR", vehicle1.getSipp());
        assertEquals("CCAR", vehicle2.getSipp());
        assertEquals("MBMN", vehicle3.getSipp());
    }

    @Test
    public void testSetSipp() throws Exception {
        vehicle1.setSipp("MBMN");
        assertEquals("MBMN", vehicle1.getSipp());
    }

    @Test
    public void testGetPrice() throws Exception {
        assertEquals(111.11f, vehicle1.getPrice(), 0);
        assertEquals(222.22f, vehicle2.getPrice(), 0);
        assertEquals(123.45f, vehicle3.getPrice(), 0);
    }

    @Test
    public void testSetPrice() throws Exception {
        vehicle1.setPrice(543.21f);
        assertEquals(543.21f, vehicle1.getPrice(), 0);
    }

    @Test
    public void testGetSupplier() throws Exception {
        assertEquals("Ford", vehicle1.getSupplier());
        assertEquals("Audi", vehicle2.getSupplier());
        assertEquals("BMW", vehicle3.getSupplier());
    }

    @Test
    public void testSetSupplier() throws Exception {
        vehicle2.setSupplier("Hertz");
        assertEquals("Hertz", vehicle2.getSupplier());

    }

    @Test
    public void testGetRating() throws Exception {
        assertEquals(9.5f, vehicle1.getRating(), 0);
        assertEquals(7.8f, vehicle2.getRating(), 0);
        assertEquals(8.6f, vehicle3.getRating(), 0);
    }

    @Test
    public void testSetRating() throws Exception {
        vehicle3.setRating(9.9f);
        assertEquals(9.9f, vehicle3.getRating(), 0);

    }

}