package com.kplex.swarm;

import com.kplex.node.Node;
import com.kplex.particle.ContinuousParticle;
import com.kplex.particle.Particle;
import com.kplex.particle.acceleration.Acceleration;
import com.kplex.particle.fitness.Fitness;
import com.kplex.particle.inertia.Inertia;
import com.kplex.util.Utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kplex.config.Constants.*;
import static com.kplex.config.Constants.LEARNING_RATE;

public class ContinuousSwarm extends Swarm<ContinuousParticle, List<Double>, List<Double>> {
    public ContinuousSwarm(Map<Integer, Node> nodes,
                           Fitness<List<Double>, List<Double>> fitness,
                           Inertia inertia,
                           Acceleration cognitive,
                           Acceleration social) {
        super(nodes, fitness, inertia, cognitive, social);

        particles = IntStream.range(0, POPULATION_SIZE)
                .mapToObj(value -> createParticle(nodes, fitness))
                .collect(Collectors.toList());

        particles.stream()
                .max(Comparator.comparingDouble(Particle::getBestFitness))
                .ifPresent(particle -> {
                    globalBestPosition = new ArrayList<>(particle.getBestPosition());
                    globalBestFitness = particle.getBestFitness();
                });
    }

    @Override
    protected void diversifySwarm() {
        IntStream.range(0, particles.size())
                .filter(value -> ThreadLocalRandom.current().nextDouble() < DIVERSIFICATION_THRESHOLD)
                .forEach(index -> particles.set(index, createParticle(nodes, fitness)));
    }

    @Override
    protected void calculateVelocity(ContinuousParticle particle, int iterationCounter) {
        particle.setVelocity(
                IntStream.range(0, numberOfNodes)
                        .mapToObj(index -> {
                            double random_cognitive_factor = ThreadLocalRandom.current().nextDouble();
                            double random_social_factor = ThreadLocalRandom.current().nextDouble();

                            return inertia.calculate(iterationCounter) * particle.getVelocity().get(index)
                                    + cognitive.calculate(iterationCounter) * random_cognitive_factor
                                    * (particle.getBestPosition().get(index)
                                        - particle.getPosition().get(index))
                                    + social.calculate(iterationCounter) * random_social_factor
                                    * (globalBestPosition.get(index) - particle.getPosition().get(index));
                        })
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void updatePosition(ContinuousParticle particle) {
        particle.setPosition(
                IntStream.range(0, numberOfNodes)
                        .mapToObj(
                                index -> Utils.boxed(
                                        particle.getPosition().get(index)
                                                + LEARNING_RATE * particle.getVelocity().get(index)
                                )
                        )
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected void updateBestKnownPosition(ContinuousParticle particle, int iterationCounter) {
        double fitness = particle.fitness();

        if (fitness > particle.getBestFitness()) {
            particle.setBestPosition(new ArrayList<>(particle.getPosition()));
            particle.setBestFitness(fitness);
        }
    }

    @Override
    protected void updateGlobalBestKnownPosition() {
        particles.stream()
                .filter(particle -> particle.getBestFitness() > globalBestFitness)
                .max(Comparator.comparingDouble(Particle::getBestFitness))
                .ifPresent(particle -> {
                    globalBestPosition = new ArrayList<>(particle.getBestPosition());
                    globalBestFitness = particle.getBestFitness();
                });
    }

    @Override
    protected ContinuousParticle createParticle(Map<Integer, Node> nodes,
                                                Fitness<List<Double>, List<Double>> fitness) {
        return new ContinuousParticle(nodes, fitness);
    }
}
