/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.label;

import org.locationtech.jts.geom.LineString;

/**
 * Allows to move a point cursor along the path of a LineString using a curvilinear coordinate system and either
 * absolute distances (from the start of the line) or offsets relative to the current position, to return the absolute
 * position of the cursor as a Point, and to get the orientation of the current segment.
 *
 * @author Andrea Aime
 * @deprecated Use {@link org.geotools.geometry.jts.LineStringCursor} instead
 */
@Deprecated
public class LineStringCursor extends org.geotools.geometry.jts.LineStringCursor {

    public LineStringCursor(LineString ls) {
        super(ls);
    }

    public LineStringCursor(org.geotools.geometry.jts.LineStringCursor cursor) {
        super(cursor);
    }
}
