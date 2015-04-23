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
package org.geotools.gml2.simple;

import org.geotools.gml2.GML;
import org.geotools.xml.Encoder;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Encodes a GML2 point
 * 
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class PointEncoder extends GeometryEncoder<Point> {

    static final QualifiedName POINT = new QualifiedName(GML.NAMESPACE, "Point", "gml");

    static final QualifiedName COORD = new QualifiedName(GML.NAMESPACE, "coord", "gml");

    static final QualifiedName X = new QualifiedName(GML.NAMESPACE, "X", "gml");

    static final QualifiedName Y = new QualifiedName(GML.NAMESPACE, "Y", "gml");

    QualifiedName point;

    QualifiedName coord;

    QualifiedName x;

    QualifiedName y;

    QualifiedName multiPolygon;

    protected PointEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        point = POINT.derive(gmlPrefix);
        coord = COORD.derive(gmlPrefix);
        x = X.derive(gmlPrefix);
        y = Y.derive(gmlPrefix);
    }

    @Override
    public void encode(Point geometry, AttributesImpl atts, GMLWriter handler) throws Exception {
        handler.startElement(point, atts);
        handler.startElement(coord, null);

        Coordinate c = geometry.getCoordinate();

        handler.startElement(x, null);
        handler.ordinate(c.x);
        handler.endElement(x);

        handler.startElement(y, null);
        handler.ordinate(c.y);
        handler.endElement(y);

        handler.endElement(coord);
        handler.endElement(point);
    }
}