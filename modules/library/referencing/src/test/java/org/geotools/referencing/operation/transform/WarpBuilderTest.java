/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.transform;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Rectangle2D;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.Matrix;
import org.geotools.api.referencing.operation.OperationNotFoundException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.operation.projection.MapProjection;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class WarpBuilderTest {
    @BeforeClass
    public static void setupClass() {
        MapProjection.SKIP_SANITY_CHECKS = true;
    }

    @Test
    public void testUTM32N() throws FactoryException, TransformException, NoninvertibleTransformException {

        CoordinateReferenceSystem utm32n = CRS.parseWKT(
                "PROJCS[\"WGS 84 / UTM zone 32N\",   GEOGCS[\"WGS 84\",     DATUM[\"World Geodetic System 1984\",       SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],       AUTHORITY[\"EPSG\",\"6326\"]],     PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],     UNIT[\"degree\", 0.017453292519943295],     AXIS[\"Geodetic longitude\", EAST],     AXIS[\"Geodetic latitude\", NORTH],     AUTHORITY[\"EPSG\",\"4326\"]],   PROJECTION[\"Transverse_Mercator\", AUTHORITY[\"EPSG\",\"9807\"]],   PARAMETER[\"central_meridian\", 9.0],   PARAMETER[\"latitude_of_origin\", 0.0],   PARAMETER[\"scale_factor\", 0.9996],   PARAMETER[\"false_easting\", 500000.0],   PARAMETER[\"false_northing\", 0.0],   UNIT[\"m\", 1.0],   AXIS[\"Easting\", EAST],   AXIS[\"Northing\", NORTH],   AUTHORITY[\"EPSG\",\"32632\"]]");
        Rectangle screen = new Rectangle(0, 0, 512, 512);
        GeneralBounds env = new GeneralBounds(new double[] {9 - 40, 0}, new double[] {9, 40});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, utm32n, screen, new int[] {32, 32}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {9 - 20, 0}, new double[] {9, 20});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, utm32n, screen, new int[] {8, 16}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {9 - 10, 0}, new double[] {9, 10});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, utm32n, screen, new int[] {4, 8}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {9 - 5, 0}, new double[] {9, 5});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, utm32n, screen, new int[] {2, 2}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {9 - 5, 0}, new double[] {9, 5});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(
                new GeneralBounds(new double[] {9 - 2, 0}, new double[] {9, 2}),
                WGS84,
                utm32n,
                screen,
                new int[] {1, 1},
                new boolean[] {false, true});
    }

    @Test
    public void testPolarStereo() throws FactoryException, TransformException, NoninvertibleTransformException {
        CoordinateReferenceSystem polar = CRS.parseWKT(
                "PROJCS[\"WGS 84 / Antarctic Polar Stereographic\",   GEOGCS[\"WGS 84\",     DATUM[\"World Geodetic System 1984\",       SPHEROID[\"WGS 84\", 6378137.0, 298.257223563, AUTHORITY[\"EPSG\",\"7030\"]],       AUTHORITY[\"EPSG\",\"6326\"]],     PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]],     UNIT[\"degree\", 0.017453292519943295],     AXIS[\"Geodetic longitude\", EAST],     AXIS[\"Geodetic latitude\", NORTH],     AUTHORITY[\"EPSG\",\"4326\"]],   PROJECTION[\"Polar Stereographic (variant B)\", AUTHORITY[\"EPSG\",\"9829\"]],   PARAMETER[\"central_meridian\", 0.0],   PARAMETER[\"Standard_Parallel_1\", -71.0],   PARAMETER[\"false_easting\", 0.0],   PARAMETER[\"false_northing\", 0.0],   UNIT[\"m\", 1.0],   AXIS[\"Easting\", \"North along 90 deg East\"],   AXIS[\"Northing\", \"North along 0 deg\"],   AUTHORITY[\"EPSG\",\"3031\"]]");
        Rectangle screen = new Rectangle(0, 0, 512, 512);
        GeneralBounds env = new GeneralBounds(new double[] {-10, -90}, new double[] {10, -85});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {16, 16}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {-10, -90}, new double[] {10, -70});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {32, 16}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {-10, -90}, new double[] {10, -45});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {64, 8}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {-10, -90}, new double[] {10, 0});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {128, 8}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {80, -90}, new double[] {110, 0});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {32, 32}, new boolean[] {false, true});
        env = new GeneralBounds(new double[] {-110, -90}, new double[] {-80, 0});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {32, 32}, new boolean[] {false, true});
        // less than a unit, but still valid, and in a place with high deformation
        env = new GeneralBounds(new double[] {-110, -109.1}, new double[] {-80, -79.9});
        env.setCoordinateReferenceSystem(WGS84);
        assertRowCols(env, WGS84, polar, screen, new int[] {16, 32}, new boolean[] {false, true});
    }

    private void assertRowCols(
            GeneralBounds sourceEnvelope,
            CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS,
            Rectangle screen,
            int[] expectedSplit,
            boolean[] reverse)
            throws TransformException, FactoryException, NoninvertibleTransformException {

        MathTransform2D crsTransform = (MathTransform2D)
                CRS.findMathTransform(CRS.getHorizontalCRS(sourceCRS), CRS.getHorizontalCRS(targetCRS));
        Bounds targetEnvelope = transformEnvelope(sourceEnvelope, targetCRS);

        AffineTransform at = worldToScreenTransform(targetEnvelope, screen, reverse);
        MathTransform2D screenTransform = new AffineTransform2D(at);
        MathTransform2D fullTranform = (MathTransform2D) ConcatenatedTransform.create(crsTransform, screenTransform);
        Rectangle2D.Double sourceDomain = new Rectangle2D.Double(
                sourceEnvelope.getLowerCorner().getOrdinate(0),
                sourceEnvelope.getLowerCorner().getOrdinate(1),
                sourceEnvelope.getSpan(0),
                sourceEnvelope.getSpan(1));
        WarpBuilder wb = new WarpBuilder(0.8);
        int[] actualSplit = wb.getRowColsSplit(fullTranform, sourceDomain);
        Assert.assertArrayEquals(expectedSplit, actualSplit);
    }

    /**/

    private AffineTransform worldToScreenTransform(Bounds mapExtent, Rectangle paintArea, boolean[] reverse)
            throws NoninvertibleTransformException {
        GeneralBounds gridRange =
                new GeneralBounds(new double[] {0, 0}, new double[] {paintArea.getWidth(), paintArea.getHeight()});
        final Matrix matrix = MatrixFactory.create(2 + 1);
        for (int i = 0; i < 2; i++) {
            int j = i;

            double scale = mapExtent.getSpan(j) / gridRange.getSpan(i);
            double offset;
            if (reverse == null || j >= reverse.length || !reverse[j]) {
                offset = mapExtent.getMinimum(j);
            } else {
                scale = -scale;
                offset = mapExtent.getMaximum(j);
            }
            offset -= scale * gridRange.getLowerCorner().getOrdinate(i);

            matrix.setElement(j, j, 0.0);
            matrix.setElement(j, i, scale);
            matrix.setElement(j, 2, offset);
        }
        return ((AffineTransform) ProjectiveTransform.create(matrix)).createInverse();
    }

    private GeneralBounds transformEnvelope(GeneralBounds env, CoordinateReferenceSystem targetCRS)
            throws TransformException, OperationNotFoundException, FactoryException {
        CoordinateOperationFactory coordinateOperationFactory = CRS.getCoordinateOperationFactory(true);

        final CoordinateOperation operation = coordinateOperationFactory.createOperation(WGS84, targetCRS);
        final GeneralBounds transformed = CRS.transform(operation, env);
        transformed.setCoordinateReferenceSystem(targetCRS);
        final MathTransform transform = operation.getMathTransform();
        GeneralBounds target = new GeneralBounds(transformed);
        transform(env, target, transform, 5);
        return target;
    }

    private Bounds transform(
            final GeneralBounds sourceEnvelope,
            GeneralBounds targetEnvelope,
            final MathTransform transform,
            int npoints)
            throws TransformException {

        npoints++; // for the starting point.

        final double[] coordinates = new double[4 * npoints * 2];
        final double xmin = sourceEnvelope.getLowerCorner().getOrdinate(0);
        final double xmax = sourceEnvelope.getUpperCorner().getOrdinate(0);
        final double ymin = sourceEnvelope.getLowerCorner().getOrdinate(1);
        final double ymax = sourceEnvelope.getUpperCorner().getOrdinate(1);
        final double scaleX = (xmax - xmin) / npoints;
        final double scaleY = (ymax - ymin) / npoints;

        int offset = 0;

        for (int t = 0; t < npoints; t++) {
            final double dx = scaleX * t;
            final double dy = scaleY * t;
            coordinates[offset++] = xmin; // Left side, increasing toward top.
            coordinates[offset++] = ymin + dy;
            coordinates[offset++] = xmin + dx; // Top side, increasing toward right.
            coordinates[offset++] = ymax;
            coordinates[offset++] = xmax; // Right side, increasing toward bottom.
            coordinates[offset++] = ymax - dy;
            coordinates[offset++] = xmax - dx; // Bottom side, increasing toward left.
            coordinates[offset++] = ymin;
        }
        assert offset == coordinates.length;
        xform(transform, coordinates, coordinates);

        // Now find the min/max of the result
        if (targetEnvelope == null) {
            return null;
        }

        for (int t = 0; t < offset; ) {
            targetEnvelope.add(new Position2D(coordinates[t++], coordinates[t++]));
        }

        return targetEnvelope;
    }

    private void xform(final MathTransform transform, final double[] src, final double[] dest)
            throws TransformException {

        final int sourceDim = transform.getSourceDimensions();
        final int targetDim = transform.getTargetDimensions();

        if (targetDim != sourceDim) {
            throw new MismatchedDimensionException();
        }

        TransformException firstError = null;
        boolean startPointTransformed = false;

        for (int i = 0; i < src.length; i += sourceDim) {
            try {
                transform.transform(src, i, dest, i, 1);

                if (!startPointTransformed) {
                    startPointTransformed = true;

                    for (int j = 0; j < i; j++) {
                        System.arraycopy(dest, j, dest, i, targetDim);
                    }
                }
            } catch (TransformException e) {
                if (firstError == null) {
                    firstError = e;
                }

                if (startPointTransformed) {
                    System.arraycopy(dest, i - targetDim, dest, i, targetDim);
                }
            }
        }

        if (!startPointTransformed && firstError != null) {
            throw firstError;
        }
    }
}
