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
package org.geotools.process.raster;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.LineString;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.raster.ContourProcess;
import org.opengis.feature.simple.SimpleFeature;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Unit tests for ContourProcess.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class ContourProcessTest {
    
    private static final double TOL = 1.0e-6;

    private static final GridCoverageFactory covFactory = CoverageFactoryFinder.getGridCoverageFactory(null);
    private ContourProcess process;
    
    @Before
    public void setup() {
        process = new ContourProcess();
    }

    
    /**
     * Creates a coverage with just two rows where values are constant within rows
     * and differ between rows, then checks for correctly generated single contour
     * between rows.
     */
    @Test
    public void singleContourInVerticalGradient() {
        final int COVERAGE_COLS = 10;
        final int COVERAGE_ROWS = 2;
        
        final double CELL_SIZE = 100;
        
        final ReferencedEnvelope WORLD = new ReferencedEnvelope(
                1000, 1000 + COVERAGE_COLS * CELL_SIZE, 
                5000, 5000 + COVERAGE_ROWS * CELL_SIZE, null);
        
        final float DATA_MIN = 100;
        final float DATA_MAX = 200;
        
        GridCoverage2D cov = createVerticalGradient(
                COVERAGE_ROWS, COVERAGE_COLS, WORLD, DATA_MIN, DATA_MAX);
        
        final double levelValue = (DATA_MIN + DATA_MAX) / 2; 
        
        SimpleFeatureCollection fc = process.execute(
                cov, 0, new double[] {levelValue}, null, null, null, null, null);

        // Should be a single contour
        assertEquals(1, fc.size());
        
        SimpleFeatureIterator iter = fc.features();
        SimpleFeature feature = null;
        try {
            feature = iter.next();
        } finally {
            iter.close();
        }
        
        // Check contour value
        Double value = (Double) feature.getAttribute("value");
        assertEquals(levelValue, value, TOL);
                
        LineString contour = (LineString) feature.getDefaultGeometry();
        Coordinate[] coords = contour.getCoordinates();
                
        // Contour should have had co-linear vertices removed by default
        assertEquals(2, coords.length);
                
        // Contour end-point X ordinates should be within half cell-width of
        // coverage X extrema
        double minX = Math.min(coords[0].x, coords[1].x);
        assertEquals(WORLD.getMinX(), minX, CELL_SIZE / 2 + TOL);
        double maxX = Math.max(coords[0].x, coords[1].x);
        assertEquals(WORLD.getMaxX(), maxX, CELL_SIZE / 2 + TOL);
                
        // Contour Y ordinate should be at mid-Y of coverage and
        // contour should be horizontal
        double expectedY = (WORLD.getMinY() + WORLD.getMaxY()) / 2;
        assertEquals(expectedY, coords[0].y, TOL);
        assertEquals(expectedY, coords[1].y, TOL);
    }
    
    /**
     * Tests that the process doesn't blow up when there are no
     * contours to return
     */
    @Test
    public void noContours() {
        // Coverage with values in range [0, 10]
        GridCoverage2D cov = createVerticalGradient(10, 10, null, 0, 10);

        // Run process asking for contours at level = 20
        SimpleFeatureCollection fc = process.execute(cov, 0, new double[20], null, null, null, null, null);
        assertNotNull(fc);
        assertTrue(fc.isEmpty());
    }
    
    
    private GridCoverage2D createVerticalGradient(
            final int dataRows, final int dataCols, 
            ReferencedEnvelope worldEnv,
            final float startValue, final float endValue) {
        
        if (dataRows < 2) {
            throw new IllegalArgumentException("dataRows must be >= 2");
        }
        if (dataCols < 1) {
            throw new IllegalArgumentException("dataCols must be positive");
        }
        
        if (worldEnv == null) {
            worldEnv = new ReferencedEnvelope(0, dataCols, 0, dataRows, null);
        }
        
        float[][] DATA = new float[dataRows][dataCols];
        float delta = (endValue - startValue) / (dataRows - 1);

        for (int iy = 0; iy < dataRows; iy++) {
            float value = startValue + iy * delta;
            for (int ix = 0; ix < dataCols; ix++) {
                DATA[iy][ix] = value;
            }
            value += delta;
        }
        
        return covFactory.create("coverage", DATA, worldEnv);
    }

}
