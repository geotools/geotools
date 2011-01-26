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
 *
 */
package org.geotools.arcsde.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jsqlparser.statement.select.PlainSelect;

import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.data.view.QueryInfoParser;
import org.geotools.arcsde.data.view.SelectQualifier;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.SessionWrapper;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.arcsde.versioning.ArcSdeVersionHandler;
import org.geotools.arcsde.versioning.AutoCommitVersionHandler;
import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.EmptyFeatureReader;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.LockingManager;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.util.logging.Logging;
import org.hsqldb.Session;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeQueryInfo;
import com.esri.sde.sdk.client.SeVersion;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;

/**
 * DataStore implementation to work upon an ArcSDE spatial database gateway.
 * <p>
 * Takes ownership of the provided {@link ISessionPool} so make sure to call {@link #dispose()} in
 * order to release resources (ArcSDE connections).
 * </p>
 * 
 * @author Gabriel Roldan (TOPP)
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/arcsde/datastore/src/main
 *         /java/org/geotools/arcsde/data/ArcSDEDataStore.java $
 * @version $Id$
 */
public class ArcSDEDataStore implements DataStore {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.arcsde.data");

    /**
     * Default value for how often, in seconds, to update the feature type name cache.
     * 
     * @see FeatureTypeInfoCache
     */
    private static final int DEFAULT_LAYER_NAMES_CACHE_UPDATE_FREQ_SECS = 60;

    /**
     * Manages listener lists for SimpleFeatureSource implementation
     */
    final FeatureListenerManager listenerManager = new FeatureListenerManager();

    final ISessionPool connectionPool;

    final FeatureTypeInfoCache typeInfoCache;

    /**
     * Name of the arcsde version to work upon
     * 
     * @see #getVersionHandler(String, Transaction)
     */
    private String version;

    /**
     * Creates a new ArcSDE DataStore working over the given connection pool
     * 
     * @param connPool
     *            pool of {@link Session} this datastore works upon.
     * @throws IOException
     */
    public ArcSDEDataStore(final ISessionPool connPool) throws IOException {
        this(connPool, null, null, false);
    }

    /**
     * Creates a new ArcSDE DataStore working over the given connection pool
     * 
     * @param connPool
     *            pool of {@link Session} this datastore works upon.
     * @param namespaceUri
     *            namespace URI for the {@link SimpleFeatureType}s, {@link AttributeType}s, and
     *            {@link AttributeDescriptor}s created by this datastore. May be {@code null}.
     * @param versionName
     *            the name of the ArcSDE version to work upon, or {@code null} for the
     *            {@link SeVersion#SE_QUALIFIED_DEFAULT_VERSION_NAME DEFAULT} version
     * @param allowNonSpatialTables
     *            whether ArcSDE registered, non-spatial tables are to be published
     * @throws IOException
     */
    public ArcSDEDataStore(final ISessionPool connPool, final String namespaceUri,
            final String versionName, final boolean allowNonSpatialTables) throws IOException {
        this.connectionPool = connPool;
        this.version = versionName == null ? SeVersion.SE_QUALIFIED_DEFAULT_VERSION_NAME
                : versionName;
        this.typeInfoCache = new FeatureTypeInfoCache(connectionPool, namespaceUri,
                DEFAULT_LAYER_NAMES_CACHE_UPDATE_FREQ_SECS, allowNonSpatialTables);
    }

    /**
     * Retrieve the connection for the provided transaction.
     * <p>
     * The connection is held open until while the transaction is underway. A a Transaction.State is
     * registered for this SessionPool in order to hold the session.
     * </p>
     * 
     * @param transaction
     * @return the session associated with the transaction
     */
    public ISession getSession(Transaction transaction) throws IOException {
        if (transaction == null) {
            throw new NullPointerException(
                    "transaction can't be null. Did you mean Transaction.AUTO_COMMIT?");
        }
        final ISession session;
        if (Transaction.AUTO_COMMIT.equals(transaction)) {
            try {
                session = connectionPool.getSession(false);
            } catch (UnavailableConnectionException e) {
                throw new RuntimeException("Session pool exhausted", e);
            }
        } else {
            SessionTransactionState state;
            state = SessionTransactionState.getState(transaction, this.connectionPool);
            session = state.getConnection();
        }
        return session;
    }

