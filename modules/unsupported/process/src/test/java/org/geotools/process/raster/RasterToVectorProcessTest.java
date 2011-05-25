/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;


import com.vividsolutions.jts.geom.Envelope;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.Process;
import org.geotools.process.ProcessFactory;
import org.geotools.process.Processors;

import org.opengis.feature.simple.SimpleFeature;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@code JMapPaneModel}.
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class RasterToVectorProcessTest {

    private static final GridCoverageFactory covFactory = CoverageFactoryFinder.getGridCoverageFactory(null);

    /**
     * Tests creating the process via the Processors class
     */
    @Test
    public void testCreateProcess() {
        System.out.println("   create process");
        Process p = Processors.createProcess(new NameImpl("gt", "RasterToVector"));
        assertNotNull(p);
        assertTrue(p instanceof RasterToVectorProcess);
    }

    /**
     * Test conversion with a tiny coverage
     */
    @Test
    public void testConvert() throws Exception {
        System.out.println("   simple grid");

        // small raster with 3 regions, two which touch at a corner
        final float[][] DATA = {
            {1, 1, 0, 1},
            {0, 1, 0, 0},
            {0, 1, 1, 1},
            {1, 0, 0, 1}
        };

        final int PERIMETER = 24;
        final int AREA = 9;

        GridCoverage2D cov = covFactory.create(
                "coverage",
                DATA,
                new ReferencedEnvelope(0, DATA[0].length, 0, DATA.length, null));

        int band = 0;
        Set<Double> outsideValues = Collections.singleton(0D);

        ProgressListener progress = null;
        SimpleFeatureCollection fc =
                RasterToVectorProcess.process(cov, band, null, outsideValues, true, progress);

        double perimeter = 0;
        double area = 0;
        FeatureIterator iter = fc.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iter.next();
                Polygon poly = (Polygon) feature.getDefaultGeometry();
                perimeter += poly.getLength();
                area += poly.getArea();
            }
        } finally {
            iter.close();
        }

        assertEquals(AREA, (int) Math.round(area));
        assertEquals(PERIMETER, (int) Math.round(perimeter));
    }

    /**
     * Test that enclosed 'outside' value areas are treated as holes
     */
    @Test
    public void testHoles() throws Exception {
        System.out.println("   grid with holes");

        final float[][] DATA = {
            {1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1}
        };

        final int NUM_POLYS = 2;

        GridCoverage2D cov = covFactory.create(
                "coverage",
                DATA,
                new ReferencedEnvelope(0, DATA[0].length, 0, DATA.length, null));

        int band = 0;
        Set<Double> outsideValues = Collections.singleton(0D);

        ProgressListener progress = null;
        SimpleFeatureCollection fc =
                RasterToVectorProcess.process(cov, band, null, outsideValues, true, progress);

        assertEquals(NUM_POLYS, fc.size());

        SimpleFeatureIterator iter = fc.features();
        try {
            while (iter.hasNext()) {
                Polygon poly = (Polygon) iter.next().getDefaultGeometry();
                assertEquals(1, poly.getNumInteriorRing());
            }

        } finally {
            iter.close();
        }
    }

    /**
     * Test that enclosed 'outside' value areas are treated as holes
     */
    @Test
    public void testNoOutside() throws Exception {
        System.out.println("   no outside values");

        final float[][] DATA = {
            {1, 1, 1, 1, 0, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 0, 0, 1, 0, 1, 0, 0, 1},
            {1, 1, 1, 1, 0, 1, 1, 1, 1}
        };

        final int NUM_POLYS = 5;

        GridCoverage2D cov = covFactory.create(
                "coverage",
                DATA,
                new ReferencedEnvelope(0, DATA[0].length, 0, DATA.length, null));

        int band = 0;

        ProgressListener progress = null;
        SimpleFeatureCollection fc =
                RasterToVectorProcess.process(cov, band, null, null, true, progress);

        assertEquals(NUM_POLYS, fc.size());
    }

    @Test
    public void testNoInside() throws Exception {
        System.out.println("   ignore inside edges");

        final float[][] DATA = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 2, 2, 2, 3, 3, 0},
            {0, 1, 1, 1, 2, 2, 2, 3, 3, 0},
            {0, 1, 1, 1, 2, 2, 2, 3, 3, 0},
            {0, 4, 4, 5, 5, 5, 6, 6, 6, 0},
            {0, 4, 4, 5, 5, 5, 6, 6, 6, 0},
            {0, 4, 4, 5, 5, 5, 6, 6, 6, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };

        final int width = DATA[0].length;
        final int height = DATA.length;

        GridCoverage2D cov = covFactory.create(
                "coverage",
                DATA,
                new ReferencedEnvelope(0, width, 0, height, null));

        int band = 0;

        ProgressListener progress = null;
        SimpleFeatureCollection fc =
                RasterToVectorProcess.process(cov, band, null, Collections.singleton(0.0), false, progress);

        assertEquals(1, fc.size());
        Geometry geom = (Geometry) fc.features().next().getDefaultGeometry();
        Envelope env = geom.getEnvelopeInternal();
        assertEquals(new Envelope(1, width-1, 1, height-1), env);
    }

    /**
     * Test conversion with an image that gave problems at one stage
     */
    @Test
    public void testProblemTiff() throws Exception {
        System.out.println("   test problem image");

        final double ROUND_OFF_TOLERANCE = 1.0e-4D;

        URL url = getClass().getResource("data/viewshed.tif");
        BufferedImage img = ImageIO.read(url);

        ReferencedEnvelope env = new ReferencedEnvelope(
                new Rectangle2D.Double(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight()),
                null);

        GridCoverage2D cov = covFactory.create("coverage", img, env);

        int band = 0;
        double outside = -1.0;
        Set<Double> outsideValues = Collections.singleton(outside);

        ProgressListener progress = null;
        SimpleFeatureCollection fc =
                RasterToVectorProcess.process(cov, band, null, outsideValues, true, progress);

        // validate geometries and sum areas
        SimpleFeatureIterator iter = fc.features();
        Map<Integer, Double> areas = new HashMap<Integer, Double>();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                assertTrue(geom.isValid());

                double gridvalue = (Double) feature.getAttribute("gridvalue");
                if (gridvalue != outside) {
                    Double sum = areas.get((int)gridvalue);
                    if (sum == null) {
                        sum = 0.0d;
                    }
                    sum += geom.getArea();
                    areas.put((int)gridvalue, sum);
                }
            }
        } finally {
            iter.close();
        }

        // compare summed areas to image data
        Map<Integer, Double> imgAreas = new HashMap<Integer, Double>();
        Raster tile = img.getTile(0, 0);
        for (int y = img.getMinY(), ny = 0; ny < img.getHeight(); y++, ny++) {
            for (int x = img.getMinX(), nx = 0; nx < img.getWidth(); x++, nx++) {
                double gridvalue = tile.getSampleDouble(x, y, 0);
                if (gridvalue != outside) {
                    Double sum = areas.get((int)gridvalue);
                    if (sum == null) {
                        sum = 1.0D;
                    } else {
                        sum += 1.0D;
                    }
                    areas.put((int)gridvalue, sum);
                }
            }
        }

        for (Integer i : imgAreas.keySet()) {
            double ratio = areas.get(i) / imgAreas.get(i);
            assertTrue(Math.abs(1.0D - ratio) < ROUND_OFF_TOLERANCE);
        }
    }
}
