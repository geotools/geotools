package org.geotools.data.simple;

import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * Filter simple reader content as it is being read.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/data/simple/FilteringSimpleFeatureReader.java $
 */
public class FilteringSimpleFeatureReader extends
        FilteringFeatureReader<SimpleFeatureType, SimpleFeature> implements SimpleFeatureReader {

    public FilteringSimpleFeatureReader(SimpleFeatureReader featureReader, Filter filter) {
        super(featureReader, filter);

    }

}
