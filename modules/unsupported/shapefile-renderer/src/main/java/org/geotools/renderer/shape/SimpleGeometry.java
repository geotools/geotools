/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import org.geotools.data.shapefile.shp.ShapeType;

import com.vividsolutions.jts.geom.Envelope;


/**
 *
 * @source $URL$
 */
public class SimpleGeometry {
    public final ShapeType type;
    public final double[][] coords;
    public final Envelope bbox;

    public SimpleGeometry(ShapeType shapeType, double[][] coords, Envelope bbox) {
        this.type = shapeType;
        this.coords = coords;
        this.bbox = bbox;
    }

    public String toString() {
        return coords[0].toString();
    }
}
