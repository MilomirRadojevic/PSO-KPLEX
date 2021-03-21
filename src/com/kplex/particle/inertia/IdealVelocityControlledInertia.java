package com.kplex.particle.inertia;

import java.util.function.Supplier;

import static com.kplex.config.Constants.*;

public class IdealVelocityControlledInertia implements ContinuousInertia {
    private static IdealVelocityControlledInertia INSTANCE;

    private IdealVelocityControlledInertia() {
        //
    }

    public static IdealVelocityControlledInertia getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new IdealVelocityControlledInertia();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(double currentInertia, int iterationCounter, Supplier<Double> averageAbsoluteVelocitySupplier) {
        double idealVelocity = INITIAL_IDEAL_VELOCITY * (1.0 + Math.cos(iterationCounter * Math.PI / MAX_ITERATIONS)) / 2.0;

        double averageAbsoluteVelocity = averageAbsoluteVelocitySupplier.get();

        if (averageAbsoluteVelocity >= idealVelocity) {
            return Math.max(currentInertia - INERTIA_STEP, FINAL_INERTIA);
        } else {
            return Math.min(currentInertia + INERTIA_STEP, INITIAL_INERTIA);
        }
    }
}
