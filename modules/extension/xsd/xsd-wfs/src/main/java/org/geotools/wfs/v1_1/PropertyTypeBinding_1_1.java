/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wfs.v1_1;

import javax.xml.namespace.QName;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.WfsFactory;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.simple.GenericGeometryEncoder;
import org.geotools.wfs.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.EncoderDelegate;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

public class PropertyTypeBinding_1_1 extends AbstractComplexEMFBinding {
    private final GMLConfiguration gml;
    private final GenericGeometryEncoder geometryEncoder;
    private static final String VALUE = "Value";

    public PropertyTypeBinding_1_1(WfsFactory factory, Configuration configuration) {
        super(factory);

        gml = initializeGMLConfiguration(configuration);
        Encoder encoder = new Encoder(gml);
        encoder.setInline(true);
        geometryEncoder = new GenericGeometryEncoder(encoder);
    }

    @Deprecated
    public PropertyTypeBinding_1_1(WfsFactory factory) {
        this(factory, null);
    }

    private GMLConfiguration initializeGMLConfiguration(Configuration configuration) {
        if (configuration == null) {
            return new GMLConfiguration();
        }

        GMLConfiguration gml = configuration.getDependency(GMLConfiguration.class);

        return gml == null ? new GMLConfiguration() : gml;
    }

    @Override
    public QName getTarget() {
        return org.geotools.wfs.WFS.PropertyType;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        return value;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return super.parse(instance, node, value);
    }

    @Override
    public Object getProperty(final Object object, QName name) throws Exception {
        if (VALUE.equals(name.getLocalPart())) {
            return (EncoderDelegate) output -> {
                Object value = ((PropertyType) object).getValue();

                output.startElement(org.geotools.wfs.WFS.NAMESPACE, VALUE, "wfs:" + VALUE, new AttributesImpl());
                if (value instanceof Geometry geometry) {

                    GMLWriter handler = new GMLWriter(
                            output,
                            new NamespaceSupport(),
                            gml.getNumDecimals(),
                            gml.getForceDecimalEncoding(),
                            gml.getPadWithZeros(),
                            "gml",
                            gml.getEncodeMeasures());

                    geometryEncoder.encode(geometry, new AttributesImpl(), handler);

                } else {
                    String s = value.toString();
                    output.characters(s.toCharArray(), 0, s.length());
                }
                output.endElement(WFS.NAMESPACE, VALUE, "wfs:" + VALUE);
            };
        }

        return super.getProperty(object, name);
    }
}
