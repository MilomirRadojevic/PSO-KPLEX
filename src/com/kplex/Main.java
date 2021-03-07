package com.kplex;

import com.kplex.node.Node;
import com.kplex.particle.ContinuousParticle;
import com.kplex.particle.acceleration.LinearlyChangingCognitive;
import com.kplex.particle.acceleration.LinearlyChangingSocial;
import com.kplex.particle.fitness.*;
import com.kplex.particle.inertia.LinearlyChangingInertia;
import com.kplex.swarm.ContinuousSwarm;
import com.kplex.swarm.Swarm;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kplex.config.Constants.MAX_ITERATIONS;
import static com.kplex.config.Constants.NUMBER_OF_SUBSWARMS;

public class Main {

    public static void main(String[] args) throws IOException {
        Instant start = Instant.now();

        Map<Integer, Node> nodes = Node.initializeNodes("src/resources/MANN_a9.clq.txt");

        List<Swarm<ContinuousParticle, List<Double>, List<Double>>> swarms = IntStream
                .range(0, NUMBER_OF_SUBSWARMS)
                .mapToObj(
                        value -> new ContinuousSwarm(
                                nodes,
                                PartitioningFitness.getInstance(),
                                LinearlyChangingInertia.getInstance(),
                                LinearlyChangingCognitive.getInstance(),
                                LinearlyChangingSocial.getInstance()
                        )
                )
                .collect(Collectors.toList());

        IntStream.iterate(1, index -> index <= MAX_ITERATIONS, index -> index + 1)
//                .peek(operand -> swarm.getParticles()
//                        .stream()
//                        .map(Particle::getPosition)
//                        .collect(Collectors.toList())
//                        .stream().map(doubles -> doubles.get(0))
//                        .collect(Collectors.toList()))
                .forEach(value -> swarms.forEach(swarm -> swarm.iterate(value)));

        List<Double> gbf = swarms.stream()
                .map(Swarm::getGlobalBestFitness)
                .collect(Collectors.toList());

        var gbp = swarms.stream()
                .map(Swarm::getGlobalBestPosition)
                .collect(Collectors.toList());

        System.out.println(gbf);

        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.printf("Total time: %d%n", timeElapsed);
    }
}
