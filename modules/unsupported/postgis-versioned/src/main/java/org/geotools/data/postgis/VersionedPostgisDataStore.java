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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultServiceInfo;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureStore;
import org.geotools.data.FeatureWriter;
import org.geotools.data.LockingManager;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.VersioningDataStore;
import org.geotools.data.jdbc.JDBCDataStoreConfig;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.MultiColumnFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.postgis.fidmapper.PostGISAutoIncrementFIDMapper;
import org.geotools.data.postgis.fidmapper.UUIDFIDMapper;
import org.geotools.data.postgis.fidmapper.VersionedFIDMapper;
import org.geotools.data.postgis.fidmapper.VersionedFIDMapperFactory;
import org.geotools.data.simple.SimpleFeatureLocking;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Postgis datastore with versioning support. On the implementation level, this subclass basically
 * acts as a mapper between the base class, that sees the data structures as how they really are,
 * and the outside view, that hides versioning columns and extra versioning tables from feature
 * types.
 * <p>
 * Assumptions made by the data store:
 * <ul>
 * <li>There is a primary key in tables that need to be turned into versioned ones</li>
 * <li>Primary key columns are mapped in the FID mapper.</li>
 * </ul>
 * 
 * @author aaime
 * @since 2.4
 * 
 *
 *
 * @source $URL$
 */
public class VersionedPostgisDataStore implements VersioningDataStore {

    /** The logger for the postgis module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");

    public static final String TBL_VERSIONEDTABLES = "versioned_tables";

    public static final String TBL_TABLESCHANGED = "tables_changed";

    public static final String TBL_CHANGESETS = "changesets";

    static final String REVISION = "revision";

    static final String VERSION = "version";

    static final Class[] SUPPORTED_FID_MAPPERS = new Class[] { BasicFIDMapper.class,
            MultiColumnFIDMapper.class, PostGISAutoIncrementFIDMapper.class, UUIDFIDMapper.class};
    /**
     * Key used to store the feature types touched by the current transaction
     */
    public static final String DIRTYTYPES = "PgVersionedDirtyTypes";

    /**
     * The wrapped postgis datastore that we leverage for most operations
     */
    protected WrappedPostgisDataStore wrapped;

    /**
     * Holds boolean markers used to determine wheter a table is versioned or not (caching to
     * increase speed since isVersioned() check is required in every other public API)
     */
    protected Map versionedMap = new HashMap();
    
    /**
     * The filter factory used for all filter operations
     */
    protected FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    /** Manages listener lists for SimpleFeatureSource implementations */
    protected FeatureListenerManager listenerManager = new FeatureListenerManager();

    public VersionedPostgisDataStore(DataSource dataSource, String schema, String namespace,
            int optimizeMode) throws IOException {
        wrapped = new WrappedPostgisDataStore(dataSource, schema, namespace, optimizeMode);
        checkVersioningDataStructures();
    }

    public VersionedPostgisDataStore(DataSource dataSource, String schema, String namespace)
            throws IOException {
        wrapped = new WrappedPostgisDataStore(dataSource, schema, namespace);
        checkVersioningDataStructures();
    }

    public VersionedPostgisDataStore(DataSource dataSource, String namespace) throws IOException {
        wrapped = new WrappedPostgisDataStore(dataSource, namespace);
        checkVersioningDataStructures();
    }

    public VersionedPostgisDataStore(DataSource dataSource) throws IOException {
        wrapped = new WrappedPostgisDataStore(dataSource);
        checkVersioningDataStructures();
    }

    protected JDBCDataStoreConfig getConfig() {
        return wrapped.getConfig();
    }
    public ServiceInfo getInfo() {
        DefaultServiceInfo info = new DefaultServiceInfo();
        info.setDescription("Features from PostGIS, managed with a version history" );
        info.setSchema( FeatureTypes.DEFAULT_NAMESPACE );
        return info;
    }
    
    public String[] getTypeNames() throws IOException {
        List names = new ArrayList(Arrays.asList(wrapped.getTypeNames()));
        names.remove(TBL_TABLESCHANGED);
        names.remove(TBL_VERSIONEDTABLES);
        for (Iterator it = names.iterator(); it.hasNext();) {
            String name = (String) it.next();
            if(isVersionedFeatureCollection(name))
                it.remove();
        }
        return (String[]) names.toArray(new String[names.size()]);
    }

    public SimpleFeatureType getSchema(String typeName) throws IOException {
        SimpleFeatureType ft = wrapped.getSchema(typeName);
        
        if(!isVersioned(typeName)) {
            return ft;
        }

        // if the feature type is versioned, we have to map the internal feature
        // type to an outside vision where versioned and pk columns are not
        // included
        
        Set names = new HashSet(Arrays.asList(filterPropertyNames(new DefaultQuery(typeName))));
        List filtered = new ArrayList();
        for (int i = 0; i < ft.getAttributeCount(); i++) {
            AttributeDescriptor cat = ft.getDescriptor(i);
            String name = cat.getLocalName().toLowerCase();
            if (names.contains(name)) {
                filtered.add(cat);
            }
        }
        AttributeDescriptor[] ats = (AttributeDescriptor[]) filtered
                .toArray(new AttributeDescriptor[filtered.size()]);

        try {
        	SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        	ftb.init(ft);
        	ftb.setAttributes(Arrays.asList(ats));
            return ftb.buildFeatureType();
        } catch (IllegalArgumentException e) {
            throw new DataSourceException(
                    "Error converting FeatureType from versioned (internal) schema "
                            + "to unversioned (external) schema " + typeName, e);
        }
    }

    public void createSchema(SimpleFeatureType featureType) throws IOException {
        wrapped.createSchema(featureType);
    }

