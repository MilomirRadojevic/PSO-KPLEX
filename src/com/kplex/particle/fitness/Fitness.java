package com.kplex.particle.fitness;


import com.kplex.particle.Particle;

@FunctionalInterface
public interface Fitness<T, U> {
    double calculate(Particle<T, U> particle);
}
