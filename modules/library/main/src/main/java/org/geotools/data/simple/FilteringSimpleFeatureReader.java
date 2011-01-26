package org.geotools.data.simple;

import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Filter simple reader content as it is being read.
 */
public class FilteringSimpleFeatureReader extends
        FilteringFeatureReader<SimpleFeatureType, SimpleFeature> implements SimpleFeatureReader {

    public FilteringSimpleFeatureReader(SimpleFeatureReader featureReader, Filter filter) {
        super(featureReader, filter);

    }

}
