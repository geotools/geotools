/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.factory.FactoryRegistryException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Manage the DB2 Spatial Extender spatial catalog information in memory to
 * improve performance.
 * 
 * <p>
 * This class is not intended to be used outside the DB2 plug-in package.
 * </p>
 * 
 * <p>
 * Currently, a different catalog is managed for each DB2 database and schema
 * specified in the creation of a DB2DataStore.  Multiple data stores created
 * for the same database and schema can share the same catalog.
 * </p>
 * 
 * <p>
 * All schema, table and column names are case sensitive.
 * </p>
 * 
 * <p>
 * Convenience methods provided for access to various types of catalog
 * information like type names, srid, etc.
 * </p>
 * 
 * <p>
 * <b>Note: the 'srid' value in DB2 is different from the srid value referenced
 * in OGC documents.  The OGC srid corresponds more closely to the DB2 'csid'
 * value. This makes life quite confusing because the DB2 'srid' is needed to
 * construct geometries in the database.  GeoTools needs the 'csid' which
 * generally corresponds to an EPSG coordinate system identifier in order to
 * create an OGC coordinate system reference. </b>
 * </p>
 * 
 * <p>
 * <b> We also assume that there is a single 'srid' associated with all the
 * geometries in a particular geometry column.  This is not required by DB2.
 * </b>
 * </p>
 * 
 * <p>
 * Do we need to consider freeing this up at some point as the HashMap of
 * catalogs is stored in a class variable?
 * </p>
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2SpatialCatalog {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.data.db2");

    /** A map of all the catalogs that have been created. */
    private static Map catalogs = new HashMap();

    /** All the DB2 geometry columns in this catalog */
    private Map geometryColumns;

    /**
     * All the DB2 coordinate systems referenced by the geometry columns in
     * this catalog.
     */
    private Map coordinateSystems = new HashMap();

    /** The specific database that this catalog was generated from. */
    private String dbURL;

    /** The table schema that identifies all the geometries in this catalog. */
    private String tableSchema;

    /**
     * The constructor for a DB2SpatialCatalog.
     * 
     * <p>
     * This is only called from getInstance(String dbURL, String tableSchema,
     * Connection conn).
     * </p>
     * 
     * <p>
     * An alternative would be to use a factory object but it isn't clear what
     * advantage that would offer.
     * </p>
     *
     * @param dbURL the database URL
     * @param tableSchema the table schema name
     */
    private DB2SpatialCatalog(String dbURL, String tableSchema) {
        super();
        this.dbURL = dbURL;
        this.tableSchema = tableSchema;
    }

    /**
     * This method should only be called by DB2SpatialCatalogTest
     */
    public static void reset() {
        catalogs = new HashMap();
    }

    /**
     * Get an instance of the DB2SpatialCatalog for the specified datastore.
     * 
     * <p>
     * If it already exists, just return the current catalog for this database
     * identified by the database connection URL and table schema.
     * </p>
     * 
     * <p>
     * If it doesn't already exist and the connection parameter is null, return
     * null.
     * </p>
     * 
     * <p>
     * If not, read the DB2 Spatial Extender catalog tables and create an
     * in-memory representation for efficient access.
     * </p>
     *
     * @param dbURL the database URL
     * @param tableSchema the table schema name
     * @param conn an active database connection or null
     *
     * @return a DB2SpatialCatalog.
     *
     * @throws SQLException if there was a failure to create a catalog.
     */
    public static DB2SpatialCatalog getInstance(String dbURL,
        String tableSchema, Connection conn) throws SQLException {
        DB2SpatialCatalog catalog = (DB2SpatialCatalog) catalogs.get(dbURL
                + tableSchema);

        // Try to create and populate a new catalog if it wasn't found and we have a database connection.			
        if ((catalog == null) && (conn != null)) {
            catalog = new DB2SpatialCatalog(dbURL, tableSchema);
            catalog.loadCatalog(conn, tableSchema);
            catalogs.put(dbURL + tableSchema, catalog);
        }

        return catalog;
    }

    /**
     * A convenience method to nicely format the schema, table and column name.
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return a concatenated key value.
     */
    public static String geomID(String tableSchema, String tableName,
        String columnName) {
        String key = tableSchema + "." + tableName + "(" + columnName + ")";

        return key;
    }

    /**
     * Loads the DB2SpatialCatalog with the values from the DB2 Spatial
     * Extender catalog views.
     * 
     * <p>
     * The spatial columns are found in the view db2gse.ST_Geometry_Columns.
     * </p>
     * 
     * <p>
     * If the SRID value returned was null, attempt to get it from the actual
     * data table with the method getSridFromTable.
     * </p>
     * 
     * <p>
     * If the coordinate system for the geometry column just returned does not
     * already exist in the map of coordinate systems construct a new
     * coordinate system.
     * </p>
     *
     * @param conn an active database connection
     * @param schemaName the schema to identify tables in this catalog
     *
     * @throws SQLException
     */
     void loadCatalog(Connection conn, String schemaName)
        throws SQLException {
        this.geometryColumns = new HashMap();

        String tableSchema;
        String tableName;
        String columnName;
        String typeName;
        Integer srsId;

        String queryGeom = "SELECT table_schema, table_name, column_name, "
            + " type_name, srs_id" + " FROM db2gse.st_geometry_columns"
            + " WHERE table_schema = '" + schemaName + "' ";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(queryGeom);

        while (rs.next()) {
            tableSchema = rs.getString(1).trim();
            tableName = rs.getString(2).trim();
            columnName = rs.getString(3).trim();
            typeName = rs.getString(4).trim();
            srsId = new Integer(rs.getInt(5));

            if (rs.wasNull()) {
                srsId = getSridFromTable(conn, tableSchema, tableName,
                        columnName);
            }

            // Try to get the coordinate system if it was already loaded.
            DB2CoordinateSystem cs = (DB2CoordinateSystem) this.coordinateSystems
                .get(srsId);

            if (cs == null) {
                cs = new DB2CoordinateSystem(conn, srsId.intValue());
                this.coordinateSystems.put(srsId, cs);
            }

            DB2GeometryColumn gc = new DB2GeometryColumn(tableSchema,
                    tableName, columnName, typeName, srsId, cs);
            LOGGER.fine("Spatial column: " + gc + " " + cs);
            this.geometryColumns.put(geomKey(gc), gc); // Save this geometry
        }

        rs.close();
        stmt.close();
    }

    /**
     * Gets the DB2 srid value by selecting the first geometry value in the
     * data table.
     * 
     * <p>
     * If no value is found, the returned default srid value is 0.
     * </p>
     *
     * @param conn
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return the DB2 srid value.
     *
     * @throws SQLException may be thrown if there is a database problem.  No
     *         local checking for exceptions.
     */
    private Integer getSridFromTable(Connection conn, String tableSchema,
        String tableName, String columnName) throws SQLException {
        Integer srsId = new Integer(0);
        String querySrid = "SELECT DB2GSE.ST_SRID(\"" + columnName + "\")"
            + " FROM \"" + tableSchema + "\".\"" + tableName + "\""
            + " WHERE \"" + columnName + "\" IS NOT NULL"
            + " FETCH FIRST ROW ONLY";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(querySrid);

        while (rs.next()) {
            srsId = new Integer(rs.getInt(1));
        }

        return srsId;
    }

    /**
     * Creates a key value to associate with this particular geometry.
     * 
     * <p>
     * This is used when a geometry is being stored in the map.
     * </p>
     *
     * @param gc a geometry column
     *
     * @return a unique string representation which can be used to look up a
     *         geometry.
     */
    private String geomKey(DB2GeometryColumn gc) {
        String key = this.dbURL + ":" + gc.getTableSchema() + "."
            + gc.getTableName() + "." + gc.getColumnName();

        return key;
    }

    /**
     * Creates a key value that can be used to look up a geometry.
     * 
     * <p>
     * This is used when a geometry is going to be looked up in the map.
     * </p>
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return a unique string representation which can be used to look up a
     *         geometry.
     */
    private String geomKey(String tableSchema, String tableName,
        String columnName) {
        String key = this.dbURL + ":" + tableSchema + "." + tableName + "."
            + columnName;

        return key;
    }

    /**
     * Gets all the type (table) names in this catalog.
     * 
     * <p>
     * There could possibly be duplicate type names if a table has more than
     * one geometry column.  Any duplicates will be eliminated.
     * </p>
     *
     * @return a String array of type names
     */
    String[] getTypeNames() {
        Set typeNames = new HashSet(); // Use a Set to eliminate duplicates
        Iterator it = this.geometryColumns.values().iterator();

        while (it.hasNext()) {
            DB2GeometryColumn gc = (DB2GeometryColumn) it.next();
            typeNames.add(gc.getTableName());
        }

        return (String[]) typeNames.toArray(new String[typeNames.size()]);
    }

    /**
     * Gets the DB2 geometry type name for this geometry.
     * 
     * <p>
     * The geometry type name will be a value like 'ST_POINT' or
     * 'ST_MULTIPOLYGON'.
     * </p>
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return the DB2 geometry type name.
     *
     * @throws IOException if a geometry was not found in the catalog for the
     *         specified schema, table and column.
     */
    String getDB2GeometryTypeName(String tableSchema, String tableName,
        String columnName) throws IOException {
        return getGeometryColumn(tableSchema, tableName, columnName)
                   .getTypeName();
    }

    /**
     * Gets the DB2 coordinate system identifier associated with this geometry
     * column.
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return the coordinate system identifier.
     *
     * @throws IOException if a geometry was not found in the catalog for the
     *         specified schema, table and column.
     */
    int getCsId(String tableSchema, String tableName, String columnName)
        throws IOException {
        return getGeometryColumn(tableSchema, tableName, columnName).getCsId();
    }

    /**
     * Gets the OpenGIS CoordinateReferenceSystem of this geometry column.
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return the coordinate reference system.
     *
     * @throws FactoryRegistryException
     * @throws FactoryException
     * @throws IOException
     */
    CoordinateReferenceSystem getCRS(String tableSchema, String tableName,
        String columnName)
        throws FactoryRegistryException, FactoryException, IOException {
        return getGeometryColumn(tableSchema, tableName, columnName).getCRS();
    }

    /**
     * Gets the DB2 srid value associated with this geometry column.
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return the DB2 srid
     *
     * @throws IOException if a geometry was not found in the catalog for the
     *         specified schema, table and column.
     */
    int getSRID(String tableSchema, String tableName, String columnName)
        throws IOException {
        return getGeometryColumn(tableSchema, tableName, columnName).getSrsId()
                   .intValue();
    }

    /**
     * Gets the geometry colum or throws an exception if not found.
     *
     * @param tableSchema
     * @param tableName
     * @param columnName
     *
     * @return a DB2GeometryColumn
     *
     * @throws IOException if a geometry was not found in the catalog for the
     *         specified schema, table and column.
     */
    private DB2GeometryColumn getGeometryColumn(String tableSchema,
        String tableName, String columnName) throws IOException {
        String geomKey = geomKey(tableSchema, tableName, columnName);
        DB2GeometryColumn gc = (DB2GeometryColumn) this.geometryColumns.get(geomKey);

        if (gc == null) {
            throw new IOException("Geometry not found: " + geomKey);
        }

        return gc;
    }

    /**
     * Returns the database URL and table schema.
     *
     * @return the database URL and table schema as a String.
     */
    public String toString() {
        return this.dbURL + "-" + this.tableSchema;
    }
}
