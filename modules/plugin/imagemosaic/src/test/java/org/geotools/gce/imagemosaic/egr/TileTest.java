/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.eclipse.imagen.PlanarImage;
import org.eclipse.imagen.media.vectorbin.ROIGeometry;
import org.geotools.geometry.jts.JTS;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;

public class TileTest {

    @Test
    public void testTileOffset() {
        Tile tile = new Tile(256, 256, 1, 1, new AffineTransform());

        // this image does not actually overlap with the tile
        PlanarImage imageOutside = toImage(new Rectangle2D.Double(0, 0, 20, 20));
        assertFalse(tile.draw(imageOutside));

        // this one does
        PlanarImage imageInside = toImage(new Rectangle2D.Double(260, 260, 20, 20));
        assertTrue(tile.draw(imageInside));
        assertFalse(tile.isFullyCovered());

        // this one fully covers it
        PlanarImage imageCovers = toImage(new Rectangle2D.Double(200, 200, 400, 400));
        assertTrue(tile.draw(imageCovers));
        assertTrue(tile.isFullyCovered());

        // final test, another image inside, but the tile is already full
        PlanarImage imageInside2 = toImage(new Rectangle2D.Double(450, 450, 20, 20));
        assertFalse(tile.draw(imageInside2));
    }

    private static PlanarImage toImage(Rectangle2D rectangle) {
        Polygon polygon = JTS.toGeometry(JTS.toEnvelope(rectangle));
        PlanarImage image = new ROIGeometry(polygon).getAsImage();
        return image;
    }
}
