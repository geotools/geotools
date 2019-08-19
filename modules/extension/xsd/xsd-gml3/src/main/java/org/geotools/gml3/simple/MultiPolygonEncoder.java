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
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 multi polygon
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class MultiPolygonEncoder extends GeometryEncoder<MultiPolygon> {

    static final QualifiedName MULTI_SURFACE =
            new QualifiedName(GML.NAMESPACE, "MultiSurface", "gml");

    static final QualifiedName SURFACE_MEMBER =
            new QualifiedName(GML.NAMESPACE, "surfaceMember", "gml");

    QualifiedName multiSurface;

    QualifiedName surfaceMember;

    PolygonEncoder pe;

    protected MultiPolygonEncoder(Encoder encoder, String gmlPrefix, String gmlUri) {
        super(encoder);
        pe = new PolygonEncoder(encoder, gmlPrefix, gmlUri);
        init(gmlPrefix, gmlUri);
    }

    protected MultiPolygonEncoder(
            Encoder encoder, String gmlPrefix, String gmlUri, boolean encodeGmlId) {
        super(encoder, encodeGmlId);
        pe = new PolygonEncoder(encoder, gmlPrefix, gmlUri, encodeGmlId);
        init(gmlPrefix, gmlUri);
    }

    private void init(String gmlPrefix, String gmlUri) {
        multiSurface = MULTI_SURFACE.derive(gmlPrefix, gmlUri);
        surfaceMember = SURFACE_MEMBER.derive(gmlPrefix, gmlUri);
    }

    @Override
    public void encode(MultiPolygon geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        atts = cloneWithGmlId(atts, gmlId);
        handler.startElement(multiSurface, atts);

        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            handler.startElement(surfaceMember, null);
            pe.encode((Polygon) geometry.getGeometryN(i), null, handler, gmlId + "." + (i + 1));
            handler.endElement(surfaceMember);
        }

        handler.endElement(multiSurface);
    }
}
