package main.java;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Vertex {
    private Coordinate vertexCoordinate;
    private Map<Vertex, Edge> linkedVertices;

    public Vertex(Coordinate vertexCoordinate) {
        this.vertexCoordinate = new Coordinate(vertexCoordinate);
        this.linkedVertices = new ConcurrentHashMap<Vertex, Edge>();
    }

    public Coordinate getVertexCoordinate() {
        return vertexCoordinate;
    }

    public Edge getLinkedVertexEdge(Vertex v) {
        Edge linkedEdge = linkedVertices.getOrDefault(v, null);
        if(linkedEdge != null)
            return new Edge(linkedEdge);
        return null;
    }

    public void addVertex(Vertex vertex, Edge edge) {
        if(vertex.getVertexCoordinate().equals(vertexCoordinate))
            return;
        if(linkedVertices.get(vertex) != null) {
            if(linkedVertices.get(vertex).getSize() > edge.getSize())
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
        for(Vertex vertex : vertexList) {
            Edge linkedEdge = getLinkedVertexEdge(vertex);
            if(linkedEdge != null && (shortestVertex == null || linkedEdge.getSize() < getLinkedVertexEdge(shortestVertex).getSize())) {
                shortestVertex = vertex;
            }
            System.out.println(shortestVertex);
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
}