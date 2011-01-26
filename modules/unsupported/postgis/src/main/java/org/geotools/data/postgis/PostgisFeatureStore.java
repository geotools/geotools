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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;
import org.geotools.data.jdbc.JDBCDataStore;
import org.geotools.data.jdbc.JDBCFeatureStore;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.filter.FidFilter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.SQLEncoderException;
import org.geotools.filter.SQLUnpacker;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;

/**
 * Implementation of a Postgis specific FeatureStore.
 * <p>
 * This mostly just rips off code from PostgisDataSource
 * It could definitely use some nice code reuse with PostgisDataStore, as they
 * have a number of similar if not identical methods right now.
 * <p>
 * Approaching deadlines, however, mean that
 * we're sticking with the code that works, instead of getting all kinds of
 * nice reuse.  This'll hopefully change.  This bypasses the writers used in
 * JDBCFeatureStore, as I'm just not yet confident in them.  We  also should
 * do some solid tests to see which is actually faster.
 *
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @task HACK: too little code reuse with PostgisDataStore.
 * @task TODO: make individual operations truly atomic.  If the transaction is
 *       an auto-commit one, then it should make a a new jdbc transaction that
 *       rollsback if there are errors while performing its action.
 */
public class PostgisFeatureStore extends JDBCFeatureStore {
    /** The logger for the postgis module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");

    /** Well Known Text writer (from JTS). */
    protected static WKTWriter geometryWriter = new WKTWriter();

    /** Factory for producing geometries (from JTS). */
    protected static GeometryFactory geometryFactory = new GeometryFactory();

    /** Well Known Text reader (from JTS). */
    protected static WKTReader geometryReader = new WKTReader(geometryFactory);

    /** Error message prefix for sql connection errors */
    protected static final String CONN_ERROR = "Some sort of database connection error: ";

    /** To create the sql where statement */
    protected PostgisSQLBuilder sqlBuilder;
    protected String tableName;

    /** the name of the column to use for the featureId */
    protected FIDMapper fidMapper;

    public PostgisFeatureStore(final PostgisDataStore postgisDataStore, final SimpleFeatureType featureType)
        throws IOException {
        super(postgisDataStore, featureType);
        tableName = featureType.getTypeName();
        fidMapper = postgisDataStore.getFIDMapper(tableName);
        sqlBuilder = (PostgisSQLBuilder) postgisDataStore.getSqlBuilder(tableName);

        AttributeDescriptor geomType = featureType.getGeometryDescriptor();

        /**
         * Override to indicate we support offset, and natural and reverse order sorting
         */
        queryCapabilities = new JDBCQueryCapabilities(featureType){
            @Override
            public boolean isOffsetSupported(){
                return true;
            }
            
            @Override
            protected boolean supportsNaturalOrderSorting() {
                return true;
            }

            @Override
            protected boolean supportsReverseOrderSorting() {
                return true;
            }
            
            @Override
            public boolean isReliableFIDSupported() {
                //parent implementation does same thing but we already have fid mapper handy
                // so might as well save a lookup
                return !isNullFidMapper(fidMapper);
            }
        };
    }

    protected int getSRID(String geomName) throws IOException {
        return getPostgisDataStore().getSRID(tableName, geomName);
    }

    /**
     * Adds quotes to an object for storage in postgis.  The object should be a
     * string or a number.  To perform an insert strings need quotes around
     * them, and numbers work fine with quotes, so this method can be called
     * on unknown objects.
     *
     * @param value The object to add quotes to.
     *
     * @return a string representation of the object with quotes.
     */
    private String addQuotes(Object value) {
        String retString;

        if (value != null) {
            retString = "'" + value.toString() + "'";
        } else {
            retString = "null";
        }

        return retString;
    }

