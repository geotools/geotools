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
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.GML;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.LineString;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 linear ring
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class LinearRingEncoder extends LineStringEncoder {

    static final QualifiedName LINEAR_RING = new QualifiedName(GML.NAMESPACE, "LinearRing", "gml");

    protected LinearRingEncoder(Encoder encoder, String gmlPrefix, String gmlUri) {
        super(encoder, LINEAR_RING.derive(gmlPrefix, gmlUri));
    }

    protected LinearRingEncoder(
            Encoder encoder, String gmlPrefix, String gmlUri, boolean encodeGmlId) {
        super(encoder, LINEAR_RING.derive(gmlPrefix, gmlUri), encodeGmlId);
    }

    @Override
    public void encode(LineString geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        // linearRing is not a geometry, just a component, has no id
        super.encode(geometry, atts, handler, null);
    }
}
