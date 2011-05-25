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
package org.geotools.referencing.operation.projection;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.MathTransform;

import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests some south-oriented map projections.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class SouthOrientedTest {
    /**
     * Small number for matrix element comparaisons.
     */
    private static final double EPS = 1E-10;

    /**
     * Parse a test CRS north or south oriented. If the CRS is fully south-oriented
     * with 0.0 northing, then it should be the EPSG:22285 one.
     */
    private static ProjectedCRS parseTransverseMercator(
            final boolean methodSouth, final boolean axisSouth, final double northing)
            throws FactoryException
    {
        final String method =
                methodSouth ? "Transverse Mercator (South Orientated)" : "Transverse Mercator";
        final String axis =
                axisSouth ? "\"Southing\", SOUTH" : "\"Northing\", NORTH";
        return (ProjectedCRS) CRS.parseWKT(
                "PROJCS[\"South African Coordinate System zone 25\", " +
                  "GEOGCS[\"Cape\", " +
                    "DATUM[\"Cape\", " +
                      "SPHEROID[\"Clarke 1880 (Arc)\", 6378249.145, 293.4663077, AUTHORITY[\"EPSG\",\"7013\"]], " +
                      "TOWGS84[-136.0, -108.0, -292.0, 0.0, 0.0, 0.0, 0.0], " +
                      "AUTHORITY[\"EPSG\",\"6222\"]], " +
                    "PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], " +
                    "UNIT[\"degree\", 0.017453292519943295], " +
                    "AXIS[\"Geodetic latitude\", NORTH], " +
                    "AXIS[\"Geodetic longitude\", EAST], " +
                    "AUTHORITY[\"EPSG\",\"4222\"]], " +
                  "PROJECTION[\"" + method + "\"], " +
                  "PARAMETER[\"central_meridian\", 25.0], " +
                  "PARAMETER[\"latitude_of_origin\", 0.0], " +
                  "PARAMETER[\"scale_factor\", 1.0], " +
                  "PARAMETER[\"false_easting\", 0.0], " +
                  "PARAMETER[\"false_northing\", " + northing + "], " +
                  "UNIT[\"m\", 1.0], " +
                  "AXIS[\"Westing\", WEST], " +
                  "AXIS[" + axis + "]]");
    }

    /**
     * Tests the Transverse Mercator South-Oriented case.
     */
    @Test
    public void testTransverseMercator() throws FactoryException {
        /*
         * Tests "Transverse Mercator" (not south-oriented) with an axis oriented toward south.
         */
        ProjectedCRS north = parseTransverseMercator(false, false, 1000);
        assertEquals(AxisDirection.WEST,  north.getCoordinateSystem().getAxis(0).getDirection());
        assertEquals(AxisDirection.NORTH, north.getCoordinateSystem().getAxis(1).getDirection());

        ProjectedCRS south = parseTransverseMercator(false, true, 1000);
        assertEquals(AxisDirection.WEST,  south.getCoordinateSystem().getAxis(0).getDirection());
        assertEquals(AxisDirection.SOUTH, south.getCoordinateSystem().getAxis(1).getDirection());

        MathTransform transform = CRS.findMathTransform(north, south);
        assertTrue(transform instanceof LinearTransform);
        Matrix matrix = ((LinearTransform) transform).getMatrix();
        assertDiagonal(matrix);
        assertFalse(matrix.isIdentity());
        assertEquals("West direction should be unchanged. ",      +1, matrix.getElement(0,0), EPS);
        assertEquals("North-South direction should be reverted.", -1, matrix.getElement(1,1), EPS);
        assertEquals("No easting expected.",                       0, matrix.getElement(0,2), EPS);
        assertEquals("No northing expected.",                      0, matrix.getElement(1,2), EPS);

        /*
         * Tests "Transverse Mercator South Oriented"
         */
        south = parseTransverseMercator(true, true, 1000);
        assertEquals(AxisDirection.WEST,  south.getCoordinateSystem().getAxis(0).getDirection());
        assertEquals(AxisDirection.SOUTH, south.getCoordinateSystem().getAxis(1).getDirection());
        transform = CRS.findMathTransform(north, south);
        assertTrue(transform instanceof LinearTransform);
        matrix = ((LinearTransform) transform).getMatrix();
        assertDiagonal(matrix);
        assertFalse(matrix.isIdentity());
        assertEquals("West direction should be unchanged. ",      +1, matrix.getElement(0,0), EPS);
        assertEquals("North-South direction should be reverted.", -1, matrix.getElement(1,1), EPS);
        assertEquals("No easting expected.",                       0, matrix.getElement(0,2), EPS);
        assertEquals("No northing expected.",                      0, matrix.getElement(1,2), EPS);

        /*
         * Tries with a different northing.
         */
        north = parseTransverseMercator(false, false, 3000);
        transform = CRS.findMathTransform(north, south);
        assertTrue(transform instanceof LinearTransform);
        matrix = ((LinearTransform) transform).getMatrix();
        assertFalse(matrix.isIdentity());
        assertEquals("West direction should be unchanged. ",      +1, matrix.getElement(0,0), EPS);
        assertEquals("North-South direction should be reverted.", -1, matrix.getElement(1,1), EPS);
        assertEquals("No easting expected.",                       0, matrix.getElement(0,2), EPS);
        assertEquals("Northing expected.",                      2000, matrix.getElement(1,2), EPS);
    }

    /**
     * Tests a Krovak projection with (SOUTH,WEST) axis.
     */
    @Test
    public void testKrovak() throws FactoryException {
        final String geoWKT =
                "GEOGCS[\"S-JTSK (Ferro)\", " +
                  "DATUM[\"S_JTSK_Ferro\", " +
                    "SPHEROID[\"Bessel 1841\", 6377397.155, 299.1528128], " +
                    "TOWGS84[570.8,85.7,462.8,4.998,1.587,5.261,3.56]], " +
                  "PRIMEM[\"Greenwich\",0], " +
                  "UNIT[\"degree\",0.0174532925199433]]";
        final String prjWKT =
                "PROJCS[\"S-JTSK(Ferro) / krovak\", " + geoWKT + ", " +
                  "PROJECTION[\"Krovak\"], " +
                  "UNIT[\"metre\",1.0], " +
                  "AXIS[\"y\",WEST], " +
                  "AXIS[\"x\",SOUTH]]";

        final CoordinateReferenceSystem sourceCRS = CRS.parseWKT(geoWKT);
        final CoordinateReferenceSystem targetCRS = CRS.parseWKT(prjWKT);
        final MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
        assertTrue(transform instanceof ConcatenatedTransform);
        final ConcatenatedTransform ct = (ConcatenatedTransform) transform;
        assertTrue(ct.transform2 instanceof LinearTransform);
        final Matrix matrix = ((LinearTransform) ct.transform2).getMatrix();
        assertDiagonal(matrix);
        assertEquals("East-West direction should be reverted. ",  -1, matrix.getElement(0,0), EPS);
        assertEquals("North-South direction should be reverted.", -1, matrix.getElement(1,1), EPS);
        assertEquals("No easting expected.",                       0, matrix.getElement(0,2), EPS);
        assertEquals("No northing expected.",                      0, matrix.getElement(1,2), EPS);
    }

    /**
     * Asserts that the specified matrix is diagonal.
     */
    private static void assertDiagonal(final Matrix matrix) {
        final int nrow = matrix.getNumRow();
        final int ncol = matrix.getNumCol();
        for (int j=0; j<nrow; j++) {
            for (int i=0; i<ncol; i++) {
                if (i != j) {
                    assertEquals("row "+j+", col "+i, 0.0, matrix.getElement(j, i), EPS);
                }
            }
        }
    }
}
