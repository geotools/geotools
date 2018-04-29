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
package org.geotools.ysld.encode;

import org.geotools.styling.Displacement;

/** Encodes a {@link Displacement} as YSLD. */
public class DisplacementEncoder extends YsldEncodeHandler<Displacement> {

    DisplacementEncoder(Displacement displace) {
        super(displace);
    }

    @Override
    protected void encode(Displacement displace) {
        if (nullIf(displace.getDisplacementX(), 0) == null
                && nullIf(displace.getDisplacementY(), 0) == null) {
            return;
        }
        put("displacement", displace.getDisplacementX(), displace.getDisplacementY());
    }
}
