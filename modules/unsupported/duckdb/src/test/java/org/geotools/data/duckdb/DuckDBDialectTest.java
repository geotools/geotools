/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDataStore;
import org.junit.Test;
import org.mockito.Mockito;

// Mock objects only; no real JDBC resources are opened in this test class.
@SuppressWarnings({"PMD.CloseResource", "PMD.CheckResultSet"})
public class DuckDBDialectTest {

    private DuckDBDialect createDialect() {
        return new DuckDBDialect(new JDBCDataStore());
    }

    @Test
    public void testDesiredTableTypesIncludesBaseTable() {
        DuckDBDialect dialect = createDialect();

        assertArrayEquals(
                new String[] {"TABLE", "BASE TABLE", "VIEW", "MATERIALIZED VIEW", "SYNONYM"},
                dialect.getDesiredTablesType());
    }

    @Test
    public void testGetMappingRecognizesNativeGeometryColumns() throws Exception {
        DuckDBDialect dialect = createDialect();
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("GEOMETRY");

        assertEquals(org.locationtech.jts.geom.Geometry.class, dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testGetMappingLeavesNonGeometryTypesToDefaultHandling() throws Exception {
        DuckDBDialect dialect = createDialect();
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("VARCHAR");

        assertNull(dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testGetMappingUsesParentMappingFallbackForNonGeometryTypes() throws Exception {
        DuckDBDialect dialect = new ParentMappingDuckDBDialect(String.class);
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("VARCHAR");

        assertEquals(String.class, dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testGetMappingRecognizesTimestampSecondPrecisionTypeName() throws Exception {
        DuckDBDialect dialect = createDialect();
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("TIMESTAMP_S");

        assertEquals(Timestamp.class, dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testGetMappingRecognizesLongTimestampWithTimeZoneTypeName() throws Exception {
        DuckDBDialect dialect = createDialect();
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("TIMESTAMP WITH TIME ZONE");

        assertEquals(Timestamp.class, dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testGetMappingRecognizesTimestamptzAliasTypeName() throws Exception {
        DuckDBDialect dialect = createDialect();
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("TIMESTAMPTZ");

        assertEquals(Timestamp.class, dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testGetMappingRecognizesLongTimeWithTimeZoneTypeName() throws Exception {
        DuckDBDialect dialect = createDialect();
        ResultSet columnMetaData = Mockito.mock(ResultSet.class);
        Connection cx = Mockito.mock(Connection.class);

        Mockito.when(columnMetaData.getString("TYPE_NAME")).thenReturn("TIME WITH TIME ZONE");

        assertEquals(Time.class, dialect.getMapping(columnMetaData, cx));
    }

    @Test
    public void testRegisterSqlTypeToClassMappingsIncludesTemporalTimezoneTypes() {
        DuckDBDialect dialect = createDialect();
        Map<Integer, Class<?>> mappings = new HashMap<>();

        dialect.registerSqlTypeToClassMappings(mappings);

        assertEquals(Time.class, mappings.get(Types.TIME_WITH_TIMEZONE));
        assertEquals(Timestamp.class, mappings.get(Types.TIMESTAMP_WITH_TIMEZONE));
    }

    @Test
    public void testRegisterSqlTypeNameToClassMappingsIncludesTemporalAliases() {
        DuckDBDialect dialect = createDialect();
        Map<String, Class<?>> mappings = new HashMap<>();

        dialect.registerSqlTypeNameToClassMappings(mappings);

        assertEquals(Time.class, mappings.get("TIME_NS"));
        assertEquals(Time.class, mappings.get("TIMETZ"));
        assertEquals(Time.class, mappings.get("TIME WITH TIME ZONE"));
        assertEquals(Timestamp.class, mappings.get("TIMESTAMP_S"));
        assertEquals(Timestamp.class, mappings.get("TIMESTAMP_MS"));
        assertEquals(Timestamp.class, mappings.get("TIMESTAMP_NS"));
        assertEquals(Timestamp.class, mappings.get("TIMESTAMPTZ"));
        assertEquals(Timestamp.class, mappings.get("TIMESTAMP WITH TIME ZONE"));
    }

    @Test
    public void testConvertValueConvertsOffsetTimeToSqlTime() {
        DuckDBDialect dialect = createDialect();
        AttributeDescriptor descriptor = mockDescriptor(Time.class);
        OffsetTime value = OffsetTime.parse("12:34:56+02:00");

        Object converted = dialect.convertValue(value, descriptor);

        assertEquals(Time.class, converted.getClass());
        assertEquals(Time.valueOf(LocalTime.of(12, 34, 56)), converted);
    }

    @Test
    public void testConvertValueConvertsTimeWithTimezoneTextToSqlTime() {
        DuckDBDialect dialect = createDialect();
        AttributeDescriptor descriptor = mockDescriptor(Time.class);

        Object converted = dialect.convertValue("12:34:56+02:00", descriptor);

        assertEquals(Time.class, converted.getClass());
        assertEquals(Time.valueOf(LocalTime.of(12, 34, 56)), converted);
    }

    @Test
    public void testConvertValueConvertsLocalTimeTextWithNanosToSqlTime() {
        DuckDBDialect dialect = createDialect();
        AttributeDescriptor descriptor = mockDescriptor(Time.class);

        Object converted = dialect.convertValue("12:34:56.123456789", descriptor);

        assertEquals(Time.class, converted.getClass());
        assertEquals(Time.valueOf(LocalTime.of(12, 34, 56, 123456789)), converted);
    }

    @Test
    public void testConvertValueLeavesInvalidTimeTextUnchanged() {
        DuckDBDialect dialect = createDialect();
        AttributeDescriptor descriptor = mockDescriptor(Time.class);

        Object converted = dialect.convertValue("not-a-time", descriptor);

        assertNull(converted);
    }

    @Test
    public void testEncodeGeometryColumnGeneralizedFallsBackCleanlyWhenDistanceIsNull() {
        DuckDBDialect dialect = createDialect();
        GeometryDescriptor descriptor = Mockito.mock(GeometryDescriptor.class);
        StringBuffer sql = new StringBuffer();

        Mockito.when(descriptor.getLocalName()).thenReturn("geom");

        dialect.encodeGeometryColumnGeneralized(descriptor, "features", 4326, sql, null);

        assertEquals("ST_AsWKB(\"features\".\"geom\"::GEOMETRY)::BLOB", sql.toString());
    }

    @Test
    public void testEncodeGeometryColumnGeneralizedUsesAliasWhenPresent() {
        DuckDBDialect dialect = createDialect();
        GeometryDescriptor descriptor = Mockito.mock(GeometryDescriptor.class);
        StringBuffer sql = new StringBuffer();

        Mockito.when(descriptor.getLocalName()).thenReturn("geom");

        dialect.encodeGeometryColumnGeneralized(descriptor, "features", 4326, sql, 12.5);

        assertEquals(
                "ST_AsWKB(ST_SimplifyPreserveTopology(\"features\".\"geom\"::GEOMETRY, 12.5))::BLOB", sql.toString());
    }

    @Test
    public void testEncodeNextSequenceValueUsesEscapedIdentifier() {
        DuckDBDialect dialect = createDialect();

        assertEquals("nextval('\"seq\"')", dialect.encodeNextSequenceValue(null, "seq"));
    }

    @Test
    public void testEncodeNextSequenceValueEscapesSchemaAndSingleQuotes() {
        DuckDBDialect dialect = createDialect();

        assertEquals("nextval('\"custom\".\"seq''name\"')", dialect.encodeNextSequenceValue("custom", "seq'name"));
    }

    @Test
    public void testGetGeometrySRIDReturnsNullWhenStSridFunctionIsUnavailable() throws Exception {
        DuckDBDialect dialect = createDialect();
        Connection cx = Mockito.mock(Connection.class);
        Statement probeStatement = Mockito.mock(Statement.class);
        ResultSet probeResult = Mockito.mock(ResultSet.class);

        Mockito.when(cx.createStatement()).thenReturn(probeStatement);
        Mockito.when(probeStatement.executeQuery(contains("duckdb_functions"))).thenReturn(probeResult);
        Mockito.doReturn(false).when(probeResult).next();

        Integer srid = dialect.getGeometrySRID(null, "ft1", "geometry", cx);

        assertNull(srid);
        verify(cx, times(1)).createStatement();
        verify(probeStatement, never())
                .executeQuery(eq("SELECT ST_SRID(\"geometry\") FROM \"ft1\" WHERE \"geometry\" IS NOT NULL LIMIT 1"));
    }

    @Test
    public void testGetGeometrySRIDReadsSridWhenStSridIsAvailable() throws Exception {
        DuckDBDialect dialect = createDialect();
        Connection cx = Mockito.mock(Connection.class);
        Statement probeStatement = Mockito.mock(Statement.class);
        ResultSet probeResult = Mockito.mock(ResultSet.class);
        Statement sridStatement = Mockito.mock(Statement.class);
        ResultSet sridResult = Mockito.mock(ResultSet.class);

        Mockito.when(cx.createStatement()).thenReturn(probeStatement, sridStatement);
        Mockito.when(probeStatement.executeQuery(contains("duckdb_functions"))).thenReturn(probeResult);
        Mockito.doReturn(true).when(probeResult).next();
        Mockito.when(sridStatement.executeQuery(contains("SELECT ST_SRID"))).thenReturn(sridResult);
        Mockito.doReturn(true).when(sridResult).next();
        Mockito.when(sridResult.getInt(1)).thenReturn(4326);

        Integer srid = dialect.getGeometrySRID(null, "ft1", "geometry", cx);

        assertEquals(Integer.valueOf(4326), srid);
        verify(cx, times(2)).createStatement();
    }

    @Test
    public void testGetGeometrySRIDDoesNotReprobeWhenFunctionAbsenceIsCached() throws Exception {
        DuckDBDialect dialect = createDialect();
        Connection cx = Mockito.mock(Connection.class);
        Statement probeStatement = Mockito.mock(Statement.class);
        ResultSet probeResult = Mockito.mock(ResultSet.class);

        Mockito.when(cx.createStatement()).thenReturn(probeStatement);
        Mockito.when(probeStatement.executeQuery(contains("duckdb_functions"))).thenReturn(probeResult);
        Mockito.doReturn(false).when(probeResult).next();

        assertNull(dialect.getGeometrySRID(null, "ft1", "geometry", cx));
        assertNull(dialect.getGeometrySRID(null, "ft1", "geometry", cx));

        verify(cx, times(1)).createStatement();
    }

    @Test
    public void testGetGeometrySRIDThrowsWhenQueryFailsDespiteAvailableFunction() throws Exception {
        DuckDBDialect dialect = createDialect();
        Connection cx = Mockito.mock(Connection.class);
        Statement probeStatement = Mockito.mock(Statement.class);
        ResultSet probeResult = Mockito.mock(ResultSet.class);
        Statement sridStatement = Mockito.mock(Statement.class);

        Mockito.when(cx.createStatement()).thenReturn(probeStatement, sridStatement);
        Mockito.when(probeStatement.executeQuery(contains("duckdb_functions"))).thenReturn(probeResult);
        Mockito.doReturn(true).when(probeResult).next();
        Mockito.when(sridStatement.executeQuery(contains("SELECT ST_SRID"))).thenThrow(new SQLException("boom"));

        try {
            dialect.getGeometrySRID(null, "ft1", "geometry", cx);
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("boom", e.getMessage());
        }
    }

    @Test
    public void testGetGeometrySRIDThrowsWhenCapabilityProbeFails() throws Exception {
        DuckDBDialect dialect = createDialect();
        Connection cx = Mockito.mock(Connection.class);
        Statement probeStatement1 = Mockito.mock(Statement.class);
        Statement probeStatement2 = Mockito.mock(Statement.class);

        Mockito.when(cx.createStatement()).thenReturn(probeStatement1, probeStatement2);
        Mockito.when(probeStatement1.executeQuery(contains("duckdb_functions")))
                .thenThrow(new SQLException("probe-failure-1"));
        Mockito.when(probeStatement2.executeQuery(contains("duckdb_functions")))
                .thenThrow(new SQLException("probe-failure-2"));

        try {
            dialect.getGeometrySRID(null, "ft1", "geometry", cx);
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("probe-failure-1", e.getMessage());
        }
        try {
            dialect.getGeometrySRID(null, "ft1", "geometry", cx);
            fail("Expected SQLException");
        } catch (SQLException e) {
            assertEquals("probe-failure-2", e.getMessage());
        }

        verify(cx, times(2)).createStatement();
    }

    @Test
    public void testSplitFilterWithMultipleBboxesPushesCombinedPredicateToSql() throws Exception {
        Path directory = Files.createTempDirectory("gt-duckdb-multibbox-");
        Path database = directory.resolve("multibbox.duckdb");
        JDBCDataStore store = DuckDBTestUtils.createStore(database, false);
        try {
            DuckDBTestUtils.runSetupSql(
                    store,
                    "CREATE TABLE \"lakes_null\" (id INTEGER PRIMARY KEY, geom GEOMETRY)",
                    "INSERT INTO \"lakes_null\" VALUES (1, ST_GeomFromText('POINT (6 6)'))");

            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            BBOX bboxDefault = ff.bbox("", 0, 0, 10, 10, null);
            BBOX bboxExplicit = ff.bbox("geom", 5, 5, 15, 15, null);
            And and = ff.and(bboxDefault, bboxExplicit);

            Filter[] split = store.getSQLDialect().splitFilter(and, store.getSchema("lakes_null"));
            assertTrue(split[0] instanceof And);
            assertEquals(and, split[0]);
            assertEquals(Filter.INCLUDE, split[1]);
        } finally {
            store.dispose();
            DuckDBTestUtils.deleteRecursively(directory);
        }
    }

    private AttributeDescriptor mockDescriptor(Class<?> binding) {
        AttributeDescriptor descriptor = Mockito.mock(AttributeDescriptor.class);
        AttributeType attributeType = Mockito.mock(AttributeType.class);
        Mockito.when(descriptor.getType()).thenReturn(attributeType);
        Mockito.doReturn(binding).when(attributeType).getBinding();
        return descriptor;
    }

    private static final class ParentMappingDuckDBDialect extends DuckDBDialect {

        private final Class<?> parentMapping;

        private ParentMappingDuckDBDialect(Class<?> parentMapping) {
            super(new JDBCDataStore());
            this.parentMapping = parentMapping;
        }

        @Override
        protected Class<?> getParentMapping(ResultSet columnMetaData, Connection cx) {
            return parentMapping;
        }
    }
}
