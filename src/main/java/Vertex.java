package main.java;

import java.util.HashMap;
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

    public void addVertex(Vertex vertex, Edge edge) {
        linkedVertices.put(vertex, edge);
    }

    /**
     * Returns the edge at given coordinate.
     * @param direction Direction.
     * @return
     */
    public Edge getEdge(Direction direction) {
        Coordinate position = new Coordinate(getVertexCoordinate());
        position.move(direction);
        for(Map.Entry<Vertex, Edge> vertexEdgeEntry : linkedVertices.entrySet()) {
            if(vertexEdgeEntry.getValue().containsCoordinate(position))
                return linkedVertices.get(vertexEdgeEntry.getKey());
        }
        return null;
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
        output += "\nConnected vertices:";
        for(Vertex vertex : linkedVertices.keySet()) {
            output += " Vectex" + vertex.getVertexCoordinate();
        }
        output += "\n}";
        return output;
    }
}