    private ArcTransactionState getState(final Transaction transaction) {
        if (Transaction.AUTO_COMMIT.equals(transaction)) {
            return null;
        }
        ArcTransactionState state = (ArcTransactionState) transaction.getState(this);
        if (state == null) {
            state = new ArcTransactionState(this, listenerManager);
            transaction.putState(this, state);
        }
        return state;
    }

    /**
     * Creates a new ArcSDE FeatureClass if {@code featureType} has at least one geometry attribute,
     * or an ObjectClass (aka, non spatial registered table) if this data store instance allows non
     * spatial tables and there's no geometry attribute in {@code featureType}.
     * <p>
     * The new arcsde table created will have an SDE managed column to be used as primary key.
     * <p>
     * 
     * @see DataStore#createSchema(SimpleFeatureType)
     * @see #createSchema(SimpleFeatureType, Map)
     */
    public void createSchema(final SimpleFeatureType featureType) throws IOException {
        Map<String, String> hints = new HashMap<String, String>();
        hints.put("rowid.column.type", "SDE");
        String rowIdName = "SDE_ROW_ID";
        while (featureType.getDescriptor(rowIdName) != null) {
            rowIdName += "2";
        }
        hints.put("rowid.column.name", rowIdName);
        createSchema(featureType, hints);
    }

    /**
     * Obtains the schema for the given featuretype name.
     * 
     * @see DataStore#getSchema(String)
     */
    public SimpleFeatureType getSchema(final String typeName) throws java.io.IOException {
        FeatureTypeInfo typeInfo = typeInfoCache.getFeatureTypeInfo(typeName);
        SimpleFeatureType schema = typeInfo.getFeatureType();
        return schema;
    }

    /**
     * List of type names; should be a list of all feature classes.
     * 
     * @return the list of full qualified feature class names on the ArcSDE database this DataStore
     *         works on. An ArcSDE full qualified class name is composed of three dot separated
     *         strings: "DATABASE.USER.CLASSNAME", wich is usefull enough to use it as namespace
     * @throws RuntimeException
     *             if an exception occurs while retrieving the list of registeres feature classes on
     *             the backend, or while obtaining the full qualified name of one of them
     */
    public String[] getTypeNames() throws IOException {
        List<String> layerNames = typeInfoCache.getTypeNames();
        return layerNames.toArray(new String[layerNames.size()]);
    }

    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setTitle("ArcSDE connection to " + connectionPool.getConfig().getServerName()
                + " using database version '" + version + "'");
        info.setDescription("GeoTools ArcSDE DataStore plugin");
        try {
            info.setSchema(typeInfoCache.getNamesapceURI() == null ? FeatureTypes.DEFAULT_NAMESPACE
                    : new URI(typeInfoCache.getNamesapceURI()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return info;
    }

    /**
     * Disposes this ArcSDEDataStore, which means disposing its session pool and hence closing all
     * the SeConnection objects held.
     * 
     * @see DataStore#dispose()
     */
    public void dispose() {
        LOGGER.fine("Disposing " + connectionPool);
        this.typeInfoCache.dispose();
        if (!connectionPool.isClosed()) {
            this.connectionPool.close();
        }
        LOGGER.fine("Session pool disposed");
    }

    @Override
    protected void finalize() {
        dispose();
    }

    /**
     * Returns an {@link ArcSDEFeatureReader}
     * <p>
     * Preconditions:
     * <ul>
     * <li><code>query != null</code>
     * <li><code>query.getTypeName() != null</code>
     * <li><code>query.getFilter != null</code>
     * <li><code>transaction != null</code>
     * </ul>
     * </p>
     * 
     * @see DataStore#getFeatureReader(Query, Transaction)
     * @return {@link ArcSDEFeatureReader} aware of the transaction state
     */
    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final Query query,
            final Transaction transaction) throws IOException {
        assert query != null;
        assert query.getTypeName() != null;
        assert query.getFilter() != null;
        assert transaction != null;

        final SimpleFeatureType featureType = getQueryType(query);

        return getFeatureReader(query, transaction, featureType);
    }

    public FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final Query query,
            final Transaction transaction, final SimpleFeatureType featureType) throws IOException {
        Filter filter = query.getFilter();
        if (filter == Filter.EXCLUDE || filter.equals(Filter.EXCLUDE)) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(featureType);
        }

