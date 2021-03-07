package com.kplex.particle.acceleration;

import static com.kplex.config.Constants.*;

public class LinearlyChangingCognitive implements ContinuousAcceleration {
    private static LinearlyChangingCognitive INSTANCE;

    private LinearlyChangingCognitive() {
        //
    }

    public static LinearlyChangingCognitive getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LinearlyChangingCognitive();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(int iterationCounter) {
        return INITIAL_COGNITIVE + (FINAL_COGNITIVE - INITIAL_COGNITIVE) * (double) iterationCounter / (double) MAX_ITERATIONS;
    }
}
