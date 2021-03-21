package com.kplex.particle.inertia;

import java.util.function.Supplier;

@FunctionalInterface
public interface Inertia {
    double calculate(double currentInertia, int iterationCounter, Supplier<Double> averageAbsoluteVelocitySupplier);
}