    /**
     * Removes the features specified by the passed filter from the PostGIS
     * database.
     *
     * @param filter An OpenGIS filter; specifies which features to remove.
     *
     * @throws IOException If anything goes wrong or if deleting is not
     *         supported.
     * @throws DataSourceException DOCUMENT ME!
     */
    public void removeFeatures(Filter filter) throws IOException {
        String sql = "";
        String whereStmt = null;

        // check locks!
        // (won't do anything if we use our own
        // database locking)
        assertFilter(filter);

        //boolean previousAutoCommit = getAutoCommit();
        //setAutoCommit(false);
        Filter encodableFilter = sqlBuilder.getPreQueryFilter(filter);
        Filter unEncodableFilter = sqlBuilder.getPostQueryFilter(filter);

        PreparedStatement statement = null;
        Connection conn = null;

        try {
            conn = getConnection();

            if (encodableFilter == null && unEncodableFilter != null) {
                encodableFilter = getEncodableFilter(unEncodableFilter);
            }

            if (encodableFilter != null) {
                StringBuffer sb = new StringBuffer();
                sb.append("DELETE FROM");
                sb.append(sqlBuilder.encodeTableName(tableName));
                sb.append(" WHERE ");
                sqlBuilder.encode(sb, encodableFilter);
                sql = sb.toString();

                //do actual delete
                LOGGER.fine("sql statment is " + sql);
                DefaultQuery query=new DefaultQuery(getSchema().getTypeName(), filter);
                ReferencedEnvelope bounds=bounds(query);
                statement = conn.prepareStatement(sql);
                statement.executeUpdate();
                
                if( bounds!=null && !bounds.isNull())
	    			getJDBCDataStore().listenerManager.fireFeaturesRemoved(getSchema().getTypeName(),
	    					getTransaction(), bounds, false);

            }

            close(statement);
        } catch (SQLException sqle) {
            close(conn, getTransaction(), sqle);

            String message = CONN_ERROR + sqle.getMessage();
            LOGGER.warning(message);
            throw new DataSourceException(message, sqle);
        } catch (SQLEncoderException ence) {
            String message = "error encoding sql from filter " + ence.getMessage();
            LOGGER.warning(message);

            throw new DataSourceException(message, ence);
        } catch (IllegalAttributeException iae) {
            throw new DataSourceException("attribute problem", iae);
        } finally {
            close(statement);
            close(conn, getTransaction(), null);
        }
    }

    /**
     * Modifies the passed attribute types with the passed objects in all
     * features that correspond to the passed OGS filter.
     *
     * @param type The attributes to modify.
     * @param value The values to put in the attribute types.
     * @param filter An OGC filter to note which attributes to modify.
     *
     * @throws IOException If modificaton is not supported, if the attribute
     *         and object arrays are not eqaul length, or if the object types
     *         do not match the attribute types.
     * @throws DataSourceException DOCUMENT ME!
     *
     * @task REVISIT: validate values with types.  Database does this a bit
     *       now, but should be more fully implemented.
     * @task REVISIT: do some nice prepared statement stuff like oracle.
     */
    public void modifyFeatures(AttributeDescriptor[] type, Object[] value, Filter filter)
        throws IOException {
        // check locks!
        // (won't do anything if we use our own
        // database locking)
        LOGGER.finer("asserting filter " + filter);
        assertFilter(filter);

        //boolean previousAutoCommit = getAutoCommit();
        //setAutoCommit(false);
        boolean fail = false;
        Connection conn = null;
        PreparedStatement statement = null;
        String fid = null;

        //check schema with filter???
//        SQLUnpacker unpacker = new SQLUnpacker(encoder.getCapabilities());
//        unpacker.unPackOR(filter);

        String whereStmt = null;
        Filter encodableFilter = sqlBuilder.getPreQueryFilter(filter);
        Filter unEncodableFilter = sqlBuilder.getPostQueryFilter(filter);

        try {
            conn = getConnection();

            if (encodableFilter == null && unEncodableFilter != null) {
                FidFilter fidFilter = getEncodableFilter(unEncodableFilter);
                encodableFilter = fidFilter;
            }

            if (encodableFilter != null) {
                StringBuffer sb = new StringBuffer();
                sqlBuilder.encode(sb, encodableFilter);
                whereStmt = "WHERE " + sb;
                final String sql = makeModifySqlStatement(type, value, whereStmt);
                statement = conn.prepareStatement(sql);
                LOGGER.finer("encoded modify is " + sql);
                setModifyPreparedStatementValues(statement, type, value);

                LOGGER.finer("encoded modify is " + sql);
                DefaultQuery query=new DefaultQuery(getSchema().getTypeName(), filter);
                ReferencedEnvelope bounds=bounds(query);
                statement.executeUpdate();
                if (bounds!=null && !bounds.isNull()) {
                    ReferencedEnvelope afterBounds = bounds(query);
                    if(afterBounds != null)
                        bounds.expandToInclude(afterBounds);
                } else {
                    bounds=bounds(query);
                }
                if (bounds!=null && !bounds.isNull())
	    			getJDBCDataStore().listenerManager.fireFeaturesChanged(getSchema().getTypeName(),
	    					getTransaction(), bounds, false);
            }

        } catch (SQLException sqle) {
            fail = true;
            close(conn, getTransaction(), sqle);

            String message = CONN_ERROR + sqle.getMessage();
            LOGGER.warning(message);
            throw new DataSourceException(message, sqle);
        } catch (SQLEncoderException ence) {
            fail = true;

            String message = "error encoding sql from filter " + ence.getMessage();
            LOGGER.warning(message);
            throw new DataSourceException(message, ence);
        } catch (IllegalAttributeException iae) {
            throw new DataSourceException("attribute problem", iae);
        } finally {
            close(statement);
            close(conn, getTransaction(), null);
        }
    }

