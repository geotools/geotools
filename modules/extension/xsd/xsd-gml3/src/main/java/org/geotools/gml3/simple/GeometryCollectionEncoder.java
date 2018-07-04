/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo).
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
import org.geotools.xml.Encoder;
import org.locationtech.jts.geom.GeometryCollection;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 generic geometry collection
 *
 * @author
 */
class GeometryCollectionEncoder extends GeometryEncoder<GeometryCollection> {

    static final QualifiedName MULTI_GEOMETRY =
            new QualifiedName(GML.NAMESPACE, "MultiGeometry", "gml");

    static final QualifiedName GEOMETRY_MEMBER =
            new QualifiedName(GML.NAMESPACE, "geometryMember", "gml");

    QualifiedName multiGeometry;

    QualifiedName geometryMember;

    GenericGeometryEncoder gge;

    protected GeometryCollectionEncoder(Encoder encoder, String gmlPrefix, String gmlUri) {
        super(encoder);
        gge = new GenericGeometryEncoder(encoder, gmlPrefix, gmlUri);
        multiGeometry = MULTI_GEOMETRY.derive(gmlPrefix, gmlUri);
        geometryMember = GEOMETRY_MEMBER.derive(gmlPrefix, gmlUri);
    }

    @Override
    public void encode(
            GeometryCollection geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        atts = cloneWithGmlId(atts, gmlId);
        handler.startElement(multiGeometry, atts);
        for (int i = 0; i < geometry.getNumGeometries(); i++) {
            handler.startElement(geometryMember, null);
            gge.encode(geometry.getGeometryN(i), null, handler, gmlId + "." + (i + 1));
            handler.endElement(geometryMember);
        }
        handler.endElement(multiGeometry);
    }
}
