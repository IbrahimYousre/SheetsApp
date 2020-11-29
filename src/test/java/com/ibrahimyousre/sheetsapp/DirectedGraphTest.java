package com.ibrahimyousre.sheetsapp;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

public class DirectedGraphTest {

    @Test
    public void testTopologicalSort_linkAdded() {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("B"), "A");
        assertThat(directedGraph.topologicalSortFrom("A")).containsExactly("A");
        assertThat(directedGraph.topologicalSortFrom("B")).containsExactly("B", "A");
    }

    @Test
    public void testTopologicalSort_linkRemoved() {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("B"), "A");
        directedGraph.setLinks(asSet(), "A");
        assertThat(directedGraph.topologicalSortFrom("A")).containsExactly("A");
        assertThat(directedGraph.topologicalSortFrom("B")).containsExactly("B");
    }

    @Test
    public void testTopologicalSort_linkAdded2() {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("B", "C"), "A");
        directedGraph.setLinks(asSet("C"), "B");
        assertThat(directedGraph.topologicalSortFrom("A")).containsExactly("A");
        assertThat(directedGraph.topologicalSortFrom("B")).containsExactly("B", "A");
        assertThat(directedGraph.topologicalSortFrom("C")).containsExactly("C", "B", "A");
    }

    @Test
    public void testTopologicalSort_cycleDetection() {
        DirectedGraph<String> directedGraph = new DirectedGraph<>();
        directedGraph.setLinks(asSet("A"), "B");
        directedGraph.setLinks(asSet("B"), "C");
        directedGraph.setLinks(asSet("C"), "A");
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> directedGraph.topologicalSortFrom("A"));
        assertThat(exception).hasMessage("Cycle Found!");
    }

    private Set<String> asSet(String... arr) {
        return new HashSet<>(asList(arr));
    }
}
