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

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.Join.Type;
import org.geotools.data.Query;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.SumVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.InFunction;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.expression.Subtract;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The driver used by JDBCDataStore to directly communicate with the database.
 *
 * <p>This class encapsulates all the database specific operations that JDBCDataStore needs to
 * function. It is implemented on a per-database basis.
 *
 * <p>
 *
 * <h3>Type Mapping</h3>
 *
 * One of the jobs of a dialect is to map sql types to java types and vice versa. This abstract
 * implementation provides default mappings for "primitive" java types. The following mappings are
 * provided. A '*' denotes that the mapping is the default java to sql mapping as well.
 *
 * <ul>
 *   <li>VARCHAR -> String *
 *   <li>CHAR -> String
 *   <li>LONGVARCHAR -> String
 *   <li>BIT -> Boolean
 *   <li>BOOLEAN -> Boolean *
 *   <li>SMALLINT -> Short *
 *   <li>TINYINT -> Short
 *   <li>INTEGER -> Integer *
 *   <li>BIGINT -> Long *
 *   <li>REAL -> Float *
 *   <li>DOUBLE -> Double *
 *   <li>FLOAT -> Double
 *   <li>NUMERIC -> BigDecimal *
 *   <li>DECIMAL -> BigDecimal
 *   <li>DATE -> java.sql.Date *
 *   <li>TIME -> java.sql.Time *
 *   <li>TIMESTAMP -> java.sql.Timestmap *
 * </ul>
 *
 * Subclasses should <b>extend</b> (not override) the following methods to configure the mappings:
 *
 * <ul>
 *   <li>{@link #registerSqlTypeToClassMappings(Map)}
 *   <li>{@link #registerSqlTypeNameToClassMappings(Map)}
 *   <li>{@link #registerClassToSqlMappings(Map)}
 * </ul>
 *
 * <p>
 *
 * <p>This class is intended to be stateless, therefore subclasses should not maintain any internal
 * state. If for some reason a subclass must keep some state around (not recommended), it must
 * ensure that the state is accessed in a thread safe manner.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public abstract class SQLDialect {
    protected static final Logger LOGGER = Logging.getLogger(SQLDialect.class);

    /** The basic filter capabilities all databases should have */
    public static FilterCapabilities BASE_DBMS_CAPABILITIES =
            new FilterCapabilities() {
                {
                    addAll(FilterCapabilities.LOGICAL_OPENGIS);
                    addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
                    addAll(InFunction.getInCapabilities());

                    // simple arithmetic
                    addType(Add.class);
                    addType(Subtract.class);
                    addType(Multiply.class);
                    addType(Divide.class);

                    // properties and literals
                    addType(PropertyName.class);
                    addType(Literal.class);

                    // simple comparisons
                    addType(PropertyIsNull.class);
                    addType(PropertyIsBetween.class);
                    addType(Id.class);
                    addType(IncludeFilter.class);
                    addType(ExcludeFilter.class);
                    addType(PropertyIsLike.class);

                    // native filter support
                    addType(NativeFilter.class);
                }
            };

    /** The datastore using the dialect */
    protected JDBCDataStore dataStore;

    /**
     * Creates the dialect.
     *
     * @param dataStore The dataStore using the dialect.
     */
    protected SQLDialect(JDBCDataStore dataStore) {
        this.dataStore = dataStore;
    }

    /**
     * Initializes a newly created database connection.
     *
     * <p>Subclasses should override this method if there is some additional action that needs to be
     * taken when a new connection to the database is created. The default implementation does
     * nothing.
     *
     * @param cx The new database connection.
     */
    public void initializeConnection(Connection cx) throws SQLException {}

    /**
     * Determines if the specified table should be included in those published
     * by the datastore.
     * <p>
     * This method returns <code>true</code> if the table should be published as
     * a feature type, otherwise it returns <code>false</code>. Subclasses should
     * override this method, this default implementation returns <code>true<code>.
     * </p>
     * <p>
     * A database connection is provided to the dialect but it should not be closed.
     * However any statements objects or result sets that are instantiated from it
     * must be closed.
     * </p>
     * @param schemaName The schema of the table, might be <code>null</code>..
     * @param tableName The name of the table.
     * @param cx Database connection.
     *
     */
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        return true;
    }

    /**
     * Registers the sql type name to java type mappings that the dialect uses when reading and
     * writing objects to and from the database.
     *
     * <p>Subclasses should extend (not override) this method to provide additional mappings, or to
     * override mappings provided by this implementation. This implementation provides the following
     * mappings:
     */
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        // TODO: do the normal types
    }

    /**
     * Determines the class mapping for a particular column of a table.
     * <p>
     * Implementing this method is optional. It is used to allow database to
     * perform custom type mappings based on various column metadata. It is called
     * before the mappings registered in {@link #registerSqlTypeToClassMappings(Map)}
     * and {@link #registerSqlTypeNameToClassMappings(Map) are used to determine
     * the mapping. Subclasses should implement as needed, this default implementation
     * returns <code>null</code>.
     * </p>
     * <p>
     * The <tt>columnMetaData</tt> argument is provided from
     * {@link DatabaseMetaData#getColumns(String, String, String, String)}.
     * </p>
     * @param columnMetaData The column metadata
     * @param cx The connection used to retrieve the metadata
     * @return The class mapped to the to column, or <code>null</code>.
     */
    public Class<?> getMapping(ResultSet columnMetaData, Connection cx) throws SQLException {
        return null;
    }

    /**
     * Handles the mapping for a user defined type.
     *
     * <p>This method is called after {@link #getMapping(ResultSet, Connection)} but before the rest
     * of the type mapping heuristics are applied.
     *
     * <p>Implementing this method is optional. It is used to allow for handling user defined types
     * or "DOMAINS". Dialects that implement this method should set the appropriate information on
     * the <tt>metadata</tt> object to allow the column to be mapped via teh regular type mapping
     * heuristics.
     *
     * @param columnMetaData The column metdata.
     * @param metadata The column metadata object that collections mapping information.
     * @param cx The database connection, not to be closed.
     */
    public void handleUserDefinedType(
            ResultSet columnMetaData, ColumnMetadata metadata, Connection cx) throws SQLException {}

    /**
     * Registers the sql type to java type mappings that the dialect uses when reading and writing
     * objects to and from the database.
     *
     * <p>Subclasses should extend (not override) this method to provide additional mappings, or to
     * override mappings provided by this implementation. This implementation provides the following
     * mappings:
     */
    public void registerSqlTypeToClassMappings(Map<Integer, Class<?>> mappings) {
        mappings.put(Integer.valueOf(Types.VARCHAR), String.class);
        mappings.put(Integer.valueOf(Types.CHAR), String.class);
        mappings.put(Integer.valueOf(Types.LONGVARCHAR), String.class);
        mappings.put(Integer.valueOf(Types.NVARCHAR), String.class);
        mappings.put(Integer.valueOf(Types.NCHAR), String.class);

        mappings.put(Integer.valueOf(Types.BIT), Boolean.class);
        mappings.put(Integer.valueOf(Types.BOOLEAN), Boolean.class);

        mappings.put(Integer.valueOf(Types.TINYINT), Short.class);
        mappings.put(Integer.valueOf(Types.SMALLINT), Short.class);

        mappings.put(Integer.valueOf(Types.INTEGER), Integer.class);
        mappings.put(Integer.valueOf(Types.BIGINT), Long.class);

        mappings.put(Integer.valueOf(Types.REAL), Float.class);
        mappings.put(Integer.valueOf(Types.FLOAT), Double.class);
        mappings.put(Integer.valueOf(Types.DOUBLE), Double.class);

        mappings.put(Integer.valueOf(Types.DECIMAL), BigDecimal.class);
        mappings.put(Integer.valueOf(Types.NUMERIC), BigDecimal.class);

        mappings.put(Integer.valueOf(Types.DATE), Date.class);
        mappings.put(Integer.valueOf(Types.TIME), Time.class);
        mappings.put(Integer.valueOf(Types.TIMESTAMP), Timestamp.class);

        mappings.put(Integer.valueOf(Types.BLOB), byte[].class);
        mappings.put(Integer.valueOf(Types.BINARY), byte[].class);
        mappings.put(Integer.valueOf(Types.CLOB), String.class);

        mappings.put(Integer.valueOf(Types.VARBINARY), byte[].class);

        // subclasses should extend to provide additional
    }

    /**
     * Registers the java type to sql type mappings that the datastore uses when reading and writing
     * objects to and from the database. *
     *
     * <p>Subclasses should extend (not override) this method to provide additional mappings, or to
     * override mappings provided by this implementation. This implementation provides the following
     * mappings:
     */
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        mappings.put(String.class, Integer.valueOf(Types.VARCHAR));

        mappings.put(Boolean.class, Integer.valueOf(Types.BOOLEAN));
        mappings.put(boolean.class, Integer.valueOf(Types.BOOLEAN));

        mappings.put(Short.class, Integer.valueOf(Types.SMALLINT));
        mappings.put(short.class, Integer.valueOf(Types.SMALLINT));

        mappings.put(Integer.class, Integer.valueOf(Types.INTEGER));
        mappings.put(int.class, Integer.valueOf(Types.INTEGER));
        mappings.put(Long.class, Integer.valueOf(Types.BIGINT));
        mappings.put(long.class, Integer.valueOf(Types.BIGINT));

        mappings.put(Float.class, Integer.valueOf(Types.REAL));
        mappings.put(float.class, Integer.valueOf(Types.REAL));
        mappings.put(Double.class, Integer.valueOf(Types.DOUBLE));
        mappings.put(double.class, Integer.valueOf(Types.DOUBLE));

        mappings.put(BigDecimal.class, Integer.valueOf(Types.NUMERIC));

        mappings.put(Date.class, Integer.valueOf(Types.DATE));
        mappings.put(Time.class, Integer.valueOf(Types.TIME));
        mappings.put(java.util.Date.class, Integer.valueOf(Types.TIMESTAMP));
        mappings.put(Timestamp.class, Integer.valueOf(Types.TIMESTAMP));

        mappings.put(byte[].class, Integer.valueOf(Types.BLOB));

        // subclasses should extend and provide additional
    }

    /**
     * Registers any overrides that should occur when mapping an integer sql type value to an
     * underlying sql type name.
     *
     * <p>The default implementation of this method does nothing. Subclasses should override in
     * cases where:
     *
     * <ul>
     *   <li>database type metadata does not provide enough information to properly map
     *   <li>to support custom types (those not in {@link Types})
     * </ul>
     */
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {}

    /**
     * Registers the set of aggregate functions the dialect is capable of handling.
     *
     * <p>Aggregate functions are handled via visitors of special types. The
     * <param>aggregates</param> maps the class of the visitor to the associated function name. This
     * base implementation handles some of the well known mappings:
     *
     * <ul>
     *   <li>{@link UniqueVisitor} -> "unique"
     *   <li>
     *   <li>{@link CountVisitor} -> "count"
     *   <li>
     *   <li>{@link MaxVisitor} -> "max"
     *   <li>
     *   <li>{@link MinVisitor} -> "min"
     *   <li>
     *   <li>{@link SumVisitor} -> "sum"
     *   <li>
     * </ul>
     *
     * Subclasses should extend (not override) to provide additional functions.
     */
    public void registerAggregateFunctions(
            Map<Class<? extends FeatureVisitor>, String> aggregates) {
        // register the well known
        aggregates.put(UniqueVisitor.class, "distinct");
        aggregates.put(CountVisitor.class, "count");
        aggregates.put(MinVisitor.class, "min");
        aggregates.put(MaxVisitor.class, "max");
        aggregates.put(SumVisitor.class, "sum");
    }

    /**
     * Returns the java class mapping for a particular column.
     *
     * <p>This method is used as a "last resort" when the mappings specified by the dialect in the
     * {@link #registerSqlTypeToClassMappings(Map)}" method fail to yield a java type.
     *
     * <p>The most common case is for databases which store all geometric values under a single
     * type, and use some secondary means to store the specific type (like a metadata table). *
     *
     * <p>This method is given a direct connection to the database. The connection must not be
     * closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>In the event that the mapping cannot be determined, this method should return <code>null
     * </code>.
     *
     * @param schemaName The schema name, may be <code>null</code>.
     * @param tableName The table name.
     * @param columnName The column name.
     * @param type The data type from {@link Types}, reported by database metadata.
     * @param cx The database connection.
     * @return The mapped type of the column, or <code>null</code> if it can not be inferred.
     */

    //    public final Class getMapping( String schemaName, String tableName, String columnName,
    // Integer type, Connection cx )
    //        throws SQLException {
    //        return null;
    //    }

    /**
     * Returns the string used to escape names.
     *
     * <p>This value is used to escape any name in a query. This includes columns, tables, schemas,
     * indexes, etc... If no escape is necessary this method should return the empty string, and
     * never return <code>null</code>.
     *
     * <p>This default implementation returns a single double quote ("), subclasses must override to
     * provide a different espcape.
     */
    public String getNameEscape() {
        return "\"";
    }

    /** Quick accessor for {@link #getNameEscape()}. */
    protected final String ne() {
        return getNameEscape();
    }

    /**
     * Surrounds a name with the SQL escape string.
     *
     * <p>If the name contains the SQL escape string, the SQL escape string is duplicated.
     */
    public String escapeName(String name) {
        String nameEscape = getNameEscape();
        if (nameEscape.isEmpty()) return name;
        StringBuilder sb = new StringBuilder();
        sb.append(nameEscape);
        int offset = 0;
        int escapeOffset;
        while ((escapeOffset = name.indexOf(nameEscape, offset)) != -1) {
            sb.append(name.substring(offset, escapeOffset));
            sb.append(nameEscape);
            sb.append(nameEscape);
            offset = escapeOffset + nameEscape.length();
        }
        sb.append(name.substring(offset));
        sb.append(nameEscape);
        return sb.toString();
    }

    /**
     * Encodes the name of a column in an SQL statement.
     *
     * <p>This method escapes <tt>raw</tt> using method {@link #escapeName(String)}. Subclasses
     * usually don't override this method and instead override {@link #getNameEscape()}.
     *
     * <p>The <tt>prefix</tt> parameter may be <code>null</code> so subclasses that do override must
     * handle that case.
     */
    public void encodeColumnName(String prefix, String raw, StringBuffer sql) {
        if (prefix != null) {
            sql.append(escapeName(prefix)).append(".");
        }
        sql.append(escapeName(raw));
    }

    /**
     * Encodes the type of a column in an SQL CREATE TABLE statement.
     *
     * <p>The default implementation simply outputs the <tt>sqlTypeName</tt> argument as is.
     * Subclasses may override this method. Such cases might include:
     *
     * <ul>
     *   <li>A type definition requires some parameter, ex: size of a varchar
     *   <li>The provided attribute (<tt>att</tt>) contains some additional restrictions that can be
     *       encoded in the type, ex: field length
     * </ul>
     */
    public void encodeColumnType(String sqlTypeName, StringBuffer sql) {
        sql.append(sqlTypeName);
    }

    /**
     * Encodes the alias of a column in an sql query.
     *
     * <p>This default implementation uses the syntax:
     *
     * <pre>as "alias"</pre>
     *
     * . Subclasses should override to provide a different syntax.
     */
    public void encodeColumnAlias(String raw, StringBuffer sql) {
        sql.append(" as ");
        encodeColumnName(null, raw, sql);
    }

    /**
     * Encodes the alias of a table in an sql query.
     *
     * <p>This default implementation uses the syntax:
     *
     * <pre>as "alias"</pre>
     *
     * . Subclasses should override to provide a different syntax.
     */
    public void encodeTableAlias(String raw, StringBuffer sql) {
        sql.append(" as ");
        encodeColumnName(null, raw, sql);
    }

    /**
     * Encodes the name of a table in an SQL statement.
     *
     * <p>This method escapes <tt>raw</tt> using method {@link #escapeName(String)}. Subclasses
     * usually dont override this method and instead override {@link #getNameEscape()}.
     */
    public void encodeTableName(String raw, StringBuffer sql) {
        sql.append(escapeName(raw));
    }

    /**
     * Encodes the name of a schema in an SQL statement.
     *
     * <p>This method escapes <tt>raw</tt> using method {@link #escapeName(String)}. Subclasses
     * usually dont override this method and instead override {@link #getNameEscape()}.
     */
    public void encodeSchemaName(String raw, StringBuffer sql) {
        sql.append(escapeName(raw));
    }

    /**
     * Returns the name of a geometric type based on its integer constant.
     *
     * <p>The constant, <tt>type</tt>, is registered in {@link
     * #registerSqlTypeNameToClassMappings(Map)}.
     *
     * <p>This default implementation returns <code>null</code>, subclasses should override.
     */
    public String getGeometryTypeName(Integer type) {
        return null;
    }

    /**
     * Returns the spatial reference system identifier (srid) for a particular geometry column.
     *
     * <p>This method is given a direct connection to the database. The connection must not be
     * closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>In the event that the srid cannot be determined, this method should return <code>null
     * </code>.
     *
     * @param schemaName The database schema, could be <code>null</code>.
     * @param tableName The table, never <code>null</code>.
     * @param columnName The column name, never <code>null</code>
     * @param cx The database connection.
     */
    public Integer getGeometrySRID(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return null;
    }

    /**
     * Returns the dimension of the coordinates in the geometry. Defaults to 2, subclasses can
     * override it.
     *
     * <p>This method is given a direct connection to the database. The connection must not be
     * closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>In the event that the dimension cannot be determined, this method should return 2
     *
     * @param schemaName The database schema, could be <code>null</code>.
     * @param tableName The table, never <code>null</code>.
     * @param columnName The column name, never <code>null</code>
     * @param cx The database connection.
     */
    public int getGeometryDimension(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return 2;
    }

    /**
     * Turns the specified srid into a {@link CoordinateReferenceSystem}, or returns <code>null
     * </code> if not possible.
     *
     * <p>The implementation might just use <code>CRS.decode("EPSG:" + srid)</code>, but most
     * spatial databases will have their own SRS database that can be queried as well.
     *
     * <p>As a rule of thumb you should override this method if your spatial database uses codes
     * that are not part of the EPSG standard database, of if for some reason you deem it preferable
     * to use your database definition instead of an official EPSG one.
     *
     * <p>Most overrides will try out to decode the official EPSG code first, and fall back on the
     * custom database definition otherwise
     */
    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        try {
            return CRS.decode("EPSG:" + srid);
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(
                        Level.FINE,
                        "Could not decode " + srid + " using the built-in EPSG database");
            }
            return null;
        }
    }

    /**
     * Returns the bounds of all geometry columns in the layer using any approach that proves to be
     * faster than the plain bounds aggregation (e.g., better than the "plain select extent(geom)
     * from table" on PostGIS), or null if none exists or the fast method has not been enabled
     * (e.g., if the fast method is just an estimate of the bounds you probably want the user to
     * enable it manually)
     *
     * @param schema The database schema, if any, or null
     * @param featureType The feature type containing the geometry columns whose bounds need to
     *     computed. Mind, it may be retyped and thus contain less geometry columns than the table
     * @return a list of referenced envelopes (some of which may be null or empty)
     */
    public List<ReferencedEnvelope> getOptimizedBounds(
            String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        return null;
    }

    /**
     * Encodes the spatial extent function of a geometry column in a SELECT statement.
     *
     * <p>This method must also be sure to properly encode the name of the column with the {@link
     * #encodeColumnName(String, StringBuffer)} function.
     */
    public abstract void encodeGeometryEnvelope(
            String tableName, String geometryColumn, StringBuffer sql);

    /**
     * Decodes the result of a spatial extent function in a SELECT statement.
     *
     * <p>This method is given direct access to a result set. The <tt>column</tt> parameter is the
     * index into the result set which contains the spatial extent value. The query for this value
     * is build with the {@link #encodeGeometryEnvelope(String, String, StringBuffer)} method.
     *
     * <p>This method must not read any other objects from the result set other then the one
     * referenced by <tt>column</tt>.
     *
     * @param rs A result set
     * @param column Index into the result set which points at the spatial extent value.
     * @param cx The database connection.
     */
    public abstract Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException;

    /**
     * Encodes the name of a geometry column in a SELECT statement.
     *
     * <p>This method should wrap the column name in any functions that are used to retrieve its
     * value. For instance, often it is necessary to use the function <code>asText</code>, or <code>
     * asWKB</code> when fetching a geometry.
     *
     * <p>This method must also be sure to properly encode the name of the column with the {@link
     * #encodeColumnName(String, String, StringBuffer)} function.
     *
     * <p>Example:
     *
     * <pre>
     *   <code>
     *   sql.append( "asText(" );
     *   column( gatt.getLocalName(), sql );
     *   sql.append( ")" );
     *   </code>
     * </pre>
     */
    public void encodeGeometryColumn(
            GeometryDescriptor gatt, String prefix, int srid, Hints hints, StringBuffer sql) {
        encodeColumnName(prefix, gatt.getLocalName(), sql);
    }

    public void encodeGeometryColumnGeneralized(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        throw new UnsupportedOperationException("Geometry generalization not supported");
    }

    public void encodeGeometryColumnSimplified(
            GeometryDescriptor gatt, String prefix, int srid, StringBuffer sql, Double distance) {
        throw new UnsupportedOperationException("Geometry simplification not supported");
    }

    /**
     * Decodes a geometry value from the result of a query.
     *
     * <p>This method is given direct access to a result set. The <tt>column</tt> parameter is the
     * index into the result set which contains the geometric value.
     *
     * <p>An implementation should deserialize the value provided by the result set into {@link
     * Geometry} object. For example, consider an implementation which deserializes from well known
     * text: <code>
     *   <pre>
     *   String wkt = rs.getString( column );
     *   if ( wkt == null ) {
     *     return null;
     *   }
     *   return new WKTReader(factory).read( wkt );
     *   </pre>
     * </code> Note that implementations must handle <code>null</code> values.
     *
     * <p>The <tt>factory</tt> parameter should be used to instantiate any geometry objects.
     */
    public abstract Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException;

    /**
     * Decodes a geometry value from the result of a query specifying the column as an index.
     *
     * <p>See {@link #decodeGeometryValue(GeometryDescriptor, ResultSet, String, GeometryFactory)}
     * for a more in depth description.
     *
     * @see {@link #decodeGeometryValue(GeometryDescriptor, ResultSet, String, GeometryFactory)}.
     */
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {

        String columnName = rs.getMetaData().getColumnName(column);
        return decodeGeometryValue(descriptor, rs, columnName, factory, cx, hints);
    }

    /**
     * Encodes the primary key definition in a CREATE TABLE statement.
     *
     * <p>Subclasses should override this method if need be, the default implementation does the
     * following:
     *
     * <pre>
     *   <code>
     *   encodeColumnName( column, sql );
     *   sql.append( " int PRIMARY KEY" );
     *   </code>
     * </pre>
     */
    public void encodePrimaryKey(String column, StringBuffer sql) {
        encodeColumnName(null, column, sql);
        sql.append(" INTEGER PRIMARY KEY");
    }

    /** Encodes the syntax for a join between two tables. */
    public void encodeJoin(Type joinType, StringBuffer sql) {
        switch (joinType) {
            case INNER:
                sql.append("INNER");
                break;
            case OUTER:
                sql.append("LEFT OUTER");
                break;
            default:
                throw new IllegalArgumentException("Join type " + joinType + " not supported");
        }
        sql.append(" JOIN");
    }

    /**
     * Encodes the CREATE TABLE statement.
     *
     * <p>Default implementation adds "CREATE TABLE" to the sql buffer. Subclasses may choose to
     * override to handle db specific syntax.
     */
    public void encodeCreateTable(StringBuffer sql) {
        sql.append("CREATE TABLE ");
    }

    /**
     * Encodes anything post a column in a CREATE TABLE statement.
     *
     * <p>This is appended after the column name and type. Subclasses may choose to override this
     * method, the default implementation does nothing.
     *
     * @param att The attribute corresponding to the column.
     */
    public void encodePostColumnCreateTable(AttributeDescriptor att, StringBuffer sql) {}

    /**
     * Encodes anything post a CREATE TABLE statement.
     *
     * <p>This is appended to a CREATE TABLE statement after the column definitions. This default
     * implementation does nothing, subclasses should override as need be.
     */
    public void encodePostCreateTable(String tableName, StringBuffer sql) {}

    /**
     * Encodes anything after the SELECT clause and before the FROM clause.
     *
     * <p>This method does not nothing, subclass may override to add additional columns.
     *
     * @param featureType The feature type being queried.
     */
    public void encodePostSelect(SimpleFeatureType featureType, StringBuffer sql) {}

    /**
     * Callback to execute any additional sql statements post a create table statement.
     *
     * <p>This method should be implemented by subclasses that need to do some post processing on
     * the database after a table has been created. Examples might include:
     *
     * <ul>
     *   <li>Creating a sequence for a primary key
     *   <li>Registering geometry column metadata
     *   <li>Creating a spatial index
     * </ul>
     *
     * <p>A common case is creating an auto incrementing sequence for the primary key of a table. It
     * should be noted that all tables created through the datastore use the column "fid" as the
     * primary key.
     *
     * <p>A direct connection to the database is provided (<tt>cx</tt>). This connection must not be
     * closed, however any statements or result sets instantiated from the connection must be
     * closed.
     *
     * @param schemaName The name of the schema, may be <code>null</code>.
     * @param featureType The feature type that has just been created on the database.
     * @param cx Database connection.
     */
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {}

    /**
     * Callback which executes after an attribute descriptor has been built from a table column.
     *
     * <p>The result set <tt>columnMetadata</tt> should not be modified in any way (including
     * scrolling) , it should only be read from.
     *
     * <p>This base implementation does nothing, subclasses should override as need be.
     *
     * @param att The built attribute descriptor.
     * @param tableName The name of the table containing the column
     * @param schemaName The name of the database scheam containing the table containing the column
     * @param cx The database connection.
     */
    public void postCreateAttribute(
            AttributeDescriptor att, String tableName, String schemaName, Connection cx)
            throws SQLException {}

    /**
     * Callback which executes after a feature type has been built from a database table.
     *
     * <p>This base implementation does nothing, subclasses should override as need be.
     *
     * @param featureType The build feature type.
     * @param metadata The database metadata.
     * @param schemaName The name of the database scheam containing the table containing the column
     * @param cx The database connection.
     */
    public void postCreateFeatureType(
            SimpleFeatureType featureType,
            DatabaseMetaData metadata,
            String schemaName,
            Connection cx)
            throws SQLException {}

    /**
     * Callback which executes before a table is about to be dropped.
     *
     * <p>This base implementation does nothing, subclasses should override as need be.
     *
     * @param schemaName The database schema containing the table.
     * @param featureType The featureType/table being dropped.
     * @param cx The database connection.
     */
    public void preDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {}

    /**
     * Callback which executes after a table has been dropped.
     *
     * <p>This base implementation does nothing, subclasses should override as need be.
     *
     * @param schemaName The database schema containing the table.
     * @param featureType The featureType/table being dropped.
     * @param cx The database connection.
     */
    public void postDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {}

    /**
     * Controls whether keys are looked up post or pre insert.
     *
     * <p>When a row is inserted into a table, and a key is automatically generated it can be looked
     * up before the insert occurs, or after the insert has been made. Returning <code>false</code>
     * will cause the lookup to occur before the insert via {@link
     * #getNextAutoGeneratedValue(String, String, String, Connection)}. Returning <code>true</code>
     * will cause the lookup to occur after the insert via {@link #getLastAutoGeneratedValue(String,
     * String, String, Connection)}.
     *
     * <p>Subclasses returning false should implement:
     *
     * <ul>
     *   <li>{@link #getNextAutoGeneratedValue(String, String, String, Connection)}
     * </ul>
     *
     * <p>Subclasses returning true should implement:
     *
     * <ul>
     *   <li>{@link #getLastAutoGeneratedValue(String, String, String, Connection)}
     * </ul>
     */
    public boolean lookupGeneratedValuesPostInsert() {
        return false;
    }

    /**
     * Obtains the next value of an auto generated column.
     *
     * <p>Implementations should determine the next value of a column for which values are
     * automatically generated by the database.
     *
     * <p>This method is given a direct connection to the database, but this connection should never
     * be closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>Implementations should handle the case where <tt>schemaName</tt> is <code>null</code>.
     *
     * @param schemaName The schema name, this might be <code>null</code>.
     * @param tableName The name of the table.
     * @param columnName The column.
     * @param cx The database connection.
     * @return The next value of the column, or <code>null</code>.
     */
    public Object getNextAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return null;
    }

    /**
     * Obtains the last value of an auto generated column.
     *
     * <p>This method is only called when {@link #lookupGeneratedValuesPostInsert()} returns true.
     * Implementations should determine the previous value of a column for which was automatically
     * generated by the database.
     *
     * <p>This method is given a direct connection to the database, but this connection should never
     * be closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>Implementations should handle the case where <tt>schemaName</tt> is <code>null</code>.
     *
     * @param schemaName The schema name, this might be <code>null</code>.
     * @param tableName The name of the table.
     * @param columnName The column.
     * @param cx The database connection.
     * @param st The statement used for the insert
     * @return The previous value of the column, or <code>null</code>.
     */
    public Object getLastAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx, Statement st)
            throws SQLException {
        return getLastAutoGeneratedValue(schemaName, tableName, columnName, cx);
    }

    /**
     * Obtains the last value of an auto generated column.
     *
     * <p>This method is only called when {@link #lookupGeneratedValuesPostInsert()} returns true.
     * Implementations should determine the previous value of a column for which was automatically
     * generated by the database.
     *
     * <p>This method is given a direct connection to the database, but this connection should never
     * be closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>Implementations should handle the case where <tt>schemaName</tt> is <code>null</code>.
     *
     * @param schemaName The schema name, this might be <code>null</code>.
     * @param tableName The name of the table.
     * @param columnName The column.
     * @param cx The database connection.
     * @return The previous value of the column, or <code>null</code>.
     */
    public Object getLastAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return null;
    }

    /**
     * Determines the name of the sequence (if any) which is used to increment generate values for a
     * table column.
     *
     * <p>This method should return null if no such sequence exists.
     *
     * <p>This method is given a direct connection to the database, but this connection should never
     * be closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * @param schemaName The schema name, this might be <code>null</code>.
     * @param tableName The table name.
     * @param columnName The column name.
     * @param cx The database connection.
     */
    public String getSequenceForColumn(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        return null;
    }

    /**
     * Obtains the next value of a sequence, incrementing the sequence to the next state in the
     * process.
     *
     * <p>Implementations should determine the next value of a column for which values are
     * automatically generated by the database.
     *
     * <p>This method is given a direct connection to the database, but this connection should never
     * be closed. However any statements or result sets instantiated from the connection must be
     * closed.
     *
     * <p>Implementations should handle the case where <tt>schemaName</tt> is <code>null</code>.
     *
     * @param schemaName The schema name, this might be <code>null</code>.
     * @param sequenceName The name of the sequence.
     * @param cx The database connection.
     * @return The next value of the sequence, or <code>null</code>.
     */
    public Object getNextSequenceValue(String schemaName, String sequenceName, Connection cx)
            throws SQLException {
        return null;
    }

    /**
     * Encodes how to get the next sequence value from the DB.
     *
     * <p>Implementations should handle the case where <tt>schemaName</tt> is <code>null</code>.
     */
    public String encodeNextSequenceValue(String schemaName, String sequenceName) {
        return null;
    }

    /**
     * Returns true if this dialect can encode both {@linkplain Query#getStartIndex()} and
     * {@linkplain Query#getMaxFeatures()} into native SQL.
     */
    public boolean isLimitOffsetSupported() {
        return false;
    }

    /**
     * Returns true if this dialect supports sorting together with the given aggregation function.
     */
    public boolean isAggregatedSortSupported(String function) {
        return false;
    }

    /** Returns true if this dialect supports group by clause. */
    public boolean isGroupBySupported() {
        return true;
    }

    /**
     * Alters the query provided so that limit and offset are natively dealt with. This might mean
     * simply appending some extra directive to the query, or wrapping it into a bigger one.
     */
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        throw new UnsupportedOperationException(
                "Ovveride this method when isLimitOffsetSupported returns true");
    }

    /**
     * Add hints to the JDBC Feature Source. A subclass can override
     *
     * <p>possible hints (but not limited to)
     *
     * <p>{@link Hints#GEOMETRY_GENERALIZATION} {@link Hints#GEOMETRY_SIMPLIFICATION}
     */
    protected void addSupportedHints(Set<Hints.Key> hints) {}

    /**
     * Determines the default length that a varchar field should be when creating datastore tables
     * from feature types.
     *
     * <p>Some dialects allow no length to be specified for varchar fields (PostGIS for example)
     * however others require a maximum length to be set.
     *
     * <p>Subclasses can override this method and either return -1 to specify that no length is
     * required, or otherwise return an appropriate default length for varchars of that dialect.
     */
    public int getDefaultVarcharSize() {
        return 255;
    }

    /**
     * Determine if a read query should be set to autocommit.
     *
     * <p>Some databases (like postgres) want this enabled to respect fetch size. The default
     * implementation is to return false.
     *
     * @return true if read queries should remain autocommit, false otherwise
     */
    public boolean isAutoCommitQuery() {
        return false;
    }

    /**
     * Return <code>true</code> if the database supports individual schemas for indices.
     *
     * <p>The SQL encoding would be <code>CREATE INDEX SCHEMANAME.INDEXNAME ON ....</code>
     *
     * <p>The default is false and the encoding is <code>CREATE INDEX INDEXNAME ON ....</code>
     *
     * @return true or false
     */
    protected boolean supportsSchemaForIndex() {
        return false;
    }

    /**
     * Performs the class "create [unique] indexName on tableName(att1, att2, ..., attN)" call.
     *
     * <p>Subclasses can override to handle special indexes (like spatial ones) and/or the hints
     */
    public void createIndex(
            Connection cx, SimpleFeatureType schema, String databaseSchema, Index index)
            throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("CREATE ");
        if (index.isUnique()) {
            sql.append("UNIQUE ");
        }
        sql.append("INDEX ");
        if (supportsSchemaForIndex() && databaseSchema != null) {
            encodeSchemaName(databaseSchema, sql);
            sql.append(".");
        }
        sql.append(escapeName(index.getIndexName()));
        sql.append(" ON ");
        if (databaseSchema != null) {
            encodeSchemaName(databaseSchema, sql);
            sql.append(".");
        }
        sql.append(escapeName(index.getTypeName())).append("(");
        for (String attribute : index.getAttributes()) {
            sql.append(escapeName(attribute)).append(", ");
        }
        sql.setLength(sql.length() - 2);
        sql.append(")");

        Statement st = null;
        try {
            st = cx.createStatement();
            st.execute(sql.toString());
            if (!cx.getAutoCommit()) {
                cx.commit();
            }
        } finally {
            dataStore.closeSafe(st);
            dataStore.closeSafe(cx);
        }
    }

    /** Drop the index. Subclasses can override to handle extra syntax or db specific situations */
    public void dropIndex(
            Connection cx, SimpleFeatureType schema, String databaseSchema, String indexName)
            throws SQLException {
        StringBuffer sql = new StringBuffer();
        sql.append("DROP INDEX ");
        if (supportsSchemaForIndex() && databaseSchema != null) {
            encodeSchemaName(databaseSchema, sql);
            sql.append(".");
        }
        sql.append(escapeName(indexName));

        Statement st = null;
        try {
            st = cx.createStatement();
            st.execute(sql.toString());
            if (!cx.getAutoCommit()) {
                cx.commit();
            }
        } finally {
            dataStore.closeSafe(st);
            dataStore.closeSafe(cx);
        }
    }

    /**
     * Returns the list of indexes for a certain table. Subclasses can override to add support for
     * db specific hints
     */
    public List<Index> getIndexes(Connection cx, String databaseSchema, String typeName)
            throws SQLException {
        DatabaseMetaData md = cx.getMetaData();
        ResultSet indexInfo = null;
        try {
            indexInfo = md.getIndexInfo(cx.getCatalog(), databaseSchema, typeName, false, true);

            Map<String, Index> indexes = new LinkedHashMap<String, Index>();
            while (indexInfo.next()) {
                short type = indexInfo.getShort("TYPE");
                if (type != DatabaseMetaData.tableIndexStatistic) {
                    String indexName = indexInfo.getString("INDEX_NAME");
                    String columnName = indexInfo.getString("COLUMN_NAME");
                    Index index = indexes.get(indexName);
                    if (index != null) {
                        index.attributes.add(columnName);
                    } else {
                        boolean unique = !indexInfo.getBoolean("NON_UNIQUE");
                        index = new Index(typeName, indexName, unique, columnName);
                        indexes.put(indexName, index);
                    }
                }
            }

            return new ArrayList<Index>(indexes.values());
        } finally {
            dataStore.closeSafe(indexInfo);
        }
    }

    /**
     * Used to apply search hints on the fully generated SQL (complete of select, from, where, sort,
     * limit/offset)
     */
    public void handleSelectHints(StringBuffer sql, SimpleFeatureType featureType, Query query) {
        // nothing to do
    }

    /** @return Table types filtered from jdbc {@link DatabaseMetaData} */
    public String[] getDesiredTablesType() {
        return new String[] {"TABLE", "VIEW", "MATERIALIZED VIEW", "SYNONYM"};
    }

    /**
     * Splits the filter into two parts, an encodable one, and a non encodable one. The default
     * implementation uses the filter capabilities to split the filter, subclasses can implement
     * their own logic if need be.
     */
    public Filter[] splitFilter(Filter filter, SimpleFeatureType schema) {
        PostPreProcessFilterSplittingVisitor splitter =
                new PostPreProcessFilterSplittingVisitor(
                        dataStore.getFilterCapabilities(), schema, null);
        filter.accept(splitter, null);

        Filter[] split = new Filter[2];
        split[0] = splitter.getFilterPre();
        split[1] = splitter.getFilterPost();

        return split;
    }

    protected PrimaryKey getPrimaryKey(String typeName) throws IOException {
        SimpleFeatureType featureType = dataStore.getSchema(typeName);
        return dataStore.getPrimaryKey(featureType);
    }

    /**
     * Reads a primary key column value. By default uses {@link ResultSet#getString(int)},
     * subclasses can use a more efficient way should they wish to
     */
    public String getPkColumnValue(ResultSet rs, PrimaryKeyColumn pkey, int columnIdx)
            throws SQLException {
        return rs.getString(columnIdx);
    }

    /**
     * Returns if points can be returned in simplified form (e.g reduced precision, like TWKB
     * encoding)
     */
    public boolean canSimplifyPoints() {
        return false;
    }
}
