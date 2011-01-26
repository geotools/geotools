package org.geotools.data.simple;

import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * SimpleFeatureReader with no content.
 */
public class EmptySimpleFeatureReader extends EmptyFeatureReader<SimpleFeatureType, SimpleFeature> implements SimpleFeatureReader{

    public EmptySimpleFeatureReader(SimpleFeatureType featureType) {
        super(featureType);        
    }

}
