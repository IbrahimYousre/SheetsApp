package com.ibrahimyousre.sheetsapp;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DirectedGraph<K> {
    Map<K, Node> nodeMap = new HashMap<>();

    void setLinks(Set<K> from, K to) {
        Set<Node> fromNodes = from.stream().map(t -> nodeMap.computeIfAbsent(t, Node::new)).collect(toSet());
        Node toNode = nodeMap.computeIfAbsent(to, Node::new);
        toNode.inGoingLinks.addAll(fromNodes);
        fromNodes.forEach(f -> f.outGoingLinks.add(toNode));
    }

    List<K> topologicalSortFrom(K from) {
        if (!nodeMap.containsKey(from)) { return singletonList(from); }
        Set<K> visited = new HashSet<>();
        Set<K> calculating = new HashSet<>();
        LinkedList<K> stack = new LinkedList<>();
        helper(visited, calculating, stack, nodeMap.get(from));
        return stack;
    }

    private void helper(Set<K> visited, Set<K> calculating, LinkedList<K> stack, Node node) {
        if (calculating.contains(node.key)) { throw new IllegalStateException("Cycle Found!"); }
        if (visited.contains(node.key)) { return; }
        visited.add(node.key);
        calculating.add(node.key);
        node.outGoingLinks.forEach(n -> helper(visited, calculating, stack, n));
        calculating.remove(node.key);
        stack.addFirst(node.key);
    }

    private class Node {
        K key;
        Set<Node> outGoingLinks = new HashSet<>();
        Set<Node> inGoingLinks = new HashSet<>();

        public Node(K key) {
            this.key = key;
        }

        public Node(Node node) {
            this.key = node.key;
            this.outGoingLinks.addAll(node.outGoingLinks);
            this.inGoingLinks.addAll(node.inGoingLinks);
        }
    }
}
