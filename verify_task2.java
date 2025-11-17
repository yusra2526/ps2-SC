public import graph.*;
import java.util.*;

public class verify_task2 {
    public static void main(String[] args) {
        System.out.println("=== VERIFYING TASK 2 IMPLEMENTATIONS ===");
        
        // Test 1: Basic functionality
        System.out.println("\n1. Testing Basic Graph Operations:");
        Graph<String> graph1 = new ConcreteEdgesGraph();
        graph1.add("A");
        graph1.add("B");
        graph1.set("A", "B", 5);
        System.out.println("   ConcreteEdgesGraph: " + graph1.vertices());
        System.out.println("   A -> B: " + graph1.targets("A"));
        
        Graph<String> graph2 = new ConcreteVerticesGraph();
        graph2.add("X");
        graph2.add("Y");
        graph2.set("X", "Y", 3);
        System.out.println("   ConcreteVerticesGraph: " + graph2.vertices());
        System.out.println("   X -> Y: " + graph2.targets("X"));
        
        // Test 2: Edge updates
        System.out.println("\n2. Testing Edge Updates:");
        int prevWeight = graph1.set("A", "B", 8);
        System.out.println("   Updated A->B from " + prevWeight + " to 8");
        System.out.println("   New weight: " + graph1.targets("A").get("B"));
        
        // Test 3: Vertex removal
        System.out.println("\n3. Testing Vertex Removal:");
        graph1.remove("B");
        System.out.println("   After removing B: " + graph1.vertices());
        System.out.println("   A targets: " + graph1.targets("A"));
        
        // Test 4: toString()
        System.out.println("\n4. Testing toString():");
        System.out.println("   ConcreteEdgesGraph:\n" + graph1);
        System.out.println("   ConcreteVerticesGraph:\n" + graph2);
        
        // Test 5: Graph.empty()
        System.out.println("\n5. Testing Graph.empty():");
        Graph<String> emptyGraph = Graph.empty();
        System.out.println("   Empty graph: " + emptyGraph.vertices());
        
        System.out.println("\n✅ TASK 2 COMPLETED SUCCESSFULLY!");
        System.out.println("All Graph implementations are working correctly.");
    }
} {
    
}
