package com.kplex.util;

import java.util.List;
import java.util.stream.IntStream;

import static com.kplex.config.Constants.BOUNDARY_MAX;
import static com.kplex.config.Constants.BOUNDARY_MIN;

public class Utils {
    public static double boxed(double original) {
        return boxed(original, BOUNDARY_MIN, BOUNDARY_MAX);
    }

    public static double boxed(double original, double min, double max) {
        return Math.min(Math.max(original, min), max);
    }

    public static double euclideanDistance(List<Double> first, List<Double> second) {
        if (first.size() != second.size()) {
            throw new RuntimeException();
        }

        return Math.sqrt(
                IntStream.range(0, first.size())
                        .mapToDouble(value -> Math.pow(first.get(value) - second.get(value), 2))
                        .sum()
        );
    }
}
