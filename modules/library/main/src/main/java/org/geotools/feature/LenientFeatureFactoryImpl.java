package org.geotools.feature;

public class LenientFeatureFactoryImpl extends AbstractFeatureFactoryImpl {
    public LenientFeatureFactoryImpl() {
        validating = false;
    }
}
