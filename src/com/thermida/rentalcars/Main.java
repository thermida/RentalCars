package com.thermida.rentalcars;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import com.thermida.rentalcars.core.*;
import com.thermida.rentalcars.comparison.*;

import org.json.*;
import com.google.gson.*;
import spark.Spark;



public class Main {

    private static String path = "http://www.rentalcars.com/js/vehicles.json";
    private static ArrayList<Vehicle> vehicles = new ArrayList<>();
    private static HashMap<String, String> carMap = new HashMap<>();
    private static HashMap<String, String> doorsCarMap = new HashMap<>();
    private static HashMap<String, String> transmissionMap = new HashMap<>();
    private static HashMap<String, String> fuelAirConMap = new HashMap<>();

    public static void main(String[] args) throws IOException, JSONException {
        //Initialise
        loadJSON();
        setupMaps();
        setScores();
        Gson gson = new Gson();

        // Run the Tasks for part 1
        ArrayList<VehicleByPrice> listOfVehiclesByPrice = getListOfVehiclesByPrice();
        ArrayList<VehicleTypeBySIPP> listOfVehicleTypesBySIPP = getListOfVehiclesBySIPP();
        ArrayList<VehicleByRating> listOfVehiclesByRating = getListOfVehiclesByRating();
        ArrayList<VehicleByScore> listOfVehiclesByScore = getListOfVehiclesByScore();

        // Print out the results
        System.out.println("-- Vehicles by Price -- \n");
        printResults(listOfVehiclesByPrice);
        System.out.println("-- Vehicle Types By SIPP -- \n");
        printResults(listOfVehicleTypesBySIPP);
        System.out.println("-- Vehicle By Rating -- \n");
        printResults(listOfVehiclesByRating);
        System.out.println("-- Vehicle By Rating -- \n");
        printResults(listOfVehiclesByScore);


        // Spark API set up
        // Rest API the results
        Spark.get("/vehiclesByPrice", (req, res) -> gson.toJson(listOfVehiclesByPrice));
        Spark.get("/vehiclesBySipp", (req, res) -> gson.toJson(listOfVehicleTypesBySIPP));
        Spark.get("/vehiclesByRating", (req, res) -> gson.toJson(listOfVehiclesByRating));
        Spark.get("/vehiclesByScore", (req, res) -> gson.toJson(listOfVehiclesByScore));

    }

    /**
     * Sorts the vehicles by price and returns the result
     * @return A list of VehicleByPrice sorted by price in ascending order
     */
    public static ArrayList<VehicleByPrice> getListOfVehiclesByPrice(){

        // Make a copy of the current vehicles
        ArrayList<Vehicle> vehiclesCopy = new ArrayList<>(vehicles.size());
        vehiclesCopy.addAll(vehicles);

        // Sort out the list copy in order of price
        Collections.sort(vehiclesCopy, new PriceComparison());

        // For each vehicle make a VehicleByPrice in the order of price
        ArrayList<VehicleByPrice> vehicleByPrices = new ArrayList<>();
        for (int i = 0; i < vehiclesCopy.size(); i++) {
            Vehicle vehicle = vehiclesCopy.get(i);
            vehicleByPrices.add(new VehicleByPrice(vehicle.getName(), vehicle.getPrice()));
        }
        return vehicleByPrices;
    }