	private FidFilter getEncodableFilter(Filter unEncodableFilter)
        throws IOException, FactoryRegistryException, IllegalAttributeException {
        // this is very similar to getFidSet - the reason is so that we
        // don't spend time constructing geometries when we don't need
        // to, but we probably could get some better code reuse.
        DefaultQuery query = new DefaultQuery();
        query.setPropertyNames(new String[0]);
        query.setFilter(unEncodableFilter);

        SimpleFeatureCollection features = getFeatures(unEncodableFilter);

        FilterFactory ff = FilterFactoryFinder.createFilterFactory();
        FidFilter fidFilter = ff.createFidFilter();
        SimpleFeatureIterator it = features.features();
        try {
            while( it.hasNext() ) {
                SimpleFeature feature = (SimpleFeature) it.next();
                fidFilter.addFid(feature.getID());
            }
        } finally {
          features.close( it );  
        }
        return fidFilter;
    }

    /**
     * strips the tableName from the fid for those in the format
     * featureName.3534 should maybe just strip out all alpha-numeric
     * characters.
     *
     * @param feature The feature for which the fid number should be stripped.
     *
     * @return The fid without the leading tablename.
     */
    private String formatFid(SimpleFeature feature) {
        String fid = feature.getID();

        if (fid.startsWith(tableName)) {
            //take out the tableName and the .
            fid = fid.substring(tableName.length() + 1);
        }

        return addQuotes(fid);
    }

    /**
     * Modifies the passed attribute types with the passed objects in all
     * features that correspond to the passed OGS filter.  A convenience
     * method for single attribute modifications.
     *
     * @param type The attributes to modify.
     * @param value The values to put in the attribute types.
     * @param filter An OGC filter to note which attributes to modify.
     *
     * @throws IOException If modificaton is not supported, if the object type
     *         do not match the attribute type.
     */
    public void modifyFeatures(AttributeDescriptor type, Object value, Filter filter)
        throws IOException {
        AttributeDescriptor[] singleType = { type };
        Object[] singleVal = { value };
        modifyFeatures(singleType, singleVal, filter);
    }

    /**
     * Builds an UPDATE sql statement suitable to be used to create a
     * PreparedStatement.
     * <p>
     * The form of the returned statement is like
     * <code>UPDATE tableName SET att = ?, att2 = ?</code>. Note the use of
     * the
     * <code>?<code> value placeholder. The number of <code>?<code> value place holders
     * in the returned statement will be equal to the number of <code>types</code> and <code>values</code>
     * passed as arguments.
     * </p>
     * 
     * @param types
     *            the attributes to be changed.
     * @param values
     *            the values to change them to, forming a one to one mapping with <code>types</code>.
     * @param whereStmt
     *            the feature to update.
     * 
     * @return an update sql statement to be used in a PreparedStatement.
     * 
     * @throws IOException
     *             if the lengths of types and values don't match.
     * @see #setModifyPreparedStatementValues
     */
    private String makeModifySqlStatement(AttributeDescriptor[] types, Object[] values, String whereStmt)
            throws IOException {
        final int arrLength = types.length;
        if (arrLength != values.length) {
            throw new IllegalArgumentException("length of value array is not " + "same length as type array");
        }
        StringBuffer sqlStatement = new StringBuffer("UPDATE ");
        sqlStatement.append(sqlBuilder.encodeTableName(tableName) + " SET ");

        for (int i = 0; i < arrLength; i++) {
            AttributeDescriptor curType = types[i];
            sqlStatement.append(sqlBuilder.encodeColumnName(curType.getLocalName()));
            sqlStatement.append(" = ");

            if (curType instanceof GeometryDescriptor) {
                //create a placeholder where to set the geometry as WKT
                int srid = getSRID(curType.getLocalName());
                sqlStatement.append("GeometryFromText( ?, ").append(srid).append(")");
            } else {
                sqlStatement.append("?");
            }

            sqlStatement.append((i < (arrLength - 1)) ? ", " : " ");
        }

        sqlStatement.append(whereStmt);
        sqlStatement.append(";");

        return sqlStatement.toString();        
    }

