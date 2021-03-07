package com.kplex.node;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Node {
    private final int label;
    private List<Node> adjacentNodes;

    private Node(Integer label) {
        this.label = label;
    }

    private static Map<Integer, List<Integer>> readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        BufferedReader reader = Files.newBufferedReader(path);

        return reader.lines()
                .filter(line -> line.startsWith("e"))
                .map(line -> line.split(" "))
                .flatMap(line -> Stream.of(Arrays.asList(line[1], line[2]), Arrays.asList(line[2], line[1])))
                .collect(
                        Collectors.toMap(
                                pair -> Integer.valueOf(pair.get(0)),
                                pair -> Collections.singletonList(Integer.valueOf(pair.get(1))),
                                (oldList, newList) -> Stream.of(oldList, newList)
                                        .flatMap(Collection::stream)
                                        .collect(Collectors.toList())
                        )
                );
    }

    public static Map<Integer, Node> initializeNodes(String filePath) throws IOException {
        Map<Integer, List<Integer>> adjacencyMatrix = readFile(filePath);

        Map<Integer, Node> nodes = adjacencyMatrix.keySet()
                .stream()
                .collect(Collectors.toMap(Function.identity(), Node::new));

        nodes.forEach(
                (integer, node) -> node.adjacentNodes = adjacencyMatrix
                        .get(integer)
                        .stream()
                        .map(nodes::get)
                        .collect(Collectors.toList())
        );

        return nodes;
    }



    public static List<Long> nodesDegrees(List<Node> nodes) {
        List<Integer> selectedLabels = nodes.stream()
                .map(Node::getLabel)
                .collect(Collectors.toList());

        return nodes.stream()
                .map(
                        node -> node.getAdjacentNodes()
                                .stream()
                                .filter(adjacentNode -> selectedLabels.contains(adjacentNode.getLabel()))
                                .count()
                )
                .collect(Collectors.toList());
    }

    public int getLabel() {
        return label;
    }

    public List<Node> getAdjacentNodes() {
        return adjacentNodes;
    }
}
