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
package org.geotools.wfs.v2_0;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.gml2.simple.FeatureCollectionEncoderDelegate;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.simple.GML32FeatureCollectionEncoderDelegate;
import org.geotools.gml3.v3_2.GML;
import org.geotools.xsd.Encoder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Encoder delegate for high speed GML 3.2 encoding of SimpleFeatureCollection
 *
 * @author Andrea Aime - GeoSolutions
 */
class WFS20FeatureCollectionEncoderDelegate extends FeatureCollectionEncoderDelegate {

    public WFS20FeatureCollectionEncoderDelegate(
            SimpleFeatureCollection features, Encoder encoder) {
        super(features, encoder, new WFS20EncoderDelegate(encoder));
        this.encodeGeometryIds = true;
    }

    @Override
    protected Attributes getPropertyAttributes(
            QualifiedName name,
            FeatureType featureType,
            AttributeDescriptor attribute,
            Object value) {
        if ("identifier".equals(name.getLocalPart())
                && GML.NAMESPACE.equals(name.getNamespaceURI())) {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(
                    null, "codeSpace", "codeSpace", null, featureType.getName().getNamespaceURI());
            return atts;
        }

        return null;
    }

    static class WFS20EncoderDelegate extends GML32FeatureCollectionEncoderDelegate.GML32Delegate {

        static final QualifiedName MEMBER =
                new QualifiedName(WFS.NAMESPACE, GML.member.getLocalPart(), "wfs");

        static final QualifiedName TUPLE = new QualifiedName(WFS.NAMESPACE, "Tuple", "wfs");

        QualifiedName tuple;

        public WFS20EncoderDelegate(Encoder encoder) {
            super(encoder);
            this.member = MEMBER;
            this.tuple = TUPLE;
        }

        @Override
        public boolean supportsTuples() {
            return true;
        }

        @Override
        public void startTuple(GMLWriter handler) throws SAXException {
            handler.startElement(member, null);
            handler.startElement(tuple, null);
        }

        @Override
        public void endTuple(GMLWriter handler) throws SAXException {
            handler.endElement(tuple);
            handler.endElement(member);
        }
    }
}
