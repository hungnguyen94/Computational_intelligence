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
        if(vertex.getVertexCoordinate() == vertexCoordinate)
            return;
        if(linkedVertices.get(vertex) != null) {
            if(linkedVertices.get(vertex).getSize() < edge.getSize())
                return;
        }
        linkedVertices.put(vertex, new Edge(edge));
    }

    /**
     * Returns the edge at given coordinate.
     * @param direction Direction.
     * @return
     */
    public Edge getEdge(Direction direction) {
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
}