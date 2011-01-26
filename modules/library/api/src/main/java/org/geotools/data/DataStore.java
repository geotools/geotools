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
import org.opengis.filter.Filter;
import org.geotools.feature.SchemaException;


/**
 * Represents a Physical Store for FeatureTypes.
 *
 * <p>
 * The source of data for FeatureTypes. Shapefiles, databases tables, etc. are
 * referenced through this interface.
 * </p>
 *
 * <p>
 * Summary of our requirements:
 * </p>
 *
 * <ul>
 * <li>
 * Provides lookup of available Feature Types
 * </li>
 * <li>
 * Provides access to low-level Readers/Writers API for a feature type
 * </li>
 * <li>
 * Provides access to high-level FeatureSource/Store/Locking API a feature type
 * </li>
 * <li>
 * Handles the conversion of filters into data source specific queries
 * </li>
 * <li>
 * Handles creation of new Feature Types
 * </li>
 * <li>
 * Provides access of Feature Type Schema information
 * </li>
 * </ul>
 *
 * Suggestions:
 *
 * <ul>
 * <li>GeoAPI - has reduced this to api to the FeatureStore construct
 *     Jody - since we are no longer using the FeatureReader/ReaderWriter in client
 *            code this would not be a bad idea.
 * </li>
 * </ul>
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 * @version $Id$
 */
public interface DataStore extends DataAccess<SimpleFeatureType, SimpleFeature>{
   
    /**
     * Used to force namespace and CS info into a persistent change.
     * <p>
     * The provided featureType should completely cover the existing schema.
     * All attributes should be accounted for and the typeName should match.
     * </p>
     * <p>
     * Suggestions:
     * </p>
     * <ul>
     * <li>Sean - don't do this</li>
     * <li>Jody - Just allow changes to metadata: CS, namespace, and others</li>
     * <li>James - Allow change/addition of attribtues</li>
     * </ul>
     * @param typeName
     * @throws IOException
     */
    void updateSchema(String typeName, SimpleFeatureType featureType)
        throws IOException;

    /**
     * Retrieves a list of of the available FeatureTypes.
     *
     * <p>
     * This is simply a list of the FeatureType names as aquiring the actual
     * FeatureType schemas may be expensive.
     * </p>
     *
     * <p>
     * Warning: this list may not be unique - the types may be
     * in separate namespaces.
     * </p>
     *
     * <p>
     * If you need to worry about such things please consider the use of
     * the Catalog and CatalogEntry interface - many DataStores support this.
     * getTypeNames is really a convience method for a Catalog.iterator() where
     * the name of each entry is returned.
     * </p>
     *
     * @return typeNames for available FeatureTypes.
     */
    String[] getTypeNames() throws IOException;
    
    /**
     * Retrieve FeatureType metadata by <code>typeName</code>.
     *
     * <p>
     * Retrieves the Schema information as a FeatureType object.
     * </p>
     *
     * @param typeName typeName of requested FeatureType
     *
     * @return FeatureType for the provided typeName
     *
     * @throws IOException If typeName cannot be found
     */
    SimpleFeatureType getSchema(String typeName) throws IOException;

    /**
     * Access a FeatureSource<SimpleFeatureType, SimpleFeature> for Query providing a high-level API.
     * <p>
     * The provided Query does not need to completely cover the existing
     * schema for Query.getTypeName(). The result will mostly likely only be
     * a FeatureSource<SimpleFeatureType, SimpleFeature> and probably wont' allow write access by the
     * FeatureStore method.
     * </p>
     * <p>
     * By using Query we allow support for reprojection, in addition
     * to overriding the CoordinateSystem used by the native FeatureType.
     * </p>
     * <p>
     * We may wish to limit this method to only support Queries using
     * Filter.EXCLUDE.
     * </p>
     * <p>
     * Update - GeoServer has an elegatent implementation of this functionality
     * that we could steal. GeoServerFeatureSource, GeoServerFeatureStore and
     * GeoServerFeatureLocking serve as a working prototype.
     * </p>
     * @param query Query.getTypeName() locates FeatureType being viewed
     *
     * @return FeatureSource<SimpleFeatureType, SimpleFeature> providing operations for featureType
     * @throws IOException If FeatureSource<SimpleFeatureType, SimpleFeature> is not available
     * @throws SchemaException If fetureType is not covered by existing schema
     */
    FeatureSource<SimpleFeatureType, SimpleFeature> getView(Query query) throws IOException,
            SchemaException;

