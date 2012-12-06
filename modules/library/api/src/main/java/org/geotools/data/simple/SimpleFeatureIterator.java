package org.geotools.data.simple;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Streaming access to simple features, required to {@link #close()} after use.
 * <p>
 * This is an explicit interface for FeatureIterator<SimpleFeature>.
 * <p>
 * Sample use:<pre> SimpleFeatureIterator i = featureCollection.features()
 * try {
 *    while( i.hasNext() ){
 *        SimpleFeature feature = i.next();
 *    }
 * }
 * finally {
 *    i.close();
 * }
 * </pre>
 *
 * @author Jody Garnett
 * @source $URL$
 */
public interface SimpleFeatureIterator extends FeatureIterator<SimpleFeature> {

}
