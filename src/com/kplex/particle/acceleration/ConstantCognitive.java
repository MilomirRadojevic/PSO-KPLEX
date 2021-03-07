package com.kplex.particle.acceleration;

import static com.kplex.config.Constants.COGNITIVE_FACTOR;

public class ConstantCognitive implements ContinuousAcceleration {
    private static ConstantCognitive INSTANCE;

    private ConstantCognitive() {
        //
    }

    public static ConstantCognitive getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConstantCognitive();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(int iterationCounter) {
        return COGNITIVE_FACTOR;
    }
}
