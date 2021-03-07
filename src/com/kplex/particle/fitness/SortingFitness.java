package com.kplex.particle.fitness;

import com.kplex.node.Node;
import com.kplex.particle.Particle;
import com.kplex.util.Pair;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.kplex.config.Constants.K;

public class SortingFitness implements ContinuousFitness {
    private static SortingFitness INSTANCE;

    private SortingFitness() {
        //
    }

    public static SortingFitness getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SortingFitness();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(Particle<List<Double>, List<Double>> particle) {
        SortedMap<Double, List<Node>> orderedNodes = IntStream.range(0, particle.getNodes().size())
                .boxed()
                .collect(
                        Collectors.toMap(
                                index -> particle.getPosition().get(index),
                                index -> Collections.singletonList(particle.getNodes().get(index + 1)),
                                (oldList, newList) -> Stream.of(oldList, newList)
                                        .flatMap(Collection::stream)
                                        .collect(Collectors.toList()),
                                TreeMap::new
                        )
                )
                .descendingMap();

        List<Pair<Double, Node>> distinctlyOrderedNodes = orderedNodes.entrySet()
                .stream()
                .flatMap(
                        doubleListEntry -> doubleListEntry.getValue()
                                .stream()
                                .map(node -> new Pair<>(doubleListEntry.getKey(), node))
                )
                .collect(Collectors.toList());

        return IntStream.range(0, distinctlyOrderedNodes.size())
                .mapToObj(index -> distinctlyOrderedNodes.subList(0, index + 1))
                .map(
                        subgraph -> Node.nodesDegrees(
                                subgraph.stream()
                                        .map(Pair::getSecond)
                                        .collect(Collectors.toList())
                        )
                )
                .takeWhile(
                        nodeDegrees -> nodeDegrees.stream()
                                .allMatch(degree -> degree >= nodeDegrees.size() - K)
                )
                .count();
    }
}
