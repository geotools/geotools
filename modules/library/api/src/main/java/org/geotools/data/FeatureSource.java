/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.Set;

import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;


/**
 * This class provides a high-level API for operations on feature data. Typically,
 * when working with a data source such as a shapefile or database table you will
 * initially create a {@code DataStore} object to connect
 * to the physical source and then retrieve a {@code FeatureSource} to work with
 * the feature data, as in this excerpt from the GeoTools Quickstart example
 * (<a href="http://geotools.org/quickstart.html">http://geotools.org/quickstart.html</a>)
 * <pre><code>
 *     File file = ...
 *     FileDataStore store = FileDataStoreFinder.getDataStore(file);
 *     FeatureSource featureSource = store.getFeatureSource();
 * </code></pre>
 * @see DataStore
 *
 * @author Jody Garnett
 * @author Ray Gallagher
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 */
public interface FeatureSource<T extends FeatureType, F extends Feature>{
    
    /**
     * Returns the name of the features (strictly, the name of the 
     * {@code AttributeDescriptor} for the features) accessible through this
     * {@code FeatureSource}.
     * <p>
     * The value returned by this method can be different to that returned by
     * {@code featureSource.getSchema().getType().getName()}.
     * This is because there is a distinction between the name applied to features
     * and the name of a feature type. When working with {@code SimpleFeature} and
     * {@code SimpleFeatureType}, for example with a shapefile data source, it is
     * common practice for feature and feature type names to be the same. However, this
     * is not the case more generally. For instance, a database can contain two
     * tables with the same structure. The feature name will refer to the table
     * while the feature type name refers to the schema (table structure).
     *
     * @since 2.5
     * @return the name of the features accessible through this {@code FeatureSource}
     */
    Name getName();
    
    /**
     * Returns information describing this {@code FeatureSource} which may
     * include title, description and spatial parameters. Note that in the
     * returned {@code ResourceInfo} object, the distinction between feature
     * name and schema (feature type) name applies as discussed for
     * {@linkplain #getName()}.
     */
    ResourceInfo getInfo();
    
    /**
     * Returns the data source, as a {@code DataAccess} object, providing
     * this {@code FeatureSource}.
     *
     * @return the data source providing this {@code FeatureSource}
     */
    DataAccess<T, F> getDataStore();

    /**
     * Enquire what what query capabilities this {@code FeatureSource}
     * natively supports. For example, whether queries can return sorted
     * results.
     * 
     * @return the native query capabilities of this {@code FeatureSource}
     * @since 2.5
     */
    QueryCapabilities getQueryCapabilities();
    
    /**
     * Registers a listening object that will be notified of changes to this
     * {@code FeatureSource}.
     *
     * @param listener the new listener
     */
    void addFeatureListener(FeatureListener listener);

    /**
     * Removes an object from this {@code FeatureSource's} listeners.
     *
     * @param listener the listener to remove
     */
    void removeFeatureListener(FeatureListener listener);

    /**
     * Retrieves features, in the form of a {@code FeatureCollection}, based
     * on an OGC {@code Filter}.
     *
     * @param filter the filter to select features; must not be {@code null}
     *        (use {@linkplain Filter#INCLUDE} instead)
     *
     * @return features retrieved by the {@code Filter}
     *
     * @throws IOException if the underlying data source cannot be accessed.
     *
     * @see Filter
     */
    FeatureCollection<T, F> getFeatures(Filter filter) throws IOException;

    /**
     * Retrieves features, in the form of a {@code FeatureCollection}, based
     * on a {@code Query}.
     *
     * @param query DataAccess query for requested information, such as typeName,
     *        maxFeatures and filter.
     *
     * @return features retrieved by the {@code Query}
     *
     * @throws IOException if the underlying data source cannot be accessed.
     *
     * @see Query
     */
    FeatureCollection<T, F> getFeatures(Query query) throws IOException;

    /**
     * Retrieves all features in the form of a {@code FeatureCollection}.
     * <p>
     * The following statements are equivalent:
     * <pre><code>
     *     featureSource.getFeatures();
     *     featureSource.getFeatures(Filter.INCLUDE);
     *     featureSource.getFeatures(Query.ALL);
     * </code></pre>
     *
     * @return features retrieved by the {@code Query}
     *
     * @throws IOException if the underlying data source cannot be accessed.
     */
    FeatureCollection<T, F> getFeatures() throws IOException;

    /**
     * Retrieves the schema (feature type) that will apply to features retrieved
     * from this {@code FeatureSource}.
     * <p>
     * For a homogeneous data source such as a shapefile or a database table,
     * this schema be that of all features. For a heterogeneous data source,
     * e.g. a GML document, the schema returned is the lowest common denominator
     * across all features.
     *
     * @return the schema that will apply to features retrieved from this
     *         {@code FeatureSource}
     */
    T getSchema();

    /**
     * Get the spatial bounds of the feature data. This is equivalent
     * to calling <code>getBounds(Query.ALL)</code>.
     * <p>
     * It is possible that this method will return null if the calculation
     * of bounds is judged to be too costly by the implementing class. 
     * In this case, you might call <code>getFeatures().getBounds()</code>
     * instead.
     *
     * @return The bounding envelope of the feature data; or {@code null}
     *         if the bounds are unknown or too costly to calculate.
     *
     * @throws IOException on any errors calculating the bounds
     */
    ReferencedEnvelope getBounds() throws IOException;

    /**
     * Get the spatial bounds of the features that would be returned by
     * the given {@code Query}.
     * <p>
     * It is possible that this method will return null if the calculation
     * of bounds is judged to be too costly by the implementing class.
     * In this case, you might call <code>getFeatures(query).getBounds()</code>
     * instead.
     *
     * @param query the query to select features
     *
     * @return The bounding envelope of the feature data; or {@code null}
     *         if the bounds are unknown or too costly to calculate.
     *
     * @throws IOException on any errors calculating the bounds
     */
    ReferencedEnvelope getBounds(Query query) throws IOException;

    /**
     * Gets the number of the features that would be returned by the given
     * {@code Query}, taking into account any settings for max features and
     * start index set on the {@code Query}.
     * <p>
     * It is possible that this method will return {@code -1} if the calculation
     * of number of features is judged to be too costly by the implementing class.
     * In this case, you might call <code>getFeatures(query).getBounds()</code>
     * instead.
     *
     * @param query the query to select features
     *
     * @return the numer of features that would be returned by the {@code Query};
     *         or {@code -1} if this cannot be calculated.
     *
     * @throws IOException if there are errors getting the count
     */
    int getCount(Query query) throws IOException;

    /**
     * Returns the set of hints that this {@code FeatureSource} supports via {@code Query} requests.
     * <p>
     * Note: the existence of a specific hint does not guarantee that it will always be honored by
     * the implementing class.
     * 
     * @see Hints#FEATURE_DETACHED
     * @see Hints#JTS_GEOMETRY_FACTORY
     * @see Hints#JTS_COORDINATE_SEQUENCE_FACTORY
     * @see Hints#JTS_PRECISION_MODEL
     * @see Hints#JTS_SRID
     * @see Hints#GEOMETRY_DISTANCE
     * @see Hints#FEATURE_2D
     * @return a set of {@code RenderingHints#Key} objects; may be empty but never {@code null}
     */
    public Set<RenderingHints.Key> getSupportedHints();
    
    // FeatureReader getFeatureReader( Query query ); // ask justin for proposal
}
