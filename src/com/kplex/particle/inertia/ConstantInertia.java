package com.kplex.particle.inertia;

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
    public double calculate(int iterationCounter) {
        return INERTIA_FACTOR;
    }
}
