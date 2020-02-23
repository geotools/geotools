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
 */
package org.geotools.geometry.jts;

import java.util.List;
import org.locationtech.jts.geom.LineString;

/**
 * Convenience interface to expose methods common to {@link CompoundCurve} and {@link CompoundRing}
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface CompoundCurvedGeometry<T extends LineString> extends CurvedGeometry<T> {

    /** Returns the linearized coordinates at the given tolerance. */
    public List<LineString> getComponents();
}
