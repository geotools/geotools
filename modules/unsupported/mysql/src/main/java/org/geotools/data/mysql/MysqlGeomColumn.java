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
package org.geotools.data.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


/**
 * MysqlGeoColumn is used by MysqlDataSource to query its specific geometric
 * information.  There should be one created for each geometry column of each
 * feature table.  It encapsulates information about the column, such as the
 * name of the corresponding geometric table, the storage type used by that
 * table, the type of geometry contained, and various other useful information
 * culled from the GEOMETRY_COLUMNS table.  It also generates the geometries
 * of the column when queried with the ID from the feature table.
 *
 * @author Chris Holmes, Vision for New York
 * @source $URL$
 * @version $Id$
 */
public class MysqlGeomColumn {
    /** For get and set Storage type, see SFS for SQL spec */
    public static final int NORMALIZED_STORAGE_TYPE = 0;

    /**
     * For get and set Storage type, see SFS for SQL spec, the Well Known
     * Binary
     */
    public static final int WKB_STORAGE_TYPE = 1;

    /** From the SFS for SQL spec, always has the meta data */
    public static final String GEOMETRY_META_NAME = "GEOMETRY_COLUMNS";

    /**
     * For use when reading in attributes.  One off due to sql columns starting
     * at 1 instead of 0, another one for Feature ID in first column.
     */
    private static final int COLUMN_OFFSET = 2;

    /** Map of sql Type to Java class */
    private static Map sqlTypeMap = new HashMap();

    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.mysql");

    /** Standard logging instance */

    //    private static Category _log = Category.getInstance(MysqlGeomColumn.class.getName());

    /** Factory for producing geometries (from JTS). */
    private static GeometryFactory geometryFactory = new GeometryFactory();

    /** Well Known Text reader (from JTS). */
    private static WKTReader geometryReader = new WKTReader(geometryFactory);

    /** A map containing the raw geometric data, accessed by its geom ID */
    private static Map gidMap = new HashMap();

    static {
        sqlTypeMap.put("TINY", Byte.class);
        sqlTypeMap.put("SHORT", Short.class);
        sqlTypeMap.put("INT", Integer.class);
        sqlTypeMap.put("LONG", Integer.class); //check this
        sqlTypeMap.put("LONGLONG", Long.class);
        sqlTypeMap.put("DOUBLE", Double.class);
        sqlTypeMap.put("VARCHAR", String.class);
        sqlTypeMap.put("DECIMAL", String.class);
        sqlTypeMap.put("CHAR", String.class);
        sqlTypeMap.put("TEXT", String.class);
        sqlTypeMap.put("BLOB", String.class); //figure this shit out
        sqlTypeMap.put("FLOAT", Float.class);
    }

    /** The catalog containing the feature table using this geometry column */
    private String feaTabCatalog;

    /** The schema containing the feature table using this geometry column */
    private String feaTabSchema;

    /** The name of the feature table using this geometry column */
    private String feaTabName;

    /**
     * The name of the geometry column in the feature table. This class is
     * basically all the information this column points to
     */
    private String feaGeomColumn;

    /** The catalog of the geometry table where the column is stored */
    private String geomTabCatalog;

    /** The schema of the geometry table where the column is stored */
    private String geomTabSchema;

    /** The name of the geometry table where the column is stored */
    private String geomTabName;

    /** The storage type, 0 for normalized SQL, 1 for WKB */
    private int storageType;

    /** The geometry type, see OGC SFS for SQL section 3.1.2.3 */
    private int geomType;

    /**
     * The number of ordinates used, corresponds to the number of dimensions in
     * the spatial reference system.
     */
    private int coordDimension;

    /** The Max Points Per Row, only used in normalized SQL 92 implementation */
    private int maxPPR;

    /**
     * The ID of the spatial reference system.  It is a foreign key reference
     * to the SPATIAL_REF_SYS table.
     */
    private int spacRefID;

    /** The featureType schema corresponding to this geometry column. */
    private SimpleFeatureType schema = null;

    /**
     * Default constructor
     */
    public MysqlGeomColumn() {
    }

    /**
     * Convenience constructor with the minimum meta information needed to do
     * anything useful.
     *
     * @param feaTabName The name of the feature table for this geometry.
     * @param feaGeomColumn The name of the column in the feature table that
     *        refers to the MysqlGeomColumn.
     * @param geomTabName The name of the table holding the geometry data.
     */
    public MysqlGeomColumn(String feaTabName, String feaGeomColumn, String geomTabName) {
        this.feaTabName = feaTabName;
        this.feaGeomColumn = feaGeomColumn;
        this.geomTabName = geomTabName;
    }

