package org.geotools.feature;

/**
 * 
 *
 * @source $URL$
 */
public class LenientFeatureFactoryImpl extends AbstractFeatureFactoryImpl {
    public LenientFeatureFactoryImpl() {
        validating = false;
    }
}
