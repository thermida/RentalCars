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
        loadJSON();
        setupMaps();
        setScores();

        ArrayList<VehicleByPrice> listOfVehiclesByPrice = getListOfVehiclesByPrice();
        ArrayList<VehicleTypeBySIPP> listOfVehicleTypesBySIPP = getListOfVehiclesBySIPP();
        ArrayList<VehicleByRating> listOfVehiclesByRating = getListOfVehiclesByRating();
        ArrayList<VehicleByScore> listOfVehiclesByScore = getListOfVehiclesByScore();

        System.out.println("-- Vehicles by Price -- \n");
        printResults(listOfVehiclesByPrice);
        System.out.println("-- Vehicle Types By SIPP -- \n");
        printResults(listOfVehicleTypesBySIPP);
        System.out.println("-- Vehicle By Rating -- \n");
        printResults(listOfVehiclesByRating);
        System.out.println("-- Vehicle By Rating -- \n");
        printResults(listOfVehiclesByScore);

        // Spark API set up
        Gson gson = new Gson();
        Spark.get("/vehiclesByPrice", (req, res) -> gson.toJson(listOfVehiclesByPrice));
        Spark.get("/vehiclesBySipp", (req, res) -> gson.toJson(listOfVehicleTypesBySIPP));
        Spark.get("/vehiclesByRating", (req, res) -> gson.toJson(listOfVehiclesByRating));
        Spark.get("/vehiclesByScore", (req, res) -> gson.toJson(listOfVehiclesByScore));

    }



    public static ArrayList<VehicleByPrice> getListOfVehiclesByPrice(){
        // Sort a copy of the vehicle list and print it out

        ArrayList<Vehicle> vehiclesCopy = new ArrayList<>(vehicles.size());
        vehiclesCopy.addAll(vehicles);

        Collections.sort(vehiclesCopy, new PriceComparison());

        ArrayList<VehicleByPrice> vehicleByPrices = new ArrayList<>();
        for (int i = 0; i < vehiclesCopy.size(); i++) {
            Vehicle vehicle = vehiclesCopy.get(i);
            vehicleByPrices.add(new VehicleByPrice(vehicle.getName(), vehicle.getPrice()));
        }
        return vehicleByPrices;
    }

    public static ArrayList<VehicleTypeBySIPP> getListOfVehiclesBySIPP(){
        ArrayList<VehicleTypeBySIPP> results =  new ArrayList<>();
        for (int i = 0; i < vehicles.size(); i++){
            Vehicle vehicle = vehicles.get(i);
            VehicleTypeBySIPP vehicleType = getSpecForVehicle(vehicle.getName(), vehicle.getSipp());
            results.add(vehicleType);
        }
        return results;
    }

    public static VehicleTypeBySIPP getSpecForVehicle(String name, String sipp){
        // Get the letters from the sipp
        String carKey = sipp.substring(0,1);
        String doorsCarKey = sipp.substring(1,2);
        String transmissionKey = sipp.substring(2, 3);
        String fuelKey = sipp.substring(3, 4);

        // Find the values for each key
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

        String[] fuelAndACArray;
        fuelAndACArray = fuelAirConType.split("/");
        String fuel = fuelAndACArray[0];
        String airCon = fuelAndACArray[1];

        VehicleTypeBySIPP vehicle = new VehicleTypeBySIPP(name, sipp, carType, doorsCarType, transmissionType, fuel, airCon);

        return vehicle;
    }

    public static ArrayList<VehicleByRating> getListOfVehiclesByRating(){
        ArrayList<VehicleByRating> results =  new ArrayList<>();
        for(String key: carMap.keySet()) {
            ArrayList<Vehicle> vehicleList = getVehiclesForCarType(key);
            if (vehicleList.size() > 0) {
                Collections.sort(vehicleList, new RatingComparison());
                Vehicle topRankedVehicle = vehicleList.get((vehicleList.size() -1));
                VehicleByRating vehicle = new VehicleByRating(topRankedVehicle.getName(), carMap.get(key), topRankedVehicle.getSupplier(), topRankedVehicle.getRating());
                results.add(vehicle);
            }
        }
        return results;
    }

    public static ArrayList<Vehicle> getVehiclesForCarType(String carType){
        ArrayList<Vehicle> results = new ArrayList<>();

        for (int i = 0; i < vehicles.size(); i++){
            Vehicle vehicle = vehicles.get(i);
            String sipp = vehicle.getSipp();
            if (sipp.substring(0,1).equals(carType)) {
                results.add(vehicle);
            }
        }
        return results;
    }

    /**
     * Returns the vehicle list according to the combined score and rating
     * @return A list of ScoreRatingVehicles sorted by combined score and rating
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
     * Calculates the score for the vehicle based on the criteria in the document
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

    private static void printResults(ArrayList list){
        for (int i = 0; i < list.size() ; i++) {
            System.out.printf("%2d. %s\n", i+1, list.get(i).toString());
        }
        System.out.println("----------------------- \n");

    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

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

    /* Used for testing */
    public static ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }
    public static void setVehicles(ArrayList<Vehicle> list) {
        vehicles = list;
    }


}
