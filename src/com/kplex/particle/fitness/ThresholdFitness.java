package com.kplex.particle.fitness;

import com.kplex.node.Node;
import com.kplex.particle.Particle;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kplex.config.Constants.K;
import static com.kplex.config.Constants.KPLEX_THRESHOLD;

public class ThresholdFitness implements ContinuousFitness {
    private static ThresholdFitness INSTANCE;

    private ThresholdFitness() {
        //
    }

    public static ThresholdFitness getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ThresholdFitness();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(Particle<List<Double>, List<Double>> particle) {
        List<Node> selectedNodes = IntStream.range(0, particle.getNodes().size())
                .filter(index -> particle.getPosition().get(index) > KPLEX_THRESHOLD)
                .mapToObj(index -> particle.getNodes().get(index + 1))
                .collect(Collectors.toList());

        List<Long> allNodesDegrees = Node.nodesDegrees(selectedNodes);

        long numberOfValidNodes = allNodesDegrees.stream()
                .filter(count -> count >= selectedNodes.size() - K)
                .count();

        List<Long> invalidNodesDegrees = allNodesDegrees.stream()
                .filter(value -> value < selectedNodes.size() - K)
                .distinct()
                .sorted(Comparator.<Long>naturalOrder().reversed())
                .collect(Collectors.toList());

        double invalidNodesCorrectionFactor = IntStream.range(1, selectedNodes.size() - K)
                .filter(value -> invalidNodesDegrees.contains((long) (selectedNodes.size() - K - value)))
                .mapToObj(value -> 1 / Math.pow(2, value))
                .reduce(0.0, Double::sum);

        return numberOfValidNodes + invalidNodesCorrectionFactor;
    }
}
