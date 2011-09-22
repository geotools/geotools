/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster.gs;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;

import org.opengis.feature.simple.SimpleFeature;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Tests for the raster to vector PolygonExtractionProcess.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class PolygonExtractionProcessTest {

    private static final GridCoverageFactory covFactory = CoverageFactoryFinder.getGridCoverageFactory(null);
    private PolygonExtractionProcess process;

    @Before
    public void setup() {
        process = new PolygonExtractionProcess();
    }
    

    @Test
    public void simpleSmallCoverage() throws Exception {
        // small raster with 3 non-zero regions
        final float[][] DATA = {
            {2, 2, 0, 3},
            {0, 2, 0, 0},
            {0, 2, 2, 2},
            {1, 0, 0, 2}
        };
        
        final int perimeters[] = { 4, 16, 4 };
        final int areas[] = {1, 7, 1};

        GridCoverage2D cov = covFactory.create(
                "coverage",
                DATA,
                new ReferencedEnvelope(0, DATA[0].length, 0, DATA.length, null));

        int band = 0;
        Set<Double> outsideValues = Collections.singleton(0D);
        SimpleFeatureCollection fc = process.execute(cov, 0, Boolean.TRUE, null, null, null, null);
        assertEquals(3, fc.size());
        
        FeatureIterator iter = fc.features();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iter.next();
                Polygon poly = (Polygon) feature.getDefaultGeometry();
                int value = ((Number) feature.getAttribute("value")).intValue();
                assertEquals(perimeters[value - 1], (int) (poly.getBoundary().getLength() + 0.5));
                assertEquals(areas[value - 1], (int) (poly.getArea() + 0.5));
            }
        } finally {
            iter.close();
        }
    }

    @Test
    public void checkThatHolesArePresentInPolygons() throws Exception {
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
        
        SimpleFeatureCollection fc = process.execute(cov, 0, Boolean.TRUE, null, null, null, null);
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

    @Test
    public void treatZeroAsDataValue() throws Exception {
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

        Number[] noDataValues = { -1 };
        SimpleFeatureCollection fc = process.execute(cov, 0, Boolean.TRUE, null, Arrays.asList(noDataValues), null, null);
        assertEquals(NUM_POLYS, fc.size());
    }


    @Test
    public void ignoreInteriorBoundariesBetweenRegions() throws Exception {
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

        SimpleFeatureCollection fc = process.execute(cov, 0, Boolean.FALSE, null, null, null, null);

        assertEquals(1, fc.size());
        Geometry geom = (Geometry) fc.features().next().getDefaultGeometry();
        Envelope env = geom.getEnvelopeInternal();
        assertEquals(new Envelope(1, width-1, 1, height-1), env);
    }
    
    /**
     * This test works with a simple L-shape raster pattern which was causing 
     * the original raster to vector code to fail.
     */
    @Test
    public void singleLShapedRegion() throws Exception {
        final float[][] DATA = {
            {0, 0, 0, 0, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0}
        };

        GridCoverage2D cov = covFactory.create(
                "coverage",
                DATA,
                new ReferencedEnvelope(0, DATA[0].length, 0, DATA.length, null));

        Set<Double> outsideValues = Collections.singleton(0D);

        SimpleFeatureCollection fc = process.execute(cov, 0, Boolean.TRUE, null, null, null, null);
        assertEquals(1, fc.size());
        
        SimpleFeature feature = fc.features().next();
        int value = ((Number) feature.getAttribute("value")).intValue();
        assertEquals(1, value);
    }

    /**
     * This test uses an image that caused the original raster to vector code
     * to fail. It is kept here to guard against regression.
     */
    @Test
    public void extractPolygonsFromViewshedRaster() throws Exception {
        final double ROUND_OFF_TOLERANCE = 1.0e-4D;

        URL url = getClass().getResource("viewshed.tif");
        BufferedImage img = ImageIO.read(url);

        Rectangle bounds = new Rectangle(img.getMinX(), img.getMinY(), img.getWidth(), img.getHeight());
        ReferencedEnvelope env = new ReferencedEnvelope(bounds, null);

        GridCoverage2D cov = covFactory.create("coverage", img, env);

        final int OUTSIDE = -1;
        List<Number> noDataValues = new ArrayList<Number>();
        noDataValues.add(OUTSIDE);
        SimpleFeatureCollection fc = process.execute(
                cov, 0, Boolean.TRUE, null, noDataValues, null, null);

        // validate geometries and sum areas
        SimpleFeatureIterator iter = fc.features();
        Map<Integer, Double> areas = new HashMap<Integer, Double>();
        try {
            while (iter.hasNext()) {
                SimpleFeature feature = iter.next();
                Geometry geom = (Geometry) feature.getDefaultGeometry();
                assertTrue(geom.isValid());

                int value = ((Number) feature.getAttribute("value")).intValue();
                if (value != OUTSIDE) {
                    Double sum = areas.get(value);
                    if (sum == null) {
                        sum = 0.0d;
                    }
                    sum += geom.getArea();
                    areas.put(value, sum);
                }
            }
        } finally {
            iter.close();
        }
        
        final double TOL = 1.0e-6;

        // compare summed areas to image data
        Map<Integer, Double> imgAreas = new HashMap<Integer, Double>();
        Raster tile = img.getTile(0, 0);
        for (int y = img.getMinY(), ny = 0; ny < img.getHeight(); y++, ny++) {
            for (int x = img.getMinX(), nx = 0; nx < img.getWidth(); x++, nx++) {
                double gridvalue = tile.getSampleDouble(x, y, 0);
                if (Math.abs(gridvalue - OUTSIDE) < TOL) {
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
