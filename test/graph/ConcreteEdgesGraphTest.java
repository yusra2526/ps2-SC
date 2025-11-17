/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteEdgesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteEdgesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteEdgesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteEdgesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }
    
    /*
     * Testing ConcreteEdgesGraph...
     */
    
    // Testing strategy for ConcreteEdgesGraph.toString()
    //   - Empty graph
    //   - Graph with vertices but no edges
    //   - Graph with vertices and edges
    //   - Verify format includes vertices and edges with weights
    
    @Test
    public void testToStringEmptyGraph() {
        Graph<String> graph = emptyInstance();
        String result = graph.toString();
        assertTrue("toString should indicate empty graph", 
                   result.contains("Empty") || result.contains("0 vertices"));
    }
    
    @Test
    public void testToStringWithVerticesAndEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("A", "B", 1);
        graph.set("B", "C", 2);
        String result = graph.toString();
        
        assertTrue("should contain vertex A", result.contains("A"));
        assertTrue("should contain vertex B", result.contains("B"));
        assertTrue("should contain vertex C", result.contains("C"));
        assertTrue("should contain edge information", 
                   result.contains("->") || result.contains("weight"));
    }
    
    /*
     * Testing Edge...
     */
    
    // Testing strategy for Edge
    //   - Construction with valid parameters
    //   - Construction with invalid parameters (should throw exception)
    //   - getSource(), getTarget(), getWeight()
    //   - toString() format
    
    @Test
    public void testEdgeConstruction() {
        Edge edge = new Edge("src", "tgt", 5);
        assertEquals("source should match", "src", edge.getSource());
        assertEquals("target should match", "tgt", edge.getTarget());
        assertEquals("weight should match", 5, edge.getWeight());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructionNullSource() {
        new Edge(null, "tgt", 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructionNullTarget() {
        new Edge("src", null, 1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeConstructionNegativeWeight() {
        new Edge("src", "tgt", -1);
    }
    
    @Test
    public void testEdgeToString() {
        Edge edge = new Edge("A", "B", 3);
        String result = edge.toString();
        assertTrue("should contain source", result.contains("A"));
        assertTrue("should contain target", result.contains("B"));
        assertTrue("should contain weight", result.contains("3"));
    }
}