/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

import java.awt.image.IndexColorModel;

/**
 * Wraps around another palette and adds last match caching. This speeds up significantly lookups on
 * maps that have large areas with constant color
 * 
 * @author Andrea Aime - GeoSolutions
 */
public class CachingColorIndexer implements ColorIndexer {
    ColorIndexer delegate;

    int lr, lg, lb, la;

    int idx = -1;

    public CachingColorIndexer(ColorIndexer delegate) {
        this.delegate = delegate;
    }

    @Override
    public IndexColorModel toIndexColorModel() {
        return delegate.toIndexColorModel();
    }

    @Override
    public int getClosestIndex(int r, int g, int b, int a) {
        synchronized (this) {
            if (r == lr && g == lg && b == lb && a == la && idx >= 0) {
                return idx;
            }
        }

        int delegateIdx = delegate.getClosestIndex(r, g, b, a);

        synchronized (this) {
            lr = r;
            lg = g;
            lb = b;
            la = a;
            idx = delegateIdx;
        }

        return delegateIdx;
    }

}
