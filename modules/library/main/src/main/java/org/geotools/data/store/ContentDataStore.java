/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.geotools.api.data.DataAccess;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFactorySpi;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.LockingManager;
import org.geotools.api.data.Query;
import org.geotools.api.data.ServiceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.FeatureTypeFactory;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.util.TypeName;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.InProcessLockingManager;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.NameImpl;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * Abstract base class for data stores.
 *
 * <p>A datastore contains a set of entries ({@link ContentEntry}). Each entry corresponds to a "real world dataset".
 * For instance, a shapefile datastore would contain a single entry which would represent the shapefile on disk. A
 * postgis datastore could contain many entries, one for each table in the database.
 *
 * <p>Each entry is identified by a name ({@link Name}). The name can be qualified with a namespace uri, or unqualified
 * (in which the namespace uri is null). An example of a datastore that might use qualified names is WFS, where in each
 * entry corresponds to a WFS "Feature Type", which have namespace qualified name. Other datastores (such as databases)
 * use unqualified names.
 *
 * <p>When entry names of a datastore are unqualified, a default namespace uri can be set "globally" on the datastore
 * itself, see {@link #setNamespaceURI(String)}. When this value is set, unqualified entry names are implicitly
 * qualified with the global namespace uri.
 *
 * <h3>Subclasses</h3>
 *
 * <p>At a minimum subclasses must implement the following methods:
 *
 * <ul>
 *   <li>{@link #createTypeNames()}
 *   <li>{@link #createFeatureSource(ContentEntry)}
 * </ul>
 *
 * The following methods may also be overriden:
 *
 * <ul>
 *   <li>{@link #createContentState(ContentEntry)}
 * </ul>
 *
 * The following methods may be overriden but <b>only</b> to narrow the return type to a specific subclass of
 * {@link ContentFeatureSource}.
 *
 * <ul>
 *   <li>{@link #getFeatureSource(String)}
 *   <li>{@link #getFeatureSource(String, Transaction)}
 *   <li>{@link #getFeatureSource(Name, Transaction)}
 * </ul>
 *
 * @author Jody Garnett, Refractions Research Inc.
 * @author Justin Deoliveira, The Open Planning Project
 * @author Niels Charlier
 */
public abstract class ContentDataStore implements DataStore {

    /**
     * When joining feature types, the UserData of the joined attribute descriptors will contain their full feature type
     * under this key
     */
    public static final String JOINED_FEATURE_TYPE = "JoinedFeatureType";

    /** Flag writer for adding new content */
    protected static final int WRITER_ADD = 0x01 << 0;
    /** Flag writer for updating content in place */
    protected static final int WRITER_UPDATE = 0x01 << 1;
    /** Flag writer for commit (AUTO_COMMIT with no events) */
    protected static final int WRITER_COMMIT = 0x01 << 2;

    /** name, entry map */
    protected final Map<Name, ContentEntry> entries;

    /** logger */
    protected final Logger LOGGER;

    /** Factory used to create feature types */
    protected FeatureTypeFactory typeFactory;

    /** Factory used to create features */
    protected FeatureFactory featureFactory;

    /** Factory used to create filters */
    protected FilterFactory filterFactory;

    /** Factory used to create geometries */
    protected GeometryFactory geometryFactory;

    /** namespace uri of the datastore itself, or default namespace */
    protected String namespaceURI;

    /** locking manager */
    protected LockingManager lockingManager = new InProcessLockingManager();

    /** factory used to create the datastore */
    protected DataStoreFactorySpi dataStoreFactory;

    public ContentDataStore() {
        // get a concurrent map so that we can do reads in parallel with writes (writes vs writes
        // are actually synchronized to prevent double work, see getEntry()).
        this.entries = new ConcurrentHashMap<>();
        // grabbing the logger here makes the logger name polymorphic (the name of the actual
        // subclass will be used
        this.LOGGER = org.geotools.util.logging.Logging.getLogger(getClass());
        // default
        setFilterFactory(CommonFactoryFinder.getFilterFactory());
    }

    //
    // Property accessors
    //

    /** The factory used to create feature types. */
    public FeatureTypeFactory getFeatureTypeFactory() {
        return typeFactory;
    }

    /** Sets the factory used to create feature types. */
    public void setFeatureTypeFactory(FeatureTypeFactory typeFactory) {
        this.typeFactory = typeFactory;
    }

    /** Sets the factory used to create features. */
    public void setFeatureFactory(FeatureFactory featureFactory) {
        this.featureFactory = featureFactory;
    }

    /** The factory used to create filters. */
    public FilterFactory getFilterFactory() {
        return filterFactory;
    }

    /** The factory used to create features. */
    public FeatureFactory getFeatureFactory() {
        return featureFactory;
    }

    /** Sets the factory used to create filters. */
    public void setFilterFactory(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    /** The factory used to create geometries. */
    public GeometryFactory getGeometryFactory() {
        return geometryFactory;
    }

    /** Sets the factory used to create geometries. */
    public void setGeometryFactory(GeometryFactory geometryFactory) {
        this.geometryFactory = geometryFactory;
    }

    /**
     * Returns the factory used to create the data store.
     *
     * @return The data store factory, possibly <code>null</code>.
     */
    public DataStoreFactorySpi getDataStoreFactory() {
        return dataStoreFactory;
    }

    /**
     * Sets the data store factory used to create the datastore.
     *
     * <p>WARNING: This property should only be set in cases where the datastore factory is stateless and does not
     * maintain any references to created datastores. Setting this property in such a case will result in a memory leak.
     */
    public void setDataStoreFactory(DataStoreFactorySpi dataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
    }

    /**
     * The namespace uri of the datastore.
     *
     * @return The namespace uri, may be <code>null</code>.
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Sets the namespace uri of the datastore.
     *
     * <p>This will be used to qualify the entries or types of the datastore.
     *
     * @param namespaceURI The namespace uri, may be <code>null</code>.
     */
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /** The logger for the datastore. */
    public Logger getLogger() {
        return LOGGER;
    }

    //
    // DataStore API
    //

    @Override
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from " + getClass().getSimpleName());
        info.setSchema(FeatureTypes.DEFAULT_NAMESPACE);
        return info;
    }

    /**
     * Returns the names of all entries or types provided by the datastore.
     *
     * <p>This method is marked final and delegates to {@link #createTypeNames()}, which subclasses are intended to
     * implement.
     *
     * @see DataStore#getTypeNames()
     */
    @Override
    public final String[] getTypeNames() throws IOException {
        List<Name> typeNames = createTypeNames();
        String[] names = new String[typeNames.size()];

        for (int i = 0; i < typeNames.size(); i++) {
            Name typeName = typeNames.get(i);
            names[i] = typeName.getLocalPart();
        }

        return names;
    }

    /**
     * Creates a new schema in the datastore.
     *
     * <p>This implementation throws a{@link UnsupportedOperationException}. Subclasses should override to support
     * schema creation.
     *
     * @see DataStore#createSchema(FeatureType)
     */
    @Override
    public void createSchema(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the feature type or schema matching the specified name.
     *
     * <p>This method calls through to <code>getFeatureSource(typeName).getSchema()</code>
     *
     * @see DataStore#getSchema(String)
     */
    @Override
    public final SimpleFeatureType getSchema(String typeName) throws IOException {
        ContentFeatureSource featureSource = getFeatureSource(typeName);
        return featureSource.getSchema();
    }

    /**
     * Returns the feature source matching the specified name.
     *
     * <p>Subclasses should not implement this method. However overriding in order to perform a type narrowing to a
     * subclasses of {@link ContentFeatureSource} is acceptable.
     *
     * @see DataStore#getFeatureSource(String)
     */
    @Override
    public ContentFeatureSource getFeatureSource(String typeName) throws IOException {
        return getFeatureSource(new NameImpl(null, typeName), Transaction.AUTO_COMMIT);
    }

    /**
     * Returns the feature source matching the specified name and explicitly specifies a transaction.
     *
     * <p>Subclasses should not implement this method. However overriding in order to perform a type narrowing to a
     * subclasses of {@link ContentFeatureSource} is acceptable.
     *
     * @see DataStore#getFeatureSource(String)
     */
    public ContentFeatureSource getFeatureSource(String typeName, Transaction tx) throws IOException {
        return getFeatureSource(new NameImpl(null, typeName), tx);
    }

    /**
     * Returns the feature source matching the specified name and explicitly specifies a transaction.
     *
     * <p>Subclasses should not implement this method. However overriding in order to perform a type narrowing to a
     * subclasses of {@link ContentFeatureSource} is acceptable.
     *
     * @see DataStore#getFeatureSource(String)
     */
    public ContentFeatureSource getFeatureSource(Name typeName, Transaction tx) throws IOException {

        ContentEntry entry = ensureEntry(typeName);

        ContentFeatureSource featureSource = createFeatureSource(entry);
        featureSource.setTransaction(tx);

        //        if ( tx != Transaction.AUTO_COMMIT ) {
        //            //setup the transaction state
        //            synchronized (tx) {
        //                if ( tx.getState( typeName ) == null ) {
        //                    tx.putState( typeName, createTransactionState(featureSource) );
        //                }
        //            }
        //        }

        return featureSource;
    }

    /**
     * Returns a feature reader for the specified query and transaction.
     *
     * <p>This method is not intended to be overridden and is marked final. This implementation delegates to
     * {@link FeatureCollection} and wraps an iterator in a {@link FeatureReader}.
     */
    @Override
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction tx)
            throws IOException {

        if (query.getTypeName() == null) {
            throw new IllegalArgumentException("Query does not specify type.");
        }

        return getFeatureSource(query.getTypeName(), tx).getReader(query);
    }

    /**
     * Returns a feature writer for the specified query and transaction.
     *
     * <p>This method is not intended to be overridden and is marked final. This implementation delegates to
     * {@link FeatureCollection} and wraps an iterator in a {@link FeatureWriter}.
     */
    @Override
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(
            String typeName, Filter filter, Transaction tx) throws IOException {

        ContentFeatureStore featureStore = ensureFeatureStore(typeName, tx);
        return featureStore.getWriter(filter, WRITER_UPDATE | WRITER_ADD);
    }

    /**
     * Helper method which gets a feature source ensuring that it is a feature store as well. If not it throws an
     * IOException.
     *
     * @param typeName The name of the feature source.
     * @param tx A transaction handle.
     * @throws IOException If the feature source is not a store.
     */
    protected final ContentFeatureStore ensureFeatureStore(String typeName, Transaction tx) throws IOException {

        ContentFeatureSource featureSource = getFeatureSource(typeName, tx);
        if (!(featureSource instanceof ContentFeatureStore)) {
            throw new IOException(typeName + " is read only");
        }

        return (ContentFeatureStore) featureSource;
    }

    /**
     * Returns a feature writer for the specified type name and transaction.
     *
     * <p>This method is convenience for <code>getFeatureWriter(typeName,Filter.INCLUDE,tx)</code>.
     */
    @Override
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction tx)
            throws IOException {

        return getFeatureWriter(typeName, Filter.INCLUDE, tx);
    }

    /**
     * Returns an appending feature writer for the specified type name and transaction.
     *
     * <p>This method is not intended to be overridden and is marked final. This implementation delegates to
     * {@link FeatureCollection} and wraps an iterator in a {@link FeatureWriter}.
     */
    @Override
    public final FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName, Transaction tx)
            throws IOException {

        ContentFeatureStore featureStore = ensureFeatureStore(typeName, tx);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = featureStore.getWriter(Filter.INCLUDE, WRITER_ADD);

        // ensure we are at the "end" as we are being asked to return this in "append" mode
        while (writer.hasNext()) {
            writer.next();
        }
        return writer;
    }

    @Override
    public final LockingManager getLockingManager() {
        return lockingManager;
    }

    @Override
    public final void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void dispose() {
        for (ContentEntry entry : entries.values()) {
            entry.dispose();
        }
        entries.clear();
    }

    /** Returns the entry for a specified name, or <code>null</code> if no such entry exists. */
    public ContentEntry getEntry(Name name) {
        return entries.get(name);
    }

    //
    // Internal API
    //

    /**
     * Instantiates a new content state for the entry.
     *
     * <p>Subclasses may override this method to return a specific subclass of {@link ContentState}.
     *
     * @param entry The entry.
     * @return A new instance of {@link ContentState} for the entry.
     */
    protected ContentState createContentState(ContentEntry entry) {
        return new ContentState(entry);
    }

    /** Helper method to wrap a non-qualified name. */
    protected final Name name(String typeName) {
        return new NameImpl(namespaceURI, typeName);
    }

    /**
     * Helper method to look up an entry in the datastore.
     *
     * <p>This method will create a new instance of {@link ContentEntry} if one does not exist.
     *
     * <p>In the event that the name does not map to an entry and one cannot be created <code>null
     * </code> will be returned. Note that {@link #ensureEntry(TypeName)} will throw an exception in this case.
     *
     * @param name The name of the entry.
     * @return The entry, or <code>null</code> if it does not exist.
     */
    protected final ContentEntry entry(Name name) throws IOException {
        ContentEntry entry = null;

        boolean found = entries.containsKey(name);
        boolean unqualifiedSearch = name.getNamespaceURI() == null;
        if (!found && unqualifiedSearch && this.namespaceURI != null) {
            Name defaultNsName = new NameImpl(namespaceURI, name.getLocalPart());
            if (entries.containsKey(defaultNsName)) {
                name = defaultNsName;
                found = true;
            }
        }

        if (!found) {
            // refresh the entries (calling createTypeNames() can be quite expensive,
            // make it count) and do namespace-less matches as required
            List<Name> typeNames = createTypeNames();

            for (Name tn : typeNames) {
                synchronized (this) {
                    if (!entries.containsKey(tn)) {
                        entry = new ContentEntry(this, tn);
                        entries.put(tn, entry);
                    }
                }

                // do namespace-less matching if necessary
                if (!found
                        && (tn.equals(name)
                                || unqualifiedSearch && tn.getLocalPart().equals(name.getLocalPart()))) {
                    name = tn;
                    found = true;
                }
            }
        }

        return entries.get(name);
    }

    /**
     * Helper method to look up an entry in the datastore which throws an {@link IOException} in the event that the
     * entry does not exist.
     *
     * @param name The name of the entry.
     * @return The entry.
     * @throws IOException If the entry does not exist, or if there was an error looking it up.
     */
    protected final ContentEntry ensureEntry(Name name) throws IOException {
        ContentEntry entry = entry(name);

        if (entry == null) {
            throw new IOException("Schema '" + name + "' does not exist.");
        }

        return entry;
    }

    /**
     * Helper method to remove an entry from the cached entry map.
     *
     * @param name The name of the entry.
     */
    protected final void removeEntry(Name name) {
        if (entries.containsKey(name)) {
            entries.remove(name);
        }
    }

    /**
     * Creates a set of qualified names corresponding to the types that the datastore provides.
     *
     * <p>Namespaces may be left <code>null</code> for data stores which do not support namespace qualified type names.
     *
     * @return A list of {@link Name}.
     * @throws IOException Any errors occuring connecting to data.
     */
    protected abstract List<Name> createTypeNames() throws IOException;

    /**
     * Instantiates new feature source for the entry.
     *
     * <p>Subclasses should override this method to return a specific subclass of {@link ContentFeatureSource}.
     *
     * @param entry The entry.
     * @return An new instance of {@link ContentFeatureSource} for the entry.
     */
    protected abstract ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException;

    /**
     * Instantiates a new transaction state object.
     *
     * <p>Subclasses should override method to return a specific instance of {@link Transaction.State}.
     *
     * @param SimpleFeatureSource The feature source / store for the new transaction state.
     */
    //    protected abstract Transaction.State createTransactionState(ContentSimpleFeatureSource
    // featureSource)
    //        throws IOException;

    /**
     * Delegates to {@link #getFeatureSource(Name, Transaction)}
     *
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    @Override
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return getFeatureSource(typeName, Transaction.AUTO_COMMIT);
    }

    /**
     * Returns the same list of names than {@link #getTypeNames()} meaning the returned Names have no namespace set.
     *
     * @since 2.5
     * @see DataAccess#getNames()
     */
    @Override
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<>(typeNames.length);
        for (String typeName : typeNames) {
            names.add(new NameImpl(typeName));
        }
        return names;
    }

    /**
     * Delegates to {@link #getSchema(String)} with {@code name.getLocalPart()}
     *
     * @since 2.5
     * @see DataAccess#getSchema(Name)
     */
    @Override
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    /**
     * Delegates to {@link #updateSchema(String, SimpleFeatureType)} with {@code name.getLocalPart()}
     *
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    @Override
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }

    /** @see DataAccess#removeSchema(Name) */
    @Override
    public void removeSchema(Name typeName) throws IOException {
        throw new UnsupportedOperationException("Schema removal not supported");
    }

    /** @see DataStore#removeSchema(String) */
    @Override
    public void removeSchema(String typeName) throws IOException {
        throw new UnsupportedOperationException("Schema removal not supported");
    }
}
