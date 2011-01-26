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
package org.geotools.data.geometryless;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.geometryless.attributeio.PointXYAttributeIO;
import org.geotools.data.geometryless.filter.SQLEncoderLocationsXY;
import org.geotools.data.jdbc.JDBCFeatureWriter;
import org.geotools.data.jdbc.QueryData;
import org.geotools.data.jdbc.SQLBuilder;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.data.sql.BypassSqlFeatureTypeHandler;
import org.geotools.data.sql.BypassSqlSQLBuilder;
import org.geotools.feature.AttributeTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Point;

/**
 * An implementation of the GeoTools Data Store API for a generic non-spatial
 * database platform.
 * 
 * This specialisation uses X,Y (lat/lon) database colums to hold point
 * geometries
 * 
 * the constructor is used to pass metadata from datastore to SQLEncoder class
 * 
 * <br>
 * Please see {@link org.geotools.data.jdbc.JDBCDataStore class JDBCDataStore}
 * and {@link org.geotools.data.DataStore interface DataStore} for DataStore
 * usage details.
 * 
 * @author Rob Atkinson rob@socialchange.net.au
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/unsupported/geometryless/src/main/java/org/geotools/data/geometryless/LocationsXYDataStore.java $
 */

public class LocationsXYDataStore extends org.geotools.data.geometryless.JDBCDataStore {
    /** The logger for the mysql module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.geometryless");

    private String XCoordColumnName = null;

    private String YCoordColumnName = null;

    private String geomName = null;

    public LocationsXYDataStore(DataSource connectionPool) throws IOException {
        super(connectionPool);
    }

    /**
     * Constructor for LocationsXYDataStore where the database schema name is
     * provided.
     * 
     * @param connectionPool
     *            a {@link org.geotools.data.jdbc.ConnectionPool ConnectionPool}
     * @param databaseSchemaName
     *            the database schema. Can be null. See the comments for the
     *            parameter schemaPattern in
     *            {@link java.sql.DatabaseMetaData#getTables(String, String, String, String[]) DatabaseMetaData.getTables},
     *            because databaseSchemaName behaves in the same way.
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public LocationsXYDataStore(DataSource connectionPool, String databaseSchemaName,
            String namespace, String x, String y, String geomName) throws IOException {
        // databaseSchemaName can be null
        super(connectionPool, databaseSchemaName, namespace);
        this.XCoordColumnName = x;
        this.YCoordColumnName = y;
        this.geomName = geomName;
    }

    /**
     * Utility method for getting a FeatureWriter for modifying existing
     * features, using no feature filtering and auto-committing. Not used for
     * adding new features.
     * 
     * @param typeName
     *            the feature type name (the table name)
     * @return a FeatureWriter for modifying existing features
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriter(String typeName) throws IOException {
        return getFeatureWriter(typeName, Filter.INCLUDE, Transaction.AUTO_COMMIT);
    }

    /**
     * Utility method for getting a FeatureWriter for adding new features, using
     * auto-committing. Not used for modifying existing features.
     * 
     * @param typeName
     *            the feature type name (the table name)
     * @return a FeatureWriter for adding new features
     * @throws IOException
     *             if the database cannot be properly accessed
     */
    public FeatureWriter<SimpleFeatureType, SimpleFeature> getFeatureWriterAppend(String typeName) throws IOException {
        return getFeatureWriterAppend(typeName, Transaction.AUTO_COMMIT);
    }

