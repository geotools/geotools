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
 * Please keep in mind that a SimpleFeatureCollection is similar to a result set; and may not
 * necessarily load everything in to memory. Treat each iterator as a forward only cursor
 * in the JDBC sense; and take care to FeatureIterator.close() after use.
 * 
* <h3>SimpleFeatureIterator close</h3>
 * <p>
 * SimpleFeatureCollection provides streaming access with
 * the following restrictions on use of {@link SimpleFeatureIterator}:
 * You must call {@link SimpleFeatureIterator#close()}. This allows
 * FeatureCollection to clean up any operating system resources used
 * to access information.
 * </p>
 * <p>
 * Example (safe) use:<pre><code>
 * SimpleFeatureIterator iterator = simpleFeatureCollection.features();
 * try {
 *     while( iterator.hasNext() ){
 *          SimpleFeature feature = iterator.hasNext();
 *          System.out.println( feature.getID() );
 *     }
 * }
 * finally {
 *     iterator.close();
 * }
 * </code></pre>
 * </p>
 * And in Java 7:<pre><code>
 * try ( SimpleFeatureIterator iterator = simpleFeatureCollection.features() ){
 *     while( iterator.hasNext() ){
 *          SimpleFeature feature = iterator.hasNext();
 *          System.out.println( feature.getID() );
 *     }
 * }
 * </code></pre>
 * <p>
 * 
 * @source $URL$
 */
public interface SimpleFeatureCollection extends FeatureCollection<SimpleFeatureType,SimpleFeature>{
    /**
     * Obtain a SimpleFeatureIterator of the Features within this SimpleFeatureCollection.
     * <p>
     * The implementation of FeatureIterator must adhere to the rules of
     * fail-fast concurrent modification. In addition (to allow for
     * resource backed collections) the <code>SimpleFeatureIterator.close()</code>
     * method must be called.
     * <p>
     * Example use:<pre><code>
     * SimpleFeatureIterator iterator=collection.features();
     * try {
     *     while( iterator.hasNext()  ){
     *          SimpleFeature feature = iterator.next();
     *          System.out.println( feature.getID() );
     *     }
     * }
     * finally {
     *     iterator.close();
     * }
     * </code></pre>
     * </p>
     */
    public SimpleFeatureIterator features();
    
    public SimpleFeatureCollection subCollection(Filter filter);
    
    public SimpleFeatureCollection sort(SortBy order);
   
}
