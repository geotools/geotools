package org.geotools.process.classify;

/** Enumeration for method of classifying numeric values into ranges (classes). */
public enum ClassificationMethod {
    /** Classifies data into equally sized ranges. */
    EQUAL_INTERVAL,

    /**
     * Classifies data into ranges such that the number of values falling into each range is
     * approximately the same.
     */
    QUANTILE,

    /** Classifies data into ranges such that ranges correspond to "clusters" of values. */
    NATURAL_BREAKS;
}
