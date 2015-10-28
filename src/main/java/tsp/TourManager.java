package main.java.tsp;

import main.java.Vertex;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hung
 */
public class TourManager {

    // Holds our cities
    private static List<Vertex> destinationCities = new ArrayList<Vertex>();

    // Adds a destination city
    public static void addCity(Vertex city) {
        destinationCities.add(city);
    }

    // Get a city
    public static Vertex getCity(int index){
        return destinationCities.get(index);
    }

    // Get the number of destination cities
    public static int numberOfCities(){
        return destinationCities.size();
    }

    public static void clearCities() {
        destinationCities.clear();
    }

    public static String toString2() {
        return destinationCities.toString();
    }
}
