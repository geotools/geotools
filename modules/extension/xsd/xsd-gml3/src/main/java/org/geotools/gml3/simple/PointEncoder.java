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

import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.GML;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.Point;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 point
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class PointEncoder extends GeometryEncoder<Point> {
    static final QualifiedName POINT = new QualifiedName(GML.NAMESPACE, "Point", "gml");

    static final QualifiedName POS = new QualifiedName(GML.NAMESPACE, "pos", "gml");

    QualifiedName point;

    QualifiedName pos;

    protected PointEncoder(Encoder encoder, String gmlPrefix, String gmlUri) {
        super(encoder);
        init(gmlPrefix, gmlUri);
    }

    protected PointEncoder(Encoder encoder, String gmlPrefix, String gmlUri, boolean encodeGmlId) {
        super(encoder, encodeGmlId);
        init(gmlPrefix, gmlUri);
    }

    private void init(String gmlPrefix, String gmlUri) {
        point = POINT.derive(gmlPrefix, gmlUri);
        pos = POS.derive(gmlPrefix, gmlUri);
    }

    @Override
    public void encode(Point geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        atts = cloneWithGmlId(atts, gmlId);
        handler.startElement(point, atts);
        handler.startElement(pos, null);
        handler.position(geometry.getCoordinateSequence());
        handler.endElement(pos);
        handler.endElement(point);
    }
}
