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
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.LineString;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML2 linestring
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class LineStringEncoder extends GeometryEncoder<LineString> {

    static final QualifiedName LINE_STRING = new QualifiedName(GML.NAMESPACE, "LineString", "gml");

    QualifiedName element;

    protected LineStringEncoder(Encoder encoder, String gmlPrefix) {
        this(encoder, LINE_STRING.derive(gmlPrefix));
    }

    protected LineStringEncoder(Encoder encoder, QualifiedName element) {
        super(encoder);
        this.element = element;
    }

    public void encode(LineString geometry, AttributesImpl atts, GMLWriter handler)
            throws Exception {
        handler.startElement(element, atts);
        handler.coordinates(geometry.getCoordinateSequence());
        handler.endElement(element);
    }
}
