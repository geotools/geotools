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
package org.geotools.jdbc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import javax.sql.DataSource;
import javax.xml.transform.TransformerException;

import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
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
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.store.ContentState;
import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsLessThanOrEqualTo;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.GmlObjectId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;


/**
 * Datastore implementation for jdbc based relational databases.
 * <p>
 * This class is not intended to be subclassed on a per database basis. Instead
 * the notion of a "dialect" is used.
 * </p>
 * <p>
 *   <h3>Dialects</h3>
 * A dialect ({@link SQLDialect}) encapsulates all the operations that are database
 * specific. Therefore to implement a jdbc based datastore one must extend SQLDialect.
 * The specific dialect to use is set using {@link #setSQLDialect(SQLDialect)}.
 * </p>
 * <p>
 *   <h3>Database Connections</h3>
 *   Connections to the underlying database are obtained through a {@link DataSource}.
 *   A datastore must be specified using {@link #setDataSource(DataSource)}.
 * </p>
 * <p>
 *   <h3>Schemas</h3>
 * This datastore supports the notion of database schemas, which is more or less
 * just a grouping of tables. When a schema is specified, only those tables which
 * are part of the schema are provided by the datastore. The schema is specified
 * using {@link #setDatabaseSchema(String)}.
 * </p>
 * <p>
 *   <h3>Spatial Functions</h3>
 * The set of spatial operations or functions that are supported by the
 * specific database are reported with a {@link FilterCapabilities} instance.
 * This is specified using {@link #setFilterCapabilities(FilterCapabilities)}.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public final class JDBCDataStore extends ContentDataStore
    implements GmlObjectStore {
    
    /**
     * The native SRID associated to a certain descriptor
     * TODO: qualify this key with 'org.geotools.jdbc'
     */
    public static final String JDBC_NATIVE_SRID = "nativeSRID";
    
    /**
     * Boolean marker stating whether the feature type is to be considered read only
     */
    public static final String JDBC_READ_ONLY = "org.geotools.jdbc.readOnly";
    
    /**
     * The key for attribute descriptor user data which specifies the original database column data 
     * type.
     */
    public static final String JDBC_NATIVE_TYPENAME = "org.geotools.jdbc.nativeTypeName";
    
    /**
     * name of table to use to store geometries when {@link #associations}
     * is set.
     */
    protected static final String GEOMETRY_TABLE = "geometry";

    /**
     * name of table to use to store multi geometries made up of non-multi
     * geometries when {@link #associations} is set.
     */
    protected static final String MULTI_GEOMETRY_TABLE = "multi_geometry";

    /**
     * name of table to use to store geometry associations when {@link #associations}
     * is set.
     */
    protected static final String GEOMETRY_ASSOCIATION_TABLE = "geometry_associations";

    /**
     * name of table to use to store feature relationships (information about
     * associations) when {@link #associations} is set.
     */
    protected static final String FEATURE_RELATIONSHIP_TABLE = "feature_relationships";

    /**
     * name of table to use to store feature associations when {@link #associations}
     * is set.
     */
    protected static final String FEATURE_ASSOCIATION_TABLE = "feature_associations";
    
    /**
     * The default primary key finder, looks in the default metadata table first, uses heuristics later
     */
    protected static final PrimaryKeyFinder DEFAULT_PRIMARY_KEY_FINDER = new CompositePrimaryKeyFinder(
            new MetadataTablePrimaryKeyFinder(), new HeuristicPrimaryKeyFinder());
    
    /**
     * The envelope returned when bounds is called against a geometryless feature type
     */
    protected static final ReferencedEnvelope EMPTY_ENVELOPE = new ReferencedEnvelope();  

    /**
     * data source
     */
    protected DataSource dataSource;

    /**
     * the dialect of sql
     */
    protected SQLDialect dialect;

    /**
     * The database schema.
     */
    protected String databaseSchema;

    /**
     * sql type to java class mappings
     */
    protected HashMap<Integer, Class<?>> sqlTypeToClassMappings;

    /**
     * sql type name to java class mappings
     */
    protected HashMap<String, Class<?>> sqlTypeNameToClassMappings;

    /**
     * java class to sql type mappings;
     */
    protected HashMap<Class<?>, Integer> classToSqlTypeMappings;

    /**
     * sql type to sql type name overrides
     */
    protected HashMap<Integer,String> sqlTypeToSqlTypeNameOverrides;

    /**
     * Feature visitor to aggregate function name
     */
    protected HashMap<Class<? extends FeatureVisitor>,String> aggregateFunctions;
    
    /**
     * flag controlling if the datastore is supporting feature and geometry
     * relationships with associations
     */
    protected boolean associations = false;
    
    /**
     * The fetch size for this datastore, defaulting to 1000. Set to a value less or equal
     * to 0 to disable fetch size limit and grab all the records in one shot.
     */
    protected int fetchSize;
    
    /**
     * flag controlling whether primary key columns of a table are exposed via the 
     * feature type.
     */
    protected boolean exposePrimaryKeyColumns = false;
    
    /**
     * Finds the primary key definitions
     */
    protected PrimaryKeyFinder primaryKeyFinder = DEFAULT_PRIMARY_KEY_FINDER;
    
    /**
     * Contains the SQL definition of the various virtual tables
     */
    protected Map<String, VirtualTable> virtualTables = new ConcurrentHashMap<String, VirtualTable>();

    /**
     * Adds a virtual table to the data store. If a virtual table with the same name was registered this
     * method will replace it with the new one.
     *      * @param vt
     * @throws IOException If the view definition is not valid
     */
    public void addVirtualTable(VirtualTable vtable) throws IOException {
        try {
            virtualTables.put(vtable.getName(), new VirtualTable(vtable));
            // the new vtable might be overriding a previous definition
            entries.remove(new NameImpl(namespaceURI, vtable.getName()));
            getSchema(vtable.getName());
        } catch(IOException e) {
            virtualTables.remove(vtable.getName());
            throw e;
        }
    }
    
    /**
     * Removes and returns the specified virtual table
     * @param name
     * @return
     */
    public VirtualTable removeVirtualTable(String name) {
        // the new vtable might be overriding a previous definition
        VirtualTable vt =  virtualTables.remove(name);
        if(vt != null) {
            entries.remove(new NameImpl(namespaceURI, name));
        }
        return vt;
    }
    

    /**
     * Returns a live, immutable view of the virtual tables map (from name to definition)  
     * @return
     */
    public Map<String, VirtualTable> getVirtualTables() {
        Map<String, VirtualTable> result = new HashMap<String, VirtualTable>();
        for (String key : virtualTables.keySet()) {
            result.put(key, new VirtualTable(virtualTables.get(key)));
        }
        return Collections.unmodifiableMap(result);
    }
    
    /**
     * Returns the finder used to build {@link PrimaryKey} representations
     * @return
     */
    public PrimaryKeyFinder getPrimaryKeyFinder() {
        return primaryKeyFinder;
    }

    /**
     * Sets the finder used to build {@link PrimaryKey} representations
     * @param primaryKeyFinder
     */
    public void setPrimaryKeyFinder(PrimaryKeyFinder primaryKeyFinder) {
        this.primaryKeyFinder = primaryKeyFinder;
    }

    /**
     * The current fetch size. The fetch size influences how many records are read from the
     * dbms at a time. If set to a value less or equal than zero, all the records will be
     * read in one shot, severily increasing the memory requirements to read a big number
     * of features.
     * @return
     */
    public int getFetchSize() {
        return fetchSize;
    }

    /**
     * Changes the fetch size.
     * @param fetchSize
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
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
        this.exposePrimaryKeyColumns = exposePrimaryKeyColumns;
    }
    
    /**
     * The dialect the datastore uses to generate sql statements in order to
     * communicate with the underlying database.
     *
     * @return The dialect, never <code>null</code>.
     */
    public SQLDialect getSQLDialect() {
        return dialect;
    }

    /**
     * Sets the dialect the datastore uses to generate sql statements in order to
     * communicate with the underlying database.
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
     * The data source the datastore uses to obtain connections to the underlying
     * database.
     *
     * @return The data source, never <code>null</code>.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Sets the data source the datastore uses to obtain connections to the underlying
     * database.
     *
     * @param dataSource The data source, never <code>null</code>.
     */
    public void setDataSource(DataSource dataSource) {
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
     * <p>
     * When this value is set only those tables which are part of the schema are
     * served through the datastore. This value can be set to <code>null</code>
     * to specify no particular schema.
     * </p>
     * @param databaseSchema The schema, may be <code>null</code>.
     */
    public void setDatabaseSchema(String databaseSchema) {
        this.databaseSchema = databaseSchema;
    }

    /**
     * The filter capabilities which reports which spatial operations the
     * underlying database can handle natively.
     *
     * @return The filter capabilities, never <code>null</code>.
     */
    public FilterCapabilities getFilterCapabilities() {
        if ( dialect instanceof PreparedStatementSQLDialect)
            return ((PreparedStatementSQLDialect)dialect).createPreparedFilterToSQL().getCapabilities();
        else
            return ((BasicSQLDialect)dialect).createFilterToSQL().getCapabilities();
    }

    /**
     * Flag controlling if the datastore is supporting feature and geometry
     * relationships with associations
     */
    public boolean isAssociations() {
        return associations;
    }

    /**
     * Sets the flag controlling if the datastore is supporting feature and geometry
     * relationships with associations
     */
    public void setAssociations(boolean foreignKeyGeometries) {
        this.associations = foreignKeyGeometries;
    }

    /**
     * The sql type to java type mappings that the datastore uses when reading
     * and writing objects to and from the database.
     * <p>
     * These mappings are derived from {@link SQLDialect#registerSqlTypeToClassMappings(java.util.Map)}
     * </p>
     * @return The mappings, never <code>null</code>.
     */
    public Map<Integer, Class<?>> getSqlTypeToClassMappings() {
        if (sqlTypeToClassMappings == null) {
            sqlTypeToClassMappings = new HashMap<Integer, Class<?>>();
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
            sqlTypeNameToClassMappings = new HashMap<String, Class<?>>();
            dialect.registerSqlTypeNameToClassMappings(sqlTypeNameToClassMappings);
        }

        return sqlTypeNameToClassMappings;
    }

    /**
     * The java type to sql type mappings that the datastore uses when reading
     * and writing objects to and from the database.
     * <p>
     * These mappings are derived from {@link SQLDialect#registerClassToSqlMappings(Map)}
     * </p>
     * @return The mappings, never <code>null</code>.
     */
    public Map<Class<?>, Integer> getClassToSqlTypeMappings() {
        if (classToSqlTypeMappings == null) {
            classToSqlTypeMappings = new HashMap<Class<?>, Integer>();
            dialect.registerClassToSqlMappings(classToSqlTypeMappings);
        }

        return classToSqlTypeMappings;
    }

    /**
     * Returns any ovverides which map integer constants for database types (from {@link Types})
     * to database type names.
     * <p>
     * This method will return an empty map when there are no overrides.
     * </p>
     */
    public Map<Integer,String> getSqlTypeToSqlTypeNameOverrides() {
        if ( sqlTypeToSqlTypeNameOverrides == null ) {
            sqlTypeToSqlTypeNameOverrides = new HashMap<Integer,String>();
            dialect.registerSqlTypeToSqlTypeNameOverrides(sqlTypeToSqlTypeNameOverrides);
        }

        return sqlTypeToSqlTypeNameOverrides;
    }

    /**
     * Returns the supported aggregate functions and the visitors they map to.
     */
    public Map<Class<? extends FeatureVisitor>,String> getAggregateFunctions() {
        if ( aggregateFunctions == null ) {
            aggregateFunctions = new HashMap();
            dialect.registerAggregateFunctions(aggregateFunctions);
        }
        return aggregateFunctions;
    }
    
    /**
     * Returns the java type mapped to the specified sql type.
     * <p>
     * If there is no such type mapped to <tt>sqlType</tt>, <code>null</code>
     * is returned.
     * </p>
     * @param sqlType The integer constant for the sql type from {@link Types}.
     *
     * @return The mapped java class, or <code>null</code>. if no such mapping exists.
     */
    public Class<?> getMapping(int sqlType) {
        return getSqlTypeToClassMappings().get(new Integer(sqlType));
    }

    /**
     * Returns the java type mapped to the specified sql type name.
     * <p>
     * If there is no such type mapped to <tt>sqlTypeName</tt>, <code>null</code>
     * is returned.
     * </p>
     * @param sqlTypeName The name of the sql type.
     *
     * @return The mapped java class, or <code>null</code>. if no such mapping exists.
     */
    public Class<?> getMapping(String sqlTypeName) {
        return getSqlTypeNameToClassMappings().get(sqlTypeName);
    }

    /**
     * Returns the sql type mapped to the specified java type.
     * <p>
     * If there is no such type mapped to <tt>clazz</tt>, <code>Types.OTHER</code>
     * is returned.
     * </p>
     * @param clazz The java class.
     *
     * @return The mapped sql type from {@link Types}, Types.OTHER if no such
     * mapping exists.
     */
    public Integer getMapping(Class<?> clazz) {
        Integer mapping = getClassToSqlTypeMappings().get(clazz);

        if (mapping == null) {
            //no match, try a "fuzzy" match in which we find the super class which matches best 
            List<Map.Entry<Class<?>, Integer>> matches = new ArrayList();
            for (Map.Entry<Class<?>, Integer> e : getClassToSqlTypeMappings().entrySet()) {
                if (e.getKey().isAssignableFrom(clazz) ) {
                    matches.add(e);
                }
            }
            if (!matches.isEmpty()) {
                if (matches.size() == 1) {
                    //single match, great, use it
                    mapping = matches.get(0).getValue();
                }
                else {
                    // sort to match lowest class in type hierarchy, if we end up with a list like:
                    // A, B where B is a super class of A, then chose A since it is the closest 
                    // subclass to match
                    
                    Collections.sort(matches, new Comparator<Map.Entry<Class<?>, Integer>>() {
                        public int compare(Entry<Class<?>, Integer> o1, Entry<Class<?>, Integer> o2) {
                            if (o1.getKey().isAssignableFrom(o2.getKey())) {
                                return 1;
                            }
                            if (o2.getKey().isAssignableFrom(o1.getKey())) {
                                return -1;
                            }
                            return 0;
                        }
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
     * <p>
     * This method will map the classes of the attributes of <tt>featureType</tt>
     * to sql types and generate a 'CREATE TABLE' statement against the underlying
     * database.
     * </p>
     * @see DataStore#createSchema(SimpleFeatureType)
     *
     * @throws IllegalArgumentException If the table already exists.
     * @throws IOException If the table cannot be created due to an error.
     */
    public void createSchema(final SimpleFeatureType featureType)
        throws IOException {
        if (entry(featureType.getName()) != null) {
            String msg = "Schema '" + featureType.getName() + "' already exists";
            throw new IllegalArgumentException(msg);
        }

        //execute the create table statement
        //TODO: create a primary key and a spatial index
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

    /**
     * 
     */
    public Object getGmlObject(GmlObjectId id, Hints hints) throws IOException {
        //geometry?
        if ( isAssociations() ) {
        
            Connection cx = createConnection();
            try {
                try {
                    Statement st = null;
                    ResultSet rs = null;
                    
                    if ( getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                        st = selectGeometrySQLPS(id.getID(), cx);
                        rs = ((PreparedStatement)st).executeQuery();
                    }
                    else {
                        String sql = selectGeometrySQL(id.getID());
                        LOGGER.log(Level.FINE, "Get GML object: {0}", sql );
                        
                        st = cx.createStatement();
                        rs = st.executeQuery( sql );
                    }
                    
                    try {
                        if ( rs.next() ) {
                            //read the geometry
                            Geometry g = getSQLDialect().decodeGeometryValue(
                                null, rs, "geometry", getGeometryFactory(), cx );
                            
                            //read the metadata
                            String name = rs.getString( "name" );
                            String desc = rs.getString( "description" );
                            setGmlProperties(g, id.getID(),name,desc);
                            
                            return g;
                        }
                    }
                    finally {
                        closeSafe( rs );
                        closeSafe( st );
                    }
                    
                }
                catch( SQLException e ) {
                    throw (IOException) new IOException().initCause( e );
                }
            }
            finally {
                closeSafe( cx );
            }
        }
        
        //regular feature, first feature out the feature type
        int i = id.getID().indexOf('.');
        if ( i == -1 ) {
            LOGGER.info( "Unable to determine feature type for GmlObjectId:" + id );
            return null; 
        }
        
        //figure out the type name from the id
        String featureTypeName = id.getID().substring( 0, i );
        SimpleFeatureType featureType = getSchema( featureTypeName );
        if ( featureType == null ) {
            throw new IllegalArgumentException( "No such feature type: " + featureTypeName );
        }
        
        //load the feature
        Id filter = getFilterFactory().id(Collections.singleton(id));
        DefaultQuery query = new DefaultQuery( featureTypeName );
        query.setFilter( filter );
        query.setHints( hints );
        
        SimpleFeatureCollection features = 
            getFeatureSource( featureTypeName ).getFeatures( query );  
        if ( !features.isEmpty() ) {
            SimpleFeatureIterator fi = features.features();
            try {
                if ( fi.hasNext() ) {
                    return fi.next();
                }
            }
            finally {
                features.close( fi );
            }
        }
        
        return null;
    }
    
    /**
     * Creates a new instance of {@link JDBCFeatureStore}.
     *
     * @see ContentDataStore#createFeatureSource(ContentEntry)
     */
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
    protected ContentState createContentState(ContentEntry entry) {
        JDBCState state = new JDBCState(entry);
        state.setExposePrimaryKeyColumns(exposePrimaryKeyColumns);
        return state;
    }
    
    /**
     * Generates the list of type names provided by the database.
     * <p>
     * The list is generated from the underlying database metadata.
     * </p>
     */
    protected List createTypeNames() throws IOException {
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
        List typeNames = new ArrayList();

        try {
            DatabaseMetaData metaData = cx.getMetaData();
            ResultSet tables = metaData.getTables(null, databaseSchema, "%",
                    new String[] { "TABLE", "VIEW" });

            try {
                while (tables.next()) {
                    String schemaName = tables.getString( "TABLE_SCHEM");
                    String tableName = tables.getString("TABLE_NAME");

                    //use the dialect to filter
                    if (!dialect.includeTable(schemaName, tableName, cx)) {
                        continue;
                    }

                    typeNames.add(new NameImpl(namespaceURI, tableName));
                }
            } finally {
                closeSafe(tables);
            }
        } catch (SQLException e) {
            throw (IOException) new IOException("Error occurred getting table name list.").initCause(e);
        } finally {
            closeSafe(cx);
        }

        
        for(String virtualTable : virtualTables.keySet()) {
            typeNames.add(new NameImpl(namespaceURI, virtualTable));
        }
        return typeNames;
    }

    /**
     * Returns the primary key object for a particular entry, deriving it from
     * the underlying database metadata.
     *
     */
    protected PrimaryKey getPrimaryKey(ContentEntry entry)
        throws IOException {
        JDBCState state = (JDBCState) entry.getState(Transaction.AUTO_COMMIT);

        if (state.getPrimaryKey() == null) {
            synchronized (this) {
                if (state.getPrimaryKey() == null) {
                    //get metadata from database
                    Connection cx = createConnection();

                    try {
                        PrimaryKey pkey;
                        String tableName = entry.getName().getLocalPart();
                        if(virtualTables.containsKey(tableName)) {
                            VirtualTable vt = virtualTables.get(tableName);
                            if(vt.getPrimaryKeyColumns().size() == 0) {
                                pkey = new NullPrimaryKey( tableName );
                            } else {
                                List<ColumnMetadata> metas = JDBCFeatureSource.getColumnMetadata(cx, vt, dialect, this);
                                
                                List<PrimaryKeyColumn> kcols = new ArrayList<PrimaryKeyColumn>();
                                for (String pkName : vt.getPrimaryKeyColumns()) {
                                    // look for the pk type
                                    Class binding = null;
                                    for(ColumnMetadata meta : metas) {
                                        if(meta.name.equals(pkName)) {
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
                            pkey = primaryKeyFinder.getPrimaryKey(this, databaseSchema, tableName, cx);
                            
                            if ( pkey == null ) {
                                String msg = "No primary key or unique index found for " + tableName + ".";
                                LOGGER.warning(msg);
    
                                pkey = new NullPrimaryKey( tableName );
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
    
    /**
     * Checks whether the tableName corresponds to a view
     */
    boolean isView(DatabaseMetaData metaData, String databaseSchema, String tableName) 
        throws SQLException  {
        
        ResultSet tables = null;
        try {
            tables = metaData.getTables(null, databaseSchema, tableName, new String[] {"VIEW"});
            return tables.next();
        } finally {
            closeSafe(tables);
        }
    }

    /*
     * Creates a key from a primary key or unique index.
     */
    PrimaryKey createPrimaryKey(ResultSet index,DatabaseMetaData metaData, String tableName,
        Connection cx ) throws SQLException {
        ArrayList<PrimaryKeyColumn> cols = new ArrayList();
        
        while( index.next() ) {
            String columnName = index.getString("COLUMN_NAME");
            //work around. For some reason the first record returned is always 'empty'
            //this was tested on Oracle and Postgres databases
            if ( columnName == null ) {
                continue;
            }
            
            //look up the type ( should only be one row )
            Class columnType = getColumnType(metaData, databaseSchema, tableName, columnName);
            
    
            //determine which type of primary key we have
            PrimaryKeyColumn col = null;
            
            //1. Auto Incrementing?
            Statement st = cx.createStatement();
    
            try {
                //not actually going to get data
                st.setFetchSize(1);
    
                StringBuffer sql = new StringBuffer();
                sql.append("SELECT ");
                dialect.encodeColumnName(columnName, sql);
                sql.append(" FROM ");
                encodeTableName(tableName, sql, null);
    
                sql.append(" WHERE 0=1");
    
                LOGGER.log(Level.FINE, "Grabbing table pk metadata: {0}", sql);
    
                ResultSet rs = st.executeQuery(sql.toString());
    
                try {
                    if (rs.getMetaData().isAutoIncrement(1)) {
                        col = new AutoGeneratedPrimaryKeyColumn( columnName, columnType );
                    }
                } finally {
                    closeSafe(rs);
                }
            } finally {
                closeSafe(st);
            }
    
            //2. Has a sequence?
            if (col == null) {
                try {
                    String sequenceName = dialect.getSequenceForColumn( databaseSchema,
                            tableName, columnName, cx );
                    if ( sequenceName != null ) {
                        col = new SequencedPrimaryKeyColumn( columnName, columnType, sequenceName );
                    }
                }
                catch( Exception e ) {
                    //log the exception , and continue on
                    LOGGER.log( Level.WARNING, "Error occured determining sequence for "
                        + columnName + ", " + tableName, e );
                }
            }
    
            if (col == null) {
                col = new NonIncrementingPrimaryKeyColumn( columnName, columnType );
            }
    
            cols.add(col);
        }
        
        if (!cols.isEmpty()) {
            return new PrimaryKey( tableName, cols );
        }
        return null; 
    }
    
    /**
     * Returns the type of the column by inspecting the metadata, with the collaboration
     * of the dialect
     */
    protected Class getColumnType(DatabaseMetaData metaData, String databaseSchema2,
            String tableName, String columnName) throws SQLException {
        ResultSet columns =  null;
        try {
            columns = metaData.getColumns(null, databaseSchema,
                    tableName, columnName);
            columns.next();
    
            int binding = columns.getInt("DATA_TYPE");
            Class columnType = getMapping(binding);
    
            if (columnType == null) {
                LOGGER.warning("No class for sql type " + binding);
                columnType = Object.class;
            }
            
            return columnType;
        } finally {
            columns.close();
        }
    }

    /**
     * Returns the primary key object for a particular feature type / table,
     * deriving it from the underlying database metadata.
     *
     */
    protected PrimaryKey getPrimaryKey(SimpleFeatureType featureType)
        throws IOException {
        return getPrimaryKey(ensureEntry(featureType.getName()));
    }
    
    /**
     * Returns the expose primary key columns flag for the specified feature type
     * @param featureType
     * @return
     * @throws IOException
     */
    protected boolean isExposePrimaryKeyColumns(SimpleFeatureType featureType) throws IOException {
        ContentEntry entry = ensureEntry(featureType.getName());
        JDBCState state = (JDBCState) entry.getState(Transaction.AUTO_COMMIT);
        return state.isExposePrimaryKeyColumns();
    }

    /**
     * Returns the bounds of the features for a particular feature type / table.
     * 
     * @param featureType
     *            The feature type / table.
     * @param query
     *            Specifies rows to include in bounds calculation, as well as how many features and
     *            the offset if needed
     */
    protected ReferencedEnvelope getBounds(SimpleFeatureType featureType, Query query, Connection cx)
            throws IOException {

        // handle geometryless case by returning an emtpy envelope
        if (featureType.getGeometryDescriptor() == null)
            return EMPTY_ENVELOPE;

        Statement st = null;
        ResultSet rs = null;
        ReferencedEnvelope bounds = new ReferencedEnvelope(featureType
                .getCoordinateReferenceSystem());
        try {
            // try optimized bounds computation only if we're targeting the entire table
            if (isFullBoundsQuery(query, featureType)) {
                List<ReferencedEnvelope> result = dialect.getOptimizedBounds(databaseSchema,
                        featureType, cx);
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
                LOGGER.log(Level.FINE, "Retriving bounding box: {0}", sql);

                st = cx.createStatement();
                rs = st.executeQuery(sql);
            }

            // scan through all the rows (just in case a non aggregated function was used)
            // and through all the columns (in case we have multiple geometry columns)
            CoordinateReferenceSystem flatCRS = CRS.getHorizontalCRS(featureType
                    .getCoordinateReferenceSystem());
            final int columns = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columns; i++) {
                    final Envelope envelope = dialect.decodeGeometryEnvelope(rs, i, st
                            .getConnection());
                    if (envelope != null) {
                        if (envelope instanceof ReferencedEnvelope) {
                            bounds = mergeEnvelope(bounds, (ReferencedEnvelope) envelope);
                        } else {
                            bounds = mergeEnvelope(bounds, new ReferencedEnvelope(envelope,
                                    flatCRS));
                        }
                    }
                }
                }
        } catch (Exception e) {
            String msg = "Error occured calculating bounds";
            throw (IOException) new IOException(msg).initCause(e);
        } finally {
            closeSafe(rs);
            closeSafe(st);
        }

        return bounds;
    }

    /**
     * Returns true if the query will hit all the geometry columns with no row filtering
     * (a condition that allows to use spatial index statistics to compute the table bounds)
     * @param query
     * @param schema
     * @return
     */
    private boolean isFullBoundsQuery(Query query, SimpleFeatureType schema) {
        
        if(query == null) {
            return true;
        }
        if(!Filter.INCLUDE.equals(query.getFilter())) {
            return false;
        }
        if(query.getProperties() == Query.ALL_PROPERTIES) {
            return true;
        }
        
        List<String> names = Arrays.asList(query.getPropertyNames());
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            if(ad instanceof GeometryDescriptor) {
                if(!names.contains(ad.getLocalName())) {
                    return false;
                }
            }
        }
        
        return true;
    }
    
    /**
     * Merges two envelopes handling possibly different CRS
     * @param base
     * @param merge
     * @return
     * @throws TransformException
     * @throws FactoryException
     */
    ReferencedEnvelope mergeEnvelope(ReferencedEnvelope base, ReferencedEnvelope merge) 
                        throws TransformException, FactoryException {
        if(base == null || base.isNull()) {
            return merge;
        } else if(merge == null || merge.isNull()) {
            return base;
        } else {
            // reproject and merge
            final CoordinateReferenceSystem crsBase = base.getCoordinateReferenceSystem();
            final CoordinateReferenceSystem crsMerge = merge.getCoordinateReferenceSystem();
            if(crsBase == null) {
                merge.expandToInclude(base);
                return merge;
            } else if(crsMerge == null) {
                base.expandToInclude(base);
                return base;
            } else {
                // both not null, are they equal?
                if(!CRS.equalsIgnoreMetadata(crsBase, crsMerge)) {
                    merge = merge.transform(crsBase, true);
                }
                base.expandToInclude(merge);
                return base;
            }
        }
    }

    /**
     * Returns the count of the features for a particular feature type / table.
     */
    protected int getCount(SimpleFeatureType featureType, Query query, Connection cx)
        throws IOException {
        
        CountVisitor v = new CountVisitor();
        getAggregateValue(v,featureType,query,cx);
        return v.getCount();
    }
    
    /**
     * Results the value of an aggregate function over a query.
     */
    protected Object getAggregateValue(FeatureVisitor visitor, SimpleFeatureType featureType, Query query, Connection cx ) 
        throws IOException {
        
        //get the name of the function
        String function = getAggregateFunctions().get( visitor.getClass() );
        if ( function == null ) {
            //try walking up the hierarchy
            Class clazz = visitor.getClass();
            while( clazz != null && function == null ) {
                clazz = clazz.getSuperclass();
                function = getAggregateFunctions().get( clazz );
            }
            
            if ( function == null ) {
                //not supported
                LOGGER.info( "Unable to find aggregate function matching visitor: " + visitor.getClass());
                return null;
            }
        }
        
        AttributeDescriptor att = null;
        Expression expression = getExpression(visitor);
        if ( expression != null ) {
            att = (AttributeDescriptor) expression.evaluate( featureType );
        }
        
        //result of the function
        try {
            Object result = null;
            List results = new ArrayList();
            Statement st = null;
            ResultSet rs = null;
            
            try {
                if ( dialect instanceof PreparedStatementSQLDialect ) {
                    st = selectAggregateSQLPS(function, att, featureType, query, cx);
                    rs = ((PreparedStatement)st).executeQuery();
                } 
                else {
                    String sql = selectAggregateSQL(function, att, featureType, query);
                    LOGGER.fine( sql );
                    
                    st = cx.createStatement();
                    rs = st.executeQuery( sql );
                }
             
                while(rs.next()) {
                    Object value = rs.getObject(1);
                    result = value;
                    results.add(value);
                }
            } finally {
                closeSafe( rs );
                closeSafe( st );
            }
            
            if ( setResult(visitor, results.size() > 1 ? results : result) ){
                return result;    
            }
            
            return null;
        }
        catch( SQLException e ) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    /**
     * Helper method for getting the expression from a visitor
     * TODO: Remove this method when there is an interface for aggregate visitors.
     * See GEOT-2325 for details.
     */
    Expression getExpression(FeatureVisitor visitor) {
        try {
            Method g = visitor.getClass().getMethod( "getExpression", null );
            if ( g != null ) {
                Object result = g.invoke( visitor, null );
                if ( result instanceof Expression ) {
                    return (Expression) result;
                }
            }
        }
        catch( Exception e ) {
            //ignore for now
        }
        
        return null;
    }
    
    /**
     * Helper method for setting the result of a aggregate functino on a visitor.
     * TODO: Remove this method when there is an interface for aggregate visitors.
     * See GEOT-2325 for details.
     */
    boolean setResult(FeatureVisitor visitor,Object result) {
        try {
            Method s = null;
            try {
                s = visitor.getClass().getMethod("setValue",result.getClass());
            }
            catch( Exception e ) {}
            
            if ( s == null ) {
                for ( Method m : visitor.getClass().getMethods()) {
                    if ( "setValue".equals( m.getName() ) ) {
                        s = m;
                        break;
                    }
                }
            }
            if ( s != null ){
                Class type = s.getParameterTypes()[0];
                if ( !type.isInstance( result ) ) {
                    //convert
                    Object converted = Converters.convert( result, type );
                    if ( converted != null ) {
                        result = converted;
                    }
                    else {
                        //could not set value
                        return false;
                    }
                }
                
                s.invoke( visitor, result );
                return true;
            }
        }
        catch( Exception e ) {
            //ignore for now
        }
        return false;
    }
    
    /**
     * Inserts a new feature into the database for a particular feature type / table.
     */
    protected void insert(SimpleFeature feature, SimpleFeatureType featureType, Connection cx)
        throws IOException {
        insert(Collections.singletonList(feature), featureType, cx);
    }

    /**
     * Inserts a collection of new features into the database for a particular
     * feature type / table.
     */
    protected void insert(Collection features, SimpleFeatureType featureType, Connection cx)
        throws IOException {
        PrimaryKey key = getPrimaryKey(featureType);

        // we do this in a synchronized block because we need to do two queries,
        // first to figure out what the id will be, then the insert statement
        synchronized (this) {
            Statement st = null;

            try {
                if ( !(dialect instanceof PreparedStatementSQLDialect) ) {
                    st = cx.createStatement();    
                }
                
                // figure out if we should determine what the fid is pre or post insert
                boolean postInsert = dialect.lookupGeneratedValuesPostInsert() && isGenerated(key);
                
                for (Iterator f = features.iterator(); f.hasNext();) {
                    SimpleFeature feature = (SimpleFeature) f.next();
                    
                    List<Object> keyValues = null;
                    boolean useExisting = Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
                    if(useExisting) {
                        keyValues = decodeFID(key, feature.getID(), true);
                    } else if (!postInsert) {
                        keyValues = getNextValues( key, cx );
                    }
                    

                    if ( dialect instanceof PreparedStatementSQLDialect ) {
                        PreparedStatement ps = insertSQLPS( featureType, feature, keyValues, cx );
                        try {
                            ((PreparedStatementSQLDialect)dialect).onInsert(ps, cx, featureType);
                            ps.execute();
                        } finally {
                            closeSafe( ps );
                        }
                    } else {
                        String sql = insertSQL(featureType, feature, keyValues, cx);
                        
                        //TODO: execute in batch to improve performance?
                        ((BasicSQLDialect)dialect).onInsert(st, cx, featureType);
                        
                        LOGGER.log(Level.FINE, "Inserting new feature: {0}", sql);
                        st.execute(sql);
                    }
                    
                    if ( keyValues == null ) {
                        //grab the key values post insert
                        keyValues = getLastValues(key,cx);
                    }
                    
                    //report the feature id as user data since we cant set the fid
                    String fid = featureType.getTypeName() + "." + encodeFID(keyValues);
                    feature.getUserData().put("fid", fid);
                }

                //st.executeBatch();
            } catch (SQLException e) {
                String msg = "Error inserting features";
                throw (IOException) new IOException(msg).initCause(e);
            } finally {
                closeSafe(st);
            }
        }
    }

    /**
     * Updates an existing feature(s) in the database for a particular feature type / table.
     */
    protected void update(SimpleFeatureType featureType, List<AttributeDescriptor> attributes,
        List<Object> values, Filter filter, Connection cx)
        throws IOException, SQLException {
        update(featureType, attributes.toArray(new AttributeDescriptor[attributes.size()]),
            values.toArray(new Object[values.size()]), filter, cx);
    }

    /**
     * Updates an existing feature(s) in the database for a particular feature type / table.
     */
    protected void update(SimpleFeatureType featureType, AttributeDescriptor[] attributes,
        Object[] values, Filter filter, Connection cx) throws IOException, SQLException {
        if ((attributes == null) || (attributes.length == 0)) {
            LOGGER.warning("Update called with no attributes, doing nothing.");

            return;
        }

        if ( dialect instanceof PreparedStatementSQLDialect ) {
            try {
                PreparedStatement ps = updateSQLPS(featureType, attributes, values, filter, cx);
                try {
                    ((PreparedStatementSQLDialect)dialect).onUpdate(ps, cx, featureType);
                    ps.execute();
                }
                finally {
                    closeSafe( ps );
                }
            } 
            catch (SQLException e) {
                throw new RuntimeException( e );
            }
        }
        else {
            String sql = updateSQL(featureType, attributes, values, filter);

            try {
                Statement st = cx.createStatement();

                try {
                    ((BasicSQLDialect)dialect).onUpdate(st, cx, featureType);
                    
                    LOGGER.log(Level.FINE, "Updating feature: {0}", sql);
                    st.execute(sql);
                }
                finally {
                    closeSafe(st);
                }
            } catch (SQLException e) {
                String msg = "Error occured updating features";
                throw (IOException) new IOException(msg).initCause(e);
            }
        }
    }

    /**
     * Deletes an existing feature in the database for a particular feature type / fid.
     */
    protected void delete(SimpleFeatureType featureType, String fid, Connection cx)
        throws IOException {
        Filter filter = filterFactory.id(Collections.singleton(filterFactory.featureId(fid)));
        delete(featureType, filter, cx);
    }

    /**
     * Deletes an existing feature(s) in the database for a particular feature type / table.
     */
    protected void delete(SimpleFeatureType featureType, Filter filter, Connection cx)
        throws IOException {
        
        Statement st = null;
        try {
            try {
                if ( dialect instanceof PreparedStatementSQLDialect ) {
                    st = deleteSQLPS(featureType,filter,cx);
                    PreparedStatement ps = (PreparedStatement) st;
                    
                    ((PreparedStatementSQLDialect) dialect).onDelete(ps, cx, featureType);
                    ps.execute();
                }
                else {
                    String sql = deleteSQL(featureType, filter);

                    st = cx.createStatement();
                    ((BasicSQLDialect) dialect).onDelete(st, cx, featureType);
                    
                    LOGGER.log(Level.FINE, "Removing feature(s): {0}", sql);
                    st.execute(sql);
                }
            }
            finally {
                closeSafe(st);
            }
        } catch (SQLException e) {
            String msg = "Error occured during delete";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }
    
    /**
     * Returns a JDCB Connection to the underlying database for the specified GeoTools
     * {@link Transaction}. This has two main use cases:
     * <ul>
     * <li>Independently accessing the underlying database directly reusing the connection pool
     * contained in the {@link JDBCDataStore}</li>
     * <li>Performing some direct access to the database in the same JDBC transaction as the
     * Geotools code</li>
     * </ul>
     * The connection shall be used in a different way depending on the use case:
     * <ul>
     * <li>
     * If the transaction is {@link Transaction#AUTO_COMMIT} or if the transaction is not shared
     * with this data store and originating {@link FeatureStore} objects it is the duty of the
     * caller to properly close the connection after usage, failure to do so will result in the
     * connection pool loose one available connection permanently</li>
     * <li>
     * If the transaction is on the other side a valid transaction is shared with Geotools the
     * client code should refrain from closing the connection, committing or rolling back, and use
     * the {@link Transaction} facilities to do so instead</li>
     * </ul>
     * 
     * @param t
     *            The GeoTools transaction. Can be {@code null}, in that case a new connection will
     *            be returned (as if {@link Transaction#AUTO_COMMIT} was provided)
     */
    public Connection getConnection(Transaction t) throws IOException {
        // short circuit this state, all auto commit transactions are using the same
        if(t == Transaction.AUTO_COMMIT) {
            Connection cx = createConnection();
            try {
                cx.setAutoCommit(true);
            } catch (SQLException e) {
                throw (IOException) new IOException().initCause(e);
            }
            return cx;
        }
        
        JDBCTransactionState tstate = (JDBCTransactionState) t.getState(this);
        if(tstate != null) {
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

    /**
     * Gets a database connection for the specified feature store.
     */
    protected final Connection getConnection(JDBCState state) throws IOException {
        return getConnection(state.getTransaction());
    }

    /**
     * Creates a new connection.
     * <p>
     * Callers of this method should close the connection when done with it.
     * </p>.
     *
     */
    protected final Connection createConnection() {
        try {
            LOGGER.fine( "CREATE CONNECTION");
            Connection cx = getDataSource().getConnection();
            // isolation level is not set in the datastore, see 
            // http://jira.codehaus.org/browse/GEOT-2021 

            //call dialect callback to initialize the connection
            dialect.initializeConnection( cx );
            return cx;
        } catch (SQLException e) {
            throw new RuntimeException("Unable to obtain connection: " + e.getMessage(), e);
        }
    }
    
    /**
     * Releases an existing connection.
     */
    protected final void releaseConnection( Connection cx, JDBCState state ) {
        //if the state is based off the AUTO_COMMIT transaction, close the 
        // connection, otherwise wait until the transaction itself is closed to 
        // close the connection
        if ( state.getTransaction() == Transaction.AUTO_COMMIT ) {
            closeSafe( cx );
        }
    }

    /**
     * Encodes a feature id from a primary key and result set values. 
     */
    protected String encodeFID( PrimaryKey pkey, ResultSet rs ) throws SQLException, IOException {
        // no pk columns
        if(pkey.getColumns().isEmpty()) {
            return SimpleFeatureBuilder.createDefaultFeatureId();
        } 
        
        // just one, no need to build support structures
        if(pkey.getColumns().size() == 1)
            return rs.getString(1);

        // more than one
        List<Object> keyValues = new ArrayList<Object>();
        for(int i = 0; i < pkey.getColumns().size(); i++) {
            String o = rs.getString(i+1);
            keyValues.add( o );
        }
        return encodeFID( keyValues );
    }
    
    protected String encodeFID( List<Object> keyValues ) {
        StringBuffer fid = new StringBuffer();
        for ( Object o : keyValues ) {
            fid.append(o).append(".");
        }
        fid.setLength(fid.length()-1);
        return fid.toString();
    }
    
    /**
     * Decodes a fid into its components based on a primary key.
     * 
     * @param strict If set to true the value of the fid will be validated against
     *   the type of the key columns. If a conversion can not be made, an exception will be thrown. 
     */
    protected List<Object> decodeFID( PrimaryKey key, String FID, boolean strict) {
        //strip off the feature type name
        if (FID.startsWith(key.getTableName() + ".")) {
            FID = FID.substring(key.getTableName().length() + 1);
        }

        try {
            FID = URLDecoder.decode(FID,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException( e );
        }

        //check for case of multi column primary key and try to backwards map using
        // "." as a seperator of values
        List values = null;
        if ( key.getColumns().size() > 1 ) {
            String[] split = FID.split( "\\." );

            //copy over to avoid array store exception
            values = new ArrayList(split.length);
            for ( int i = 0; i < split.length; i++ ) {
                values.add(split[i]);
            }
        } else {
            //single value case
            values = new ArrayList();
            values.add( FID );
        }
        if ( values.size() != key.getColumns().size() ) {
            throw new IllegalArgumentException( "Illegal fid: " + FID + ". Expected "
                + key.getColumns().size() + " values but got " + values.size() );
        }

        //convert to the type of the key
        //JD: usually this would be done by the dialect directly when the value
        // actually gets set but the FIDMapper interface does not report types
        for ( int i = 0; i < values.size(); i++ ) {
            Object value = values.get( i );
            if ( value != null ) {
                Class type = key.getColumns().get( i ).getType();
                Object converted = Converters.convert( value, type );
                if ( converted != null ) {
                    values.set(i, converted);
                }
                if (strict && !type.isInstance( values.get( i ) ) ) {
                    throw new IllegalArgumentException( "Value " + values.get( i ) + " illegal for type " + type.getName() );
                }
            }
        }

        return values;
    }
    
    /**
     * Determines if a primary key is made up entirely of column which are generated
     * via an auto-generating column or a sequence.
     */
    protected boolean isGenerated( PrimaryKey pkey ) {
        for ( PrimaryKeyColumn col : pkey.getColumns() ) {
            if ( !(col instanceof AutoGeneratedPrimaryKeyColumn ) ) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets the next value of a primary key.
     */
    protected List<Object> getNextValues( PrimaryKey pkey, Connection cx ) throws SQLException, IOException {
        ArrayList<Object> next = new ArrayList<Object>();
        for( PrimaryKeyColumn col : pkey.getColumns() ) {
            next.add( getNextValue( col, pkey, cx ) );
        }
        return next;
    }
    
    /**
     * Gets the next value for the column of a primary key.
     */
    protected Object getNextValue( PrimaryKeyColumn col, PrimaryKey pkey, Connection cx ) throws SQLException, IOException {
        Object next = null;
        
        if ( col instanceof AutoGeneratedPrimaryKeyColumn ) {
            next = dialect.getNextAutoGeneratedValue(databaseSchema, pkey.getTableName(), col.getName(), cx );
        }
        else if ( col instanceof SequencedPrimaryKeyColumn ) {
            String sequenceName = ((SequencedPrimaryKeyColumn)col).getSequenceName();
            next = dialect.getNextSequenceValue(databaseSchema, sequenceName, cx );
        }
        else {
            //try to calculate
            Class t =  col.getType();
          
          //is the column numeric?
          if ( Number.class.isAssignableFrom( t ) ) {
              //is the column integral? 
              if ( t == Short.class || t == Integer.class || t == Long.class 
                  || BigInteger.class.isAssignableFrom( t ) || BigDecimal.class.isAssignableFrom(t) ) {
                  
                  StringBuffer sql = new StringBuffer();
                  sql.append( "SELECT MAX(");
                  dialect.encodeColumnName( col.getName() , sql );
                  sql.append( ") + 1 FROM ");
                  encodeTableName(pkey.getTableName(), sql, null);
                  
                  LOGGER.log(Level.FINE, "Getting next FID: {0}", sql);
                  
                  Statement st = cx.createStatement();
                  try {
                      ResultSet rs = st.executeQuery( sql.toString() );
                      try {
                          rs.next();
                          next = rs.getObject( 1 );
                          
                          if ( next == null ) {
                              //this probably means there was no data in the table, set to 1
                              //TODO: probably better to do a count to check... but if this 
                              // value already exists the db will throw an error when it tries
                              // to insert
                              next = new Integer(1);
                          }
                      }
                      finally {
                          closeSafe( rs );
                      }
                  }
                  finally {
                      closeSafe( st );
                  }
              }
          }
          else if ( CharSequence.class.isAssignableFrom( t ) ) {
              //generate a random string
              next = SimpleFeatureBuilder.createDefaultFeatureId();
          }
          
          if ( next == null ) {
              throw new IOException( "Cannot generate key value for column of type: " + t.getName() );    
          }
        }
        
        return next;
    }

    /**
     * Gets the last value of a generated primary key.
     */
    protected List<Object> getLastValues( PrimaryKey pkey, Connection cx ) throws SQLException, IOException {
        ArrayList<Object> last = new ArrayList<Object>();
        for( PrimaryKeyColumn col : pkey.getColumns() ) {
            last.add( getLastValue( col, pkey, cx ) );
        }
        return last;
    }
    
    /**
     * Gets the last value of a generated primary key column.
     */
    protected Object getLastValue( PrimaryKeyColumn col, PrimaryKey pkey, Connection cx ) throws SQLException, IOException {
        Object last = null;
        
        if ( col instanceof AutoGeneratedPrimaryKeyColumn ) {
            last = dialect.getLastAutoGeneratedValue(databaseSchema, pkey.getTableName(), col.getName(), cx );
        }
        else {
            throw new IllegalArgumentException("Column " + col.getName() + " is not generated." );
        }
        
        return last;
    }
    
    //
    // SQL generation
    //
    /**
     * Generates a 'CREATE TABLE' sql statement.
     */
    protected String createTableSQL(SimpleFeatureType featureType, Connection cx)
        throws Exception {
        //figure out the names and types of the columns
        String[] columnNames = new String[featureType.getAttributeCount()];
        String[] sqlTypeNames = null;
        Class[] classes = new Class[featureType.getAttributeCount()];

        //figure out which columns can not be null
        boolean[] nillable = new boolean[featureType.getAttributeCount()];

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            AttributeDescriptor attributeType = featureType.getDescriptor(i);

            //column name
            columnNames[i] = attributeType.getLocalName();

            //column type 
            classes[i] = attributeType.getType().getBinding();
            
            //can be null?
            nillable[i] = attributeType.getMinOccurs() <= 0 || attributeType.isNillable();
        }

        sqlTypeNames = getSQLTypeNames(classes, cx);
        for ( int i = 0; i < sqlTypeNames.length; i++ ) {
            if ( sqlTypeNames[i] == null ) {
                String msg = "Unable to map " + columnNames[i] + "( " + classes[i].getName() + ")";
                throw new RuntimeException( msg );
            }
        }
        
        return createTableSQL(featureType.getTypeName(), columnNames, sqlTypeNames, nillable, "fid", featureType);
    }

    /**
     * Ensures that that the specified transaction has access to features specified by a filter.
     * <p>
     * If any features matching the filter are locked, and the transaction does not have authorization
     * with respect to the lock, an exception is thrown.
     * </p>
     * @param featureType The feature type / table.
     * @param filter The filters.
     * @param tx The transaction.
     * @param cx The database connection.
     */
    protected void ensureAuthorization(SimpleFeatureType featureType, Filter filter, Transaction tx, Connection cx) 
        throws IOException, SQLException {
        
        Query query = new DefaultQuery(featureType.getTypeName(), filter, Query.NO_NAMES);

        Statement st = null;
        try {
            ResultSet rs = null;
            if ( getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                st = selectSQLPS(featureType, query, cx);
                
                PreparedStatement ps = (PreparedStatement) st;
                ((PreparedStatementSQLDialect)getSQLDialect()).onSelect(ps, cx, featureType);
                rs = ps.executeQuery();
            }
            else {
                String sql = selectSQL(featureType, query);
                
                st = cx.createStatement();
                ((BasicSQLDialect)getSQLDialect()).onSelect(st, cx, featureType);
                
                LOGGER.fine( sql );
                rs = st.executeQuery( sql );
            }
            
            try {
                PrimaryKey key = getPrimaryKey( featureType );
                InProcessLockingManager lm = (InProcessLockingManager) getLockingManager();
                while( rs.next() ) {
                    String fid = featureType.getTypeName() + "." + encodeFID( key, rs );
                    lm.assertAccess(featureType.getTypeName(), fid, tx );
                }
            }
            finally {
                closeSafe( rs );
            }
        }
        finally {
            closeSafe( st );
        }
    }
    
    /**
     * Helper method for creating geometry association table if it does not
     * exist.
     */
    protected void ensureAssociationTablesExist(Connection cx)
        throws IOException, SQLException {
        // look for feature relationship table
        ResultSet tables = cx.getMetaData()
                             .getTables(null, databaseSchema, FEATURE_RELATIONSHIP_TABLE, null);

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
        tables = cx.getMetaData().getTables(null, databaseSchema, FEATURE_ASSOCIATION_TABLE, null);

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
        tables = cx.getMetaData().getTables(null, databaseSchema, GEOMETRY_TABLE, null);

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
        tables = cx.getMetaData().getTables(null, databaseSchema, MULTI_GEOMETRY_TABLE, null);

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
        tables = cx.getMetaData().getTables(null, databaseSchema, GEOMETRY_ASSOCIATION_TABLE, null);

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
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     */
    protected String createRelationshipTableSQL(Connection cx)
        throws SQLException {
        String[] sqlTypeNames = getSQLTypeNames(new Class[] { String.class, String.class }, cx);
        String[] columnNames = new String[] { "table", "col" };

        return createTableSQL(FEATURE_RELATIONSHIP_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the association table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     */
    protected String createAssociationTableSQL(Connection cx)
        throws SQLException {
        String[] sqlTypeNames = getSQLTypeNames(new Class[] {
                    String.class, String.class, String.class, String.class
                }, cx);
        String[] columnNames = new String[] { "fid", "rtable", "rcol", "rfid" };

        return createTableSQL(FEATURE_ASSOCIATION_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the geometry table.
     *
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     */
    protected String createGeometryTableSQL(Connection cx)
        throws SQLException {
        String[] sqlTypeNames = getSQLTypeNames(new Class[] {
                    String.class, String.class, String.class, String.class, Geometry.class
                }, cx);
        String[] columnNames = new String[] { "id", "name", "description", "type", "geometry" };

        return createTableSQL(GEOMETRY_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the multi_geometry table.
     *  <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     */
    protected String createMultiGeometryTableSQL(Connection cx)
        throws SQLException {
        String[] sqlTypeNames = getSQLTypeNames(new Class[] { String.class, String.class, Boolean.class }, cx);
        String[] columnNames = new String[] { "id", "mgid", "ref" };

        return createTableSQL(MULTI_GEOMETRY_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for the relationship table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param table The table of the association
     * @param column The column of the association
     */
    protected String selectRelationshipSQL(String table, String column) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("table", sql);
        sql.append(",");
        dialect.encodeColumnName("col", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_RELATIONSHIP_TABLE, sql, null);

        if (table != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("table", sql);
            sql.append(" = ");
            dialect.encodeValue(table, String.class, sql);
        }

        if (column != null) {
            if (table == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName("col", sql);
            sql.append(" = ");
            dialect.encodeValue(column, String.class, sql);
        }

        return sql.toString();
    }
    
    /**
     * Creates the prepared statement for a query against the relationship table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param table The table of the association
     * @param column The column of the association
     */
    protected PreparedStatement selectRelationshipSQLPS(String table, String column, Connection cx) 
        throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("table", sql);
        sql.append(",");
        dialect.encodeColumnName("col", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_RELATIONSHIP_TABLE, sql, null);

        if (table != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("table", sql);
            sql.append(" = ? ");
        }

        if (column != null) {
            if (table == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName("col", sql);
            sql.append(" = ? ");
        }

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if ( table != null ) {
            ps.setString( 1, table );
        }
        if ( column != null ) {
            ps.setString( table != null ? 2 : 1 , column );
        }
        return ps;
    }

    /**
     * Creates the sql for the association table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param fid The feature id of the association
     */
    protected String selectAssociationSQL(String fid) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("fid", sql);
        sql.append(",");
        dialect.encodeColumnName("rtable", sql);
        sql.append(",");
        dialect.encodeColumnName("rcol", sql);
        sql.append(", ");
        dialect.encodeColumnName("rfid", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_ASSOCIATION_TABLE, sql, null);
        
        if (fid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("fid", sql);
            sql.append(" = ");
            dialect.encodeValue(fid, String.class, sql);
        }

        return sql.toString();
    }

    /**
     * Creates the prepared statement for the association table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param fid The feature id of the association
     */
    protected PreparedStatement selectAssociationSQLPS(String fid, Connection cx ) 
        throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("fid", sql);
        sql.append(",");
        dialect.encodeColumnName("rtable", sql);
        sql.append(",");
        dialect.encodeColumnName("rcol", sql);
        sql.append(", ");
        dialect.encodeColumnName("rfid", sql);

        sql.append(" FROM ");
        encodeTableName(FEATURE_ASSOCIATION_TABLE, sql, null);
        
        if (fid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("fid", sql);
            sql.append(" = ?");
            
        }
        
        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if ( fid != null ) {
            ps.setString( 1, fid );
        }
        
        return ps;
    }
    
    /**
     * Creates the sql for a select from the geometry table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param gid The geometry id to select for, may be <code>null</code>
     *
     */
    protected String selectGeometrySQL(String gid) throws SQLException {
        
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("id", sql);
        sql.append(",");
        dialect.encodeColumnName("name", sql);
        sql.append(",");
        dialect.encodeColumnName("description", sql);
        sql.append(",");
        dialect.encodeColumnName("type", sql);
        sql.append(",");
        dialect.encodeColumnName("geometry", sql);
        sql.append(" FROM ");
        encodeTableName( GEOMETRY_TABLE, sql, null );
        
        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("id", sql);
            sql.append(" = ");
            dialect.encodeValue(gid, String.class, sql);
        }

        return sql.toString();
    }

    /**
     * Creates the prepared for a select from the geometry table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param gid The geometry id to select for, may be <code>null</code>
     *
     */
    protected PreparedStatement selectGeometrySQLPS(String gid,Connection cx) throws SQLException{
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("id", sql);
        sql.append(",");
        dialect.encodeColumnName("name", sql);
        sql.append(",");
        dialect.encodeColumnName("description", sql);
        sql.append(",");
        dialect.encodeColumnName("type", sql);
        sql.append(",");
        dialect.encodeColumnName("geometry", sql);
        sql.append(" FROM ");
        encodeTableName( GEOMETRY_TABLE, sql, null );
        
        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("id", sql);
            sql.append(" = ?");
        }

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if ( gid != null ) {
            ps.setString( 1, gid );
        }
        
        return ps;
    }
    
    /**
     * Creates the sql for a select from the multi geometry table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param gid The geometry id to select for, may be <code>null</code>.
     */
    protected String selectMultiGeometrySQL(String gid) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("id", sql);
        sql.append(",");
        dialect.encodeColumnName("mgid", sql);
        sql.append(",");
        dialect.encodeColumnName("ref", sql);

        sql.append(" FROM ");
        encodeTableName(MULTI_GEOMETRY_TABLE, sql, null);

        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("id", sql);
            sql.append(" = ");
            dialect.encodeValue(gid, String.class, sql);
        }

        return sql.toString();
    }
    
    /**
     * Creates the prepared statement for a select from the multi geometry table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     * @param gid The geometry id to select for, may be <code>null</code>.
     */
    protected PreparedStatement selectMultiGeometrySQLPS(String gid, Connection cx)
        throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("id", sql);
        sql.append(",");
        dialect.encodeColumnName("mgid", sql);
        sql.append(",");
        dialect.encodeColumnName("ref", sql);

        sql.append(" FROM ");
        encodeTableName(MULTI_GEOMETRY_TABLE, sql, null);

        if (gid != null) {
            sql.append(" WHERE ");

            dialect.encodeColumnName("id", sql);
            sql.append(" = ?");
        }

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if (gid != null) {
            ps.setString( 1, gid );
        }

        return ps;
    }

    /**
     * Creates the sql for the geometry association table.
     * <p>
     * This method is only called when {@link JDBCDataStore#isAssociations()}
     * is true.
     * </p>
     */
    protected String createGeometryAssociationTableSQL(Connection cx)
        throws SQLException {
        String[] sqlTypeNames = getSQLTypeNames(new Class[] {
                    String.class, String.class, String.class, Boolean.class
                }, cx);
        String[] columnNames = new String[] { "fid", "gname", "gid", "ref" };

        return createTableSQL(GEOMETRY_ASSOCIATION_TABLE, columnNames, sqlTypeNames, null, null, null);
    }

    /**
     * Creates the sql for a select from the geometry association table.
     * <p>
     * </p>
     * @param fid The fid to select for, may be <code>null</code>
     * @param gid The geometry id to select for, may be <code>null</code>
     * @param gname The geometry name to select for, may be <code>null</code>
     */
    protected String selectGeometryAssociationSQL(String fid, String gid, String gname) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("fid", sql);
        sql.append(",");
        dialect.encodeColumnName("gid", sql);
        sql.append(",");
        dialect.encodeColumnName("gname", sql);
        sql.append(",");
        dialect.encodeColumnName("ref", sql);

        sql.append(" FROM ");
        encodeTableName(GEOMETRY_ASSOCIATION_TABLE, sql, null);

        if (fid != null) {
            sql.append(" WHERE ");
            dialect.encodeColumnName("fid", sql);
            sql.append(" = ");
            dialect.encodeValue(fid, String.class, sql);
        }

        if (gid != null) {
            if (fid == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName("gid", sql);
            sql.append(" = ");
            dialect.encodeValue(gid, String.class, sql);
        }

        if (gname != null) {
            if ((fid == null) && (gid == null)) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName("gname", sql);
            sql.append(" = ");
            dialect.encodeValue(gname, String.class, sql);
        }

        return sql.toString();
    }
    /**
     * Creates the prepared statement for a select from the geometry association table.
     * <p>
     * </p>
     * @param fid The fid to select for, may be <code>null</code>
     * @param gid The geometry id to select for, may be <code>null</code>
     * @param gname The geometry name to select for, may be <code>null</code>
     */
    protected PreparedStatement selectGeometryAssociationSQLPS(String fid, String gid, String gname, Connection cx)
        throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        dialect.encodeColumnName("fid", sql);
        sql.append(",");
        dialect.encodeColumnName("gid", sql);
        sql.append(",");
        dialect.encodeColumnName("gname", sql);
        sql.append(",");
        dialect.encodeColumnName("ref", sql);

        sql.append(" FROM ");
        encodeTableName(GEOMETRY_ASSOCIATION_TABLE, sql, null);

        if (fid != null) {
            sql.append(" WHERE ");
            dialect.encodeColumnName("fid", sql);
            sql.append(" = ? ");
        }

        if (gid != null) {
            if (fid == null) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName("gid", sql);
            sql.append(" = ? ");
        }

        if (gname != null) {
            if ((fid == null) && (gid == null)) {
                sql.append(" WHERE ");
            } else {
                sql.append(" AND ");
            }

            dialect.encodeColumnName("gname", sql);
            sql.append(" = ?");
        }

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        if ( fid != null ) {
            ps.setString( 1, fid );
        }
        
        if ( gid != null ) {
            ps.setString( fid != null ? 2 : 1, gid );
        }
        
        if ( gname != null ) {
            ps.setString( fid != null ? (gid != null ? 3 : 2 ) : (gid != null ? 2 : 1 ), gname );
        }
        
        return ps;
    }
    
    /**
     * Helper method for building a 'CREATE TABLE' sql statement.
     */
    private String createTableSQL(String tableName, String[] columnNames, String[] sqlTypeNames,
        boolean[] nillable, String pkeyColumn, SimpleFeatureType featureType ) throws SQLException {
        //build the create table sql
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE TABLE ");

        encodeTableName(tableName, sql, null);
        sql.append(" ( ");

        //primary key column
        if (pkeyColumn != null) {
            dialect.encodePrimaryKey(pkeyColumn, sql);
            sql.append(", ");
        }

        //normal attributes
        for (int i = 0; i < columnNames.length; i++) {
            //the column name
            dialect.encodeColumnName(columnNames[i], sql);
            sql.append(" ");

            //some sql dialects require varchars to have an
            // associated size with them
            int length = -1;
            if ( sqlTypeNames[i].toUpperCase().startsWith( "VARCHAR" ) ) {
                if ( featureType != null ) {
                    AttributeDescriptor att = featureType.getDescriptor(columnNames[i]);
                    length = findVarcharColumnLength( att );
                }
            }
            
            //only apply a length if one exists (i.e. to applicable varchars)
            if ( length == -1 ) {
                dialect.encodeColumnType(sqlTypeNames[i], sql);  
            } else {  
                dialect.encodeColumnType(sqlTypeNames[i] + "("+ length + ")", sql);
            }

            //nullable
            if ( nillable != null && !nillable[i] ) {
                sql.append( " NOT NULL ");
            }

            //delegate to dialect to encode column postamble
            if (featureType != null) {
                AttributeDescriptor att = featureType.getDescriptor(columnNames[i]);
                dialect.encodePostColumnCreateTable(att, sql);
            }
            
            //sql.append(sqlTypeNames[i]);
            if (i < (sqlTypeNames.length - 1)) {
                sql.append(", ");
            }
        }

        sql.append(" ) ");

        //encode anything post create table
        dialect.encodePostCreateTable(tableName, sql);

        return sql.toString();
    }

    /**
     * Searches the attribute descriptor restrictions in an attempt to determine
     * the length of the specified varchar column.
     */
    private Integer findVarcharColumnLength(AttributeDescriptor att) {
        for ( Filter r : att.getType().getRestrictions() ) {
            if( r instanceof PropertyIsLessThanOrEqualTo ) {
                PropertyIsLessThanOrEqualTo c = (PropertyIsLessThanOrEqualTo) r;
                if ( c.getExpression1() instanceof Function &&
                    ((Function) c.getExpression1()).getName().toLowerCase().endsWith( "length") ) {
                    if ( c.getExpression2() instanceof Literal ) {
                        Integer length = c.getExpression2().evaluate(null,Integer.class);
                        if ( length != null ) {
                            return length;
                        }
                    }
                }
            }
        }

        return dialect.getDefaultVarcharSize();
    }

    /**
     * Helper method for determining what the sql type names are for a set of
     * classes.
     * <p>
     * This method uses a combination of dialect mappings and database metadata
     * to determine which sql types map to the specified classes.
     * </p>
     */
    private String[] getSQLTypeNames(Class[] classes, Connection cx)
        throws SQLException {
        //figure out what the sql types are corresponding to the feature type
        // attributes
        int[] sqlTypes = new int[classes.length];
        String[] sqlTypeNames = new String[sqlTypes.length];

        for (int i = 0; i < classes.length; i++) {
            Class clazz = classes[i];
            Integer sqlType = getMapping(clazz);

            if (sqlType == null) {
                LOGGER.warning("No sql type mapping for: " + clazz);
                sqlType = Types.OTHER;
            }

            sqlTypes[i] = sqlType;

            //if this a geometric type, get the name from teh dialect
            //if ( attributeType instanceof GeometryDescriptor ) {
            if (Geometry.class.isAssignableFrom(clazz)) {
                String sqlTypeName = dialect.getGeometryTypeName(sqlType);

                if (sqlTypeName != null) {
                    sqlTypeNames[i] = sqlTypeName;
                }
            }
            
            //check the overrides
            String sqlTypeName = getSqlTypeToSqlTypeNameOverrides().get( sqlType );
            if ( sqlTypeName != null ) {
                sqlTypeNames[i] = sqlTypeName;
            }

        }

        //figure out the type names that correspond to the sql types from 
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
                    //check if we already have the type name from the dialect
                    if (sqlTypeNames[i] != null) {
                        continue;
                    }

                    if (sqlType == sqlTypes[i]) {
                        sqlTypeNames[i] = sqlTypeName;
                    }
                }
            }
        } finally {
            closeSafe(types);
        }
        
        // apply the overrides specified by the dialect
        Map<Integer, String> overrides = getSqlTypeToSqlTypeNameOverrides();
        for (int i = 0; i < sqlTypes.length; i++) {
            String override = overrides.get(sqlTypes[i]);
            if(override != null)
                sqlTypeNames[i] = override;
        }

        return sqlTypeNames;
    }

    /**
     * Generates a 'SELECT p1, p2, ... FROM ... WHERE ...' statement.
     * 
     * @param featureType
     *            the feature type that the query must return (may contain less
     *            attributes than the native one)
     * @param attributes
     *            the properties queried, or {@link Query#ALL_NAMES} to gather
     *            all of them
     * @param query
     *            the query to be run. The type name and property will be ignored, as they are
     *            supposed to have been already embedded into the provided feature type
     * @param sort
     *            sort conditions
     */
    protected String selectSQL(SimpleFeatureType featureType, Query query) throws IOException, SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        //column names

        //primary key
        PrimaryKey key = null;
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> pkColumnNames = getColumnNames(key);
        
        // we need to add the primary key columns only if they are not already exposed
        for ( PrimaryKeyColumn col : key.getColumns() ) {
            dialect.encodeColumnName(col.getName(), sql);
            sql.append(",");
        }
        
        //other columns
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            String columnName = att.getLocalName();
            // skip the eventually exposed pk column values
            if(pkColumnNames.contains(columnName))
                continue;
            if (att instanceof GeometryDescriptor) {
                //encode as geometry
            	encodeGeometryColumn((GeometryDescriptor) att, sql, query.getHints());

                //alias it to be the name of the original geometry
                dialect.encodeColumnAlias(columnName, sql);
            } else {
                dialect.encodeColumnName(columnName, sql);
            }

            sql.append(",");
        }

        sql.setLength(sql.length() - 1);
        dialect.encodePostSelect(featureType, sql);
        
        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, query.getHints());

        //filtering
        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                // grab the full feature type, as we might be encoding a filter
                // that uses attributes that aren't returned in the results
                SimpleFeatureType fullSchema = getSchema(featureType.getTypeName());
                FilterToSQL toSQL = createFilterToSQL(fullSchema);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        //sorting
        sort(featureType, query.getSortBy(), key, sql);
        
        // finally encode limit/offset, if necessary
        applyLimitOffset(sql, query);

        return sql.toString();
    }

    /**
     * Encodes the sort-by portion of an sql query
     * @param featureType
     * @param sort
     * @param key
     * @param sql
     * @throws IOException
     */
    void sort(SimpleFeatureType featureType, SortBy[] sort,
            PrimaryKey key, StringBuffer sql) throws IOException {
        if ((sort != null) && (sort.length > 0)) {
            sql.append(" ORDER BY ");

            for (int i = 0; i < sort.length; i++) {
                String order;
                if (sort[i].getSortOrder() == SortOrder.DESCENDING) {
                    order = " DESC";
                } else {
                    order = " ASC";
                }
                
                if(SortBy.NATURAL_ORDER.equals(sort[i])|| SortBy.REVERSE_ORDER.equals(sort[i])) {
                    if(key instanceof NullPrimaryKey)
                        throw new IOException("Cannot do natural order without a primary key");
                    
                    for ( PrimaryKeyColumn col : key.getColumns() ) {
                        dialect.encodeColumnName(col.getName(), sql);
                        sql.append(order);
                        sql.append(",");
                    }
                } else {
                    dialect.encodeColumnName(getPropertyName(featureType, sort[i].getPropertyName()),
                            sql);
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
     * @param featureType
     *            the feature type that the query must return (may contain less
     *            attributes than the native one)
     * @param attributes
     *            the properties queried, or {@link Query#ALL_NAMES} to gather
     *            all of them
     * @param query
     *            the query to be run. The type name and property will be ignored, as they are
     *            supposed to have been already embedded into the provided feature type
     * @param cx
     *            The database connection to be used to create the prepared
     *            statement
     */
    protected PreparedStatement selectSQLPS( SimpleFeatureType featureType, Query query, Connection cx )
        throws SQLException, IOException {
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");

        // primary key
        PrimaryKey key = null;

        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> pkColumnNames = getColumnNames(key);

        for ( PrimaryKeyColumn col : key.getColumns() ) {
            dialect.encodeColumnName(col.getName(), sql);
            sql.append(",");
        }
        
        //other columns
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            // skip the eventually exposed pk column values
            String columnName = att.getLocalName();
            if(pkColumnNames.contains(columnName))
                continue;
            
            if (att instanceof GeometryDescriptor) {
                //encode as geometry
            	encodeGeometryColumn((GeometryDescriptor) att, sql, query.getHints());

                //alias it to be the name of the original geometry
                dialect.encodeColumnAlias(columnName, sql);
            } else {
                dialect.encodeColumnName(columnName, sql);
            }

            sql.append(",");
        }

        sql.setLength(sql.length() - 1);
        dialect.encodePostSelect(featureType, sql);
        
        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, query.getHints());

        //filtering
        PreparedFilterToSQL toSQL = null;
        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                // grab the full feature type, as we might be encoding a filter
                // that uses attributes that aren't returned in the results
                SimpleFeatureType fullSchema = getSchema(featureType.getTypeName());
                toSQL = createPreparedFilterToSQL(fullSchema);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        //sorting
        sort(featureType, query.getSortBy(), key, sql);
        
        // finally encode limit/offset, if necessary
        applyLimitOffset(sql, query);

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(fetchSize);
        
        if ( toSQL != null ) {
            setPreparedFilterValues( ps, toSQL, 0, cx );
        } 
        
        return ps;
    }
    
    /**
     * Helper method for setting the values of the WHERE class of a prepared statement.
     * 
     */
    protected void setPreparedFilterValues( PreparedStatement ps, PreparedFilterToSQL toSQL, int offset, Connection cx ) 
        throws SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
       
        for ( int i = 0; i < toSQL.getLiteralValues().size(); i++) {
            Object value = toSQL.getLiteralValues().get(i);
            Class binding = toSQL.getLiteralTypes().get(i);
            Integer srid = toSQL.getSRIDs().get(i);
            if(srid == null)
                srid = -1;
            
            if(binding != null && Geometry.class.isAssignableFrom(binding))
                dialect.setGeometryValue((Geometry) value, srid, binding, ps, offset + i+1);
            else
                dialect.setValue( value, binding, ps, offset + i+1, cx );
            if ( LOGGER.isLoggable( Level.FINE ) ) {
                LOGGER.fine( (i+1) + " = " + value );
            }
        }
    }
    
    /**
     * Helper method for executing a property name against a feature type.
     * <p>
     * This method will fall back on {@link PropertyName#getPropertyName()} if
     * it does not evaulate against the feature type.
     * </p>
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
     * @param query Specifies which features are to be used for the bounds computation
     *              (and in particular uses filter, start index and max features)
     */
    protected String selectBoundsSQL(SimpleFeatureType featureType, Query query) throws SQLException {
        StringBuffer sql = new StringBuffer();

        boolean offsetLimit = checkLimitOffset(query);
        if(offsetLimit) {
            // envelopes are aggregates, just like count, so we must first isolate
            // the rows against which the aggregate will work in a subquery
            sql.append(" SELECT *");
        } else {
            sql.append("SELECT ");
            buildEnvelopeAggregates(featureType, sql);
        }

        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, query.getHints());

        Filter filter = query.getFilter();
        if (filter != null  && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        // finally encode limit/offset, if necessary
        if(offsetLimit) {
            applyLimitOffset(sql, query);
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

        return sql.toString();
    }
    
    /**
     * Generates a 'SELECT' prepared statement which selects bounds.
     * 
     * @param featureType The feature type / table.
     * @param query Specifies which features are to be used for the bounds computation
     *              (and in particular uses filter, start index and max features)
     * @param cx A database connection.
     */
    protected PreparedStatement selectBoundsSQLPS(SimpleFeatureType featureType, Query query, Connection cx)
        throws SQLException {
        
        StringBuffer sql = new StringBuffer();

        boolean offsetLimit = checkLimitOffset(query);
        if(offsetLimit) {
            // envelopes are aggregates, just like count, so we must first isolate
            // the rows against which the aggregate will work in a subquery
            sql.append(" SELECT *");
        } else {
            sql.append("SELECT ");
            buildEnvelopeAggregates(featureType, sql);
        }

        sql.append(" FROM ");
        encodeTableName(featureType.getTypeName(), sql, query.getHints());

        // encode the filter
        PreparedFilterToSQL toSQL = null;
        Filter filter = query.getFilter();
        if (filter != null  && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                toSQL = createPreparedFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        // finally encode limit/offset, if necessary
        if(offsetLimit) {
            applyLimitOffset(sql, query);
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
        

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        
        if ( toSQL != null ) {
            setPreparedFilterValues(ps, toSQL, 0, cx);
        }
        
        return ps;
    }

    /**
     * Builds a list of the aggregate function calls necesary to compute each geometry
     * column bounds
     * @param featureType
     * @param sql
     */
    void buildEnvelopeAggregates(SimpleFeatureType featureType, StringBuffer sql) {
        //walk through all geometry attributes and build the query
        for (Iterator a = featureType.getAttributeDescriptors().iterator(); a.hasNext();) {
            AttributeDescriptor attribute = (AttributeDescriptor) a.next();
            if (attribute instanceof GeometryDescriptor) {
                String geometryColumn = featureType.getGeometryDescriptor().getLocalName();
                dialect.encodeGeometryEnvelope(featureType.getTypeName(), geometryColumn, sql);
                sql.append(",");
            }
        }
        sql.setLength(sql.length() - 1);
    }
    
    /**
     * Generates a 'SELECT count(*) FROM' sql statement. In case limit/offset is 
     * used, we'll need to apply them on a <code>select *<code>
     * as limit/offset usually alters the number of returned rows 
     * (and a count returns just one), and then count on the result of that first select
     */
    protected String selectCountSQL(SimpleFeatureType featureType, Query query) throws SQLException {
        //JD: this method should not be called anymore
        return selectAggregateSQL("count",null,featureType,query);
    }

    /**
     * Generates a 'SELECT count(*) FROM' prepared statement.
     */
    protected PreparedStatement selectCountSQLPS(SimpleFeatureType featureType, Query query, Connection cx ) 
        throws SQLException {
        //JD: this method shold not be called anymore
        return selectAggregateSQLPS("count",null,featureType,query,cx);
    }
    
    /**
     * Generates a 'SELECT <function>() FROM' statement.
     */
    protected String selectAggregateSQL(String function, AttributeDescriptor att, 
            SimpleFeatureType featureType, Query query) throws SQLException {
        StringBuffer sql = new StringBuffer();

        boolean limitOffset = checkLimitOffset(query);
        if(limitOffset) {
            sql.append("SELECT * FROM ");
        } else {
            sql.append("SELECT ");
            encodeFunction(function,att,query,sql);
            sql.append( " FROM ");
        }
        encodeTableName(featureType.getTypeName(), sql, query.getHints());

        Filter filter = query.getFilter();
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        if(limitOffset) {
            applyLimitOffset(sql, query);
            
            StringBuffer sql2 = new StringBuffer("SELECT ");
            encodeFunction(function,att,query,sql2);
            sql2.append(" AS gt_result_");
            sql2.append(" FROM (");
            sql.insert(0,sql2.toString());
            sql.append(") gt_limited_");
        }

        return sql.toString();
    }
    
    /**
     * Generates a 'SELECT <function>() FROM' prepared statement.
     */
    protected PreparedStatement selectAggregateSQLPS(String function, AttributeDescriptor att, SimpleFeatureType featureType, Query query, Connection cx)
        throws SQLException {
        
        StringBuffer sql = new StringBuffer();

        boolean limitOffset = checkLimitOffset(query);
        if(limitOffset) {
            sql.append("SELECT * FROM ");
        } else {
            sql.append("SELECT ");
            encodeFunction(function,att,query,sql);
            sql.append( " FROM ");
        }
        encodeTableName(featureType.getTypeName(), sql, query.getHints());

        Filter filter = query.getFilter();
        PreparedFilterToSQL toSQL = null;
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                toSQL = createPreparedFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }
        
        if(limitOffset) {
            applyLimitOffset(sql, query);
            
            StringBuffer sql2 = new StringBuffer("SELECT ");
            encodeFunction(function,att,query,sql2);
            sql2.append(" AS gt_result_");
            sql2.append(" FROM (");
            sql.insert(0,sql2.toString());
            sql.append(") gt_limited_");
        }
        
        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ps.setFetchSize(fetchSize);
        
        if ( toSQL != null ) {
            setPreparedFilterValues(ps, toSQL, 0, cx);
        } 
        
        return ps;
    }
    
    protected void encodeFunction( String function, AttributeDescriptor att, Query query, StringBuffer sql ) {
        sql.append(function).append("(");
        if ( att == null ) {
            sql.append( "*" );
        }
        else {
            if ( att instanceof GeometryDescriptor ) {
                encodeGeometryColumn((GeometryDescriptor)att, sql,query.getHints());
            }
            else {
                dialect.encodeColumnName( att.getLocalName(), sql);
            }
        }
        
        sql.append(")");
    }
    
    /**
     * Generates a 'DELETE FROM' sql statement.
     */
    protected String deleteSQL(SimpleFeatureType featureType, Filter filter) throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM ");
        encodeTableName(featureType.getTypeName(), sql, null);

        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        return sql.toString();
    }

    /**
     * Generates a 'DELETE FROM' prepared statement.
     */
    protected PreparedStatement deleteSQLPS(SimpleFeatureType featureType, Filter filter, Connection cx ) 
        throws SQLException {
        StringBuffer sql = new StringBuffer();

        sql.append("DELETE FROM ");
        encodeTableName(featureType.getTypeName(), sql, null);

        PreparedFilterToSQL toSQL = null;
        if (filter != null && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                toSQL = createPreparedFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        LOGGER.fine( sql.toString() );
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        
        if ( toSQL != null ) {
            setPreparedFilterValues(ps, toSQL, 0, cx);
        }
        
        return ps;
    }

    /**
     * Generates a 'INSERT INFO' sql statement.
     * @throws IOException 
     */
    protected String insertSQL(SimpleFeatureType featureType, SimpleFeature feature, 
            List keyValues, Connection cx) throws SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();
        
        // grab the primary key and collect the pk column names 
        PrimaryKey key = null; 
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
        Set<String> pkColumnNames = getColumnNames(key);
       
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        encodeTableName(featureType.getTypeName(), sql, null);

        //column names
        sql.append(" ( ");

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            String colName = featureType.getDescriptor(i).getLocalName();
            // skip the pk columns in case we have exposed them
            if(pkColumnNames.contains(colName)) {
                continue;
            }
            dialect.encodeColumnName(colName, sql);
            sql.append(",");
        }

        //primary key values
        boolean useExisting = Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
        for (PrimaryKeyColumn col : key.getColumns() ) {
            //only include if its non auto generating
            if ( !(col instanceof AutoGeneratedPrimaryKeyColumn )  || useExisting) {
                dialect.encodeColumnName(col.getName(), sql);
                sql.append( ",");
            }
        } 
        sql.setLength(sql.length() - 1);

        //values
        sql.append(" ) VALUES ( ");

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            AttributeDescriptor att = featureType.getDescriptor(i);
            String colName = att.getLocalName();
            // skip the pk columns in case we have exposed them, we grab the
            // value from the pk itself
            if(pkColumnNames.contains(colName)) {
                continue;
            }
            
            Class binding = att.getType().getBinding();

            Object value = feature.getAttribute(colName);

            if (value == null) {
                if (!att.isNillable()) {
                    //TODO: throw an exception    
                }

                sql.append("null");
            } else {
                if (Geometry.class.isAssignableFrom(binding)) {
                    try {
                        Geometry g = (Geometry) value;
                        int srid = getGeometrySRID(g, att);
                        dialect.encodeGeometryValue(g, srid, sql);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    dialect.encodeValue(value, binding, sql);
                }
            }

            sql.append(",");
        }
        // handle the primary key
        for ( int i = 0; i < key.getColumns().size(); i++ ) {
            PrimaryKeyColumn col = key.getColumns().get( i );
            
            //only include if its non auto generating
            if (!(col instanceof AutoGeneratedPrimaryKeyColumn ) || useExisting) {
                try {
                    //Object value = getNextValue(col, key, cx);
                    Object value = keyValues.get( i );
                    dialect.encodeValue( value, col.getType(), sql );
                    sql.append( "," );
                } 
                catch( Exception e ) {
                    throw new RuntimeException( e );
                }
            }
        }
        sql.setLength(sql.length() - 1);

        sql.append(")");

        return sql.toString();
    }

    /**
     * Returns the set of the primary key column names. The set is guaranteed to have the same
     * iteration order as the primary key.
     * @param key
     */
    protected LinkedHashSet<String> getColumnNames(PrimaryKey key) {
        LinkedHashSet<String> pkColumnNames = new LinkedHashSet<String>();
        for (PrimaryKeyColumn pkcol : key.getColumns()) {
            pkColumnNames.add(pkcol.getName());
        }
        return pkColumnNames;
    }

    /**
     * Generates a 'INSERT INFO' prepared statement.
     */
    protected PreparedStatement insertSQLPS(SimpleFeatureType featureType, SimpleFeature feature, List keyValues, Connection cx) 
        throws IOException, SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
        
        // grab the primary key and collect the pk column names 
        PrimaryKey key = null; 
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
        Set<String> pkColumnNames = getColumnNames(key);
        
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ");
        encodeTableName(featureType.getTypeName(), sql, null);

        // column names
        sql.append(" ( ");

        for (int i = 0; i < featureType.getAttributeCount(); i++) {
            String colName = featureType.getDescriptor(i).getLocalName();
            // skip the pk columns in case we have exposed them
            if(pkColumnNames.contains(colName)) {
                continue;
            }

            dialect.encodeColumnName(colName, sql);
            sql.append(",");
        }

        // primary key values
        boolean useExisting = Boolean.TRUE.equals(feature.getUserData().get(Hints.USE_PROVIDED_FID));
        for (PrimaryKeyColumn col : key.getColumns() ) {
            //only include if its non auto generating
            if ( !(col instanceof AutoGeneratedPrimaryKeyColumn ) || useExisting ) {
                dialect.encodeColumnName(col.getName(), sql);
                sql.append( ",");
            }
        }
        sql.setLength(sql.length() - 1);

        // values
        sql.append(" ) VALUES ( ");
        for (AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            String colName = att.getLocalName();
            // skip the pk columns in case we have exposed them, we grab the
            // value from the pk itself
            if(pkColumnNames.contains(colName)) {
                continue;
            }
            
            // geometries might need special treatment, delegate to the dialect
            if(att instanceof GeometryDescriptor) {
                Geometry geometry = (Geometry) feature.getAttribute(att.getName());
                dialect.prepareGeometryValue(geometry, getDescriptorSRID(att), att.getType().getBinding(),  sql );
            } else {
                sql.append("?");
            }
            sql.append(",");
        }
        for (PrimaryKeyColumn col : key.getColumns() ) {
            //only include if its non auto generating
            if ( !(col instanceof AutoGeneratedPrimaryKeyColumn ) || useExisting) {
                sql.append("?").append( ",");
            }
        }
        
        sql.setLength(sql.length()-1);
        sql.append(")");
        LOGGER.log(Level.FINE, "Inserting new feature with ps: {0}", sql);
        
        //create the prepared statement
        PreparedStatement ps = cx.prepareStatement(sql.toString());
        
        //set the attribute values
        int i = 1;
        for(AttributeDescriptor att : featureType.getAttributeDescriptors()) {
            String colName = att.getLocalName();
            // skip the pk columns in case we have exposed them, we grab the
            // value from the pk itself
            if(pkColumnNames.contains(colName)) {
                continue;
            }
            
            Class binding = att.getType().getBinding();

            Object value = feature.getAttribute(colName);
            if (value == null) {
                if (!att.isNillable()) {
                    //TODO: throw an exception    
                }
            }
            
            if (Geometry.class.isAssignableFrom(binding)) {
                Geometry g = (Geometry) value;
                int srid = getGeometrySRID(g, att);
                dialect.setGeometryValue( g, srid, binding, ps, i );
            } else {
                dialect.setValue( value, binding, ps, i, cx );
            }
            if ( LOGGER.isLoggable( Level.FINE ) ) {
                LOGGER.fine( (i) + " = " + value );    
            }
            i++;
        }
        
        //set the key values 
        //mind, we are reusing the i index from the previous loop
        for ( int j = 0; j < key.getColumns().size(); j++ ) {
            PrimaryKeyColumn col = key.getColumns().get( j );
            //only include if its non auto generating
            if (!(col instanceof AutoGeneratedPrimaryKeyColumn ) || useExisting) {
                //get the next value for the column
                //Object value = getNextValue( col, key, cx );
                Object value = keyValues.get( j );
                dialect.setValue( value, col.getType(), ps, i, cx);
                i++;
                if ( LOGGER.isLoggable( Level.FINE ) ) {
                    LOGGER.fine( (i) + " = " + value );    
                }
            }
        }
        
        return ps;
    }
    
    /**
     * Looks up the geometry srs by trying a number of heuristics. Returns -1 if all attempts
     * at guessing the srid failed.
     */
    protected int getGeometrySRID(Geometry g, AttributeDescriptor descriptor) throws IOException {
        int srid = getDescriptorSRID(descriptor);
        
        if ( g == null ) {
            return srid;
        }
        
        // check for srid in the jts geometry then
        if (srid <= 0 && g.getSRID() > 0) {
            srid = g.getSRID();
        }
        
        // check if the geometry has anything
        if (srid <= 0) {
            // check for crs object
            CoordinateReferenceSystem crs = (CoordinateReferenceSystem) g
                .getUserData();

            if (crs != null) {
                try {
                    Integer candidate = CRS.lookupEpsgCode(crs, false);
                    if(candidate != null)
                        srid = candidate;
                } catch(Exception e) {
                    // ok, we tried...
                }
            }
        }
        
        return srid;
    }

    /**
     * Extracts the eventual native SRID user property from the descriptor, 
     * returns -1 if not found
     * @param descriptor
     */
    protected int getDescriptorSRID(AttributeDescriptor descriptor) {
        int srid = -1;
        
        // check if we have stored the native srid in the descriptor (we should)
        if(descriptor.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID) != null)
            srid = (Integer) descriptor.getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID);
        
        return srid;
    }
    
    /**
     * Generates an 'UPDATE' sql statement.
     */
    protected String updateSQL(SimpleFeatureType featureType, AttributeDescriptor[] attributes,
        Object[] values, Filter filter) throws IOException, SQLException {
        BasicSQLDialect dialect = (BasicSQLDialect) getSQLDialect();
        
        // grab the primary key and collect the pk column names 
        PrimaryKey key = null; 
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
        Set<String> pkColumnNames = getColumnNames(key);
        
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        encodeTableName(featureType.getTypeName(), sql, null);

        sql.append(" SET ");

        for (int i = 0; i < attributes.length; i++) {
            // skip exposed pk columns, they are read only
            String attName = attributes[i].getLocalName();
            if(pkColumnNames.contains(attName)) {
                continue;
            }
            // build "colName = value" 
            dialect.encodeColumnName(attName, sql);
            sql.append(" = ");
            
            if ( Geometry.class.isAssignableFrom( attributes[i].getType().getBinding() ) ) {
                try {
                    Geometry g = (Geometry) values[i];
                    int srid = getGeometrySRID(g, attributes[i]);
                    dialect.encodeGeometryValue(g, srid, sql);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                dialect.encodeValue(values[i], attributes[i].getType().getBinding(), sql);    
            }
            
            sql.append(",");
        }

        sql.setLength(sql.length() - 1);
        sql.append(" ");

        if (filter != null  && !Filter.INCLUDE.equals(filter)) {
            //encode filter
            try {
                FilterToSQL toSQL = createFilterToSQL(featureType);
                sql.append(" ").append(toSQL.encodeToString(filter));
            } catch (FilterToSQLException e) {
                throw new RuntimeException(e);
            }
        }

        return sql.toString();
    }
    
    /**
     * Generates an 'UPDATE' prepared statement.
     */
    protected PreparedStatement updateSQLPS(SimpleFeatureType featureType, AttributeDescriptor[] attributes,
            Object[] values, Filter filter, Connection cx ) throws IOException, SQLException {
        PreparedStatementSQLDialect dialect = (PreparedStatementSQLDialect) getSQLDialect();
        
        // grab the primary key and collect the pk column names 
        PrimaryKey key = null; 
        try {
            key = getPrimaryKey(featureType);
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
        Set<String> pkColumnNames = getColumnNames(key);
        
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE ");
        encodeTableName(featureType.getTypeName(), sql, null);

        sql.append(" SET ");

        for (int i = 0; i < attributes.length; i++) {
            // skip exposed primary key values, they are read only
            AttributeDescriptor att = attributes[i];
            String attName = att.getLocalName();
            if(pkColumnNames.contains(attName)) {
                continue;
            }
            
            dialect.encodeColumnName(attName, sql);
            sql.append(" = ");
            
            // geometries might need special treatment, delegate to the dialect
            if(attributes[i] instanceof GeometryDescriptor) {
                Geometry geometry = (Geometry) values[i];
                final Class<?> binding = att.getType().getBinding();
                dialect.prepareGeometryValue(geometry, getDescriptorSRID(att), binding,  sql );
            } else {
                sql.append("?");
            }
            sql.append(",");
        }
        sql.setLength(sql.length() - 1);
        sql.append(" ");

        PreparedFilterToSQL toSQL = null;
        if (filter != null  && !Filter.INCLUDE.equals(filter)) {
            //encode filter
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
            if(pkColumnNames.contains(attName)) {
                continue;
            }
            
            Class binding = att.getType().getBinding();
            if (Geometry.class.isAssignableFrom( binding ) ) {
                Geometry g = (Geometry) values[i];
                dialect.setGeometryValue(g, getDescriptorSRID(att), binding, ps, j+1);
            } else {
                dialect.setValue( values[i], binding, ps, j+1, cx);    
            }
            if ( LOGGER.isLoggable( Level.FINE ) ) {
                LOGGER.fine( (j+1) + " = " + values[i] );
            }
            // we do this only if we did not skip the exposed pk
            j++;
        }
        
        if ( toSQL != null ) {
            setPreparedFilterValues(ps, toSQL, i, cx);
            //for ( int j = 0; j < toSQL.getLiteralValues().size(); j++, i++)  {
            //    Object value = toSQL.getLiteralValues().get( j );
            //    Class binding = toSQL.getLiteralTypes().get( j );
            //    
            //    dialect.setValue( value, binding, ps, i+1, cx );
            //    if ( LOGGER.isLoggable( Level.FINE ) ) {
            //        LOGGER.fine( (i+1) + " = " + value );
            //}
        }
        
        return ps;
    }

    /**
     * Creates a new instance of a filter to sql encoder.
     * <p>
     * The <tt>featureType</tt> may be null but it is not recommended. Such a 
     * case where this may neccessary is when a literal needs to be encoded in 
     * isolation.
     * </p>
     */
    protected FilterToSQL createFilterToSQL(SimpleFeatureType featureType) {
        return initializeFilterToSQL( ((BasicSQLDialect)dialect).createFilterToSQL(), featureType  );
    }
    
    /**
     * Creates a new instance of a filter to sql encoder to be 
     * used in a prepared statement.
     * 
     */
    protected PreparedFilterToSQL createPreparedFilterToSQL (SimpleFeatureType featureType) {
        return initializeFilterToSQL( ((PreparedStatementSQLDialect)dialect).createPreparedFilterToSQL(), featureType );
    }
    
    /**
     * Helper method to initialize a filter encoder instance. 
     */
    protected <F extends FilterToSQL> F initializeFilterToSQL( F toSQL, final SimpleFeatureType featureType ) {
        toSQL.setSqlNameEscape(dialect.getNameEscape());
        
        if ( featureType != null ) {
            //set up a fid mapper
            //TODO: remove this
            final PrimaryKey key;

            try {
                key = getPrimaryKey(featureType);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FIDMapper mapper = new FIDMapper() {
                    public String createID(Connection conn, SimpleFeature feature, Statement statement)
                        throws IOException {
                        return null;
                    }

                    public int getColumnCount() {
                        return key.getColumns().size();
                    }

                    public int getColumnDecimalDigits(int colIndex) {
                        return 0;
                    }

                    public String getColumnName(int colIndex) {
                        return key.getColumns().get( colIndex ).getName();
                    }

                    public int getColumnSize(int colIndex) {
                        return 0;
                    }

                    public int getColumnType(int colIndex) {
                        return 0;
                    }

                    public String getID(Object[] attributes) {
                        return null;
                    }

                    public Object[] getPKAttributes(String FID)
                        throws IOException {
                        return decodeFID(key,FID,false).toArray();
                    }

                    public boolean hasAutoIncrementColumns() {
                        return false;
                    }

                    public void initSupportStructures() {
                    }

                    public boolean isAutoIncrement(int colIndex) {
                        return false;
                    }

                    public boolean isVolatile() {
                        return false;
                    }

                    public boolean returnFIDColumnsAsAttributes() {
                        return false;
                    }

                    public boolean isValid(String fid) {
                        return true;
                    }
                };
            toSQL.setFeatureType(featureType);    
            toSQL.setPrimaryKey(key);
            toSQL.setFIDMapper(mapper);
            toSQL.setDatabaseSchema(databaseSchema);
        }
        
        return toSQL;
    }

    /**
     * Helper method to encode table name which checks if a schema is set and
     * prefixes the table name with it.
     * @param hints TODO
     */
    protected void encodeTableName(String tableName, StringBuffer sql, Hints hints) throws SQLException {
        VirtualTable vtDefinition = virtualTables.get(tableName);
        if(vtDefinition != null) {
            sql.append("(").append(vtDefinition.expandParameters(hints)).append(")");
            dialect.encodeTableAlias("vtable", sql);
        } else {
            if (databaseSchema != null) {
                dialect.encodeSchemaName(databaseSchema, sql);
                sql.append(".");
            }
    
            dialect.encodeTableName(tableName, sql);
        }
    }

    /**
     * Helper method for setting the gml:id of a geometry as user data.
     */
    protected void setGmlProperties(Geometry g, String gid, String name, String description) {
        // set up the user data
        Map userData = null;

        if (g.getUserData() != null) {
            if (g.getUserData() instanceof Map) {
                userData = (Map) g.getUserData();
            } else {
                userData = new HashMap();
                userData.put(g.getUserData().getClass(), g.getUserData());
            }
        } else {
            userData = new HashMap();
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
     * Applies the limit/offset elements to the query if they are specified
     * and if the dialect supports them
     * @param sql The sql to be modified
     * @param the query that holds the limit and offset parameters
     */
    void applyLimitOffset(StringBuffer sql, Query query) {
        if(checkLimitOffset(query)) {
            final Integer offset = query.getStartIndex();
            final int limit = query.getMaxFeatures();
            dialect.applyLimitOffset(sql, limit, offset != null ? offset : 0);
        }
    }
    
    /**
     * Checks if the query needs limit/offset treatment
     * @param query
     * @return true if the query needs limit/offset treatment and if the sql dialect can do that natively
     */
    boolean checkLimitOffset(Query query) {
        // if we cannot, don't bother checking the query
        if(!dialect.isLimitOffsetSupported())
            return false;
        
        // the check the query has at least a non default value for limit/offset
        final Integer offset = query.getStartIndex();
        final int limit = query.getMaxFeatures();
        return limit != Integer.MAX_VALUE || (offset != null && offset > 0);
    }
    
    /**
     * Utility method for closing a result set.
     * <p>
     * This method closed the result set "safely" in that it never throws an
     * exception. Any exceptions that do occur are logged at {@link Level#FINER}.
     * </p>
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
     * <p>
     * This method closed the statement"safely" in that it never throws an
     * exception. Any exceptions that do occur are logged at {@link Level#FINER}.
     * </p>
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
     * <p>
     * This method closed the connection "safely" in that it never throws an
     * exception. Any exceptions that do occur are logged at {@link Level#FINER}.
     * </p>
     * @param cx The connection to close.
     */
    public void closeSafe(Connection cx) {
        if (cx == null) {
            return;
        }

        try {
//            System.out.println("Closing connection " + System.identityHashCode(cx));
            cx.close();
            LOGGER.fine( "CLOSE CONNECTION");
        } catch (SQLException e) {
            String msg = "Error occurred closing connection";
            LOGGER.warning(msg);

            if (LOGGER.isLoggable(Level.FINER)) {
                LOGGER.log(Level.FINER, msg, e);
            }
        }
    }
    
    protected void finalize() throws Throwable {
        if(dataSource != null) {
            LOGGER.severe("There's code using JDBC based datastore and " +
                    "not disposing them. This may lead to temporary loss of database connections. " +
                    "Please make sure all data access code calls DataStore.dispose() " +
                    "before freeing all references to it");
            dispose();
        }
        
    }
    
    public void dispose() {
        if(dataSource != null && dataSource instanceof ManageableDataSource) {
            try {
                ManageableDataSource mds = (ManageableDataSource) dataSource; 
                mds.close();
            } catch(SQLException e) {
                // it's ok, we did our best..
                LOGGER.log(Level.FINE, "Could not close dataSource", e);
            }
        }
        dataSource = null;
    }
    /**
     * Checks if geometry generalization required and makes sense
     * 
     * @param hints 	hints hints passed in
     * @param gatt 		Geometry attribute descriptor
     * @return			true to indicate generalization 
     */    
    protected boolean isGeneralizationRequired(Hints hints,GeometryDescriptor gatt) {
    	return isGeometryReduceRequired(hints, gatt, Hints.GEOMETRY_GENERALIZATION);
    }
    
    /**
     * Checks if geometry simplification required and makes sense
     * 
     * @param hints 	hints hints passed in
     * @param gatt 		Geometry attribute descriptor
     * @return			true to indicate simplification 
     */
    protected boolean isSimplificationRequired(Hints hints, GeometryDescriptor gatt) {
    	return isGeometryReduceRequired(hints, gatt, Hints.GEOMETRY_SIMPLIFICATION);    	
    }
    /**
     * Checks if reduction required and makes sense
     *       
     * @param hints	  hints passed in 
     * @param gatt   Geometry attribute descriptor
     * @param param  {@link Hints#GEOMETRY_GENERALIZATION} or {@link Hints#GEOMETRY_SIMPLIFICATION}
     * @return true to indicate reducing the geometry, false otherwise
     */
    protected boolean isGeometryReduceRequired(Hints hints, GeometryDescriptor gatt, Hints.Key param) {
    	if (hints==null) return false;
    	if (hints.containsKey(param)==false) return false;
    	if (gatt.getType().getBinding() == Point.class)  
    		return false;
    	return true;    		
    }

    /**
     * Encoding a geometry column with respect to hints
     * Supported Hints are provided by {@link SQLDialect#addSupportedHints(Set)}
     * 
     * @param gatt
     * @param sql
     * @param hints , may be null 
     */
    protected void encodeGeometryColumn(GeometryDescriptor gatt, StringBuffer sql,Hints hints) {
    	
    	int srid = getDescriptorSRID(gatt);
    	if (isGeneralizationRequired(hints, gatt)==true) {
    		Double distance = (Double) hints.get(Hints.GEOMETRY_GENERALIZATION);
    		dialect.encodeGeometryColumnGeneralized(gatt, srid,sql,distance);
    		return;    		
    	}
    		     	
    	if (isSimplificationRequired(hints, gatt)==true) {
    		Double distance = (Double) hints.get(Hints.GEOMETRY_SIMPLIFICATION);
    		dialect.encodeGeometryColumnSimplified(gatt,srid, sql,distance);
    		return;    		
    	}
    	   	    	
    	dialect.encodeGeometryColumn(gatt,srid, sql);        
    }
    
    /**
     * Builds a transaction object around a user provided connection. The returned transaction
     * allows the store to work against an externally managed transaction, such as in J2EE
     * enviroments. It is the duty of the caller to ensure the connection is to the same database
     * managed by this {@link JDBCDataStore}.
     * 
     * Calls to {@link Transaction#commit()}, {@link Transaction#rollback()} and
     * {@link Transaction#close()} will not result in corresponding calls to the provided
     * {@link Connection} object.
     * 
     * @param conn
     *            The externally managed connection
     */
    public Transaction buildTransaction(Connection cx) {
        DefaultTransaction tx = new DefaultTransaction();

        State state = new JDBCTransactionState(cx, this, true);
        tx.putState(this, state);

        return tx;
    }
    
}
