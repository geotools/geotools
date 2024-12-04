package org.geotools.wfs.v1_1;

import javax.xml.namespace.QName;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.WfsFactory;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.simple.GenericGeometryEncoder;
import org.geotools.wfs.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;
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

    private static final String VALUE = "Value";

    public PropertyTypeBinding_1_1(WfsFactory factory) {
        super(factory);
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
            return (EncoderDelegate)
                    output -> {
                        Object value = ((PropertyType) object).getValue();

                        output.startElement(
                                org.geotools.wfs.WFS.NAMESPACE,
                                VALUE,
                                "wfs:" + VALUE,
                                new AttributesImpl());
                        if (value instanceof Geometry) {
                            Geometry geometry = (Geometry) value;

                            GMLConfiguration gml = new GMLConfiguration();

                            Encoder encoder = new Encoder(gml);
                            encoder.setInline(true);

                            GenericGeometryEncoder geometryEncoder =
                                    new GenericGeometryEncoder(encoder);
                            GMLWriter handler =
                                    new GMLWriter(
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
