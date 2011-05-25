/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureListenerManager;
import org.geotools.data.FeatureLockException;
import org.geotools.data.FeatureReader;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Geometry;


/**
 * An abstract class that uses sql statements to insert, update and delete
 * features from the database. Useful when the resultset got from the database
 * is not updatable, for example.
 *
 * @task TODO: Use prepared statements for inserts.  Jody says that oracle
 *             at least will perform faster, and I imagine postgis will
 *             too.  This will require a bit of rearchitecture, since the
 *             statement should just be made once, right now even if there
 *             were many features coming in they would all have to make
 *             a new prepared statement - should be able to do it before
 *             and then just fill it up for each feature.  And for oracle
 *             Jody has some convenience methods in his SDO stuff that
 *             works with prepared statements and STRUCTS directly.
 *             See http://jira.codehaus.org/browse/GEOT-219 (close when done).
 *
 * @author Andrea Aime
 * @author chorner
 *
 * @source $URL$
 * @version $Id$
 * 
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public abstract class JDBCTextFeatureWriter extends JDBCFeatureWriter {
    /** The logger for the jdbc module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.jdbc");
    protected FIDMapper mapper = null;

    /** indicates the lock attempt is in progress */
    final int STATE_WAIT = 1;

    /** indicates the lock attempt was successful */
    final int STATE_SUCCESS = 2;

    /** indicates the lock attempt failed horribly */
    final int STATE_FAILURE = 3;
    private FeatureListenerManager listenerManager;
    
    /**
     * Creates a new instance of JDBCFeatureWriter
     *
     * @param fReader
     * @param queryData
     *
     * @throws IOException
     */
    public JDBCTextFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> fReader, QueryData queryData)
        throws IOException {
        super(fReader, queryData);
        mapper = queryData.getMapper();
        listenerManager = queryData.getListenerManager();
    }

    /**
     * Override that uses sql statements to perform the operation.
     *
     * @see org.geotools.data.jdbc.JDBCFeatureWriter#doInsert(org.geotools.data.jdbc.MutableFIDFeature)
     */
    protected void doInsert(MutableFIDFeature current)
        throws IOException, SQLException {
    	if (LOGGER.isLoggable(Level.FINE)) 
    		LOGGER.fine("inserting into postgis feature " + current);

        Statement statement = null;
        Connection conn = null;

        try {
            conn = queryData.getConnection();
            statement = conn.createStatement();

            String sql = makeInsertSql(current);
            if (LOGGER.isLoggable(Level.FINE)) 
            	LOGGER.fine(sql);
            statement.executeUpdate(sql);

            // should the ID be generated during an insert, we need to read it back
            // and set it into the feature
            if (((mapper.getColumnCount() > 0)
                && mapper.hasAutoIncrementColumns())) {
//          if (((mapper.getColumnCount() > 0))) {
                current.setID(mapper.createID(conn, current, statement));
            }
        } catch (SQLException sqle) {
            String msg = "SQL Exception writing geometry column" + sqle.getLocalizedMessage();
            LOGGER.log(Level.SEVERE, msg, sqle);
            queryData.close(sqle);
            throw new DataSourceException(msg, sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    String msg = "Error closing JDBC Statement";
                    LOGGER.log(Level.WARNING, msg, e);
                }
            }
        }
    }

    /**
     * Creates a sql insert statement.  Uses each feature's schema, which makes
     * it possible to insert out of order, as well as inserting less than all
     * features.
     *
     * @param feature the feature to add.
     *
     * @return an insert sql statement.
     *
     * @throws IOException
     */
    protected String makeInsertSql(SimpleFeature feature) throws IOException {
        FeatureTypeInfo ftInfo = queryData.getFeatureTypeInfo();
        SimpleFeatureType featureType = ftInfo.getSchema();

        String tableName = encodeName(featureType.getTypeName());
        List<AttributeDescriptor> attributeTypes = featureType.getAttributeDescriptors();

        String attrValue;

        StringBuffer statementSQL = new StringBuffer("INSERT INTO " + tableName
                + " (");

        // either add statements to append non autoincrement colums, or gather
        // the auto-increment ones
        Set autoincrementColumns = null;
        if (!mapper.returnFIDColumnsAsAttributes()) {
            autoincrementColumns = Collections.EMPTY_SET;
            for (int i = 0; i < mapper.getColumnCount(); i++) {
                if (!(mapper.isAutoIncrement(i) && feature.getAttribute(mapper.getColumnName(i)) == null)) {
                    statementSQL.append(mapper.getColumnName(i)).append(",");
                }
            }
        } else {
            autoincrementColumns = new HashSet();
            for (int i = 0; i < mapper.getColumnCount(); i++) {
                if (mapper.isAutoIncrement(i)) {
                    autoincrementColumns.add(mapper.getColumnName(i));
                }
            }
        }

        // encode insertion for attributes, but remember to avoid auto-increment ones, 
        // they may be included in the feature type as well
        for (int i = 0; i < attributeTypes.size(); i++) {
            String attName = attributeTypes.get(i).getLocalName();
            if(!autoincrementColumns.contains(attName) || feature.getAttribute(attName) != null) {
                String colName = encodeColumnName(attName);
                statementSQL.append(colName).append(",");
            }
        }

        statementSQL.setCharAt(statementSQL.length() - 1, ')');
        statementSQL.append(" VALUES (");

        if (!mapper.returnFIDColumnsAsAttributes()
                && !mapper.hasAutoIncrementColumns()) {
            String FID = mapper.createID(queryData.getConnection(), feature,
                    null);
            if( current instanceof MutableFIDFeature ){
                ((MutableFIDFeature)current).setID(FID);
            }
            Object[] primaryKey = mapper.getPKAttributes(FID);

            for (int i = 0; i < primaryKey.length; i++) {
                if (!mapper.isAutoIncrement(i) || primaryKey[i] != null) {
                    attrValue = addQuotes(primaryKey[i]);
                    statementSQL.append(attrValue).append(",");
                }
            }
        }

        Object[] attributes = feature.getAttributes().toArray();

        for (int i = 0; i < attributeTypes.size(); i++) {
            attrValue = null;
            if (attributeTypes.get(i) instanceof GeometryDescriptor) {
                GeometryDescriptor descriptor = (GeometryDescriptor) attributeTypes.get(i);
                String geomName = attributeTypes.get(i).getLocalName();
                int srid = ftInfo.getSRID(geomName);
                Geometry geometry = (Geometry) attributes[i];
                
                int dimension = 2;
                if(descriptor.getUserData().get(Hints.COORDINATE_DIMENSION) instanceof Integer) {
                    dimension = (Integer) descriptor.getUserData().get(Hints.COORDINATE_DIMENSION);
                } else if(descriptor.getCoordinateReferenceSystem() != null) {
                    dimension = descriptor.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
                }
                
                if( geometry==null ){
                    attrValue="NULL";
                }else
                    attrValue = getGeometryInsertText(geometry, srid, dimension);
            } else {
                if(!autoincrementColumns.contains(attributeTypes.get(i).getLocalName()) || attributes[i] != null)
                    attrValue = addQuotes(attributes[i]);
            }

            if(attrValue != null)
                statementSQL.append(attrValue + ",");
        }

        statementSQL.setCharAt(statementSQL.length() - 1, ')');

        return (statementSQL.toString());
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
    protected String addQuotes(Object value) {
        String retString;

        if (value != null) {
            if(value instanceof Number)
                retString = value.toString();
            else
                retString = "'" + doubleQuote(value) + "'";
        } else {
            retString = "null";
        }

        return retString;
    }

    String doubleQuote(Object obj) {
        return obj.toString().replaceAll("'", "''");
    }

    /**
     * Turns a geometry into the textual version needed for the sql statement
     *
     * @param geom
     * @param srid
     *
     */
    protected abstract String getGeometryInsertText(Geometry geom, int srid) throws IOException;
    
    /**
     * Turns a geometry into the textual version needed for the sql statement
     *
     * @param geom
     * @param srid
     * @param dimension
     */
    protected String getGeometryInsertText(Geometry geom, int srid, int dimension) throws IOException {
        return getGeometryInsertText(geom, srid);
    }

    /**
     * Override that uses sql statements to perform the operation.
     *
     * @see org.geotools.data.FeatureWriter#remove()
     */
    public void remove() throws IOException {
    	if (LOGGER.isLoggable(Level.FINE)) 
              LOGGER.fine("inserting into postgis feature " + current);

        Statement statement = null;
        Connection conn = null;

        try {
            conn = queryData.getConnection();
            statement = conn.createStatement();
            ReferencedEnvelope bounds = ReferencedEnvelope.reference(this.live.getBounds());
            String sql = makeDeleteSql(current);
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine(sql);
            
            //System.out.println(sql);
            statement.executeUpdate(sql);

            listenerManager.fireFeaturesRemoved(getFeatureType()
                                                    .getTypeName(),
                queryData.getTransaction(), bounds, false);
        } catch (SQLException sqle) {
            String msg = "SQL Exception writing geometry column";
            LOGGER.log(Level.SEVERE, msg, sqle);
            queryData.close(sqle);
            throw new DataSourceException(msg, sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    String msg = "Error closing JDBC Statement";
                    LOGGER.log(Level.WARNING, msg, e);
                }
            }
        }
    }

    /**
     * Generates the query for the sql delete statement
     *
     * @param feature
     *
     *
     * @throws IOException
     */
    protected String makeDeleteSql(SimpleFeature feature) throws IOException {
        FeatureTypeInfo ftInfo = queryData.getFeatureTypeInfo();
        SimpleFeatureType fetureType = ftInfo.getSchema();

        String tableName = encodeName(fetureType.getTypeName());

        StringBuffer statementSQL = new StringBuffer("DELETE FROM " + tableName
                + " WHERE ");
        Object[] pkValues = mapper.getPKAttributes(feature.getID());

        for (int i = 0; i < mapper.getColumnCount(); i++) {
            statementSQL.append( encodeColumnName( mapper.getColumnName(i) )).append(" = ").append(addQuotes(
                    pkValues[i]));

            if (i < (mapper.getColumnCount() - 1)) {
                statementSQL.append(" AND ");
            }
        }

        return (statementSQL.toString());
    }

    /**
     * Override that uses sql statements to perform the operation.
     *
     * @see org.geotools.data.jdbc.JDBCFeatureWriter#doUpdate(org.geotools.feature.Feature,
     *      org.geotools.feature.Feature)
     */
    protected void doUpdate(SimpleFeature live, SimpleFeature current)
        throws IOException, SQLException {
        
    	if (LOGGER.isLoggable(Level.FINE)) 
             LOGGER.fine("updating postgis feature " + current);

        Statement statement = null;
        Connection conn = null;

        try {
            conn = queryData.getConnection();
            statement = conn.createStatement();
            
            boolean hasLock = false;
            String sql = makeSelectForUpdateSql(current);
            if (sql == null) {
                LOGGER.fine("Lock acquisition not attempted, JDBCTextFeatureWriter may block during concurrent updates");
            } else { //we have a statement, let's use it
                ResultSet result = null;
                try {
                    result = statement.executeQuery(sql);
                    //TODO: read the result
//                    if (result != null) {
//                        System.out.println(result.toString());
//                    }
                    hasLock = true;
                } catch (SQLException e) {
                    LOGGER.severe(e.getLocalizedMessage());
                    throw new FeatureLockException("Your feature is locked!", current.getID(), e); //do not catch
                } finally {
                    if (result != null) {
                        try {
                            result.close();
                        } catch (SQLException e) {}
                        result = null;
                    }
                }
            }
            
            if (sql == null || hasLock) {
                //attempt the update if we have a lock, or we are too lazy to check
                sql = makeUpdateSql(live, current);
                if (LOGGER.isLoggable(Level.FINE)) 
                    LOGGER.fine(sql);
                statement.executeUpdate(sql);
            } else { //shouldn't be called?
                throw new IOException("Feature Lock failed; giving up");
            }
        } catch (SQLException sqle) {
            String msg = "SQL Exception writing geometry column";
            LOGGER.log(Level.SEVERE, msg, sqle);
            queryData.close(sqle);
            throw new DataSourceException(msg, sqle);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    String msg = "Error closing JDBC Statement";
                    LOGGER.log(Level.WARNING, msg, e);
                }
            }
        }
    }

    /**
     * Generate the select for update statement, which will attempt to
     * lock features for update.  This should be overwritten by databases
     * which want to take advantage of this method.
     * 
     * This method is called in a timer thread, to prevent blocking.
     * 
     * @since 2.2.0
     * @param current
     * @return sql string or null
     */
    protected String makeSelectForUpdateSql(SimpleFeature current) {
        return null;
    }
    
    /**
     * Generate the update sql statement
     *
     * @param live
     * @param current
     *
     *
     * @throws IOException
     */
    protected String makeUpdateSql(SimpleFeature live, SimpleFeature current)
        throws IOException {
        FeatureTypeInfo ftInfo = queryData.getFeatureTypeInfo();
        SimpleFeatureType featureType = ftInfo.getSchema();
        AttributeDescriptor[] attributes = (AttributeDescriptor[]) featureType.getAttributeDescriptors().toArray(new AttributeDescriptor[featureType.getAttributeDescriptors().size()]);

        String tableName = encodeName(featureType.getTypeName());

        StringBuffer statementSQL = new StringBuffer("UPDATE " + tableName
                + " SET ");

        for (int i = 0; i < current.getAttributeCount(); i++) {
            Object currAtt = current.getAttribute(i);
            Object liveAtt = live.getAttribute(i);

            if (!DataUtilities.attributesEqual(liveAtt, currAtt)) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.fine("modifying att# " + i + " to " + currAtt);
                }

				String attrValue = null;
				if (attributes[i] instanceof GeometryDescriptor) {
				    String geomName = attributes[i].getLocalName();
				    int srid = ftInfo.getSRID(geomName);
				    Geometry geometry = (Geometry) currAtt;
                    if( geometry == null )
                        attrValue="NULL";
                    else
                        attrValue = getGeometryInsertText(geometry, srid);
				} else {
				    attrValue = addQuotes(currAtt);
				}


				String colName = encodeColumnName(attributes[i].getLocalName());
                statementSQL.append(colName).append(" = ")
                            .append(attrValue).append(", ");
            }
        }

        //erase the last comma
        statementSQL.setLength(statementSQL.length() - 2);
        statementSQL.append(" WHERE ");

        Object[] pkValues = mapper.getPKAttributes(current.getID());

        for (int i = 0; i < mapper.getColumnCount(); i++) {
            statementSQL.append(mapper.getColumnName(i)).append(" = ").append(addQuotes(
                    pkValues[i]));

            if (i < (mapper.getColumnCount() - 1)) {
                statementSQL.append(" AND ");
            }
        }

        return (statementSQL.toString());
    }

    /**
     * This version does not use QueryData udpate/insert/remove methods, but
     * uses separate queries instead
     *
     * @see org.geotools.data.jdbc.JDBCFeatureWriter#useQueryDataForInsert()
     */
    protected boolean useQueryDataForInsert() {
        return false;
    }
}
