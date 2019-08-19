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
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML2 multi polygon
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class MultiPolygonEncoder extends GeometryEncoder<MultiPolygon> {

    static final QualifiedName MULTI_POLYGON =
            new QualifiedName(GML.NAMESPACE, "MultiPolygon", "gml");

    static final QualifiedName POLYGON_MEMBER =
            new QualifiedName(GML.NAMESPACE, "polygonMember", "gml");

    QualifiedName multiPolygon;

    QualifiedName polygonMember;

    PolygonEncoder pe;

    protected MultiPolygonEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        pe = new PolygonEncoder(encoder, gmlPrefix);
        multiPolygon = MULTI_POLYGON.derive(gmlPrefix);
        polygonMember = POLYGON_MEMBER.derive(gmlPrefix);
    }

    @Override
    public void encode(MultiPolygon geometry, AttributesImpl atts, GMLWriter handler)
            throws Exception {
        handler.startElement(multiPolygon, atts);

        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            handler.startElement(polygonMember, null);
            pe.encode((Polygon) geometry.getGeometryN(i), null, handler);
            handler.endElement(polygonMember);
        }

        handler.endElement(multiPolygon);
    }
}
