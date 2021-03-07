package com.kplex.particle.acceleration;

@FunctionalInterface
public interface Acceleration {
    double calculate(int iterationCounter);
}
