package org.geotools.data.simple;

import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Access to "simple" Feature content where each feature has the same SimpleFeatureType.
 * <p>
 * Please keep in mind that a FeatureCollection is similar to a result set; and may not
 * necessarily load everything in to memory. Treat each iterator as a forward only cursor
 * in the JDBC sense; and take care to close iterators when not in use.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/api/src/main/java/org/geotools/data/simple/SimpleFeatureCollection.java $
 */
public interface SimpleFeatureCollection extends FeatureCollection<SimpleFeatureType,SimpleFeature>{
	
    public SimpleFeatureIterator features();
    
    public SimpleFeatureCollection subCollection(Filter filter);
    
    public SimpleFeatureCollection sort(SortBy order);
   
}
