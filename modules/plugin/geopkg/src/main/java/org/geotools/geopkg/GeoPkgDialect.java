/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static java.lang.String.format;
import static org.geotools.geopkg.GeoPackage.GEOMETRY_COLUMNS;
import static org.geotools.geopkg.GeoPackage.GEOPACKAGE_CONTENTS;
import static org.geotools.geopkg.GeoPackage.SPATIAL_REF_SYS;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Level;
import org.geotools.feature.FeatureTypes;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.Geometries;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geopkg.Entry.DataType;
import org.geotools.geopkg.geom.GeoPkgGeomReader;
import org.geotools.geopkg.geom.GeoPkgGeomWriter;
import org.geotools.jdbc.EnumMapper;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.PreparedFilterToSQL;
import org.geotools.jdbc.PreparedStatementSQLDialect;
import org.geotools.jdbc.PrimaryKey;
import org.geotools.jdbc.PrimaryKeyColumn;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The GeoPackage SQL Dialect.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 */
public class GeoPkgDialect extends PreparedStatementSQLDialect {

    static final String HAS_SPATIAL_INDEX = "hasGeopkgSpatialIndex";

    protected GeoPkgGeomWriter.Configuration geomWriterConfig;

    public GeoPkgDialect(JDBCDataStore dataStore, GeoPkgGeomWriter.Configuration writerConfig) {
        super(dataStore);
        this.geomWriterConfig = writerConfig;
    }

    public GeoPkgDialect(JDBCDataStore dataStore) {
        super(dataStore);
        geomWriterConfig = new GeoPkgGeomWriter.Configuration();
    }

    @Override
    public void initializeConnection(Connection cx) throws SQLException {
        GeoPackage.init(cx);
    }

    @Override
    public boolean includeTable(String schemaName, String tableName, Connection cx)
            throws SQLException {
        Statement st = cx.createStatement();

        try {
            ResultSet rs =
                    st.executeQuery(
                            String.format(
                                    "SELECT * FROM gpkg_contents WHERE"
                                            + " table_name = '%s' AND data_type = '%s'",
                                    tableName, DataType.Feature.value()));
            try {
                return rs.next();
            } finally {
                rs.close();
            }
        } finally {
            dataStore.closeSafe(st);
        }
    }

    @Override
    public void encodePrimaryKey(String column, StringBuffer sql) {
        super.encodePrimaryKey(column, sql);
        sql.append(" AUTOINCREMENT");
    }

    @Override
    public void encodeGeometryEnvelope(String tableName, String geometryColumn, StringBuffer sql) {
        encodeColumnName(null, geometryColumn, sql);
    }

    @Override
    public Envelope decodeGeometryEnvelope(ResultSet rs, int column, Connection cx)
            throws SQLException, IOException {
        Geometry g = geometry(rs.getBytes(column));
        return g != null ? g.getEnvelopeInternal() : null;
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            String column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return geometry(descriptor.getType().getBinding(), rs.getBytes(column), factory, hints);
    }

    @Override
    public Geometry decodeGeometryValue(
            GeometryDescriptor descriptor,
            ResultSet rs,
            int column,
            GeometryFactory factory,
            Connection cx,
            Hints hints)
            throws IOException, SQLException {
        return geometry(descriptor.getType().getBinding(), rs.getBytes(column), factory, hints);
    }

