package com.kplex.particle.inertia;

import java.util.function.Supplier;

import static com.kplex.config.Constants.*;

public class LinearlyChangingInertia implements ContinuousInertia {
    private static LinearlyChangingInertia INSTANCE;

    private LinearlyChangingInertia() {
        //
    }

    public static LinearlyChangingInertia getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LinearlyChangingInertia();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(double currentInertia, int iterationCounter, Supplier<Double> averageAbsoluteVelocitySupplier) {
        return INITIAL_INERTIA + (FINAL_INERTIA - INITIAL_INERTIA) * (double) iterationCounter / (double) MAX_ITERATIONS;
    }
}
