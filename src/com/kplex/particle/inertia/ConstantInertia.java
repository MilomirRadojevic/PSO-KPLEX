package com.kplex.particle.inertia;

import java.util.function.Supplier;

import static com.kplex.config.Constants.INERTIA_FACTOR;

public class ConstantInertia implements ContinuousInertia {
    private static ConstantInertia INSTANCE;

    private ConstantInertia() {
        //
    }

    public static ConstantInertia getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConstantInertia();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(double currentInertia, int iterationCounter, Supplier<Double> averageAbsoluteVelocitySupplier) {
        return INERTIA_FACTOR;
    }
}
