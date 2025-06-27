/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.composite;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * A color component accessor for integer oriented rasters in BGR order
 *
 * @author Andrea Aime - GeoSolutions
 */
class IntegerBgrAccessor implements RgbaAccessor {

    Raster raster;

    int[] pixels;

    boolean hasAlpha;

    public IntegerBgrAccessor(Raster raster, boolean hasAlpha) {
        this.raster = raster;
        this.pixels = new int[raster.getWidth()];
        this.hasAlpha = hasAlpha;
    }

    @Override
    public void readRow(int y) {
        raster.getDataElements(0, y, raster.getWidth(), 1, pixels);
    }

    @Override
    public void getColor(int x, int[] rgba) {
        int pixel = pixels[x];
        rgba[0] = pixel & 0xFF;
        rgba[1] = pixel >> 8 & 0xFF;
        rgba[2] = pixel >> 16 & 0xFF;
        rgba[3] = hasAlpha ? pixel >> 24 & 0xFF : 255;
    }

    @Override
    public void setColor(int x, int r, int g, int b, int a) {
        if (!hasAlpha) {
            a = 255;
        }
        int pixel = a << 24 | r << 16 | g << 8 | b & 0xFF;
        pixels[x] = pixel;
    }

    @Override
    public void writeRow(int y, WritableRaster destination) {
        destination.setDataElements(0, y, raster.getWidth(), 1, pixels);
    }
}
