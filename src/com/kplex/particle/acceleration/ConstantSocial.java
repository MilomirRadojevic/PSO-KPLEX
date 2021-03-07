package com.kplex.particle.acceleration;

import static com.kplex.config.Constants.SOCIAL_FACTOR;

public class ConstantSocial implements ContinuousAcceleration {
    private static ConstantSocial INSTANCE;

    private ConstantSocial() {
        //
    }

    public static ConstantSocial getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConstantSocial();
        }

        return INSTANCE;
    }

    @Override
    public double calculate(int iterationCounter) {
        return SOCIAL_FACTOR;
    }
}
