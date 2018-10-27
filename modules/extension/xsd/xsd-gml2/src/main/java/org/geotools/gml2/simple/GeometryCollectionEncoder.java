/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.locationtech.jts.geom.GeometryCollection;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML2 generic geometry collection
 *
 * @author
 */
public class GeometryCollectionEncoder extends GeometryEncoder<GeometryCollection> {
    static final QualifiedName GEOMETRY_COLLECTION =
            new QualifiedName(GML.NAMESPACE, "GeometryCollection", "gml");

    QualifiedName element;
    static Encoder encoder;

    public GeometryCollectionEncoder(Encoder encoder, String gmlPrefix) {
        super(encoder);
        GeometryCollectionEncoder.encoder = encoder;
    }

    @Override
    public void encode(GeometryCollection geometry, AttributesImpl atts, GMLWriter handler)
            throws Exception {
        handler.startElement(GEOMETRY_COLLECTION, atts);
        if (geometry.getNumGeometries() < 1) {
            throw new Exception("More than 1 geometry required!");
        } else {
            GenericGeometryEncoder gec =
                    new GenericGeometryEncoder(GeometryCollectionEncoder.encoder);
            // For every geometry within the GeometryCollection call encoder
            for (int i = 0; i < geometry.getNumGeometries(); i++) {
                gec.encode(geometry.getGeometryN(i), atts, handler);
            }
        }
        handler.endElement(GEOMETRY_COLLECTION);
    }
}
