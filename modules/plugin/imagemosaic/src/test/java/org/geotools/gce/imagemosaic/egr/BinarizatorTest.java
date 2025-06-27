/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.egr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.util.logging.Logger;
import javax.media.jai.ROI;
import org.geotools.util.logging.Logging;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/** @author Emanuele Tajariol <etj at geo-solutions.it> */
public class BinarizatorTest {
    private static final Logger LOGGER = Logging.getLogger(BinarizatorTest.class);

    private static final int TILE_WIDTH = 64;

    private static final int TILE_HEIGHT = 64;

    GeometryFactory gf = new GeometryFactory();

    public BinarizatorTest() {}

    @Test
    public void testTileNum() {
        Polygon bbox = createBBox(0, 0, 6400, 6400);
        {
            Binarizator bin = new Binarizator(bbox, 256, 256, TILE_WIDTH, TILE_HEIGHT);
            assertEquals(4 * 4, bin.getActiveTiles().size());
        }
        {
            Binarizator bin = new Binarizator(bbox, 257, 256, TILE_WIDTH, TILE_HEIGHT);
            assertEquals(4 * 5, bin.getActiveTiles().size());
        }
        {
            Binarizator bin = new Binarizator(bbox, 257, 257, TILE_WIDTH, TILE_HEIGHT);
            assertEquals(5 * 5, bin.getActiveTiles().size());
        }
    }

    /**
     * This test used to work with antialias off and drawing performed with draw + fill. Its seems that antialias on and
     * fill() only will draw a square of 44x44 px, not 45x45. TO BE CHECKED.
     */
    @Test
    @Ignore
    public void testPixelNumber() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        Polygon feature = createBBox(100, 100, 540, 540);
        boolean added = bin.add(feature);

        assertTrue("Feature not added", added);

        LOGGER.info("Checking tile : " + bin.getActiveTiles().get(3));

        assertEquals(0, bin.getActiveTiles().get(1).getCoverageCount());
        assertEquals(45 * 45, bin.getActiveTiles().get(3).getCoverageCount());
    }

    @Test
    public void testTileRemoval() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        assertEquals(4 * 4, bin.getActiveTiles().size());

        Polygon feature = createBBox(0, 0, 640, 640);
        boolean added = bin.add(feature);

        assertTrue("Feature not added", added);

        assertEquals(4 * 4 - 1, bin.getActiveTiles().size());
    }

    @Test
    public void testTileRemovalRaster() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        assertEquals(4 * 4, bin.getActiveTiles().size());

        Polygon feature = createBBox(0, 0, 640, 640);
        boolean added = bin.add(toRasterROI(feature));

        assertTrue("Feature not added", added);

        assertEquals(4 * 4 - 1, bin.getActiveTiles().size());
    }

    @Test
    public void testTileRemovalRasterOffset() {
        // offset origin so that tile raster space won't line up with ROI ones
        Polygon reqBBox = createBBox(1000, 1000, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        assertEquals(4 * 4, bin.getActiveTiles().size());

        Polygon feature = createBBox(1000, 1000, 1640, 1640);
        boolean added = bin.add(toRasterROI(feature));

        assertTrue("Feature not added", added);

        assertEquals(4 * 4 - 1, bin.getActiveTiles().size());
    }

    @NotNull
    private static ROI toRasterROI(Polygon feature) {
        return new ROI(new ROIGeometry(feature).getAsImage());
    }

    @Test
    public void testFeatRemovalInMissingTile() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        Polygon feature = createBBox(0, 0, 640, 640);
        boolean added = bin.add(feature);
        assertTrue("Feature not added", added);

        assertEquals("Tile not removed", 4 * 4 - 1, bin.getActiveTiles().size());

        Polygon hidden = createBBox(10, 10, 600, 600);
        boolean addedhidden = bin.add(hidden);
        assertFalse("Hidden feature has been added", addedhidden);
    }

    @Test
    public void testFeatRemovalInMissingTileRaster() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        Polygon feature = createBBox(0, 0, 640, 640);
        boolean added = bin.add(toRasterROI(feature));
        assertTrue("Feature not added", added);

        assertEquals("Tile not removed", 4 * 4 - 1, bin.getActiveTiles().size());

        Polygon hidden = createBBox(10, 10, 600, 600);
        boolean addedhidden = bin.add(toRasterROI(hidden));
        assertFalse("Hidden feature has been added", addedhidden);
    }

    @Test
    public void testHiddenFeature() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        assertEquals(4 * 4, bin.getActiveTiles().size());

        Polygon feature = createBBox(9, 9, 639, 639);
        boolean added = bin.add(feature);
        assertTrue("Feature not added", added);

        assertEquals("Tile should not be removed", 4 * 4, bin.getActiveTiles().size());

        Polygon hidden = createBBox(10, 10, 600, 600);
        boolean addedhidden = bin.add(hidden);
        assertFalse("Hidden feature has been added", addedhidden);
    }

    @Test
    public void testHiddenFeatureRaster() {
        Polygon reqBBox = createBBox(0, 0, 2560, 2560);
        Binarizator bin = new Binarizator(reqBBox, 256, 256, TILE_WIDTH, TILE_HEIGHT);

        assertEquals(4 * 4, bin.getActiveTiles().size());

        Polygon feature = createBBox(9, 9, 639, 639);
        boolean added = bin.add(toRasterROI(feature));
        assertTrue("Feature not added", added);

        assertEquals("Tile should not be removed", 4 * 4, bin.getActiveTiles().size());

        Polygon hidden = createBBox(10, 10, 600, 600);
        boolean addedhidden = bin.add(toRasterROI(hidden));
        assertFalse("Hidden feature has been added", addedhidden);
    }

    protected Polygon createBBox(double x0, double y0, double x1, double y1) {
        CoordinateSequence points = new CoordinateArraySequence(5, 2);
        points.setOrdinate(0, 0, x0);
        points.setOrdinate(0, 1, y0);
        points.setOrdinate(1, 0, x1);
        points.setOrdinate(1, 1, y0);
        points.setOrdinate(2, 0, x1);
        points.setOrdinate(2, 1, y1);
        points.setOrdinate(3, 0, x0);
        points.setOrdinate(3, 1, y1);
        points.setOrdinate(4, 0, x0);
        points.setOrdinate(4, 1, y0);

        LinearRing lr = new LinearRing(points, gf);

        return new Polygon(lr, null, gf);
    }
}
