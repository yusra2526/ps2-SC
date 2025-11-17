/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Tests for instance methods of Graph.
 * 
 * <p>PS2 instructions: you MUST NOT add constructors, fields, or non-@Test
 * methods to this class, or change the spec of {@link #emptyInstance()}.
 * Your tests MUST only obtain Graph instances by calling emptyInstance().
 * Your tests MUST NOT refer to specific concrete implementations.
 */
public abstract class GraphInstanceTest {
    
    // Testing strategy
    //   - add(): empty graph, duplicate vertices, multiple vertices
    //   - set(): new edges, update edges, remove edges (weight=0), self-loops
    //   - remove(): vertices with/without edges, non-existent vertices
    //   - vertices(): empty, multiple vertices, verify unmodifiable
    //   - sources(): no sources, multiple sources, non-existent target
    //   - targets(): no targets, multiple targets, non-existent source
    
    /**
     * Overridden by implementation-specific test classes.
     * 
     * @return a new empty graph of the particular implementation being tested
     */
    public abstract Graph<String> emptyInstance();
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("expected new graph to have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }
    
    // Tests for add(String vertex)
    
    @Test
    public void testAddVertexToEmptyGraph() {
        Graph<String> graph = emptyInstance();
        assertTrue("should be able to add vertex to empty graph", 
                   graph.add("v1"));
        assertEquals("graph should contain added vertex", 
                     Set.of("v1"), graph.vertices());
    }
    
    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("v1");
        assertFalse("should not be able to add duplicate vertex", 
                    graph.add("v1"));
        assertEquals("graph should still contain only one vertex", 
                     Set.of("v1"), graph.vertices());
    }
    
    @Test
    public void testAddMultipleVertices() {
        Graph<String> graph = emptyInstance();
        graph.add("v1");
        graph.add("v2");
        graph.add("v3");
        assertEquals("graph should contain all added vertices", 
                     Set.of("v1", "v2", "v3"), graph.vertices());
    }
    
    // Tests for set(String source, String target, int weight)
    
    @Test
    public void testSetNewEdge() {
        Graph<String> graph = emptyInstance();
        int prevWeight = graph.set("v1", "v2", 5);
        assertEquals("previous weight should be 0 for new edge", 0, prevWeight);
        assertEquals("graph should contain both vertices", 
                     Set.of("v1", "v2"), graph.vertices());
        assertEquals("target should have correct source", 
                     Map.of("v1", 5), graph.sources("v2"));
        assertEquals("source should have correct target", 
                     Map.of("v2", 5), graph.targets("v1"));
    }
    
    @Test
    public void testSetUpdateEdgeWeight() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 5);
        int prevWeight = graph.set("v1", "v2", 8);
        assertEquals("should return previous weight", 5, prevWeight);
        assertEquals("edge weight should be updated", 
                     Map.of("v1", 8), graph.sources("v2"));
    }
    
    @Test
    public void testSetRemoveEdgeWithZeroWeight() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 5);
        int prevWeight = graph.set("v1", "v2", 0);
        assertEquals("should return previous weight", 5, prevWeight);
        assertTrue("vertices should still exist", graph.vertices().containsAll(Set.of("v1", "v2")));
        assertTrue("sources should be empty", graph.sources("v2").isEmpty());
        assertTrue("targets should be empty", graph.targets("v1").isEmpty());
    }
    
    @Test
    public void testSetSelfLoop() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v1", 3);
        assertEquals("graph should contain vertex", Set.of("v1"), graph.vertices());
        assertEquals("self-loop should be in sources", Map.of("v1", 3), graph.sources("v1"));
        assertEquals("self-loop should be in targets", Map.of("v1", 3), graph.targets("v1"));
    }
    
    @Test
    public void testSetMultipleEdgesFromSameSource() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 1);
        graph.set("v1", "v3", 2);
        graph.set("v1", "v4", 3);
        
        assertEquals("source should have multiple targets", 
                     Map.of("v2", 1, "v3", 2, "v4", 3), graph.targets("v1"));
    }
    
    @Test
    public void testSetMultipleEdgesToSameTarget() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v4", 1);
        graph.set("v2", "v4", 2);
        graph.set("v3", "v4", 3);
        
        assertEquals("target should have multiple sources", 
                     Map.of("v1", 1, "v2", 2, "v3", 3), graph.sources("v4"));
    }
    
    // Tests for remove(String vertex)
    
    @Test
    public void testRemoveExistingVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("v1");
        assertTrue("should be able to remove existing vertex", graph.remove("v1"));
        assertTrue("graph should be empty after removal", graph.vertices().isEmpty());
    }
    
    @Test
    public void testRemoveNonExistentVertex() {
        Graph<String> graph = emptyInstance();
        assertFalse("should not be able to remove non-existent vertex", 
                    graph.remove("v1"));
    }
    
    @Test
    public void testRemoveVertexWithOutgoingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 1);
        graph.set("v1", "v3", 2);
        
        assertTrue("should remove vertex with outgoing edges", graph.remove("v1"));
        assertFalse("vertex should be gone", graph.vertices().contains("v1"));
        assertTrue("targets of removed vertex should be empty", graph.targets("v1").isEmpty());
        assertTrue("edges to v2 should be gone", graph.sources("v2").isEmpty());
        assertTrue("edges to v3 should be gone", graph.sources("v3").isEmpty());
    }
    
    @Test
    public void testRemoveVertexWithIncomingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v3", 1);
        graph.set("v2", "v3", 2);
        
        assertTrue("should remove vertex with incoming edges", graph.remove("v3"));
        assertFalse("vertex should be gone", graph.vertices().contains("v3"));
        assertTrue("sources of removed vertex should be empty", graph.sources("v3").isEmpty());
        assertFalse("edge from v1 should be gone", graph.targets("v1").containsKey("v3"));
        assertFalse("edge from v2 should be gone", graph.targets("v2").containsKey("v3"));
    }
    
    // Tests for vertices()
    
    @Test
    public void testVerticesUnmodifiable() {
        Graph<String> graph = emptyInstance();
        graph.add("v1");
        graph.add("v2");
        
        Set<String> vertices = graph.vertices();
        try {
            vertices.add("v3");
            fail("should not be able to modify vertices set");
        } catch (UnsupportedOperationException e) {
            // expected
        }
        
        assertEquals("original graph should not be modified", 
                     Set.of("v1", "v2"), graph.vertices());
    }
    
    // Tests for sources(String target)
    
    @Test
    public void testSourcesNoIncomingEdges() {
        Graph<String> graph = emptyInstance();
        graph.add("v1");
        assertTrue("sources should be empty for vertex with no incoming edges", 
                   graph.sources("v1").isEmpty());
    }
    
    @Test
    public void testSourcesMultipleIncomingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v3", 1);
        graph.set("v2", "v3", 2);
        graph.set("v4", "v3", 3);
        
        Map<String, Integer> expected = Map.of("v1", 1, "v2", 2, "v4", 3);
        assertEquals("sources should match expected", expected, graph.sources("v3"));
    }
    
    @Test
    public void testSourcesNonExistentVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("sources should be empty for non-existent vertex", 
                   graph.sources("nonexistent").isEmpty());
    }
    
    @Test
    public void testSourcesUnmodifiable() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 1);
        
        Map<String, Integer> sources = graph.sources("v2");
        try {
            sources.put("v3", 2);
            fail("should not be able to modify sources map");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    // Tests for targets(String source)
    
    @Test
    public void testTargetsNoOutgoingEdges() {
        Graph<String> graph = emptyInstance();
        graph.add("v1");
        assertTrue("targets should be empty for vertex with no outgoing edges", 
                   graph.targets("v1").isEmpty());
    }
    
    @Test
    public void testTargetsMultipleOutgoingEdges() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 1);
        graph.set("v1", "v3", 2);
        graph.set("v1", "v4", 3);
        
        Map<String, Integer> expected = Map.of("v2", 1, "v3", 2, "v4", 3);
        assertEquals("targets should match expected", expected, graph.targets("v1"));
    }
    
    @Test
    public void testTargetsNonExistentVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("targets should be empty for non-existent vertex", 
                   graph.targets("nonexistent").isEmpty());
    }
    
    @Test
    public void testTargetsUnmodifiable() {
        Graph<String> graph = emptyInstance();
        graph.set("v1", "v2", 1);
        
        Map<String, Integer> targets = graph.targets("v1");
        try {
            targets.put("v3", 2);
            fail("should not be able to modify targets map");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }
    
    // Comprehensive integration test
    
    @Test
    public void testComplexGraphOperations() {
        Graph<String> graph = emptyInstance();
        
        // Build a complex graph
        graph.set("A", "B", 1);
        graph.set("A", "C", 2);
        graph.set("B", "C", 3);
        graph.set("C", "D", 4);
        graph.set("D", "A", 5);
        
        // Verify structure
        assertEquals(Set.of("A", "B", "C", "D"), graph.vertices());
        assertEquals(Map.of("D", 5), graph.sources("A"));
        assertEquals(Map.of("B", 1, "C", 2), graph.targets("A"));
        assertEquals(Map.of("A", 1, "C", 3), graph.sources("B"));
        assertEquals(Map.of("C", 3), graph.targets("B"));
        
        // Remove a vertex and verify cleanup
        assertTrue(graph.remove("C"));
        assertEquals(Set.of("A", "B", "D"), graph.vertices());
        assertFalse(graph.targets("A").containsKey("C"));
        assertFalse(graph.sources("D").containsKey("C"));
    }
}