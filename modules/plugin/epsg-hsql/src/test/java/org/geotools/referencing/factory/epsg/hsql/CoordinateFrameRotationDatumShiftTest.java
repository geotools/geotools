/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Checks the datum shift synthesized from a datum {@code toWGS84} against PROJ, mostly on Coordinate Frame Rotation
 * (EPSG 9607) datums, where getting the rotation signs wrong moves points by hundreds of metres, plus a Position Vector
 * (9606) and a Geocentric Translation (9603) datum as controls.
 *
 * <p>Expected values are golden values from PROJ 9.4.0, so a forward/inverse round trip cannot hide an error that is
 * symmetric in both directions. To regenerate one row, giving the ordinates in the authority axis order:
 *
 * <pre>
 * echo "50.5 4.45" | cs2cs -f "%.9f" EPSG:4313 EPSG:4326
 * </pre>
 *
 * <p>PROJ picks the same operation GeoTools does for each of these datums, which is what makes the comparison fair;
 * {@code projinfo -s EPSG:4313 -t EPSG:4326 --summary} lists its choice, to compare against the operation code in the
 * row label.
 */
@RunWith(Parameterized.class)
public class CoordinateFrameRotationDatumShiftTest {

    /**
     * About 10 cm, for the rows in degrees. GeoTools and PROJ agree within 5 mm on the rotating shifts, and within a
     * couple of centimetres on the 3 parameter one, while a wrong rotation sign moves the points by 79 m or more.
     */
    private static final double DEGREE_TOL = 1e-6;

    /** A centimetre, for the row in metres: PROJ reaches ETRS89 directly, GeoTools pivots through WGS84. */
    private static final double METRE_TOL = 0.01;

    /**
     * The test cases, each row holding:
     *
     * <ul>
     *   <li>a label with the datum, its transformation method and the EPSG operation holding the parameters
     *   <li>the source and target CRS codes
     *   <li>a control point inside the operation area of validity
     *   <li>the same point according to PROJ
     *   <li>the tolerance, in target CRS units
     * </ul>
     *
     * <p>Coordinates follow the authority axis order of their CRS, so latitude first for the geographic ones.
     *
     * <p>The 9606 and 9603 rows use methods whose parameters are taken as stored, guarding against the sign conversion
     * reaching where it does not belong. The last row is the reprojection this fix originated from, Belgian Lambert 72
     * to Belgian Lambert 2008, projected on both sides.
     */
    @Parameterized.Parameters(name = "{0}")
    public static List<Object[]> data() {
        return List.of(
                row84("Porto Santo 1995, 9607, op 1967", "4663", 32.865, -16.77, 32.868624659, -16.774081734),
                row84("Kudams, 9607, op 1062", "4319", 29.31, 47.97, 29.310043451, 47.970236677),
                row84("RT90, 9607, op 1896", "4124", 62.0, 17.1, 61.999168343, 17.096576462),
                row84("Bermuda 1957, 9607, op 15970", "4216", 32.32, -64.75, 32.321408101, -64.749722126),
                row84("JAD69, 9607, op 15927", "4242", 18.11, -77.3, 18.112602833, -77.298954168),
                row84("Lisbon 1890, 9607, op 1990", "4666", 39.55, -7.875, 39.551475823, -7.876391501),
                row84("MGI, 9607, op 1194", "4312", 47.24, 14.875, 47.239561498, 14.874067263),
                row84("S-JTSK, 9607, op 5239", "4156", 49.82, 15.475, 49.819283451, 15.473779812),
                row84("PRS92, 9607, op 15708", "4683", 12.59, 122.995, 12.588674142, 122.996404018),
                row84("BD72, 9607, op 15929", "4313", 50.5, 4.45, 50.499444918, 4.451263025),
                row84("Pulkovo 1942, 9606, op 1303", "4284", 44.06, 51.02, 44.060055096, 51.018838638),
                row84("Massawa, 9603, op 1165", "4262", 15.23, 39.87, 15.229046273, 39.869080453),
                // specific case that triggered the bug fix and investigation
                new Object[] {
                    "BD72 to Belgian Lambert 2008, 9607, op 15929",
                    "EPSG:31370",
                    "EPSG:3812",
                    150000.0,
                    170000.0,
                    649999.686856,
                    670000.411639,
                    METRE_TOL
                });
    }

    /** A row transforming to WGS84, where every coordinate is in degrees. */
    private static Object[] row84(String name, String code, double x, double y, double expectedX, double expectedY) {
        return new Object[] {name, "EPSG:" + code, "EPSG:4326", x, y, expectedX, expectedY, DEGREE_TOL};
    }

    private final String sourceCode;
    private final String targetCode;
    private final double x;
    private final double y;
    private final double expectedX;
    private final double expectedY;
    private final double tolerance;

    public CoordinateFrameRotationDatumShiftTest(
            String name,
            String sourceCode,
            String targetCode,
            double x,
            double y,
            double expectedX,
            double expectedY,
            double tolerance) {
        this.sourceCode = sourceCode;
        this.targetCode = targetCode;
        this.x = x;
        this.y = y;
        this.expectedX = expectedX;
        this.expectedY = expectedY;
        this.tolerance = tolerance;
    }

    @Test
    public void testSynthesizedShiftMatchesProj() throws Exception {
        Position transformed = shift(CRS.decode(sourceCode), CRS.decode(targetCode), x, y);

        assertEquals("first ordinate", expectedX, transformed.getOrdinate(0), tolerance);
        assertEquals("second ordinate", expectedY, transformed.getOrdinate(1), tolerance);
    }

    /** The inverse is assembled separately, so walk the same PROJ pair backwards. */
    @Test
    public void testInverseSynthesizedShiftMatchesProj() throws Exception {
        Position transformed = shift(CRS.decode(targetCode), CRS.decode(sourceCode), expectedX, expectedY);

        assertEquals("first ordinate", x, transformed.getOrdinate(0), tolerance);
        assertEquals("second ordinate", y, transformed.getOrdinate(1), tolerance);
    }

    private static Position shift(
            CoordinateReferenceSystem source, CoordinateReferenceSystem target, double x, double y) throws Exception {
        MathTransform transform = new DefaultCoordinateOperationFactory()
                .createOperation(source, target)
                .getMathTransform();
        return transform.transform(new Position2D(source, x, y), null);
    }
}
