/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import javax.annotation.Nullable;

/**
 * A zoom context that can find half way points between zoom levels.
 *
 * @author Kevin Smith, Boundless
 */
public abstract class MedialZoomContext implements ZoomContext {

    public MedialZoomContext() {
        super();
    }

    /** Get a scale between the given zoom level and the next */
    protected abstract double getMedialScale(int level);

    @Override
    public ScaleRange getRange(@Nullable Integer min, @Nullable Integer max) {
        double minDenom = 0;
        double maxDenom = Double.POSITIVE_INFINITY;
        // Note that scale denominator is inverse to zoom so the maximum denominator is controlled
        // by the minimum zoom and vis versa
        if (min != null) {
            maxDenom = getMedialScale(min - 1);
        }
        if (max != null) {
            minDenom = getMedialScale(max);
        }
        return new ScaleRange(minDenom, maxDenom);
    }
}
