/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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

import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * The tests for the AreaGridProcess
 * 
 * @author Luca Paolino - GeoSolutions
 *
 * @source $URL$
 */
public class AreaGridProcessTest {

    private static final Logger logger = Logging.getLogger(AreaGridProcessTest.class);

    @Test(expected = ProcessException.class)
    public void testAreaGridCRS() throws Exception {
        logger.info("AREAGRIDPROCESS: Performing CRS test");
        AreaGridProcess process = new AreaGridProcess();
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, null);
        process.execute(envelope, -2, 10);
    }

    @Test(expected = ProcessException.class)
    public void testAreaGridParameters() throws Exception {
        logger.info("AREAGRIDPROCESS: Performing parameter test");
        AreaGridProcess process = new AreaGridProcess();
        CoordinateReferenceSystem crs = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, crs);
        process.execute(envelope, -2, 10);
    }

    @Test
    public void testWorldArea() throws Exception {
        logger.info("AREAGRIDPROCESS: Performing process execute test");
        int cx = 180;
        int cy = 90;
        CoordinateReferenceSystem crs = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        ReferencedEnvelope envelope = new ReferencedEnvelope(-180, 180, -90, 90, crs);

        double sum = computeTotalArea(cx, cy, envelope);
        
        // earth surface from from wikipedia
        double area = 510072000000000.0;
        // 1% error off the expected value using a 2 degree grid
        assertEquals(0, (area - sum) / area, 0.01); 
    }
    
    @Test
    public void testColoradoArea() throws Exception {
        logger.info("AREAGRIDPROCESS: Performing process execute test");
        CoordinateReferenceSystem crs = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;
        ReferencedEnvelope envelope = new ReferencedEnvelope(-102.05, -109.05, 37, 41, crs);

        double sum = computeTotalArea(100, 100, envelope);
        
        // Colorado surface from from wikipedia
        double area = 269837000000.0;
        // 0.1% error off the expected value using a 100x100 grid
        assertEquals(0, (area - sum) / area, 0.001); 
    }


    private double computeTotalArea(int width, int height, ReferencedEnvelope envelope) {
        AreaGridProcess process = new AreaGridProcess();
        GridCoverage2D grid = process.execute(envelope, width, height);
        assertEquals(envelope, new ReferencedEnvelope(grid.getEnvelope()));
        assertEquals(width, grid.getGridGeometry().getGridRange().getSpan(0));
        assertEquals(height, grid.getGridGeometry().getGridRange().getSpan(1));
        
        double sum = 0.0;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                GridCoordinates2D gridCoordinate = new GridCoordinates2D(i, j);
                float[] band = new float[1];
                float[] result = grid.evaluate(gridCoordinate, band);
                sum = sum + result[0];
            }
        }
        return sum;
    }

}
