package com.company;

import java.util.*;

public class NaiveClassifier<T, K> {
    private final Dictionary<K, Integer> totalCategoryCount;
    private final Dictionary<K, Dictionary<T, Integer>> featureCountPerCategory;
    private final Dictionary<T, Integer> totalFeatureCount;

    public NaiveClassifier() {
        featureCountPerCategory = new Hashtable<>();
        totalFeatureCount = new Hashtable<>();
        totalCategoryCount = new Hashtable<>();
    }


    public void learn(ByesClass<T, K> ByesClass) {
        for (T feature : ByesClass.getFeatureSet())
            incrementFeature(feature, ByesClass.getCategory());
        incrementCategory(ByesClass.getCategory());
    }

    public ByesClass<T, K> classify(Collection<T> features) {
        SortedSet<ByesClass<T, K>> probabilities = categoryProbabilities(features);

        if (probabilities.size() > 0) {
            return probabilities.last();
        }
        return null;
    }

    public Set<K> getCategories() {
        return ((Hashtable<K, Integer>) totalCategoryCount).keySet();
    }

    public int getCategoriesTotal() {
        int toReturn = 0;
        for (Enumeration<Integer> e = totalCategoryCount.elements(); e.hasMoreElements(); ) {
            toReturn += e.nextElement();
        }
        return toReturn;
    }

    public void incrementFeature(T feature, K category) {
        Dictionary<T, Integer> features = featureCountPerCategory.get(category);
        if (features == null) {
            featureCountPerCategory.put(category,
                    new Hashtable<>());
            features = featureCountPerCategory.get(category);
        }
        Integer count = features.get(feature);
        if (count == null) {
            features.put(feature, 0);
            count = features.get(feature);
        }
        features.put(feature, ++count);

        Integer totalCount = totalFeatureCount.get(feature);
        if (totalCount == null) {
            totalFeatureCount.put(feature, 0);
            totalCount = totalFeatureCount.get(feature);
        }
        totalFeatureCount.put(feature, ++totalCount);
    }

    public void incrementCategory(K category) {
        Integer count = totalCategoryCount.get(category);
        if (count == null) {
            totalCategoryCount.put(category, 0);
            count = totalCategoryCount.get(category);
        }
        totalCategoryCount.put(category, ++count);
    }

    public int getCategoryCount(K category) {
        Integer count = totalCategoryCount.get(category);
        return (count == null) ? 0 : count;
    }

    public double featureProbability(T feature, K category) {
        final double currentFeatureTotalCount = (totalFeatureCount.get(feature) == null) ? 0 : totalFeatureCount.get(feature);

        if (currentFeatureTotalCount == 0) {
            return 0;
        } else {
            Dictionary<T, Integer> features = featureCountPerCategory.get(category);

            if (features == null) return 0;
            Integer count = features.get(feature);

            return (double) ((count == null) ? 0 : count) / currentFeatureTotalCount;
        }
    }

    public double featureWeighedAverage(T feature, K category) {
        Integer totals = totalFeatureCount.get(feature);
        if (totals == null) totals = 0;
        return (0.5 + totals * featureProbability(feature, category)) / (0.1 + totals);
    }

    private double featuresProbabilityProduct(Collection<T> features, K category) {
        double product = 1.0f;
        for (T feature : features)
            product *= featureWeighedAverage(feature, category);
        return product;
    }

    private double categoryProbability(Collection<T> features, K category) {
        return ((double) getCategoryCount(category) / (double) getCategoriesTotal()) * featuresProbabilityProduct(features, category);
    }

    private SortedSet<ByesClass<T, K>> categoryProbabilities(Collection<T> features) {
        SortedSet<ByesClass<T, K>> probabilities =
                new TreeSet<>(
                        (o1, o2) -> {
                            int toReturn = Double.compare(
                                    o1.getProbability(), o2.getProbability());
                            if ((toReturn == 0)
                                    && !o1.getCategory().equals(o2.getCategory()))
                                toReturn = -1;
                            return toReturn;
                        });

        for (K category : getCategories())
            probabilities.add(new ByesClass<>(
                    features, category,
                    categoryProbability(features, category)));
        return probabilities;
    }

}
