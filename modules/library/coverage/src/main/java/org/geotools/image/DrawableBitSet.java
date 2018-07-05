/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.BitSet;
import org.locationtech.jts.awt.ShapeWriter;
import org.locationtech.jts.geom.Geometry;

/**
 * An analogous of {@link BitSet} which is explicitly 2 dimensional and allows drawing a shape to
 * quickly turn on a large area of the map. This class is not thread safe.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class DrawableBitSet {

    BufferedImage image;
    WritableRaster raster;
    int[] pixel = new int[1];

    public DrawableBitSet(int width, int height) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        raster = image.getRaster();
    }

    public boolean get(int x, int y) {
        raster.getPixel(x, y, pixel);

        return pixel[0] > 0;
    }

    public void set(int x, int y) {
        pixel[0] = 1;
        raster.setPixel(x, y, pixel);
    }

    public void set(Geometry geom) {
        if (geom == null) {
            return;
        }

        set(new ShapeWriter().toShape(geom));
    }

    public void set(Shape shape) {
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.draw(shape);
        g2d.fill(shape);
        g2d.dispose();
    }

    public BufferedImage getImage() {
        return image;
    }
}
