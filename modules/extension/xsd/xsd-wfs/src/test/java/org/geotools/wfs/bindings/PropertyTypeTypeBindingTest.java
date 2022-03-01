/* (c) 2016 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.wfs.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.xml.namespace.QName;
import net.opengis.wfs.PropertyType;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PropertyTypeTypeBindingTest extends WFSTestSupport {

    public PropertyTypeTypeBindingTest() {
        super(WFS.PropertyType, PropertyType.class, Binding.OVERRIDE);
    }

    @Test
    public void testParseNullValue() throws Exception {
        String xml = "<Property>" + "<Name>myproperty</Name>" + "<Value></Value>" + "</Property>";
        buildDocument(xml);

        PropertyType gc = (PropertyType) parse();
        assertNotNull(gc);
        assertEquals(new QName(WFS.NAMESPACE, "myproperty"), gc.getName());
        assertNull(gc.getValue());
    }

    @Override
    @Test
    public void testParse() throws Exception {
        String xml =
                "<Property>" + "<Name>myproperty</Name>" + "<Value>test</Value>" + "</Property>";
        buildDocument(xml);

        PropertyType gc = (PropertyType) parse();
        assertNotNull(gc);
        assertEquals(new QName(WFS.NAMESPACE, "myproperty"), gc.getName());
        assertEquals("test", gc.getValue());
    }

    @Override
    @Test
    public void testEncode() throws Exception {
        PropertyType property = factory.createPropertyType();
        property.setValue("test");
        property.setName(new QName(WFS.NAMESPACE, "myproperty"));

        final Document dom = encode(property, WFS.Property);
        final Element root = dom.getDocumentElement();

        assertName(WFS.Property, root);
        assertEquals(2, root.getChildNodes().getLength());
        Element value = getElementByQName(root, new QName(WFS.NAMESPACE, "Value"));
        assertEquals("test", value.getFirstChild().getNodeValue());
        Element name = getElementByQName(root, new QName(WFS.NAMESPACE, "Name"));
        assertEquals("wfs:myproperty", name.getFirstChild().getNodeValue());
    }

    @Test
    public void testEncodeNull() throws Exception {
        PropertyType property = factory.createPropertyType();
        property.setName(new QName(WFS.NAMESPACE, "myproperty"));

        final Document dom = encode(property, WFS.Property);
        final Element root = dom.getDocumentElement();

        assertName(WFS.Property, root);
        assertEquals(2, root.getChildNodes().getLength());
        Element value = getElementByQName(root, new QName(WFS.NAMESPACE, "Value"));
        assertNull(value.getNodeValue());
        Element name = getElementByQName(root, new QName(WFS.NAMESPACE, "Name"));
        assertEquals("wfs:myproperty", name.getFirstChild().getNodeValue());
    }
}
