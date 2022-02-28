package org.geotools.wfs.v2_0.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.namespace.QName;
import net.opengis.wfs20.PropertyType;
import net.opengis.wfs20.ValueReferenceType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PropertyTypeBindingTest extends WFSTestSupport {

    @Test
    public void testParseNullValue() throws Exception {
        String xml =
                "<Property>"
                        + "<ValueReference action=\"replace\">myproperty</ValueReference>"
                        + "<Value></Value>"
                        + "</Property>";
        buildDocument(xml);

        PropertyType gc = (PropertyType) parse();
        assertNotNull(gc);
        assertNotNull(gc.getValueReference());
        assertEquals(new QName(WFS.NAMESPACE, "myproperty"), gc.getValueReference().getValue());
        assertNull(gc.getValue());
    }

    @Test
    public void testParseNotNullValue() throws Exception {
        String xml =
                "<Property>"
                        + "<ValueReference action=\"replace\">myproperty</ValueReference>"
                        + "<Value>test</Value>"
                        + "</Property>";
        buildDocument(xml);

        PropertyType gc = (PropertyType) parse();
        assertNotNull(gc);
        assertNotNull(gc.getValueReference());
        assertEquals(new QName(WFS.NAMESPACE, "myproperty"), gc.getValueReference().getValue());
        assertEquals("test", gc.getValue());
    }

    @Test
    public void testEncodeNotNull() throws Exception {
        PropertyType property = Wfs20Factory.eINSTANCE.createPropertyType();
        property.setValue("test");

        ValueReferenceType valueRefType = Wfs20Factory.eINSTANCE.createValueReferenceType();
        valueRefType.setValue(new QName(WFS.NAMESPACE, "myproperty"));
        property.setValueReference(valueRefType);
        final Document dom = encode(property, WFS.Property);
        final Element root = dom.getDocumentElement();

        assertEquals(2, root.getChildNodes().getLength());
        Element value = getElementByQName(root, new QName(WFS.NAMESPACE, "Value"));
        assertEquals("test", value.getFirstChild().getNodeValue());
        Element name = getElementByQName(root, new QName(WFS.NAMESPACE, "ValueReference"));
        assertEquals("myproperty", name.getFirstChild().getNodeValue());
    }

    @Test
    public void testEncodeNull() throws Exception {
        PropertyType property = Wfs20Factory.eINSTANCE.createPropertyType();

        ValueReferenceType valueRefType = Wfs20Factory.eINSTANCE.createValueReferenceType();
        valueRefType.setValue(new QName(WFS.NAMESPACE, "myproperty"));
        property.setValueReference(valueRefType);
        final Document dom = encode(property, WFS.Property);
        final Element root = dom.getDocumentElement();

        assertEquals(2, root.getChildNodes().getLength());
        Element value = getElementByQName(root, new QName(WFS.NAMESPACE, "Value"));
        assertNull(value.getNodeValue());
        Element name = getElementByQName(root, new QName(WFS.NAMESPACE, "ValueReference"));
        assertEquals("myproperty", name.getFirstChild().getNodeValue());
    }
}
