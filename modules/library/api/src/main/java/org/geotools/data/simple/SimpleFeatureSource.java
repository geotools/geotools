package org.geotools.data.simple;

import java.io.IOException;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

/**
 * FeatureSource explicitly working with SimpleFeatureCollection.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/api/src/main/java/org/geotools/data/simple/SimpleFeatureSource.java $
 */
public interface SimpleFeatureSource extends FeatureSource<SimpleFeatureType,SimpleFeature> {
    public SimpleFeatureCollection getFeatures() throws IOException;

    public SimpleFeatureCollection getFeatures(Filter filter)
            throws IOException;

    public SimpleFeatureCollection getFeatures(Query query)
            throws IOException;
}
