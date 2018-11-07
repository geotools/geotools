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
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML2 multi line string
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class MultiLineStringEncoder extends GeometryEncoder<MultiLineString> {

    static final QualifiedName MULTI_LINE_STRING =
            new QualifiedName(GML.NAMESPACE, "MultiLineString", "gml");

    static final QualifiedName LINE_STRING_MEMBER =
            new QualifiedName(GML.NAMESPACE, "lineStringMember", "gml");

    LineStringEncoder lse;

    LinearRingEncoder lre;

    QualifiedName multiLineString;

    QualifiedName lineStringMember;

    protected MultiLineStringEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        lse = new LineStringEncoder(encoder, gmlPrefix);
        lre = new LinearRingEncoder(encoder, gmlPrefix);
        multiLineString = MULTI_LINE_STRING.derive(gmlPrefix);
        lineStringMember = LINE_STRING_MEMBER.derive(gmlPrefix);
    }

    @Override
    public void encode(MultiLineString geometry, AttributesImpl atts, GMLWriter handler)
            throws Exception {
        handler.startElement(multiLineString, atts);

        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            handler.startElement(lineStringMember, null);
            LineString line = (LineString) geometry.getGeometryN(i);
            if (line instanceof LinearRing) {
                lre.encode(line, null, handler);
            } else {
                lse.encode(line, null, handler);
            }
            handler.endElement(lineStringMember);
        }

        handler.endElement(multiLineString);
    }
}
