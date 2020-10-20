package com.company;

import java.util.Arrays;
import java.util.Collection;

public class ByesClass<T, K> {

    private final Collection<T> featureSet;

    private final K category;

    private final double probability;

    //Laplace'a because basic a == probability == 1
    public ByesClass(Collection<T> featureSet, K category) {
        this(featureSet, category, 1.0);
    }

    public ByesClass(Collection<T> featureSet, K category, double probability) {
        this.featureSet = featureSet;
        this.category = category;
        this.probability = probability;
    }

    public Collection<T> getFeatureSet() {
        return featureSet;
    }

    public double getProbability() {
        return probability;
    }

    public K getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "category=" + category +
                ", probability=" + probability +
                ", featureSet=" + Arrays.toString(featureSet.toArray());
    }
}