    /**
     * Returns a list of vehicles types based on their sipp
     * @return A list of VehicleTypeBySIPP
     */
    public static ArrayList<VehicleTypeBySIPP> getListOfVehiclesBySIPP(){
        // Get the spec for each vehicle in the list
        ArrayList<VehicleTypeBySIPP> results =  new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++){
            Vehicle vehicle = vehicles.get(i);
            VehicleTypeBySIPP vehicleType = getSpecForVehicle(vehicle.getName(), vehicle.getSipp());
            results.add(vehicleType);
        }
        return results;
    }

    /**
     * Returns a new VehicleTypeBySIPP based on the sipp
     * @param name The vehicles name
     * @param sipp The sipp of the vehicle
     * @return The VehicleTypeBySIPP for that vehicle
     */
    public static VehicleTypeBySIPP getSpecForVehicle(String name, String sipp){
        // Get the letters from the sipp
        String carKey = sipp.substring(0,1);
        String doorsCarKey = sipp.substring(1,2);
        String transmissionKey = sipp.substring(2, 3);
        String fuelKey = sipp.substring(3, 4);

        // Find the values for each key
        // if key has no values then return unknown
        String carType = carMap.get(carKey);
        if (carType == null)
            carType = "Unknown";

        String doorsCarType = doorsCarMap.get(doorsCarKey);
        if (doorsCarType == null)
            doorsCarType = "Unknown";

        String transmissionType = transmissionMap.get(transmissionKey);
        if (transmissionType == null)
            transmissionType = "Unknown";

        String fuelAirConType = fuelAirConMap.get(fuelKey);
        if (fuelAirConType == null)
            fuelAirConType = "Unknown";

        // Split the fuel air con into two separate strings
        String[] fuelAndACArray;
        fuelAndACArray = fuelAirConType.split("/");

        String fuel = fuelAndACArray[0];
        String airCon = fuelAndACArray[1];

        // make new VechileTypeBySIPP
        VehicleTypeBySIPP vehicle = new VehicleTypeBySIPP(name, sipp, carType, doorsCarType, transmissionType, fuel, airCon);

        return vehicle;
    }

    /**
     * Returns a list of highest rated suppliers of a car type
     * @return A list of VehicleByRating sorted by rating
     */
    public static ArrayList<VehicleByRating> getListOfVehiclesByRating(){
        ArrayList<VehicleByRating> results =  new ArrayList<>();
        // For each car type
        for(String key: carMap.keySet()) {
            // Get the vehicles of that type in the list
            ArrayList<Vehicle> vehicleList = getVehiclesForCarType(key);
            // if there is cars of that type
            if (vehicleList.size() > 0) {
                // Sort the list according to rating and get the highest
                Collections.sort(vehicleList, new RatingComparison());
                Vehicle topRankedVehicle = vehicleList.get(0);
                VehicleByRating vehicle = new VehicleByRating(topRankedVehicle.getName(), carMap.get(key), topRankedVehicle.getSupplier(), topRankedVehicle.getRating());
                results.add(vehicle);
            }
        }
        return results;
    }

    /**
     * Returns a list of the vehicles for a certain car type
     * @param carType The type of vehicle to look for
     * @return a list of vehicles of that car type
     */
    public static ArrayList<Vehicle> getVehiclesForCarType(String carType){
        ArrayList<Vehicle> results = new ArrayList<>();
        // For each car type
        for (int i = 0; i < vehicles.size(); i++){
            Vehicle vehicle = vehicles.get(i);
            // Check if the sipp contains the right car type
            // if so add it to the list
            String sipp = vehicle.getSipp();
            if (sipp.substring(0,1).equals(carType)) {
                results.add(vehicle);
            }
        }
        return results;
    }

    /**
     * Returns the vehicle list according to the combined score and rating
     * @return A list of VehicleByScore sorted by combined score and rating
     */
    public static ArrayList<VehicleByScore> getListOfVehiclesByScore() {
        ArrayList<VehicleByScore> result = new ArrayList<>();

        // Get the score for each vehicle
        ArrayList<Vehicle> vehiclesCopy = new ArrayList<>(vehicles.size());
        vehiclesCopy.addAll(vehicles);

        // Sort by combined score & rating and print out the result
        Collections.sort(vehiclesCopy, new ScoreComparison());
        for (int i = 0; i < vehiclesCopy.size(); i++) {
            Vehicle vehicle = vehiclesCopy.get(i);
            VehicleByScore vehicleWithScore = new VehicleByScore(vehicle.getName(), vehicle.getScore(), vehicle.getRating());
            result.add(vehicleWithScore);
        }
        return result;
    }

    public static void setScores(){
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle vehicle = vehicles.get(i);
            int score = getScoreForVehicle(vehicle.getSipp());
            vehicle.setScore(score);
        }
    }

    /**
     * Calculates the score for the vehicle
     * @param sipp The sipp for the vehicle
     * @return The vehicle's score
     */
    private static int getScoreForVehicle(String sipp) {
        String transmission = sipp.substring(2,3);
        String fuelAC = sipp.substring(3, 4);
        int total = 0;

        if (transmission.equals("M"))
            total += 1;
        else if (transmission.equals("A"))
            total += 5;

        if (fuelAC.equals("R"))
            total += 2;

        return total;
    }


    /**
     * Reads the file from path and parses the file into the vehicles list
     */
    private static void loadJSON() throws IOException, JSONException{
        JSONObject json = readJsonFromUrl(path);
        JSONArray  jsonVehicles= json.getJSONObject("Search").getJSONArray("VehicleList");

        // Create a new Vehicle object for each entry in the json list and add it to an array list
        for (int i = 0; i < jsonVehicles.length(); i++) {
            JSONObject jsonVehicle = jsonVehicles.getJSONObject(i);
            double rating = jsonVehicle.getDouble("rating");
            Vehicle vehicle = new Vehicle(
                    jsonVehicle.getString("name"),
                    jsonVehicle.getString("sipp"),
                    jsonVehicle.getDouble("price"),
                    jsonVehicle.getString("supplier"),
                    jsonVehicle.getDouble("rating")
            );

            vehicles.add(vehicle);
        }
    }

    /**
     * Prints the results of an Array List of vehicles
     * @param list The array list to print
     */
    private static void printResults(ArrayList list){
        for (int i = 0; i < list.size() ; i++) {
            System.out.printf("%2d. %s\n", i+1, list.get(i).toString());
        }
        System.out.println("----------------------- \n");

    }


    /**
     * Reads all the buffer reader and converts it to a string
     * @param rd the reading file
     * @return a string of the file
     */
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    /**
     * reads a json from a url
     * @param url
     * @return the json object from the url
     * @throws IOException
     * @throws JSONException
     */
    private static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    /**
     * Creates the maps for the SIPP
     */
    public static void setupMaps(){
        // Car type map
        carMap.put("M", "Mini");
        carMap.put("E", "Economy");
        carMap.put("C", "Compact");
        carMap.put("I", "Intermediate");
        carMap.put("S", "Standard");
        carMap.put("F", "Full size");
        carMap.put("P", "Premium");
        carMap.put("L", "Luxury");
        carMap.put("X", "Special");

        // Doors/car type map
        doorsCarMap.put("B", "2 doors");
        doorsCarMap.put("C", "4 doors");
        doorsCarMap.put("D", "5 doors");
        doorsCarMap.put("W", "Estate");
        doorsCarMap.put("T", "Convertible");
        doorsCarMap.put("F", "SUV");
        doorsCarMap.put("P", "Pick up");
        doorsCarMap.put("V", "Passenger Van");

        // Transmission map
        transmissionMap.put("M", "Manual");
        transmissionMap.put("A", "Automatic");

        // Fuel/Air con map
        fuelAirConMap.put("N", "Petrol/no AC");
        fuelAirConMap.put("R", "Petrol/AC");
    }


    public static ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
    public static void setVehicles(ArrayList<Vehicle> list) {
        vehicles = list;
    }


}
