/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An implementation of Graph using vertices list representation.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed weighted graph where:
    //   - vertices list contains all Vertex objects in the graph
    //   - Each Vertex maintains its incoming and outgoing edges
    // Representation invariant:
    //   - vertices != null
    //   - All Vertex objects in vertices are valid (non-null, consistent state)
    //   - No duplicate vertex labels
    //   - All edge weights > 0
    // Safety from rep exposure:
    //   - All fields are private and final
    //   - vertices() returns an unmodifiable set
    //   - sources() and targets() return unmodifiable maps
    //   - Vertex objects are not exposed directly
    
    /**
     * Construct an empty ConcreteVerticesGraph.
     */
    public ConcreteVerticesGraph() {
        checkRep();
    }
    
    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert vertices != null : "vertices should not be null";
        
        // Check for duplicate vertex labels
        Set<String> labels = vertices.stream()
            .map(Vertex::getLabel)
            .collect(Collectors.toSet());
        assert labels.size() == vertices.size() : "duplicate vertex labels found";
        
        // Check all vertices are valid
        for (Vertex vertex : vertices) {
            assert vertex != null : "vertex should not be null";
            vertex.checkRep();
        }
    }
    
    @Override 
    public boolean add(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex cannot be null");
        }
        
        // Check if vertex already exists
        for (Vertex v : vertices) {
            if (v.getLabel().equals(vertex)) {
                return false;
            }
        }
        
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }
    
    @Override 
    public int set(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("source and target cannot be null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("weight cannot be negative: " + weight);
        }
        
        // Ensure vertices exist
        add(source);
        add(target);
        
        // Find the vertices
        Vertex sourceVertex = findVertex(source);
        Vertex targetVertex = findVertex(target);
        
        // Get previous weight
        int previousWeight = sourceVertex.getTargetWeight(target);
        
        // Remove existing edges
        sourceVertex.removeOutgoingEdge(target);
        targetVertex.removeIncomingEdge(source);
        
        if (weight > 0) {
            // Add or update edge
            sourceVertex.addOutgoingEdge(target, weight);
            targetVertex.addIncomingEdge(source, weight);
        }
        
        checkRep();
        return previousWeight;
    }
    
    @Override 
    public boolean remove(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex cannot be null");
        }
        
        Vertex vertexToRemove = findVertex(vertex);
        if (vertexToRemove == null) {
            return false;
        }
        
        // Remove all edges to this vertex
        for (Vertex v : vertices) {
            boolean hadOutgoingEdge = v.removeOutgoingEdgeTo(vertex);
            if (hadOutgoingEdge) {
                // If we removed an outgoing edge to the vertex being removed,
                // also remove the incoming edge from that vertex
                vertexToRemove.removeIncomingEdge(v.getLabel());
            }
            v.removeIncomingEdge(vertex);
        }
        
        // Remove the vertex itself
        vertices.remove(vertexToRemove);
        
        checkRep();
        return true;
    }
    
    @Override 
    public Set<String> vertices() {
        return Collections.unmodifiableSet(
            vertices.stream()
                .map(Vertex::getLabel)
                .collect(Collectors.toSet())
        );
    }
    
    @Override 
    public Map<String, Integer> sources(String target) {
        if (target == null) {
            throw new IllegalArgumentException("target cannot be null");
        }
        
        Vertex targetVertex = findVertex(target);
        if (targetVertex == null) {
            return Collections.emptyMap();
        }
        
        return Collections.unmodifiableMap(new HashMap<>(targetVertex.getSources()));
    }
    
    @Override 
    public Map<String, Integer> targets(String source) {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        
        Vertex sourceVertex = findVertex(source);
        if (sourceVertex == null) {
            return Collections.emptyMap();
        }
        
        return Collections.unmodifiableMap(new HashMap<>(sourceVertex.getTargets()));
    }
    
    /**
     * Find a vertex by its label.
     * 
     * @param label the vertex label to find
     * @return the Vertex object with the given label, or null if not found
     */
    private Vertex findVertex(String label) {
        for (Vertex vertex : vertices) {
            if (vertex.getLabel().equals(label)) {
                return vertex;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (vertices.isEmpty()) {
            return "Empty graph (0 vertices)";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices:\n");
        for (Vertex vertex : vertices) {
            sb.append(vertex).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Mutable vertex in a directed weighted graph.
 * This class is internal to the rep of ConcreteVerticesGraph.
 */
class Vertex {
    
    private final String label;
    private final Map<String, Integer> sources; // incoming edges: source -> weight
    private final Map<String, Integer> targets; // outgoing edges: target -> weight
    
    // Abstraction function:
    //   Represents a vertex in a directed graph with:
    //   - label: the vertex identifier
    //   - sources: map of incoming edges (source vertex -> weight)
    //   - targets: map of outgoing edges (target vertex -> weight)
    // Representation invariant:
    //   - label != null and not empty
    //   - sources != null, targets != null
    //   - All weights in sources and targets > 0
    // Safety from rep exposure:
    //   - label is immutable String
    //   - sources and targets are mutable but only exposed via unmodifiable maps
    //   - All public methods return defensive copies or unmodifiable views
    
    /**
     * Construct a Vertex with the given label.
     * 
     * @param label the vertex label, not null
     * @throws IllegalArgumentException if label is null
     */
    public Vertex(String label) {
        if (label == null) {
            throw new IllegalArgumentException("label cannot be null");
        }
        
        this.label = label;
        this.sources = new HashMap<>();
        this.targets = new HashMap<>();
        
        checkRep();
    }
    
    /**
     * Check the representation invariant.
     */
    public void checkRep() {
        assert label != null : "label cannot be null";
        assert sources != null : "sources cannot be null";
        assert targets != null : "targets cannot be null";
        
        // Check all weights are positive
        for (int weight : sources.values()) {
            assert weight > 0 : "source weight must be positive: " + weight;
        }
        for (int weight : targets.values()) {
            assert weight > 0 : "target weight must be positive: " + weight;
        }
    }
    
    /**
     * @return the label of this vertex
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * @return an unmodifiable view of the incoming edges (source -> weight)
     */
    public Map<String, Integer> getSources() {
        return Collections.unmodifiableMap(sources);
    }
    
    /**
     * @return an unmodifiable view of the outgoing edges (target -> weight)
     */
    public Map<String, Integer> getTargets() {
        return Collections.unmodifiableMap(targets);
    }
    
    /**
     * Get the weight of an outgoing edge to the given target.
     * 
     * @param target the target vertex
     * @return the weight of the edge to target, or 0 if no such edge
     */
    public int getTargetWeight(String target) {
        return targets.getOrDefault(target, 0);
    }
    
    /**
     * Add an incoming edge to this vertex.
     * 
     * @param source the source vertex
     * @param weight the edge weight, must be positive
     * @throws IllegalArgumentException if weight <= 0
     */
    public void addIncomingEdge(String source, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive: " + weight);
        }
        sources.put(source, weight);
        checkRep();
    }
    
    /**
     * Add an outgoing edge from this vertex.
     * 
     * @param target the target vertex
     * @param weight the edge weight, must be positive
     * @throws IllegalArgumentException if weight <= 0
     */
    public void addOutgoingEdge(String target, int weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive: " + weight);
        }
        targets.put(target, weight);
        checkRep();
    }
    
    /**
     * Remove an incoming edge from this vertex.
     * 
     * @param source the source vertex to remove
     */
    public void removeIncomingEdge(String source) {
        sources.remove(source);
        checkRep();
    }
    
    /**
     * Remove an outgoing edge from this vertex.
     * 
     * @param target the target vertex to remove
     */
    public void removeOutgoingEdge(String target) {
        targets.remove(target);
        checkRep();
    }
    
    /**
     * Remove an outgoing edge and return whether it existed.
     * 
     * @param target the target vertex to remove
     * @return true if the edge existed, false otherwise
     */
    public boolean removeOutgoingEdgeTo(String target) {
        boolean removed = targets.containsKey(target);
        targets.remove(target);
        checkRep();
        return removed;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertex '").append(label).append("': ");
        sb.append("incoming=").append(sources).append(", ");
        sb.append("outgoing=").append(targets);
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Vertex other = (Vertex) obj;
        return label.equals(other.label);
    }
    
    @Override
    public int hashCode() {
        return label.hashCode();
    }
}