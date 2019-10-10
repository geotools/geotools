/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import javax.media.jai.Interpolation;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.parameter.GeneralParameterValue;

/** All of the information in a {@link PGRasterReader#read(GeneralParameterValue[])} request. */
class ReadRequest {

    /** The reader */
    PGRasterReader reader;

    /** The raster / overview being requested */
    RasterColumn raster;

    /** Requested bounds in world coordinates */
    GeneralEnvelope bounds;

    /** Requested bounds transformed to raster native crs */
    ReferencedEnvelope nativeBounds;

    /** Requested area in raster coordinates */
    Rectangle region;

    /** Resolution (unit/pixel) of the request */
    Point.Double resolution;

    /** List of timestamps requested */
    List<?> times;

    /** Request overview policy */
    OverviewPolicy overviewPolicy;

    OverviewPolicy overviewPolicy() {
        return overviewPolicy != null ? overviewPolicy : OverviewPolicy.getDefaultPolicy();
    }

    /** Request interpolation */
    Interpolation interpolation;

    Interpolation interpolation() {
        return interpolation != null
                ? interpolation
                : Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
    }

    Color backgroundColor;

    ReadRequest(PGRasterReader reader) {
        this.reader = reader;
    }
}
