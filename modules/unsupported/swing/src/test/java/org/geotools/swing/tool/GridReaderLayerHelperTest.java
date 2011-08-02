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

import org.opengis.referencing.operation.MathTransform;
import java.io.File;
import java.util.Map;
import java.util.Random;

import javax.media.jai.TiledImage;

import org.jaitools.imageutils.ImageUtils;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.referencing.datum.PixelInCell;
import static org.junit.Assert.*;

/**
 * Unit tests for GridReaderLayerHelper.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $URL$
 */
public class GridReaderLayerHelperTest {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 50;
    private static final int NUM_TEST_POINTS = WIDTH * HEIGHT / 10;
    
    // Envelope with aspect ratio = WIDTH / HEIGHT
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(140, 144, -33, -35, DefaultGeographicCRS.WGS84);
    
    private static final Random rand = new Random();
    private static TiledImage image;
    private static AbstractGridCoverage2DReader reader;
    
    private InfoToolHelper helper;
    private Layer layer;
    private MapContent mapContent;
    private MathTransform worldToGridTransform;
    
    
    @BeforeClass
    public static void setupOnce() throws Exception {
        createImageAndReader();
    }

    @Before
    public void setup() {
        layer = new GridReaderLayer(reader, null);
        mapContent = new MapContent();
        mapContent.addLayer(layer);
        
        helper = new GridReaderLayerHelper();
        helper.setMapContent(mapContent);
        helper.setLayer(layer);
    }

    @Test
    public void getInfo() throws Exception {
        DirectPosition2D pos = new DirectPosition2D(WORLD.getCoordinateReferenceSystem());
        
        for (int i = 0; i < NUM_TEST_POINTS; i++) {
            pos.x = WORLD.getMinX() + WORLD.getWidth() * rand.nextDouble();
            pos.y = WORLD.getMinY() + WORLD.getHeight() * rand.nextDouble();
            InfoToolResult info = helper.getInfo(pos);
            
            int[] values = getValues(pos);
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
    
    private int[] getValues(DirectPosition pos) throws Exception {
        if (worldToGridTransform == null) {
            worldToGridTransform = 
                    reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER).inverse();
        }
        
        DirectPosition gridPos = worldToGridTransform.transform(pos, null);
        int x = (int) gridPos.getOrdinate(0);
        int y = (int) gridPos.getOrdinate(1);
        
        int[] values = new int[image.getNumBands()];
        for (int band = 0; band < values.length; band++) {
            values[band] = image.getSample(x, y, band);
        }
        
        return values;
    }
    
    private static void createImageAndReader() throws Exception {
        image = ImageUtils.createConstantImage(WIDTH, HEIGHT, new Integer[]{0, 0, 0});
        for (int band = 0; band < image.getNumBands(); band++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    image.setSample(x, y, band, rand.nextInt(256));
                }
            }
        }
        
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridCoverage2D coverage = gcf.create("cov", image, WORLD);
        
        File file = File.createTempFile("test-image", ".tiff");
        GeoTiffWriter writer = new GeoTiffWriter(file);
        writer.write(coverage, null);
        writer.dispose();
        
        reader = new GeoTiffReader(file);
    }
}