    /**
     * Constructs an AttributeType from a row in a ResultSet. The ResultSet
     * contains the information retrieved by a call to getColumns() on the
     * DatabaseMetaData object. This information can be used to construct an
     * Attribute Type.
     * 
     * <p>
     * This simply returns the constructed geometry when the column is the first
     * (X)
     * </p>
     * 
     * <p>
     * Note: Overriding methods must never move the current row pointer in the
     * result set.
     * </p>
     * 
     * @param rs
     *            The ResultSet containing the result of a
     *            DatabaseMetaData.getColumns call.
     * 
     * @return The AttributeType built from the ResultSet.
     * 
     * @throws SQLException
     *             If an error occurs processing the ResultSet.
     * @throws DataSourceException
     *             Provided for overriding classes to wrap exceptions caused by
     *             other operations they may perform to determine additional
     *             types. This will only be thrown by the default implementation
     *             if a type is present that is not present in the
     *             TYPE_MAPPINGS.
     */
    protected AttributeDescriptor buildAttributeType(ResultSet rs) throws IOException {
        final int COLUMN_NAME = 4;
        final int DATA_TYPE = 5;
        final int TYPE_NAME = 6;

        try {
            int dataType = rs.getInt(DATA_TYPE);
            String colName = rs.getString(COLUMN_NAME);
            LOGGER.fine("dataType: " + dataType + " " + rs.getString(TYPE_NAME) + " " + colName);
            Class type = (Class) TYPE_MAPPINGS.get(new Integer(dataType));

            // This should be improved - first should probably check for
            // presence of both the x and y columns, only create the geometry
            // if both are found, instead of just ignoring the y - right now
            // the y could just not exist. And then if either do not exist
            // an exception should be thrown.
            // Also, currently the name of the geometry is hard coded -
            // do we want it to be user configurable? ch
            if (colName.equals(XCoordColumnName)) {
                // do type checking here, during config, not during reading.
                if (Number.class.isAssignableFrom(type)) {
                    return new AttributeTypeBuilder().binding(Point.class).buildDescriptor(geomName);
                } else {
                    String excMesg = "Specified X column of " + colName + " of type: " + type
                            + ", can not be used as x point";
                    throw new DataSourceException(excMesg);
                }

            } else if (colName.equals(YCoordColumnName)) {
                if (Number.class.isAssignableFrom(type)) {
                    return null;
                } else {
                    String excMesg = "Specified X column of " + colName + " of type: " + type
                            + ", can not be used as x point";
                    throw new DataSourceException(excMesg);
                }
            } else {
                return super.buildAttributeType(rs);
            }
        } catch (SQLException e) {
            throw new IOException("SQL exception occurred: " + e.getMessage());
        }
    }

    // public SQLBuilder getSqlBuilder(String typeName) throws IOException {
    //    	
    // SQLEncoderLocationsXY encoder = new
    // SQLEncoderLocationsXY(XCoordColumnName,YCoordColumnName);
    // encoder.setFIDMapper(getFIDMapper(typeName));
    // return new LocationsXYSQLBuilder(encoder, XCoordColumnName,
    // YCoordColumnName);
    // }

    public SQLBuilder getSqlBuilder(String typeName) throws IOException {
        String xCol = XCoordColumnName;
        String yCol = YCoordColumnName;
        BypassSqlFeatureTypeHandler ftHanlder = (BypassSqlFeatureTypeHandler) super.typeHandler;
        if (ftHanlder.isView(typeName)) {
            String sqlQeury = ftHanlder.getQuery(typeName);
            Map aliases = BypassSqlSQLBuilder.parseAliases(sqlQeury);
            xCol = (String) aliases.get(xCol);
            yCol = (String) aliases.get(yCol);
        }

        SQLEncoderLocationsXY encoder = new SQLEncoderLocationsXY(xCol, yCol);
        encoder.setFeatureType(ftHanlder.getSchema(typeName));
        encoder.setFIDMapper(getFIDMapper(typeName));
       
        return new LocationsXYSQLBuilder(encoder, geomName, XCoordColumnName, YCoordColumnName,
                ftHanlder);
    }

    /**
     * @see org.geotools.data.jdbc.JDBCDataStore#getGeometryAttributeIO(org.geotools.feature.AttributeType)
     */
    protected AttributeIO getGeometryAttributeIO(AttributeDescriptor type, QueryData queryData) {
        return new PointXYAttributeIO();
    }

    protected JDBCFeatureWriter createFeatureWriter(FeatureReader <SimpleFeatureType, SimpleFeature> reader, QueryData queryData)
            throws IOException {
        LOGGER.fine("returning jdbc feature writer");

        return new GeometrylessFeatureWriter(reader, queryData);
    }

}
