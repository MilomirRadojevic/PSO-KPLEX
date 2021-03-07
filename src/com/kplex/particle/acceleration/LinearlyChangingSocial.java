package com.kplex.particle.acceleration;

import static com.kplex.config.Constants.*;

public class LinearlyChangingSocial implements ContinuousAcceleration {
    private static LinearlyChangingSocial INSTANCE;

    private LinearlyChangingSocial() {
        //
    }

    public static LinearlyChangingSocial getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LinearlyChangingSocial();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(int iterationCounter) {
        return INITIAL_SOCIAL + (FINAL_SOCIAL - INITIAL_SOCIAL) * (double) iterationCounter / (double) MAX_ITERATIONS;
    }
}
