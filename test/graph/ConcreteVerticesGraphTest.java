/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests for ConcreteVerticesGraph.
 * 
 * This class runs the GraphInstanceTest tests against ConcreteVerticesGraph, as
 * well as tests for that particular implementation.
 * 
 * Tests against the Graph spec should be in GraphInstanceTest.
 */
public class ConcreteVerticesGraphTest extends GraphInstanceTest {
    
    /*
     * Provide a ConcreteVerticesGraph for tests in GraphInstanceTest.
     */
    @Override public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }
    
    /*
     * Testing ConcreteVerticesGraph...
     */
    
    // Testing strategy for ConcreteVerticesGraph.toString()
    //   - Empty graph
    //   - Graph with vertices but no edges
    //   - Graph with vertices and edges
    //   - Verify format includes vertices and their connections
    
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
                   result.contains("incoming") || result.contains("outgoing"));
    }
    
    /*
     * Testing Vertex...
     */
    
    // Testing strategy for Vertex
    //   - Construction with valid label
    //   - Adding/removing incoming and outgoing edges
    //   - Getting sources and targets
    //   - toString() format
    
    @Test
    public void testVertexConstruction() {
        Vertex vertex = new Vertex("A");
        assertEquals("label should match", "A", vertex.getLabel());
        assertTrue("new vertex should have no sources", vertex.getSources().isEmpty());
        assertTrue("new vertex should have no targets", vertex.getTargets().isEmpty());
    }
    
    @Test
    public void testVertexAddEdge() {
        Vertex source = new Vertex("A");
        Vertex target = new Vertex("B");
        
        source.addOutgoingEdge("B", 2);
        target.addIncomingEdge("A", 2);
        
        assertEquals("source should have target", 1, source.getTargets().size());
        assertEquals("target should have source", 1, target.getSources().size());
        assertEquals("edge weight should match", Integer.valueOf(2), source.getTargets().get("B"));
        assertEquals("edge weight should match", Integer.valueOf(2), target.getSources().get("A"));
    }
    
    @Test
    public void testVertexRemoveEdge() {
        Vertex vertex = new Vertex("A");
        vertex.addOutgoingEdge("B", 2);
        vertex.addIncomingEdge("C", 3);
        
        vertex.removeOutgoingEdge("B");
        vertex.removeIncomingEdge("C");
        
        assertTrue("outgoing edges should be empty", vertex.getTargets().isEmpty());
        assertTrue("incoming edges should be empty", vertex.getSources().isEmpty());
    }
    
    @Test
    public void testVertexToString() {
        Vertex vertex = new Vertex("A");
        vertex.addOutgoingEdge("B", 2);
        vertex.addIncomingEdge("C", 3);
        
        String result = vertex.toString();
        assertTrue("should contain label", result.contains("A"));
        assertTrue("should contain outgoing edge", result.contains("B") || result.contains("2"));
        assertTrue("should contain incoming edge", result.contains("C") || result.contains("3"));
    }
}