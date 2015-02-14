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
 *
 */
package org.geotools.data.oracle.sdo;

import org.geotools.data.oracle.sdo.MDSYS.SDO_GEOMETRY;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;

/**
 * Unit tests for {@link SDO} <code>create*</code> methods. 
 * These test that the SDO create methods work correctly to read raw Oracle
 * SDO_GEOMETRY structures into {@link Geometry} objects.
 * These tests do not require a connection to Oracle. 
 * 
 * @author mbdavis
 */
public class SDOCreateTest {

    private static final int NULL = 0;

    public void testTest() throws Exception {
        testXYZM_MultiLineString();
    }

    @Test
    public void testXY_Point() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(2001, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1, 1), MDSYS.SDO_ORDINATE_ARRAY(50, 50));
        checkValue(oraGeom, "POINT (50 50)");
    }

    @Test
    public void testXYM_Point() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3301, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1, 1), MDSYS.SDO_ORDINATE_ARRAY(50, 50, 100));
        checkValue(oraGeom, 3, "POINT (50 50 100)");
    }

    @Test
    public void testXYZM_Point() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(4001, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1, 1), MDSYS.SDO_ORDINATE_ARRAY(50, 50, 100, 200));
        checkValue(oraGeom, 3, "POINT (50 50 100)");
    }

    @Test
    public void testXYZ_LineString() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3002, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 2, 1), MDSYS.SDO_ORDINATE_ARRAY(0, 0, 0, 50, 50, 100));
        checkValue(oraGeom, "LINESTRING (0 0 0, 50 50 100)");
    }

    @Test
    public void testXYM_LineString() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3302, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 2, 1), MDSYS.SDO_ORDINATE_ARRAY(1, 1, 20, 2, 2, 30));
        checkValue(oraGeom, "LINESTRING (1 1, 2 2)");
    }

    @Test
    public void testXYMZ_LineString() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(4302, 8307, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 2, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 2, 3, 50, 50, 100, 200));
        checkValue(oraGeom, "LINESTRING (0 0, 50 50)");
    }

    @Test
    public void testXYZ_MultiPoint() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3005, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1, 2),
                MDSYS.SDO_ORDINATE_ARRAY(50, 50, 5, 100, 200, 300));
        checkValue(oraGeom, "MULTIPOINT ((50 50 5), (100 200 300))");
    }

    @Test
    public void testXYZ_MultiLineString() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3006, 8307, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 2, 1, 7, 2, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 2, 50, 50, 100, 10, 10, 12, 150, 150, 110));
        checkValue(oraGeom, "MULTILINESTRING ((0 0, 50 50), (10 10, 150 150))");
    }

    @Test
    public void testXYMZ_MultiLineString() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(4306, 8307, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 2,
                1, 9, 2, 1), MDSYS.SDO_ORDINATE_ARRAY(0, 0, 2, 3, 50, 50, 100, 200, 10, 10, 12, 13,
                150, 150, 110, 210));
        checkValue(oraGeom, "MULTILINESTRING ((0 0, 50 50), (10 10, 150 150))");
    }

    @Test
    public void testXYZM_MultiLineString() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(4406, 8307, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 2,
                1, 9, 2, 1), MDSYS.SDO_ORDINATE_ARRAY(0, 0, 2, 3, 50, 50, 100, 200, 10, 10, 12, 13,
                150, 150, 110, 210));
        checkValue(oraGeom, "MULTILINESTRING ((0 0, 50 50), (10 10, 150 150))");
    }

    @Test
    public void testXY_Polygon() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(2003, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 50, 0, 50, 50, 0, 50, 0, 0));
        checkValue(oraGeom, "POLYGON ((0 0, 50 0, 50 50, 0 50, 0 0))");
    }

    @Test
    public void testXY_Polygon_With_Unclosed_Ring() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(2003, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 50, 0, 50, 50, 0, 50));
        // geometry will be automatically fixed by appending one copy
        // of the start point
        checkValue(oraGeom, "POLYGON ((0 0, 50 0, 50 50, 0 50, 0 0))");
    }

    @Test
    public void testXY_Polygon_With_Single_Point_Ring() throws Exception {
        // geometries will be automatically fixed by appending three copies
        // of the start point
        SDO_GEOMETRY onePointOraGeom = MDSYS.SDO_GEOMETRY(2003, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0));

        checkValue(onePointOraGeom, "POLYGON ((0 0, 0 0, 0 0, 0 0))");
    }

    @Test
    public void testXY_Polygon_With_Closed_Two_Points_Ring() throws Exception {
        // geometries will be automatically fixed by appending two copies
        // of the start point
        SDO_GEOMETRY twoPointsOraGeom = MDSYS.SDO_GEOMETRY(2003, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 0, 0));

        checkValue(twoPointsOraGeom, "POLYGON ((0 0, 0 0, 0 0, 0 0))");
    }

    @Test
    public void testXY_Polygon_With_Closed_Three_Points_Ring() throws Exception {
        // geometries will be automatically fixed by appending one copy
        // of the start point
        SDO_GEOMETRY threePointsOraGeom = MDSYS.SDO_GEOMETRY(2003, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 50, 0, 0, 0));

        checkValue(threePointsOraGeom, "POLYGON ((0 0, 50 0, 0 0, 0 0))");
    }

    @Test
    public void testXYZ_Polygon() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3003, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 99, 50, 0, 99, 50, 50, 99, 0, 50, 99, 0, 0, 99));
        checkValue(oraGeom, "POLYGON ((0 0, 50 0, 50 50, 0 50, 0 0))");
    }

    @Test
    public void testXYM_Polygon() throws Exception {
        SDO_GEOMETRY oraGeom = MDSYS.SDO_GEOMETRY(3303, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(0, 0, 99, 50, 0, 99, 50, 50, 99, 0, 50, 99, 0, 0, 99));
        checkValue(oraGeom, "POLYGON ((0 0, 50 0, 50 50, 0 50, 0 0))");
    }

    @Test
    public void testXYZ_PolygonWithHole() throws Exception {
        checkValue(MDSYS.SDO_GEOMETRY(3003, NULL, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1, 28,
                2003, 1), MDSYS.SDO_ORDINATE_ARRAY(2, 4, 99, 4, 3, 99, 10, 3, 99, 13, 5, 99, 13, 9,
                99, 11, 13, 99, 5, 13, 99, 2, 11, 99, 2, 4, 99, 7, 5, 99, 7, 10, 99, 10, 10, 99,
                10, 5, 99, 7, 5, 99)),
                "POLYGON ((2 4, 4 3, 10 3, 13 5, 13 9, 11 13, 5 13, 2 11, 2 4), (7 5, 7 10, 10 10, 10 5, 7 5))");
    }

    @Test
    public void testXYM_PolygonWithHole() throws Exception {
        checkValue(MDSYS.SDO_GEOMETRY(3303, NULL, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1, 28,
                2003, 1), MDSYS.SDO_ORDINATE_ARRAY(2, 4, 99, 4, 3, 99, 10, 3, 99, 13, 5, 99, 13, 9,
                99, 11, 13, 99, 5, 13, 99, 2, 11, 99, 2, 4, 99, 7, 5, 99, 7, 10, 99, 10, 10, 99,
                10, 5, 99, 7, 5, 99)),
                "POLYGON ((2 4, 4 3, 10 3, 13 5, 13 9, 11 13, 5 13, 2 11, 2 4), (7 5, 7 10, 10 10, 10 5, 7 5))");
    }

    @Test
    public void testXY_MultiPolygon() throws Exception {
        checkValue(MDSYS.SDO_GEOMETRY(2007, NULL, NULL,
                MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1, 11, 1003, 1),
                MDSYS.SDO_ORDINATE_ARRAY(2, 3, 7, 3, 7, 9, 2, 9, 2, 3, 9, 5, 13, 5, 11, 5, 9, 5)),
                "MULTIPOLYGON (((2 3, 7 3, 7 9, 2 9, 2 3)), ((9 5, 13 5, 11 5, 9 5)))");
    }

    @Test
    public void testXYZ_MultiPolygon() throws Exception {
        checkValue(MDSYS.SDO_GEOMETRY(3007, NULL, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1, 16,
                1003, 1), MDSYS.SDO_ORDINATE_ARRAY(2, 3, 99, 7, 3, 99, 7, 9, 99, 2, 9, 99, 2, 3,
                99, 9, 5, 99, 13, 5, 99, 11, 5, 99, 9, 5, 99)),
                "MULTIPOLYGON (((2 3, 7 3, 7 9, 2 9, 2 3)), ((9 5, 13 5, 11 5, 9 5)))");
    }

    @Test
    public void testXYM_MultiPolygon() throws Exception {
        checkValue(MDSYS.SDO_GEOMETRY(3307, NULL, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1, 16,
                1003, 1), MDSYS.SDO_ORDINATE_ARRAY(2, 3, 99, 7, 3, 99, 7, 9, 99, 2, 9, 99, 2, 3,
                99, 9, 5, 99, 13, 5, 99, 11, 5, 99, 9, 5, 99)),
                "MULTIPOLYGON (((2 3, 7 3, 7 9, 2 9, 2 3)), ((9 5, 13 5, 11 5, 9 5)))");
    }

    @Test
    public void testXYZM_MultiPolygon() throws Exception {
        checkValue(MDSYS.SDO_GEOMETRY(4307, NULL, NULL, MDSYS.SDO_ELEM_INFO_ARRAY(1, 1003, 1, 21,
                1003, 1), MDSYS.SDO_ORDINATE_ARRAY(2, 3, 99, 88, 7, 3, 99, 88, 7, 9, 99, 88, 2, 9,
                99, 88, 2, 3, 99, 88, 9, 5, 99, 88, 13, 5, 99, 88, 11, 5, 99, 88, 9, 5, 99, 88)),
                "MULTIPOLYGON (((2 3, 7 3, 7 9, 2 9, 2 3)), ((9 5, 13 5, 11 5, 9 5)))");
    }

    @Test
    public void testXY_GeometryCollection_Doc() throws Exception {
        checkValue(  MDSYS.SDO_GEOMETRY(2004, NULL, NULL,
                              MDSYS.SDO_ELEM_INFO_ARRAY(1,1,1, 3,2,1, 7,1003,1, 17,1003,1, 25,2003,1),
                              MDSYS.SDO_ORDINATE_ARRAY(
                                    1,1,
                                    1,2, 2,1,
                                    2,2, 3,2, 3,3, 2,3, 2,2,
                                    5,1, 9,5, 5,5, 5,1,
                                    5,3, 6,4, 6,3, 5,3 ) ), 
                      "GEOMETRYCOLLECTION (POINT (1 1), LINESTRING (1 2, 2 1), POLYGON ((2 2, 3 2, 3 3, 2 3, 2 2)), POLYGON ((5 1, 9 5, 5 5, 5 1), (5 3, 6 4, 6 3, 5 3)))");
    }
    
    @Test
    public void testXYM_GeometryCollection_Doc() throws Exception {
        checkValue(  MDSYS.SDO_GEOMETRY(3304, NULL, NULL,
                              MDSYS.SDO_ELEM_INFO_ARRAY(1,1,1, 4,2,1, 10,1003,1, 25,1003,1, 37,2003,1),
                              MDSYS.SDO_ORDINATE_ARRAY(
                                    1,1,99,
                                    1,2,99, 2,1,99,
                                    2,2,99, 3,2,99, 3,3,99, 2,3,99, 2,2,99,
                                    5,1,99, 9,5,99, 5,5,99, 5,1,99,
                                    5,3,99, 6,4,99, 6,3,99, 5,3,99 ) ), 
                      "GEOMETRYCOLLECTION (POINT (1 1), LINESTRING (1 2, 2 1), POLYGON ((2 2, 3 2, 3 3, 2 3, 2 2)), POLYGON ((5 1, 9 5, 5 5, 5 1), (5 3, 6 4, 6 3, 5 3)))");
    }

    // =======================================================
    void checkValue(SDO_GEOMETRY oraGeom, String wkt) {
        checkValue(oraGeom, -1, wkt);
    }

    void checkValue(SDO_GEOMETRY oraGeom, int targetDim, String wkt) {
        final GeometryFactory geometryFactory = new GeometryFactory();
        WKTReader wktRdr = new WKTReader();

        final Geometry actual = create(oraGeom, geometryFactory);

        Geometry expected = null;
        try {
            expected = wktRdr.read(wkt);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        boolean isEqual = actual.equalsNorm(expected);
        if (!isEqual) {
            System.out.println("Expected " + expected + ", actual " + actual);
        }
        assertTrue(isEqual);
    }

    private Geometry create(SDO_GEOMETRY oraGeom, GeometryFactory gf) {
        return SDO.create(gf, oraGeom.gType, oraGeom.srid, oraGeom.ptType, oraGeom.elemInfo,
                oraGeom.ordinates);
    }

}