    /**
     * Sets the values corresponding to the PreparedStatement placeholders.
     * <p>
     * The number of <code>"?"</code> placeholders in the statement, the number of attributes
     * in <code>types</code> and the numbers of <code>values</code> shall be the same.
     * </p>
     * <p>
     * This method is intended to be used after creating a prepared statement through
     * {@link #makeModifySqlStatement} in order to set the statement values.
     * </p>
     * 
     * @param types
     *            the attribute to be changed.
     * @param values
     *            the value to change it to.
     * @param whereStmt
     *            the feature to update.
     * 
     * @return an update sql statement.
     * 
     * @throws IOException
     *             if the lengths of types and values don't match.
     * @throws SQLException 
     */
    private void setModifyPreparedStatementValues(PreparedStatement statement,
            AttributeDescriptor[] types, Object[] values) throws IOException, SQLException {
        final int arrLength = types.length;
        if (arrLength != values.length) {
            throw new IllegalArgumentException("length of value array is not "
                    + "same length as type array");
        }

        final PostgisDataStore dataStore = getPostgisDataStore();
        for (int i = 0; i < arrLength; i++) {
            Object newValue = values[i];

            AttributeDescriptor curType = types[i];
            if (curType instanceof GeometryDescriptor) {
                newValue = geometryWriter.write((Geometry) newValue);
                // prepStatement indexing starts at 1...
                statement.setObject(1 + i, newValue);
            }else{
                //Get the jdbc column type
                final Class target = curType.getType().getBinding();
                final int jdbcType = dataStore.getJdbcType(target).intValue();
                // prepStatement indexing starts at 1...
                statement.setObject(1 + i, newValue, jdbcType);
            }
        }
    }

    /**
     * Performs the setFeautres operation by removing all and then adding the
     * full collection.  This is not efficient, the add, modify and  remove
     * operations should be used instead, this is just to follow the
     * interface.
     *
     * @return DOCUMENT ME!
     *
     * @task REVISIT: to abstract class, same as oracle.
     */

    /*public void setFeatures(FeatureCollection features)
       throws IOException {
       boolean originalAutoCommit = getAutoCommit();
       setAutoCommit(false);
       removeFeatures(null);
       addFeatures(features);
       //commit();
       //setAutoCommit(originalAutoCommit);
       }*/
    protected PostgisDataStore getPostgisDataStore() {
        return (PostgisDataStore) super.getJDBCDataStore();
    }

    /**
     * Creates a SQL statement for the PostGIS database.
     * 
     * @deprecated please use makeSql(query) 
     * @param unpacker the object to get the encodable filter.
     * @param query the getFeature query - for the tableName, properties and
     *        maxFeatures.
     * @throws IOException if there are problems encoding the sql.
     */
    public String makeSql(SQLUnpacker unpacker, Query query) throws IOException {
        //one to one relationship for now, so typeName is not used.
        //String tableName = query.getTypeName();

        boolean useLimit = (unpacker.getUnSupported() == null);
        Filter filter = unpacker.getSupported();
        LOGGER.fine("Filter in making sql is " + filter);

        AttributeDescriptor[] attributeTypes = getAttTypes(query);
        int numAttributes = attributeTypes.length;

        StringBuffer sqlStatement = new StringBuffer("SELECT ");
        if (!fidMapper.returnFIDColumnsAsAttributes()) {
            for (int i = 0; i < fidMapper.getColumnCount(); i++) {
                sqlStatement.append(fidMapper.getColumnName(i));
                if (numAttributes > 0 || i < (fidMapper.getColumnCount() - 1))
                    sqlStatement.append(", ");
            }
        }

        LOGGER.finer("making sql for " + numAttributes + " attributes");

        for (int i = 0; i < numAttributes; i++) {
            String curAttName = attributeTypes[i].getLocalName();

            if (Geometry.class.isAssignableFrom(attributeTypes[i].getType().getBinding())) {
                sqlStatement.append(", AsText(force_2d(\"" + curAttName + "\"))");
            } else {
                sqlStatement.append(", \"" + curAttName + "\"");
            }
        }

        String where = "";

        if (filter != null) {
            try {
                StringBuffer sb = new StringBuffer();
                sqlBuilder.encode(sb, filter);
                where = "WHERE " + sb.toString();
            } catch (SQLEncoderException sqle) {
                String message = "Encoder error" + sqle.getMessage();
                LOGGER.warning(message);
                throw new DataSourceException(message, sqle);
            }
        }

        //int limit = HARD_MAX_FEATURES;
        String limit = "";

        if (useLimit) {
            limit = " LIMIT " + query.getMaxFeatures();
        }

        sqlStatement.append(" FROM \"" + tableName + "\" " + where + limit + ";").toString();
        LOGGER.fine("sql statement is " + sqlStatement);

        return sqlStatement.toString();
    }
    

