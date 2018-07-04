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

import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.gml2.GML;
import org.geotools.xml.Encoder;
import org.locationtech.jts.geom.Envelope;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encodes a GML2 Envelope
 *
 * @author Justin Deoliveira, OpenGeo
 * @author Andrea Aime - GeoSolutions
 */
class EnvelopeEncoder extends ObjectEncoder<Envelope> {

    static final QualifiedName BOX = new QualifiedName(GML.NAMESPACE, "Box", "gml");

    QualifiedName box;

    protected EnvelopeEncoder(Encoder e, String gmlNamespace) {
        super(e);
        box = BOX.derive(gmlNamespace);
    }

    @Override
    public void encode(Envelope e, AttributesImpl atts, GMLWriter handler) throws Exception {
        handler.startElement(box, atts);
        handler.coordinates(
                new LiteCoordinateSequence(e.getMinX(), e.getMinY(), e.getMaxX(), e.getMaxY()));
        handler.endElement(box);
    }
}
