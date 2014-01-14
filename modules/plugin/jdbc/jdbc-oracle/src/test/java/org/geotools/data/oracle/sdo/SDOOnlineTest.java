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

import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.OracleConnection;
import oracle.sql.STRUCT;

import org.geotools.data.jdbc.datasource.DataSourceFinder;
import org.geotools.data.jdbc.datasource.UnWrapper;
import org.geotools.data.oracle.OracleTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Test the functionality of the {@link SDO} utility class.
 * 
 * @see GeometryFixture
 * @see SDOTestSetup
 * @author Jody Garnett (LISAsoft)
 * 
 *         TODO To change the template for this generated type comment go to Window - Preferences - Java - Code Style - Code Templates
 * 
 * @source $URL$
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
    public void connect() throws Exception {
        super.connect();
        fixture = new GeometryFixture();
        this.connection = setup.getDataSource().getConnection();
        
        UnWrapper unwrapper = DataSourceFinder.getUnWrapper(this.connection);
        OracleConnection oraConn = (OracleConnection) unwrapper.unwrap(this.connection);
        converter = new GeometryConverter(oraConn);
    }

    // called from teardown
    protected void disconnect() throws Exception {
        connection.close();
        super.disconnect();
    }

    final public void testGType() throws SQLException {
        assertEquals(2003, SDO.gType(fixture.rectangle));
    }

    final public void testGTypeD() {
        assertEquals(2, SDO.D(fixture.rectangle));
    }

    final public void testGTypeL() {
        assertEquals(0, SDO.L(fixture.rectangle));
    }

    final public void testGTypeTT() {
        assertEquals(03, SDO.TT(fixture.rectangle));
    }

    final public void testSRID() throws SQLException {
        assertEquals(-1, SDO.SRID(fixture.rectangle));
    }

    final public void testElemInfo() throws SQLException {
        int elemInfo[] = SDO.elemInfo(fixture.rectangle);
        assertEquals(1, elemInfo[0]);
        assertEquals(1003, elemInfo[1]);
        assertEquals(3, elemInfo[2]);
    }

    final public void testElemInfoStartingOffset() {
        assertEquals(1, SDO.elemInfoStartingOffset(fixture.rectangle));
    }

    final public void testElemInfoEType() {
        assertEquals(1003, SDO.elemInfoEType(fixture.rectangle));
    }

    final public void testGeometryElemInfoInterpretation() {
        assertEquals(3, SDO.elemInfoInterpretation(fixture.rectangle));
    }

    final public void testOrdinates() throws SQLException {
        double ords[] = SDO.ordinates(fixture.rectangle);
        assertEquals("length", 4, ords.length);
        assertEquals("x1", 1, ords[0], 0.00001);
        assertEquals("y1", 1, ords[1], 0.00001);
        assertEquals("x2", 5, ords[2], 0.00001);
        assertEquals("y2", 7, ords[3], 0.00001);
    }

    final public void testDecodePoint() throws SQLException {
        if (this.connection == null)
            return;
        STRUCT datum = converter.toSDO(fixture.point);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.point, geom);
    }

    final public void testDecodeLine() throws SQLException {
        if (this.connection == null)
            return;
        STRUCT datum = converter.toSDO(fixture.lineString);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.lineString, geom);
    }

    final public void testDecodeRectangle() throws SQLException {
        if (this.connection == null)
            return;
        STRUCT datum = converter.toSDO(fixture.rectangle);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.rectangle, geom);
    }

    final public void testDecodePolygon() throws SQLException {
        if (this.connection == null)
            return;
        STRUCT datum = converter.toSDO(fixture.polygon);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.polygon, geom);
    }

    /**
     * Polygon examples used to illustrate compound encoding.</p> <code><pre>
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
     * </pre></code>
     * <p>
     * A Polygon with expected encoding:
     * </p>
     * <ul>
     * <li><b>SDO_GTYPE:</b><code>2003</code><br/>
     * 2 dimensional polygon, 3 for polygon</li>
     * <li><b>SDO_SRID:</b><code>NULL</code></li>
     * <li><b>SDO_POINT:</b>NULL></li>
     * <li><b>SDO_ELEM_INFO:</b><code>(1,1003,1,19,2003,1)</code><br/>
     * Two triplets
     * <ul>
     * <li>(1,1003,1): exterior polygon ring starting at 1</li>
     * 
     * <li>(19,2003,1): interior polygon ring starting at 19</li>
     * </ul>
     * </li>
     * <li><b>SDO_ORDINATES:</b> <code><pre>
     *        (2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4,
     *         7,5, 7,10, 10,10, 10,5, 7,5)
     *     </code>
     * 
     * <pre/></li>
     * </ul>
     * <p>
     * SQL:
     * </p>
     * <code><pre>
     * MDSYS.SDO_GEOMETRY(
     *   2003,
     *   NULL,
     *   NULL,
     *   MDSYS.SDO_ELEM_INFO_ARRAY(1,1003,1, 19,2003,1),
     *   MDSYS.SDO_ORDINATE_ARRAY(2,4, 4,3, 10,3, 13,5, 13,9, 11,13, 5,13, 2,11, 2,4,
     *       7,5, 7,10, 10,10, 10,5, 7,5)
     * )
     * </pre></code>
     */
    final public void testPolygonEncoding() throws SQLException {
        if (this.connection == null)
            return;

        Geometry g = fixture.polygonWithHole;
        STRUCT datum = converter.toSDO(g);

        assertEquals(2003, SDO.gType(g));
        assertEquals(-1, SDO.SRID(g));
        assertNull(SDO.point(g));

        int elemInfo[] = SDO.elemInfo(g);
        assertEquals("elemInfo", new int[] { 1, 1003, 1, // polygon
                19, 2003, 1 }, // hole
                elemInfo);

        double ords[] = SDO.ordinates(g);
        double expt[] = new double[] { 2, 4, 4, 3, 10, 3, 13, 5, 13, 9, 11, 13, 5, 13, 2, 11, 2, 4, // ring
                7, 5, 7, 10, 10, 10, 10, 5, 7, 5 }; // hole
        assertEquals("ords", expt, ords);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.polygonWithHole, geom);
    }

    final public void testDecodePolygonWithHole() throws SQLException {
        if (this.connection == null)
            return;

        STRUCT datum = converter.toSDO(fixture.polygonWithHole);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.polygonWithHole, geom);
    }

    final public void testDecodeMultiPoint() throws SQLException {
        if (this.connection == null)
            return;

        STRUCT datum = converter.toSDO(fixture.multiPoint);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertEquals(fixture.multiPoint, geom);
    }

    final public void testDecodeMultiLine() throws SQLException {
        if (this.connection == null)
            return;

        STRUCT datum = converter.toSDO(fixture.multiLineString);
        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertNotNull(geom);
        assertEquals(fixture.multiLineString, geom);
    }

    final public void testDecodeMultiPolygon() throws SQLException {
        if (this.connection == null)
            return;

        STRUCT datum = converter.toSDO(fixture.multiPolygon);

        // System.out.println(fixture.multiPolygon);
        // System.out.println( Data.toString( datum ) );

        Geometry geom = (Geometry) converter.asGeometry(datum);

        // spatial.trace( "origional", fixture.multiPolygon );
        // spatial.trace( "tansmorgify", geom );

        assertEquals(fixture.multiPolygon, geom);
    }

    final public void testDecodeMultiPolygonWithHole() throws SQLException {
        if (this.connection == null)
            return;

        STRUCT datum = converter.toSDO(fixture.multiPolygonWithHole);

        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertNotNull(geom);
        assertTrue(geom.isValid());

        assertFalse(fixture.multiPolygonWithHole.equalsExact(geom));
        assertTrue(fixture.multiPolygonWithHole.equals(geom));
    }

    final public void testGeometryCollection() throws SQLException {
        if (this.connection == null)
            return;

        STRUCT datum = converter.toSDO(fixture.geometryCollection);

        Geometry geom = (Geometry) converter.asGeometry(datum);

        assertNotNull(geom);
        assertTrue(fixture.geometryCollection.isValid());
        assertTrue(geom.isValid());
        assertEquals(fixture.geometryCollection, geom);
    }
    
    final public void testGeometryCollection2() throws Exception {
        if(this.connection == null)
            return;
        
        String wkt = "GEOMETRYCOLLECTION (LINESTRING (679572.8376 5151850.0275, 679583.1288 5151850.8366, "
                + "679615.3222 5151853.3675, 679611.828 5151902.3184, 679611.846517919 5151904.66336728, "
                + "679611.995 5151923.466, 679602.995 5151920.386, 679582.765 5151918.536, "
                + "679577.8433 5151918.0814, 679567.425 5151917.796), "
                + "LINESTRING (679569.221815255 5151900.91179101, 679611.846517919 5151904.66336728), "
                + "POINT (679611.982873552 5151904.66229049))"; 
        Geometry original = new WKTReader().read(wkt);
        original.setSRID(25832);
        STRUCT datum = converter.toSDO(original);
        Geometry geom = (Geometry) converter.asGeometry(datum);
        assertEquals(25832, geom.getSRID());
        assertEquals(original, geom);
    }
    
    final public void testGeometryCollection3() throws Exception {
        if(this.connection == null)
            return;
        
        String wkt = "GEOMETRYCOLLECTION (POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0)), POINT (5 5))"; 
        Geometry original = new WKTReader().read(wkt);
        original.setSRID(4326);
        STRUCT datum = converter.toSDO(original);
        Geometry geom = (Geometry) converter.asGeometry(datum);
        assertEquals(4326, geom.getSRID());
        assertEquals(original, geom);
    }
    
    final public void testGeometryCollectionMultipoint() throws Exception {
        if(this.connection == null)
            return;
        
        String wkt = "GEOMETRYCOLLECTION (POLYGON ((0 0, 10 0, 10 10, 0 10, 0 0)), MULTIPOINT ((5 5), (10 10)))"; 
        Geometry original = new WKTReader().read(wkt);
        original.setSRID(4326);
        STRUCT datum = converter.toSDO(original);
        Geometry geom = (Geometry) converter.asGeometry(datum);
        assertEquals(4326, geom.getSRID());
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
        if (expected == null && actual == null)
            return;
        if (message == null)
            message = "";
        assertNotNull(message + "(expected)", expected);
        assertNotNull(message + "(actual)", actual);
        assertNotNull(message + "(expected)", expected);
        assertTrue(message, expected.equalsExact(actual));
    }

    protected void assertEquals(String message, int[] expected, int actual[]) {
        if (expected == null && actual == null)
            return;
        if (message == null)
            message = "array";
        assertNotNull(message, expected);
        assertNotNull(message, actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(message + ":" + i, expected[i], actual[i]);
        }
    }

    protected void assertEquals(String message, double[] expected, double actual[]) {
        if (expected == null && actual == null)
            return;
        if (message == null)
            message = "array";
        assertNotNull(message, expected);
        assertNotNull(message, actual);
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(message + ":" + i, expected[i], actual[i], 0.0);
        }
    }
    
}
