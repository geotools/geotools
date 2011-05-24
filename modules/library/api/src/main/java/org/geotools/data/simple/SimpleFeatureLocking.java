package org.geotools.data.simple;

import org.geotools.data.FeatureLocking;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Used to lock feature content to protect against other threads (or depending on the source of data other applications)
 * making modifications when you are not looking.
 * <p>
 * The locks operate more as a lease for a specific period of time. In effect you are only
 * locking for a set time period; so even if your application or machine crashes the lock
 * will eventually be released allowing others to play.
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/api/src/main/java/org/geotools/data/simple/SimpleFeatureLocking.java $
 */
public interface SimpleFeatureLocking extends SimpleFeatureStore, FeatureLocking<SimpleFeatureType, SimpleFeature> {
    
}
