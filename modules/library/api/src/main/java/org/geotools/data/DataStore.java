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

import java.io.IOException;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.geotools.data.simple.SimpleFeatureSource;


/**
 * This represents a physical source of feature data, such as a shapefiles or
 * database, where the features will be instances of {@code SimpleFeature}.
 * It is derived from the {@code DataAccess} interface (which works at the more
 * general {@code Feature} level.
 *
 * @see DataAccess
 * @see org.opengis.feature.Feature
 * @see SimpleFeature
 *
 * @author Jody Garnett, Refractions Research
 *
 * @source $URL$
 * @version $Id$
 */
public interface DataStore extends DataAccess<SimpleFeatureType, SimpleFeature>{
   
    /**
     * Applies a new schema to the given feature type. This can be used
     * to add or remove properties. The resulting update will be persistent.
     *
     * @param typeName name of the feature type to update
     *
     * @param featureType the new schema to apply
     *
     * @throws IOException on error
     */
    void updateSchema(String typeName, SimpleFeatureType featureType)
        throws IOException;

    /**
     * Gets the names of feature types available in this {@code DataStore}.
     * Please note that this is not guaranteed to return a list of unique
     * names since the same unqualified name may be present in separate
     * namespaces within the {@code DataStore}.
     *
     * @return names of feature types available in this {@code DataStore}
     *
     * @throws IOException if data access errors occur
     */
    String[] getTypeNames() throws IOException;
    
    /**
     * Gets the type information (schema) for the specified feature type.
     *
     * @param typeName the feature type name
     *
     * @return the requested feature type
     *
     * @throws IOException if {@code typeName} is not available
     */
    SimpleFeatureType getSchema(String typeName) throws IOException;

    /**
     * Gets a {@code SimpleFeatureSource} for features of the specified
     * type. {@code SimpleFeatureSource} provides a high-level API for
     * feature operations.
     * <p>
     * The resulting {@code SimpleFeatureSource} may implment more functionality
     * as in this example:
     * <pre><code>
     *
     * SimpleFeatureSource fsource = dataStore.getFeatureSource("roads");
     * if (fsource instanceof SimpleFeatureStore) {
     *     // we have write access to the feature data
     *     SimpleFeatureStore fstore = (SimpleFeatureStore) fs;
     * }
     * else {
     *     System.out.println("We do not have write access to roads");
     * }
     * </code></pre>
     *
     * @param typeName the feature type
     *
     * @return a {@code SimpleFeatureSource} (or possibly a subclass) providing
     *         operations for features of the specified type
     *
     * @throws IOException if data access errors occur
     *
     * @see SimpleFeatureSource
     * @see org.geotools.data.simple.SimpleFeatureStore
     */
    SimpleFeatureSource getFeatureSource(String typeName) throws IOException;
    
    /**
     * Gets a {@code SimpleFeatureSource} for features of the type
     * specified by a qualified name (namespace plus type name).
     *
     * @param typeName the qualified name of the feature type
     *
     * @return a {@code SimpleFeatureSource} (or possibly a subclass) providing
     *         operations for features of the specified type
     *
     * @throws IOException if data access errors occur
     *
     * @see #getFeatureSource(String)
     * @see SimpleFeatureSource
     * @see org.geotools.data.simple.SimpleFeatureStore
     */
    SimpleFeatureSource getFeatureSource(Name typeName) throws IOException;
    

    /**
     * Gets a {@code FeatureReader} for features selected by the given
     * {@code Query}.  {@code FeatureReader} provies an iterator-style
     * API to feature data.
     * <p>
     * The {@code Query} provides the schema for the form of the returned
     * features as well as a {@code Filter} to constrain the features
     * available via the reader.
     * <p>
     * The {@code Transaction} can be used to externalize the state of the
     * {@code DataStore}. Examples of this include a {@code JDBCDataStore}
     * sharing a connection for use across several {@code FeatureReader} requests;
     * and a {@code ShapefileDataStore} redirecting requests to an alternate file
     * during the course of a {@code Transaction}.
     *
     * @param query a query providing the schema and constraints for
     *        features that the reader will return
     *
     * @param transaction a transaction that this reader will operate against
     *
     * @throws IOException if data access errors occur
     *
     * @return an instance of {@code FeatureReader}
     */
    FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            Transaction transaction) throws IOException;

    /**
     * Gets a {@code FeatureWriter} to modify features in this {@code DataStore}.
     * {@code FeatureWriter} provides an iterator style API to features.
     * <p>
     * The returned writer does <b>not</b> allow features to be added.
     *
     * @param typeName the type name for features that will be accessible
     *
     * @param filter defines additional constraints on the features that will
     *        be accessible
     *
     * @param transaction the transation that the returned writer operates
     *        against
     *
     * @return an instance of {@code FeatureWriter}
     *
     * @throws IOException if data access errors occur
     *
     * @see #getFeatureWriterAppend(String, Transaction)
     */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException;

    /**
     * Gets a {@code FeatureWriter} to modify features in this {@code DataStore}.
     * {@code FeatureWriter} provides an iterator style API to features.
     * <p>
     * The returned writer does <b>not</b> allow features to be added.
     *
     * @param typeName the type name for features that will be accessible
     *
     * @param transaction the transation that the returned writer operates
     *        against
     *
     * @return an instance of {@code FeatureWriter}
     *
     * @throws IOException if data access errors occur
     *
     * @see #getFeatureWriterAppend(String, Transaction)
     */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException;

    /**
     * Gets a {@code FeatureWriter} that can add new features to the {@code DataStore}.
     * <p>
     * The {@code FeatureWriter} will return {@code false} when its {@code hasNext()}
     * method is called, but {@code next()} can be used to acquire new features.
     *
     * @param typeName name of the feature type for which features will be added
     *
     * @param transaction the transaction to operate against
     *
     * @return an instance of {@code FeatureWriter} that can only be used to
     *         append new features
     *
     * @throws IOException if data access errors occur
     */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException;

    /**
     * Retrieve a per featureID based locking service from this {@code DataStore}.
     *
     * @return an instance of {@code LockingManager}; or {@code null} if locking
     *         is handled by the {@code DataStore} in a different fashion
     */
    LockingManager getLockingManager();

}
