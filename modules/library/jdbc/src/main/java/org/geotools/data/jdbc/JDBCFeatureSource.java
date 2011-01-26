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
package org.geotools.data.jdbc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.NullFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.SQLEncoderException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;


/**
 * A JDBCFeatureSource that can opperate as a starting point for your own
 * implementations.
 * 
 * <p>
 * This class is distinct from the AbstractFeatureSource implementations as
 * JDBC provides us with so many opertunities for optimization.
 * </p>
 * Client code must implement:
 * 
 * <ul>
 * <li>
 * getJDBCDataStore()
 * </li>
 * </ul>
 * 
 * It is recomended that clients implement optimizations for:
 * <ul>
 * <li>
 * getBounds( Query )
 * </li>
 * <li>
 * getCount( Query )
 * </li>
 * </ul>
 * 
 *
 * @author Jody Garnett, Refractions Research
 * @source $URL$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class JDBCFeatureSource implements SimpleFeatureSource {
    /** The logger for the filter module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc");
    
    /**
     * A default QueryCapabilities implementation for JDBCFeatureSource with template methods
     * JDBCFeatureSource subclasses may override.
     * <p>
     * This default implementation assumes sorting is supported both in ascending and descending
     * order by any FeatureType attribute. Sorting by {@link SortBy#NATURAL_ORDER} and
     * {@link SortBy#REVERSE_ORDER}, by the other hand, defaults to <code>false</code>, since a
     * datastore may take explicit actions in orther to support those concepts, though most of the
     * time it implies sorting by the primary key for JDBC datastores.
     * </p>
     * 
     * @author Gabriel Roldan (TOPP)
     * @version $Id$
     * @since 2.4.3
     * @see #supportsNaturalOrderSorting()
     * @see #supportsReverseOrderSorting()
     * @see #supportsPropertySorting(PropertyName, SortOrder)
     */
    protected class JDBCQueryCapabilities extends QueryCapabilities {

        private SimpleFeatureType featureType;

        /**
         * Creates a new JDBCQueryCapabilities to check for sorting support over the
         * attributes of the provided feature type.
         * @param fullFeatureType the feature type with all the registered attribtues
         */
        public JDBCQueryCapabilities(SimpleFeatureType fullFeatureType) {
            this.featureType = fullFeatureType;
        }

        /**
         * Overrides to delegate to the three template methods in order to check for sorting
         * capabilities over the natural and reverse order, and each specific attribute type.
         */
        @Override
        public boolean supportsSorting(final SortBy[] sortAttributes) {
            if(super.supportsSorting(sortAttributes))
                return true;
            
            for (int i = 0; i < sortAttributes.length; i++) {
                SortBy sortBy = sortAttributes[i];
                if (SortBy.NATURAL_ORDER == sortBy) {
                    if(!supportsNaturalOrderSorting()){
                        return false;
                    }
                }else if (SortBy.REVERSE_ORDER == sortBy) {
                    if(!supportsReverseOrderSorting()){
                        return false;
                    }
                }else{
                    PropertyName propertyName = sortBy.getPropertyName();
                    SortOrder sortOrder = sortBy.getSortOrder();
                    if (!supportsPropertySorting(propertyName, sortOrder)) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Indicates whether sorting by {@link SortBy#NATURAL_ORDER} is supported; defaults to
         * <code>false</code>.
         * 
         * @return false, override if NATURAL_ORDER sorting is supported.
         */
        protected boolean supportsNaturalOrderSorting() {
            return false;
        }

        /**
         * Indicates whether sorting by {@link SortBy#REVERSE_ORDER} is supported; defaults to
         * <code>false</code>.
         * 
         * @return false, override if REVERSE_ORDER sorting is supported.
         */
        protected boolean supportsReverseOrderSorting() {
            return false;
        }

        /**
         * Template method to check for sorting support in the given sort order for a specific
         * attribute type, given by a PropertyName expression.
         * <p>
         * This default implementation assumes both orders are supported as long as the property
         * name corresponds to the name of one of the attribute types in the complete FeatureType.
         * </p>
         * 
         * @param propertyName the expression holding the property name to check for sortability
         *            support
         * @param sortOrder the order, ascending or descending, to check for sortability support
         *            over the given property name.
         * @return true if propertyName refers to one of the FeatureType attributes
         */
        protected boolean supportsPropertySorting(PropertyName propertyName, SortOrder sortOrder) {
            AttributeDescriptor descriptor = (AttributeDescriptor) propertyName.evaluate(featureType);
            if (descriptor != null) {
                return true;
            }
            String attName = propertyName.getPropertyName();
            AttributeDescriptor attribute = featureType.getDescriptor(attName);
            return attribute != null;
        }
        
        /**
         * Consults the fid mapper for the feature source, if the null feature map reliable fids
         * not supported. 
         */
        @Override
        public boolean isReliableFIDSupported() {
            FIDMapper mapper;
            try {
                mapper = JDBCFeatureSource.this.dataStore.getFIDMapper(featureType.getTypeName());
            } 
            catch (IOException e) {
                LOGGER.warning( "Unable to access fid mapper" );
                LOGGER.log( Level.FINE, "", e );
                return super.isReliableFIDSupported();
            }
            
            return !isNullFidMapper( mapper );
        }
        
        /**
         * Helper method to test if a fid mapper is a null fid mapper.
         */
        protected boolean isNullFidMapper( FIDMapper mapper ) {
            if ( mapper instanceof TypedFIDMapper ) {
                mapper = ((TypedFIDMapper)mapper).getWrappedMapper();
            }
            
            return mapper instanceof NullFIDMapper;
        }
        
        @Override
        public boolean isUseProvidedFIDSupported() {
            return this instanceof FeatureStore;
        }
    }
    
    /** FeatureType being provided */
    private SimpleFeatureType featureType;
    
    /** JDBCDataStore based dataStore used to aquire content */
    private JDBC1DataStore dataStore;

    protected QueryCapabilities queryCapabilities;
    
    /**
     * JDBCFeatureSource creation.
     * 
     * <p>
     * Constructs a FeatureStore that opperates against the provided
     * jdbcDataStore to serve up the contents of featureType.
     * </p>
     *
     * @param jdbcDataStore DataStore containing contents
     * @param featureType FeatureType being served
     */
    public JDBCFeatureSource(JDBC1DataStore jdbcDataStore,
        SimpleFeatureType featureType) {
        this.featureType = featureType;
        this.dataStore = jdbcDataStore;
        //We assume jdbc datastores can sort by any field. Be sure to override
        //if you support @id, NATURAL_ORDER, or REVERSE_ORDER
        this.queryCapabilities = new JDBCQueryCapabilities(featureType);
    }
    
    /**
     * Returns the same name than the feature type (ie,
     * {@code getSchema().getName()} to honor the simple feature land common
     * practice of calling the same both the Features produces and their types
     * 
     * @since 2.5
     * @see FeatureSource#getName()
     */
    public Name getName() {
        return getSchema().getName();
    }
    
    public ResourceInfo getInfo() {
        return new ResourceInfo(){
            final Set<String> words = new HashSet<String>();
            {
                words.add("features");
                words.add( JDBCFeatureSource.this.getSchema().getTypeName() );
            }
            public ReferencedEnvelope getBounds() {
                try {
                    return JDBCFeatureSource.this.getBounds();
                } catch (IOException e) {
                    return null;
                }
            }
            public CoordinateReferenceSystem getCRS() {
                return JDBCFeatureSource.this.getSchema().getCoordinateReferenceSystem();
            }
    
            public String getDescription() {
                return null;
            }
    
            public Set<String> getKeywords() {
                return words;
            }
    
            public String getName() {
                return JDBCFeatureSource.this.getSchema().getTypeName();
            }
    
            public URI getSchema() {
                Name name = JDBCFeatureSource.this.getSchema().getName();
                URI namespace;
                try {
                    namespace = new URI( name.getNamespaceURI() );
                    return namespace;                    
                } catch (URISyntaxException e) {
                    return null;
                }                
            }
    
            public String getTitle() {
                Name name = JDBCFeatureSource.this.getSchema().getName();
                return name.getLocalPart();
            }
            
        };
    }
    /**
     * Retrieve DataStore for this FetureSource.
     *
     *
     * @see org.geotools.data.FeatureSource#getDataStore()
     */
    public DataStore getDataStore() {
        return getJDBCDataStore();
    }

    /**
     * Allows access to JDBCDataStore(). Description
     * 
     * <p>
     * Subclass must implement
     * </p>
     *
     * @return JDBDataStore managing this FeatureSource
     */
    public JDBC1DataStore getJDBCDataStore() {
        return dataStore;
    }

    /**
     * Adds FeatureListener to the JDBCDataStore against this FeatureSource.
     *
     * @param listener
     *
     * @see org.geotools.data.FeatureSource#addFeatureListener(org.geotools.data.FeatureListener)
     */
    public void addFeatureListener(FeatureListener listener) {
        getJDBCDataStore().listenerManager.addFeatureListener(this, listener);
    }

    /**
     * Remove FeatureListener to the JDBCDataStore against this FeatureSource.
     *
     * @param listener
     *
     * @see org.geotools.data.FeatureSource#removeFeatureListener(org.geotools.data.FeatureListener)
     */
    public void removeFeatureListener(FeatureListener listener) {
        getJDBCDataStore().listenerManager.removeFeatureListener(this, listener);
    }

    /**
     * Retrieve the Transaction this SimpleFeatureSource is opperating against.
     * 
     * <p>
     * For a plain JDBCFeatureSource that cannot modify this will always be
     * Transaction.AUTO_COMMIT.
     * </p>
     *
     * @return DOCUMENT ME!
     */
    public Transaction getTransaction() {
        return Transaction.AUTO_COMMIT;
    }

    /**
     * Provides an interface to for the Resutls of a Query.
     * 
     * <p>
     * Various queries can be made against the results, the most basic being to
     * retrieve Features.
     * </p>
     *
     * @param request
     *
     *
     * @throws IOException
     *
     * @see org.geotools.data.FeatureSource#getFeatures(org.geotools.data.Query)
     */
    public SimpleFeatureCollection getFeatures(Query request) throws IOException {
        String typeName = featureType.getTypeName();

        if ((request.getTypeName() != null) && !typeName.equals(request.getTypeName())) {
            throw new IOException("Cannot query " + typeName + " with:" + request);
        }
        
        if (request.getTypeName() == null) {
            request = new DefaultQuery(request);
            ((DefaultQuery) request).setTypeName(featureType.getTypeName());
        }
        
        final QueryCapabilities queryCapabilities = getQueryCapabilities();
        if(!queryCapabilities.supportsSorting(request.getSortBy())){
           throw new DataSourceException("DataStore cannot provide the requested sort order");
        }
        
        return new JDBCFeatureCollection(this, request);
    }

    /**
     * Retrieve all Feature matching the Filter
     *
     * @param filter
     * @return SimpleFeatureCollection
     * @throws IOException
     */
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return getFeatures(new DefaultQuery(featureType.getTypeName(), filter));
    }

    /**
     * Retrieve all Features
     *
     * @return SimpleFeature colleciton of all features
     *
     * @throws IOException
     */
    public SimpleFeatureCollection getFeatures() throws IOException {
        return getFeatures(Filter.INCLUDE);
    }

    /**
     * Retrieve Bounds of all Features.
     * <p>
     * Currently returns null, consider getFeatures().getBounds() instead.
     * </p>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @return null representing the lack of an optimization
     *
     * @throws IOException DOCUMENT ME!
     */
    public ReferencedEnvelope getBounds() throws IOException {
        return getBounds(Query.ALL);
    }

    /**
     * Retrieve Bounds of Query results.
     * 
     * <p>
     * Currently returns null, consider getFeatures( query ).getBounds()
     * instead.
     * </p>
     * 
     * <p>
     * Subclasses may override this method to perform the appropriate
     * optimization for this result.
     * </p>
     *
     * @param query Query we are requesting the bounds of
     *
     * @return null representing the lack of an optimization
     *
     * @throws IOException DOCUMENT ME!
     */
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        if (query.getFilter() == Filter.EXCLUDE) {
            if(featureType!=null)
                return new ReferencedEnvelope(new Envelope(),featureType.getGeometryDescriptor().getCoordinateReferenceSystem());
            return new ReferencedEnvelope();
        }               
        return null; // too expensive right now :-)
    }
    /**
     * Retrieve total number of Query results.
     * <p>
     * SQL: SELECT COUNT(*) as cnt FROM table WHERE filter
     * </p>
     * 
     * @param query Query we are requesting the count of
     * @return Count of indicated query
     */
    public int getCount(Query query) throws IOException {
        return count(query, getTransaction() );        
    }

    /**
     * Direct SQL query number of rows in query.
     * 
     * <p>
     * Note this is a low level SQL statement and if it fails the provided
     * Transaction will be rolled back.
     * </p>
     * <p>
     * SQL: SELECT COUNT(*) as cnt FROM table WHERE filter
     * </p>
     * @param query
     * @param transaction
     *
     * @return Number of rows in query, or -1 if not optimizable.
     *
     * @throws IOException Usual on the basis of a filter error
     */
    public int count(Query query, Transaction transaction)
        throws IOException {
        Filter filter = query.getFilter();

        if (Filter.EXCLUDE.equals(filter)) {
            return 0;
        }

        JDBC1DataStore jdbc = getJDBCDataStore();
        SQLBuilder sqlBuilder = jdbc.getSqlBuilder(featureType.getTypeName());

        Filter preFilter = (Filter) sqlBuilder.getPreQueryFilter(filter); 
        Filter postFilter = (Filter) sqlBuilder.getPostQueryFilter(filter); 
        if (postFilter != null && !Filter.INCLUDE.equals(postFilter)) {
            // this would require postprocessing the filter
            // so we cannot optimize
            return -1;
        }

        Connection conn = null;

        try {
            conn = jdbc.getConnection(transaction);

            String typeName = getSchema().getTypeName();
            StringBuffer sql = new StringBuffer();
            //chorner: we should hit an indexed column, * will likely tablescan
            sql.append("SELECT COUNT(*) as cnt");
            sqlBuilder.sqlFrom(sql, typeName);
            sqlBuilder.sqlWhere(sql, preFilter); 
            
            LOGGER.finer("SQL: " + sql);

            Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql.toString());
            results.next();

            int count = results.getInt("cnt");
            results.close();
            statement.close();

            return count;
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, transaction, sqlException);
            conn = null;
            throw new DataSourceException("Could not count "
                + query.getHandle(), sqlException);
        } catch (SQLEncoderException e) {
            // could not encode count
            // but at least we did not break the connection
            return -1;
        } finally {
            JDBCUtils.close(conn, transaction, null);
        }
    }

    /**
     * Retrieve FeatureType represented by this FeatureSource
     *
     * @return FeatureType for FeatureSource
     *
     * @see org.geotools.data.FeatureSource#getSchema()
     */
    public SimpleFeatureType getSchema() {
        return featureType;
    }

    protected Connection getConnection() throws IOException {
        return getJDBCDataStore().getConnection(getTransaction());
    }

    protected void close(Connection conn, Transaction trans, SQLException sqle) {
        JDBCUtils.close(conn, trans, sqle);
    }

    protected void close(ResultSet rs) {
        JDBCUtils.close(rs);
    }

    protected void close(Statement statement) {
        JDBCUtils.close(statement);
    }
    
    /**
     * By default, only detached feature is supported
     */
    
     public Set getSupportedHints() {
            return dataStore.getSupportedHints();
     }

     /**
      * Returns the default jdbc query capabilities, subclasses should 
      * override to advertise their specific capabilities where they differ.
      * <p>
      * For instance, the capabilities returned:
      * <ul>
      * <li>Does not support startIndex
      * <li>Supports sorting by any attribute in the feature type. Be sure to override
      * if that's not the case, or you also support sorting by @id, SortBy.NATURAL_ORDER,
      * or SortBy.REVERSE_ORDER.
      * </ul>
      * </p>
      * 
      * @see FeatureSource#getQueryCapabilities()
      */
    public QueryCapabilities getQueryCapabilities() {
        return queryCapabilities;
    }
}
