/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.simple;

import org.geotools.feature.FeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Access to "simple" Feature content where each feature has the same SimpleFeatureType.
 *
 * <p>Please keep in mind that a SimpleFeatureCollection is similar to a result set; and may not
 * necessarily load everything in to memory. Treat each iterator as a forward only cursor in the
 * JDBC sense; and take care to FeatureIterator.close() after use.
 *
 * <h3>SimpleFeatureIterator close</h3>
 *
 * <p>SimpleFeatureCollection provides streaming access with the following restrictions on use of
 * {@link SimpleFeatureIterator}: You must call {@link SimpleFeatureIterator#close()}. This allows
 * FeatureCollection to clean up any operating system resources used to access information.
 *
 * <p>Example (safe) use:
 *
 * <pre><code>
 * SimpleFeatureIterator iterator = simpleFeatureCollection.features();
 * try {
 *     while( iterator.hasNext() ){
 *          SimpleFeature feature = iterator.next();
 *          System.out.println( feature.getID() );
 *     }
 * }
 * finally {
 *     iterator.close();
 * }
 * </code></pre>
 *
 * And in Java 7:
 *
 * <pre><code>
 * try ( SimpleFeatureIterator iterator = simpleFeatureCollection.features() ){
 *     while( iterator.hasNext() ){
 *          SimpleFeature feature = iterator.next();
 *          System.out.println( feature.getID() );
 *     }
 * }
 * </code></pre>
 *
 * <p>
 */
public interface SimpleFeatureCollection
        extends FeatureCollection<SimpleFeatureType, SimpleFeature> {
    /**
     * Obtain a SimpleFeatureIterator of the Features within this SimpleFeatureCollection.
     *
     * <p>The implementation of FeatureIterator must adhere to the rules of fail-fast concurrent
     * modification. In addition (to allow for resource backed collections) the <code>
     * SimpleFeatureIterator.close()</code> method must be called.
     *
     * <p>Example use:
     *
     * <pre><code>
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
     */
    public SimpleFeatureIterator features();

    public SimpleFeatureCollection subCollection(Filter filter);

    public SimpleFeatureCollection sort(SortBy order);
}