    @Override
    public void setGeometryValue(
            Geometry g, int dimension, int srid, Class binding, PreparedStatement ps, int column)
            throws SQLException {
        if (g == null || g.isEmpty()) {
            ps.setNull(column, Types.BLOB);
        } else {
            g.setSRID(srid);
            try {
                ps.setBytes(column, new GeoPkgGeomWriter(dimension, geomWriterConfig).write(g));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /** */
    private Geometry geometry(
            Class geometryType, byte[] bytes, GeometryFactory factory, Hints hints)
            throws IOException {
        GeoPkgGeomReader geoPkgGeomReader = new GeoPkgGeomReader(bytes);
        geoPkgGeomReader.setFactory(factory);
        geoPkgGeomReader.setHints(hints);
        geoPkgGeomReader.setGeometryType(geometryType);
        return bytes != null ? geoPkgGeomReader.get() : null;
    }

    Geometry geometry(byte[] b) throws IOException {
        return geometry(null, b, null, null);
    }

    @Override
    public String getGeometryTypeName(Integer type) {
        return Geometries.getForSQLType(type).getName();
    }

    @Override
    public void registerSqlTypeNameToClassMappings(Map<String, Class<?>> mappings) {
        super.registerSqlTypeNameToClassMappings(mappings);
        mappings.put("DOUBLE", Double.class);
        mappings.put("BOOLEAN", Boolean.class);
        mappings.put("DATE", java.sql.Date.class);
        mappings.put("TIMESTAMP", java.sql.Timestamp.class);
        mappings.put("TIME", java.sql.Time.class);
        mappings.put("DATETIME", java.sql.Timestamp.class);
    }

    @Override
    public void registerClassToSqlMappings(Map<Class<?>, Integer> mappings) {
        super.registerClassToSqlMappings(mappings);
        // add geometry mappings
        for (Geometries g : Geometries.values()) {
            mappings.put(g.getBinding(), g.getSQLType());
        }
        // override some internal defaults
        mappings.put(Byte.class, Types.TINYINT);
        mappings.put(Short.class, Types.SMALLINT);
        mappings.put(Long.class, Types.BIGINT);
        mappings.put(Integer.class, Types.INTEGER);
        mappings.put(Double.class, Types.REAL);
        mappings.put(Boolean.class, Types.INTEGER);
    }

    @Override
    public void registerSqlTypeToSqlTypeNameOverrides(Map<Integer, String> overrides) {
        super.registerSqlTypeToSqlTypeNameOverrides(overrides);

        // The following SQL Data Types are just decorative in SQLite
        // (see https://www.sqlite.org/datatype3.html),
        // but will allow GeoTools to handle some usual java.sql.Types
        // not mapped to raw SQL types by org.sqlite.jdbc3.JDBC3DatabaseMetaData.getTypeInfo()

        // Numbers
        overrides.put(Types.BOOLEAN, "BOOLEAN");
        overrides.put(Types.TINYINT, "TINYINT");
        overrides.put(Types.SMALLINT, "SMALLINT");
        overrides.put(Types.INTEGER, "MEDIUMINT");
        overrides.put(Types.BIGINT, "INTEGER");
        overrides.put(Types.DOUBLE, "DOUBLE");
        overrides.put(Types.NUMERIC, "NUMERIC");

        // Temporal
        overrides.put(Types.DATE, "DATE");
        overrides.put(Types.TIME, "TIME");
        overrides.put(Types.TIMESTAMP, "TIMESTAMP");
    }

    @Override
    public Class<?> getMapping(ResultSet columns, Connection cx) throws SQLException {
        String tbl = columns.getString("TABLE_NAME");
        String col = columns.getString("COLUMN_NAME");
        String typeName = columns.getString("TYPE_NAME");

        String sql =
                format(
                        "SELECT b.geometry_type_name"
                                + " FROM %s a, %s b"
                                + " WHERE a.table_name = b.table_name"
                                + " AND b.table_name = ?"
                                + " AND b.column_name = ?",
                        GEOPACKAGE_CONTENTS, GEOMETRY_COLUMNS);

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(String.format("%s; 1=%s, 2=%s", sql, tbl, col));
        }

        try (PreparedStatement ps = cx.prepareStatement(sql)) {
            ps.setString(1, tbl);
            ps.setString(2, col);

            ResultSet rs = ps.executeQuery();
            try {
                if (rs.next()) {
                    String t = rs.getString(1);
                    Geometries g = Geometries.getForName(t);
                    if (g != null) {
                        return g.getBinding();
                    }
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        }

        // if it's an enumeration, switch data type to string
        @SuppressWarnings("PMD.CloseResource") // using the pool of the store, no need to close
        GeoPackage geoPackage = geopkg();
        try {
            GeoPkgSchemaExtension extension = geoPackage.getExtension(GeoPkgSchemaExtension.class);
            List<DataColumn> dataColumns = extension.getDataColumns(tbl, cx);
            for (DataColumn dataColumn : dataColumns) {
                if (col.equals(dataColumn.getColumnName())) {
                    DataColumnConstraint constraint = dataColumn.getConstraint();
                    if (constraint instanceof DataColumnConstraint.Enum) {
                        return String.class;
                    }
                }
            }
        } catch (IOException e) {
            throwSQLException(e);
        }

        // handle GeoPackage integer type expectations
        if ("TINYINT".equals(typeName)) return Byte.class;
        else if ("SMALLINT".equals(typeName)) return Short.class;
        else if ("MEDIUMINT".equals(typeName)) return Integer.class;
        else if ("INT".equals(typeName) || "INTEGER".equals(typeName)) return Long.class;

        return null;
    }

    @Override
    public Filter getRestrictions(ResultSet columns, Connection cx) throws SQLException {
        String tbl = columns.getString("TABLE_NAME");
        String col = columns.getString("COLUMN_NAME");

        // if it's an enumeration, switch data type to string
        @SuppressWarnings("PMD.CloseResource") // using the pool of the store, no need to close
        GeoPackage geoPackage = geopkg();
        try {
            GeoPkgSchemaExtension schemas = geoPackage.getExtension(GeoPkgSchemaExtension.class);
            List<DataColumn> dataColumns = schemas.getDataColumns(tbl, cx);
            for (DataColumn dataColumn : dataColumns) {
                if (col.equals(dataColumn.getColumnName())) {
                    DataColumnConstraint constraint = dataColumn.getConstraint();
                    if (constraint instanceof DataColumnConstraint.Enum) {
                        DataColumnConstraint.Enum ec = (DataColumnConstraint.Enum) constraint;
                        return FeatureTypes.createFieldOptions(ec.getValues().values());
                    }
                }
            }
        } catch (IOException e) {
            throwSQLException(e);
        }

        return null;
    }

    private void throwSQLException(IOException e) throws SQLException {
        if (e.getCause() instanceof SQLException) {
            throw (SQLException) e.getCause();
        } else {
            throw new SQLException(e);
        }
    }

    @Override
    public void postCreateTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {

        FeatureEntry fe = (FeatureEntry) featureType.getUserData().get(FeatureEntry.class);
        if (fe == null) {
            fe = new FeatureEntry();
            fe.setIdentifier(featureType.getTypeName());
            fe.setDescription(featureType.getTypeName());
            fe.setTableName(featureType.getTypeName());
            fe.setLastChange(new java.util.Date());
        }

        GeometryDescriptor gd = featureType.getGeometryDescriptor();
        if (gd != null) {
            fe.setGeometryColumn(gd.getLocalName());
            fe.setGeometryType(Geometries.getForBinding((Class) gd.getType().getBinding()));
        }

        CoordinateReferenceSystem crs = featureType.getCoordinateReferenceSystem();
        if (crs != null) {
            if (DefaultEngineeringCRS.GENERIC_2D == crs) {
                fe.setSrid(GeoPackage.GENERIC_PROJECTED_SRID);
            } else {
                try {
                    Integer epsgCode = CRS.lookupEpsgCode(crs, true);
                    if (epsgCode != null) {
                        fe.setSrid(epsgCode);
                    }
                } catch (FactoryException e) {
                    LOGGER.log(Level.WARNING, "Error looking up epsg code for " + crs, e);
                }
            }
        }

        @SuppressWarnings("PMD.CloseResource") // using the pool of the store, no need to close
        GeoPackage geopkg = geopkg();
        try {
            geopkg.addGeoPackageContentsEntry(fe, cx);
            geopkg.addGeometryColumnsEntry(fe, cx);

            // other geometry columns are possible
            for (PropertyDescriptor descr : featureType.getDescriptors()) {
                if (descr instanceof GeometryDescriptor) {
                    GeometryDescriptor gd1 = (GeometryDescriptor) descr;
                    if (!(gd1.getLocalName()).equals(fe.getGeometryColumn())) {
                        FeatureEntry fe1 = new FeatureEntry();
                        fe1.init(fe);
                        fe1.setGeometryColumn(gd1.getLocalName());
                        fe1.setGeometryType(
                                Geometries.getForBinding((Class) gd1.getType().getBinding()));
                        geopkg.addGeometryColumnsEntry(fe1, cx);
                    }
                }
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }

        // check if enum columns need to be declared, and do so
        for (AttributeDescriptor ad : featureType.getAttributeDescriptors()) {
            DataColumn dc = (DataColumn) ad.getUserData().get(GeoPackage.DATA_COLUMN);
            if (dc != null) {
                if (!ad.getLocalName().equals(dc.getColumnName())) {
                    throw new IllegalArgumentException(
                            "Expected column name "
                                    + ad.getLocalName()
                                    + " but got"
                                    + dc.getColumnName());
                }
                geopkg.getExtension(GeoPkgSchemaExtension.class)
                        .addDataColumn(featureType.getTypeName(), dc, cx);
            } else {
                List<?> options = FeatureTypes.getFieldOptions(ad);
                if (options != null && !options.isEmpty()) {
                    dc = new DataColumn();
                    dc.setColumnName(ad.getLocalName());
                    dc.setName(ad.getLocalName());
                    Map<String, String> optionsMap = new LinkedHashMap<>();
                    for (int i = 0; i < options.size(); i++) {
                        optionsMap.put(String.valueOf(i), String.valueOf(options.get(i)));
                    }
                    String constraintName =
                            featureType.getTypeName() + "_" + ad.getLocalName() + "_enum";
                    DataColumnConstraint.Enum dcc =
                            new DataColumnConstraint.Enum(constraintName, optionsMap);
                    dc.setConstraint(dcc);
                    geopkg.getExtension(GeoPkgSchemaExtension.class)
                            .addDataColumn(featureType.getTypeName(), dc, cx);
                }
            }
        }
    }

    @Override
    public void postDropTable(String schemaName, SimpleFeatureType featureType, Connection cx)
            throws SQLException {
        super.postDropTable(schemaName, featureType, cx);
        FeatureEntry fe = (FeatureEntry) featureType.getUserData().get(FeatureEntry.class);
        if (fe == null) {
            fe = new FeatureEntry();
            fe.setIdentifier(featureType.getTypeName());
            fe.setDescription(featureType.getTypeName());
            fe.setTableName(featureType.getTypeName());
        }
        @SuppressWarnings("PMD.CloseResource") // using the pool of the store, no need to close
        GeoPackage geopkg = geopkg();
        try {
            geopkg.deleteGeoPackageContentsEntry(fe);
            geopkg.deleteGeometryColumnsEntry(fe);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public Integer getGeometrySRID(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        try {
            FeatureEntry fe = geopkg().feature(tableName, cx);
            return fe != null ? fe.getSrid() : null;
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public int getGeometryDimension(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        try {
            FeatureEntry fe = geopkg().feature(tableName, cx);
            if (fe != null) {
                return 2 + (fe.isZ() ? 1 : 0) + (fe.isM() ? 1 : 0);
            } else { // fallback - shouldn't happen
                return super.getGeometryDimension(schemaName, tableName, columnName, cx);
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public CoordinateReferenceSystem createCRS(int srid, Connection cx) throws SQLException {
        try {
            return GeoPackage.decodeSRID(srid);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Unable to create CRS from epsg code " + srid, e);

            // try looking up in spatial ref sys
            String sql =
                    String.format(
                            "SELECT definition FROM %s WHERE auth_srid = %d",
                            SPATIAL_REF_SYS, srid);
            LOGGER.fine(sql);

            Statement st = cx.createStatement();
            ResultSet rs = st.executeQuery(sql);
            try {
                if (rs.next()) {
                    String wkt = rs.getString(1);
                    try {
                        return CRS.parseWKT(wkt);
                    } catch (Exception e2) {
                        LOGGER.log(Level.FINE, "Unable to create CRS from wkt: " + wkt, e2);
                    }
                }
            } finally {
                dataStore.closeSafe(rs);
                dataStore.closeSafe(st);
            }
        }

        return super.createCRS(srid, cx);
    }

    @Override
    public boolean lookupGeneratedValuesPostInsert() {
        // the JDBC driver does not support returning multiple
        // keys, but statement batching provides a big boost in terms
        // of insertion performance. So allow retrieving the keys correctly
        // when the insert batch size is 1, but avoid errors when they are not
        // (for code using GeoPackage.add(...) to add lots of features, with
        // no interest in the generated primary keys
        return dataStore.getBatchInsertSize() == 1;
    }

    @Override
    public Object getLastAutoGeneratedValue(
            String schemaName, String tableName, String columnName, Connection cx)
            throws SQLException {
        Statement st = cx.createStatement();
        try {
            ResultSet rs = st.executeQuery("SELECT last_insert_rowid()");
            try {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } finally {
                dataStore.closeSafe(rs);
            }
        } finally {
            dataStore.closeSafe(st);
        }

        return null;
    }

    GeoPackage geopkg() {
        return new GeoPackage(dataStore);
    }

    @Override
    public boolean isLimitOffsetSupported() {

        return true;
    }

    @Override
    public void applyLimitOffset(StringBuffer sql, int limit, int offset) {
        if (limit > 0 && limit < Integer.MAX_VALUE) {
            sql.append(" LIMIT " + limit);
            if (offset > 0) {
                sql.append(" OFFSET " + offset);
            }
        } else if (offset > 0) {
            // see
            // https://stackoverflow.com/questions/10491492/sqllite-with-skip-offset-only-not-limit
            sql.append(" LIMIT -1");
            sql.append(" OFFSET " + offset);
        }
    }

    @Override
    public PreparedFilterToSQL createPreparedFilterToSQL() {
        GeoPkgFilterToSQL fts = new GeoPkgFilterToSQL(this);
        return fts;
    }

    @Override
    public void setValue(
            Object value, Class binding, PreparedStatement ps, int column, Connection cx)
            throws SQLException {
        // get the sql type
        Integer sqlType = dataStore.getMapping(binding);

        // handl null case
        if (value == null) {
            ps.setNull(column, sqlType);
            return;
        }

        switch (sqlType) {
            case Types.DATE:
                ps.setString(column, ((Date) value).toString());
                break;
            case Types.TIME:
                ps.setString(column, ((Time) value).toString());
                break;
            case Types.TIMESTAMP:
                // 2020-02-19 23:00:00.0  --> 2020-02-19 23:00:00.0Z
                // We need the "Z" or sql lite will interpret the value as local time
                ps.setString(column, ((Timestamp) value).toString() + "Z");
                break;
            default:
                super.setValue(value, binding, ps, column, cx);
        }
    }

    @Override
    public void encodeColumnType(String sqlTypeName, StringBuffer sql) {
        sql.append(sqlTypeName.toUpperCase()); // may keep cite tests happy about geom names
    }

    @Override
    public void postCreateAttribute(
            AttributeDescriptor att, String tableName, String schemaName, Connection cx)
            throws SQLException {
        super.postCreateAttribute(att, tableName, schemaName, cx);

        String attributeName = att.getLocalName();
        if (att instanceof GeometryDescriptor) {
            String sql =
                    "SELECT * FROM gpkg_extensions WHERE (lower(table_name)=lower('"
                            + tableName
                            + "') "
                            + "AND lower(column_name)=lower('"
                            + attributeName
                            + "') "
                            + "AND extension_name='gpkg_rtree_index')";
            try (Statement st = cx.createStatement();
                    ResultSet rs = st.executeQuery(sql)) {
                // did we get a result?
                boolean hasSpatialIndex = rs.next();
                att.getUserData().put(HAS_SPATIAL_INDEX, hasSpatialIndex);
            }
        }

        try {
            if (FeatureTypes.getFieldOptions(att) != null) {
                List<DataColumn> dataColumns =
                        geopkg().getExtension(GeoPkgSchemaExtension.class)
                                .getDataColumns(tableName, cx);
                for (DataColumn dataColumn : dataColumns) {
                    // little hack here, GeoPackage schema extension is being evolved so that
                    // an array of values can be stored as a JSON array, in that case the enum
                    // is applied to every entry in the array, not to the whole string, so the
                    // normal enum mapping does not apply. If a column declares a mime type, its
                    // contents are likely complex, and the enum will have to be applied
                    // to the items inside, not to the whole, so the EnumMapper must not be created.
                    if (attributeName.equals(dataColumn.getColumnName())
                            && dataColumn.getMimeType() == null) {
                        DataColumnConstraint.Enum dcc =
                                (DataColumnConstraint.Enum) dataColumn.getConstraint();
                        EnumMapper mapper = new EnumMapper();
                        for (Map.Entry<String, String> entry : dcc.getValues().entrySet()) {
                            mapper.addMapping(Integer.valueOf(entry.getKey()), entry.getValue());
                        }
                        att.getUserData().put(JDBCDataStore.JDBC_ENUM_MAP, mapper);
                    }
                }
            }
        } catch (IOException e) {
            throwSQLException(e);
        }
    }

    @Override
    public Filter[] splitFilter(Filter filter, SimpleFeatureType schema) {
        // sqlite does not have ST_* function support but can do a rtree search, assuming
        // there are rtrees to hit
        // This implementation only supports figuring a bbox in case there is a single
        // indexed spatial attribute (could be extended to use multiple spatial attributes if need
        // be)
        final GeometryDescriptor searchAttribute = simpleSpatialSearch(filter, schema);
        if (searchAttribute != null) {
            Envelope envelope =
                    (Envelope) filter.accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, null);
            if (envelope != null
                    && !envelope.isNull()
                    && !Double.isInfinite(envelope.getWidth())
                    && !Double.isInfinite(envelope.getHeight())) {
                // split assuming there is no spatial support
                Filter[] split = super.splitFilter(filter, schema);
                FilterFactory ff = dataStore.getFilterFactory();
                BBOX bbox =
                        ff.bbox(
                                searchAttribute.getLocalName(),
                                envelope.getMinX(),
                                envelope.getMinY(),
                                envelope.getMaxX(),
                                envelope.getMaxY(),
                                null);
                split[0] = ff.and(split[0], bbox);

                return split;
            }
        }
        // fallback, use normal encoding
        return super.splitFilter(filter, schema);
    }

    /**
     * Checks if the filter uses a single spatial attribute, and such spatial attribute is indexed
     */
    private GeometryDescriptor simpleSpatialSearch(Filter filter, SimpleFeatureType schema) {
        FilterAttributeExtractor attributeExtractor = new FilterAttributeExtractor();
        filter.accept(attributeExtractor, null);
        Set<String> attributes = attributeExtractor.getAttributeNameSet();
        GeometryDescriptor geometryAttribute = null;
        for (String name : attributes) {
            // handle default geometry case
            AttributeDescriptor ad;
            if ("".equals(name)) {
                ad = schema.getGeometryDescriptor();
            } else {
                ad = schema.getDescriptor(name);
            }
            // check if geometris
            if (ad instanceof GeometryDescriptor) {
                if (geometryAttribute != null) {
                    // two different attributes found
                    return null;
                }
                // can run both on spatial filters and without
                geometryAttribute = (GeometryDescriptor) ad;
            }
        }

        return geometryAttribute;
    }

    @Override
    protected PrimaryKey getPrimaryKey(String typeName) throws IOException {
        return super.getPrimaryKey(typeName);
    }

    @Override
    protected <T> T convert(Object value, Class<T> binding) {
        if (Integer.class.equals(binding) && value instanceof Boolean) {
            return (T) Integer.valueOf(Boolean.TRUE.equals(value) ? 1 : 0);
        }
        return super.convert(value, binding);
    }

    @Override
    public String getPkColumnValue(ResultSet rs, PrimaryKeyColumn pkey, int columnIdx)
            throws SQLException {
        // by spec primary key of a GeoPackage column is integer, but maybe other tables do not
        // follow this rule. Calling rs.getInt avoids an inefficient block of code in sqlite driver
        if (Integer.class.equals(pkey.getType())) {
            // primary keys cannot contain null values, so no need to check rs.wasNull() and
            // thus saving an expensive native call
            return String.valueOf(rs.getInt(columnIdx));
        } else {
            return rs.getString(columnIdx);
        }
    }

    @Override
    protected void addSupportedHints(Set<Hints.Key> hints) {
        hints.add(Hints.GEOMETRY_DISTANCE);
        hints.add(Hints.SCREENMAP);
    }

    /**
     * SQLite dates are just strings, they don't get converted to Date in case of aggregation, do it
     * here instead
     */
    @Override
    public Function<Object, Object> getAggregateConverter(
            FeatureVisitor visitor, SimpleFeatureType featureType) {
        Optional<List<Class>> maybeResultTypes = getResultTypes(visitor, featureType);
        if (maybeResultTypes.isPresent()) {
            List<Class> resultTypes = maybeResultTypes.get();
            if (resultTypes.size() == 1) {
                Class targetType = resultTypes.get(0);
                if (java.util.Date.class.isAssignableFrom(targetType)) {
                    return v -> {
                        Object converted = Converters.convert(v, targetType);
                        if (converted == null) {
                            LOGGER.log(
                                    Level.WARNING,
                                    "Could not convert "
                                            + v
                                            + " to the desired return type "
                                            + targetType);
                            return v;
                        }
                        return converted;
                    };
                }
            }
        }
        // otherwise no conversion needed
        return Function.identity();
    }

    @Override
    public Integer getSQLType(AttributeDescriptor ad) {
        // enumerations are mapped to integer values
        if (FeatureTypes.getFieldOptions(ad) != null) {
            return Types.INTEGER;
        }

        return null;
    }

    @Override
    public List<ReferencedEnvelope> getOptimizedBounds(
            String schema, SimpleFeatureType featureType, Connection cx)
            throws SQLException, IOException {
        FeatureEntry entry = geopkg().feature(featureType.getTypeName(), cx);
        if (entry != null && entry.getBounds() != null) {
            ReferencedEnvelope bounds = entry.getBounds();
            // make sure the bounds are not empty of the default 0,0,0,0
            if (!bounds.isEmpty()
                    && !bounds.isNull()
                    && !(bounds.getMinX() == 0
                            && bounds.getMinY() == 0
                            && bounds.getMaxX() == 0
                            && bounds.getMaxY() == 0)) {
                return Arrays.asList(entry.getBounds());
            }
        }

        return null;
    }
}
