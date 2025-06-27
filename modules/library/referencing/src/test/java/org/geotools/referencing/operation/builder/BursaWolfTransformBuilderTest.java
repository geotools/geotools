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
 */
package org.geotools.referencing.operation.builder;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralPosition;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.operation.transform.GeocentricTranslation;
import org.junit.Test;

public final class BursaWolfTransformBuilderTest {
    /** Test {@link BursaWolfTransformBuilder}. */
    @Test
    public void testBursaWolfParamCalculaterXrotation() throws FactoryException, TransformException {
        Random random = new Random(773418718);

        double R = 6370000;
        double angle = random.nextDouble() * 10 / 3600 * Math.PI / 180;
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);

        List<MappedPosition> vectors = new ArrayList<>();

        vectors.add(new MappedPosition(new GeneralPosition(R, 0, 0), new GeneralPosition(R, 0, 0)));

        vectors.add(new MappedPosition(new GeneralPosition(0, cos * R, -sin * R), new GeneralPosition(0, R, 0)));

        vectors.add(new MappedPosition(new GeneralPosition(0, sin * R, cos * R), new GeneralPosition(0, 0, R)));

        double[] points = new double[vectors.size() * 3];

        for (int i = 0; i < vectors.size(); i++) {
            points[i * 3] = vectors.get(i).getSource().getCoordinate()[0];
            points[i * 3 + 1] = vectors.get(i).getSource().getCoordinate()[1];
            points[i * 3 + 2] = vectors.get(i).getSource().getCoordinate()[2];
        }

        double[] dstPoints = new double[points.length];

        MathTransformBuilder BWPT = new BursaWolfTransformBuilder(vectors);
        BWPT.getMathTransform().transform(points, 0, dstPoints, 0, points.length / 3);

        for (int i = 0; i < vectors.size(); i++) {
            assertEquals(dstPoints[i * 3], vectors.get(i).getTarget().getCoordinate()[0], 1E-2);
            assertEquals(dstPoints[i * 3 + 1], vectors.get(i).getTarget().getCoordinate()[1], 1E-2);
            assertEquals(dstPoints[i * 3 + 2], vectors.get(i).getTarget().getCoordinate()[2], 1E-2);
        }
    }

    /**
     * The test that generates random transformation parameters and source points. The destination points are calculated
     * using generated parameters. Then the parameters are computed by the builder and compared against original.
     */
    @Test
    public void test2BursaWolfParamCalculater() throws FactoryException, TransformException {
        double R = 6370000;
        Random random = new Random(143477662);

        Position ptSrc;
        Position ptDst;
        List<MappedPosition> vectors = new ArrayList<>();

        BursaWolfParameters bwp = new BursaWolfParameters(null);
        bwp.dx = random.nextDouble() * 100;
        bwp.dy = random.nextDouble() * 100;
        bwp.dz = random.nextDouble() * 100;
        bwp.ex = random.nextDouble() * 10;
        bwp.ey = random.nextDouble() * 10;
        bwp.ez = random.nextDouble() * 10;
        bwp.ppm = random.nextDouble() * 10;

        GeocentricTranslation gt = new GeocentricTranslation(bwp);

        for (int i = 0; i < 3 * 10; i++) {
            double gamma = (45 + random.nextDouble() * 10) * Math.PI / 180;
            double alfa = (45 + random.nextDouble() * 10) * Math.PI / 180;

            //   generate source points
            ptSrc = new GeneralPosition(
                    R * Math.sin(gamma) * Math.cos(alfa), R * Math.sin(gamma) * Math.cos(alfa), R * Math.cos(gamma));

            double[] pom = new double[3];

            //  generates destination points
            gt.transform(ptSrc.getCoordinate(), 0, pom, 0, 1);
            ptDst = new GeneralPosition(pom);
            vectors.add(new MappedPosition(ptSrc, ptDst));
        }

        BursaWolfTransformBuilder BWPT = new BursaWolfTransformBuilder(vectors);
        assertEquals(BWPT.getBursaWolfParameters(null).dx, bwp.dx, 1E-2);
        assertEquals(BWPT.getBursaWolfParameters(null).dy, bwp.dy, 1E-2);
        assertEquals(BWPT.getBursaWolfParameters(null).dz, bwp.dz, 1E-2);
        assertEquals(BWPT.getBursaWolfParameters(null).ex, bwp.ex, 1E-2);
        assertEquals(BWPT.getBursaWolfParameters(null).ey, bwp.ey, 1E-2);
        assertEquals(BWPT.getBursaWolfParameters(null).ez, bwp.ez, 1E-2);
        assertEquals(BWPT.getBursaWolfParameters(null).ppm, bwp.ppm, 1E-2);

        assertEquals(BWPT.getErrorStatistics().rms(), 0, 1E-2);
    }
}
