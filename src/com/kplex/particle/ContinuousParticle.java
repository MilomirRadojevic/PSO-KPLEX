package com.kplex.particle;

import com.kplex.node.Node;
import com.kplex.particle.fitness.Fitness;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kplex.config.Constants.*;

public class ContinuousParticle extends Particle<List<Double>, List<Double>> {
    public ContinuousParticle(Map<Integer, Node> nodes,
                              Fitness<List<Double>, List<Double>> fitness) {
        super(nodes, fitness);

        position = IntStream.range(0, nodes.size())
                .mapToObj(value -> ThreadLocalRandom.current().nextDouble(BOUNDARY_MIN, BOUNDARY_MAX))
                .collect(Collectors.toList());

        velocity = IntStream.range(0, nodes.size())
                .mapToObj(
                        value -> ThreadLocalRandom.current().nextDouble(BOUNDARY_MIN, BOUNDARY_MAX)
                                * (ThreadLocalRandom.current().nextBoolean() ? 1 : -1)
                )
                .collect(Collectors.toList());

        bestPosition = new ArrayList<>(position);
        bestFitness = fitness();
    }
}