    /**
     * A convenience constructor, when you there is an open connection, and
     * only using  flat features.  This constructor will not work with feature
     * tables that contain multiple geometries as the query on the feature
     * table will return multiple rows, which will be discarded.  For multiple
     * geometries an array of MysqlGeomColumns must be created, each
     * initialized with the default constructor, filling in the values through
     * the accesssor functions.
     *
     * @param dbConnection An open connection to the database.
     * @param feaTableName The feature table that references this Geometry Col.
     *
     * @throws SQLException if there were problems accessing the database.
     * @throws SchemaException if there were problems creating the schema.
     *
     * @task TODO: Get rid of this constructor, move the functionality outside.
     */
    public MysqlGeomColumn(Connection dbConnection, String feaTableName)
        throws SQLException, SchemaException {
        this.feaTabName = feaTableName;

        try {
            Statement statement = dbConnection.createStatement();

            //MySQL does not pre-compile statements, so making prepared
            //statements leads to no performance increases.
            String sqlQuery = makeGeomSql(feaTableName);
            LOGGER.warning("SQL q = " + sqlQuery);

            ResultSet result = statement.executeQuery(sqlQuery);

            while (result.next()) {
                //only flat features for now, with multiple geometries
                //all but the last feature will be discarded
                feaTabCatalog = result.getString(1);
                feaTabSchema = result.getString(2);
                feaGeomColumn = result.getString(4);
                geomTabCatalog = result.getString(5);
                geomTabSchema = result.getString(6);
                geomTabName = result.getString(7);
                storageType = result.getInt(8);
                geomType = result.getInt(9);
                coordDimension = result.getInt(10);
                maxPPR = result.getInt(11);
                spacRefID = result.getInt(12);
            }

            LOGGER.finer("creating new geometry column with values: " + feaTabName + " "
                + feaGeomColumn + " " + geomTabName);
            result = statement.executeQuery("SELECT * FROM " + geomTabName);

            //currently selects all, should be more elegant as we get complex
            //queries.  Ideally move outside and call populate data on results.
            int gid = 0;
            String wkb = null; //now it is actually Well Known Text, waiting for WKB reader

            while (result.next()) {
                gid = result.getInt(1);
                wkb = result.getString(6);
                populateData(gid, wkb);
            }

            result.close();
            statement.close();
        } catch (SQLException e) {
            LOGGER.warning("Some sort of database connection error: " + e.getMessage());
        }
    }

    /**
     * Creates a sql select statement to get the information on the Geometry
     * column of a feature table.
     *
     * @param feaTableName The feature table we want information about.
     *
     * @return The SQL statement to get the geometry data.
     */
    private String makeGeomSql(String feaTableName) {
        return "SELECT * FROM " + GEOMETRY_META_NAME + " WHERE F_TABLE_NAME = '" + feaTableName
        + "';";
    }

    /**
     * Stores the geometry information by geometry ID, so that it can be
     * queried later.  Currently only takes Well Known Text.  This should
     * eventually change to Well Known Binary, possible stored as a bit
     * stream?  And in time an overloaded populateData that allows for
     * normalized SQL 92 storage.
     *
     * @param geomID the primary key for a row in the Geometry Table;
     * @param wellKnownText the WKT representation of the geometry; tasks:
     *        TODO: Well Known Binary, and normalized SQL 92 (see SFS for for
     *        SQL spec 2.2.5)
     */
    public void populateData(int geomID, String wellKnownText) {
        LOGGER.finer("putting " + wellKnownText + " into gidMap");
        gidMap.put(new Integer(geomID), wellKnownText);

        //we should probably change to objects, GID not necessarily an
        //int, and the getString will change to blob when we do WKB
    }

    /**
     * Takes out a geometry according to its ID.
     *
     * @param geomID the primary key for a rwo in the Geometry Table
     */
    public void removeData(int geomID) {
        gidMap.remove(new Integer(geomID));
    }

    /**
     * Returns a jts Geometry when queried with a geometry ID.
     *
     * @param geomID the ID of the feature geometry.
     *
     * @return a jts geometry represention of the stored data, returns null is
     *         it is not found.
     *
     * @throws DataSourceException if there is trouble with the Database.
     */
    public Geometry getGeometry(int geomID) throws DataSourceException {
        String wellKnownText;
        Geometry returnGeometry = null;
        wellKnownText = (String) gidMap.get(new Integer(geomID));
        LOGGER.finer("about to create geometry for " + wellKnownText);

        if (wellKnownText == null) {
            return null;
        } else {
            try {
                returnGeometry = geometryReader.read(wellKnownText);
            } catch (ParseException e) {
                LOGGER.finer("Failed to parse the geometry from Mysql: " + e.getMessage());
            }

            return returnGeometry;
        }
    }

    /**
     * Setter method for feature catalog.
     *
     * @param catalog the name of the catalog.
     */
    public void setFeaTableCat(String catalog) {
        feaTabCatalog = catalog;
    }

    /**
     * Getter method for Feature Catalog.
     *
     * @return the name of the catalog.
     */
    public String getFeaTableCat() {
        return feaTabCatalog;
    }

    /**
     * Setter method for feature schema.
     *
     * @param schema the name of the schema.
     */
    public void setFeaTableSchema(String schema) {
        feaTabSchema = schema;
    }

    /**
     * Getter method for feature schema.
     *
     * @return the name of the schema.
     */
    public String getFeaTableSchema() {
        return feaTabSchema;
    }