    /**
     * Access a FeatureSource<SimpleFeatureType, SimpleFeature> for typeName providing a high-level API.
     *
     * <p>
     * The resulting FeatureSource<SimpleFeatureType, SimpleFeature> may implment more functionality:
     * </p>
     * <pre><code>
     *
     * FeatureSource<SimpleFeatureType, SimpleFeature> fsource = dataStore.getFeatureSource( "roads" );
     * FeatureStore fstore = null;
     * if( fsource instanceof FeatureLocking ){
     *     fstore = (FeatureStore<SimpleFeatureType, SimpleFeature>) fs;
     * }
     * else {
     *     System.out.println("We do not have write access to roads");
     * }
     * </code>
     * </pre>
     *
     * @param typeName
     *
     * @return FeatureSource<SimpleFeatureType, SimpleFeature> (or subclass) providing operations for typeName
     */
    FeatureSource<SimpleFeatureType, SimpleFeature> getFeatureSource(String typeName)
            throws IOException;

    /**
     * Access a FeatureReader providing access to Feature information.
     *
     * <p>
     * <b>Filter</b> is used as a low-level indication of constraints.
     * (Implementations may resort to using a FilteredFeatureReader, or
     * provide their own optimizations)
     * </p>
     *
     * <p>
     * <b>FeatureType</b> provides a template for the returned FeatureReader
     * </p>
     *
     * <ul>
     * <li>
     * featureType.getTypeName(): used by JDBC as the table reference to query
     * against. Shapefile reader may need to store a lookup to the required
     * filename.
     * </li>
     * <li>
     * featureType.getAttributeTypes(): describes the requested content. This
     * may be a subset of the complete FeatureType defined by the DataStore.
     * </li>
     * <li>
     * getType.getNamespace(): describes the requested namespace for the
     * results (may be different then the one used internally)
     * </li>
     * </ul>
     *
     * <p>
     * <b>Transaction</b> to externalize DataStore state on a per Transaction
     * basis. The most common example is a JDBC datastore saving a Connection
     * for use across several FeatureReader requests. Similarly a Shapefile
     * reader may wish to redirect FeatureReader requests to a alternate
     * filename over the course of a Transaction.
     * </p>
     *
     * <p>
     * <b>Notes For Implementing DataStore</b>
     * </p>
     *
     * <p>
     * Subclasses may need to retrieve additional attributes, beyond those
     * requested by featureType.getAttributeTypes(), in order to correctly
     * apply the <code>filter</code>.<br>
     * These Additional <b>attribtues</b> should be not be returned by
     * FeatureReader. Subclasses may use ReTypeFeatureReader to aid in
     * acomplishing this.
     * </p>
     * <p>
     * Helper classes for implementing a FeatureReader (in order):
     * </p>
     * <ul>
     * <li>
     * DefaultFeatureReader
     * - basic support for creating a FeatureReader for an AttributeReader
     * </li>
     * <li>
     * FilteringFeatureReader
     * - filtering support
     * </li>
     * <li>
     * DiffFeatureReader
     * - In-Process Transaction Support (see TransactionStateDiff)
     * </li>
     * <li>
     * ReTypeFeatureReader
     * - Feature Type schema manipulation of namesspace and attribute type subsets
     * </li>
     * <li>
     * EmptyFeatureReader
     * - provides no content for Filter.EXCLUDE optimizations
     * </li>
     * </ul>
     * <p>
     * Sample use (not optimized):
     * </p>
     * <pre><code>
     * if (filter == Filter.EXCLUDE) {
     *      return new EmptyFeatureReader(featureType);
     *  }
     *
     *  String typeName = featureType.getTypeName();
     *  FeatureType schema = getSchema( typeName );
     *  FeatureReader reader = new DefaultFeatureReader( getAttributeReaders(), schema );
     *
     *  if (filter != Filter.INCLUDE) {
     *      reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, filter);
     *  }
     *
     *  if (transaction != Transaction.AUTO_COMMIT) {
     *      Map diff = state(transaction).diff(typeName);
     *      reader = new DiffFeatureReader(reader, diff);
     *  }
     *
     *  if (!featureType.equals(reader.getFeatureType())) {
     *      reader = new ReTypeFeatureReader(reader, featureType);
     *  }
     * return reader
     * </code></pre>
     * <p>
     * Locking support does not need to be provided for FeatureReaders.
     * </p>
     *
     * @param query Requested form of the returned Features and the filter used
     *              to constraints the results
     * @param transaction Transaction this query operates against
     *
     * @return FeatureReader Allows Sequential Processing of featureType
     */
    FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,
            Transaction transaction) throws IOException;

    /**
     * Access FeatureWriter for modification of existing DataStore contents.
     *
     * <p>
     * To limit FeatureWriter to the FeatureTypes defined by this DataStore,
     * typeName is used to indicate FeatureType. The resulting
     * feature writer will allow modifications against the
     * same FeatureType provided by getSchema( typeName )
     * </p>
     *
     * <p>
     * The FeatureWriter will provide access to the existing contents of the
     * FeatureType referenced by typeName. The provided filter will be used
     * to skip over Features as required.
     * </p>
     *
     * <b>Notes For Implementing DataStore</b>
     * </p>
     *
     * <p>
     * The returned FeatureWriter <b>does not</b> support the addition of new
     * Features to FeatureType (it would need to police your modifications to
     * agree with <code>filer</code>).  As such it will return
     * <code>false</code> for getNext() when it reaches the end of the Query
     * and NoSuchElementException when next() is called.
     * </p>
     *
     * <p>
     * Helper classes for implementing a FeatureWriter (in order):
     * </p>
     * <li>
     * InProcessLockingManager.checkedWriter( writer )
     * - provides a check against locks before allowing modification
     *
     * <li>
     * FilteringFeatureWriter
     * - filtering support for FeatureWriter (does not allow new content)
     * </li>
     * <li>
     * DiffFeatureWriter
     * - In-Process Transaction Support (see TransactionStateDiff)
     * </li>
     * <li>
     * EmptyFeatureWriter
     * - provides no content for Filter.EXCLUDE optimizations
     * </li>
     * </ul>
     *
     * @param typeName Indicates featureType to be modified
     * @param filter constraints used to limit the modification
     * @param transaction Transaction this query operates against
     *
     * @return FeatureWriter Allows Sequential Modification of featureType
     */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Filter filter, Transaction transaction) throws IOException;

    /**
     * Access FeatureWriter for modification of the DataStore typeName.
     *
     * <p>
     * FeatureWriters will need to be limited to the FeatureTypes defined by
     * the DataStore, the easiest way to express this limitation is to the
     * FeatureType by a provided typeName.
     * </p>
     *
     * <p>
     * The returned FeatureWriter will return <code>false</code> for getNext()
     * when it reaches the end of the Query.
     * </p>
     *
     * @param typeName Indicates featureType to be modified
     * @param transaction Transaction to operates against
     *
     * @return FeatureReader Allows Sequential Processing of featureType
     */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName,
            Transaction transaction) throws IOException;

    /**
     * Aquire a FeatureWriter for adding new content to a FeatureType.
     *
     * <p>
     * This FeatureWriter will return <code>false</code> for hasNext(), however
     * next() may be used to aquire new Features that may be writen out to add
     * new content.
     * </p>
     *
     * @param typeName Indicates featureType to be modified
     * @param transaction Transaction to operates against
     *
     * @return FeatureWriter that may only be used to append new content
     *
     * @throws IOException
     */
    FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName,
            Transaction transaction) throws IOException;

    /**
     * Retrieve a per featureID based locking service from this DataStore.
     *
     * <p>
     * It is common to return an instanceof InProcessLockingManager for
     * DataStores that do not provide native locking.
     * </p>
     *
     * <p>
     * AbstractFeatureLocking makes use of this service to provide locking
     * support. You are not limitied by this implementation and may simply
     * return <code>null</code> for this value.
     * </p>
     *
     * @return DataStores may return <code>null</code>, if the handling locking
     *         in another fashion.
     */
    LockingManager getLockingManager();

}
