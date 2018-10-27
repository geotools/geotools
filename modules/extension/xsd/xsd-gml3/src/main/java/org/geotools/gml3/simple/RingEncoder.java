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
import org.locationtech.jts.geom.Geometry;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML3 curved ring
 *
 * @author Andrea Aime - GeoSolutions
 */
public class RingEncoder extends MultiLineStringEncoder {

    static final QualifiedName RING = new QualifiedName(GML.NAMESPACE, "Ring", "gml");

    private QualifiedName ring;

    protected RingEncoder(Encoder e, String gmlPrefix, String gmlUri) {
        super(e, gmlPrefix, gmlUri, true);
        init(gmlPrefix, gmlUri);
    }

    protected RingEncoder(Encoder e, String gmlPrefix, String gmlUri, boolean encodeGmlId) {
        super(e, gmlPrefix, gmlUri, true, encodeGmlId);
        init(gmlPrefix, gmlUri);
    }

    private void init(String gmlPrefix, String gmlUri) {
        this.ring = RING.derive(gmlPrefix, gmlUri);
    }

    @Override
    public void encode(Geometry geometry, AttributesImpl atts, GMLWriter handler, String gmlId)
            throws Exception {
        handler.startElement(ring, atts);

        encodeMembers(geometry, handler, gmlId);

        handler.endElement(ring);
    }
}
