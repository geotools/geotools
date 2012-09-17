/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.geotools.data.FeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;


/**
 * Collection of features, often handled as a result set.
 * <p>
 * Where possible FeatureCollection is method compatible with {@link Collection}.
 * In keeping with the rules set for by {@link Collection}, some methods are
 * optional, and may throw an UnsupportedOperationException.
 * </p>
 * <p>
 * SimpleFeatureCollection house rules:
 * <ul>
 * <li>Each iterator is considered a connection which my be closed (see example below)</li>
 * <li>Features are not specifically ordered within the SimpleFeatureCollection</li>
 * <li>Two instances cannot exist with the same {@link FeatureId}</li>
 * </ul>
 * </p>
 * <p>
 * <h3>FeatureIterator close</h3>
 * <p>
 * FeatureCollection provides streaming access. With this in mind we have
 * a restriction on the use of {@link FeatureIterator}: You must call
 * {@link FeatureIterator#close()}. This allows FeatureCollection
 * to clean up any operating system resources used to access information.
 * </p>
 * <p>
 * Example (safe) use:<pre><code>
 * FeatureIterator iterator = collection.features();
 * try {
 *     while( iterator.hasNext() ){
 *          Feature feature = iterator.hasNext();
 *          System.out.println( feature.getID() );
 *     }
 * }
 * finally {
 *     iterator.close();
 * }
 * </code></pre>
 * </p>
 * <p>
 * Handy Tip: Although many resource backed collections will choose
 * to release resources at when the iterator has reached the end of its contents
 * this is not something you should rely on.
 * </p>
 * <h2>FeatureCollection Implementation Tips</h2>
 * <p>
 * Try and close up resources when you can detect that an Iterator is no
 * longer in use.
 * </p>
 * <p>
 * FeatureCollection is used in two fashions, as a result set, where each iterator acts
 * as a cursor over the content. Also as a predefined query which can be refined
 * further. An example is using featureCollection.subCollection( Filter ) or
 * featureCollection.sort( SortBy ) before listing features out of a FeatureCollection.
 * </p>
 *
 * @see org.geotools.Feature
 * @author Ian Turton, CCG
 * @author Rob Hranac, VFNY
 * @author Ian Schneider, USDA-ARS
 * @author Jody Garnett, Refractions Research, Inc.
 *
 * @source $URL$
 * @version $Id$
 */
public interface FeatureCollection<T extends FeatureType, F extends Feature> {
    /**
     * Obtain a FeatureIterator<SimpleFeature> of the Features within this collection.
     * <p>
     * The implementation of Collection must adhere to the rules of
     * fail-fast concurrent modification. In addition (to allow for
     * resource backed collections, the <code>close( Iterator )</code>
     * method must be called.
     * <p>
     *
     * This is almost equivalent to:
     * <ul>
     * <li>a Type-Safe call to:
     * <code>getAttribute(getFeatureType().getAttributeType(0).getName()).iterator();</code>.
     * <li>A Java 5:<code>Iterator&lt;Feature&gt;</code>
     * </ul>
     * </p>
     * Example (safe) use:<pre><code>
     * FeatureIterator<SimpleFeature> iterator=collection.features();
     * try {
     *     while( iterator.hasNext()  ){
     *          Feature feature = iterator.next();
     *          System.out.println( feature.getID() );
     *     }
     * }
     * finally {
     *     collection.close( iterator );
     * }
     * </code></pre>
     * </p>
     *
     * <p>
     * GML Note: The contents of this iterator are considered to be defined by
     * <b>featureMember</b> tags (and/or the single allowed <b>FeatureMembers</b> tag).
     * Please see getFeatureType for more details.
     * </p>
     *
     * @return A FeatureIterator.
     */
    FeatureIterator<F> features();

    /**
     * The schema for the child feature members of this collection.
     * <p>
     * Represents the most general FeatureType in common to all the features in this
     * collection.
     * <ul>
     * <li>For a collection backed by a shapefiles (or database tables) the FeatureType returned by getSchema() will
     * complete describe each and every child in the collection.
     * <li>For mixed content FeatureCollections you will need to check the FeatureType of each Feature as it
     * is retrived from the collection
     * <li>The degenerate case returns the "_Feature" FeatureType, where the
     * only thing known is that the contents are Features.
     * </ul>
     * </p>
     * @return FeatureType describing the "common" schema to all child features of this collection
     */
    T getSchema();

    
    /**
     * ID used when serializing to GML
     */
    String getID();
    
    /**
     * Visit the contents of a feature collection.
     * <p>
     * The order of traversal is dependent on the FeatureCollection implementation; some
     * collections are able to make efficient use of an internal index in order to quickly
     * visit features located in the same region.
     * </p>
     * 
     * @param visitor Closure applied to each feature in turn.
     * @param progress Used to report progress, may be used to interrupt the operation
     *
     * @since 2.5
     */
    void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException;

    /**
     * SimpleFeatureCollection "view" indicated by provided filter.
     * <p>
     * The contents of the returned SimpleFeatureCollection are determined by
     * applying the provider Filter to the entire contents of this
     * FeatureCollection. The result is "live" and modifications will
     * be shared.
     * <p>
     * This method is used cut down on the number of filter based methods
     * required for a useful SimpleFeatureCollection construct. The FeatureCollections
     * returned really should be considered as a temporary "view" used to
     * control the range of a removeAll, or modify operation.
     * <p>
     * Example Use:
     * <pre><code>
     * collection.subCollection( filter ).clear();
     * </code></pre>
     * The above recommended use is agreement with the Collections API precident of
     * List.subList( start, end ).
     * <p>
     * The results of subCollection:
     * <ul>
     * <li>are to be considered unordered
     * <li>may be an ordered FeatureList if requested when sortBy is indicated
     * </ul>
     * </p>
     * @see FeatureList
     * @param filter
     * @return SimpleFeatureCollection identified as subset.
     */
    public FeatureCollection<T, F> subCollection(Filter filter);

    /**
     * Obtained sorted contents.
     * <p>
     * This method may not be supported by all implementations, consider
     * the use of FeatureSource.features( Query ).
     * 
     * @param order Sort order
     * @return FeatureCollection sorted in the indicated order
     */
    public FeatureCollection<T, F> sort(SortBy order);

    /**
     * Get the total bounds of this collection which is calculated by doing a
     * union of the bounds of each feature inside of it
     *
     * @return An Envelope containing the total bounds of this collection.
     */
    ReferencedEnvelope getBounds();
    
    //
    // ResourceCollection methods
    //   
    /**
     * @see java.util.Collection#contains(Object)
     */
    boolean contains(Object o);
    
    /**
     * @see java.util.Collection#containsAll(Collection)
     */
    boolean containsAll(Collection<?> o);

    /**
     * Returns <tt>true</tt> if this feature collection contains no features.
     *
     * @return <tt>true</tt> if this collection contains no features
     */
    boolean isEmpty();
    
    /**
     * Please note this operation may be expensive when working with remote content.
     * 
     * @see java.util.Collection#size()
     */
    int size();
    
    /** @see java.util.Collection#toArray() */    
    Object[] toArray();
    
    /** @see java.util.Collection#toArray(Object[]) */ 
    <O> O[] toArray(O[] a);    
}
