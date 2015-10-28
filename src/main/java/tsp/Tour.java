package main.java.tsp;

/*
* Tour.java
* Stores a candidate tour
*/

import main.java.ACO;
import main.java.Vertex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Tour{

    // Holds our tour of cities
    private List<Vertex> tour = new ArrayList<Vertex>();
    // Cache
    private double fitness = 0;
    private int distance = 0;

    // Constructs a blank tour
    public Tour(){
        for (int i = 0; i < TourManager.numberOfCities(); i++) {
            tour.add(null);
        }
    }

    public Tour(List tour){
        this.tour = tour;
    }

    public Tour tourWithoutFirstLast() {
        List<Vertex> tourWithout = new LinkedList<>(tour);
//        ((LinkedList)tourWithout).removeFirst();
//        ((LinkedList)tourWithout).removeLast();
        return new Tour(tourWithout);
    }

    public List<Vertex> tourWithFirstLast() {
        List<Vertex> tourWithout = new LinkedList<>(tour);
        ((LinkedList)tourWithout).addFirst(ACO.maze.getVertex(ACO.startingCoordinate));
        ((LinkedList)tourWithout).addLast(ACO.maze.getVertex(ACO.goalCoordinate));
        return tourWithout;
    }

    // Creates a random individual
    public void generateIndividual() {
        // Loop through all our destination cities and add them to our tour
        for (int cityIndex = 0; cityIndex < TourManager.numberOfCities(); cityIndex++) {
            setCity(cityIndex, TourManager.getCity(cityIndex));
        }

        // Randomly reorder the tour
        Collections.shuffle(tour);
    }

    // Gets a city from the tour
    public Vertex getCity(int tourPosition) {
        return (Vertex)tour.get(tourPosition);
    }

    // Gets a city from the tour
    public Vertex getLastCity() {
        return (tour).get(tour.size()-1);
    }

    // Sets a city in a certain position within a tour
    public void setCity(int tourPosition, Vertex city) {
        tour.set(tourPosition, city);
        // If the tours been altered we need to reset the fitness and distance
        fitness = 0;
        distance = 0;
    }

    // Gets the tours fitness
    public double getFitness() {
        if (fitness == 0) {
            fitness = 1/(double)getDistance();
        }
        return fitness;
    }

    // Gets the total distance of the tour
    public int getDistance(){
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < tourSize() - 1; cityIndex++) {
                // Get city we're travelling from
                Vertex fromCity = getCity(cityIndex);
                // City we're travelling to
                Vertex destinationCity;
                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
//                if(cityIndex+1 < tourSize()){
                    destinationCity = getCity(cityIndex+1);
//                }
//                else{
//                    destinationCity = getCity(0);
//                }
                // Get the distance between the two cities
                tourDistance += fromCity.getLinkedVertexLength(destinationCity);
            }
            int distanceStart = ACO.maze.getVertex(ACO.startingCoordinate).getLinkedVertexLength(getCity(0));
            int distanceEnd = getLastCity().getLinkedVertexLength(ACO.maze.getVertex(ACO.goalCoordinate));
            tourDistance = tourDistance+distanceStart+distanceEnd;
            distance = tourDistance;
        }
        return distance;
    }

    // Gets the total distance of the tour
    public int getDistance2(){
//        Tour t2 = tourWithoutFirstLast(); //tourWithFirstLast();
        List<Vertex> t2 = tourWithFirstLast();
        if (distance == 0) {
            int tourDistance = 0;
            // Loop through our tour's cities
            for (int cityIndex=0; cityIndex < t2.size(); cityIndex++) {
                // Get city we're travelling from
                Vertex fromCity = t2.get(cityIndex);

                // City we're travelling to
                Vertex destinationCity;
                // Check we're not on our tour's last city, if we are set our
                // tour's final destination city to our starting city
                if(cityIndex + 1 < t2.size()){
                    destinationCity = t2.get(cityIndex + 1);
                }
                else{
                    destinationCity = t2.get(0);
                }
                // Get the distance between the two cities
                tourDistance += fromCity.getLinkedVertexLength(destinationCity);
            }
            distance = tourDistance;
        }
        return distance;
    }

    // Get number of cities on our tour
    public int tourSize() {
        return tour.size();
    }

    // Check if the tour contains a city
    public boolean containsCity(Vertex city){
        return tour.contains(city);
    }

    @Override
    public String toString() {
        String geneString = "|";
        for (int i = 0; i < tourSize(); i++) {
            geneString += getCity(i).getVertexCoordinate().toString()+"|";
        }
        return geneString;
    }
}
