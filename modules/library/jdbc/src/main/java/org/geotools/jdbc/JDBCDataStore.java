/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.geotools.jdbc.VirtualTable.WHERE_CLAUSE_PLACE_HOLDER;
import static org.geotools.jdbc.VirtualTable.WHERE_CLAUSE_PLACE_HOLDER_LENGTH;
import static org.geotools.jdbc.VirtualTable.setKeepWhereClausePlaceHolderHint;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureStore;
import org.geotools.data.GmlObjectStore;
import org.geotools.data.InProcessLockingManager;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.Transaction.State;
import org.geotools.data.jdbc.FilterToSQL;
import org.geotools.data.jdbc.FilterToSQLException;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentState;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.GroupByVisitor;
import org.geotools.feature.visitor.LimitingVisitor;
import org.geotools.feature.visitor.UniqueCountVisitor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.ExpressionTypeVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JoinInfo.JoinPart;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.BinaryExpression;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.GmlObjectId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Datastore implementation for jdbc based relational databases.
 *
 * <p>This class is not intended to be subclassed on a per database basis. Instead the notion of a
 * "dialect" is used.
 *
 * <p>
 *
 * <h3>Dialects</h3>
 *
 * A dialect ({@link SQLDialect}) encapsulates all the operations that are database specific.
 * Therefore to implement a jdbc based datastore one must extend SQLDialect. The specific dialect to
 * use is set using {@link #setSQLDialect(SQLDialect)}.
 *
 * <p>
 *
 * <h3>Database Connections</h3>
 *
 * Connections to the underlying database are obtained through a {@link DataSource}. A datastore
 * must be specified using {@link #setDataSource(DataSource)}.
 *
 * <p>
 *
 * <h3>Schemas</h3>
 *
 * This datastore supports the notion of database schemas, which is more or less just a grouping of
 * tables. When a schema is specified, only those tables which are part of the schema are provided
 * by the datastore. The schema is specified using {@link #setDatabaseSchema(String)}.
 *
 * <p>
 *
 * <h3>Spatial Functions</h3>
 *
 * The set of spatial operations or functions that are supported by the specific database are
 * reported with a {@link FilterCapabilities} instance. This is specified using {@link
 * #setFilterCapabilities(FilterCapabilities)}.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public final class JDBCDataStore extends ContentDataStore implements GmlObjectStore {

    /** Caches the "setValue" method in various aggregate visitors */
    private static SoftValueHashMap<Class, Method> AGGREGATE_SETVALUE_CACHE =
            new SoftValueHashMap<>(1000);

    /**
     * When true, record a stack trace documenting who disposed the JDBCDataStore. If dispose() is
     * called a second time we can identify the offending parties.
     */
    protected static final Boolean TRACE_ENABLED =
            "true".equalsIgnoreCase(System.getProperty("gt2.jdbc.trace"));

    /**
     * The native SRID associated to a certain descriptor TODO: qualify this key with
     * 'org.geotools.jdbc'
     */
    public static final String JDBC_NATIVE_SRID = "nativeSRID";

    /** Boolean marker stating whether the feature type is to be considered read only */
    public static final String JDBC_READ_ONLY = "org.geotools.jdbc.readOnly";

    /** Boolean marker stating whether an attribute is part of the primary key */
    public static final String JDBC_PRIMARY_KEY_COLUMN = "org.geotools.jdbc.pk.column";

    /**
     * The key for attribute descriptor user data which specifies the original database column data
     * type.
     */
    public static final String JDBC_NATIVE_TYPENAME = "org.geotools.jdbc.nativeTypeName";

    /**
     * The key for attribute descriptor user data which specifies the original database column data
     * type, as a {@link Types} value.
     */
    public static final String JDBC_NATIVE_TYPE = "org.geotools.jdbc.nativeType";

    /** Used to specify the column alias to use when encoding a column in a select */
    public static final String JDBC_COLUMN_ALIAS = "org.geotools.jdbc.columnAlias";

    /** Contains a {@link EnumMapper} to support enums mapped from integer values */
    public static final String JDBC_ENUM_MAP = "org.geotools.jdbc.enumMap";

    /** name of table to use to store geometries when {@link #associations} is set. */
    protected static final String GEOMETRY_TABLE = "geometry";

    /**
     * name of table to use to store multi geometries made up of non-multi geometries when {@link
     * #associations} is set.
     */
    protected static final String MULTI_GEOMETRY_TABLE = "multi_geometry";

    /** name of table to use to store geometry associations when {@link #associations} is set. */
    protected static final String GEOMETRY_ASSOCIATION_TABLE = "geometry_associations";

    /**
     * name of table to use to store feature relationships (information about associations) when
     * {@link #associations} is set.
     */
    protected static final String FEATURE_RELATIONSHIP_TABLE = "feature_relationships";

    /** name of table to use to store feature associations when {@link #associations} is set. */
    protected static final String FEATURE_ASSOCIATION_TABLE = "feature_associations";

    /** The envelope returned when bounds is called against a geometryless feature type */
    protected static final ReferencedEnvelope EMPTY_ENVELOPE = new ReferencedEnvelope();

    /** Max number of ids to use for the optimized locks checking filter. */
    public static final int MAX_IDS_IN_FILTER = 100;

    /** data source */
    protected DataSource dataSource;

    /**
     * Used with TRACE_ENABLED to record the thread responsible for disposing the JDBCDataStore. In
     * the event dispose() is called a second time this throwable is used to identify the offending
     * party.
     */
    private Throwable disposedBy = null;

    /** the dialect of sql */
    public SQLDialect dialect;

    /** The database schema. */
    protected String databaseSchema;

    /** sql type to java class mappings */
    protected HashMap<Integer, Class<?>> sqlTypeToClassMappings;

    /** sql type name to java class mappings */
    protected HashMap<String, Class<?>> sqlTypeNameToClassMappings;

    /** java class to sql type mappings; */
    protected HashMap<Class<?>, Integer> classToSqlTypeMappings;

    /** sql type to sql type name overrides */
    protected HashMap<Integer, String> sqlTypeToSqlTypeNameOverrides;

    /** cache of sqltypes found in database */
    protected ConcurrentHashMap<Integer, String> dBsqlTypesCache;

    /** Feature visitor to aggregate function name */
    protected HashMap<Class<? extends FeatureVisitor>, String> aggregateFunctions;

    /** java supported filter function mappings to dialect name; */
    protected HashMap<String, String> supportedFunctions;

    /**
     * flag controlling if the datastore is supporting feature and geometry relationships with
     * associations
     */
    protected boolean associations = false;

    /**
     * The fetch size for this datastore, defaulting to 1000. Set to a value less or equal to 0 to
     * disable fetch size limit and grab all the records in one shot.
     */
    public int fetchSize;

    /**
     * The number of features to bufferize while inserting in order to do batch inserts.
     *
     * <p>By default 1 to avoid backward compatibility with badly written code that forgets to close
     * the JDBCInsertFeatureWriter or does it after closing the DB connection.
     */
    protected int batchInsertSize = 1;

    /** flag controlling whether primary key columns of a table are exposed via the feature type. */
    protected boolean exposePrimaryKeyColumns = false;

    /**
     * Finds the primary key definitions (instantiated here because the finders might keep state)
     */
    protected PrimaryKeyFinder primaryKeyFinder =
            new CompositePrimaryKeyFinder(
                    new MetadataTablePrimaryKeyFinder(), new HeuristicPrimaryKeyFinder());

    /** Contains the SQL definition of the various virtual tables */
    protected Map<String, VirtualTable> virtualTables = new ConcurrentHashMap<>();

    /** The listeners that are allowed to handle the connection lifecycle */
    protected List<ConnectionLifecycleListener> connectionLifecycleListeners =
            new CopyOnWriteArrayList<>();

    protected JDBCCallbackFactory callbackFactory = JDBCCallbackFactory.NULL;

    private volatile NamePatternEscaping namePatternEscaping;

    public JDBCDataStore() {
        super();
    }

    public void setCallbackFactory(JDBCCallbackFactory factory) {
        this.callbackFactory = factory;
    }

    public JDBCCallbackFactory getCallbackFactory() {
        return callbackFactory;
    }

    public JDBCFeatureSource getAbsoluteFeatureSource(String typeName) throws IOException {
        ContentFeatureSource featureSource = getFeatureSource(typeName);
        if (featureSource instanceof JDBCFeatureSource) {
            return (JDBCFeatureSource) featureSource;
        }
        return ((JDBCFeatureStore) featureSource).getFeatureSource();
    }

    /**
     * Adds a virtual table to the data store. If a virtual table with the same name was registered
     *
     * @throws IOException If the view definition is not valid
     */
    public void createVirtualTable(VirtualTable vtable) throws IOException {
        try {
            virtualTables.put(vtable.getName(), new VirtualTable(vtable));
            // the new vtable might be overriding a previous definition
            entries.remove(new NameImpl(namespaceURI, vtable.getName()));
            getSchema(vtable.getName());
        } catch (IOException e) {
            virtualTables.remove(vtable.getName());
            throw e;
        }
    }

    /** Returns a modifiable list of connection lifecycle listeners */
    public List<ConnectionLifecycleListener> getConnectionLifecycleListeners() {
        return connectionLifecycleListeners;
    }

    /** Removes and returns the specified virtual table */
    public VirtualTable dropVirtualTable(String name) {
        // the new vtable might be overriding a previous definition
        VirtualTable vt = virtualTables.remove(name);
        if (vt != null) {
            entries.remove(new NameImpl(namespaceURI, name));
        }
        return vt;
    }

    /** Returns a live, immutable view of the virtual tables map (from name to definition) */
    public Map<String, VirtualTable> getVirtualTables() {
        Map<String, VirtualTable> result = new HashMap<>();
        for (String key : virtualTables.keySet()) {
            result.put(key, new VirtualTable(virtualTables.get(key)));
        }
        return Collections.unmodifiableMap(result);
    }

    /** Returns the finder used to build {@link PrimaryKey} representations */
    public PrimaryKeyFinder getPrimaryKeyFinder() {
        return primaryKeyFinder;
    }

    /** Sets the finder used to build {@link PrimaryKey} representations */
    public void setPrimaryKeyFinder(PrimaryKeyFinder primaryKeyFinder) {
        this.primaryKeyFinder = primaryKeyFinder;
    }

    /**
     * The current fetch size. The fetch size influences how many records are read from the dbms at
     * a time. If set to a value less or equal than zero, all the records will be read in one shot,
     * severily increasing the memory requirements to read a big number of features.
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /** Changes the fetch size. */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    /** @return the number of features to bufferize while inserting in order to do batch inserts. */
    public int getBatchInsertSize() {
        return batchInsertSize;
    }

    /**
     * Set the number of features to bufferize while inserting in order to do batch inserts.
     *
     * <p>Warning: when changing this value from its default of 1, the behavior of the {@link
     * JDBCInsertFeatureWriter} is changed in non backward compatible ways. If your code closes the
     * writer before closing the connection, you are fine. Plus, the feature added events will be
     * delayed until a batch is actually inserted.
     */
    public void setBatchInsertSize(int batchInsertSize) {
        this.batchInsertSize = batchInsertSize;
    }

    /**
     * Determines if the datastore creates feature types which include those columns / attributes
     * which compose the primary key.
     */
    public boolean isExposePrimaryKeyColumns() {
        return exposePrimaryKeyColumns;
    }

    /**
     * Sets the flag controlling if the datastore creates feature types which include those columns
     * / attributes which compose the primary key.
     */
    public void setExposePrimaryKeyColumns(boolean exposePrimaryKeyColumns) {
        if (this.exposePrimaryKeyColumns != exposePrimaryKeyColumns) {
            entries.clear();
        }
        this.exposePrimaryKeyColumns = exposePrimaryKeyColumns;
    }

    /**
     * The dialect the datastore uses to generate sql statements in order to communicate with the
     * underlying database.
     *
     * @return The dialect, never <code>null</code>.
     */
    public SQLDialect getSQLDialect() {
        return dialect;
    }

    /**
     * Sets the dialect the datastore uses to generate sql statements in order to communicate with
     * the underlying database.
     *
     * @param dialect The dialect, never <code>null</code>.
     */
    public void setSQLDialect(SQLDialect dialect) {
        if (dialect == null) {
            throw new NullPointerException();
        }

        this.dialect = dialect;
    }

    /**
     * The data source the datastore uses to obtain connections to the underlying database.
     *
     * @return The data source, never <code>null</code>.
     */
    public DataSource getDataSource() {
        if (dataSource == null) {
            // Should never return null so throw an exception
            if (TRACE_ENABLED) {
                // If TRACE_ENABLED disposedBy may have stored an exception
                if (disposedBy == null) {
                    LOGGER.log(Level.WARNING, "JDBCDataStore was never given a DataSource.");
                    throw new IllegalStateException(
                            "DataSource not available as it was never set.");
                } else {
                    LOGGER.log(
                            Level.WARNING, "JDBCDataStore was disposed:" + disposedBy, disposedBy);
                    throw new IllegalStateException(
                            "DataSource not available after calling dispose().");
                }
            } else {
                throw new IllegalStateException(
                        "DataSource not available after calling dispose() or before being set.");
            }
        }
        return dataSource;
    }

    /**
     * Sets the data source the datastore uses to obtain connections to the underlying database.
     *
     * @param dataSource The data source, never <code>null</code>.
     */
    public void setDataSource(DataSource dataSource) {
        if (this.dataSource != null) {
            LOGGER.log(
                    Level.FINE,
                    "Setting DataSource on JDBCDataStore that already has DataSource set");
        }
        if (dataSource == null) {
            throw new IllegalArgumentException(
                    "JDBCDataStore's DataSource should not be set to null");
        }
        this.dataSource = dataSource;
    }

    /**
     * The schema from which this datastore is serving tables from.
     *
     * @return the schema, or <code>null</code> if non specified.
     */
    public String getDatabaseSchema() {
        return databaseSchema;
    }

    /**
     * Set the database schema for the datastore.
     *
     * <p>When this value is set only those tables which are part of the schema are served through
     * the datastore. This value can be set to <code>null</code> to specify no particular schema.
     *
     * @param databaseSchema The schema, may be <code>null</code>.
     */
    public void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    /**
     * The filter capabilities which reports which spatial operations the underlying database can
     * handle natively.
     *
     * @return The filter capabilities, never <code>null</code>.
     */
    public FilterCapabilities getFilterCapabilities() {
        if (dialect instanceof PreparedStatementSQLDialect)
            return ((PreparedStatementSQLDialect) dialect)
                    .createPreparedFilterToSQL()
                    .getCapabilities();
        else return ((BasicSQLDialect) dialect).createFilterToSQL().getCapabilities();
    }

    /**
     * Flag controlling if the datastore is supporting feature and geometry relationships with
     * associations
     */
    public boolean isAssociations() {
        return associations;
    }

    /**
     * Sets the flag controlling if the datastore is supporting feature and geometry relationships
     * with associations
     */
    public void setAssociations(boolean foreignKeyGeometries) {
        this.associations = foreignKeyGeometries;
    }

    /**
     * The sql type to java type mappings that the datastore uses when reading and writing objects
     * to and from the database.
     *
     * <p>These mappings are derived from {@link
     * SQLDialect#registerSqlTypeToClassMappings(java.util.Map)}
     *
     * @return The mappings, never <code>null</code>.
     */
    public Map<Integer, Class<?>> getSqlTypeToClassMappings() {
        if (sqlTypeToClassMappings == null) {
            sqlTypeToClassMappings = new HashMap<>();
            dialect.registerSqlTypeToClassMappings(sqlTypeToClassMappings);
        }

        return sqlTypeToClassMappings;
    }

    /**
     * The sql type name to java type mappings that the dialect uses when
     * reading and writing objects to and from the database.
     * <p>
     * These mappings are derived from {@link SQLDialect#registerSqlTypeNameToClassMappings(Map)}
     * </p>
     *
     * @return The mappings, never <code>null<code>.
     */
    public Map<String, Class<?>> getSqlTypeNameToClassMappings() {
        if (sqlTypeNameToClassMappings == null) {
            sqlTypeNameToClassMappings = new HashMap<>();
            dialect.registerSqlTypeNameToClassMappings(sqlTypeNameToClassMappings);
        }

        return sqlTypeNameToClassMappings;
    }

    /**
     * The java type to sql type mappings that the datastore uses when reading and writing objects
     * to and from the database.
     *
     * <p>These mappings are derived from {@link SQLDialect#registerClassToSqlMappings(Map)}
     *
     * @return The mappings, never <code>null</code>.
     */
    public Map<Class<?>, Integer> getClassToSqlTypeMappings() {
        if (classToSqlTypeMappings == null) {
            HashMap<Class<?>, Integer> classToSqlTypeMappings = new HashMap<>();
            dialect.registerClassToSqlMappings(classToSqlTypeMappings);
            this.classToSqlTypeMappings = classToSqlTypeMappings;
        }
        return classToSqlTypeMappings;
    }

    /**
     * Returns any ovverides which map integer constants for database types (from {@link Types}) to
     * database type names.
     *
     * <p>This method will return an empty map when there are no overrides.
     */
    public Map<Integer, String> getSqlTypeToSqlTypeNameOverrides() {
        if (sqlTypeToSqlTypeNameOverrides == null) {
            sqlTypeToSqlTypeNameOverrides = new HashMap<>();
            dialect.registerSqlTypeToSqlTypeNameOverrides(sqlTypeToSqlTypeNameOverrides);
        }

        return sqlTypeToSqlTypeNameOverrides;
    }

    /**
     * Returns a map integer constants for database types (from {@link Types}) to database type
     * names.
     *
     * <p>This method will return an empty map when there are no overrides.
     */
    public ConcurrentHashMap<Integer, String> getDBsqlTypesCache() {
        if (dBsqlTypesCache == null) {
            dBsqlTypesCache = new ConcurrentHashMap<>();
        }

        return dBsqlTypesCache;
    }

    /** Returns the supported aggregate functions and the visitors they map to. */
    public Map<Class<? extends FeatureVisitor>, String> getAggregateFunctions() {
        if (aggregateFunctions == null) {
            aggregateFunctions = new HashMap<>();
            dialect.registerAggregateFunctions(aggregateFunctions);
        }
        return aggregateFunctions;
    }

    /**
     * Returns the java type mapped to the specified sql type.
     *
     * <p>If there is no such type mapped to <tt>sqlType</tt>, <code>null</code> is returned.
     *
     * @param sqlType The integer constant for the sql type from {@link Types}.
     * @return The mapped java class, or <code>null</code>. if no such mapping exists.
     */
    public Class<?> getMapping(int sqlType) {
        return getSqlTypeToClassMappings().get(Integer.valueOf(sqlType));
    }

    /**
     * Returns the java type mapped to the specified sql type name.
     *
     * <p>If there is no such type mapped to <tt>sqlTypeName</tt>, <code>null</code> is returned.
     *
     * @param sqlTypeName The name of the sql type.
     * @return The mapped java class, or <code>null</code>. if no such mapping exists.
     */
    public Class<?> getMapping(String sqlTypeName) {
        return getSqlTypeNameToClassMappings().get(sqlTypeName);
    }

    /**
     * Returns the sql type mapped to the specified java type.
     *
     * <p>If there is no such type mapped to <tt>clazz</tt>, <code>Types.OTHER</code> is returned.
     *
     * @param clazz The java class.
     * @return The mapped sql type from {@link Types}, Types.OTHER if no such mapping exists.
     */
    public Integer getMapping(Class<?> clazz) {
        Integer mapping = getClassToSqlTypeMappings().get(clazz);

        // check for arrays, but don't get fooled by BLOB/CLOB java counterparts
        if (mapping == null && clazz.isArray()) {
            mapping = Types.ARRAY;
        }

        if (mapping == null) {
            // no match, try a "fuzzy" match in which we find the super class which matches best
            List<Map.Entry<Class<?>, Integer>> matches = new ArrayList<>();
            for (Map.Entry<Class<?>, Integer> e : getClassToSqlTypeMappings().entrySet()) {
                if (e.getKey().isAssignableFrom(clazz)) {
                    matches.add(e);
                }
            }
            if (!matches.isEmpty()) {
                if (matches.size() == 1) {
                    // single match, great, use it
                    mapping = matches.get(0).getValue();
                } else {
                    // sort to match lowest class in type hierarchy, if we end up with a list like:
                    // A, B where B is a super class of A, then chose A since it is the closest
                    // subclass to match

                    Collections.sort(
                            matches,
                            (o1, o2) -> {
                                if (o1.getKey().isAssignableFrom(o2.getKey())) {
                                    return 1;
                                }
                                if (o2.getKey().isAssignableFrom(o1.getKey())) {
                                    return -1;
                                }
                                return 0;
                            });
                    if (matches.get(1).getKey().isAssignableFrom(matches.get(0).getKey())) {
                        mapping = matches.get(0).getValue();
                    }
                }
            }
        }
        if (mapping == null) {
            mapping = Types.OTHER;
            LOGGER.warning("No mapping for " + clazz.getName());
        }

        return mapping;
    }

    /**
     * Creates a table in the underlying database from the specified table.
     *
     * <p>This method will map the classes of the attributes of <tt>featureType</tt> to sql types
     * and generate a 'CREATE TABLE' statement against the underlying database.
     *
     * @see DataStore#createSchema(SimpleFeatureType)
     * @throws IllegalArgumentException If the table already exists.
     * @throws IOException If the table cannot be created due to an error.
     */
    @Override
    public void createSchema(final SimpleFeatureType featureType) throws IOException {
        if (entry(featureType.getName()) != null) {
            String msg = "Schema '" + featureType.getName() + "' already exists";
            throw new IllegalArgumentException(msg);
        }

        // execute the create table statement
        // TODO: create a primary key and a spatial index
        Connection cx = createConnection();

        try {
            String sql = createTableSQL(featureType, cx);
            LOGGER.log(Level.FINE, "Create schema: {0}", sql);

            Statement st = cx.createStatement();

            try {
                st.execute(sql);
            } finally {
                closeSafe(st);
            }

            dialect.postCreateTable(databaseSchema, featureType, cx);
        } catch (Exception e) {
            String msg = "Error occurred creating table";
            throw (IOException) new IOException(msg).initCause(e);
        } finally {
            closeSafe(cx);
        }
    }

    @Override
    public void removeSchema(String typeName) throws IOException {
        removeSchema(name(typeName));
    }

    @Override
    public void removeSchema(Name typeName) throws IOException {
        if (entry(typeName) == null) {
            String msg = "Schema '" + typeName + "' does not exist";
            throw new IllegalArgumentException(msg);
        }

        // check for virtual table
        if (virtualTables.containsKey(typeName.getLocalPart())) {
            dropVirtualTable(typeName.getLocalPart());
            return;
        }

        SimpleFeatureType featureType = getSchema(typeName);

        // execute the drop table statement
        Connection cx = createConnection();
        try {
            // give the dialect a chance to cleanup pre
            dialect.preDropTable(databaseSchema, featureType, cx);

            String sql = dropTableSQL(featureType, cx);
            LOGGER.log(Level.FINE, "Drop schema: {0}", sql);

            Statement st = cx.createStatement();

            try {
                st.execute(sql);
            } finally {
                closeSafe(st);
            }

            dialect.postDropTable(databaseSchema, featureType, cx);
            removeEntry(typeName);
        } catch (Exception e) {
            String msg = "Error occurred dropping table";
            throw (IOException) new IOException(msg).initCause(e);
        } finally {
            closeSafe(cx);
        }
    }

    /** */
    @Override
    public Object getGmlObject(GmlObjectId id, Hints hints) throws IOException {
        // geometry?
        if (isAssociations()) {

            Connection cx = createConnection();
            try {
                try {
                    Statement st = null;
                    ResultSet rs = null;

                    if (getSQLDialect() instanceof PreparedStatementSQLDialect) {
                        st = selectGeometrySQLPS(id.getID(), cx);
                        rs = ((PreparedStatement) st).executeQuery();
                    } else {
                        String sql = selectGeometrySQL(id.getID());
                        LOGGER.log(Level.FINE, "Get GML object: {0}", sql);

                        st = cx.createStatement();
                        rs = st.executeQuery(sql);
                    }

                    try {
                        if (rs.next()) {
                            // read the geometry
                            Geometry g =
                                    getSQLDialect()
                                            .decodeGeometryValue(
                                                    null,
                                                    rs,
                                                    "geometry",
                                                    getGeometryFactory(),
                                                    cx,
                                                    hints);

                            // read the metadata
                            String name = rs.getString("name");
                            String desc = rs.getString("description");
                            setGmlProperties(g, id.getID(), name, desc);

                            return g;
                        }
                    } finally {
                        closeSafe(rs);
                        closeSafe(st);
                    }

                } catch (SQLException e) {
                    throw (IOException) new IOException().initCause(e);
                }
            } finally {
                closeSafe(cx);
            }
        }

        // regular feature, first feature out the feature type
        int i = id.getID().indexOf('.');
        if (i == -1) {
            LOGGER.info("Unable to determine feature type for GmlObjectId:" + id);
            return null;
        }

        // figure out the type name from the id
        String featureTypeName = id.getID().substring(0, i);
        SimpleFeatureType featureType = getSchema(featureTypeName);
        if (featureType == null) {
            throw new IllegalArgumentException("No such feature type: " + featureTypeName);
        }

        // load the feature
        Id filter = getFilterFactory().id(Collections.singleton(id));
        Query query = new Query(featureTypeName);
        query.setFilter(filter);
        query.setHints(hints);

        SimpleFeatureCollection features = getFeatureSource(featureTypeName).getFeatures(query);
        if (!features.isEmpty()) {
            try (SimpleFeatureIterator fi = features.features()) {
                if (fi.hasNext()) {
                    return fi.next();
                }
            }
        }

        return null;
    }

    /**
     * Creates a new instance of {@link JDBCFeatureStore}.
     *
     * @see ContentDataStore#createFeatureSource(ContentEntry)
     */
    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        // grab the schema, it carries a flag telling us if the feature type is read only
        SimpleFeatureType schema = entry.getState(Transaction.AUTO_COMMIT).getFeatureType();
        if (schema == null) {
            // if the schema still haven't been computed, force its computation so
            // that we can decide if the feature type is read only
            schema = new JDBCFeatureSource(entry, null).buildFeatureType();
            entry.getState(Transaction.AUTO_COMMIT).setFeatureType(schema);
        }

        Object readOnlyMarker = schema.getUserData().get(JDBC_READ_ONLY);
        if (Boolean.TRUE.equals(readOnlyMarker)) {
            return new JDBCFeatureSource(entry, null);
        }
        return new JDBCFeatureStore(entry, null);
    }

    //    /**
    //     * Creates a new instance of {@link JDBCTransactionState}.
    //     */
    //    protected State createTransactionState(ContentSimpleFeatureSource featureSource)
    //        throws IOException {
    //        return new JDBCTransactionState((JDBCFeatureStore) featureSource);
    //    }

    /**
     * Creates an instanceof {@link JDBCState}.
     *
     * @see ContentDataStore#createContentState(ContentEntry)
     */
    @Override
    protected ContentState createContentState(ContentEntry entry) {
        JDBCState state = new JDBCState(entry);
        state.setExposePrimaryKeyColumns(exposePrimaryKeyColumns);
        return state;
    }

    /**
     * Generates the list of type names provided by the database.
     *
     * <p>The list is generated from the underlying database metadata.
     */
    @Override
    protected List<Name> createTypeNames() throws IOException {
        Connection cx = createConnection();

        /*
         *        <LI><B>TABLE_CAT</B> String => table catalog (may be <code>null</code>)
         *        <LI><B>TABLE_SCHEM</B> String => table schema (may be <code>null</code>)
         *        <LI><B>TABLE_NAME</B> String => table name
         *        <LI><B>TABLE_TYPE</B> String => table type.  Typical types are "TABLE",
         *                        "VIEW",        "SYSTEM TABLE", "GLOBAL TEMPORARY",
         *                        "LOCAL TEMPORARY", "ALIAS", "SYNONYM".
         *        <LI><B>REMARKS</B> String => explanatory comment on the table
         *  <LI><B>TYPE_CAT</B> String => the types catalog (may be <code>null</code>)
         *  <LI><B>TYPE_SCHEM</B> String => the types schema (may be <code>null</code>)
         *  <LI><B>TYPE_NAME</B> String => type name (may be <code>null</code>)
         *  <LI><B>SELF_REFERENCING_COL_NAME</B> String => name of the designated
         *                  "identifier" column of a typed table (may be <code>null</code>)
         *        <LI><B>REF_GENERATION</B> String => specifies how values in
         *                  SELF_REFERENCING_COL_NAME are created. Values are
         *                  "SYSTEM", "USER", "DERIVED". (may be <code>null</code>)
         */
        List<Name> typeNames = new ArrayList<>();

        try {
            DatabaseMetaData metaData = cx.getMetaData();
            Set<String> availableTableTypes = new HashSet<>();

            ResultSet tableTypes = null;
            try {
                tableTypes = metaData.getTableTypes();
                while (tableTypes.next()) {
                    availableTableTypes.add(tableTypes.getString("TABLE_TYPE"));
                }
            } finally {
                closeSafe(tableTypes);
            }
            Set<String> queryTypes = new HashSet<>();
            for (String desiredTableType : dialect.getDesiredTablesType()) {
                if (availableTableTypes.contains(desiredTableType)) {
                    queryTypes.add(desiredTableType);
                }
            }
            ResultSet tables =
                    metaData.getTables(
                            null,
                            escapeNamePattern(metaData, databaseSchema),
                            "%",
                            queryTypes.toArray(new String[0]));
            try {
                if (fetchSize > 1) {
                    tables.setFetchSize(fetchSize);
                }
                while (tables.next()) {
                    String schemaName = tables.getString("TABLE_SCHEM");
                    String tableName = tables.getString("TABLE_NAME");

                    // use the dialect to filter
                    if (!dialect.includeTable(schemaName, tableName, cx)) {
                        continue;
                    }

                    typeNames.add(new NameImpl(namespaceURI, tableName));
                }
            } finally {
                closeSafe(tables);
            }
        } catch (SQLException e) {
            throw (IOException)
                    new IOException("Error occurred getting table name list.").initCause(e);
        } finally {
            closeSafe(cx);
        }

        for (String virtualTable : virtualTables.keySet()) {
            typeNames.add(new NameImpl(namespaceURI, virtualTable));
        }
        return typeNames;
    }

    /**
     * Returns the primary key object for a particular entry, deriving it from the underlying
     * database metadata.
     */
    protected PrimaryKey getPrimaryKey(ContentEntry entry) throws IOException {
        JDBCState state = (JDBCState) entry.getState(Transaction.AUTO_COMMIT);

        if (state.getPrimaryKey() == null) {
            synchronized (this) {
                if (state.getPrimaryKey() == null) {
                    // get metadata from database
                    Connection cx = createConnection();

                    try {
                        PrimaryKey pkey = null;
                        String tableName = entry.getName().getLocalPart();
                        if (virtualTables.containsKey(tableName)) {
                            VirtualTable vt = virtualTables.get(tableName);
                            if (vt.getPrimaryKeyColumns().size() == 0) {
                                pkey = new NullPrimaryKey(tableName);
                            } else {
                                List<ColumnMetadata> metas =
                                        JDBCFeatureSource.getColumnMetadata(cx, vt, dialect, this);

                                List<PrimaryKeyColumn> kcols = new ArrayList<>();
                                for (String pkName : vt.getPrimaryKeyColumns()) {
                                    // look for the pk type
                                    Class binding = null;
                                    for (ColumnMetadata meta : metas) {
                                        if (meta.name.equals(pkName)) {
                                            binding = meta.binding;
                                        }
                                    }

                                    // we build a pk without type, the JDBCFeatureStore will do this
                                    // for us while building the primary key
                                    kcols.add(new NonIncrementingPrimaryKeyColumn(pkName, binding));
                                }
                                pkey = new PrimaryKey(tableName, kcols);
                            }
                        } else {
                            try {
                                pkey =
                                        primaryKeyFinder.getPrimaryKey(
                                                this, databaseSchema, tableName, cx);
                            } catch (SQLException e) {
                                LOGGER.log(
                                        Level.WARNING,
                                        "Failure occurred while looking up the primary key with "
                                                + "finder: "
                                                + primaryKeyFinder,
                                        e);
                            }

                            if (pkey == null) {
                                String msg =
                                        "No primary key or unique index found for "
                                                + tableName
                                                + ".";
                                LOGGER.info(msg);

                                pkey = new NullPrimaryKey(tableName);
                            }
                        }

                        state.setPrimaryKey(pkey);
                    } catch (SQLException e) {
                        String msg = "Error looking up primary key";
                        throw (IOException) new IOException(msg).initCause(e);
                    } finally {
                        closeSafe(cx);
                    }
                }
            }
        }

        return state.getPrimaryKey();
    }

    /** Checks whether the tableName corresponds to a view */
    boolean isView(DatabaseMetaData metaData, String databaseSchema, String tableName)
            throws SQLException {

        ResultSet tables = null;
        try {
            tables =
                    metaData.getTables(
                            null,
                            escapeNamePattern(metaData, databaseSchema),
                            escapeNamePattern(metaData, tableName),
                            new String[] {"VIEW"});
            return tables.next();
        } finally {
            closeSafe(tables);
        }
    }

    /*
     * Creates a key from a primary key or unique index.
     */
    PrimaryKey createPrimaryKey(
            ResultSet index, DatabaseMetaData metaData, String tableName, Connection cx)
            throws SQLException {
        ArrayList<PrimaryKeyColumn> cols = new ArrayList<>();

        while (index.next()) {
            String columnName = index.getString("COLUMN_NAME");
            // work around. For some reason the first record returned is always 'empty'
            // this was tested on Oracle and Postgres databases
            if (columnName == null) {
                continue;
            }

            // look up the type ( should only be one row )
            Class columnType = getColumnType(metaData, databaseSchema, tableName, columnName);

            // determine which type of primary key we have
            PrimaryKeyColumn col = null;

            // 1. Auto Incrementing?
            Statement st = cx.createStatement();

            try {
                // not actually going to get data
                st.setFetchSize(1);

                StringBuffer sql = new StringBuffer();
                sql.append("SELECT ");
                dialect.encodeColumnName(null, columnName, sql);
                sql.append(" FROM ");
                encodeTableName(tableName, sql, null);

                sql.append(" WHERE 0=1");

                LOGGER.log(Level.FINE, "Grabbing table pk metadata: {0}", sql);

                ResultSet rs = st.executeQuery(sql.toString());

                try {
                    if (rs.getMetaData().isAutoIncrement(1)) {
                        col = new AutoGeneratedPrimaryKeyColumn(columnName, columnType);
                    }
                } finally {
                    closeSafe(rs);
                }
            } finally {
                closeSafe(st);
            }

            // 2. Has a sequence?
            if (col == null) {
                try {
                    String sequenceName =
                            dialect.getSequenceForColumn(databaseSchema, tableName, columnName, cx);
                    if (sequenceName != null) {
                        col = new SequencedPrimaryKeyColumn(columnName, columnType, sequenceName);
                    }
                } catch (Exception e) {
                    // log the exception , and continue on
                    LOGGER.log(
                            Level.WARNING,
                            "Error occured determining sequence for "
                                    + columnName
                                    + ", "
                                    + tableName,
                            e);
                }
            }

            if (col == null) {
                col = new NonIncrementingPrimaryKeyColumn(columnName, columnType);
            }

            cols.add(col);
        }

        if (!cols.isEmpty()) {
            return new PrimaryKey(tableName, cols);
        }
        return null;
    }

    /**
     * Returns the type of the column by inspecting the metadata, with the collaboration of the
     * dialect
     */
    protected Class getColumnType(
            DatabaseMetaData metaData, String databaseSchema2, String tableName, String columnName)
            throws SQLException {
        ResultSet columns = null;
        try {
            columns =
                    metaData.getColumns(
                            null,
                            escapeNamePattern(metaData, databaseSchema),
                            escapeNamePattern(metaData, tableName),
                            escapeNamePattern(metaData, columnName));
            if (!columns.next()) {
                throw new SQLException("Could not find metadata for column");
            }

            int binding = columns.getInt("DATA_TYPE");
            Class columnType = getMapping(binding);

            if (columnType == null) {
                LOGGER.warning("No class for sql type " + binding);
                columnType = Object.class;
            }

            return columnType;
        } finally {
            closeSafe(columns);
        }
    }

    /**
     * Returns the primary key object for a particular feature type / table, deriving it from the
     * underlying database metadata.
     */
    public PrimaryKey getPrimaryKey(SimpleFeatureType featureType) throws IOException {
        return getPrimaryKey(ensureEntry(featureType.getName()));
    }

    /** Returns the expose primary key columns flag for the specified feature type */
    protected boolean isExposePrimaryKeyColumns(SimpleFeatureType featureType) throws IOException {
        ContentEntry entry = ensureEntry(featureType.getName());
        JDBCState state = (JDBCState) entry.getState(Transaction.AUTO_COMMIT);
        return state.isExposePrimaryKeyColumns();
    }

    /**
     * Returns the bounds of the features for a particular feature type / table.
     *
     * @param featureType The feature type / table.
     * @param query Specifies rows to include in bounds calculation, as well as how many features
     *     and the offset if needed
     */
    protected ReferencedEnvelope getBounds(
            SimpleFeatureType featureType, Query query, Connection cx) throws IOException {

        // handle geometryless case by returning an emtpy envelope
        if (featureType.getGeometryDescriptor() == null) return EMPTY_ENVELOPE;

        Statement st = null;
        ResultSet rs = null;
        ReferencedEnvelope bounds =
                ReferencedEnvelope.create(featureType.getCoordinateReferenceSystem());
        try {
            // try optimized bounds computation only if we're targeting the entire table
            if (isFullBoundsQuery(query, featureType)) {
                List<ReferencedEnvelope> result =
                        dialect.getOptimizedBounds(databaseSchema, featureType, cx);
                if (result != null && !result.isEmpty()) {
                    // merge the envelopes into one
                    for (ReferencedEnvelope envelope : result) {
                        bounds = mergeEnvelope(bounds, envelope);
                    }
                    return bounds;
                }
            }

            // build an aggregate query
            if (dialect instanceof PreparedStatementSQLDialect) {
                st = selectBoundsSQLPS(featureType, query, cx);
                rs = ((PreparedStatement) st).executeQuery();
            } else {
                String sql = selectBoundsSQL(featureType, query);
                LOGGER.log(Level.FINE, "Retrieving bounding box: {0}", sql);

                st = cx.createStatement();
                rs = st.executeQuery(sql);
            }

            // scan through all the rows (just in case a non aggregated function was used)
            // and through all the columns (in case we have multiple geometry columns)
            CoordinateReferenceSystem flatCRS =
                    CRS.getHorizontalCRS(featureType.getCoordinateReferenceSystem());
            final int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    final Envelope envelope =
                            dialect.decodeGeometryEnvelope(rs, i, st.getConnection());
                    if (envelope != null) {
                        if (envelope instanceof ReferencedEnvelope) {
                            bounds = mergeEnvelope(bounds, (ReferencedEnvelope) envelope);
                        } else {
                            bounds =
                                    mergeEnvelope(
                                            bounds, new ReferencedEnvelope(envelope, flatCRS));
                        }
                    }
                }
            }
        } catch (Exception e) {
            String msg = "Error occured calculating bounds for " + featureType.getTypeName();
            throw (IOException) new IOException(msg).initCause(e);
        } finally {
            closeSafe(rs);
            closeSafe(st);
        }

        return bounds;
    }

    /**
     * Returns true if the query will hit all the geometry columns with no row filtering (a
     * condition that allows to use spatial index statistics to compute the table bounds)
     */
    private boolean isFullBoundsQuery(Query query, SimpleFeatureType schema) {
        if (query == null) {
            return true;
        }
        if (!query.isMaxFeaturesUnlimited()) {
            return false; // there is a limit
        }
        if (query.getStartIndex() != null && query.getStartIndex() > 0) {
            return false; // there is an offset
        }
        if (!Filter.INCLUDE.equals(query.getFilter())) {
            return false;
        }
        if (query.getProperties() == Query.ALL_PROPERTIES) {
            return true;
        }

        List<String> names = Arrays.asList(query.getPropertyNames());
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            if (ad instanceof GeometryDescriptor) {
                if (!names.contains(ad.getLocalName())) {
                    return false;
                }
            }
        }

        return true;
    }

    /** Merges two envelopes handling possibly different CRS */
    ReferencedEnvelope mergeEnvelope(ReferencedEnvelope base, ReferencedEnvelope merge)
            throws TransformException, FactoryException {
        if (base == null || base.isNull()) {
            return merge;
        } else if (merge == null || merge.isNull()) {
            return base;
        } else {
            // reproject and merge
            final CoordinateReferenceSystem crsBase = base.getCoordinateReferenceSystem();
            final CoordinateReferenceSystem crsMerge = merge.getCoordinateReferenceSystem();
            if (crsBase == null) {
                merge.expandToInclude(base);
                return merge;
            } else if (crsMerge == null) {
                base.expandToInclude(base);
                return base;
            } else {
                // both not null, are they equal?
                if (!CRS.equalsIgnoreMetadata(crsBase, crsMerge)) {
                    merge = merge.transform(crsBase, true);
                }
                base.expandToInclude(merge);
                return base;
            }
        }
    }

    /** Returns the count of the features for a particular feature type / table. */
    protected int getCount(SimpleFeatureType featureType, Query query, Connection cx)
            throws IOException {

        CountVisitor v = new CountVisitor();
        getAggregateValue(v, featureType, query, cx);
        return v.getCount();
    }

    /**
     * Results the value of an aggregate function over a query.
     *
     * @return generated result, or null if unsupported
     */
    protected Object getAggregateValue(
            FeatureVisitor visitor, SimpleFeatureType featureType, Query query, Connection cx)
            throws IOException {
        // check if group by is supported by the underlying store
        if (isGroupByVisitor(visitor)
                && (!dialect.isGroupBySupported()
                        || !isSupportedGroupBy(featureType, (GroupByVisitor) visitor))) {
            return null;
        }
        // try to match the visitor with an aggregate function
        String function = matchAggregateFunction(visitor);
        if (function == null) {
            // this visitor is not supported
            return null;
        }
        // try to extract an aggregate attribute from the visitor
        Expression aggregateExpression = null;
        if (!isCountVisitor(visitor)) {
            aggregateExpression = getAggregateExpression(visitor);
            if (aggregateExpression != null && !fullySupports(aggregateExpression)) {
                return null;
            }
        }

        // if the visitor is limiting the result to a given start - max, we will
        // try to apply limits to the aggregate query
        LimitingVisitor limitingVisitor = null;
        if (visitor instanceof LimitingVisitor) {
            limitingVisitor = (LimitingVisitor) visitor;
        }
        // if the visitor is a group by visitor we extract the group by attributes
        List<Expression> groupByExpressions = extractGroupByExpressions(visitor);
        // result of the function
        try {
            Object result = null;
            List<Object> results = new ArrayList<>();
            Statement st = null;
            ResultSet rs = null;

            try {
                if (dialect instanceof PreparedStatementSQLDialect) {
                    st =
                            selectAggregateSQLPS(
                                    function,
                                    aggregateExpression,
                                    groupByExpressions,
                                    featureType,
                                    query,
                                    limitingVisitor,
                                    cx);
                    rs = ((PreparedStatement) st).executeQuery();
                } else {
                    String sql =
                            selectAggregateSQL(
                                    function,
                                    aggregateExpression,
                                    groupByExpressions,
                                    featureType,
                                    query,
                                    limitingVisitor);
                    LOGGER.fine(sql);

                    st = cx.createStatement();
                    st.setFetchSize(fetchSize);
                    rs = st.executeQuery(sql);
                }

                // give the dialect an opportunity to convert outputs, if needed, for databases
                // with a weak/problematic type system (e.g., sqlite)
                java.util.function.Function<Object, Object> converter =
                        dialect.getAggregateConverter(visitor, featureType);

                while (rs.next()) {
                    if (groupByExpressions == null || groupByExpressions.isEmpty()) {
                        Object value = rs.getObject(1);
                        result = converter.apply(value);
                        results.add(result);
                    } else {
                        results.add(
                                extractValuesFromResultSet(
                                        cx,
                                        featureType,
                                        rs,
                                        groupByExpressions,
                                        converter,
                                        query.getHints()));
                    }
                }
            } finally {
                closeSafe(rs);
                closeSafe(st);
            }

            if (groupByExpressions != null && !groupByExpressions.isEmpty()) {
                setResult(visitor, results);
                return results;
            } else if (setResult(visitor, results.size() > 1 ? results : result)) {
                return result == null ? results : result;
            }

            return null;
        } catch (SQLException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Checks if the groupBy is a supported one, that is, if it's possible to turn to SQL the
     * various {@link Expression} it's using
     */
    private boolean isSupportedGroupBy(SimpleFeatureType featureType, GroupByVisitor visitor) {
        return visitor.getGroupByAttributes().stream()
                .allMatch(
                        xp -> {
                            if (!fullySupports(xp)) return false;

                            // Geometry attributes require a GeometryDescriptor to be encoded and
                            // read back,
                            // cannot do that with a generic expression
                            Class type =
                                    (Class) xp.accept(new ExpressionTypeVisitor(featureType), null);
                            if (type == null || !Geometry.class.isAssignableFrom(type)) return true;

                            // the expression is a geometry, check it's an actual known attribute,
                            // and that the database can group on geometries
                            return getGeometryDescriptor(featureType, xp) != null
                                    && dialect.canGroupOnGeometry();
                        });
    }

    /**
     * Determines if the expression and all its sub expressions are supported.
     *
     * @param expression the expression to be tested.
     * @return true if all sub filters are supported, false otherwise.
     * @throws IllegalArgumentException If a null filter is passed in. As this function is recursive
     *     a null in a logic filter will also cause an error.
     */
    private boolean fullySupports(Expression expression) {
        if (expression == null) {
            throw new IllegalArgumentException("Null expression can not be unpacked");
        }

        FilterCapabilities filterCapabilities = getFilterCapabilities();

        if (!filterCapabilities.supports(expression.getClass())) {
            return false;
        }

        // check the known composite expressions
        if (expression instanceof BinaryExpression) {
            BinaryExpression be = (BinaryExpression) expression;
            return fullySupports(be.getExpression1()) && fullySupports(be.getExpression2());
        } else if (expression instanceof Function) {
            Function function = (Function) expression;
            for (Expression fe : function.getParameters()) {
                if (!fullySupports(fe)) {
                    return false;
                }
            }
        }

        return true;
    }

    // Helper method that checks if the visitor is of type count visitor.
    protected boolean isCountVisitor(FeatureVisitor visitor) {
        if (visitor instanceof CountVisitor) {
            // is count visitor nothing else to test
            return true;
        }
        // the visitor maybe wrapper by a group by visitor
        return isGroupByVisitor(visitor)
                && ((GroupByVisitor) visitor).getAggregateVisitor() instanceof CountVisitor;
    }

    /**
     * Helper method the checks if a feature visitor is a group by visitor,
     *
     * @param visitor the feature visitor
     * @return TRUE if the visitor is a group by visitor otherwise FALSE
     */
    protected boolean isGroupByVisitor(FeatureVisitor visitor) {
        return visitor instanceof GroupByVisitor;
    }

    /**
     * Helper method that will try to match a feature visitor with an aggregate function. If no
     * aggregate function machs the visitor NULL will be returned.
     *
     * @param visitor the feature visitor
     * @return the match aggregate function name, or NULL if no match
     */
    protected String matchAggregateFunction(FeatureVisitor visitor) {
        // if is a group by visitor we use use the internal aggregate visitor class otherwise we use
        // the visitor class
        Class visitorClass =
                isGroupByVisitor(visitor)
                        ? ((GroupByVisitor) visitor).getAggregateVisitor().getClass()
                        : visitor.getClass();
        String function = null;
        // try to find a matching aggregate function walking up the hierarchy if necessary
        while (function == null && visitorClass != null) {
            function = getAggregateFunctions().get(visitorClass);
            visitorClass = visitorClass.getSuperclass();
        }
        if (function == null) {
            // this visitor don't match any aggregate function NULL will be returned
            LOGGER.info(
                    "Unable to find aggregate function matching visitor: " + visitor.getClass());
        }
        return function;
    }

    private Expression getAggregateExpression(FeatureVisitor visitor) {
        // if is a group by visitor we need to use the internal aggregate visitor
        FeatureVisitor aggregateVisitor =
                isGroupByVisitor(visitor)
                        ? ((GroupByVisitor) visitor).getAggregateVisitor()
                        : visitor;
        Expression expression = getExpression(aggregateVisitor);
        if (expression == null) {
            // no aggregate attribute available, NULL will be returned
            LOGGER.info("Visitor " + visitor.getClass() + " has no aggregate attribute.");
            return null;
        }
        return expression;
    }

    /**
     * Helper method that extracts a list of group by attributes from a group by visitor. If the
     * visitor is not a group by visitor an empty list will be returned.
     *
     * @param visitor the feature visitor
     * @return the list of the group by attributes or an empty list
     */
    protected List<Expression> extractGroupByExpressions(FeatureVisitor visitor) {
        // if is a group by visitor we get the list of attributes expressions otherwise we get an
        // empty list
        List<Expression> expressions =
                isGroupByVisitor(visitor)
                        ? ((GroupByVisitor) visitor).getGroupByAttributes()
                        : new ArrayList<>();
        return expressions;
    }

    /**
     * Helper method that translate the result set to the appropriate group by visitor result format
     */
    protected GroupByVisitor.GroupByRawResult extractValuesFromResultSet(
            Connection cx,
            SimpleFeatureType featureType,
            ResultSet resultSet,
            List<Expression> groupBy,
            java.util.function.Function<Object, Object> converter,
            Hints hints)
            throws SQLException, IOException {
        List<Object> groupByValues = new ArrayList<>();
        int numberOfGroupByAttributes = groupBy.size();
        for (int i = 0; i < numberOfGroupByAttributes; i++) {
            GeometryDescriptor gd = getGeometryDescriptor(featureType, groupBy.get(i));
            Object result;
            if (gd != null) {
                result =
                        dialect.decodeGeometryValue(
                                gd, resultSet, i + 1, new GeometryFactory(), cx, hints);
            } else {
                result = resultSet.getObject(i + 1);
            }

            groupByValues.add(result);
        }
        Object aggregated = resultSet.getObject(numberOfGroupByAttributes + 1);
        Object converted = converter.apply(aggregated);
        return new GroupByVisitor.GroupByRawResult(groupByValues, converted);
    }

    /**
     * Helper method for getting the expression from a visitor TODO: Remove this method when there
     * is an interface for aggregate visitors. See GEOT-2325 for details.
     */
    Expression getExpression(FeatureVisitor visitor) {
        if (visitor instanceof CountVisitor) {
            return null;
        }
        try {
            Method g = visitor.getClass().getMethod("getExpression", null);
            if (g != null) {
                Object result = g.invoke(visitor, null);
                if (result instanceof Expression) {
                    return (Expression) result;
                }
            }
        } catch (Exception e) {
            // ignore for now
        }

        return null;
    }

    /**
     * Helper method for setting the result of a aggregate functino on a visitor. TODO: Remove this
     * method when there is an interface for aggregate visitors. See GEOT-2325 for details.
     */
    boolean setResult(FeatureVisitor visitor, Object result) {
        try {
            Method s = null;
            if (AGGREGATE_SETVALUE_CACHE.containsKey(visitor.getClass())) {
                s = AGGREGATE_SETVALUE_CACHE.get(visitor.getClass());
            } else {
                try {
                    s = visitor.getClass().getMethod("setValue", result.getClass());
                } catch (Exception e) {
                }

                if (s == null) {
                    for (Method m : visitor.getClass().getMethods()) {
                        if ("setValue".equals(m.getName()) && m.getParameterCount() == 1) {
                            s = m;
                            break;
                        }
                    }
                }
                AGGREGATE_SETVALUE_CACHE.put(visitor.getClass(), s);
            }
            if (s != null) {
                Class<?> type = s.getParameterTypes()[0];
                if (!type.isInstance(result)) {
                    // convert
                    Object converted = Converters.convert(result, type);
                    if (converted != null) {
                        result = converted;
                    } else {
                        // could not set value
                        return false;
                    }
                }

                s.invoke(visitor, result);
                return true;
            }
        } catch (Exception e) {
            LOGGER.log(
                    Level.INFO,
                    "Failed to set optimized result, will fall back on full collection visit",
                    e);
        }
        return false;
    }

    /** Inserts a new feature into the database for a particular feature type / table. */
    protected void insert(SimpleFeature feature, SimpleFeatureType featureType, Connection cx)
            throws IOException {
        insert(Collections.singletonList(feature), featureType, cx);
    }

    /**
     * Inserts a collection of new features into the database for a particular feature type / table.
     */
    protected void insert(
            Collection<? extends SimpleFeature> features,
            SimpleFeatureType featureType,
            Connection cx)
            throws IOException {
        PrimaryKey key = getPrimaryKey(featureType);

        // we do this in a synchronized block because we need to do two queries,
        // first to figure out what the id will be, then the insert statement
        synchronized (this) {
            try {
                if (dialect instanceof PreparedStatementSQLDialect) {
                    Map<InsertionClassifier, Collection<SimpleFeature>> kinds =
                            InsertionClassifier.classify(featureType, features);
                    for (InsertionClassifier kind : kinds.keySet()) {
                        insertPS(kinds.get(kind), kind, featureType, cx, key);
                    }
                } else {
                    Collection<SimpleFeature> useExistings = new ArrayList<>();
                    Collection<SimpleFeature> notUseExistings = new ArrayList<>();
                    for (SimpleFeature cur : features) {
                        (InsertionClassifier.useExisting(cur) ? useExistings : notUseExistings)
                                .add(cur);
                    }
                    insertNonPS(useExistings, featureType, cx, key, true);
                    insertNonPS(notUseExistings, featureType, cx, key, false);
                }
            } catch (SQLException e) {
                String msg = "Error inserting features";
                throw (IOException) new IOException(msg).initCause(e);
            }
        }
    }

    /** Specialized insertion for dialects that are using prepared statements. */
    private void insertPS(
            Collection<SimpleFeature> features,
            InsertionClassifier kind,
            SimpleFeatureType featureType,
            Connection cx,
            PrimaryKey key)
            throws IOException, SQLException {
        final PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        final KeysFetcher keysFetcher = KeysFetcher.create(this, cx, kind.useExisting, key);

        final String sql = buildInsertPS(kind, featureType, keysFetcher, dialect);
        LOGGER.log(Level.FINE, "Inserting new features with ps: {0}", sql);

        // create the prepared statement
        final PreparedStatement ps;
        if (keysFetcher.isPostInsert()) {
            // ask the DB to return the values of all the keys after the insertion
            ps = cx.prepareStatement(sql, keysFetcher.getColumnNames());
        } else {
            ps = cx.prepareStatement(sql);
        }
        try {
            for (SimpleFeature feature : features) {
                // set the attribute values
                int i = 1;
                for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
                    String colName = att.getLocalName();
                    // skip the pk columns in case we have exposed them, we grab the
                    // value from the pk itself
                    if (keysFetcher.isKey(colName)) {
                        continue;
                    }

                    Class binding = att.getType().getBinding();
                    EnumMapper mapper =
                            (EnumMapper) att.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);

                    Object value = feature.getAttribute(colName);
                    if (value == null && !att.isNillable()) {
                        throw new IOException(
                                "Cannot set a NULL value on the not null column " + colName);
                    }

                    if (Geometry.class.isAssignableFrom(binding)) {
                        Geometry g = (Geometry) value;
                        int srid = getGeometrySRID(g, att);
                        int dimension = getGeometryDimension(g, att);
                        dialect.setGeometryValue(g, dimension, srid, binding, ps, i);
                    } else if (this.dialect.isArray(att)) {
                        dialect.setArrayValue(value, att, ps, i, cx);
                    } else {
                        if (mapper != null) {
                            value = mapper.fromString((String) value);
                            binding = Integer.class;
                        }
                        dialect.setValue(value, binding, ps, i, cx);
                    }
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine((i) + " = " + value);
                    }
                    i++;
                }

                keysFetcher.setKeyValues(dialect, ps, cx, featureType, feature, i);

                dialect.onInsert(ps, cx, featureType);
                ps.addBatch();
            }
            int[] inserts = ps.executeBatch();
            checkAllInserted(inserts, features.size());
            keysFetcher.postInsert(featureType, features, ps);
        } finally {
            closeSafe(ps);
        }
    }

    static void checkAllInserted(int[] inserts, int size) throws IOException {
        int sum = 0;
        for (int cur : inserts) {
            if (cur == PreparedStatement.SUCCESS_NO_INFO) {
                return; // cannot check
            } else if (cur == PreparedStatement.EXECUTE_FAILED) {
                throw new IOException("Failed to insert some features");
            }
            sum += cur;
        }
        if (sum != size) {
            throw new IOException("Failed to insert some features");
        }
    }

    /** Build the insert statement that will be used in a PreparedStatement. */
    private String buildInsertPS(
            InsertionClassifier kind,
            SimpleFeatureType featureType,
            KeysFetcher keysFetcher,
            PreparedStatementSQLDialect dialect)
            throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        encodeTableName(featureType.getTypeName(), sql, null);

        // column names
        sql.append(" ( ");

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            String colName = featureType.getDescriptor(i).getLocalName();
            // skip the pk columns in case we have exposed them
            if (keysFetcher.isKey(colName)) {
                continue;
            }

            dialect.encodeColumnName(null, colName, sql);
            sql.append(",");
        }

        // primary key values
        keysFetcher.addKeyColumns(sql);
        sql.setLength(sql.length() - 1); // remove the last coma

        // values
        sql.append(" ) VALUES ( ");
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            String colName = att.getLocalName();
            // skip the pk columns in case we have exposed them, we grab the
            // value from the pk itself
            if (keysFetcher.isKey(colName)) {
                continue;
            }

            // geometries might need special treatment, delegate to the dialect
            if (att instanceof GeometryDescriptor) {
                Class<? extends Geometry> geometryClass =
                        kind.geometryTypes.get(att.getName().getLocalPart());
                dialect.prepareGeometryValue(
                        geometryClass,
                        getDescriptorDimension(att),
                        getDescriptorSRID(att),
                        att.getType().getBinding(),
                        sql);
            } else {
                sql.append("?");
            }
            sql.append(",");
        }
        keysFetcher.addKeyBindings(sql);

        sql.setLength(sql.length() - 1);
        sql.append(")");
        return sql.toString();
    }

    /** Specialized insertion for dialects that are not using prepared statements. */
    private void insertNonPS(
            Collection<? extends SimpleFeature> features,
            SimpleFeatureType featureType,
            Connection cx,
            PrimaryKey key,
            boolean useExisting)
            throws IOException, SQLException {
        if (features.isEmpty()) {
            return;
        }
        final Statement st = cx.createStatement();
        final KeysFetcher keysFetcher = KeysFetcher.create(this, cx, useExisting, key);
        try {
            for (SimpleFeature feature : features) {
                String sql = insertSQL(featureType, feature, keysFetcher, cx);

                ((BasicSQLDialect) dialect).onInsert(st, cx, featureType);

                LOGGER.log(Level.FINE, "Inserting new feature: {0}", sql);
                if (keysFetcher.hasAutoGeneratedKeys()) {
                    st.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                } else {
                    st.executeUpdate(sql);
                }

                keysFetcher.postInsert(featureType, feature, cx, st);
            }
        } finally {
            closeSafe(st);
        }
    }

    /** Updates an existing feature(s) in the database for a particular feature type / table. */
    protected void update(
            SimpleFeatureType featureType,
            List<AttributeDescriptor> attributes,
            List<Object> values,
            Filter filter,
            Connection cx)
            throws IOException, SQLException {
        update(
                featureType,
                attributes.toArray(new AttributeDescriptor[attributes.size()]),
                values.toArray(new Object[values.size()]),
                filter,
                cx);
    }

    /** Updates an existing feature(s) in the database for a particular feature type / table. */
    protected void update(
            SimpleFeatureType featureType,
            AttributeDescriptor[] attributes,
            Object[] values,
            Filter filter,
            Connection cx)
            throws IOException, SQLException {
        if ((attributes == null) || (attributes.length == 0)) {
            LOGGER.warning("Update called with no attributes, doing nothing.");

            return;
        }

        // grab primary key
        PrimaryKey key = null;
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> pkColumnNames = getColumnNames(key);

        // do a check to ensure that the update includes at least one non primary key column
        boolean nonPkeyColumn = false;
        for (AttributeDescriptor att : attributes) {
            if (!pkColumnNames.contains(att.getLocalName())) {
                nonPkeyColumn = true;
            }
        }
        if (!nonPkeyColumn) {
            throw new IllegalArgumentException(
                    "Illegal update, must include at least one non primary key column, "
                            + "all primary key columns are ignored.");
        }
        if (dialect instanceof PreparedStatementSQLDialect) {
            try {
                PreparedStatement ps =
                        updateSQLPS(featureType, attributes, values, filter, pkColumnNames, cx);
                try {
                    ((PreparedStatementSQLDialect) dialect).onUpdate(ps, cx, featureType);
                    ps.execute();
                } finally {
                    closeSafe(ps);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            String sql = updateSQL(featureType, attributes, values, filter, pkColumnNames);

            try {
                Statement st = cx.createStatement();

                try {
                    ((BasicSQLDialect) dialect).onUpdate(st, cx, featureType);

                    LOGGER.log(Level.FINE, "Updating feature: {0}", sql);
                    st.execute(sql);
                } finally {
                    closeSafe(st);
                }
            } catch (SQLException e) {
                String msg = "Error occured updating features";
                throw (IOException) new IOException(msg).initCause(e);
            }
        }
    }

    /** Deletes an existing feature in the database for a particular feature type / fid. */
    protected void delete(SimpleFeatureType featureType, String fid, Connection cx)
            throws IOException {
        Filter filter = filterFactory.id(Collections.singleton(filterFactory.featureId(fid)));
        delete(featureType, filter, cx);
    }

    /** Deletes an existing feature(s) in the database for a particular feature type / table. */
    protected void delete(SimpleFeatureType featureType, Filter filter, Connection cx)
            throws IOException {

        Statement st = null;
        try {
            try {
                if (dialect instanceof PreparedStatementSQLDialect) {
                    st = deleteSQLPS(featureType, filter, cx);
                    @SuppressWarnings("PMD.CloseResource") // actually being closed later
                    PreparedStatement ps = (PreparedStatement) st;

                    ((PreparedStatementSQLDialect) dialect).onDelete(ps, cx, featureType);
                    ps.execute();
                } else {
                    String sql = deleteSQL(featureType, filter);

                    st = cx.createStatement();
                    ((BasicSQLDialect) dialect).onDelete(st, cx, featureType);

                    LOGGER.log(Level.FINE, "Removing feature(s): {0}", sql);
                    st.execute(sql);
                }
            } finally {
                closeSafe(st);
            }
        } catch (SQLException e) {
            String msg = "Error occured during delete";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }

    /**
     * Returns a JDBC Connection to the underlying database for the specified GeoTools {@link
     * Transaction}. This has two main use cases:
     *
     * <ul>
     *   <li>Independently accessing the underlying database directly reusing the connection pool
     *       contained in the {@link JDBCDataStore}
     *   <li>Performing some direct access to the database in the same JDBC transaction as the
     *       Geotools code
     * </ul>
     *
     * The connection shall be used in a different way depending on the use case:
     *
     * <ul>
     *   <li>If the transaction is {@link Transaction#AUTO_COMMIT} or if the transaction is not
     *       shared with this data store and originating {@link FeatureStore} objects it is the duty
     *       of the caller to properly close the connection after usage, failure to do so will
     *       result in the connection pool loose one available connection permanently
     *   <li>If the transaction is on the other side a valid transaction is shared with Geotools the
     *       client code should refrain from closing the connection, committing or rolling back, and
     *       use the {@link Transaction} facilities to do so instead
     * </ul>
     *
     * @param t The GeoTools transaction. Can be {@code null}, in that case a new connection will be
     *     returned (as if {@link Transaction#AUTO_COMMIT} was provided)
     */
    public Connection getConnection(Transaction t) throws IOException {
        // short circuit this state, all auto commit transactions are using the same
        if (t == Transaction.AUTO_COMMIT) {
            Connection cx = createConnection();
            try {
                if (!cx.getAutoCommit()) {
                    cx.setAutoCommit(true);
                }
            } catch (SQLException e) {
                throw (IOException) new IOException().initCause(e);
            }
            return cx;
        }

        JDBCTransactionState tstate = (JDBCTransactionState) t.getState(this);
        if (tstate != null) {
            return tstate.cx;
        } else {
            Connection cx = createConnection();
            try {
                cx.setAutoCommit(false);
            } catch (SQLException e) {
                throw (IOException) new IOException().initCause(e);
            }

            tstate = new JDBCTransactionState(cx, this);
            t.putState(this, tstate);
            return cx;
        }
    }

    /** Gets a database connection for the specified feature store. */
    protected final Connection getConnection(JDBCState state) throws IOException {
        return getConnection(state.getTransaction());
    }

    /**
     * Creates a new connection.
     *
     * <p>Callers of this method should close the connection when done with it. .
     */
    protected final Connection createConnection() {
        try {
            LOGGER.fine("CREATE CONNECTION");

            Connection cx = getDataSource().getConnection();

            // isolation level is not set in the datastore, see
            // http://jira.codehaus.org/browse/GEOT-2021

            // call dialect callback to initialize the connection
            dialect.initializeConnection(cx);

            // if there is any lifecycle listener use it
            if (!connectionLifecycleListeners.isEmpty()) {
                List<ConnectionLifecycleListener> locals =
                        new ArrayList<>(connectionLifecycleListeners);
                return new LifecycleConnection(this, cx, locals);
            }
            return cx;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to obtain connection: " + e.getMessage(), e);
        }
    }

    /**
     * Releases an existing connection (paying special attention to {@link Transaction#AUTO_COMMIT}.
     *
     * <p>If the state is based off the AUTO_COMMIT transaction - close using {@link
     * #closeSafe(Connection)}. Otherwise wait until the transaction itself is closed to close the
     * connection.
     */
    protected final void releaseConnection(Connection cx, JDBCState state) {
        if (state.getTransaction() == Transaction.AUTO_COMMIT) {
            closeSafe(cx);
        }
    }

    /**
     * Calls through to: <code><pre>
     *   encodeFID(pkey, rs, 0);
     * </pre></code>
     */
    protected String encodeFID(PrimaryKey pkey, ResultSet rs) throws SQLException, IOException {
        return encodeFID(pkey, rs, 0);
    }

    /**
     * Encodes a feature id from a primary key and result set values.
     *
     * <p><tt>offset</tt> specifies where in the result set to start from when reading values for
     * the primary key.
     */
    protected String encodeFID(PrimaryKey pkey, ResultSet rs, int offset)
            throws SQLException, IOException {
        // no pk columns
        List<PrimaryKeyColumn> columns = pkey.getColumns();
        if (columns.isEmpty()) {
            return SimpleFeatureBuilder.createDefaultFeatureId();
        }

        // just one, no need to build support structures
        if (columns.size() == 1) {
            return dialect.getPkColumnValue(rs, columns.get(0), offset + 1);
        }

        // more than one
        List<Object> keyValues = new ArrayList<>();
        for (int i = 0; i < columns.size(); i++) {
            String o = dialect.getPkColumnValue(rs, columns.get(0), offset + i + 1);
            keyValues.add(o);
        }
        return encodeFID(keyValues);
    }

    protected static String encodeFID(List<Object> keyValues) {
        StringBuffer fid = new StringBuffer();
        for (Object o : keyValues) {
            fid.append(o).append(".");
        }
        fid.setLength(fid.length() - 1);
        return fid.toString();
    }

    /**
     * Decodes a fid into its components based on a primary key.
     *
     * @param strict If set to true the value of the fid will be validated against the type of the
     *     key columns. If a conversion can not be made, an exception will be thrown.
     */
    public static List<Object> decodeFID(PrimaryKey key, String FID, boolean strict) {
        // strip off the feature type name
        if (FID.startsWith(key.getTableName() + ".")) {
            FID = FID.substring(key.getTableName().length() + 1);
        }

        try {
            FID = URLDecoder.decode(FID, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        // check for case of multi column primary key and try to backwards map using
        // "." as a seperator of values
        List<Object> values = null;
        if (key.getColumns().size() > 1) {
            String[] split = FID.split("\\.");

            // copy over to avoid array store exception
            values = new ArrayList<>(split.length);
            for (String s : split) {
                values.add(s);
            }
        } else {
            // single value case
            values = new ArrayList<>();
            values.add(FID);
        }
        if (values.size() != key.getColumns().size()) {
            throw new IllegalArgumentException(
                    "Illegal fid: "
                            + FID
                            + ". Expected "
                            + key.getColumns().size()
                            + " values but got "
                            + values.size());
        }

        // convert to the type of the key
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (value != null) {
                Class<?> type = key.getColumns().get(i).getType();
                Object converted = Converters.convert(value, type);
                if (converted != null) {
                    values.set(i, converted);
                }
                if (strict && !type.isInstance(values.get(i))) {
                    throw new IllegalArgumentException(
                            "Value " + values.get(i) + " illegal for type " + type.getName());
                }
            }
        }

        return values;
    }

    /**
     * Determines if a primary key is made up entirely of column which are generated via an
     * auto-generating column or a sequence.
     */
    protected boolean isGenerated(PrimaryKey pkey) {
        for (PrimaryKeyColumn col : pkey.getColumns()) {
            if (!(col instanceof AutoGeneratedPrimaryKeyColumn)) {
                return false;
            }
        }

        return true;
    }

    //
    // SQL generation
    //
    /** Generates a 'CREATE TABLE' sql statement. */
    protected String createTableSQL(SimpleFeatureType featureType, Connection cx) throws Exception {
        // figure out the names and types of the columns
        String[] columnNames = new String[featureType.getAttributeCount()];
        Class[] classes = new Class[featureType.getAttributeCount()];

        // figure out which columns can not be null
        boolean[] nillable = new boolean[featureType.getAttributeCount()];

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = featureType.getDescriptor(i);

            // column name
            columnNames[i] = attributeType.getLocalName();

            // column type
            classes[i] = attributeType.getType().getBinding();

            // can be null?
            nillable[i] = attributeType.getMinOccurs() <= 0 || attributeType.isNillable();

            // eventual options
        }

        String[] sqlTypeNames = getSQLTypeNames(featureType.getAttributeDescriptors(), cx);
        for (int i = 0; i < sqlTypeNames.length; i++) {
            if (sqlTypeNames[i] == null) {
                String msg = "Unable to map " + columnNames[i] + "( " + classes[i].getName() + ")";
                throw new RuntimeException(msg);
            }
        }

        return createTableSQL(
                featureType.getTypeName(),
                columnNames,
                sqlTypeNames,
                nillable,
                findPrimaryKeyColumnName(featureType),
                featureType);
    }

    /*
     * search feature type looking for suitable unique column for primary key.
     */
    protected String findPrimaryKeyColumnName(SimpleFeatureType featureType) {
        String[] suffix = {"", "_1", "_2"};
        String[] base = {"fid", "id", "gt_id", "ogc_fid"};

        for (String b : base) {
            O:
            for (String s : suffix) {
                String name = b + s;
                for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
                    if (ad.getLocalName().equalsIgnoreCase(name)) {
                        continue O;
                    }
                }
                return name;
            }
        }

        // practically should never get here, but just fall back and fail later
        return "fid";
    }

    /** Generates a 'DROP TABLE' sql statement. */
    protected String dropTableSQL(SimpleFeatureType featureType, Connection cx) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("DROP TABLE ");

        encodeTableName(featureType.getTypeName(), sql, null);

        return sql.toString();
    }

    /**
     * Ensures that that the specified transaction has access to features specified by a filter.
     *
     * <p>If any features matching the filter are locked, and the transaction does not have
     * authorization with respect to the lock, an exception is thrown.
     *
     * @param featureType The feature type / table.
     * @param filter The filters.
     * @param tx The transaction.
     * @param cx The database connection.
     */
    protected void ensureAuthorization(
            SimpleFeatureType featureType, Filter filter, Transaction tx, Connection cx)
            throws IOException, SQLException {

        InProcessLockingManager lm = (InProcessLockingManager) getLockingManager();
        // verify if we have any lock to check
        Map locks = lm.locks(featureType.getTypeName());
        if (!locks.isEmpty()) {
            // limiting query to only extract locked features
            if (locks.size() <= MAX_IDS_IN_FILTER) {
                Set<FeatureId> ids = getLockedIds(locks);
                Id lockFilter = getFilterFactory().id(ids);
                // intersect given filter with ids filter
                filter = getFilterFactory().and(filter, lockFilter);
            }
            Query query = new Query(featureType.getTypeName(), filter, Query.NO_NAMES);

            Statement st = null;
            try {
                ResultSet rs = null;
                if (getSQLDialect() instanceof PreparedStatementSQLDialect) {
                    st = selectSQLPS(featureType, query, cx);

                    @SuppressWarnings("PMD.CloseResource") // actually being closed later
                    PreparedStatement ps = (PreparedStatement) st;
                    ((PreparedStatementSQLDialect) getSQLDialect()).onSelect(ps, cx, featureType);
                    rs = ps.executeQuery();
                } else {
                    String sql = selectSQL(featureType, query);

                    st = cx.createStatement();
                    st.setFetchSize(fetchSize);
                    ((BasicSQLDialect) getSQLDialect()).onSelect(st, cx, featureType);

                    LOGGER.fine(sql);
                    rs = st.executeQuery(sql);
                }

                try {
                    PrimaryKey key = getPrimaryKey(featureType);

                    while (rs.next()) {
                        String fid = featureType.getTypeName() + "." + encodeFID(key, rs);
                        lm.assertAccess(featureType.getTypeName(), fid, tx);
                    }
                } finally {
                    closeSafe(rs);
                }
            } finally {
                closeSafe(st);
            }
        }
    }

    /** Extracts a set of FeatureId objects from the locks Map. */
    private Set<FeatureId> getLockedIds(Map locks) {
        Set<FeatureId> ids = new HashSet<>();
        for (Object lock : locks.keySet()) {
            ids.add(getFilterFactory().featureId(lock.toString()));
        }
        return ids;
    }

    /** Helper method for creating geometry association table if it does not exist. */
    protected void ensureAssociationTablesExist(Connection cx) throws IOException, SQLException {
        // look for feature relationship table
        DatabaseMetaData metadata = cx.getMetaData();
        ResultSet tables =
                metadata.getTables(
                        null,
                        escapeNamePattern(metadata, databaseSchema),
                        escapeNamePattern(metadata, FEATURE_RELATIONSHIP_TABLE),
                        null);

        try {
            if (!tables.next()) {
                // does not exist, create it
                String sql = createRelationshipTableSQL(cx);
                LOGGER.log(Level.FINE, "Creating relationship table: {0}", sql);

                Statement st = cx.createStatement();

                try {
                    st.execute(sql);
                } finally {
                    closeSafe(st);
                }
            }
        } finally {
            closeSafe(tables);
        }

        // look for feature association table
        tables =
                metadata.getTables(
                        null,
                        escapeNamePattern(metadata, databaseSchema),
                        escapeNamePattern(metadata, FEATURE_ASSOCIATION_TABLE),
                        null);

        try {
            if (!tables.next()) {
                // does not exist, create it
                String sql = createAssociationTableSQL(cx);
                LOGGER.log(Level.FINE, "Creating association table: {0}", sql);

                Statement st = cx.createStatement();

                try {
                    st.execute(sql);
                } finally {
                    closeSafe(st);
                }
            }
        } finally {
            closeSafe(tables);
        }

        // look up for geometry table
        tables =
                metadata.getTables(
                        null,
                        escapeNamePattern(metadata, databaseSchema),
                        escapeNamePattern(metadata, GEOMETRY_TABLE),
                        null);

        try {
            if (!tables.next()) {
                // does not exist, create it
                String sql = createGeometryTableSQL(cx);
                LOGGER.log(Level.FINE, "Creating geometry table: {0}", sql);

                Statement st = cx.createStatement();

                try {
                    st.execute(sql);
                } finally {
                    closeSafe(st);
                }
            }
        } finally {
            closeSafe(tables);
        }

        // look up for multi geometry table
        tables =
                metadata.getTables(
                        null,
                        escapeNamePattern(metadata, databaseSchema),
                        escapeNamePattern(metadata, MULTI_GEOMETRY_TABLE),
                        null);

        try {
            if (!tables.next()) {
                // does not exist, create it
                String sql = createMultiGeometryTableSQL(cx);
                LOGGER.log(Level.FINE, "Creating multi-geometry table: {0}", sql);

                Statement st = cx.createStatement();

                try {
                    st.execute(sql);
                } finally {
                    closeSafe(st);
                }
            }
        } finally {
            closeSafe(tables);
        }

        // look up for metadata for geometry association table
        tables =
                metadata.getTables(
                        null,
                        escapeNamePattern(metadata, databaseSchema),
                        escapeNamePattern(metadata, GEOMETRY_ASSOCIATION_TABLE),
                        null);

        try {
            if (!tables.next()) {
                // does not exist, create it
                String sql = createGeometryAssociationTableSQL(cx);
                LOGGER.log(Level.FINE, "Creating geometry association table: {0}", sql);

                Statement st = cx.createStatement();

                try {
                    st.execute(sql);
                } finally {
                    closeSafe(st);
                }
            }
        } finally {
            closeSafe(tables);
        }
    }

    /**
     * Creates the sql for the relationship table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     */
    protected String createRelationshipTableSQL(Connection cx) throws SQLException {
        String[] sqlTypeNames = getSQLTypeNames(descriptors(String.class, String.class), cx);
        String[] columnNames = {"table", "col"};

        return createTableSQL(
                FEATURE_RELATIONSHIP_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the association table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     */
    protected String createAssociationTableSQL(Connection cx) throws SQLException {
        String[] sqlTypeNames =
                getSQLTypeNames(
                        descriptors(String.class, String.class, String.class, String.class), cx);
        String[] columnNames = {"fid", "rtable", "rcol", "rfid"};

        return createTableSQL(
                FEATURE_ASSOCIATION_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the geometry table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     */
    protected String createGeometryTableSQL(Connection cx) throws SQLException {
        String[] sqlTypeNames =
                getSQLTypeNames(
                        descriptors(
                                String.class,
                                String.class,
                                String.class,
                                String.class,
                                Geometry.class),
                        cx);
        String[] columnNames = {"id", "name", "description", "type", "geometry"};

        return createTableSQL(GEOMETRY_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the multi_geometry table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     */
    protected String createMultiGeometryTableSQL(Connection cx) throws SQLException {
        String[] sqlTypeNames =
                getSQLTypeNames(descriptors(String.class, String.class, Boolean.class), cx);
        String[] columnNames = {"id", "mgid", "ref"};

        return createTableSQL(MULTI_GEOMETRY_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    private List<AttributeDescriptor> descriptors(Class<?>... classes) {
        AttributeTypeBuilder tb = new AttributeTypeBuilder();
        List<AttributeDescriptor> result = new ArrayList<>();
        for (int i = 0; i < classes.length; i++) {
            Class<?> aClass = classes[i];
            AttributeDescriptor ad = tb.name("a" + i).binding(aClass).buildDescriptor("a" + i);
            result.add(ad);
        }

        return result;
    }

    /**
     * Creates the sql for the relationship table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param table The table of the association
     * @param column The column of the association
     */
    protected String selectRelationshipSQL(String table, String column) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "table", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "col", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_RELATIONSHIP_TABLE, sql, null);

        if (table != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "table", sql);
            sql.append(" = ");
            dialect.encodeValue(table, String.class, sql);
        }

        if (column != null) {
            if (table == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName(null, "col", sql);
            sql.append(" = ");
            dialect.encodeValue(column, String.class, sql);
        }

        return sql.toString();
    }

    /**
     * Creates the prepared statement for a query against the relationship table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param table The table of the association
     * @param column The column of the association
     */
    protected PreparedStatement selectRelationshipSQLPS(String table, String column, Connection cx)
            throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "table", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "col", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_RELATIONSHIP_TABLE, sql, null);

        if (table != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "table", sql);
            sql.append(" = ? ");
        }

        if (column != null) {
            if (table == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName(null, "col", sql);
            sql.append(" = ? ");
        }

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if (table != null) {
            ps.setString(1, table);
        }
        if (column != null) {
            ps.setString(table != null ? 2 : 1, column);
        }
        return ps;
    }

    /**
     * Creates the sql for the association table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param fid The feature id of the association
     */
    protected String selectAssociationSQL(String fid) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "fid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "rtable", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "rcol", sql);
        sql.append(", ");
        dialect.encodeColumnName(null, "rfid", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_ASSOCIATION_TABLE, sql, null);

        if (fid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "fid", sql);
            sql.append(" = ");
            dialect.encodeValue(fid, String.class, sql);
        }

        return sql.toString();
    }

    /**
     * Creates the prepared statement for the association table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param fid The feature id of the association
     */
    protected PreparedStatement selectAssociationSQLPS(String fid, Connection cx)
            throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "fid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "rtable", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "rcol", sql);
        sql.append(", ");
        dialect.encodeColumnName(null, "rfid", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_ASSOCIATION_TABLE, sql, null);

        if (fid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "fid", sql);
            sql.append(" = ?");
        }

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if (fid != null) {
            ps.setString(1, fid);
        }

        return ps;
    }

    /**
     * Creates the sql for a select from the geometry table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param gid The geometry id to select for, may be <code>null</code>
     */
    protected String selectGeometrySQL(String gid) throws SQLException {

        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "id", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "name", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "description", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "type", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "geometry", sql);
        sql.append(" FROM ");
        encodeTableName(GEOMETRY_TABLE, sql, null);

        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "id", sql);
            sql.append(" = ");
            dialect.encodeValue(gid, String.class, sql);
        }

        return sql.toString();
    }

    /**
     * Creates the prepared for a select from the geometry table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param gid The geometry id to select for, may be <code>null</code>
     */
    protected PreparedStatement selectGeometrySQLPS(String gid, Connection cx) throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "id", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "name", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "description", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "type", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "geometry", sql);
        sql.append(" FROM ");
        encodeTableName(GEOMETRY_TABLE, sql, null);

        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "id", sql);
            sql.append(" = ?");
        }

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if (gid != null) {
            ps.setString(1, gid);
        }

        return ps;
    }

    /**
     * Creates the sql for a select from the multi geometry table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param gid The geometry id to select for, may be <code>null</code>.
     */
    protected String selectMultiGeometrySQL(String gid) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "id", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "mgid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "ref", sql);

        sql.append(" FROM ");
        encodeTableName(MULTI_GEOMETRY_TABLE, sql, null);

        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "id", sql);
            sql.append(" = ");
            dialect.encodeValue(gid, String.class, sql);
        }

        return sql.toString();
    }

    /**
     * Creates the prepared statement for a select from the multi geometry table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     *
     * @param gid The geometry id to select for, may be <code>null</code>.
     */
    protected PreparedStatement selectMultiGeometrySQLPS(String gid, Connection cx)
            throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "id", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "mgid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "ref", sql);

        sql.append(" FROM ");
        encodeTableName(MULTI_GEOMETRY_TABLE, sql, null);

        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName(null, "id", sql);
            sql.append(" = ?");
        }

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if (gid != null) {
            ps.setString(1, gid);
        }

        return ps;
    }

    /**
     * Creates the sql for the geometry association table.
     *
     * <p>This method is only called when {@link JDBCDataStore#isAssociations()} is true.
     */
    protected String createGeometryAssociationTableSQL(Connection cx) throws SQLException {
        String[] sqlTypeNames =
                getSQLTypeNames(
                        descriptors(String.class, String.class, String.class, Boolean.class), cx);
        String[] columnNames = {"fid", "gname", "gid", "ref"};

        return createTableSQL(
                GEOMETRY_ASSOCIATION_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for a select from the geometry association table.
     *
     * <p>
     *
     * @param fid The fid to select for, may be <code>null</code>
     * @param gid The geometry id to select for, may be <code>null</code>
     * @param gname The geometry name to select for, may be <code>null</code>
     */
    protected String selectGeometryAssociationSQL(String fid, String gid, String gname)
            throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "fid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "gid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "gname", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "ref", sql);

        sql.append(" FROM ");
        encodeTableName(GEOMETRY_ASSOCIATION_TABLE, sql, null);

        if (fid != null) {
            sql.append(" WHERE ");
            dialect.encodeColumnName(null, "fid", sql);
            sql.append(" = ");
            dialect.encodeValue(fid, String.class, sql);
        }

        if (gid != null) {
            if (fid == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName(null, "gid", sql);
            sql.append(" = ");
            dialect.encodeValue(gid, String.class, sql);
        }

        if (gname != null) {
            if ((fid == null) && (gid == null)) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName(null, "gname", sql);
            sql.append(" = ");
            dialect.encodeValue(gname, String.class, sql);
        }

        return sql.toString();
    }
    /**
     * Creates the prepared statement for a select from the geometry association table.
     *
     * <p>
     *
     * @param fid The fid to select for, may be <code>null</code>
     * @param gid The geometry id to select for, may be <code>null</code>
     * @param gname The geometry name to select for, may be <code>null</code>
     */
    protected PreparedStatement selectGeometryAssociationSQLPS(
            String fid, String gid, String gname, Connection cx) throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName(null, "fid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "gid", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "gname", sql);
        sql.append(",");
        dialect.encodeColumnName(null, "ref", sql);

        sql.append(" FROM ");
        encodeTableName(GEOMETRY_ASSOCIATION_TABLE, sql, null);

        if (fid != null) {
            sql.append(" WHERE ");
            dialect.encodeColumnName(null, "fid", sql);
            sql.append(" = ? ");
        }

        if (gid != null) {
            if (fid == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName(null, "gid", sql);
            sql.append(" = ? ");
        }

        if (gname != null) {
            if ((fid == null) && (gid == null)) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName(null, "gname", sql);
            sql.append(" = ?");
        }

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if (fid != null) {
            ps.setString(1, fid);
        }

        if (gid != null) {
            ps.setString(fid != null ? 2 : 1, gid);
        }

        if (gname != null) {
            ps.setString(fid != null ? (gid != null ? 3 : 2) : (gid != null ? 2 : 1), gname);
        }

        return ps;
    }

    /** Helper method for building a 'CREATE TABLE' sql statement. */
    private String createTableSQL(
            String tableName,
            String[] columnNames,
            String[] sqlTypeNames,
            boolean[] nillable,
            String pkeyColumn,
            SimpleFeatureType featureType)
            throws SQLException {
        // build the create table sql
        StringBuffer sql = new StringBuffer();
        dialect.encodeCreateTable(sql);

        encodeTableName(tableName, sql, null);
        sql.append(" ( ");

        // primary key column
        if (pkeyColumn != null) {
            dialect.encodePrimaryKey(pkeyColumn, sql);
            sql.append(", ");
        }

        // normal attributes
        for (int i = 0; i < columnNames.length; i++) {
            // the column name
            dialect.encodeColumnName(null, columnNames[i], sql);
            sql.append(" ");

            // some sql dialects require varchars to have an
            // associated size with them
            int length = -1;
            if (sqlTypeNames[i].toUpperCase().startsWith("VARCHAR")) {
                if (featureType != null) {
                    AttributeDescriptor att = featureType.getDescriptor(columnNames[i]);
                    length = findVarcharColumnLength(att);
                }
            }

            // only apply a length if one exists (i.e. to applicable varchars)
            if (length == -1) {
                dialect.encodeColumnType(sqlTypeNames[i], sql);
            } else {
                dialect.encodeColumnType(sqlTypeNames[i] + "(" + length + ")", sql);
            }

            // nullable
            if (nillable != null && !nillable[i]) {
                sql.append(" NOT NULL ");
            }

            // delegate to dialect to encode column postamble
            if (featureType != null) {
                AttributeDescriptor att = featureType.getDescriptor(columnNames[i]);
                dialect.encodePostColumnCreateTable(att, sql);
            }

            // sql.append(sqlTypeNames[i]);
            if (i < (sqlTypeNames.length - 1)) {
                sql.append(", ");
            }
        }

        sql.append(" ) ");

        // encode anything post create table
        dialect.encodePostCreateTable(tableName, sql);

        return sql.toString();
    }

    /**
     * Searches the attribute descriptor restrictions in an attempt to determine the length of the
     * specified varchar column.
     */
    private Integer findVarcharColumnLength(AttributeDescriptor att) {
        for (Filter r : att.getType().getRestrictions()) {
            if (r instanceof PropertyIsLessThanOrEqualTo) {
                PropertyIsLessThanOrEqualTo c = (PropertyIsLessThanOrEqualTo) r;
                if (c.getExpression1() instanceof Function
                        && ((Function) c.getExpression1())
                                .getName()
                                .toLowerCase()
                                .endsWith("length")) {
                    if (c.getExpression2() instanceof Literal) {
                        Integer length = c.getExpression2().evaluate(null, Integer.class);
                        if (length != null) {
                            return length;
                        }
                    }
                }
            }
        }

        return dialect.getDefaultVarcharSize();
    }

    /**
     * Helper method for determining what the sql type names are for a set of classes.
     *
     * <p>This method uses a combination of dialect mappings and database metadata to determine
     * which sql types map to the specified classes.
     */
    private String[] getSQLTypeNames(List<AttributeDescriptor> descriptors, Connection cx)
            throws SQLException {
        // figure out what the sql types are corresponding to the feature type
        // attributes
        int[] sqlTypes = new int[descriptors.size()];
        String[] sqlTypeNames = new String[sqlTypes.length];

        for (int i = 0; i < descriptors.size(); i++) {
            AttributeDescriptor ad = descriptors.get(i);
            Object nativeTypeName = ad.getUserData().get(JDBC_NATIVE_TYPENAME);
            if (nativeTypeName instanceof String) {
                sqlTypeNames[i] = (String) nativeTypeName;
                continue;
            }

            Class clazz = ad.getType().getBinding();
            Integer sqlType;
            Object nativeType = ad.getUserData().get(JDBC_NATIVE_TYPE);
            if (nativeType instanceof Integer) {
                sqlType = (Integer) nativeType;
            } else {
                sqlType = dialect.getSQLType(ad);
            }

            if (sqlType == null) {
                sqlType = getMapping(clazz);
            }

            if (sqlType == null) {
                LOGGER.warning(
                        "No sql type mapping for: " + ad.getLocalName() + " of type " + clazz);
                sqlType = Types.OTHER;
            }

            sqlTypes[i] = sqlType;

            // if this a geometric type, get the name from the dialect
            // if ( attributeType instanceof GeometryDescriptor ) {
            if (Geometry.class.isAssignableFrom(clazz)) {
                String sqlTypeName = dialect.getGeometryTypeName(sqlType);

                if (sqlTypeName != null) {
                    sqlTypeNames[i] = sqlTypeName;
                }
            }

            // check types previously read from DB
            String sqlTypeDBName = getDBsqlTypesCache().get(sqlType);
            if (sqlTypeDBName != null) {
                sqlTypeNames[i] = sqlTypeDBName;
            }
        }
        // GEOT-6347 if all sql type names have been found in dialect dont
        // go to database
        boolean allTypesFound = !ArrayUtils.contains(sqlTypeNames, null);
        if (!allTypesFound) {
            LOGGER.log(Level.WARNING, "Fetching fields from Database");
            // figure out the type names that correspond to the sql types from
            // the database metadata
            DatabaseMetaData metaData = cx.getMetaData();

            /*
            *      <LI><B>TYPE_NAME</B> String => Type name
            *        <LI><B>DATA_TYPE</B> int => SQL data type from java.sql.Types
            *        <LI><B>PRECISION</B> int => maximum precision
            *        <LI><B>LITERAL_PREFIX</B> String => prefix used to quote a literal
            *      (may be <code>null</code>)
            *        <LI><B>LITERAL_SUFFIX</B> String => suffix used to quote a literal
                (may be <code>null</code>)
            *        <LI><B>CREATE_PARAMS</B> String => parameters used in creating
            *      the type (may be <code>null</code>)
            *        <LI><B>NULLABLE</B> short => can you use NULL for this type.
            *      <UL>
            *      <LI> typeNoNulls - does not allow NULL values
            *      <LI> typeNullable - allows NULL values
            *      <LI> typeNullableUnknown - nullability unknown
            *      </UL>
            *        <LI><B>CASE_SENSITIVE</B> boolean=> is it case sensitive.
            *        <LI><B>SEARCHABLE</B> short => can you use "WHERE" based on this type:
            *      <UL>
            *      <LI> typePredNone - No support
            *      <LI> typePredChar - Only supported with WHERE .. LIKE
            *      <LI> typePredBasic - Supported except for WHERE .. LIKE
            *      <LI> typeSearchable - Supported for all WHERE ..
            *      </UL>
            *        <LI><B>UNSIGNED_ATTRIBUTE</B> boolean => is it unsigned.
            *        <LI><B>FIXED_PREC_SCALE</B> boolean => can it be a money value.
            *        <LI><B>AUTO_INCREMENT</B> boolean => can it be used for an
            *      auto-increment value.
            *        <LI><B>LOCAL_TYPE_NAME</B> String => localized version of type name
            *      (may be <code>null</code>)
            *        <LI><B>MINIMUM_SCALE</B> short => minimum scale supported
            *        <LI><B>MAXIMUM_SCALE</B> short => maximum scale supported
            *        <LI><B>SQL_DATA_TYPE</B> int => unused
            *        <LI><B>SQL_DATETIME_SUB</B> int => unused
            *        <LI><B>NUM_PREC_RADIX</B> int => usually 2 or 10
            */
            ResultSet types = metaData.getTypeInfo();

            try {
                while (types.next()) {
                    int sqlType = types.getInt("DATA_TYPE");
                    String sqlTypeName = types.getString("TYPE_NAME");

                    for (int i = 0; i < sqlTypes.length; i++) {
                        // check if we already have the type name from the dialect
                        if (sqlTypeNames[i] != null) {
                            continue;
                        }

                        if (sqlType == sqlTypes[i]) {
                            sqlTypeNames[i] = sqlTypeName;
                            // GEOT-6347
                            // cache the sqlType which required going to database for sql types
                            getDBsqlTypesCache().putIfAbsent(sqlType, sqlTypeName);
                        }
                    }
                }
            } finally {
                closeSafe(types);
            }
        }

        // apply the overrides specified by the dialect
        Map<Integer, String> overrides = getSqlTypeToSqlTypeNameOverrides();
        for (int i = 0; i < sqlTypes.length; i++) {
            String override = overrides.get(sqlTypes[i]);
            if (override != null) sqlTypeNames[i] = override;
        }

        return sqlTypeNames;
    }

    /**
     * Generates a 'SELECT p1, p2, ... FROM ... WHERE ...' statement.
     *
     * @param featureType the feature type that the query must return (may contain less attributes
     *     than the native one)
     * @param query the query to be run. The type name and property will be ignored, as they are
     *     supposed to have been already embedded into the provided feature type
     */
    protected String selectSQL(SimpleFeatureType featureType, Query query)
            throws IOException, SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        // column names
        selectColumns(featureType, null, query, sql);
        sql.setLength(sql.length() - 1);
        dialect.encodePostSelect(featureType, sql);

        // from
        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, setKeepWhereClausePlaceHolderHint(query));

        // filtering
        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            sql.append(" WHERE ");
            // encode filter
            filter(featureType, filter, sql);
        }

        // sorting
        sort(featureType, query.getSortBy(), null, sql);

        // encode limit/offset, if necessary
        applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());

        // add search hints if the dialect supports them
        applySearchHints(featureType, query, sql);

        return sql.toString();
    }

    private void applySearchHints(SimpleFeatureType featureType, Query query, StringBuffer sql) {
        // we can apply search hints only on real tables
        if (virtualTables.containsKey(featureType.getTypeName())) {
            return;
        }

        dialect.handleSelectHints(sql, featureType, query);
    }

    protected String selectJoinSQL(SimpleFeatureType featureType, JoinInfo join, Query query)
            throws IOException, SQLException {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        // column names
        selectColumns(featureType, join.getPrimaryAlias(), query, sql);

        // joined columns
        for (JoinPart part : join.getParts()) {
            selectColumns(part.getQueryFeatureType(), part.getAlias(), query, sql);
        }

        sql.setLength(sql.length() - 1);
        dialect.encodePostSelect(featureType, sql);

        // from
        sql.append(" FROM ");

        // join clauses
        encodeTableJoin(featureType, join, query, sql);

        // filtering
        encodeWhereJoin(featureType, join, sql);

        // TODO: sorting
        sort(featureType, query.getSortBy(), join.getPrimaryAlias(), sql);

        // finally encode limit/offset, if necessary
        applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());

        return sql.toString();
    }

    void selectColumns(SimpleFeatureType featureType, String prefix, Query query, StringBuffer sql)
            throws IOException {

        // primary key
        PrimaryKey key = null;
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> pkColumnNames = getColumnNames(key);

        // we need to add the primary key columns only if they are not already exposed
        for (PrimaryKeyColumn col : key.getColumns()) {
            dialect.encodeColumnName(prefix, col.getName(), sql);
            if (prefix != null) {
                // if a prefix is specified means we are joining so use a prefix to avoid clashing
                // with primary key columsn with the same name from other tables in the join
                dialect.encodeColumnAlias(prefix + "_" + col.getName(), sql);
            }
            sql.append(",");
        }

        // other columns
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            String columnName = att.getLocalName();
            // skip the eventually exposed pk column values
            if (pkColumnNames.contains(columnName)) continue;

            String alias = null;
            if (att.getUserData().containsKey(JDBC_COLUMN_ALIAS)) {
                alias = (String) att.getUserData().get(JDBC_COLUMN_ALIAS);
            }

            if (att instanceof GeometryDescriptor) {
                // encode as geometry
                encodeGeometryColumn((GeometryDescriptor) att, prefix, sql, query.getHints());

                if (alias == null) {
                    // alias it to be the name of the original geometry
                    alias = columnName;
                }
            } else {
                dialect.encodeColumnName(prefix, columnName, sql);
            }

            if (alias != null) {
                dialect.encodeColumnAlias(alias, sql);
            }

            sql.append(",");
        }
    }

    FilterToSQL filter(SimpleFeatureType featureType, Filter filter, StringBuffer sql)
            throws IOException {
        SimpleFeatureType fullSchema = getSchema(featureType.getTypeName());
        FilterToSQL toSQL = getFilterToSQL(fullSchema);
        return filter(featureType, filter, sql, toSQL);
    }

    FilterToSQL filter(
            SimpleFeatureType featureType, Filter filter, StringBuffer sql, FilterToSQL toSQL)
            throws IOException {

        try {
            // grab the full feature type, as we might be encoding a filter
            // that uses attributes that aren't returned in the results
            toSQL.setInline(true);
            String filterSql = toSQL.encodeToString(filter);
            int whereClauseIndex = sql.indexOf(WHERE_CLAUSE_PLACE_HOLDER);
            if (whereClauseIndex != -1) {
                sql.replace(
                        whereClauseIndex,
                        whereClauseIndex + WHERE_CLAUSE_PLACE_HOLDER_LENGTH,
                        "AND " + filterSql);
                sql.append("1 = 1");
            } else {
                sql.append(filterSql);
            }
            return toSQL;
        } catch (FilterToSQLException e) {
            throw new RuntimeException(e);
        }
    }

    private FilterToSQL getFilterToSQL(SimpleFeatureType fullSchema) {
        return dialect instanceof PreparedStatementSQLDialect
                ? createPreparedFilterToSQL(fullSchema)
                : createFilterToSQL(fullSchema);
    }

    /** Encodes the sort-by portion of an sql query */
    void sort(SimpleFeatureType featureType, SortBy[] sort, String prefix, StringBuffer sql)
            throws IOException {
        if ((sort != null) && (sort.length > 0)) {
            PrimaryKey key = getPrimaryKey(featureType);
            sql.append(" ORDER BY ");

            for (SortBy sortBy : sort) {
                String order;
                if (sortBy.getSortOrder() == SortOrder.DESCENDING) {
                    order = " DESC";
                } else {
                    order = " ASC";
                }

                if (SortBy.NATURAL_ORDER.equals(sortBy) || SortBy.REVERSE_ORDER.equals(sortBy)) {
                    if (key instanceof NullPrimaryKey)
                        throw new IOException(
                                "Cannot do natural order without a primary key, please add it or "
                                        + "specify a manual sort over existing attributes");

                    for (PrimaryKeyColumn col : key.getColumns()) {
                        dialect.encodeColumnName(prefix, col.getName(), sql);
                        sql.append(order);
                        sql.append(",");
                    }
                } else {
                    dialect.encodeColumnName(
                            prefix, getPropertyName(featureType, sortBy.getPropertyName()), sql);
                    sql.append(order);
                    sql.append(",");
                }
            }

            sql.setLength(sql.length() - 1);
        }
    }

    /**
     * Generates a 'SELECT p1, p2, ... FROM ... WHERE ...' prepared statement.
     *
     * @param featureType the feature type that the query must return (may contain less attributes
     *     than the native one)
     * @param query the query to be run. The type name and property will be ignored, as they are
     *     supposed to have been already embedded into the provided feature type
     * @param cx The database connection to be used to create the prepared statement
     */
    protected PreparedStatement selectSQLPS(
            SimpleFeatureType featureType, Query query, Connection cx)
            throws SQLException, IOException {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        // column names
        selectColumns(featureType, null, query, sql);
        sql.setLength(sql.length() - 1);
        dialect.encodePostSelect(featureType, sql);

        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, setKeepWhereClausePlaceHolderHint(query));

        // filtering
        PreparedFilterToSQL toSQL = null;
        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            sql.append(" WHERE ");

            // encode filter
            toSQL = (PreparedFilterToSQL) filter(featureType, filter, sql);
        }

        // sorting
        sort(featureType, query.getSortBy(), null, sql);

        // finally encode limit/offset, if necessary
        applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());

        // add search hints if the dialect supports them
        applySearchHints(featureType, query, sql);

        LOGGER.fine(sql.toString());
        PreparedStatement ps =
                cx.prepareStatement(
                        sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(fetchSize);

        if (toSQL != null) {
            setPreparedFilterValues(ps, toSQL, 0, cx);
        }

        return ps;
    }

    protected PreparedStatement selectJoinSQLPS(
            SimpleFeatureType featureType, JoinInfo join, Query query, Connection cx)
            throws SQLException, IOException {

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        selectColumns(featureType, join.getPrimaryAlias(), query, sql);

        // joined columns
        for (JoinPart part : join.getParts()) {
            selectColumns(part.getQueryFeatureType(), part.getAlias(), query, sql);
        }

        sql.setLength(sql.length() - 1);
        dialect.encodePostSelect(featureType, sql);

        sql.append(" FROM ");

        // join clauses
        encodeTableJoin(featureType, join, query, sql);

        // filtering
        List<FilterToSQL> toSQLs = encodeWhereJoin(featureType, join, sql);

        // sorting
        sort(featureType, query.getSortBy(), join.getPrimaryAlias(), sql);

        // finally encode limit/offset, if necessary
        applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());

        LOGGER.fine(sql.toString());
        PreparedStatement ps =
                cx.prepareStatement(
                        sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(fetchSize);

        setPreparedFilterValues(ps, toSQLs, cx);

        return ps;
    }

    /**
     * Helper method for setting the values of the WHERE class of a prepared statement from a list
     * of PreparedFilterToSQL.
     */
    protected void setPreparedFilterValues(
            PreparedStatement ps, List<FilterToSQL> toSQLs, Connection cx) throws SQLException {
        int offset = 0;
        for (FilterToSQL fts : toSQLs) {
            PreparedFilterToSQL toSQL = (PreparedFilterToSQL) fts;
            setPreparedFilterValues(ps, toSQL, offset, cx);
            offset += toSQL.getLiteralValues().size();
        }
    }

    /** Helper method for setting the values of the WHERE class of a prepared statement. */
    public void setPreparedFilterValues(
            PreparedStatement ps, PreparedFilterToSQL toSQL, int offset, Connection cx)
            throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        for (int i = 0; i < toSQL.getLiteralValues().size(); i++) {
            Object value = toSQL.getLiteralValues().get(i);
            Class binding = toSQL.getLiteralTypes().get(i);
            Integer srid = toSQL.getSRIDs().get(i);
            Integer dimension = toSQL.getDimensions().get(i);
            AttributeDescriptor ad = toSQL.getDescriptors().get(i);
            if (srid == null) {
                srid = -1;
            }
            if (dimension == null) {
                dimension = 2;
            }

            if (binding != null && Geometry.class.isAssignableFrom(binding)) {
                dialect.setGeometryValue(
                        (Geometry) value, dimension, srid, binding, ps, offset + i + 1);
            } else if (ad != null && this.dialect.isArray(ad)) {
                dialect.setArrayValue(value, ad, ps, offset + i + 1, cx);
            } else {
                dialect.setValue(value, binding, ps, offset + i + 1, cx);
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine((i + 1) + " = " + value);
            }
        }
    }

    /**
     * Helper method for executing a property name against a feature type.
     *
     * <p>This method will fall back on {@link PropertyName#getPropertyName()} if it does not
     * evaulate against the feature type.
     */
    protected String getPropertyName(SimpleFeatureType featureType, PropertyName propertyName) {
        AttributeDescriptor att = (AttributeDescriptor) propertyName.evaluate(featureType);

        if (att != null) {
            return att.getLocalName();
        }

        return propertyName.getPropertyName();
    }

    /**
     * Generates a 'SELECT' sql statement which selects bounds.
     *
     * @param featureType The feature type / table.
     * @param query Specifies which features are to be used for the bounds computation (and in
     *     particular uses filter, start index and max features)
     */
    protected String selectBoundsSQL(SimpleFeatureType featureType, Query query)
            throws SQLException {
        StringBuffer sql = new StringBuffer();

        boolean offsetLimit = checkLimitOffset(query.getStartIndex(), query.getMaxFeatures());
        if (offsetLimit) {
            // envelopes are aggregates, just like count, so we must first isolate
            // the rows against which the aggregate will work in a subquery
            sql.append(" SELECT *");
        } else {
            sql.append("SELECT ");
            buildEnvelopeAggregates(featureType, sql);
        }

        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, setKeepWhereClausePlaceHolderHint(query));

        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        // finally encode limit/offset, if necessary
        if (offsetLimit) {
            applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());
            // build the prologue
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT ");
            buildEnvelopeAggregates(featureType, sb);
            sb.append("FROM (");
            // wrap the existing query
            sql.insert(0, sb.toString());
            sql.append(")");
            dialect.encodeTableAlias("GT2_BOUNDS_", sql);
        }

        // add search hints if the dialect supports them
        applySearchHints(featureType, query, sql);

        return sql.toString();
    }

    /**
     * Generates a 'SELECT' prepared statement which selects bounds.
     *
     * @param featureType The feature type / table.
     * @param query Specifies which features are to be used for the bounds computation (and in
     *     particular uses filter, start index and max features)
     * @param cx A database connection.
     */
    protected PreparedStatement selectBoundsSQLPS(
            SimpleFeatureType featureType, Query query, Connection cx) throws SQLException {

        StringBuffer sql = new StringBuffer();

        boolean offsetLimit = checkLimitOffset(query.getStartIndex(), query.getMaxFeatures());
        if (offsetLimit) {
            // envelopes are aggregates, just like count, so we must first isolate
            // the rows against which the aggregate will work in a subquery
            sql.append(" SELECT *");
        } else {
            sql.append("SELECT ");
            buildEnvelopeAggregates(featureType, sql);
        }

        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, setKeepWhereClausePlaceHolderHint(query));

        // encode the filter
        PreparedFilterToSQL toSQL = null;
        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                toSQL = createPreparedFilterToSQL(featureType);
                int whereClauseIndex = sql.indexOf(WHERE_CLAUSE_PLACE_HOLDER);
                if (whereClauseIndex != -1) {
                    toSQL.setInline(true);
                    sql.replace(
                            whereClauseIndex,
                            whereClauseIndex + WHERE_CLAUSE_PLACE_HOLDER_LENGTH,
                            "AND " + toSQL.encodeToString(filter));
                    toSQL.setInline(false);
                } else {
                    sql.append(" ").append(toSQL.encodeToString(filter));
                }
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        // finally encode limit/offset, if necessary
        if (offsetLimit) {
            applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());
            // build the prologue
            StringBuffer sb = new StringBuffer();
            sb.append("SELECT ");
            buildEnvelopeAggregates(featureType, sb);
            sb.append("FROM (");
            // wrap the existing query
            sql.insert(0, sb.toString());
            sql.append(")");
            dialect.encodeTableAlias("GT2_BOUNDS_", sql);
        }

        // add search hints if the dialect supports them
        applySearchHints(featureType, query, sql);

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());

        if (toSQL != null) {
            setPreparedFilterValues(ps, toSQL, 0, cx);
        }

        return ps;
    }

    /**
     * Builds a list of the aggregate function calls necesary to compute each geometry column bounds
     */
    void buildEnvelopeAggregates(SimpleFeatureType featureType, StringBuffer sql) {
        // walk through all geometry attributes and build the query
        for (AttributeDescriptor attribute : featureType.getAttributeDescriptors()) {
            if (attribute instanceof GeometryDescriptor) {
                String geometryColumn = attribute.getLocalName();
                dialect.encodeGeometryEnvelope(featureType.getTypeName(), geometryColumn, sql);
                sql.append(",");
            }
        }
        sql.setLength(sql.length() - 1);
    }

    protected String selectAggregateSQL(
            String function,
            Expression att,
            List<Expression> groupByExpressions,
            SimpleFeatureType featureType,
            Query query,
            LimitingVisitor visitor)
            throws SQLException, IOException {
        StringBuffer sql = new StringBuffer();
        doSelectAggregateSQL(function, att, groupByExpressions, featureType, query, visitor, sql);
        return sql.toString();
    }

    protected PreparedStatement selectAggregateSQLPS(
            String function,
            Expression att,
            List<Expression> groupByExpressions,
            SimpleFeatureType featureType,
            Query query,
            LimitingVisitor visitor,
            Connection cx)
            throws SQLException, IOException {

        StringBuffer sql = new StringBuffer();
        List<FilterToSQL> toSQL =
                doSelectAggregateSQL(
                        function, att, groupByExpressions, featureType, query, visitor, sql);

        LOGGER.fine(sql.toString());

        PreparedStatement ps =
                cx.prepareStatement(
                        sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(fetchSize);

        setPreparedFilterValues(ps, toSQL, cx);

        return ps;
    }

    /**
     * Helper method to factor out some commonalities between selectAggregateSQL, and
     * selectAggregateSQLPS
     */
    List<FilterToSQL> doSelectAggregateSQL(
            String function,
            Expression expr,
            List<Expression> groupByExpressions,
            SimpleFeatureType featureType,
            Query query,
            LimitingVisitor visitor,
            StringBuffer sql)
            throws SQLException, IOException {
        JoinInfo join =
                !query.getJoins().isEmpty() ? JoinInfo.create(query, featureType, this) : null;

        List<FilterToSQL> toSQL = new ArrayList<>();
        boolean queryLimitOffset = checkLimitOffset(query.getStartIndex(), query.getMaxFeatures());
        boolean visitorLimitOffset =
                visitor == null ? false : visitor.hasLimits() && dialect.isLimitOffsetSupported();
        // grouping over expressions is complex, as we need
        boolean groupByComplexExpressions = hasComplexExpressions(groupByExpressions);
        if (queryLimitOffset && !visitorLimitOffset && !groupByComplexExpressions) {
            if (join != null) {
                // don't select * to avoid ambigous result set
                sql.append("SELECT ");
                dialect.encodeColumnName(null, join.getPrimaryAlias(), sql);
                sql.append(".* FROM ");
            } else {
                sql.append("SELECT * FROM ");
            }
        } else {
            sql.append("SELECT ");
            FilterToSQL filterToSQL = getFilterToSQL(featureType);
            if (groupByExpressions != null && !groupByExpressions.isEmpty()) {
                try {
                    // we encode all the group by attributes as columns names
                    int i = 1;
                    for (Expression expression : groupByExpressions) {
                        GeometryDescriptor gd = getGeometryDescriptor(featureType, expression);
                        if (gd != null) {
                            dialect.encodeGeometryColumn(
                                    gd, null, getDescriptorSRID(gd), null, sql);
                        } else {
                            sql.append(filterToSQL.encodeToString(expression));
                        }
                        // if we are using complex group by, we have to given them an alias
                        if (groupByComplexExpressions) {
                            sql.append(" as ").append(getAggregateExpressionAlias(i++));
                        }
                        sql.append(", ");
                    }
                } catch (FilterToSQLException e) {
                    throw new RuntimeException("Failed to encode group by expressions", e);
                }
            }

            if (groupByComplexExpressions) {
                // if encoding a sub-query, the source of the aggregation function must
                // also be given an alias (we could use * too, but there is a risk of conflicts)
                if (expr != null) {
                    try {
                        sql.append(filterToSQL.encodeToString(expr));
                        sql.append(" as gt_agg_src");
                    } catch (FilterToSQLException e) {
                        throw new RuntimeException("Failed to encode group by expressions", e);
                    }
                } else {
                    // remove the last comma and space
                    sql.setLength(sql.length() - 2);
                }
            } else {
                encodeFunction(function, expr, sql, filterToSQL);
            }
            toSQL.add(filterToSQL);
            sql.append(" FROM ");
        }

        if (join != null) {
            encodeTableJoin(featureType, join, query, sql);
        } else {
            encodeTableName(
                    featureType.getTypeName(), sql, setKeepWhereClausePlaceHolderHint(query));
        }

        if (join != null) {
            toSQL.addAll(encodeWhereJoin(featureType, join, sql));
        } else {
            Filter filter = query.getFilter();
            if (filter != null && !Filter.INCLUDE.equals(filter)) {
                sql.append(" WHERE ");
                toSQL.add(filter(featureType, filter, sql));
            }
        }
        if (dialect.isAggregatedSortSupported(function)) {
            sort(featureType, query.getSortBy(), null, sql);
        }

        // apply limits
        if (visitorLimitOffset) {
            applyLimitOffset(sql, visitor.getStartIndex(), visitor.getMaxFeatures());
        } else if (queryLimitOffset) {
            applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());
        }
        boolean isUniqueCount = visitor instanceof UniqueCountVisitor;
        // if the limits were in query or there is a group by with complex expressions
        // or there is a UniqueCountVisitor
        // we need to roll what was built so far in a sub-query
        if (queryLimitOffset || groupByComplexExpressions || isUniqueCount) {
            StringBuffer sql2 = new StringBuffer("SELECT ");
            try {
                if (groupByExpressions != null && !groupByExpressions.isEmpty()) {
                    FilterToSQL filterToSQL = getFilterToSQL(featureType);
                    int i = 1;
                    for (Expression expression : groupByExpressions) {
                        if (groupByComplexExpressions) {
                            sql2.append(getAggregateExpressionAlias(i++));
                        } else {
                            sql2.append(filterToSQL.encodeToString(expression));
                        }
                        sql2.append(",");
                    }
                    toSQL.add(filterToSQL);
                }
            } catch (FilterToSQLException e) {
                throw new RuntimeException("Failed to encode group by expressions", e);
            }
            FilterToSQL filterToSQL = getFilterToSQL(featureType);
            boolean countQuery =
                    isUniqueCount || (groupByComplexExpressions && "count".equals(function));
            if (countQuery) sql2.append("count(*)");
            else if (groupByComplexExpressions)
                sql2.append(function).append("(").append("gt_agg_src").append(")");
            else encodeFunction(function, expr, sql2, filterToSQL);
            toSQL.add(filterToSQL);
            sql2.append(" AS gt_result_");
            sql2.append(" FROM (");
            sql.insert(0, sql2);
            sql.append(") gt_limited_");
        }

        FilterToSQL filterToSQL = getFilterToSQL(featureType);
        encodeGroupByStatement(groupByExpressions, sql, filterToSQL, groupByComplexExpressions);
        toSQL.add(filterToSQL);

        // add search hints if the dialect supports them
        applySearchHints(featureType, query, sql);

        return toSQL;
    }

    /**
     * Returns a GeometryDescriptor backing the specified expression, if it's a PropertyName
     * matching a geometry column in the table. Null otherwise.
     */
    private GeometryDescriptor getGeometryDescriptor(
            SimpleFeatureType featureType, Expression expression) {
        if (!(expression instanceof PropertyName)) return null;
        PropertyName pn = (PropertyName) expression;
        AttributeDescriptor ad = featureType.getDescriptor(pn.getPropertyName());
        if (ad instanceof GeometryDescriptor) return (GeometryDescriptor) ad;
        return null;
    }

    private String getAggregateExpressionAlias(int idx) {
        return "gt_agg_" + idx;
    }

    /** Returns true if the expressions have anything but property names */
    private boolean hasComplexExpressions(List<Expression> expressions) {
        if (expressions == null || expressions.isEmpty()) {
            return false;
        }

        return expressions.stream().anyMatch(x -> !(x instanceof PropertyName));
    }

    /**
     * Helper method that adds a group by statement to the SQL query. If the list of group by
     * attributes is empty or NULL no group by statement is add.
     *
     * @param groupByExpressions the group by attributes to be encoded
     * @param sql the sql query buffer
     */
    protected void encodeGroupByStatement(
            List<Expression> groupByExpressions,
            StringBuffer sql,
            FilterToSQL filterToSQL,
            boolean aggregateOnExpression) {
        if (groupByExpressions == null || groupByExpressions.isEmpty()) {
            // there is not group by attributes to encode so nothing to do
            return;
        }
        sql.append(" GROUP BY ");
        int i = 1;
        for (Expression groupByExpression : groupByExpressions) {
            if (aggregateOnExpression) {
                sql.append("gt_agg_" + i++);
            } else {
                try {
                    sql.append(filterToSQL.encodeToString(groupByExpression));
                } catch (FilterToSQLException e) {
                    throw new RuntimeException(e);
                }
            }
            sql.append(", ");
        }
        sql.setLength(sql.length() - 2);
    }

    protected void encodeFunction(
            String function, Expression expression, StringBuffer sql, FilterToSQL filterToSQL) {
        sql.append(function).append("(");
        if (expression == null) {
            sql.append("*");
        } else {
            try {
                sql.append(filterToSQL.encodeToString(expression));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }
        sql.append(")");
    }

    /** Generates a 'DELETE FROM' sql statement. */
    protected String deleteSQL(SimpleFeatureType featureType, Filter filter) throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM ");
        encodeTableName(featureType.getTypeName(), sql, null);

        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        return sql.toString();
    }

    /** Generates a 'DELETE FROM' prepared statement. */
    protected PreparedStatement deleteSQLPS(
            SimpleFeatureType featureType, Filter filter, Connection cx) throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM ");
        encodeTableName(featureType.getTypeName(), sql, null);

        PreparedFilterToSQL toSQL = null;
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                toSQL = createPreparedFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        LOGGER.fine(sql.toString());
        PreparedStatement ps = cx.prepareStatement(sql.toString());

        if (toSQL != null) {
            setPreparedFilterValues(ps, toSQL, 0, cx);
        }

        return ps;
    }

    /** Generates a 'INSERT INFO' sql statement. */
    protected String insertSQL(
            SimpleFeatureType featureType,
            SimpleFeature feature,
            KeysFetcher keysFetcher,
            Connection cx)
            throws SQLException, IOException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        encodeTableName(featureType.getTypeName(), sql, null);

        // column names
        sql.append(" ( ");

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            String colName = featureType.getDescriptor(i).getLocalName();
            // skip the pk columns in case we have exposed them
            if (keysFetcher.isKey(colName)) {
                continue;
            }
            dialect.encodeColumnName(null, colName, sql);
            sql.append(",");
        }

        // primary key values
        keysFetcher.addKeyColumns(sql);
        sql.setLength(sql.length() - 1);

        // values
        sql.append(" ) VALUES ( ");

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            AttributeDescriptor att = featureType.getDescriptor(i);
            String colName = att.getLocalName();
            // skip the pk columns in case we have exposed them, we grab the
            // value from the pk itself
            if (keysFetcher.isKey(colName)) {
                continue;
            }

            Class binding = att.getType().getBinding();
            EnumMapper mapper = (EnumMapper) att.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);

            Object value = feature.getAttribute(colName);

            if (value == null) {
                if (!att.isNillable()) {
                    throw new IOException(
                            "Cannot set a NULL value on the not null column " + colName);
                }

                sql.append("null");
            } else {
                if (Geometry.class.isAssignableFrom(binding)) {
                    try {
                        Geometry g = (Geometry) value;
                        int srid = getGeometrySRID(g, att);
                        int dimension = getGeometryDimension(g, att);
                        dialect.encodeGeometryValue(g, dimension, srid, sql);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    if (mapper != null) {
                        value = mapper.fromString((String) value);
                        binding = Integer.class;
                    }
                    dialect.encodeValue(value, binding, sql);
                }
            }

            sql.append(",");
        }
        // handle the primary key
        keysFetcher.setKeyValues(this, cx, featureType, feature, sql);
        sql.setLength(sql.length() - 1); // remove last comma

        sql.append(")");

        return sql.toString();
    }

    /**
     * Returns the set of the primary key column names. The set is guaranteed to have the same
     * iteration order as the primary key.
     */
    protected static LinkedHashSet<String> getColumnNames(PrimaryKey key) {
        LinkedHashSet<String> pkColumnNames = new LinkedHashSet<>();
        for (PrimaryKeyColumn pkcol : key.getColumns()) {
            pkColumnNames.add(pkcol.getName());
        }
        return pkColumnNames;
    }

    /**
     * Looks up the geometry srs by trying a number of heuristics. Returns -1 if all attempts at
     * guessing the srid failed.
     */
    protected int getGeometrySRID(Geometry g, AttributeDescriptor descriptor) throws IOException {
        int srid = getDescriptorSRID(descriptor);

        if (g == null) {
            return srid;
        }

        // check for srid in the jts geometry then
        if (srid <= 0 && g.getSRID() > 0) {
            srid = g.getSRID();
        }

        // check if the geometry has anything
        if (srid <= 0 && g.getUserData() instanceof CoordinateReferenceSystem) {
            // check for crs object
            CoordinateReferenceSystem crs = (CoordinateReferenceSystem) g.getUserData();

            try {
                Integer candidate = CRS.lookupEpsgCode(crs, false);
                if (candidate != null) srid = candidate;
            } catch (Exception e) {
                // ok, we tried...
            }
        }

        return srid;
    }

    /**
     * Looks up the geometry dimension by trying a number of heuristics. Returns 2 if all attempts
     * at guessing the dimension failed.
     */
    protected int getGeometryDimension(Geometry g, AttributeDescriptor descriptor)
            throws IOException {
        int dimension = getDescriptorDimension(descriptor);

        if (g == null || dimension > 0) {
            return dimension;
        }

        // check for dimension in the geometry coordinate sequences
        CoordinateSequenceDimensionExtractor dex = new CoordinateSequenceDimensionExtractor();
        g.apply(dex);
        return dex.getDimension();
    }

    /**
     * Extracts the eventual native SRID user property from the descriptor, returns -1 if not found
     */
    protected int getDescriptorSRID(AttributeDescriptor descriptor) {
        int srid = -1;

        // check if we have stored the native srid in the descriptor (we should)
        if (descriptor.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null)
            srid = (Integer) descriptor.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);

        return srid;
    }

    /**
     * Extracts the eventual native dimension user property from the descriptor, returns -1 if not
     * found
     */
    protected int getDescriptorDimension(AttributeDescriptor descriptor) {
        int dimension = -1;

        // check if we have stored the native srid in the descriptor (we should)
        if (descriptor.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null) {
            dimension = (Integer) descriptor.getUserData().get(Hints.COORDINATE_DIMENSION);
        }

        return dimension;
    }

    /** Generates an 'UPDATE' sql statement. */
    protected String updateSQL(
            SimpleFeatureType featureType,
            AttributeDescriptor[] attributes,
            Object[] values,
            Filter filter,
            Set<String> pkColumnNames)
            throws IOException, SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        encodeTableName(featureType.getTypeName(), sql, null);

        sql.append(" SET ");

        for (int i = 0; i < attributes.length; i++) {
            // skip exposed pk columns, they are read only
            AttributeDescriptor att = attributes[i];
            String attName = att.getLocalName();
            if (pkColumnNames.contains(attName)) {
                continue;
            }

            // build "colName = value"
            dialect.encodeColumnName(null, attName, sql);
            sql.append(" = ");

            Class<?> binding = att.getType().getBinding();
            Object value = values[i];
            if (Geometry.class.isAssignableFrom(binding)) {
                try {
                    Geometry g = (Geometry) value;
                    int srid = getGeometrySRID(g, att);
                    int dimension = getGeometryDimension(g, att);
                    dialect.encodeGeometryValue(g, dimension, srid, sql);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                EnumMapper mapper = (EnumMapper) att.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);
                if (mapper != null) {
                    value = mapper.fromString(Converters.convert(value, String.class));
                    binding = Integer.class;
                }
                dialect.encodeValue(value, binding, sql);
            }

            sql.append(",");
        }

        sql.setLength(sql.length() - 1);
        sql.append(" ");

        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        return sql.toString();
    }

    /** Generates an 'UPDATE' prepared statement. */
    protected PreparedStatement updateSQLPS(
            SimpleFeatureType featureType,
            AttributeDescriptor[] attributes,
            Object[] values,
            Filter filter,
            Set<String> pkColumnNames,
            Connection cx)
            throws IOException, SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        encodeTableName(featureType.getTypeName(), sql, null);

        sql.append(" SET ");

        for (int i = 0; i < attributes.length; i++) {
            // skip exposed primary key values, they are read only
            AttributeDescriptor att = attributes[i];
            String attName = att.getLocalName();
            if (pkColumnNames.contains(attName)) {
                continue;
            }

            dialect.encodeColumnName(null, attName, sql);
            sql.append(" = ");

            // geometries might need special treatment, delegate to the dialect
            if (attributes[i] instanceof GeometryDescriptor) {
                Geometry geometry = (Geometry) values[i];
                final Class<?> binding = att.getType().getBinding();
                dialect.prepareGeometryValue(
                        geometry,
                        getDescriptorDimension(att),
                        getDescriptorSRID(att),
                        binding,
                        sql);
            } else {
                sql.append("?");
            }
            sql.append(",");
        }
        sql.setLength(sql.length() - 1);
        sql.append(" ");

        PreparedFilterToSQL toSQL = null;
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            // encode filter
            try {
                toSQL = createPreparedFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        PreparedStatement ps = cx.prepareStatement(sql.toString());
        LOGGER.log(Level.FINE, "Updating features with prepared statement: {0}", sql);

        int i = 0;
        int j = 0;
        for (; i < attributes.length; i++) {
            // skip exposed primary key columns
            AttributeDescriptor att = attributes[i];
            String attName = att.getLocalName();
            if (pkColumnNames.contains(attName)) {
                continue;
            }

            Class binding = att.getType().getBinding();
            Object value = values[i];
            if (Geometry.class.isAssignableFrom(binding)) {
                Geometry g = (Geometry) value;
                dialect.setGeometryValue(
                        g, getDescriptorDimension(att), getDescriptorSRID(att), binding, ps, j + 1);
            } else {
                EnumMapper mapper = (EnumMapper) att.getUserData().get(JDBCDataStore.JDBC_ENUM_MAP);
                if (mapper != null) {
                    value = mapper.fromString(Converters.convert(value, String.class));
                    binding = Integer.class;
                }
                dialect.setValue(value, binding, ps, j + 1, cx);
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine((j + 1) + " = " + value);
            }
            // we do this only if we did not skip the exposed pk
            j++;
        }

        if (toSQL != null) {
            setPreparedFilterValues(ps, toSQL, j, cx);
        }

        return ps;
    }

    /**
     * Creates a new instance of a filter to sql encoder.
     *
     * <p>The <tt>featureType</tt> may be null but it is not recommended. Such a case where this may
     * neccessary is when a literal needs to be encoded in isolation.
     */
    public FilterToSQL createFilterToSQL(SimpleFeatureType featureType) {
        return initializeFilterToSQL(((BasicSQLDialect) dialect).createFilterToSQL(), featureType);
    }

    /** Creates a new instance of a filter to sql encoder to be used in a prepared statement. */
    public PreparedFilterToSQL createPreparedFilterToSQL(SimpleFeatureType featureType) {
        return initializeFilterToSQL(
                ((PreparedStatementSQLDialect) dialect).createPreparedFilterToSQL(), featureType);
    }

    /** Helper method to initialize a filter encoder instance. */
    protected <F extends FilterToSQL> F initializeFilterToSQL(
            F toSQL, final SimpleFeatureType featureType) {
        toSQL.setSqlNameEscape(dialect.getNameEscape());

        if (featureType != null) {
            // set up a fid mapper
            // TODO: remove this
            final PrimaryKey key;

            try {
                key = getPrimaryKey(featureType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            toSQL.setFeatureType(featureType);
            toSQL.setPrimaryKey(key);
            toSQL.setDatabaseSchema(databaseSchema);
        }

        return toSQL;
    }

    /**
     * Helper method to encode table name which checks if a schema is set and prefixes the table
     * name with it.
     */
    public void encodeTableName(String tableName, StringBuffer sql, Hints hints)
            throws SQLException {
        encodeAliasedTableName(tableName, sql, hints, null);
    }

    /**
     * Helper method to encode table name which checks if a schema is set and prefixes the table
     * name with it, with the addition of an alias to the name
     */
    public void encodeAliasedTableName(
            String tableName, StringBuffer sql, Hints hints, String alias) throws SQLException {
        VirtualTable vtDefinition = virtualTables.get(tableName);
        if (vtDefinition != null) {
            sql.append("(").append(vtDefinition.expandParameters(hints)).append(")");
            if (alias == null) {
                alias = "vtable";
            }
            dialect.encodeTableAlias(alias, sql);
        } else {
            if (databaseSchema != null) {
                dialect.encodeSchemaName(databaseSchema, sql);
                sql.append(".");
            }

            dialect.encodeTableName(tableName, sql);
            if (alias != null) {
                dialect.encodeTableAlias(alias, sql);
            }
        }
    }

    /** Helper method to encode the join clause(s) of a query. */
    protected void encodeTableJoin(
            SimpleFeatureType featureType, JoinInfo join, Query query, StringBuffer sql)
            throws SQLException {
        encodeAliasedTableName(
                featureType.getTypeName(), sql, query.getHints(), join.getPrimaryAlias());

        for (JoinPart part : join.getParts()) {
            sql.append(" ");
            dialect.encodeJoin(part.getJoin().getType(), sql);
            sql.append(" ");
            encodeAliasedTableName(
                    part.getQueryFeatureType().getTypeName(),
                    sql,
                    setKeepWhereClausePlaceHolderHint(null, true),
                    part.getAlias());
            sql.append(" ON ");

            Filter j = part.getJoinFilter();

            FilterToSQL toSQL = getFilterToSQL(null);
            toSQL.setInline(true);
            try {
                sql.append(" ").append(toSQL.encodeToString(j));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (sql.indexOf(WHERE_CLAUSE_PLACE_HOLDER) >= 0) {
            // this means that one of the joined table provided a placeholder
            throw new RuntimeException(
                    "Joins between virtual tables that provide a :where_placeholder: are not supported: "
                            + sql);
        }
    }

    protected List<FilterToSQL> encodeWhereJoin(
            SimpleFeatureType featureType, JoinInfo join, StringBuffer sql) throws IOException {

        List<FilterToSQL> toSQL = new ArrayList<>();

        boolean whereEncoded = false;
        Filter filter = join.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            sql.append(" WHERE ");
            whereEncoded = true;

            // encode filter
            toSQL.add(filter(featureType, filter, sql));
        }

        // filters for joined feature types
        for (JoinPart part : join.getParts()) {
            filter = part.getPreFilter();
            if (filter == null || Filter.INCLUDE.equals(filter)) continue;

            if (!whereEncoded) {
                sql.append(" WHERE ");
                whereEncoded = true;
            } else {
                sql.append(" AND ");
            }

            toSQL.add(filter(part.getQueryFeatureType(), filter, sql));
        }

        return toSQL;
    }

    /** Helper method for setting the gml:id of a geometry as user data. */
    protected void setGmlProperties(Geometry g, String gid, String name, String description) {
        // set up the user data
        Map<Object, Object> userData = null;

        if (g.getUserData() != null) {
            if (g.getUserData() instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<Object, Object> cast = (Map) g.getUserData();
                userData = cast;
            } else {
                userData = new HashMap<>();
                userData.put(g.getUserData().getClass(), g.getUserData());
            }
        } else {
            userData = new HashMap<>();
        }

        if (gid != null) {
            userData.put("gml:id", gid);
        }

        if (name != null) {
            userData.put("gml:name", name);
        }

        if (description != null) {
            userData.put("gml:description", description);
        }

        g.setUserData(userData);
    }

    /**
     * Applies the givenb limit/offset elements if the dialect supports them
     *
     * @param sql The sql to be modified
     * @param offset starting index
     * @param limit max number of features
     */
    void applyLimitOffset(StringBuffer sql, final Integer offset, final int limit) {
        if (checkLimitOffset(offset, limit)) {
            dialect.applyLimitOffset(sql, limit, offset != null ? offset : 0);
        }
    }

    /**
     * Applies the limit/offset elements to the query if they are specified and if the dialect
     * supports them
     *
     * @param sql The sql to be modified
     * @param query the query that holds the limit and offset parameters
     */
    public void applyLimitOffset(StringBuffer sql, Query query) {
        applyLimitOffset(sql, query.getStartIndex(), query.getMaxFeatures());
    }

    /**
     * Checks if the query needs limit/offset treatment
     *
     * @return true if the query needs limit/offset treatment and if the sql dialect can do that
     *     natively
     */
    boolean checkLimitOffset(final Integer offset, final int limit) {
        // if we cannot, don't bother checking the query
        if (!dialect.isLimitOffsetSupported()) return false;

        return limit != Integer.MAX_VALUE || (offset != null && offset > 0);
    }

    /**
     * Utility method for closing a result set.
     *
     * <p>This method closed the result set "safely" in that it never throws an exception. Any
     * exceptions that do occur are logged at {@link Level#FINER}.
     *
     * @param rs The result set to close.
     */
    public void closeSafe(ResultSet rs) {
        if (rs == null) {
            return;
        }

        try {
            rs.close();
        } catch (SQLException e) {
            String msg = "Error occurred closing result set";
            LOGGER.warning(msg);

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, msg, e);
            }
        }
    }

    /**
     * Utility method for closing a statement.
     *
     * <p>This method closed the statement"safely" in that it never throws an exception. Any
     * exceptions that do occur are logged at {@link Level#FINER}.
     *
     * @param st The statement to close.
     */
    public void closeSafe(Statement st) {
        if (st == null) {
            return;
        }

        try {
            st.close();
        } catch (SQLException e) {
            String msg = "Error occurred closing statement";
            LOGGER.warning(msg);

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, msg, e);
            }
        }
    }

    /**
     * Utility method for closing a connection.
     *
     * <p>This method closed the connection "safely" in that it never throws an exception. Any
     * exceptions that do occur are logged at {@link Level#FINER}.
     *
     * @param cx The connection to close.
     */
    public void closeSafe(Connection cx) {
        if (cx == null) {
            return;
        }

        try {
            //            System.out.println("Closing connection " + System.identityHashCode(cx));
            cx.close();
            LOGGER.fine("CLOSE CONNECTION");
        } catch (SQLException e) {
            String msg = "Error occurred closing connection";
            LOGGER.warning(msg);

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, msg, e);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        if (dataSource != null) {
            LOGGER.severe(
                    "There's code using JDBC based datastore and "
                            + "not disposing them. This may lead to temporary loss of database connections. "
                            + "Please make sure all data access code calls DataStore.dispose() "
                            + "before freeing all references to it");
            dispose();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (dataSource != null && dataSource instanceof ManageableDataSource) {
            try {
                @SuppressWarnings("PMD.CloseResource") // actually closing it here
                ManageableDataSource mds = (ManageableDataSource) dataSource;
                mds.close();
            } catch (SQLException e) {
                // it's ok, we did our best..
                LOGGER.log(Level.FINE, "Could not close dataSource", e);
            }
        }
        // Store the exception for logging later if the object is used after disposal
        if (TRACE_ENABLED) {
            disposedBy =
                    new RuntimeException(
                            "DataSource disposed by thread " + Thread.currentThread().getName());
        }
        dataSource = null;
    }
    /**
     * Checks if geometry generalization required and makes sense
     *
     * @param hints hints hints passed in
     * @param gatt Geometry attribute descriptor
     * @return true to indicate generalization
     */
    protected boolean isGeneralizationRequired(Hints hints, GeometryDescriptor gatt) {
        return isGeometryReduceRequired(hints, gatt, Hints.GEOMETRY_GENERALIZATION);
    }

    /**
     * Checks if geometry simplification required and makes sense
     *
     * @param hints hints hints passed in
     * @param gatt Geometry attribute descriptor
     * @return true to indicate simplification
     */
    protected boolean isSimplificationRequired(Hints hints, GeometryDescriptor gatt) {
        return isGeometryReduceRequired(hints, gatt, Hints.GEOMETRY_SIMPLIFICATION);
    }
    /**
     * Checks if reduction required and makes sense
     *
     * @param hints hints passed in
     * @param gatt Geometry attribute descriptor
     * @param param {@link Hints#GEOMETRY_GENERALIZATION} or {@link Hints#GEOMETRY_SIMPLIFICATION}
     * @return true to indicate reducing the geometry, false otherwise
     */
    protected boolean isGeometryReduceRequired(
            Hints hints, GeometryDescriptor gatt, Hints.Key param) {
        if (hints == null) return false;
        if (hints.containsKey(param) == false) return false;
        if (gatt.getType().getBinding() == Point.class && !dialect.canSimplifyPoints())
            return false;
        return true;
    }

    /**
     * Encoding a geometry column with respect to hints Supported Hints are provided by {@link
     * SQLDialect#addSupportedHints(Set)}
     *
     * @param hints , may be null
     */
    public void encodeGeometryColumn(GeometryDescriptor gatt, StringBuffer sql, Hints hints) {
        encodeGeometryColumn(gatt, null, sql, hints);
    }

    protected void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, StringBuffer sql, Hints hints) {

        int srid = getDescriptorSRID(gatt);
        if (isGeneralizationRequired(hints, gatt) == true) {
            Double distance = (Double) hints.get(Hints.GEOMETRY_GENERALIZATION);
            dialect.encodeGeometryColumnGeneralized(gatt, prefix, srid, sql, distance);
            return;
        }

        if (isSimplificationRequired(hints, gatt) == true) {
            Double distance = (Double) hints.get(Hints.GEOMETRY_SIMPLIFICATION);
            dialect.encodeGeometryColumnSimplified(gatt, prefix, srid, sql, distance);
            return;
        }

        dialect.encodeGeometryColumn(gatt, prefix, srid, hints, sql);
    }

    /**
     * Builds a transaction object around a user provided connection. The returned transaction
     * allows the store to work against an externally managed transaction, such as in J2EE
     * enviroments. It is the duty of the caller to ensure the connection is to the same database
     * managed by this {@link JDBCDataStore}.
     *
     * <p>Calls to {@link Transaction#commit()}, {@link Transaction#rollback()} and {@link
     * Transaction#close()} will not result in corresponding calls to the provided {@link
     * Connection} object.
     *
     * @param cx The externally managed connection
     */
    public Transaction buildTransaction(Connection cx) {
        DefaultTransaction tx = new DefaultTransaction();

        State state = new JDBCTransactionState(cx, this, true);
        tx.putState(this, state);

        return tx;
    }

    /** Creates a new database index */
    public void createIndex(Index index) throws IOException {
        SimpleFeatureType schema = getSchema(index.typeName);
        Connection cx = null;
        try {
            cx = getConnection(Transaction.AUTO_COMMIT);
            dialect.createIndex(cx, schema, databaseSchema, index);
        } catch (SQLException e) {
            throw new IOException("Failed to create index", e);
        } finally {
            closeSafe(cx);
        }
    }

    /** Creates a new database index */
    public void dropIndex(String typeName, String indexName) throws IOException {
        SimpleFeatureType schema = getSchema(typeName);

        Connection cx = null;
        try {
            cx = getConnection(Transaction.AUTO_COMMIT);
            dialect.dropIndex(cx, schema, databaseSchema, indexName);
        } catch (SQLException e) {
            throw new IOException("Failed to create index", e);
        } finally {
            closeSafe(cx);
        }
    }

    /**
     * Lists all indexes associated to the given feature type
     *
     * @param typeName Name of the type for which indexes are searched. It's mandatory
     */
    public List<Index> getIndexes(String typeName) throws IOException {
        // just to ensure we have the type name specified
        getSchema(typeName);

        Connection cx = null;
        try {
            cx = getConnection(Transaction.AUTO_COMMIT);
            return dialect.getIndexes(cx, databaseSchema, typeName);
        } catch (SQLException e) {
            throw new IOException("Failed to create index", e);
        } finally {
            closeSafe(cx);
        }
    }

    /**
     * Escapes a name pattern used in e.g. {@link java.sql.DatabaseMetaData#getColumns(String,
     * String, String, String)} when passed in argument is an exact name and not a pattern.
     *
     * <p>When a table name or column name contains underscore (or percen, but this is rare) the
     * underscore is treated as a placeholder and not an actual character. So if our intention is to
     * match an exact name, we must escape such characters.
     */
    public String escapeNamePattern(DatabaseMetaData metaData, String name) throws SQLException {
        if (namePatternEscaping == null) {
            namePatternEscaping = new NamePatternEscaping(metaData.getSearchStringEscape());
        }
        return namePatternEscaping.escape(name);
    }
}