        final String typeName = query.getTypeName();
        ArcSdeVersionHandler versionHandler = getVersionHandler(typeName, transaction);
        ISession session = getSession(transaction);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        try {
            reader = getFeatureReader(query, featureType, session, versionHandler);
        } catch (IOException ioe) {
            session.dispose();
            throw ioe;
        } catch (RuntimeException re) {
            session.dispose();
            throw re;
        }
        return reader;
    }

    /**
     * Returns an {@link ArcSDEFeatureReader} for the given query that works against the given
     * connection.
     * <p>
     * Explicitly stating the connection to use allows for the feature reader to fetch the
     * differences (additions/modifications/deletions) made while a transaction is in progress.
     * </p>
     * 
     * @param query
     *            the Query containing the request criteria
     * @param session
     *            the session to use to retrieve content.
     * @return
     * @throws IOException
     */
    private FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final Query query,
            final SimpleFeatureType targetSchema, final ISession session,
            final ArcSdeVersionHandler versionHandler) throws IOException {

        final String typeName = query.getTypeName();

        final FeatureTypeInfo typeInfo = typeInfoCache.getFeatureTypeInfo(typeName, session);
        final SimpleFeatureType completeSchema = typeInfo.getFeatureType();
        final ArcSDEQuery sdeQuery;

        final Filter queryFilter = query.getFilter();

        if (queryFilter == Filter.EXCLUDE || queryFilter.equals(Filter.EXCLUDE)) {
            return new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(targetSchema);
        }

        if (typeInfo.isInProcessView()) {
            SeQueryInfo definitionQuery = typeInfo.getSdeDefinitionQuery();
            PlainSelect viewSelectStatement = typeInfo.getDefinitionQuery();
            sdeQuery = ArcSDEQuery.createInprocessViewQuery(session, completeSchema, query,
                    definitionQuery, viewSelectStatement);
        } else {
            final FIDReader fidStrategy = typeInfo.getFidStrategy();
            sdeQuery = ArcSDEQuery.createQuery(session, completeSchema, query, fidStrategy,
                    versionHandler);
        }

        final GeometryFactory geometryFactory = getGeometryFactory(query.getHints());

        // this is the one which's gonna close the connection when done
        final ArcSDEAttributeReader attReader;
        attReader = new ArcSDEAttributeReader(sdeQuery, geometryFactory, session);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        final Filter unsupportedFilter = sdeQuery.getFilters().getUnsupportedFilter();

        try {
            final ArcSDEFeatureReader sdeReader;
            sdeReader = new ArcSDEFeatureReader(attReader);
            reader = sdeReader;
        } catch (SchemaException e) {
            throw new RuntimeException("Schema missmatch, should never happen!: " + e.getMessage(),
                    e);
        }

        if (!unsupportedFilter.equals(Filter.INCLUDE)) {
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader,
                    unsupportedFilter);
        }

        if (!targetSchema.equals(reader.getFeatureType())) {
            LOGGER.fine("Recasting feature type to subtype by using a ReTypeFeatureReader");
            reader = new ReTypeFeatureReader(reader, targetSchema, false);
        }

        if (query.getMaxFeatures() != Query.DEFAULT_MAX) {
            reader = new MaxFeatureReader<SimpleFeatureType, SimpleFeature>(reader,
                    query.getMaxFeatures());
        }

        return reader;
    }

    private GeometryFactory getGeometryFactory(final Hints queryHints) {
        // setup the geometry factory according to the hints
        final Hints hints;
        if (queryHints == null) {
            hints = GeoTools.getDefaultHints();
        } else {
            hints = queryHints;
        }

        GeometryFactory gf = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);

        if (gf == null) {
            PrecisionModel pm = (PrecisionModel) hints.get(Hints.JTS_PRECISION_MODEL);
            if (pm == null) {
                pm = new PrecisionModel();
            }
            Integer SRID = (Integer) hints.get(Hints.JTS_SRID);
            int srid = SRID == null ? 0 : SRID.intValue();
            Integer dimension = (Integer) hints.get(Hints.COORDINATE_DIMENSION);
            CoordinateSequenceFactory csFactory = (CoordinateSequenceFactory) hints
                    .get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
            if (csFactory == null) {
                if (dimension == null || dimension <= 3) {
                    csFactory = CoordinateArraySequenceFactory.instance();
                } else {
                    csFactory = new LiteCoordinateSequenceFactory();
                }
            }
            gf = new GeometryFactory(pm, srid, csFactory);
        }
        return gf;
    }

    public SimpleFeatureType getQueryType(Query query) throws IOException {
        final String typeName = query.getTypeName();
        final String propertyNames[] = query.getPropertyNames();

        final FeatureTypeInfo typeInfo = typeInfoCache.getFeatureTypeInfo(typeName);
        final SimpleFeatureType completeSchema = typeInfo.getFeatureType();

        SimpleFeatureType featureType = completeSchema;

        if (!query.retrieveAllProperties() || query.getCoordinateSystem() != null) {
            try {
                featureType = DataUtilities.createSubType(featureType, propertyNames,
                        query.getCoordinateSystem());
            } catch (SchemaException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
                throw new DataSourceException("Could not create Feature Type for query", e);
            }
        }
        return featureType;
    }

    /**
     * @see DataStore#getFeatureSource(String)
     * @return {@link FeatureSource} or {@link FeatureStore} depending on if the user has write
     *         permissions over <code>typeName</code>
     */
    public SimpleFeatureSource getFeatureSource(final String typeName) throws IOException {
        final FeatureTypeInfo typeInfo = typeInfoCache.getFeatureTypeInfo(typeName);
        SimpleFeatureSource fsource;
        if (typeInfo.isWritable()) {
            fsource = new ArcSdeFeatureStore(typeInfo, this);
        } else {
            fsource = new ArcSdeFeatureSource(typeInfo, this);
        }
        return fsource;
    }

    /**
     * Delegates to {@link #getFeatureWriter(String, Filter, Transaction) getFeatureWriter(typeName,
     * Filter.INCLUDE, transaction)}
     * 
     * @see DataStore#getFeatureWriter(String, Transaction)
     */
    public ArcSdeFeatureWriter getFeatureWriter(final String typeName, final Transaction transaction)
            throws IOException {
        return getFeatureWriter(typeName, Filter.INCLUDE, transaction);
    }

    /**
     * @param typeName
     * @param transaction
     * @return
     * @throws IOException
     */
    ArcSdeVersionHandler getVersionHandler(final String typeName, final Transaction transaction)
            throws IOException {
        ArcSdeVersionHandler versionHandler = ArcSdeVersionHandler.NONVERSIONED_HANDLER;
        {
            final FeatureTypeInfo featureTypeInfo = typeInfoCache.getFeatureTypeInfo(typeName);
            final boolean versioned = featureTypeInfo.isVersioned();

            ArcTransactionState state = getState(transaction);
            if (null == state) {
                if (versioned) {
                    versionHandler = new AutoCommitVersionHandler(version);
                }
            } else {
                versionHandler = state.getVersionHandler(versioned, version);
            }
        }
        return versionHandler;
    }

    /**
     * @see DataStore#getFeatureWriter(String, Filter, Transaction)
     */
    public ArcSdeFeatureWriter getFeatureWriter(final String typeName, final Filter filter,
            final Transaction transaction) throws IOException {
        final ArcSdeVersionHandler versionHandler = getVersionHandler(typeName, transaction);
        // get the connection the streamed writer content has to work over
        // so the reader and writer share it
        final ISession session = getSession(transaction);

        try {
            final FeatureTypeInfo typeInfo = typeInfoCache.getFeatureTypeInfo(typeName, session);
            if (!typeInfo.isWritable()) {
                throw new DataSourceException(typeName + " is not writable");
            }
            final SimpleFeatureType featureType = typeInfo.getFeatureType();

            final FeatureReader<SimpleFeatureType, SimpleFeature> reader;
            if (Filter.EXCLUDE.equals(filter)) {
                reader = new EmptyFeatureReader<SimpleFeatureType, SimpleFeature>(featureType);
            } else {
                final Query query = new Query(typeName, filter);
                final ISession nonDisposableSession = new SessionWrapper(session) {
                    @Override
                    public void dispose() throws IllegalStateException {
                        // do nothing, we don't want the reader to close the session
                    }
                };
                reader = getFeatureReader(query, featureType, nonDisposableSession, versionHandler);
            }

            final ArcSdeFeatureWriter writer;

            final FIDReader fidReader = typeInfo.getFidStrategy();

            final ArcTransactionState state = getState(transaction);

            if (Transaction.AUTO_COMMIT == transaction) {
                writer = new AutoCommitFeatureWriter(fidReader, featureType, reader, session,
                        listenerManager, versionHandler);
            } else {
                // if there's a transaction, the reader and the writer will
                // share the connection held in the transaction state
                writer = new TransactionFeatureWriter(fidReader, featureType, reader, state,
                        versionHandler, listenerManager);
            }
            return writer;
        } catch (IOException e) {
            try {
                session.rollbackTransaction();
            } finally {
                session.dispose();
            }
            throw e;
        } catch (RuntimeException e) {
            try {
                session.rollbackTransaction();
            } catch (IOException e1) {
                LOGGER.log(Level.SEVERE, "Error rolling back transaction on " + session, e);
            } finally {
                session.dispose();
            }
            throw e;
        }
    }

    /**
     * Delegates to {@link #getFeatureWriter(String, Filter, Transaction) getFeatureWriter(typeName,
     * Filter.EXCLUDE, transaction)}
     * 
     * @see DataStore#getFeatureWriterAppend(String, Transaction)
     */
    public ArcSdeFeatureWriter getFeatureWriterAppend(final String typeName,
            final Transaction transaction) throws IOException {
        return getFeatureWriter(typeName, Filter.EXCLUDE, transaction);
    }

    /**
     * @return <code>null</code>, no locking yet
     * @see DataStore#getLockingManager()
     */
    public LockingManager getLockingManager() {
        return null;
    }

    /**
     * This operation is not supported at this version of the GeoTools ArcSDE plugin.
     * 
     * @see DataStore#updateSchema(String, SimpleFeatureType)
     */
    public void updateSchema(final String typeName, final SimpleFeatureType featureType)
            throws IOException {
        throw new UnsupportedOperationException("Schema modification not supported");
    }

    /**
     * Delegates to {@link #getFeatureSource(String)} with {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public SimpleFeatureSource getFeatureSource(Name typeName) throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    /**
     * Returns the same list of names than {@link #getTypeNames()} meaning the returned Names have
     * no namespace set.
     * 
     * @since 2.5
     * @see DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        return typeInfoCache.getNames();
    }

    /**
     * Delegates to {@link #getSchema(String)} with {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getSchema(Name)
     */
    public SimpleFeatureType getSchema(Name name) throws IOException {
        return getSchema(name.getLocalPart());
    }

    /**
     * Delegates to {@link #updateSchema(String, SimpleFeatureType)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public void updateSchema(Name typeName, SimpleFeatureType featureType) throws IOException {
        updateSchema(typeName.getLocalPart(), featureType);
    }

    // ////// NON API Methods /////////

    /**
     * Creates a given FeatureType on the ArcSDE instance this DataStore is running over.
     * <p>
     * This deviation from the {@link DataStore#createSchema(SimpleFeatureType)} API is to allow the
     * specification of ArcSDE specific hints for the "Feature Class" to create:
     * <ul>
     * At this time the following hints may be passed:
     * <li><b>configuration.keywords</b>: database configuration keyword to use for the newly create
     * feature type. In not present, <code>"DEFAULTS"</code> will be used.</li>
     * <li><b>rowid.column.name</b>: indicates the name of the table column to set up as the unique
     * identifier, and thus to be used as feature id.</li>
     * <li><b>rowid.column.type</b>: The row id column type. Must be one of the following allowed
     * values: <code>"NONE"</code>, <code>"USER"</code>, <code>"SDE"</code> in order to set up the
     * row id column name to not be managed at all, to be user managed or to be managed by ArcSDE,
     * respectively. Refer to the ArcSDE documentation for an explanation of the meanings of those
     * terms.</li>
     * </ul>
     * </p>
     * 
     * @param featureType
     * @param hints
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public void createSchema(final SimpleFeatureType featureType, final Map<String, String> hints)
            throws IOException, IllegalArgumentException {

        if (featureType.getGeometryDescriptor() == null && !typeInfoCache.isAllowNonSpatialTables()) {
            throw new DataSourceException("This DataStore does not allow FeatureTypes with no "
                    + "geometry attributes");
        }

        final ISession session = getSession(Transaction.AUTO_COMMIT);
        try {
            ArcSDEAdapter.createSchema(featureType, hints, session);
        } finally {
            session.dispose();
        }
        typeInfoCache.reset();
    }

    /**
     * Supported constructs:
     * <ul>
     * <li>FromItems
     * <li>SelectItems
     * <li>Top (as in SELECT TOP 10 * FROM...)
     * <li>Where
     * </ul>
     * 
     * @param typeName
     * @param select
     * @throws IOException
     */
    void registerView(final String typeName, final PlainSelect select) throws IOException {
        if (typeName == null)
            throw new NullPointerException("typeName");
        if (select == null)
            throw new NullPointerException("select");
        if (Arrays.asList(getTypeNames()).contains(typeName)) {
            throw new IllegalArgumentException(typeName + " already exists as a FeatureType");
        }

        verifyQueryIsSupported(select);

        final ISession session = getSession(Transaction.AUTO_COMMIT);

        try {
            final PlainSelect qualifiedSelect = SelectQualifier.qualify(session, select);
            // System.out.println(qualifiedSelect);

            final SeQueryInfo queryInfo;
            try {
                LOGGER.fine("creating definition query info");
                queryInfo = QueryInfoParser.parse(session, qualifiedSelect);
            } catch (SeException e) {
                throw new ArcSdeException("Error Parsing select: " + qualifiedSelect, e);
            }
            FeatureTypeInfo typeInfo = ArcSDEAdapter.createInprocessViewSchema(session, typeName,
                    typeInfoCache.getNamesapceURI(), qualifiedSelect, queryInfo);

            typeInfoCache.addInprocessViewInfo(typeInfo);
        } finally {
            session.dispose();
        }
    }

    /**
     * Unsupported constructs:
     * <ul>
     * <li>GroupByColumnReferences
     * <li>Joins
     * <li>Into
     * <li>Limit
     * </ul>
     * Not yet verified to work:
     * <ul>
     * <li>Distinct
     * <li>Having
     * <li>
     * </ul>
     * 
     * @param select
     * @throws UnsupportedOperationException
     *             if any of the unsupported constructs are found on <code>select</code>
     */
    private void verifyQueryIsSupported(PlainSelect select) throws UnsupportedOperationException {
        List<Object> errors = new LinkedList<Object>();
        // @TODO errors.add(select.getDistinct());
        // @TODO errors.add(select.getHaving());
        verifyUnsupportedSqlConstruct(errors, select.getGroupByColumnReferences());
        verifyUnsupportedSqlConstruct(errors, select.getInto());
        verifyUnsupportedSqlConstruct(errors, select.getJoins());
        verifyUnsupportedSqlConstruct(errors, select.getLimit());
        if (errors.size() > 0) {
            throw new UnsupportedOperationException("The following constructs are not supported: "
                    + errors);
        }
    }

    /**
     * If construct is not null or an empty list, adds it to the list of errors.
     * 
     * @param errors
     * @param construct
     */
    @SuppressWarnings("unchecked")
    private void verifyUnsupportedSqlConstruct(List<Object> errors, Object construct) {
        if (construct instanceof List) {
            List<Object> constructsList = (List<Object>) construct;
            if (constructsList.size() > 0) {
                errors.addAll(constructsList);
            }
        } else if (construct != null) {
            errors.add(construct);
        }
    }
}