    /**
     * Setter method for feature table name.
     *
     * @param name the name of the feature table.
     */
    public void setFeaTableName(String name) {
        feaTabName = name;
    }

    /**
     * Getter method for feature table name.
     *
     * @return the name of the feature table.
     */
    public String getFeaTableName() {
        return feaTabName;
    }

    /**
     * Setter method for geometry column.
     *
     * @param name the name of the column.
     */
    public void setGeomColName(String name) {
        feaGeomColumn = name;
    }

    /**
     * Getter method for geometry column.
     *
     * @return the name of the column.
     */
    public String getGeomColName() {
        return feaGeomColumn;
    }

    /**
     * Setter method for geometry catalog.
     *
     * @param catalog the name of the catalog.
     */
    public void setGeomTableCat(String catalog) {
        geomTabCatalog = catalog;
    }

    /**
     * Getter method for Geometry Catalog.
     *
     * @return the name of the catalog.
     */
    public String getGeomTableCat() {
        return geomTabCatalog;
    }

    /**
     * Setter method for geometry schema.
     *
     * @param schema the name of the catalog.
     */
    public void setGeomTableSchema(String schema) {
        geomTabSchema = schema;
    }

    /**
     * Getter method for geometry schema
     *
     * @return the name of the schema.
     */
    public String getGeomTableSchema() {
        return geomTabSchema;
    }

    /**
     * Setter method for geometry table name.
     *
     * @param name the name of the geometry table.
     */
    public void setGeomTableName(String name) {
        geomTabName = name;
    }

    /**
     * Getter method for geometry table name.
     *
     * @return the name of the catalog.
     */
    public String getGeomTableName() {
        return geomTabName;
    }

    /**
     * Sets the type used for storage in the geometry column.
     *
     * @param sType 0 for NORMALIZED_STORAGE_TYPE 1, for WKB_STORAGE_TYPE
     */
    public void setStorageType(int sType) {
        storageType = sType;
    }

    /**
     * Gets the type used for storage in the geometry column.
     *
     * @return 0 for NORMALIZED_STORAGE_TYPE, 1 for WKB_STORAGE_TYPE
     */
    public int getStorageType() {
        return storageType;
    }

    /**
     * Sets the Geometry type of the geometry column.
     *
     * @param gType the geometery type
     */
    public void setGeomType(int gType) {
        geomType = gType;
    }

    /**
     * Gets the Geometry type of the geometry column.
     *
     * @return the int representation of the GeometryType
     *
     * @task TODO: implement a hashmap so we return jts Geometry Class  Types
     *       instead of ints.
     */
    public int getGeomType() {
        return geomType;
    }

    /**
     * Gets the schema for this geometry column.
     *
     * @return the schema corresponding to this geometry column.
     */
    public SimpleFeatureType getSchema() {
        return schema;
    }

    /**
     * sets the schema for this geometry column.
     *
     * @param schema for this geometry column.
     */
    public void setSchema(SimpleFeatureType schema) {
        //TODO: check to make sure the schema is correct (geom col names are same, etc.)
        this.schema = schema;
    }

    /**
     * Creates the schema, a FeatureType of the attributes.
     *
     * @param metaData from the query of the feature table.
     * @param geoColumn the name of the geometry column in the feature table.
     *
     * @return a FeatureType of the attributes.
     *
     * @throws SQLException if there was database connectivity issues.
     * @throws SchemaException if there was problems creating the FeatureType.
     *
     * @todo Fix FeatureType name - IanS tasks TODO: put this method
     *       MysqlGeomColumn or a SchemaFactory.
     */
    public static SimpleFeatureType makeSchema(ResultSetMetaData metaData, String geoColumn)
        throws SQLException, SchemaException {
        String columnName = null;
        Class colClass = null;
        int numCols = metaData.getColumnCount();
        AttributeDescriptor[] attributes = new AttributeDescriptor[numCols - 1];

        LOGGER.finer("about to loop through cols");

        // loop through all columns except first, as it's the featureID
        for (int i = 2; i <= numCols; i++) {
            columnName = metaData.getColumnName(i);
            LOGGER.finer("reading col: " + i + " named: " + columnName);
            LOGGER.finer("reading col: " + metaData.getColumnTypeName(i));
            AttributeTypeBuilder build = new AttributeTypeBuilder();
            // set column name and type from database
            //TODO: use MysqlGeomColumn.getGeomType, once it's fully implemented
            build.setNillable(true);
            if (columnName.equals(geoColumn)) { //if it is a geomtry column, by name
            	//build.setCRS(crs); TODO: use CRS's in mysql
            	build.setBinding(Geometry.class);
                attributes[i - COLUMN_OFFSET] = build.buildDescriptor(columnName);
            } else {
                colClass = (Class) sqlTypeMap.get(metaData.getColumnTypeName(i));
                build.setBinding(colClass);
                attributes[i - COLUMN_OFFSET] = build.buildDescriptor(columnName);
            }
        }

        // @todo Fix FeatureType name - IanS 
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName("mysql-features");
        b.addAll(attributes);
        return b.buildFeatureType();
    }
}
