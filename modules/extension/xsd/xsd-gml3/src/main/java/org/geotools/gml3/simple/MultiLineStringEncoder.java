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
package org.geotools.gml3.simple;

import org.geotools.geometry.jts.CurvedGeometry;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.GML;
import org.geotools.xml.Encoder;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;

/**
 * Encodes a GML3 multi line string
 * 
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class MultiLineStringEncoder extends GeometryEncoder<Geometry> {
    
    static final QualifiedName MULTI_CURVE = new QualifiedName(GML.NAMESPACE, "MultiCurve", "gml");

    static final QualifiedName CURVE_MEMBER = new QualifiedName(GML.NAMESPACE, "curveMember", "gml");

    static final QualifiedName MULTI_LINE_STRING = new QualifiedName(GML.NAMESPACE, "MultiLineString", "gml");

    static final QualifiedName LINE_STRING_MEMBER = new QualifiedName(GML.NAMESPACE, "lineStringMember", "gml");

    LineStringEncoder lse;

    LinearRingEncoder lre;

    CurveEncoder ce;

    QualifiedName multiContainer;

    QualifiedName member;

    boolean curveEncoding;

    protected MultiLineStringEncoder(Encoder encoder, String gmlPrefix, boolean curveEncoding) {
        super(encoder);
        lse = new LineStringEncoder(encoder, gmlPrefix);
        lre = new LinearRingEncoder(encoder, gmlPrefix);
        ce = new CurveEncoder(encoder, gmlPrefix);
        this.curveEncoding = curveEncoding;
        if(curveEncoding) {
            multiContainer = MULTI_CURVE.derive(gmlPrefix);
            member = CURVE_MEMBER.derive(gmlPrefix);
        } else {
            multiContainer = MULTI_LINE_STRING.derive(gmlPrefix);
            member = LINE_STRING_MEMBER.derive(gmlPrefix);
        }
    }

    @Override
    public void encode(Geometry geometry, AttributesImpl atts, GMLWriter handler)
            throws Exception {
        handler.startElement(multiContainer, atts);

        encodeMembers(geometry, handler);

        handler.endElement(multiContainer);
    }

    protected void encodeMembers(Geometry geometry, GMLWriter handler) throws SAXException,
            Exception {
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            handler.startElement(member, null);
            LineString line = (LineString) geometry.getGeometryN(i);
            if (curveEncoding && line instanceof CurvedGeometry) {
                ce.encode(line, null, handler);
            } else if (line instanceof LinearRing) {
                lre.encode(line, null, handler);
            } else {
                lse.encode(line, null, handler);
            }
            handler.endElement(member);
        }
    }



}