/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Point;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML2 point
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class PointEncoder extends GeometryEncoder<Point> {

    static final QualifiedName POINT = new QualifiedName(GML.NAMESPACE, "Point", "gml");

    static final QualifiedName COORD = new QualifiedName(GML.NAMESPACE, "coord", "gml");

    QualifiedName point;

    QualifiedName coord;

    QualifiedName multiPolygon;

    protected PointEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        point = POINT.derive(gmlPrefix);
        coord = COORD.derive(gmlPrefix);
    }

    @Override
    public void encode(Point geometry, AttributesImpl atts, GMLWriter handler) throws Exception {
        handler.startElement(point, atts);
        handler.coordinates(geometry.getCoordinateSequence());
        handler.endElement(point);
    }
}
