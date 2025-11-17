/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An implementation of Graph using edges list representation.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed weighted graph where:
    //   - vertices set contains all vertex labels in the graph
    //   - edges list contains all directed edges with positive weights
    // Representation invariant:
    //   - vertices != null, edges != null
    //   - All vertices in edges exist in vertices set
    //   - All edge weights > 0
    //   - No duplicate edges (same source and target)
    // Safety from rep exposure:
    //   - All fields are private and final
    //   - vertices() returns unmodifiable set
    //   - sources() and targets() return unmodifiable maps
    //   - Edge class is immutable
    
    /**
     * Construct an empty ConcreteEdgesGraph.
     */
    public ConcreteEdgesGraph() {
        checkRep();
    }
    
    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert vertices != null : "vertices should not be null";
        assert edges != null : "edges should not be null";
        
        // Check all edges have valid vertices and weights
        for (Edge edge : edges) {
            assert vertices.contains(edge.getSource()) : 
                "edge source must be in vertices: " + edge.getSource();
            assert vertices.contains(edge.getTarget()) : 
                "edge target must be in vertices: " + edge.getTarget();
            assert edge.getWeight() > 0 : "edge weight must be positive: " + edge.getWeight();
        }
        
        // Check for duplicate edges
        Set<String> edgeSet = new HashSet<>();
        for (Edge edge : edges) {
            String edgeKey = edge.getSource() + "->" + edge.getTarget();
            assert !edgeSet.contains(edgeKey) : "duplicate edge: " + edgeKey;
            edgeSet.add(edgeKey);
        }
    }
    
    @Override 
    public boolean add(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex cannot be null");
        }
        boolean added = vertices.add(vertex);
        checkRep();
        return added;
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
        vertices.add(source);
        vertices.add(target);
        
        // Find existing edge and get previous weight
        int previousWeight = 0;
        Edge existingEdge = null;
        
        for (Edge edge : edges) {
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                existingEdge = edge;
                previousWeight = edge.getWeight();
                break;
            }
        }
        
        // Remove existing edge if found
        if (existingEdge != null) {
            edges.remove(existingEdge);
        }
        
        // Add new edge if weight > 0
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }
        
        checkRep();
        return previousWeight;
    }
    
    @Override 
    public boolean remove(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("vertex cannot be null");
        }
        
        if (!vertices.contains(vertex)) {
            return false;
        }
        
        // Remove vertex
        vertices.remove(vertex);
        
        // Remove all edges connected to this vertex
        edges.removeIf(edge -> 
            edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        
        checkRep();
        return true;
    }
    
    @Override 
    public Set<String> vertices() {
        return Collections.unmodifiableSet(vertices);
    }
    
    @Override 
    public Map<String, Integer> sources(String target) {
        if (target == null) {
            throw new IllegalArgumentException("target cannot be null");
        }
        
        Map<String, Integer> result = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                result.put(edge.getSource(), edge.getWeight());
            }
        }
        return Collections.unmodifiableMap(result);
    }
    
    @Override 
    public Map<String, Integer> targets(String source) {
        if (source == null) {
            throw new IllegalArgumentException("source cannot be null");
        }
        
        Map<String, Integer> result = new HashMap<>();
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                result.put(edge.getTarget(), edge.getWeight());
            }
        }
        return Collections.unmodifiableMap(result);
    }
    
    @Override
    public String toString() {
        if (vertices.isEmpty()) {
            return "Empty graph (0 vertices, 0 edges)";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices and ")
          .append(edges.size()).append(" edges:\n");
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges:\n");
        for (Edge edge : edges) {
            sb.append("  ").append(edge).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Immutable directed weighted edge.
 * This class is internal to the rep of ConcreteEdgesGraph.
 */
class Edge {
    
    private final String source;
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //   Represents a directed edge from source to target with given weight
    // Representation invariant:
    //   - source != null, target != null
    //   - weight > 0
    // Safety from rep exposure:
    //   - All fields are private and final
    //   - All fields are immutable types (String, int)
    
    /**
     * Construct an Edge with given source, target, and weight.
     * 
     * @param source the source vertex, not null
     * @param target the target vertex, not null  
     * @param weight the edge weight, must be positive
     * @throws IllegalArgumentException if source or target is null, or weight <= 0
     */
    public Edge(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("source and target cannot be null");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("weight must be positive: " + weight);
        }
        
        this.source = source;
        this.target = target;
        this.weight = weight;
        
        checkRep();
    }
    
    /**
     * Check the representation invariant.
     */
    private void checkRep() {
        assert source != null : "source cannot be null";
        assert target != null : "target cannot be null";
        assert weight > 0 : "weight must be positive: " + weight;
    }
    
    /**
     * @return the source vertex of this edge
     */
    public String getSource() {
        return source;
    }
    
    /**
     * @return the target vertex of this edge
     */
    public String getTarget() {
        return target;
    }
    
    /**
     * @return the weight of this edge
     */
    public int getWeight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Edge other = (Edge) obj;
        return source.equals(other.source) && 
               target.equals(other.target) && 
               weight == other.weight;
    }
    
    @Override
    public int hashCode() {
        return source.hashCode() * 31 + target.hashCode() * 17 + weight;
    }
}