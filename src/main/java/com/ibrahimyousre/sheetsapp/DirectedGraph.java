package com.ibrahimyousre.sheetsapp;

import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Value;

public class DirectedGraph<K> {
    private final Map<K, Node> nodeMap = new HashMap<>();

    void setLinks(Set<K> from, K to) {
        Node toNode = nodeMap.computeIfAbsent(to, Node::new);
        List<Node> garbageCandidate = new ArrayList<>();
        toNode.inGoingLinks.forEach(n -> {
            n.outGoingLinks.remove(toNode);
            garbageCandidate.add(n);
        });
        garbageCandidate.add(toNode);
        toNode.inGoingLinks.clear();
        from.stream().map(f -> nodeMap.computeIfAbsent(f, Node::new))
                .peek(toNode.inGoingLinks::add)
                .forEach(f -> f.outGoingLinks.add(toNode));
        garbageCandidate.stream().filter(Node::isEmpty).map(Node::getKey).forEach(nodeMap::remove);
    }

    List<K> topologicalSortFrom(K from) {
        if (!nodeMap.containsKey(from)) { return singletonList(from); }
        Set<K> visited = new HashSet<>();
        Set<K> calculating = new HashSet<>();
        LinkedList<K> list = new LinkedList<>();
        helper(visited, calculating, list, nodeMap.get(from));
        return list;
    }

    private void helper(Set<K> visited, Set<K> calculating, LinkedList<K> list, Node node) {
        if (calculating.contains(node.key)) { throw new IllegalStateException("Cycle Found!"); }
        if (visited.contains(node.key)) { return; }
        visited.add(node.key);
        calculating.add(node.key);
        node.outGoingLinks.forEach(n -> helper(visited, calculating, list, n));
        calculating.remove(node.key);
        list.addFirst(node.key);
    }

    @Value
    private class Node {
        K key;
        @EqualsAndHashCode.Exclude
        Set<Node> outGoingLinks = new HashSet<>();
        @EqualsAndHashCode.Exclude
        Set<Node> inGoingLinks = new HashSet<>();

        public Node(K key) {
            this.key = key;
        }

        public boolean isEmpty() {
            return inGoingLinks.isEmpty() && outGoingLinks.isEmpty();
        }
    }
}