    public void updateSchema(String typeName, SimpleFeatureType featureType) throws IOException {
        throw new IOException("VersionedPostgisDataStore.updateSchema not yet implemented");
        // TODO: implement updateSchema when the postgis data store does it
    }

    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query, Transaction trans) throws IOException {
        if (!isVersioned(query.getTypeName())) {
            return wrapped.getFeatureReader(query, trans);
        }

        // check what revision we have to gather, and build a modified query
        // that extracts it
        DefaultQuery versionedQuery = buildVersionedQuery(query);

         FeatureReader<SimpleFeatureType, SimpleFeature> reader = wrapped.getFeatureReader(versionedQuery, trans);
        VersionedFIDMapper mapper = (VersionedFIDMapper) getFIDMapper(query.getTypeName());
        return new VersionedFeatureReader(reader, mapper);
    }

    public LockingManager getLockingManager() {
        return wrapped.getLockingManager();
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Transaction transaction)
            throws IOException {
        return getFeatureWriter(typeName, Filter.INCLUDE, transaction);
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName, Filter filter, Transaction transaction)
            throws IOException {
        if(TBL_CHANGESETS.equals(typeName))
            throw new DataSourceException("Changesets feature type is read only");
        
        if (!isVersioned(typeName))
            return wrapped.getFeatureWriter(typeName, filter, transaction);

        return getFeatureWriterInternal(typeName, filter, transaction, false);
    }

    /**
     * Returns either a standard feature writer, or a pure append feature writer
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterInternal(String typeName, Filter filter,
            Transaction transaction, boolean append) throws IOException, DataSourceException {
        // check transaction definition is ok
        // checkTransactionProperties(transaction);

        // if transaction is auto-commit, we have to create one, otherwise
        // the database structure may be ruined, plus we need support for
        // transaction state and properties.
        // Yet, we need to remember and commit the transaction when writer is
        // closed
        boolean autoCommit = false;
        if (transaction.equals(Transaction.AUTO_COMMIT)) {
            transaction = new DefaultTransaction();
            autoCommit = true;
        }

        // build a filter that extracts the last revision and remaps fid filters
        Filter revisionedFilter = buildVersionedFilter(typeName, filter, new RevisionInfo());

        // Gather the update writer, used to expire old revisions, and the
        // append writer, used to create new revisions
        FeatureWriter<SimpleFeatureType, SimpleFeature> updateWriter;
        if (append)
            updateWriter = null;
        else
            updateWriter = wrapped.getFeatureWriter(typeName, revisionedFilter, transaction);
        FeatureWriter<SimpleFeatureType, SimpleFeature> appendWriter = wrapped.getFeatureWriterAppend(typeName, transaction);

        // mark this feature type as dirty
        VersionedJdbcTransactionState state = wrapped.getVersionedJdbcTransactionState(transaction);
        state.setTypeNameDirty(typeName);

        // finally, return a feature writer that will do the proper
        // versioning job
        VersionedFIDMapper mapper = (VersionedFIDMapper) getFIDMapper(typeName);
        VersionedFeatureWriter writer = new VersionedFeatureWriter(updateWriter, appendWriter,
                getSchema(typeName), state, mapper, autoCommit);
        writer.setFeatureListenerManager(listenerManager);
        return writer;
    }

    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName, Transaction transaction)
            throws IOException {
        if (!isVersioned(typeName))
            return wrapped.getFeatureWriterAppend(typeName, transaction);

        return getFeatureWriterInternal(typeName, Filter.EXCLUDE, transaction, true);
    }

    public SimpleFeatureSource getFeatureSource(String typeName) throws IOException {
        if (isVersioned(typeName))
            return new VersionedPostgisFeatureStore(getSchema(typeName), this);

        SimpleFeatureSource source = wrapped.getFeatureSource(typeName);
        
        // changesets should be read only for the outside world
        if(TBL_CHANGESETS.equals(typeName))
            return new WrappingPostgisFeatureSource(source, this);
        
        // for the others, wrap so that we don't report the wrong owning datastore
        if(source instanceof SimpleFeatureLocking)
            return new WrappingPostgisFeatureLocking((SimpleFeatureLocking) source, this);
        else if(source instanceof SimpleFeatureStore)
            return new WrappingPostgisFeatureStore((SimpleFeatureStore) source, this);
        else 
            return new WrappingPostgisFeatureSource((SimpleFeatureSource) source, this);
    }

    public SimpleFeatureSource getView(Query query) throws IOException, SchemaException {
        throw new UnsupportedOperationException("At the moment getView(Query) is not supported");
    }

    /**
     * Returns true if the specified feature type is versioned, false otherwise
     * 
     * @param typeName
     * @return
     */
    public boolean isVersioned(String typeName) throws IOException {
        return isVersioned(typeName, null);
    }

    /**
     * Alters the versioned state of a feature type
     * 
     * @param typeName
     *            the type name that must be changed
     * @param versioned
     *            if true, the type gets version enabled, if false versioning is disabled
     * @param t
     *            the transaction used to performe version enabling. It shall contain user and
     *            commit message as properties.
     * @throws IOException
     */
    public synchronized void setVersioned(String typeName, boolean versioned, String author,
            String message) throws IOException {
        if (typeName == null)
            throw new NullPointerException("TypeName cannot be null");
        if (typeName.equals(TBL_CHANGESETS) && versioned)
            throw new IOException(TBL_CHANGESETS
                    + " is exposed as a log facility, and cannot be versioned");

        // no change, no action
        if (isVersioned(typeName) == versioned)
            return;

        if (versioned) { // turn on versioning
            enableVersioning(typeName, author, message);
        } else { // turn off versioning
            disableVersioning(typeName, author, message);
        }
        versionedMap.put(typeName, Boolean.valueOf(versioned));
    }

    /**
     * Checks wheter a type name is versioned or not.
     * 
     * @param typeName
     *            the feature type to check
     * @param transaction
     *            a transaction, or null if you don't have one (use null to avoid a new revision
     *            being created for this operation)
     * @return true if the type is versioned, false otherwise
     * @throws IOException
     */
    protected boolean isVersioned(String typeName, Transaction transaction) throws IOException {
        Boolean versioned = (Boolean) versionedMap.get(typeName);
        if (versioned == null) {
            // first check the type exists for good, this will throw an exception if the
            // schema does not
            if(isVersionedFeatureCollection(typeName))
                throw new DataSourceException("Could not find type " + typeName);
            if(!Arrays.asList(wrapped.getTypeNames()).contains(typeName))
                throw new DataSourceException("Unknown feature type " + typeName);
            
            Connection conn = null;
            Statement st = null;
            PostgisSQLBuilder sqlb = wrapped.createSQLBuilder();
            ResultSet rs = null;
            try {
                if (transaction != null)
                    conn = wrapped.getConnection(transaction);
                else
                    conn = wrapped.getDataSource().getConnection();
                st = conn.createStatement();

                rs = executeQuery(st, "SELECT COUNT(*) from "
                        + sqlb.encodeTableName(TBL_VERSIONEDTABLES) + " WHERE SCHEMA = '"
                        + getConfig().getDatabaseSchemaName() + "'" //
                        + " AND NAME='" + typeName + "'" //
                        + " AND VERSIONED = TRUE");
                rs.next();
                versioned = new Boolean(rs.getInt(1) > 0);
            } catch (SQLException e) {
                throw new DataSourceException("Error occurred while checking versioned tables,"
                        + " database support tables are probably corrupt", e);
            } finally {
                JDBCUtils.close(rs);
                JDBCUtils.close(st);
                JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
            }
            versionedMap.put(typeName, versioned);
        }
        return versioned.booleanValue();
    }
    
    /**
     * Gets a connection for the provided transaction.
     */
    public Connection getConnection(Transaction t) throws IOException {
        return wrapped.getConnection(t);
    }

    public boolean isVersionedFeatureCollection(String typeName) throws IOException {
        if(!typeName.endsWith("_vfc_view"))
            return false;
        
         String originalName = getVFCTableName(typeName);
         return Arrays.asList(wrapped.getTypeNames()).contains(originalName);
    }

    /**
     * @see PostgisDataStore#setWKBEnabled(boolean)
     * @param enabled
     */
    public void setWKBEnabled(boolean enabled) {
        wrapped.setWKBEnabled(enabled);
    }

    /**
     * @see PostgisDataStore#setWKBEnabled(boolean)
     * @param enabled
     */
    public void setLooseBbox(boolean enabled) {
        wrapped.setLooseBbox(enabled);
    }

    public FIDMapper getFIDMapper(String tableName) throws IOException {
        return wrapped.getFIDMapper(tableName);
    }

    /**
     * Returns the last revision of the repository
     * 
     * @return
     */
    public long getLastRevision() throws IOException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = wrapped.getDataSource().getConnection();
            st = conn.createStatement();

            rs = st.executeQuery("select max(revision) from " + TBL_CHANGESETS);
            rs.next();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new DataSourceException("Error getting last revision.", e);
        } finally {
            JDBCUtils.close(rs);
            JDBCUtils.close(st);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    /**
     * Returns a list of type names modified between <code>version1</code> and
     * <code>version2</code>, with the first version excluded.
     * 
     * @param version1
     *            the first version
     * @param version2
     *            the second version, which may be null, if you need to refer the latest version
     * @return an array or type names, eventually empty, never null
     */
    public String[] getModifiedFeatureTypes(String version1, String version2) throws IOException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        RevisionInfo r1 = new RevisionInfo(version1);
        RevisionInfo r2 = new RevisionInfo(version2);

        if (r1.revision > r2.revision) {
            // swap them
            RevisionInfo tmp = r1;
            r1 = r2;
            r1 = tmp;
        }

        // no change occurr between n and n
        if (r1.revision == Long.MAX_VALUE || r1.revision == r2.revision)
            return new String[0];

        try {
            conn = wrapped.getDataSource().getConnection();
            st = conn.createStatement();

            rs = st.executeQuery("select distinct(name) from " + TBL_VERSIONEDTABLES + " where id in "
                    + "(select versionedtable from " + TBL_TABLESCHANGED + " where revision > "
                    + r1.revision + " and revision <= " + r2.revision + ")");
            List result = new ArrayList();
            while (rs.next())
                result.add(rs.getString(1));
            return (String[]) result.toArray(new String[result.size()]);
        } catch (SQLException e) {
            throw new DataSourceException("Error getting feature types modified between "
                    + r1.revision + " and " + r2.revision, e);
        } finally {
            JDBCUtils.close(rs);
            JDBCUtils.close(st);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
    }

    /**
     * Returns a set of feature ids for features that where modified, created or deleted between
     * version1 and version2 and that matched the specified filter at least in one revision between
     * version1 and version2. <br>
     * If called on an unversioned feature type, will return empty Sets.
     * <p>
     * The semantics is a little complex, so here is a more detailed explaination:
     * <ul>
     * <li>A feature is said to have been modified between version1 and version2 if a new state of
     * it has been created after version1 and before or at version2 (included), or if it has been
     * deleted between version1 (excluded) and version2 (included).</li>
     * <li>Filter is used to match every state between version1 and version2, so all new states
     * after version1, but also the states existent at version1 provided they existed also at
     * version1 + 1.</li>
     * <li>If at least one state matches the filter, the feature id is returned.</li>
     * </ul>
     * The result is composed of three sets of feature ids:
     * <ul>
     * <li>A matched feature created after version1 is included in the created set</li>
     * <li>A matched feature deleted before or at version2 is included in the deleted set</li>
     * <li>A matched feature not included in the created/deleted sets is included in the modified
     * set</li>
     * </ul>
     * The following graph illustrates feature matching and set destination. Each line represents a
     * feature lifeline, with different symbols for filter matched states, unmatched states, state
     * creation, expiration, and lack of feature existance.<br>
     * 
     * <pre>
     *                         v1                         v2
     *                         |                          |
     *                f1 ======]..........................|........... Not returned
     *                f2 ======][-------------------------|----------- Not returned     
     *                f3 ======|==].......................|........... Returned (deleted)
     *                f4 ======|==][----------------------|---]....... Returned (modified)
     *                f5 ......|.[=======]................|........... Returned (created/deleted)
     *                f5 ......[=========]................|........... Returned (deleted)
     *                f5 ......[-------------------][=====|====]...... Returned (modified)
     *                f6 [-----|----][=============][-----|----------- Returned (modified)
     * </pre>
     * 
     * Legend:
     * <ul>
     * <li> -: unmatched state</li>
     * <li> =: matched state</li>
     * <li> .: no state (feature has ben deleted)</li>
     * <li> [: creation of a state</li>
     * <li> ]: expiration of a state</li>
     * </ul>
     * 
     * @param version1 -
     *            the first revision
     * @param version2 -
     *            the second revision, or null if you want the diff between version1 and current
     * @param filter a filter to limit the features that must be taken into consideration
     * @param users an eventual list of user ids that can be used to further filter the features,
     * only features touched by any of these users will be  
     * @param transaction
     * @throws IOException
     * @throws IllegalAttributeException
     * @throws NoSuchElementException
     */
    public ModifiedFeatureIds getModifiedFeatureFIDs(String typeName, String version1,
            String version2, Filter originalFilter, String[] users, Transaction transaction) throws IOException {
        if(originalFilter == null)
            originalFilter = Filter.INCLUDE;
        RevisionInfo r1 = new RevisionInfo(version1);
        RevisionInfo r2 = new RevisionInfo(version2);

        if (!isVersioned(typeName)) {
            return new ModifiedFeatureIds(r1, r2);
        } else if (r1.revision > r2.revision) {
            // swap them
            RevisionInfo tmp = r1;
            r1 = r2;
            r2 = tmp;
        }
        
        // version enable the filter
        Filter filter = transformFidFilter(typeName, originalFilter);
        
        // gather the minimum revision at which it makes sense to check for changes
        // then check if it makes sense to have any result, and limit the minimum rev
        // to avoid picking up the changeset where the feature type was revision enabled
        long baseRevision = getBaseRevision(typeName, transaction);
        if(baseRevision > r2.revision)
            return new ModifiedFeatureIds(r1, r2); 
        if(baseRevision > r1.revision)
            r1.revision = baseRevision;
        
        // gather revisions where the specified users were involved... that would be
        // a job for joins, but I don't want to make this code datastore dependent, so
        // far this one is relatively easy to port over to other dbms, I would like it
        // to stay so
        Set userRevisions = getRevisionsCreatedBy(typeName, r1, r2, users, transaction);
        
        // We have to perform the following query:
        // ------------------------------------------------------------
        // select rowId, revisionCreated, [columnsForSecondaryFilter]
        // from data
        // where (
        // (revision < r1 and expired > r1 and expired <= r2)
        // or
        // (revision > r1 and revision <= r2)
        // )
        // and [encodableFilterComponent]
        // and revision in [user created revisions]
        // order by rowId, revisionCreated
        // ------------------------------------------------------------
        // and then run the post filter against the results.
        // Beware, the query extracts rows that do match the prefilter, so it does not
        // allow us to conclude that a feature has been created after r1 only because
        // the smallest revision attribute with find is > r1. There may be a feature
        // that was already there, but that matches the filter only after r1.
        // A second query, fid filter based, is required to decide whether a feature
        // has really come to live after r1.
        // The same goes for deletion, the may be a feature that matches the filter
        // only before r2, and does not match it in the state laying across r2 (if there
        // is one, if a rollback already occurred in the past, there may be holes, intervals
        // where the feature did not exist, a hole is always created when a feature is deleted
        // and then the deletion is rolled back).

        // build a list of columns we need to get out. We need the fid columns, revision (which is
        // part of the internal type fid) and everything to run the post filter against
        Set columns = new HashSet();
        SQLBuilder builder = wrapped.getSqlBuilder(typeName);
        SimpleFeatureType internalType = wrapped.getSchema(typeName);
        Filter preFilter = builder.getPreQueryFilter(filter);
        Filter postFilter = builder.getPostQueryFilter(filter);
        columns.addAll(Arrays.asList(DataUtilities.attributeNames(postFilter, internalType)));
        VersionedFIDMapper mapper = (VersionedFIDMapper) wrapped.getFIDMapper(typeName);
        for (int i = 0; i < mapper.getColumnCount(); i++) {
            columns.add(mapper.getColumnName(i));
        }
        columns.add("revision");
        columns.add("expired");

        // build a filter to extract stuff modified between r1 and r2 and matching the prefilter
        Filter revLeR1 = ff.lessOrEqual(ff.property("revision"), ff.literal(r1.revision));
        Filter expGtR1 = ff.greater(ff.property("expired"), ff.literal(r1.revision));
        Filter expLeR2 = ff.lessOrEqual(ff.property("expired"), ff.literal(r2.revision));
        Filter expLtR2 = ff.less(ff.property("expired"), ff.literal(r2.revision));
        Filter revGtR1 = ff.greater(ff.property("revision"), ff.literal(r1.revision));
        Filter revLeR2 = ff.lessOrEqual(ff.property("revision"), ff.literal(r2.revision));
        Filter versionFilter;
        if(r2.isLast())
            versionFilter = ff.or(ff.and(revLeR1, ff.and(expGtR1, expLtR2)), revGtR1);
        else
            versionFilter = ff.or(ff.and(revLeR1, ff.and(expGtR1, expLeR2)), ff.and(revGtR1,
                revLeR2));
        // ... merge in the prefilter
        Filter newFilter = null;
        if (Filter.EXCLUDE.equals(preFilter)) {
            return new ModifiedFeatureIds(r1, r2);
        } else if (Filter.INCLUDE.equals(preFilter)) {
            newFilter = versionFilter;
        } else {
            Filter clone = transformFidFilter(typeName, preFilter);
            newFilter = ff.and(versionFilter, clone);
        }
        // ... and the user revision checks
        if(userRevisions != null) {
            // if no revisions touched by those users, no changes
            if(userRevisions.isEmpty())
                return new ModifiedFeatureIds(r1, r2);
            
            List urFilters = new ArrayList(userRevisions.size());
            PropertyName revisionProperty = ff.property("revision");
            for (Iterator it = userRevisions.iterator(); it.hasNext();) {
                Long revision = (Long) it.next();
                urFilters.add(ff.equals(revisionProperty, ff.literal(revision)));
            }
            newFilter = ff.and(newFilter, ff.or(urFilters));
        }
        

        // query the underlying datastore
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        Set matched = new HashSet();
        Set createdBefore = new HashSet();
        Set expiredAfter = new HashSet();
        try {
            // ... first gather all fids that do match the pre and post filters between r1 and r2
            // and gather all those fids that we already know were born before r1 or deleted after
            // r2
            String[] colArray = (String[]) columns.toArray(new String[columns.size()]);
            DefaultQuery q = new DefaultQuery(typeName, newFilter, colArray);
            fr = wrapped.getFeatureReader(q, transaction);
            while (fr.hasNext()) {
                SimpleFeature f = fr.next();
                long revision = ((Long) f.getAttribute("revision")).longValue();
                long expired = ((Long) f.getAttribute("expired")).longValue();

                // get the external id, the one that really gives us feature identity
                // and not just feature history
                String id = mapper.getUnversionedFid(f.getID());
                if (!matched.contains(id)
                        && (revision > r1.revision || (expired > r1.revision && expired <= r2.revision))
                        && postFilter.evaluate(f)) {
                    matched.add(id);
                }
                // little optimization, pre-gather all stuff that we already know was created before
                // or deleted after the interval we are taking into consideration
                if (revision <= r1.revision)
                    createdBefore.add(id);
                if (expired > r2.revision)
                    expiredAfter.add(id);
            }
            fr.close();
            fr = null;

            // now onto the created ones. We do start from candidates, those matched for
            // which the prefilter did not return a version before r1 (which does not mean it
            // does not exists...)
            Set created = new HashSet(matched);
            created.removeAll(createdBefore);
            if (!created.isEmpty()) {
                Filter r1FidFilter = buildFidFilter(created);
                Filter r1Filter = buildVersionedFilter(typeName, r1FidFilter, r1);
                DefaultQuery r1q = new DefaultQuery(typeName, r1Filter, colArray);
                fr = wrapped.getFeatureReader(r1q, transaction);
                while (fr.hasNext()) {
                    String versionedId = fr.next().getID();
                    String unversionedId = mapper.getUnversionedFid(versionedId);
                    created.remove(unversionedId);
                }
                fr.close();
                fr = null;
            }

            // and then onto the deleted ones. Same reasoning
            Set deleted = new HashSet(matched);
            deleted.removeAll(expiredAfter);
            if (!deleted.isEmpty()) {
                Filter r2FidFilter = buildFidFilter(deleted);
                Filter r2Filter = buildVersionedFilter(typeName, r2FidFilter, r2);
                DefaultQuery r2q = new DefaultQuery(typeName, r2Filter, colArray);
                fr = wrapped.getFeatureReader(r2q, transaction);
                while (fr.hasNext()) {
                    String versionedId = fr.next().getID();
                    String unversionedId = mapper.getUnversionedFid(versionedId);
                    deleted.remove(unversionedId);
                }
                fr.close();
                fr = null;
            }

            // all matched that are not created after or deleted before are the "modified" ones
            Set modified = new HashSet(matched);
            modified.removeAll(created);
            modified.removeAll(deleted);
            
            // oh, finally we have all we need to return :-)
            return new ModifiedFeatureIds(r1, r2, created, deleted, modified);
        } catch (Exception e) {
            throw new DataSourceException("Error occurred while computing modified fids", e);
        } finally {
            if (fr != null)
                fr.close();
        }
    }

    /**
     * Gathers the revisions created by a certain group of users between two specified revisions
     * @param r1 the first revision
     * @param r2 the second revision
     * @param users an array of user 
     * @return
     */
    Set getRevisionsCreatedBy(String typeName, RevisionInfo r1, RevisionInfo r2, String[] users, Transaction transaction) throws IOException {
        if(users == null || users.length == 0)
            return null;
        
        List filters = new ArrayList(users.length);
        for (int i = 0; i < users.length; i++) {
            filters.add(ff.equals(ff.property("author"), ff.literal(users[i])));
        }
        Filter revisionFilter = ff.between(ff.property("revision"), ff.literal(r1.revision), ff.literal(r2.revision));
        Filter userFilter = ff.and(ff.or(filters), revisionFilter);
        
        // again, here we could filter with a join on the feature type we're investigating, but...
        Query query = new DefaultQuery(VersionedPostgisDataStore.TBL_CHANGESETS, userFilter, new String[] {"revision"});
        Set revisions = new HashSet();
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        try  {
            fr = wrapped.getFeatureReader(query, transaction);
            while(fr.hasNext()) {
                SimpleFeature f = fr.next();
                revisions.add(f.getAttribute("revision"));
            }
        } catch(IllegalAttributeException e) {
            throw new DataSourceException("Error reading revisions modified by users " + Arrays.asList(users), e);
        } finally {
            if(fr != null)
                fr.close();
        }
        return revisions;
    }
    
    /**
     * Returns the base revision for the specified feature type.<p>
     * The base revision is the revision at which the feature type has been version enabled.
     * returned) 
     * @param typeName
     * @return 
     */
    long getBaseRevision(String typeName, Transaction transaction) throws IOException {
        // first grab the table code
        DefaultQuery q = new DefaultQuery(TBL_VERSIONEDTABLES);
        q.setFilter(ff.equal(ff.property("name"), ff.literal(typeName), true));
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        Long tableId;
        try {
            fr = wrapped.getFeatureReader(q, transaction);
            SimpleFeature feature = fr.next();
            tableId = Long.parseLong(feature.getID().substring(TBL_VERSIONEDTABLES.length() + 1));
        } finally {
            if(fr != null) fr.close();
        }
        
        if(tableId == null) {
            throw new RuntimeException("Table " + typeName + " does not appear " +
            		"to be versioned, there is no record of it in " + TBL_VERSIONEDTABLES);
        }
        
        // next find the revision at which it was version enabled (it's the oldest)
        q = new DefaultQuery(TBL_TABLESCHANGED);
        q.setFilter(ff.equal(ff.property("versionedtable"), ff.literal(tableId), false));
        q.setSortBy(new org.opengis.filter.sort.SortBy[] { ff.sort(REVISION, SortOrder.ASCENDING) });
        q.setMaxFeatures(1);
        try {
            fr = wrapped.getFeatureReader(q, transaction);
            if(!fr.hasNext()) {
                return -1; // this is equivalent to "FIRST"
            } else {
                SimpleFeature feature = fr.next();
                return (Long) feature.getAttribute(REVISION);
            }
        } finally {
            if(fr != null) fr.close();
        }
    }

    /**
     * Builds a filter from a set of feature ids, since there is no convenient way to build it using
     * the factory
     * 
     * @param ff
     * @param ids
     * @return
     */
    Filter buildFidFilter(Set ids) {
        Set featureIds = new HashSet();
        for (Iterator it = ids.iterator(); it.hasNext();) {
            String id = (String) it.next();
            featureIds.add(ff.featureId(id));
        }
        return ff.id(featureIds);
    }

    /**
     * Makes sure the required versioning data structures are available in the database
     * 
     * @throws IOException
     */
    protected synchronized void checkVersioningDataStructures() throws IOException {
        Connection conn = null;
        Statement st = null;
        ResultSet tables = null;
        try {
            // gather a connection in auto commit mode, DDL are not subject to
            // transactions anyways
            conn = wrapped.getDataSource().getConnection();
            conn.setAutoCommit(false);

            // gather all table names and check the required tables are there
            boolean changeSets = false;
            boolean tablesChanged = false;
            boolean versionedTables = false;
            DatabaseMetaData meta = conn.getMetaData();
            String[] tableType = { "TABLE" };
            tables = meta.getTables(null, getConfig().getDatabaseSchemaName(), "%", tableType);
            while (tables.next()) {
                String tableName = tables.getString(3);
                if (tableName.equals(TBL_CHANGESETS))
                    changeSets = true;
                if (tableName.equals(TBL_TABLESCHANGED))
                    tablesChanged = true;
                if (tableName.equals(TBL_VERSIONEDTABLES))
                    versionedTables = true;

            }

            // if all tables are there, assume their schema is ok and go on
            // TODO: really check the schema is the one we want
            if (!(changeSets && tablesChanged && versionedTables)) {

                // if we have a partial match, become really angry, someone
                // messed up with the schema
                if (changeSets || tablesChanged || versionedTables) {
                    String msg = "The versioning tables are not complete, yet some table with the same name is there.\n";
                    msg += "Remove tables (";
                    if (changeSets)
                        msg += TBL_CHANGESETS + " ";
                    if (tablesChanged)
                        msg += TBL_TABLESCHANGED + " ";
                    if (versionedTables)
                        msg += TBL_VERSIONEDTABLES;
                    msg += ") before using again the versioned data store";
                    throw new IOException(msg);
                }

                // ok, lets create versioning support tables then
                st = conn.createStatement();
                // according to internet searches, the max length of postgres
                // tables identifiers
                // is 63 chars
                // (http://www.postgresql.org/docs/faqs.FAQ_DEV.html#item2.2,
                // NAMEDATALEN is 64 but the string is null terminated)
                execute(st, "CREATE TABLE " + TBL_VERSIONEDTABLES + "(ID SERIAL PRIMARY KEY, "
                        + "SCHEMA VARCHAR(63) NOT NULL, NAME VARCHAR(63) NOT NULL, "
                        + "VERSIONED BOOLEAN NOT NULL)");
                execute(st, "CREATE TABLE " + TBL_CHANGESETS + "(REVISION BIGSERIAL PRIMARY KEY, "
                        + "AUTHOR VARCHAR(256), "
                        + "DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, " //
                        + "MESSAGE TEXT)");
                String schema = getConfig().getDatabaseSchemaName();
                if (schema == null)
                    schema = "public";
                execute(st, "SELECT ADDGEOMETRYCOLUMN('" + schema + "', '" + TBL_CHANGESETS
                        + "', 'bbox', 4326,  'POLYGON', 2)");

                execute(st, "CREATE TABLE " + TBL_TABLESCHANGED
                        + "(REVISION BIGINT NOT NULL REFERENCES " + TBL_CHANGESETS
                        + ", VERSIONEDTABLE INT NOT NULL REFERENCES " + TBL_VERSIONEDTABLES
                        + ", PRIMARY KEY (REVISION, VERSIONEDTABLE))");

                // and finally commit table creation (yes, Postgres supports
                // transacted DDL)
                conn.commit();
            }
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, sqlException);
            conn = null;

            String message = "Error querying database for list of tables:"
                    + sqlException.getMessage();
            throw new DataSourceException(message, sqlException);
        } finally {
            JDBCUtils.close(tables);
            JDBCUtils.close(st);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }

        resetTypeInfo();
    }

    /**
     * Makes sure type infos and fid mappers are recomputed when table structures do change
     * 
     * @throws IOException
     */
    void resetTypeInfo() throws IOException {
        wrapped.getTypeHandler().forceRefresh();
        wrapped.getTypeHandler().resetFIDMappers();

        // update the list of versioned types so that the factory know what fid
        // mappers do need filtering
        VersionedFIDMapperFactory factory = (VersionedFIDMapperFactory) wrapped
                .getFIDMapperFactory();
        factory.setVersionedTypes(getVersionedTypeNames());

        // ensure this does not get a typed fid mapper for changesets
        // we want easy extraction of the generated revision
//        wrapped.setFIDMapper(TBL_CHANGESETS, new PostGISAutoIncrementFIDMapper(TBL_CHANGESETS, "revision",
//                Types.BIGINT, true));
    }

    private String[] getVersionedTypeNames() throws IOException {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        List tables = new ArrayList();
        try {
            PostgisSQLBuilder sqlb = wrapped.createSQLBuilder();
            conn = wrapped.getDataSource().getConnection();
            st = conn.createStatement();

            rs = executeQuery(st, "SELECT NAME from " + sqlb.encodeTableName(TBL_VERSIONEDTABLES)
                    + " WHERE SCHEMA = '" + getConfig().getDatabaseSchemaName() + "'" //
                    + " AND VERSIONED ='" + true + "'");
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        } catch (SQLException sqlException) {
            String message = "Error querying database for list of versioned tables:"
                    + sqlException.getMessage();
            throw new DataSourceException(message, sqlException);
        } finally {
            JDBCUtils.close(rs);
            JDBCUtils.close(st);
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }
        return (String[]) tables.toArray(new String[tables.size()]);
    }

    /**
     * Enables versioning for the specified feature type, appending a commit message to the
     * changeset
     * 
     * @param typeName
     * @param author
     * @param message
     * @throws IOException
     * @throws DataSourceException
     */
    private void enableVersioning(String typeName, String author, String message)
            throws IOException, DataSourceException {
        // this will make the fid mapper be computed and stick... but we do have
        // timeouts...
        FIDMapper mapper = wrapped.getFIDMapper(typeName);

        // check we can version enable this table, we need a supported fid
        // mapper
        if (!checkSupportedMapper(mapper)) {
            if (mapper instanceof TypedFIDMapper)
                mapper = ((TypedFIDMapper) mapper).getWrappedMapper();
            throw new IOException("This feature type (" + typeName + ") is associated to "
                    + "an unsupported fid mapper: " + mapper.getClass() + "\n"
                    + "The supported fid mapper classes are: "
                    + Arrays.asList(SUPPORTED_FID_MAPPERS));
        }
        // TODO: check for tables that have version reserved column names,
        // we may throw better error messages

        // have a default message
        if (message == null)
            message = "Version enabling " + typeName;

        // alter table structure in a transaction
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        PostgisSQLBuilder sqlb = wrapped.createSQLBuilder();
        Transaction t = new DefaultTransaction();
        t.putProperty(VersioningDataStore.AUTHOR, author);
        t.putProperty(VersioningDataStore.MESSAGE, message);
        try {
            // gather the transaction state and pick the version number, also
            // update the dirty feature types
            // --> if we do this among other alter tables a deadlock occurs,
            // don't know why...
            VersionedJdbcTransactionState state = wrapped.getVersionedJdbcTransactionState(t);
            state.setTypeNameDirty(typeName);
            long revision = state.getRevision();

            // gather bbox, we need it for the first commit msg
            Envelope envelope = wrapped.getFeatureSource(typeName).getBounds();
            if (envelope != null) {
                final GeometryDescriptor defaultGeometry = wrapped.getSchema(typeName).getGeometryDescriptor();
                if(defaultGeometry != null) {
                    CoordinateReferenceSystem crs = defaultGeometry.getCoordinateReferenceSystem();
                    if (crs != null)
                        envelope = JTS.toGeographic(envelope, crs);
                    state.expandDirtyBounds(envelope);
                }
            }

            // setup for altering tables (and ensure a versioned state is
            // attached to the transaction
            conn = state.getConnection();
            PkDescriptor pk = getPrimaryKeyConstraintName(conn, typeName);
            if (pk == null)
                throw new DataSourceException("Cannot version tables without primary keys");

            // build a comma separated list of old pk columns
            String colList = "";
            for (int i = 0; i < pk.columns.length; i++) {
                colList += "," + pk.columns[i];
            }

            // drop the old primary key
            st = conn.createStatement();
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " DROP CONSTRAINT "
                    + pk.name);

            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName)
                    + " ADD COLUMN REVISION BIGINT REFERENCES " + TBL_CHANGESETS);
            // TODO: add some runtime check that acts as a foreign key iif
            // the value is not Long.MAX_VALUE
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName)
                    + " ADD COLUMN EXPIRED BIGINT NOT NULL DEFAULT " + Long.MAX_VALUE);
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName)
                    + " ADD COLUMN CREATED BIGINT REFERENCES " + TBL_CHANGESETS);

            // update all rows in the table with the new revision number
            // and turn revision into a not null column
            execute(st, "UPDATE " + sqlb.encodeTableName(typeName) 
                    + " SET REVISION = " + revision
                    + " , CREATED = " + revision);
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName)
                    + " ALTER REVISION SET NOT NULL");
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName)
                    + " ALTER CREATED SET NOT NULL");

            // now recreate the primary key with revision as first column
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " ADD CONSTRAINT "
                    + pk.name + " PRIMARY KEY(REVISION " + colList + ")");
            // add secondary index
            execute(st, "CREATE INDEX " + typeName.toUpperCase() + "_REVIDX" + " ON " + typeName
                    + "(EXPIRED" + colList + ")");

            // mark the table as versioned. First check if we already have
            // records for this table
            // then insert or update
            rs = executeQuery(st, "SELECT VERSIONED from " + sqlb.encodeTableName(TBL_VERSIONEDTABLES)
                    + " WHERE SCHEMA = '" + getConfig().getDatabaseSchemaName() + "'" //
                    + " AND NAME='" + typeName + "'");
            if (rs.next()) {
                // we already have the table listed, it was versioned in the past
                execute(st, "UPDATE " + sqlb.encodeTableName(TBL_VERSIONEDTABLES) //
                        + " SET VERSIONED = TRUE " //
                        + " WHERE SCHEMA = '" + getConfig().getDatabaseSchemaName() + "'" //
                        + " AND NAME='" + typeName + "'");
            } else {
                // this has never been versioned, insert new records
                execute(st, "INSERT INTO " + sqlb.encodeTableName(TBL_VERSIONEDTABLES)
                        + " VALUES(default, " + "'" + getConfig().getDatabaseSchemaName() + "','"
                        + typeName + "', TRUE)");
            }
            rs.close();
            
            // create view to support versioned feature collection extraction
            createVersionedFeatureCollectionView(typeName, conn);

            // phew... done!
            t.commit();

            // and now wipe out the cached feature type, we just changed it, but
            // do not change the fid mapper, it's still ok (or it isn't?)
            // MIND, this needs to be done _after_ the transaction is committed,
            // otherewise transaction writing will try to get metadata with
            // alters still in progress and the whole thing will lock up
            resetTypeInfo();
        } catch (SQLException sql) {
            throw new DataSourceException("Error occurred during version enabling. "
                    + "Does your table have columns with reserved names?", sql);
        } catch (TransformException e) {
            throw new DataSourceException(
                    "Error occurred while trying to compute the lat/lon bounding box "
                            + "affected by this operation", e);
        } finally {
            JDBCUtils.close(rs);
            JDBCUtils.close(st);
            JDBCUtils.close(conn, t, null);
            t.close();
        }
    }
    
    void createVersionedFeatureCollectionView(String typeName) throws IOException {
        Connection conn = null;
        Transaction t = new DefaultTransaction();
        try {
            conn = wrapped.getConnection(t);
            createVersionedFeatureCollectionView(typeName, conn);
            t.commit();
        } finally {
            JDBCUtils.close(conn, t, null);
        }
    }

    /**
     * Creates the <TABLE>_VFC_VIEW for the specified feature type
     * @param conn
     * @param typeName
     */
    private void createVersionedFeatureCollectionView(String typeName, Connection conn) throws IOException {
        Statement st = null;
        try {
            st = conn.createStatement();
            
            String viewName = getVFCViewName(typeName);
            st.execute("CREATE VIEW " + viewName + "\n " 
                    + "AS SELECT "
                    + "cr.revision as \"creationVersion\", cr.author as \"createdBy\", "
                    + "cr.date as \"creationDate\", cr.message as \"creationMessage\",  "
                    + "lu.revision as \"lastUpdateVersion\", lu.author as \"lastUpdatedBy\", "
                    + "lu.date as \"lastUpdateDate\", lu.message as \"lastUpdateMessage\"," 
                    + typeName + ".*\n"
                    + "FROM " + typeName + " inner join " + " changesets lu on " + typeName 
                    + ".revision = lu.revision " +
                    " inner join changesets cr on " + typeName + ".created = cr.revision");
            
            // make sure there is no other row for this view (one might have remained
            // due to errors, and we would end up with a primary key violation)
            st.execute("DELETE FROM geometry_columns " +
            		   "WHERE f_table_schema = current_schema()" +
            		   "AND f_table_name = '" + viewName + "'");
            
            st.execute("INSERT INTO geometry_columns \n" +
                       "SELECT '', current_schema(), '" + viewName + "', " +
                       "       gc.f_geometry_column, gc.coord_dimension, gc.srid, gc.type\n" +
                       "FROM geometry_columns as gc\n" +
                       "WHERE gc.f_table_name = '" + typeName + "'");
        } catch(SQLException e) {
            throw new DataSourceException("Issues creating versioned feature collection view for " 
                    + typeName, e);
        }finally {
            JDBCUtils.close(st);
        }
    }

    /**
     * Given a type name returns the name of the versioned feature collection view
     * associated to it
     * @param typeName
     * @return
     */
    public static String getVFCViewName(String typeName) {
        return typeName + "_vfc_view";
    }
    
    /**
     * Given a versioned feature collection view name returns the base table name
     * @param vfcTypeName
     * @return
     * @throws IOException
     */
    public static String getVFCTableName(String vfcTypeName) throws IOException {
        if(vfcTypeName.endsWith("_vfc_view"))
          return vfcTypeName.substring(0, vfcTypeName.lastIndexOf("_vfc_view"));
        else
            throw new IOException("Specified type " + vfcTypeName 
                    + " is not a versioned feature collection view");
    }

    /**
     * Disables versioning for the specificed feature type, appeding a commit message to the
     * changeset
     * 
     * @param typeName
     * @param author
     * @param message
     * @throws IOException
     */
    private void disableVersioning(String typeName, String author, String message)
            throws IOException {
        // have a default message
        if (message == null)
            message = "Version disabling " + typeName;

        // alter table structure in a transaction
        Connection conn = null;
        Statement st = null;
        PostgisSQLBuilder sqlb = wrapped.createSQLBuilder();
        Transaction t = new DefaultTransaction();
        t.putProperty(VersioningDataStore.AUTHOR, author);
        t.putProperty(VersioningDataStore.MESSAGE, message);
        try {
            // gather the transaction state and pick the version number, also
            // update the dirty feature types
            // --> if we do this among other alter tables a deadlock occurs,
            // don't know why...
            VersionedJdbcTransactionState state = wrapped.getVersionedJdbcTransactionState(t);
            state.setTypeNameDirty(typeName);

            // the following is funny, but if I don't gather the revision now, the transactio may
            // lock... not sure why...
            state.getRevision();

            // gather bbox, we need it for the first commit msg
            Envelope envelope = wrapped.getFeatureSource(typeName).getBounds();
            if (envelope != null && wrapped.getSchema(typeName).getGeometryDescriptor() != null) {
                CoordinateReferenceSystem crs = wrapped.getSchema(typeName).getGeometryDescriptor()
                        .getCoordinateReferenceSystem();
                if (crs != null)
                    envelope = JTS.toGeographic(envelope, crs);
                state.expandDirtyBounds(envelope);
            }

            // drop the versioning feature collection view
            conn = state.getConnection();
            st = conn.createStatement();
            try {
                st.execute("DROP VIEW " + typeName + "_vfc_view");
            } catch(SQLException e) {
                // if the view wasn't there no problem
            }
            st.execute("DELETE FROM geometry_columns WHERE f_table_schema = current_schema() " +
            		"   AND f_table_name = '" + typeName + "_vfc_view'");
            
            // remove all non active rows
            st.execute("DELETE FROM " + sqlb.encodeTableName(typeName) + " WHERE expired <> " + Long.MAX_VALUE);
            
            // build a comma separated list of old pk columns, just skip the
            // first which we know is "revision"
            PkDescriptor pk = getPrimaryKeyConstraintName(conn, typeName);
            if (pk == null)
                throw new DataSourceException("Cannot version tables without primary keys");
            
            String colList = "";
            for (int i = 1; i < pk.columns.length; i++) {
                colList += "," + pk.columns[i];
            }
            colList = colList.substring(1);

            // drop the current primary key and the index
            execute(st, "DROP INDEX " + sqlb.encodeTableName(typeName.toLowerCase() + "_revidx"));
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " DROP CONSTRAINT "
                    + pk.name);

            // drop versioning columns
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " DROP COLUMN REVISION");
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " DROP COLUMN EXPIRED");
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " DROP COLUMN CREATED");

            // now recreate theold primary key with revision as first column
            execute(st, "ALTER TABLE " + sqlb.encodeTableName(typeName) + " ADD CONSTRAINT "
                    + pk.name + " PRIMARY KEY(" + colList + ")");

            // mark the table as versioned
            execute(st, "UPDATE " + sqlb.encodeTableName(TBL_VERSIONEDTABLES)
                    + " SET VERSIONED = FALSE WHERE SCHEMA = '"
                    + getConfig().getDatabaseSchemaName() + "' AND NAME = '" + typeName + "'");

            // phew... done!
            t.commit();

            // and now wipe out the cached feature type, we just changed it, but
            // do not change the fid mapper, it's still ok (or it isn't?)
            // MIND, this needs to be done _after_ the transaction is committed,
            // otherewise transaction writing will try to get metadata with
            // alters still in progress and the whole thing will lock up
            resetTypeInfo();
        } catch (SQLException sql) {
            throw new DataSourceException("Error occurred during version disabling", sql);
        } catch (TransformException e) {
            throw new DataSourceException(
                    "Error occurred while trying to compute the lat/lon bounding box "
                            + "affected by this operation", e);
        } finally {
            JDBCUtils.close(st);
            JDBCUtils.close(conn, t, null);
            t.close();
        }

    }

    /**
     * Logs the sql at info level, then executes the command
     * 
     * @param st
     * @param sql
     * @throws SQLException
     */
    protected void execute(Statement st, String sql) throws SQLException {
        LOGGER.fine(sql);
        st.execute(sql);
    }

    /**
     * Logs the sql at info level, then executes the command
     * 
     * @param st
     * @param sql
     * @throws SQLException
     */
    protected ResultSet executeQuery(Statement st, String sql) throws SQLException {
        LOGGER.fine(sql);
        return st.executeQuery(sql);
    }

    // /**
    // * Checks versioning transaction properties are there. At the moment the
    // * check is strict, we may want to support default values thought.
    // *
    // * @param t
    // * @throws IOException
    // */
    // private void checkTransactionProperties(Transaction t) throws IOException
    // {
    // if (t.getProperty(AUTHOR) == null)
    // throw new IOException(
    // "Transaction author property should be set, it's not");
    // }

    /**
     * Returns the primary key constraint name for the specified table
     * 
     * @param conn
     * @param typeName
     * @return
     */
    private PkDescriptor getPrimaryKeyConstraintName(Connection conn, String table)
            throws SQLException {
        PkDescriptor descriptor = null;
        ResultSet columns = null;
        try {
            // extract primary key information
            columns = conn.getMetaData().getPrimaryKeys(null, getConfig().getDatabaseSchemaName(),
                    table);
            if (!columns.next())
                return null;

            // build the descriptor
            descriptor = new PkDescriptor();
            descriptor.name = columns.getString("PK_NAME");
            List colnames = new ArrayList();
            do {
                colnames.add(columns.getString("COLUMN_NAME"));
            } while (columns.next());
            descriptor.columns = (String[]) colnames.toArray(new String[colnames.size()]);
        } finally {
            JDBCUtils.close(columns);
        }
        return descriptor;
    }

    /**
     * Returs true if the provided fid mapper is supported by the verisioning engine, false
     * otherwise
     * 
     * @param mapper
     * @return
     */
    private boolean checkSupportedMapper(FIDMapper mapper) {
        if (mapper instanceof TypedFIDMapper) {
            mapper = ((TypedFIDMapper) mapper).getWrappedMapper();
        }
        for (int i = 0; i < SUPPORTED_FID_MAPPERS.length; i++) {
            if (SUPPORTED_FID_MAPPERS[i].isAssignableFrom(mapper.getClass()))
                return true;

        }
        return false;
    }

    /**
     * Takes a filter and merges in the extra conditions needed to extract the specified revision
     * 
     * @param filter
     *            The original filter
     * @param ri
     *            The revision information
     * 
     * @return a new filter
     * @throws FactoryRegistryException
     * @throws IOException
     */
    Filter buildVersionedFilter(String featureTypeName, Filter filter, RevisionInfo ri)
            throws IOException {
        // build extra filter we need to append to query in order to retrieve
        // the desired revision
        Filter extraFilter = null;
        if (ri.isLast()) {
            // expired = Long.MAX_VALUE
            extraFilter = ff.equals(ff.property("expired"), ff.literal(Long.MAX_VALUE));
        } else {
            // revision <= [revision]
            // and expired > [revision]
            Filter revf = ff.lessOrEqual(ff.property("revision"), ff.literal(ri.revision));
            Filter expf = ff.greater(ff.property("expired"), ff.literal(ri.revision));
            extraFilter = ff.and(revf, expf);
        }

        // handle include and exclude separately since the
        // filter factory does not handle them properly
        if (filter.equals(Filter.EXCLUDE)) {
            return Filter.EXCLUDE;
        }
        if (filter.equals(Filter.INCLUDE))
            return extraFilter;

        // we need to turn eventual fid filters into normal filters since we
        // played tricks on fids and hidden the revision attribute
        // (which is part of the primary key)
        Filter transformedFidFilter = transformFidFilter(featureTypeName, filter);
        Filter newFilter = ff.and(transformedFidFilter, extraFilter);

        return newFilter;
    }

    /**
     * Takes a fid filter with external fids and turns it into a set of filters against internal
     * feature type attributes, that is, an equivalent filter that can run against the internal,
     * versioned feature type
     * 
     * @param featureTypeName
     * @param filter
     * @return
     * @throws IOException
     * @throws FactoryRegistryException
     */
    protected Filter transformFidFilter(String featureTypeName, Filter filter) throws IOException,
            FactoryRegistryException {
        if(isVersionedFeatureCollection(featureTypeName))
            featureTypeName = getVFCTableName(featureTypeName);
        SimpleFeatureType featureType = wrapped.getSchema(featureTypeName);
        VersionedFIDMapper mapper = (VersionedFIDMapper) wrapped.getFIDMapper(featureTypeName);
        FidTransformeVisitor transformer = new FidTransformeVisitor(ff, featureType, mapper);
        
        Filter clone = (Filter) filter.accept(transformer, null);
        return clone;
    }

    /**
     * Given an original query and a revision info, builds an equivalent query that will work
     * against the specified revision
     * 
     * @param query
     * @param ri
     * @return
     * @throws FactoryRegistryException
     * @throws IOException
     */
    DefaultQuery buildVersionedQuery(Query query) throws IOException {
        RevisionInfo ri = new RevisionInfo(query.getVersion());
        // build a filter that limits the number
        Filter newFilter = buildVersionedFilter(query.getTypeName(), query.getFilter(), ri);
        DefaultQuery versionedQuery = new DefaultQuery(query);
        versionedQuery.setPropertyNames(filterPropertyNames(query));
        versionedQuery.setFilter(newFilter);
        return versionedQuery;
    }

    /**
     * Returns all property names besides versioning properties
     * 
     * @param query
     * @return
     * @throws IOException
     */
    private String[] filterPropertyNames(Query query) throws IOException {
        String[] names = wrapped.propertyNames(query);

        Set extraColumns = new HashSet();
        if (isVersionedFeatureCollection(query.getTypeName()) || isVersioned(query.getTypeName(), null)) {
            extraColumns.add("revision");
            extraColumns.add("expired");
            extraColumns.add("created");
        }
        FIDMapper mapper = getFIDMapper(query.getTypeName());
        for (int i = 0; i < mapper.getColumnCount(); i++) {
            extraColumns.add(mapper.getColumnName(i).toLowerCase());
        }

        List filtered = new ArrayList(names.length);
        for (int i = 0; i < names.length; i++) {
            if (!extraColumns.contains(names[i]))
                filtered.add(names[i]);
        }
        return (String[]) filtered.toArray(new String[filtered.size()]);
    }

    /**
     * Simple struct used to carry primary key metadata information
     * 
     * @author aaime
     * 
     */
    private static class PkDescriptor {
        String name;

        String[] columns;
    }

    
    public void dispose() {
        if(wrapped != null) {
            wrapped.dispose();
            wrapped = null;
        }
    }

    
    /**
     * Delegates to {@link #getFeatureSource(String)} with
     * {@code name.getLocalPart()}
     * 
     * @since 2.5
     * @see DataAccess#getFeatureSource(Name)
     */
    public SimpleFeatureSource getFeatureSource(Name typeName)
            throws IOException {
        return getFeatureSource(typeName.getLocalPart());
    }

    /**
     * Returns the same list of names than {@link #getTypeNames()} meaning the
     * returned Names have no namespace set.
     * 
     * @since 2.5
     * @see DataAccess#getNames()
     */
    public List<Name> getNames() throws IOException {
        String[] typeNames = getTypeNames();
        List<Name> names = new ArrayList<Name>(typeNames.length);
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
}
