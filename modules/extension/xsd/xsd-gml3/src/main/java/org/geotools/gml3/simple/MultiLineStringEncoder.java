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
package org.geotools.gml3.simple;

import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.GML;
import org.geotools.xml.Encoder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 multi line string
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class MultiLineStringEncoder extends GeometryEncoder<Geometry> {

    static final QualifiedName MULTI_CURVE = new QualifiedName(GML.NAMESPACE, "MultiCurve", "gml");

    static final QualifiedName CURVE_MEMBER =
            new QualifiedName(GML.NAMESPACE, "curveMember", "gml");

    static final QualifiedName MULTI_LINE_STRING =
            new QualifiedName(GML.NAMESPACE, "MultiLineString", "gml");

    static final QualifiedName LINE_STRING_MEMBER =
            new QualifiedName(GML.NAMESPACE, "lineStringMember", "gml");

    LineStringEncoder lse;

    LinearRingEncoder lre;

    CurveEncoder ce;

    QualifiedName multiContainer;

    QualifiedName member;

    boolean curveEncoding;

    protected MultiLineStringEncoder(
            Encoder encoder, String gmlPrefix, String gmlUri, boolean curveEncoding) {
        super(encoder);
        lse = new LineStringEncoder(encoder, gmlPrefix, gmlUri);
        lre = new LinearRingEncoder(encoder, gmlPrefix, gmlUri);
        ce = new CurveEncoder(encoder, gmlPrefix, gmlUri);
        this.curveEncoding = curveEncoding;
        if (curveEncoding) {
            multiContainer = MULTI_CURVE.derive(gmlPrefix, gmlUri);
            member = CURVE_MEMBER.derive(gmlPrefix, gmlUri);
        } else {
            multiContainer = MULTI_LINE_STRING.derive(gmlPrefix, gmlUri);
            member = LINE_STRING_MEMBER.derive(gmlPrefix, gmlUri);
        }
    }

    @Override
    public void encode(Geometry geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        atts = cloneWithGmlId(atts, gmlId);
        handler.startElement(multiContainer, atts);

        encodeMembers(geometry, handler, gmlId);

        handler.endElement(multiContainer);
    }

    protected void encodeMembers(Geometry geometry, GMLWriter handler, String gmlId)
            throws SAXException, Exception {
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            handler.startElement(member, null);
            LineString line = (LineString) geometry.getGeometryN(i);
            String childId = gmlId + "." + (i + 1);
            if (curveEncoding && line instanceof CurvedGeometry) {
                ce.encode(line, null, handler, childId);
            } else if (line instanceof LinearRing) {
                lre.encode(line, null, handler, childId);
            } else {
                lse.encode(line, null, handler, childId);
            }
            handler.endElement(member);
        }
    }
}
