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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 */
package org.geotools.data.oracle.sdo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleStruct;
import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.OracleTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTReader;

/**
 * Test the functionality of the {@link SDO} utility class.
 *
 * @see GeometryFixture
 * @see JDBCTestSetup
 * @author Jody Garnett (LISAsoft)
 */
public class SDOOnlineTest extends JDBCTestSupport {
    GeometryFixture fixture;
    GeometryConverter converter;
    private Connection connection;

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new OracleTestSetup();
    }

    // called from setup
    @Override
    public void connect() throws Exception {
        super.connect();
        fixture = new GeometryFixture();
        this.connection = setup.getDataSource().getConnection();

        UnWrapper unwrapper = DataSourceFinder.getUnWrapper(this.connection);
        @SuppressWarnings("PMD.CloseResource") // will be closed in #disconnect
        OracleConnection oraConn = (OracleConnection) unwrapper.unwrap(this.connection);
        converter = new GeometryConverter(oraConn);
    }

    // called from teardown
    @Override
    protected void disconnect() throws Exception {
        connection.close();
        super.disconnect();
    }

    @Test
    public final void testGType() throws SQLException {
        Assert.assertEquals(2003, SDO.gType(fixture.rectangle));
    }

    @Test
    public final void testGTypeD() {
        Assert.assertEquals(2, SDO.D(fixture.rectangle));
    }

    @Test
    public final void testGTypeL() {
        Assert.assertEquals(0, SDO.L(fixture.rectangle));
    }

    @Test
    public final void testGTypeTT() {
        Assert.assertEquals(03, SDO.TT(fixture.rectangle));
    }

    @Test
    public final void testSRID() {
        Assert.assertEquals(-1, SDO.SRID(fixture.rectangle));
    }

    @Test
    public final void testElemInfo() throws SQLException {
        int[] elemInfo = SDO.elemInfo(fixture.rectangle);
        Assert.assertEquals(1, elemInfo[0]);
        Assert.assertEquals(1003, elemInfo[1]);
        Assert.assertEquals(3, elemInfo[2]);
    }

    @Test
    public final void testElemInfoStartingOffset() {
        Assert.assertEquals(1, SDO.elemInfoStartingOffset(fixture.rectangle));
    }

    @Test
    public final void testElemInfoEType() {
        Assert.assertEquals(1003, SDO.elemInfoEType(fixture.rectangle));
    }

    @Test
    public final void testGeometryElemInfoInterpretation() {
        Assert.assertEquals(3, SDO.elemInfoInterpretation(fixture.rectangle));
    }

    @Test
    public final void testOrdinates() throws SQLException {
        double[] ords = SDO.ordinates(fixture.rectangle);
        Assert.assertEquals("length", 4, ords.length);
        Assert.assertEquals("x1", 1, ords[0], 0.00001);
        Assert.assertEquals("y1", 1, ords[1], 0.00001);
        Assert.assertEquals("x2", 5, ords[2], 0.00001);
        Assert.assertEquals("y2", 7, ords[3], 0.00001);
    }

    @Test
    public final void testDecodePoint() throws SQLException {
        if (this.connection == null) return;
        OracleStruct datum = converter.toSDO(fixture.point);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.point, geom);
    }

    @Test
    public final void testDecodeLine() throws SQLException {
        if (this.connection == null) return;
        OracleStruct datum = converter.toSDO(fixture.lineString);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.lineString, geom);
    }

    @Test
    public final void testDecodeRectangle() throws SQLException {
        if (this.connection == null) return;
        OracleStruct datum = converter.toSDO(fixture.rectangle);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.rectangle, geom);
    }

    @Test
    public final void testDecodePolygon() throws SQLException {
        if (this.connection == null) return;
        OracleStruct datum = converter.toSDO(fixture.polygon);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.polygon, geom);
    }

    /**
     * Polygon examples used to illustrate compound encoding.
     *
     * <pre>{@code
     *   5,13+-------------+   11,13
     *      /               \
     * 2,11+                 \
     *     | 7,10+----+10,10  \
     *     |     |    |       +13,9
     *     |     |    |       |
     *     |     |    |       |
     *     |  7,5+----+10,5   +13,5
     *  2,4+                  /
     *      \                /
     *   4,3+---------------+10,3
     *
     * }</pre>
     *
     * <p>A Polygon with expected encoding:
     *
     * <ul>
     *   <li><b>SDO_GTYPE:</b>{@code 2003}<br>
     *       2 dimensional polygon, 3 for polygon
     *   <li><b>SDO_SRID:</b>{@code NULL}
     *   <li><b>SDO_POINT:</b>NULL>
     *   <li><b>SDO_ELEM_INFO:</b>{@code (1,1003,1,19,2003,1)}<br>
     *       Two triplets
     *       <ul>
     *         <li>(1,1003,1): exterior polygon ring starting at 1
     *         <li>(19,2003,1): interior polygon ring starting at 19
     *       </ul>
     *   <li><b>SDO_ORDINATES:</b>
     *       <pre>{@code
     * (2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4,
     *  7,5, 7,10, 10,10, 10,5, 7,5)
     *
     * }</pre>
     * </ul>
     *
     * <p>SQL:
     *
     * <pre>{@code
     * MDSYS.SDO_GEOMETRY(
     *   2003,
     *   NULL,
     *   NULL,
     *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1, 19,2003,1),
     *   MDSYS.SDO_ORDINATE_ARRAY(2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4,
     *       7,5, 7,10, 10,10, 10,5, 7,5)
     * )
     * }</pre>
     */
    @Test
    public final void testPolygonEncoding() throws SQLException {
        if (this.connection == null) return;

        Geometry g = fixture.polygonWithHole;
        OracleStruct datum = converter.toSDO(g);

        Assert.assertEquals(2003, SDO.gType(g));
        Assert.assertEquals(-1, SDO.SRID(g));
        assertNull(SDO.point(g));

        int[] elemInfo = SDO.elemInfo(g);
        assertEquals(
                "elemInfo",
                new int[] {
                    1, 1003, 1, // polygon
                    19, 2003, 1
                }, // hole
                elemInfo);

        double[] ords = SDO.ordinates(g);
        double[] expt = {
            2, 4, 4, 3, 10, 3, 13, 5, 13, 9, 11, 13, 5, 13, 2, 11, 2, 4, // ring
            7, 5, 7, 10, 10, 10, 10, 5, 7, 5
        }; // hole
        assertEquals("ords", expt, ords);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.polygonWithHole, geom);
    }

    @Test
    public final void testDecodePolygonWithHole() throws SQLException {
        if (this.connection == null) return;

        OracleStruct datum = converter.toSDO(fixture.polygonWithHole);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.polygonWithHole, geom);
    }

    @Test
    public final void testDecodeMultiPoint() throws SQLException {
        if (this.connection == null) return;

        OracleStruct datum = converter.toSDO(fixture.multiPoint);
        Geometry geom = converter.asGeometry(datum);

        assertEquals(fixture.multiPoint, geom);
    }

    @Test
    public final void testDecodeMultiLine() throws SQLException {
        if (this.connection == null) return;

        OracleStruct datum = converter.toSDO(fixture.multiLineString);
        Geometry geom = converter.asGeometry(datum);

        assertNotNull(geom);
        assertEquals(fixture.multiLineString, geom);
    }

    @Test
    public final void testDecodeMultiPolygon() throws SQLException {
        if (this.connection == null) return;

        OracleStruct datum = converter.toSDO(fixture.multiPolygon);

        // System.out.println(fixture.multiPolygon);
        // System.out.println( Data.toString( datum ) );

        Geometry geom = converter.asGeometry(datum);

        // spatial.trace( "origional", fixture.multiPolygon );
        // spatial.trace( "tansmorgify", geom );

        assertEquals(fixture.multiPolygon, geom);
    }

    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    @Test
    public final void testDecodeMultiPolygonWithHole() throws SQLException {
        if (this.connection == null) return;

        OracleStruct datum = converter.toSDO(fixture.multiPolygonWithHole);

        Geometry geom = converter.asGeometry(datum);

        assertNotNull(geom);
        assertTrue(geom.isValid());

        assertFalse(fixture.multiPolygonWithHole.equalsExact(geom));
        assertTrue(fixture.multiPolygonWithHole.equals(geom));
    }

    @Test
    public final void testGeometryCollection() throws SQLException {
        if (this.connection == null) return;

        OracleStruct datum = converter.toSDO(fixture.geometryCollection);

        Geometry geom = converter.asGeometry(datum);

        assertNotNull(geom);
        assertTrue(fixture.geometryCollection.isValid());
        assertTrue(geom.isValid());
        assertEquals(fixture.geometryCollection, geom);
    }

    @Test
    public final void testGeometryCollection2() throws Exception {
        if (this.connection == null) return;

        String wkt = "GEOMETRYCOLLECTION (LINESTRING (679572.8376 5151850.0275, 679583.1288 5151850.8366, "
                + "679615.3222 5151853.3675, 679611.828 5151902.3184, 679611.846517919 5151904.66336728, "
                + "679611.995 5151923.466, 679602.995 5151920.386, 679582.765 5151918.536, "
                + "679577.8433 5151918.0814, 679567.425 5151917.796), "
                + "LINESTRING (679569.221815255 5151900.91179101, 679611.846517919 5151904.66336728), "
                + "POINT (679611.982873552 5151904.66229049))";
        Geometry original = new WKTReader().read(wkt);
        original.setSRID(25832);
        OracleStruct datum = converter.toSDO(original);
        Geometry geom = converter.asGeometry(datum);
        Assert.assertEquals(25832, geom.getSRID());
        assertEquals(original, geom);
    }

    @Test
    public final void testGeometryCollection3() throws Exception {
        if (this.connection == null) return;

        String wkt = "GEOMETRYCOLLECTION (POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0)), POINT (5 5))";
        Geometry original = new WKTReader().read(wkt);
        original.setSRID(4326);
        OracleStruct datum = converter.toSDO(original);
        Geometry geom = converter.asGeometry(datum);
        Assert.assertEquals(4326, geom.getSRID());
        assertEquals(original, geom);
    }

    @Test
    public final void testGeometryCollectionMultipoint() throws Exception {
        if (this.connection == null) return;

        String wkt = "GEOMETRYCOLLECTION (POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0)), MULTIPOINT ((5 5), (10 10)))";
        Geometry original = new WKTReader().read(wkt);
        original.setSRID(4326);
        OracleStruct datum = converter.toSDO(original);
        Geometry geom = converter.asGeometry(datum);
        Assert.assertEquals(4326, geom.getSRID());
        assertEquals(original, geom);
    }

    //
    // Geometry Comparison
    //
    //
    protected void assertEquals(Geometry expected, Geometry actual) {
        assertEquals(null, expected, actual);
    }

    protected void assertEquals(String message, Geometry expected, Geometry actual) {
        if (expected == null && actual == null) return;
        if (message == null) message = "";
        assertNotNull(message + "(expected)", expected);
        assertNotNull(message + "(actual)", actual);
        assertNotNull(message + "(expected)", expected);
        assertTrue(message, expected.equalsExact(actual));
    }

    protected void assertEquals(String message, int[] expected, int[] actual) {
        if (expected == null && actual == null) return;
        if (message == null) message = "array";
        assertNotNull(message, expected);
        assertNotNull(message, actual);
        Assert.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(message + ":" + i, expected[i], actual[i]);
        }
    }

    protected void assertEquals(String message, double[] expected, double[] actual) {
        if (expected == null && actual == null) return;
        if (message == null) message = "array";
        assertNotNull(message, expected);
        assertNotNull(message, actual);
        Assert.assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            Assert.assertEquals(message + ":" + i, expected[i], actual[i], 0.0);
        }
    }
}
