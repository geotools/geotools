package org.geotools.data.simple;

import org.geotools.data.collection.DelegateFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/** @source $URL$ */
public class DelegateSimpleFeatureReader
        extends DelegateFeatureReader<SimpleFeatureType, SimpleFeature>
        implements SimpleFeatureReader {

    public DelegateSimpleFeatureReader(
            SimpleFeatureType featureType, SimpleFeatureIterator features) {
        super(featureType, features);
    }
}
