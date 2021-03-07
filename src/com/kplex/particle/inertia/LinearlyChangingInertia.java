package com.kplex.particle.inertia;

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
    public double calculate(int iterationCounter) {
        return MIN_INERTIA + (MAX_INERTIA - MIN_INERTIA) * (double) iterationCounter / (double) MAX_ITERATIONS;
    }
}
