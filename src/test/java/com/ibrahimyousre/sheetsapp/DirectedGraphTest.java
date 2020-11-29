package com.ibrahimyousre.sheetsapp;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class DirectedGraphTest {

    @Test
    public void testTopologicalSort_scenario1() throws Exception {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("B"), "A");
        assertThat(directedGraph.topologicalSortFrom("A")).containsExactly("A");
        assertThat(directedGraph.topologicalSortFrom("B")).contains("B", "A");
    }

    @Test
    public void testTopologicalSort_scenario2() throws Exception {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("B", "C"), "A");
        directedGraph.setLinks(asSet("C"), "B");
        assertThat(directedGraph.topologicalSortFrom("A")).contains("A");
        assertThat(directedGraph.topologicalSortFrom("B")).contains("B", "A");
        assertThat(directedGraph.topologicalSortFrom("C")).contains("C", "B", "A");
    }

    @Test
    public void testTopologicalSort_cycleDetection() throws Exception {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("A"), "B");
        directedGraph.setLinks(asSet("B"), "C");
        directedGraph.setLinks(asSet("C"), "A");
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            assertThat(directedGraph.topologicalSortFrom("A")).contains("A");
        });
        assertThat(exception).hasMessage("Cycle Found!");
    }

    private Set<String> asSet(String... arr) {
        return new HashSet<>(asList(arr));
    }
}
