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
 * A color component accessor for byte oriented rasters in RGB order
 *
 * @author Andrea Aime - GeoSolutions
 */
class ByteRgbAccessor implements RgbaAccessor {

    final Raster raster;

    final byte[] pixels;

    final boolean hasAlpha;

    final int pixelSize;

    public ByteRgbAccessor(Raster raster, boolean hasAlpha) {
        this.raster = raster;
        this.pixelSize = hasAlpha ? 4 : 3;
        this.pixels = new byte[raster.getWidth() * pixelSize];
        this.hasAlpha = hasAlpha;
    }

    @Override
    public void readRow(int y) {
        raster.getDataElements(0, y, raster.getWidth(), 1, pixels);
    }

    @Override
    public void getColor(int x, int[] rgba) {
        final int base = x * pixelSize;
        rgba[0] = pixels[base] & 0xFF;
        rgba[1] = pixels[base + 1] & 0xFF;
        rgba[2] = pixels[base + 2] & 0xFF;
        rgba[3] = hasAlpha ? pixels[base + 3] & 0xFF : 255;
    }

    @Override
    public void setColor(int x, int r, int g, int b, int a) {
        final int base = x * pixelSize;
        pixels[base] = (byte) (r & 0xFF);
        pixels[base + 1] = (byte) (g & 0xFF);
        pixels[base + 2] = (byte) (b & 0xFF);
        if (hasAlpha) {
            pixels[base + 3] = (byte) (a & 0xFF);
        }
    }

    @Override
    public void writeRow(int y, WritableRaster destination) {
        destination.setDataElements(0, y, raster.getWidth(), 1, pixels);
    }
}
