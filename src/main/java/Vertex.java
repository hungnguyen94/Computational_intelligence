package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Vertex {
    private Coordinate vertexCoordinate;
    private Map<Vertex, Edge> linkedVertices;

    public Vertex(Coordinate vertexCoordinate) {
        this.vertexCoordinate = new Coordinate(vertexCoordinate);
        this.linkedVertices = new HashMap<Vertex, Edge>();
    }

    public Coordinate getVertexCoordinate() {
        return vertexCoordinate;
    }

    public Edge getLinkedVertexEdge(Vertex v) {
        if(v == null)
            return new Edge();
        Edge linkedEdge = linkedVertices.getOrDefault(v, null);
        if(linkedEdge != null)
            return new Edge(linkedEdge);

        return new Edge();
    }

    public int getLinkedVertexLength(Vertex v) {
        if(v == null)
            return 99999999;
        Edge linkedEdge = linkedVertices.getOrDefault(v, null);
        if(linkedEdge != null)
            return linkedEdge.getSize();
        return 99999999;
    }

    public void addVertex(Vertex vertex, Edge edge) {
        if(vertex.getVertexCoordinate() == getVertexCoordinate())
            return;
        if(linkedVertices.get(vertex) != null) {
            if(linkedVertices.get(vertex).getSize() > edge.getSize())
                linkedVertices.put(vertex, new Edge(edge));
        } else {
            linkedVertices.put(vertex, new Edge(edge));
        }

    }

    /**
     * Returns the edge at given coordinate.
     * @param direction Direction.
     * @return
     */
    public synchronized Edge getEdge(Direction direction) {
        Coordinate position = new Coordinate(getVertexCoordinate());
        position.move(direction);
        Edge shortestEdges = null;
        for(Map.Entry<Vertex, Edge> vertexEdgeEntry : linkedVertices.entrySet()) {
            if(vertexEdgeEntry.getValue().containsCoordinate(position)) {
                if(shortestEdges == null || linkedVertices.get(vertexEdgeEntry.getKey()).getSize() < shortestEdges.getSize())
                    shortestEdges = linkedVertices.get(vertexEdgeEntry.getKey());
            }
        }
        return shortestEdges;
    }

    /**
     * Returns the shortest edge of the vertices.
     * @param vertexList
     * @return shortestedge
     */
    public synchronized Edge getShortestEdge(List<Vertex> vertexList) {
        Coordinate position = new Coordinate(getVertexCoordinate());
        Vertex shortestVertex = null;
        List<Vertex> filteredNullList = new ArrayList<>();
        for(Vertex vertex : vertexList) {
            if(vertex != null) {
                filteredNullList.add(vertex);
            }
        }
        for(Vertex vertex : filteredNullList) {
            Edge linkedEdge = getLinkedVertexEdge(vertex);
            if(linkedEdge != null && (shortestVertex == null || linkedEdge.getSize() < getLinkedVertexEdge(shortestVertex).getSize())) {
                shortestVertex = vertex;
            }
        }
        return getLinkedVertexEdge(shortestVertex);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Vertex vertex = (Vertex) o;

        return getVertexCoordinate().equals(vertex.getVertexCoordinate());
    }

    @Override
    public int hashCode() {
        int result = getVertexCoordinate().hashCode();
        result = 312 * result;
        return result;
    }

    @Override
    public String toString() {
        String output = "Vertex" + vertexCoordinate.toString() + " {";
        for(Map.Entry<Vertex, Edge> vertexEdgeEntry : linkedVertices.entrySet()) {
            output += "\n\tVertex" + vertexEdgeEntry.getKey().getVertexCoordinate() + ": ";
            output += vertexEdgeEntry.getValue();
        }
        output += "\n}";
        return output;
    }

    public String toString2() {
        String output = "Vertex" + vertexCoordinate.toString() + " {";
        for(Map.Entry<Vertex, Edge> vertexEdgeEntry : linkedVertices.entrySet()) {
            output += "\n\tVertex" + vertexEdgeEntry.getKey().getVertexCoordinate() + ": ";
            output += vertexEdgeEntry.getValue();
        }
        output += "\n}";
        return output;
    }
}