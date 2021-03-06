package com.kplex.swarm;

import com.kplex.node.Node;
import com.kplex.particle.Particle;
import com.kplex.particle.acceleration.Acceleration;
import com.kplex.particle.fitness.Fitness;
import com.kplex.particle.inertia.Inertia;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.kplex.config.Constants.MAX_ITERATIONS;

public abstract class Swarm<T extends Particle<U, V>, U, V> {
    protected Map<Integer, Node> nodes;
    protected int numberOfNodes;
    protected List<T> particles;
    protected U globalBestPosition;
    protected double globalBestFitness;
    protected final Fitness<U, V> fitness;
    protected final Inertia inertia;
    protected final Acceleration cognitive;
    protected final Acceleration social;
    protected double currentInertia;

    protected Swarm(Map<Integer, Node> nodes,
                    Fitness<U, V> fitness,
                    Inertia inertia,
                    Acceleration cognitive,
                    Acceleration social) {
        this.nodes = nodes;
        numberOfNodes = nodes.size();
        this.fitness = fitness;
        this.inertia = inertia;
        this.cognitive = cognitive;
        this.social = social;
    }

    public void iterate(int iterationCounter) {
        diversifySwarm();
        updateGlobalBestKnownPosition();
        calculateInertia(iterationCounter);
        
        particles.parallelStream()
                .forEach(
                        ((Consumer<T>) particle -> calculateVelocity(particle, iterationCounter))
                                .andThen(this::updatePosition)
                                .andThen(particle -> updateBestKnownPosition(particle, iterationCounter))
                );
        updateGlobalBestKnownPosition();
    }
    
    protected abstract void diversifySwarm();

    protected abstract void calculateInertia(int iterationCounter);

    protected abstract void calculateVelocity(T particle, int iterationCounter);

    protected abstract void updatePosition(T particle);

    protected abstract void updateBestKnownPosition(T particle, int iterationCounter);

    protected abstract void updateGlobalBestKnownPosition();

    protected abstract T createParticle(Map<Integer, Node> nodes, Fitness<U, V> fitness);

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public List<T> getParticles() {
        return particles;
    }

    public U getGlobalBestPosition() {
        return globalBestPosition;
    }

    public double getGlobalBestFitness() {
        return globalBestFitness;
    }

    public static <T extends Particle<U, V>, U, V, W extends Swarm<T, U, V>> void replaceWorstSwarm(List<W> swarms,
                                                                          int iterationCounter,
                                                                          Supplier<W> swarmGenerator) {
        // TODO parametrize 250
        if (iterationCounter % 250 == 0 && iterationCounter < MAX_ITERATIONS) {
            swarms.stream()
                    .min(Comparator.comparingDouble(Swarm::getGlobalBestFitness))
                    .ifPresent(swarm -> swarm = swarmGenerator.get());
        }
    }

    public void printNodes() {
        this.nodes.forEach(
                (integer, node) -> System.out.printf(
                        "%d: {%s}%n",
                        integer,
                        node.getAdjacentNodes()
                                .stream()
                                .map(Node::getLabel)
                                .sorted()
                                .map(Object::toString)
                                .collect(Collectors.joining(", "))
                )
        );
    }
}