    /**
     * Gets the attribute types from the query.  If all are requested then
     * returns all attribute types of this query.  If only certain
     * propertyNames are requested then this returns the correct attribute
     * types, throwing an exception is they can not be found.
     *
     * @param query contains the propertyNames.
     *
     * @return the array of attribute types to be returned by getFeature.
     *
     * @throws IOException if query contains a propertyName that is not a part
     *         of this type's schema.
     */
    private AttributeDescriptor[] getAttTypes(Query query) throws IOException {
        AttributeDescriptor[] schemaTypes = (AttributeDescriptor[]) getSchema().getAttributeDescriptors().toArray(new AttributeDescriptor[getSchema().getAttributeDescriptors().size()]);

        if (query.retrieveAllProperties()) {
            return schemaTypes;
        } else {
            List attNames = Arrays.asList(query.getPropertyNames());
            AttributeDescriptor[] retAttTypes = new AttributeDescriptor[attNames.size()];
            int retPos = 0;

            for (int i = 0, n = schemaTypes.length; i < n; i++) {
                String schemaTypeName = schemaTypes[i].getLocalName();

                if (attNames.contains(schemaTypeName)) {
                    retAttTypes[retPos++] = schemaTypes[i];
                }
            }

            //TODO: better error reporting, and completely test this method.
            if (attNames.size() != retPos) {
                String msg =
                    "attempted to request a property, "
                        + attNames.get(0)
                        + " that is not part of the schema ";
                throw new IOException(msg);
            }

            return retAttTypes;
        }
    }

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
        return bounds(query);
    }

    // TODO: change this so that it queries for just the bbox instead of the entire sub-query schema columns!
    //        (this is harder than you might think because of filter requirements!)
    //
    protected ReferencedEnvelope bounds(Query query) throws IOException {
        Filter filter = query.getFilter();

        if (filter == Filter.EXCLUDE) {
            return new ReferencedEnvelope(new ReferencedEnvelope(), query.getCoordinateSystem());
        }

        SimpleFeatureType schema = getSchema();
        JDBCDataStore jdbc = (JDBCDataStore)getJDBCDataStore();
        SQLBuilder sqlBuilder = jdbc.getSqlBuilder(schema.getTypeName());

        Filter postQueryFilter = sqlBuilder.getPostQueryFilter(query.getFilter());
        if (postQueryFilter != null && !postQueryFilter.equals(Filter.INCLUDE)) {
            // this would require postprocessing the filter
            // so we cannot optimize
            return null;
        }

        Connection conn = null;

        try {
            conn = getConnection();

            ReferencedEnvelope retEnv = new ReferencedEnvelope();
            Filter preFilter = sqlBuilder.getPreQueryFilter(query.getFilter());
            AttributeDescriptor[] attributeTypes = (AttributeDescriptor[]) schema.getAttributeDescriptors().toArray(new AttributeDescriptor[schema.getAttributeDescriptors().size()]);
            SimpleFeatureType schemaNew = schema;
            	//DJB: this should ensure that schema has a geometry in it or the bounds query has no chance of working
			if(!query.retrieveAllProperties()) {
				try {
                    schemaNew = DataUtilities.createSubType(schema, query.getPropertyNames());
                    if (schemaNew.getGeometryDescriptor() == null)  // does the sub-schema have a geometry in it?
                    {
                    	//uh-oh better get one!
                    	if (schema.getGeometryDescriptor() != null)  // does the entire schema have a geometry in it? 
                    	{
                    		//buff-up the sub-schema so it has the default geometry in it.
	                    	ArrayList al = new ArrayList (Arrays.asList(query.getPropertyNames()));
	                    	al.add(schema.getGeometryDescriptor().getLocalName());
	                    	schemaNew = DataUtilities.createSubType(schema, (String[]) al.toArray(new String[1]) );       
                    	}
                    }
                } catch (SchemaException e1) {
                    throw new DataSourceException("Could not create subtype", e1);
                }
			}
			 // at this point, the query should have a geometry in it. 
			 // BUT, if there's no geometry in the table, then the query will not (obviously) have a geometry in it.
			 
			 attributeTypes = (AttributeDescriptor[]) schemaNew.getAttributeDescriptors().toArray(new AttributeDescriptor[schema.getAttributeDescriptors().size()]);
				 
            for (int j = 0, n = schemaNew.getAttributeCount(); j < n; j++) {
                if (Geometry.class.isAssignableFrom(attributeTypes[j].getType().getBinding())) // same as .isgeometry() - see new featuretype javadoc
                {
                    String attName = attributeTypes[j].getLocalName();
                    ReferencedEnvelope curEnv = getEnvelope(conn, attName, sqlBuilder, filter);

                    if (curEnv == null) {
                        return null;
                    }

                    retEnv.expandToInclude(curEnv);
                }
            }

            LOGGER.finer("returning bounds " + retEnv);
            
            // handle reprojection and crs forcing
            CoordinateReferenceSystem base = null;
            if(query.getCoordinateSystem() != null)
                base = query.getCoordinateSystem();
            else if(schemaNew.getGeometryDescriptor() != null)
                base = schemaNew.getGeometryDescriptor().getCoordinateReferenceSystem();
            CoordinateReferenceSystem dest = query.getCoordinateSystemReproject();
            
            ReferencedEnvelope result = new ReferencedEnvelope(retEnv, base);
            if(base != null && dest != null)
                result = result.transform(dest, true);
            return result;
        } catch (SQLException sqlException) {
            JDBCUtils.close(conn, transaction, sqlException);
            conn = null;
            throw new DataSourceException("Could not count " + query.getHandle(), sqlException);
        } catch (SQLEncoderException e) {
            // could not encode count
            // but at least we did not break the connection
            return null;
        } catch (ParseException parseE) {
            String message = "Could not read geometry: " + parseE.getMessage();

            return null;
        } catch (FactoryException e) {
            throw new DataSourceException("Could not reproject", e);
        }  catch (TransformException e) {
            throw new DataSourceException("Could not reproject", e);
        } finally {
            JDBCUtils.close(conn, transaction, null);
        }
    }

    //REVISIT: do we want maxFeatures here too?  If we don't have maxFeatures then the answer
    //is still always going to be right (and guaranteed to be right, as opposed to two selects
    // that could be slightly different).  And the performance hit shouldn't be all that much.
    protected ReferencedEnvelope getEnvelope(
        Connection conn,
        String geomName,
        SQLBuilder sqlBuilder,
        Filter filter)
        throws SQLException, SQLEncoderException, IOException, ParseException {
        String typeName = getSchema().getTypeName();
        
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT AsText(force_2d(Envelope(" );
        
        //check if we can apply the estimated_extent optimization
        boolean useEstimatedExtent = ( filter == null || filter == Filter.INCLUDE ) 
        	&& ((PostgisDataStore)getDataStore()).isEstimatedExtent();
        
        if ( useEstimatedExtent ) {
        	sql.append("estimated_extent(");	
        	sql.append("'" + typeName + "','" + geomName + "'))));");
        }
        else {
        	sql.append("Extent(\"" + geomName + "\")))) " );
        	sqlBuilder.sqlFrom(sql, typeName);
            sqlBuilder.sqlWhere(sql, filter);
        }
        
      
        LOGGER.fine("SQL: " + sql);

        Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sql.toString());
        results.next();

        String wkt = results.getString(1);
        ReferencedEnvelope retEnv = null;

        if (wkt == null) {
            return null;
        } else {
            retEnv = ReferencedEnvelope.reference(geometryReader.read(wkt).getEnvelopeInternal());
        }

        results.close();
        statement.close();

        return retEnv;
    }	
    
    
}
