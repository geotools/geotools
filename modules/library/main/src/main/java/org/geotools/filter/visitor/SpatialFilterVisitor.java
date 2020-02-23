/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

/**
 * Filter that can be applied to determine if a Filter contains any spatial filter
 *
 * @author Andrea Aime - GeoSolutions
 * @since 2.7.5
 */
public class SpatialFilterVisitor extends DefaultFilterVisitor {

    boolean hasSpatialFilter = false;

    /** True if the filter had a spatial filter, false otherwise */
    public boolean hasSpatialFilter() {
        return hasSpatialFilter;
    }

    /** Resets this visitor so that it can be reused on another filter */
    public void reset() {
        hasSpatialFilter = false;
    }

    public Object visit(final BBOX filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Beyond filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Contains filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Crosses filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Disjoint filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(DWithin filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Equals filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Intersects filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Overlaps filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Touches filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }

    public Object visit(Within filter, Object data) {
        hasSpatialFilter = true;
        return data;
    }
}
