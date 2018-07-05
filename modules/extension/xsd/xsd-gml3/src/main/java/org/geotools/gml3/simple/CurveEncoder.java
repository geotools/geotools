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

import org.geotools.geometry.jts.CompoundCurvedGeometry;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.SingleCurvedGeometry;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.GML;
import org.geotools.xml.Encoder;
import org.locationtech.jts.geom.LineString;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 Curve
 *
 * @author Andrea Aime - GeoSolutions
 */
class CurveEncoder extends GeometryEncoder<LineString> {

    static final QualifiedName CURVE = new QualifiedName(GML.NAMESPACE, "Curve", "gml");

    static final QualifiedName SEGMENTS = new QualifiedName(GML.NAMESPACE, "segments", "gml");

    static final QualifiedName LINE_STRING_SEGMENT =
            new QualifiedName(GML.NAMESPACE, "LineStringSegment", "gml");

    static final QualifiedName ARC_STRING = new QualifiedName(GML.NAMESPACE, "ArcString", "gml");

    QualifiedName curve;

    QualifiedName segments;

    QualifiedName lineStringSegment;

    QualifiedName arcString;

    protected CurveEncoder(Encoder e, String gmlPrefix, String gmlUri) {
        super(e);
        this.curve = CURVE.derive(gmlPrefix, gmlUri);
        this.segments = SEGMENTS.derive(gmlPrefix, gmlUri);
        this.lineStringSegment = LINE_STRING_SEGMENT.derive(gmlPrefix, gmlUri);
        this.arcString = ARC_STRING.derive(gmlPrefix, gmlUri);
    }

    public void encode(LineString geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        if (gmlId != null) {
            atts = cloneWithGmlId(atts, gmlId);
        }

        handler.startElement(curve, atts);
        handler.startElement(segments, null);

        encodeContents(geometry, handler);

        handler.endElement(segments);
        handler.endElement(curve);
    }

    private void encodeContents(LineString geometry, GMLWriter handler) throws Exception {
        if (geometry instanceof SingleCurvedGeometry) {
            SingleCurvedGeometry curve = (SingleCurvedGeometry) geometry;
            encodeCurve(curve, handler);
        } else if (geometry instanceof CompoundCurvedGeometry) {
            CompoundCurvedGeometry<LineString> compound = (CompoundCurvedGeometry) geometry;
            for (LineString component : compound.getComponents()) {
                encodeContents(component, handler);
            }
        } else {
            encodeLinestring(geometry, handler);
        }
    }

    private void encodeLinestring(LineString geometry, GMLWriter handler) throws Exception {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(GML.NAMESPACE, "interpolation", "interpolation", "", "linear");
        handler.startElement(lineStringSegment, atts);

        handler.posList(geometry.getCoordinateSequence());

        handler.endElement(lineStringSegment);
    }

    private void encodeCurve(SingleCurvedGeometry curve, GMLWriter handler) throws Exception {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(
                GML.NAMESPACE, "interpolation", "interpolation", "", "circularArc3Points");
        handler.startElement(arcString, atts);

        handler.posList(new LiteCoordinateSequence(curve.getControlPoints()));

        handler.endElement(arcString);
    }
}
