package org.geotools.data.simple;

import org.geotools.data.DelegatingFeatureWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * 
 *
 * @source $URL$
 */
public interface DelegatingSimpleFeatureWriter extends DelegatingFeatureWriter<SimpleFeatureType, SimpleFeature>,
        SimpleFeatureWriter {

    public SimpleFeatureWriter getDelegate();
}
