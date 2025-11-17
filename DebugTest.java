import graph.*;
import java.util.*;

public class DebugTest {
    public static void main(String[] args) {
        System.out.println("=== Debugging Complex Graph Operations ===");

        // Test ConcreteEdgesGraph
        System.out.println("\n1. ConcreteEdgesGraph:");
        debugGraph(new ConcreteEdgesGraph());

        // Test ConcreteVerticesGraph
        System.out.println("\n2. ConcreteVerticesGraph:");
        debugGraph(new ConcreteVerticesGraph());
    }

    static void debugGraph(Graph<String> graph) {
        // Build the complex graph
        graph.set("A", "B", 1);
        graph.set("A", "C", 2);
        graph.set("B", "C", 3);
        graph.set("C", "D", 4);
        graph.set("D", "A", 5);

        System.out.println("   All vertices: " + graph.vertices());
        System.out.println("   Sources of A: " + graph.sources("A"));
        System.out.println("   Sources of B: " + graph.sources("B"));
        System.out.println("   Sources of C: " + graph.sources("C"));
        System.out.println("   Sources of D: " + graph.sources("D"));
        System.out.println("   Targets of A: " + graph.targets("A"));
        System.out.println("   Targets of B: " + graph.targets("B"));
        System.out.println("   Targets of C: " + graph.targets("C"));
        System.out.println("   Targets of D: " + graph.targets("D"));

        // The problematic assertion
        Map<String, Integer> expected = Map.of("A", 1, "C", 3);
        Map<String, Integer> actual = graph.sources("B");
        System.out.println("   Expected sources of B: " + expected);
        System.out.println("   Actual sources of B: " + actual);
        System.out.println("   Match: " + expected.equals(actual));
    }
}