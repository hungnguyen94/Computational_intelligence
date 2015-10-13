package main.java;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private int type;
    private int pheromones;
    private List<Vertex> linkedVertexes;


    public Vertex() {
        this.linkedVertexes = new ArrayList<Vertex>();
    }

    public void addVertex(Vertex v) {
        linkedVertexes.add(v);
    }

    public void setPheromones(int t){
        pheromones = t;
    }

    public int getPheromones(){
        return pheromones;
    }
}