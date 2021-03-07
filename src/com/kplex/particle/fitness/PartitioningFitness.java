package com.kplex.particle.fitness;

import com.kplex.node.Node;
import com.kplex.particle.Particle;
import com.kplex.util.Pair;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kplex.config.Constants.*;

public class PartitioningFitness implements ContinuousFitness {
    private static PartitioningFitness INSTANCE;

    private PartitioningFitness() {
        //
    }

    public static PartitioningFitness getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PartitioningFitness();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(Particle<List<Double>, List<Double>> particle) {
        Map<Double, List<Node>> partitionedNodes = IntStream.range(0, particle.getNodes().size())
                .mapToObj(index -> new Pair<>(particle.getPosition().get(index), particle.getNodes().get(index + 1)))
                .collect(
                        Collectors.groupingBy(
                                pair -> Math.floor(
                                        particle.getNodes().size()
                                                * (pair.getFirst() - BOUNDARY_MIN)
                                                / (BOUNDARY_MAX - BOUNDARY_MIN)
                                ),
                                Collectors.mapping(Pair::getSecond, Collectors.toList())
                        )
                );

        Map<Boolean, List<Long>> partitionDegrees = partitionedNodes.values()
                .stream()
                .map(Node::nodesDegrees)
                .map(
                        nodeDegrees -> new Pair<>(
                                nodeDegrees,
                                nodeDegrees.stream()
                                        .filter(nodeDegree -> nodeDegree >= nodeDegrees.size() - K)
                                        .count()
                        )
                )
                .collect(
                        Collectors.partitioningBy(
                                pair -> pair.getFirst().stream()
                                        .allMatch(nodeDegree -> nodeDegree >= pair.getFirst().size() - K),
                                Collectors.mapping(Pair::getSecond, Collectors.toList())
                        )
                );

        long totalCorrect = partitionDegrees.values()
                .stream()
                .map(
                        degrees -> degrees.stream()
                                .mapToLong(Long::longValue)
                                .sum()
                )
                .mapToLong(Long::longValue)
                .sum();

        long largestValidPartition = partitionDegrees.get(true)
                .stream()
                .max(Long::compareTo)
                .orElse(0L);

//        return totalCorrect + (double) largestValidPartition / particle.getNodes().size();
//        return largestValidPartition + (double) totalCorrect / (particle.getNodes().size() + 1.0);
//        return largestValidPartition;
    }
}
