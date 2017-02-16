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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.Transaction;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.Hints;
import org.geotools.factory.Hints.Key;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.feature.Association;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 *
 * @source $URL$
 */
public class JDBCFeatureSource extends ContentFeatureSource {
    
    private static final Logger LOGGER = Logging.getLogger(JDBCFeatureSource.class);

    /**
     * primary key of the table
     */
    PrimaryKey primaryKey;

    /**
     * Creates the new feature store.
     * @param entry The datastore entry.
     * @param query The defining query.
     */
    public JDBCFeatureSource(ContentEntry entry,Query query) throws IOException {
        super(entry,query);
        
        //TODO: cache this
        primaryKey = ((JDBCDataStore) entry.getDataStore()).getPrimaryKey(entry);
    }
    
    /**
     * Copy existing feature source
     * @param featureSource jdbc feature source
     * @throws IOException
     */
    protected JDBCFeatureSource(JDBCFeatureSource featureSource) throws IOException{
        super(featureSource.entry, featureSource.query);
    }
    
    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        return new JDBCQueryCapabilities(this);
    }
    
    @Override
    protected void addHints(Set<Key> hints) {
        // mark the features as detached, that is, the user can directly alter them
        // without altering the state of the datastore
        hints.add(Hints.FEATURE_DETACHED);
        getDataStore().getSQLDialect().addSupportedHints(hints);
    }

    /**
     * Type narrow to {@link JDBCDataStore}.
     */
    public JDBCDataStore getDataStore() {
        return (JDBCDataStore) super.getDataStore();
    }

    /**
     * Type narrow to {@link JDBCState}.
     */
    public JDBCState getState() {
        return (JDBCState) super.getState();
    }

    /**
     * Returns the primary key of the table backed by feature store.
     */
    public PrimaryKey getPrimaryKey() {
        return primaryKey;
    }
    
    /**
     * Sets the flag which will expose columns which compose a tables identifying or primary key,
     * through feature type attributes. 
     * <p>
     * Note: setting this flag which affect all feature sources created from or working against 
     * the current transaction.
     * </p>
     */
    public void setExposePrimaryKeyColumns(boolean exposePrimaryKeyColumns) {
        ((JDBCState)entry.getState(transaction)).setExposePrimaryKeyColumns(exposePrimaryKeyColumns);
    }
    
    /**
     * The flag which will expose columns which compose a tables identifying or primary key,
     * through feature type attributes.
     */
    public boolean isExposePrimaryKeyColumns() {
        return ((JDBCState)entry.getState(transaction)).isExposePrimaryKeyColumns();
    }
    
    /**
     * Builds the feature type from database metadata.
     */
    protected SimpleFeatureType buildFeatureType() throws IOException {
        //grab the primary key
        PrimaryKey pkey = getDataStore().getPrimaryKey(entry);
        VirtualTable virtualTable = getDataStore().getVirtualTables().get(entry.getTypeName());
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        
        // setup the read only marker if no pk or null pk or it's a view
        boolean readOnly = false;
        if(pkey == null || pkey instanceof NullPrimaryKey || virtualTable != null) {
            readOnly = true;
        }

        //set up the name
        String tableName = entry.getName().getLocalPart();
        tb.setName(tableName);

        //set the namespace, if not null
        if (entry.getName().getNamespaceURI() != null) {
            tb.setNamespaceURI(entry.getName().getNamespaceURI());
        } else {
            //use the data store
            tb.setNamespaceURI(getDataStore().getNamespaceURI());
        }

        //grab the state
        JDBCState state = getState();
        
        //grab the schema
        String databaseSchema = getDataStore().getDatabaseSchema();

        //ensure we have a connection
        Connection cx = getDataStore().getConnection(state);
        
        // grab the dialect
        SQLDialect dialect = getDataStore().getSQLDialect();


        //get metadata about columns from database
        try {
            DatabaseMetaData metaData = cx.getMetaData();
            // get metadata about columns from database
            List<ColumnMetadata> columns;
            if (virtualTable != null) {
                columns = getColumnMetadata(cx, virtualTable, dialect, getDataStore());
            } else {
                columns = getColumnMetadata(cx, databaseSchema, tableName, dialect);
            }

            for (ColumnMetadata column : columns) {
                String name = column.name;

                //do not include primary key in the type if not exposing primary key columns
                boolean pkColumn = false;
                for ( PrimaryKeyColumn pkeycol : pkey.getColumns() ) {
                    if ( name.equals( pkeycol.getName() ) ) {
                        if ( !state.isExposePrimaryKeyColumns() ) {
                            name = null;
                            break;
                        } else {
                            pkColumn = true;
                        }
                    }
                    // in views we don't know the pk type, grab it now
                    if(pkeycol.type == null) {
                        pkeycol.type = column.binding;
                    }
                }
             
                if (name == null) {
                    continue;
                }

                //check for association
                if (getDataStore().isAssociations()) {
                    getDataStore().ensureAssociationTablesExist(cx);

                    //check for an association
                    Statement st = null;
                    ResultSet relationships = null;
                    if ( getDataStore().getSQLDialect() instanceof PreparedStatementSQLDialect ) {
                        st = getDataStore().selectRelationshipSQLPS(tableName, name, cx);
                        relationships = ((PreparedStatement)st).executeQuery();
                    }
                    else {
                        String sql = getDataStore().selectRelationshipSQL(tableName, name);
                        getDataStore().getLogger().fine(sql);
                        
                        st = cx.createStatement();
                        relationships = st.executeQuery(sql);
                    }

                   try {
                        if (relationships.next()) {
                            //found, create a special mapping 
                            tb.add(name, Association.class);

                            continue;
                        }
                    } finally {
                        getDataStore().closeSafe(relationships);
                        getDataStore().closeSafe(st);
                    }
                    
                }

                //first ask the dialect
                Class binding = column.binding;

                if (binding == null) {
                    //determine from type name mappings
                    binding = getDataStore().getMapping(column.typeName);
                }

                if (binding == null) {
                    //determine from type mappings
                    binding = getDataStore().getMapping(column.sqlType);
                }

                // if still not found, ignore the column we don't know about
                if (binding == null) {
                    getDataStore().getLogger().warning("Could not find mapping for '" + name 
                            + "', ignoring the column and setting the feature type read only");
                	readOnly = true;
                	continue;
                }
                
                // store the native database type in the attribute descriptor user data
                ab.addUserData(JDBCDataStore.JDBC_NATIVE_TYPENAME, column.typeName);

                // nullability
                if (!column.nullable) {
                    ab.nillable(false);
                    ab.minOccurs(1);
                }
                
                AttributeDescriptor att = null;
                
                //determine if this attribute is a geometry or not
                if (Geometry.class.isAssignableFrom(binding)) {
                    //add the attribute as a geometry, try to figure out 
                    // its srid first
                    Integer srid = null;
                    CoordinateReferenceSystem crs = null;
                    try {
                        if(virtualTable != null) {
                            srid = virtualTable.getNativeSrid(name);
                        } else {
                            srid = dialect.getGeometrySRID(databaseSchema, tableName, name, cx);
                        }
                        if(srid != null)
                            crs = dialect.createCRS(srid, cx);
                    } catch (Exception e) {
                        String msg = "Error occured determing srid for " + tableName + "."
                            + name;
                        getDataStore().getLogger().log(Level.WARNING, msg, e);
                    }
                    
                    // compute the dimension too
                    int dimension = 2;
                    try {
                        if(virtualTable != null) {
                            dimension = virtualTable.getDimension(name);
                        } else {
                            dimension = dialect.getGeometryDimension(databaseSchema, tableName, name, cx);
                        }
                    } catch(Exception e) {
                        String msg = "Error occured determing dimension for " + tableName + "."
                                + name;
                        getDataStore().getLogger().log(Level.WARNING, msg, e);
                    }

                    ab.setBinding(binding);
                    ab.setName(name);
                    ab.setCRS(crs);
                    if(srid != null) {
                        ab.addUserData(JDBCDataStore.JDBC_NATIVE_SRID, srid);
                    }
                    ab.addUserData(Hints.COORDINATE_DIMENSION, dimension);
                    att = ab.buildDescriptor(name, ab.buildGeometryType());
                } else {
                    //add the attribute
                    ab.setName(name);
                    ab.setBinding(binding);
                    att = ab.buildDescriptor(name, ab.buildType());
                }
                // mark primary key columns
                if (pkey.getColumn(att.getLocalName()) != null) {
                    att.getUserData().put(JDBCDataStore.JDBC_PRIMARY_KEY_COLUMN, true);
                }
                
                //call dialect callback
                dialect.postCreateAttribute( att, tableName, databaseSchema, cx);
                tb.add(att);
            }

            //build the final type
            SimpleFeatureType ft = tb.buildFeatureType();
            
            // mark it as read only if necessary 
            // (the builder userData method affects attributes, not the ft itself)
            if(readOnly) {
                ft.getUserData().put(JDBCDataStore.JDBC_READ_ONLY, Boolean.TRUE);
            }

            //call dialect callback
            dialect.postCreateFeatureType(ft, metaData, databaseSchema, cx);
            return ft;
        } catch (SQLException e) {
            String msg = "Error occurred building feature type";
            throw (IOException) new IOException(msg).initCause(e);
        } finally {
            getDataStore().releaseConnection( cx, state );
        }
    }

    /**
     * Helper method for splitting a filter.
     */
    Filter[] splitFilter(Filter original) {
        return splitFilter(original, this);
    }

    Filter[] splitFilter(Filter original, FeatureSource source) {
        JDBCFeatureSource featureSource = null;
        if (source instanceof JDBCFeatureSource) {
            featureSource = (JDBCFeatureSource) source;
        }
        else {
            featureSource = ((JDBCFeatureStore)source).getFeatureSource();
        }

        Filter[] split = new Filter[2];
        if ( original != null ) {
            //create a filter splitter
            PostPreProcessFilterSplittingVisitor splitter = new PostPreProcessFilterSplittingVisitor(getDataStore()
                    .getFilterCapabilities(), featureSource.getSchema(), null);
            original.accept(splitter, null);
        
            split[0] = splitter.getFilterPre();
            split[1] = splitter.getFilterPost();
        }
        
        // handle three-valued logic differences by adding "is not null" checks in the filter,
        // the simplifying filter visitor will take care of them if they are redundant
        NullHandlingVisitor nhv = new NullHandlingVisitor(source.getSchema());
        split[0] = (Filter) split[0].accept(nhv, null);
        
        SimplifyingFilterVisitor visitor = new SimplifyingFilterVisitor();
        visitor.setFIDValidator( new PrimaryKeyFIDValidator( featureSource ) );
        visitor.setFeatureType(getSchema());
        split[0] = (Filter) split[0].accept(visitor, null);
        split[1] = (Filter) split[1].accept(visitor, null);
        
        return split;
    }

    protected int getCountInternal(Query query) throws IOException {
        JDBCDataStore dataStore = getDataStore();

        //split the filter
        Filter[] split = splitFilter( query.getFilter() );
        Filter preFilter = split[0];
        Filter postFilter = split[1];
        
        boolean manual = (postFilter != null) && (postFilter != Filter.INCLUDE);
        if (!manual && !query.getJoins().isEmpty()) {
            //check any join post filters as well
            JoinInfo join = JoinInfo.create(query, this);
            manual = join.hasPostFilters();
        }
            if (manual) {
                try {
                    //calculate manually, dont use datastore optimization
                    getDataStore().getLogger().fine("Calculating size manually");
    
                    int count = 0;
    
                    //grab a reader
                     FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader( query );
                    try {
                        while (reader.hasNext()) {
                            reader.next();
                            count++;
                        }
                    } finally {
                        reader.close();
                    }
    
                    return count;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                //no post filter, we have a preFilter, or preFilter is null.. 
                // either way we can use the datastore optimization
                Connection cx = dataStore.getConnection(getState());
                try {
                    DefaultQuery q = new DefaultQuery(query);
                    q.setFilter(preFilter);
                    int count = dataStore.getCount(getSchema(), q, cx);
                    // if native support for limit and offset is not implemented, we have to ajust the result
                    if(!dataStore.getSQLDialect().isLimitOffsetSupported()) {
                        if(query.getStartIndex() != null && query.getStartIndex() > 0) {
                            if(query.getStartIndex() > count)
                                count = 0;
                            else
                                count -= query.getStartIndex();
                        }
                        if(query.getMaxFeatures() > 0 && count > query.getMaxFeatures())
                            count = query.getMaxFeatures();
                    }
                    return count;
                }
                finally {
                    dataStore.releaseConnection(cx, getState());
                }
            } 
        
    }
    
    protected ReferencedEnvelope getBoundsInternal(Query query)
            throws IOException {
        JDBCDataStore dataStore = getDataStore();

        //split the filter
        Filter[] split = splitFilter( query.getFilter() );
        Filter preFilter = split[0];
        Filter postFilter = split[1];
        
        try {
            
            if ((postFilter != null) && (postFilter != Filter.INCLUDE) 
                    || (query.getMaxFeatures() < Integer.MAX_VALUE && !canLimit())
                    || (query.getStartIndex() != null && query.getStartIndex() > 0 && !canOffset())) {
                //calculate manually, don't use datastore optimization
                getDataStore().getLogger().fine("Calculating bounds manually");

                // grab the 2d part of the crs 
                CoordinateReferenceSystem flatCRS = CRS.getHorizontalCRS(getSchema().getCoordinateReferenceSystem());
                ReferencedEnvelope bounds = new ReferencedEnvelope(flatCRS);

                // grab a reader
                DefaultQuery q = new DefaultQuery(query);
                q.setFilter(postFilter);
                FeatureReader<SimpleFeatureType, SimpleFeature> i = getReader(q);
                try {
                    if (i.hasNext()) {
                        SimpleFeature f = i.next();
                        bounds.init(f.getBounds());

                        while (i.hasNext()) {
                            f = i.next();
                            bounds.include(f.getBounds());
                        }
                    }
                } finally {
                    i.close();
                }

                return bounds;
            } 
            else {
                //post filter was null... pre can be set or null... either way
                // use datastore optimization
                Connection cx = dataStore.getConnection(getState());
                try {
                    DefaultQuery q = new DefaultQuery(query);
                    q.setFilter(preFilter);
                    return dataStore.getBounds(getSchema(), q, cx);
                }
                finally {
                    getDataStore().releaseConnection( cx, getState() );
                }
            } 
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    protected boolean canFilter() {
        return true;
    }
    
    protected boolean canSort() {
        return true;
    }
    
    protected boolean canRetype() {
        return true;
    }
    
    @Override
    protected boolean canLimit() {
        return getDataStore().getSQLDialect().isLimitOffsetSupported();
    }
    
    @Override
    protected boolean canOffset() {
        return getDataStore().getSQLDialect().isLimitOffsetSupported();
    }
    
    @Override
    protected boolean canTransact() {
        return true;
    }
    
    protected  FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        // split the filter
        Filter[] split = splitFilter(query.getFilter());
        Filter preFilter = split[0];
        Filter postFilter = split[1];
        boolean postFilterRequired = postFilter != null && postFilter != Filter.INCLUDE;

        // rebuild a new query with the same params, but just the pre-filter
        DefaultQuery preQuery = new DefaultQuery(query);
        preQuery.setFilter(preFilter);
        // in case of post filtering, we cannot do native paging
        if(postFilterRequired) {
            preQuery.setStartIndex(0);
            preQuery.setMaxFeatures(Integer.MAX_VALUE);
        }

        // Build the feature type returned by this query. Also build an eventual extra feature type
        // containing the attributes we might need in order to evaluate the post filter
        SimpleFeatureType[] types = 
            buildQueryAndReturnFeatureTypes(getSchema(), query.getPropertyNames(), postFilter);
        SimpleFeatureType querySchema = types[0];
        SimpleFeatureType returnedSchema = types[1];

        //grab connection
        Connection cx = getDataStore().getConnection(getState());
        
        //create the reader
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        
        try {            
            SQLDialect dialect = getDataStore().getSQLDialect();

            // allow dialect to override this if needed
            if(getState().getTransaction() == Transaction.AUTO_COMMIT) {
                cx.setAutoCommit(dialect.isAutoCommitQuery());
            }

            if (query.getJoins().isEmpty()) {
                //regular query
                if ( dialect instanceof PreparedStatementSQLDialect ) {
                    PreparedStatement ps = getDataStore().selectSQLPS(querySchema, preQuery, cx);
                    reader = new JDBCFeatureReader( ps, cx, this, querySchema, query );
                } else {
                    //build up a statement for the content
                    String sql = getDataStore().selectSQL(querySchema, preQuery);
                    getDataStore().getLogger().fine(sql);
        
                    reader = new JDBCFeatureReader( sql, cx, this, querySchema, query );
                }
            }
            else {
                JoinInfo join = JoinInfo.create(preQuery, this);

                if ( dialect instanceof PreparedStatementSQLDialect ) {
                    PreparedStatement ps =getDataStore().selectJoinSQLPS(querySchema, join, preQuery, cx);
                    reader = new JDBCJoiningFeatureReader(ps, cx, this, querySchema, join, query);
                } else {
                    //build up a statement for the content
                    String sql = getDataStore().selectJoinSQL(querySchema, join, preQuery);
                    getDataStore().getLogger().fine(sql);
        
                    reader = new JDBCJoiningFeatureReader(sql, cx, this, querySchema, join, query);
                }
                
                //check for post filters
                if (join.hasPostFilters()) {
                    reader = new JDBCJoiningFilteringFeatureReader(reader, join);
                    //TODO: retyping 
                }
            }
        } catch (Throwable e) { // NOSONAR
            // close the connection
            getDataStore().closeSafe(cx);
            // safely rethrow
            if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw (IOException) new IOException().initCause(e);
            }
        }
        

        // if post filter, wrap it
        if (postFilterRequired) {
            reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader, postFilter);
            if(!returnedSchema.equals(querySchema)) {
                reader = new ReTypeFeatureReader(reader, returnedSchema);
            }

            // offset
            int offset = query.getStartIndex() != null ? query.getStartIndex() : 0;
            if(offset > 0 ) {
                // skip the first n records
                for(int i = 0; i < offset && reader.hasNext(); i++) {
                    reader.next();
                }
            }

            // max feature limit
            if (query.getMaxFeatures() >= 0 && query.getMaxFeatures() < Integer.MAX_VALUE ) {
                reader = new MaxFeatureReader<SimpleFeatureType, SimpleFeature>(reader, query.getMaxFeatures());
            }
        }

        return reader;
    }

    SimpleFeatureType[] buildQueryAndReturnFeatureTypes(SimpleFeatureType featureType, 
        String[] propertyNames, Filter filter) {

        SimpleFeatureType[] types = null; 
        if(propertyNames == Query.ALL_NAMES) {
            return new SimpleFeatureType[]{featureType, featureType};
        } else {
            SimpleFeatureType returnedSchema = SimpleFeatureTypeBuilder.retype(featureType, propertyNames);
            SimpleFeatureType querySchema = returnedSchema;
            
            if (filter != null && !filter.equals(Filter.INCLUDE)) {
                FilterAttributeExtractor extractor = new FilterAttributeExtractor(featureType);
                filter.accept(extractor, null);
                
                String[] extraAttributes = extractor.getAttributeNames();
                if(extraAttributes != null && extraAttributes.length > 0) {
                    List<String> allAttributes = new ArrayList<String>(Arrays.asList(propertyNames)); 
                    for (String extraAttribute : extraAttributes) {
                        if(!allAttributes.contains(extraAttribute))
                            allAttributes.add(extraAttribute);
                    }
                    String[] allAttributeArray = 
                        allAttributes.toArray(new String[allAttributes.size()]);
                    querySchema = SimpleFeatureTypeBuilder.retype(getSchema(), allAttributeArray);
                }
            }
            types = new SimpleFeatureType[]{querySchema, returnedSchema};
        }
        return types;
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        // special case for nearest visit, it's the sum of two other visits
        if(visitor instanceof NearestVisitor) {
            return handleNearestVisitor(query, visitor);
        } else {
            // grab connection using the current transaction
            Connection cx = getDataStore().getConnection(getState());
            try {
                Object result = getDataStore().getAggregateValue(visitor, getSchema(), query, cx);
                return result != null;
            }
            finally {
            	// release the connection - behaviour depends on Transaction.AUTO_COMMIT
            	getDataStore().releaseConnection(cx, getState());
            }
        }
    }

    /**
     * Special case of nearest visitor, which can be computed by combining a min and a max visit
     * @param query
     * @param visitor
     * @param nearest value, or null if not supported
     * @throws IOException
     */
    private boolean handleNearestVisitor(Query query, FeatureVisitor visitor) throws IOException {
        NearestVisitor nearest = (NearestVisitor) visitor;
        Object targetValue = nearest.getValueToMatch();
        Expression expr = nearest.getExpression();
        String attribute = null;
        
        if( expr != null && expr instanceof PropertyName){
            attribute = ((PropertyName)expr).getPropertyName();
        }
        if( attribute == null ) {
            return false; // optimization restricted to column evaulation
        }
        
        // check what we're dealing with (and mind, Geometry is Comparable for JTS, but not for databases
        AttributeDescriptor descriptor = getSchema().getDescriptor(attribute);
        if( descriptor == null ) {
            return false; // optimization restricted to column evaulation
        }
        Class binding = descriptor.getType().getBinding();
        if(Geometry.class.isAssignableFrom(binding) || !(Comparable.class.isAssignableFrom(binding))) {
            // we may roll out KNN support in the dialect for geometries, but for the moment, we say we can't
            return false;
        }
        
        // grab max of values lower than the target
        FilterFactory ff = getDataStore().getFilterFactory();
        Query qBelow = new Query(query);
        Filter lessFilter = ff.lessOrEqual(ff.property(attribute), ff.literal(targetValue));
        qBelow.setFilter(ff.and(query.getFilter(), lessFilter));
        MaxVisitor max = new MaxVisitor(attribute);
        handleVisitor(qBelow, max);
        Comparable maxBelow = (Comparable) max.getResult().getValue();
        if(maxBelow != null && maxBelow.equals(targetValue)) {
            // shortcut exit, we had a exact match
            nearest.setValue(maxBelow, null);
        } else {
            // grab mind of values higher than the target
            Query qAbove = new Query(query);
            Filter aboveFilter = ff.greater(ff.property(attribute), ff.literal(targetValue));
            qAbove.setFilter(ff.and(query.getFilter(), aboveFilter));
            MinVisitor min = new MinVisitor(attribute);
            handleVisitor(qAbove, min);
            Comparable minAbove = (Comparable) min.getResult().getValue();
            nearest.setValue(maxBelow, minAbove);
        }
        
        return true;
    }
    
    /**
     * Computes the column metadata from a plain database table
     * @param cx
     * @param databaseSchema
     * @param tableName
     * @param dialect
     * @return
     * @throws SQLException
     */
    List<ColumnMetadata> getColumnMetadata(Connection cx, String databaseSchema, String tableName, SQLDialect dialect)
            throws SQLException {
        List<ColumnMetadata> result = new ArrayList<ColumnMetadata>();

        DatabaseMetaData metaData = cx.getMetaData();

        /*
         * <UL>
         * <LI><B>COLUMN_NAME</B> String => column name
         * <LI><B>DATA_TYPE</B> int => SQL type from java.sql.Types
         * <LI><B>TYPE_NAME</B> String => Data source dependent type name, for a UDT the type name
         * is fully qualified
         * <LI><B>COLUMN_SIZE</B> int => column size. For char or date types this is the maximum
         * number of characters, for numeric or decimal types this is precision.
         * <LI><B>BUFFER_LENGTH</B> is not used.
         * <LI><B>DECIMAL_DIGITS</B> int => the number of fractional digits
         * <LI><B>NUM_PREC_RADIX</B> int => Radix (typically either 10 or 2)
         * <LI><B>NULLABLE</B> int => is NULL allowed.
         * <UL>
         * <LI>columnNoNulls - might not allow <code>NULL</code> values
         * <LI>columnNullable - definitely allows <code>NULL</code> values
         * <LI>columnNullableUnknown - nullability unknown
         * </UL>
         * <LI><B>COLUMN_DEF</B> String => default value (may be <code>null</code>)
         * <LI>
         * <B>IS_NULLABLE</B> String => "NO" means column definitely does not allow NULL values;
         * "YES" means the column might allow NULL values. An empty string means nobody knows.
         * </UL>
         */
        ResultSet columns = metaData.getColumns(cx.getCatalog(),
                getDataStore().escapeNamePattern(metaData, databaseSchema),
                getDataStore().escapeNamePattern(metaData, tableName),
                "%");
        if(getDataStore().getFetchSize() > 0) {
            columns.setFetchSize(getDataStore().getFetchSize());
        }

        try {
            while (columns.next()) {
                ColumnMetadata column = new ColumnMetadata();
                column.name = columns.getString("COLUMN_NAME");
                column.typeName = columns.getString("TYPE_NAME");
                column.sqlType = columns.getInt("DATA_TYPE");
                column.nullable = "YES".equalsIgnoreCase(columns.getString("IS_NULLABLE"));
                column.binding = dialect.getMapping(columns, cx);
                
                //support for user defined types, allow the dialect to handle them
                if (column.sqlType == Types.DISTINCT) {
                    dialect.handleUserDefinedType(columns, column, cx);
                }
                
                result.add(column);
            }
        } finally {
            getDataStore().closeSafe(columns);
        }

        return result;
    }
    
    /**
     * Computes the column metadata by running the virtual table query
     * @param cx
     * @param vtable
     * @param dialect
     * @return
     * @throws SQLException
     */
    static List<ColumnMetadata> getColumnMetadata(Connection cx, VirtualTable vtable, SQLDialect dialect, JDBCDataStore store) throws SQLException {
        List<ColumnMetadata> result = new ArrayList<ColumnMetadata>();

        Statement st = null;
        ResultSet rs = null;
        try {
            String sql;
            // avoid actually running the query as it might be very expensive
            // and just grab the metadata instead
            StringBuffer sb = new StringBuffer();
            sb.append("select * from (");
            sb.append(vtable.expandParameters(null));
            sb.append(")");
            dialect.encodeTableAlias("vtable", sb);
            // state we don't want rows, we just want to gather the results metadata
            sb.append( " where 1 = 0");
            sql = sb.toString();
            
            st = cx.createStatement();
            
            LOGGER.log(Level.FINE, "Gathering sql view result structure: {0}", sql);
            
            rs = st.executeQuery(sql);
            
            ResultSetMetaData metadata = rs.getMetaData();
            for(int i = 1; i < metadata.getColumnCount() + 1; i++) {
                ColumnMetadata column = new ColumnMetadata();
                column.name = metadata.getColumnLabel(i);
                column.typeName = metadata.getColumnTypeName(i);
                column.sqlType = metadata.getColumnType(i);
                column.nullable = metadata.isNullable(i) != ResultSetMetaData.columnNoNulls;
                column.srid = vtable.getNativeSrid(column.name);
                column.binding = vtable.getGeometryType(column.name);
                if(column.binding == null) {
                    // determine from type mappings
                    column.binding = store.getMapping(column.typeName);
    
                    if (column.binding == null) {
                        //determine from type name mappings
                        column.binding = store.getMapping(column.sqlType);
                    }
                }
                result.add(column);
            }
        } finally {
            store.closeSafe(st);
            store.closeSafe(rs);
        }
        
        return result;
    }
    
}
