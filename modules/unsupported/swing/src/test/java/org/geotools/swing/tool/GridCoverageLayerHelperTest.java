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

package org.geotools.swing.tool;


import java.util.Map;
import java.util.Random;

import javax.media.jai.TiledImage;

import org.jaitools.imageutils.ImageUtils;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for GridCoverageLayerHelper.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $URL$
 */
public class GridCoverageLayerHelperTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;
    private static final int NUM_TEST_POINTS = WIDTH * HEIGHT / 10;
    
    // Envelope with aspect ratio = WIDTH / HEIGHT
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(140, 144, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final Random rand = new Random();
    private static GridCoverage2D coverage;
    
    private GridCoverageLayerHelper helper;
    private Layer layer;
    private MapContent mapContent;
    
    
    @BeforeClass
    public static void setupOnce() {
        createCoverage();
    }

    @Before
    public void setup() {
        layer = new GridCoverageLayer(coverage, null);
        mapContent = new MapContent();
        mapContent.addLayer(layer);
        
        helper = new GridCoverageLayerHelper();
        helper.setMapContent(mapContent);
        helper.setLayer(layer);
    }

    @Test
    public void getInfo() throws Exception {
        DirectPosition2D pos = new DirectPosition2D(WORLD.getCoordinateReferenceSystem());
        int[] values = new int[coverage.getNumSampleDimensions()];
        
        for (int i = 0; i < NUM_TEST_POINTS; i++) {
            pos.x = WORLD.getMinX() + WORLD.getWidth() * rand.nextDouble();
            pos.y = WORLD.getMinY() + WORLD.getHeight() * rand.nextDouble();
            InfoToolResult info = helper.getInfo(pos);
            
            coverage.evaluate((DirectPosition) pos, values);
            Map<String, Object> featureData = info.getFeatureData(0);
            for (int band = 0; band < values.length; band++) {
                Object o = featureData.get("Band " + band);
                assertNotNull(o);
                assertEquals(values[band], ((Number) o).intValue());
            }
        }
    }
    
    @Test
    public void getInfoOutsideCoverageReturnsEmptyResult() throws Exception {
        DirectPosition2D pos = new DirectPosition2D(
                WORLD.getCoordinateReferenceSystem(),
                WORLD.getMaxX() + 1,
                WORLD.getMaxY() + 1);
        
        InfoToolResult info = helper.getInfo(pos);
        assertNotNull(info);
        assertEquals(0, info.getNumFeatures());
    }
    
    private static void createCoverage() {
        TiledImage image = ImageUtils.createConstantImage(WIDTH, HEIGHT, new Integer[]{0, 0, 0});
        for (int band = 0; band < image.getNumBands(); band++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    image.setSample(x, y, band, rand.nextInt(256));
                }
            }
        }
        
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        coverage = gcf.create("cov", image, WORLD);
    }
}
