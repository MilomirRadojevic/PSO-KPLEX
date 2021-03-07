package com.kplex.particle;

import com.kplex.node.Node;
import com.kplex.particle.fitness.Fitness;

import java.util.Map;

public abstract class Particle<T, U> {
    protected final Map<Integer, Node> nodes;
    protected T position;
    protected U velocity;
    protected T bestPosition;
    protected double bestFitness;
    protected final Fitness<T, U> fitness;

    protected Particle(Map<Integer, Node> nodes, Fitness<T, U> fitness) {
        this.nodes = nodes;
        this.fitness = fitness;
    }

    public double fitness() {
        return fitness.calculate(this);
    }

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public T getPosition() {
        return position;
    }

    public void setPosition(T position) {
        this.position = position;
    }

    public U getVelocity() {
        return velocity;
    }

    public void setVelocity(U velocity) {
        this.velocity = velocity;
    }

    public T getBestPosition() {
        return bestPosition;
    }

    public void setBestPosition(T bestPosition) {
        this.bestPosition = bestPosition;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public void setBestFitness(double bestFitness) {
        this.bestFitness = bestFitness;
    }
}
