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
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.util.Set;

import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;


/**
 * Highlevel API for Features from a specific location.
 *
 * <p>
 * Individual Shapefiles, databases tables , etc. are referenced through this
 * interface. Compare and constrast with DataStore.
 * </p>
 *
 * <p>
 * Differences from DataStore:
 * </p>
 *
 * <ul>
 * <li>
 * This is a prototype DataSource replacement, the Transaction methods have
 * been moved to an external object, and the locking api has been intergrated.
 * </li>
 * <li>
 * FeatureCollection<SimpleFeatureType, SimpleFeature> has been replaced with FeatureResult as we do not wish to
 * indicate that results can be stored in memory.
 * </li>
 * <li>
 * FeatureSource<SimpleFeatureType, SimpleFeature> has been split into three interfaces, the intention is to use
 * the instanceof opperator to check capabilities rather than the previous
 * DataSourceMetaData.
 * </li>
 * </ul>
 *
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
     * Returns the qualified name for the Features this FeatureSource serves.
     * <p>
     * Note this is different from {@code getSchema().getType().getName()} (that
     * is, the feature type name), this name specifies the
     * {@link PropertyDescriptor#getName() AttributeDescriptor name} for the
     * Features served by this source. So,
     * {@code FeatureSoruce.getName() ==  FeatureSource.getFeatures().next().getAttributeDescriptor().getName()}.
     * </p>
     * <p>
     * Though it's a common practice when dealing with {@link SimpleFeatureType}
     * and {@link SimpleFeature} to assume they're equal. There's no conflict
     * (as per the dynamic typing system the {@code org.opengis.feature} package
     * defines) in a Feature and its type sharing the same name, as well as in a
     * GML schema an element declaration and a type definition may be named the
     * same. Yet, the distinction becomes important as we get rid of that
     * assumption and thus allow to reuse a type definition for different
     * FeatureSoruces, decoupling the descriptor (homologous to the Feature
     * element declaration in a GML schema) from its type definition.
     * </p>
     * <p>
     * So, even if implementors are allowed to delegate to
     * {@code getSchema().getName()} if they want to call the fatures and their
     * type the same, client code asking a
     * {@link DataAccess#getFeatureSource(Name)} shall use this name to request
     * for a FeatureSource, rather than the type name, as used in pre 2.5
     * versions of GeoTools. For example, if we have a FeatureSource named
     * {@code Roads} and its type is named {@code Roads_Type}, the
     * {@code DataAccess} shall be queried through {@code Roads}, not
     * {@code Roads_Type}.
     * </p>
     * 
     * @since 2.5
     * @return the name of the AttributeDescriptor for the Features served by
     *         this FeatureSource
     */
    Name getName();
    
    /**
     * Information describing the contents of this resoruce.
     * <p>
     * Please note that for FeatureContent:
     * <ul>
     * <li>name - unqiue with in the context of a Service
     * <li>schema - used to identify the type of resource; usually gml schema; although it may be more specific
     * <ul>
     */
    ResourceInfo getInfo();
    
    /**
     * Access to the DataStore implementing this FeatureStore.
     *
     * @return DataStore implementing this FeatureStore
     */
    DataAccess<T, F> getDataStore();

    /**
     * Returns and indication of what query capabilities this FeatureSource
     * supports natively.
     * 
     * @return a QueryCapabilities object containing the native query capabilities.
     * @since 2.5
     */
    QueryCapabilities getQueryCapabilities();
    
    /**
     * Adds a listener to the list that's notified each time a change to the
     * FeatureStore occurs.
     *
     * @param listener FeatureListener
     */
    void addFeatureListener(FeatureListener listener);

    /**
     * Removes a listener from the list that's notified each time a change to
     * the FeatureStore occurs.
     *
     * @param listener FeatureListener
     */
    void removeFeatureListener(FeatureListener listener);

    /**
     * Loads features from the datasource into the returned FeatureResults,
     * based on the passed query.
     *
     * @param query a datasource query object.  It encapsulates requested
     *        information, such as typeName, maxFeatures and filter.
     *
     * @return Collection The collection to put the features into.
     *
     * @throws IOException For all data source errors.
     *
     * @see Query
     */
    FeatureCollection<T, F> getFeatures(Query query) throws IOException;

    /**
     * Loads features from the datasource into the returned FeatureResults,
     * based on the passed filter.
     *
     * @param filter An OpenGIS filter; specifies which features to retrieve.
     *        <tt>null</tt> is not allowed, use Filter.INCLUDE instead.
     *
     * @return Collection The collection to put the features into.
     *
     * @throws IOException For all data source errors.
     */
    FeatureCollection<T, F> getFeatures(Filter filter) throws IOException;

    /**
     * Loads all features from the datasource into the return FeatureResults.
     *
     * <p>
     * Filter.INCLUDE can also be used to get all features.  Calling this function
     * is equivalent to using {@link Query#ALL}
     * </p>
     *
     * @return Collection The collection to put the features into.
     *
     * @throws IOException For all data source errors.
     */
    FeatureCollection<T, F> getFeatures() throws IOException;

    /**
     * Retrieves the featureType that features extracted from this datasource
     * will be created with.
     *
     * <p>
     * The schema returned is the LCD supported by all available Features. In
     * the common case of shapfiles and database table this schema will match
     * that of every feature available. In the degenerate GML case this will
     * simply reflect the gml:AbstractFeatureType.
     * </p>
     *
     * @return the schema of features created by this datasource.
     *
     * @task REVISIT: Our current FeatureType model is not yet advanced enough
     *       to handle multiple featureTypes.  Should getSchema take a
     *       typeName now that a query takes a typeName, and thus DataSources
     *       can now support multiple types? Or just wait until we can
     *       programmatically make powerful enough schemas?
     * @task REVISIT: we could also just use DataStore to capture multi
     *       FeatureTypes?
     */
    T getSchema();

    /**
     * Gets the bounding box of this datasource.
     *
     * <p>
     * With getBounds(Query) this becomes a convenience method for
     * getBounds(Query.ALL), that is the bounds for all features contained
     * here.
     * </p>
     *
     * <p>
     * If getBounds() returns <code>null</code> due to expense consider using
     * <code>getFeatures().getBounds()</code> as a an alternative.
     * </p>
     *
     * @return The bounding box of the datasource or null if unknown and too
     *         expensive for the method to calculate.
     *
     * @throws IOException if there are errors getting the bounding box.
     *
     * @task REVISIT: Do we need this or can we use getBounds( Query.ALL )?
     */
    ReferencedEnvelope getBounds() throws IOException;

    /**
     * Gets the bounding box of the features that would be returned by this
     * query.
     *
     * <p>
     * To retrieve the bounds of the DataSource please use <code>getBounds(
     * Query.ALL )</code>.
     * </p>
     *
     * <p>
     * This method is needed if we are to stream features to a gml out, since a
     * FeatureCollection<SimpleFeatureType, SimpleFeature> must have a boundedBy element.
     * </p>
     *
     * <p>
     * If getBounds(Query) returns <code>null</code> due to expense consider
     * using <code>getFeatures(Query).getBounds()</code> as a an alternative.
     * </p>
     *
     * @param query Contains the Filter, and optionally MaxFeatures and StartIndex to 
     *  find the bounds for.
     *
     * @return The bounding box of the datasource or null if unknown and too
     *         expensive for the method to calculate or any errors occur.
     *
     * @throws IOException DOCUMENT ME!
     */
    ReferencedEnvelope getBounds(Query query) throws IOException;

    /**
     * Gets the number of the features that would be returned by this query.
     * 
     * <p>
     * If getBounds(Query) returns <code>-1</code> due to expense consider
     * using <code>getFeatures(Query).getCount()</code> as a an alternative.
     * </p>
     * <p>
     * This method should take into account the Query's {@link Query#getMaxFeatures() maxFeatures}
     * and {@link Query#getStartIndex() startIndex}, if present, in order to 
     * consistently return the number of features the query would return.
     * </p>
     *
     * @param query Contains the Filter, and optionally MaxFeatures and StartIndex to 
     *  find the count for.
     *
     * @return The number of Features provided by the Query or <code>-1</code>
     *         if count is too expensive to calculate or any errors or occur.
     *
     * @throws IOException if there are errors getting the count
     */
    int getCount(Query query) throws IOException;

    /**
     * Returns the set of hints this {@link FeatureSource} is able to support.<p>
     * Hints are to be specified in the {@link Query}, for each data access where they
     * may be required.<br>
     * Depending on the actual value provide by the user, the {@link FeatureSource}
     * may decide not to honor the hint.
     * @return a set of {@link RenderingHints#Key} objects (eventually empty, never null).
     */
    public Set<RenderingHints.Key> getSupportedHints();
    
    // FeatureReader getFeatureReader( Query query ); // ask justin for proposal
}
