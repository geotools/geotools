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

import java.awt.image.WritableRaster;

/**
 * Wraps an iterator against a non pre-multiplied color iterator, and premultiplies rgb accordingly
 *
 * @author Andrea Aime - GeoSolutions
 */
class PremultiplyAccessor implements RgbaAccessor {

    RgbaAccessor delegate;

    public PremultiplyAccessor(RgbaAccessor delegate) {
        super();
        this.delegate = delegate;
    }

    public void readRow(int y) {
        delegate.readRow(y);
    }

    public void getColor(int x, int[] rgba) {
        delegate.getColor(x, rgba);

        int a = rgba[3];

        // premultiply if needed
        if (a != 255) {
            double mul = a / 255d;
            rgba[0] = (int) Math.round(rgba[0] * mul);
            rgba[1] = (int) Math.round(rgba[1] * mul);
            rgba[2] = (int) Math.round(rgba[2] * mul);
        }
    }

    public void setColor(int x, int r, int g, int b, int a) {
        // undo premultiply if required
        if (a != 255) {
            double mul = a / 255d;
            r = (int) Math.round(r / mul);
            g = (int) Math.round(g / mul);
            b = (int) Math.round(b / mul);
        }
        delegate.setColor(x, r, g, b, a);
    }

    @Override
    public void writeRow(int y, WritableRaster destination) {
        delegate.writeRow(y, destination);
    }
}
