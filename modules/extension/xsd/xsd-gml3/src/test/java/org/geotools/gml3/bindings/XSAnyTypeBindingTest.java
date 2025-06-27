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
package org.geotools.gml3.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.feature.AttributeImpl;
import org.geotools.feature.ComplexAttributeImpl;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.ComplexTypeImpl;
import org.geotools.gml3.GML3TestSupport;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.xs.XSSchema;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaLocator;
import org.geotools.xsd.XSD;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;

public class XSAnyTypeBindingTest extends GML3TestSupport {

    private static final String SAMPLE_CLASS_VALUE = "1.1.1";

    private static final String SAMPLE_UNRESTRICTED_VALUE =
            "Arbitrary text content: <value>XML</value> special characters will be escaped automatically.";

    static class ANYTYPETEST extends XSD {
        /** singleton instance */
        private static ANYTYPETEST instance = new ANYTYPETEST();

        public static final String NAMESPACE = "http://www.geotools.org/anytypetest";

        public static final QName OBSERVATION = new QName("http://www.geotools.org/anytypetest", "Observation");

        public static final QName UNRESTRICTED = new QName(NAMESPACE, "unrestrictedEl");

        /** private constructor. */
        private ANYTYPETEST() {}

        public static ANYTYPETEST getInstance() {
            return instance;
        }

        @Override
        public String getNamespaceURI() {
            return NAMESPACE;
        }

        /** Returns the location of 'AnyTypeTest.xsd'. */
        @Override
        public String getSchemaLocation() {
            return getClass().getResource("AnyTypeTest.xsd").toString();
        }

        @Override
        public SchemaLocator createSchemaLocator() {
            // we explicity return null here because of a circular dependnecy with
            // gml3 schema... returning null breaks the circle when the schemas are
            // being built
            return null;
        }

        /* Attributes */
    }

    static class AnyTypeTestConfiguration extends Configuration {
        public AnyTypeTestConfiguration() {
            super(ANYTYPETEST.getInstance());
        }

        @Override
        protected void registerBindings(MutablePicoContainer container) {}
    }

    static class MyConfiguration extends Configuration {

        public MyConfiguration() {
            super(ANYTYPETEST.getInstance());
            addDependency(new GMLConfiguration());
        }
    }

    @Override
    protected Map<String, String> getNamespaces() {
        final Map<String, String> namespaces = super.getNamespaces();
        namespaces.put("test", "http://www.geotools.org/anytypetest");
        return namespaces;
    }

    @Override
    protected Configuration createConfiguration() {
        return new MyConfiguration();
    }

    @Test
    public void testEncode() throws Exception {
        QName observation = ANYTYPETEST.OBSERVATION;
        ComplexAttribute myCode = checkAnyTypeTest(observation, SAMPLE_CLASS_VALUE);
        Document dom = encode(myCode, observation);
        // print(dom);
        assertEquals("test:Observation", dom.getDocumentElement().getNodeName());
        assertEquals(
                1, dom.getDocumentElement().getElementsByTagName("test:class").getLength());
        assertNotNull(dom.getDocumentElement()
                .getElementsByTagName("test:class")
                .item(0)
                .getFirstChild());
        assertEquals(
                SAMPLE_CLASS_VALUE,
                dom.getDocumentElement()
                        .getElementsByTagName("test:class")
                        .item(0)
                        .getFirstChild()
                        .getNodeValue());
    }

    @Test
    public void testEncodeUnrestricted() throws Exception {
        QName typeName = ANYTYPETEST.UNRESTRICTED;
        ComplexAttribute unrestricted = createUnrestrictedAttr(typeName, SAMPLE_UNRESTRICTED_VALUE);
        Document dom = encode(unrestricted, typeName);
        // print(dom);
        assertEquals("test:unrestrictedEl", dom.getDocumentElement().getNodeName());
        assertEquals(SAMPLE_UNRESTRICTED_VALUE, dom.getDocumentElement().getTextContent());
    }

    public ComplexAttribute checkAnyTypeTest(QName typeName, String classValue) {
        Name myType = new NameImpl(typeName.getNamespaceURI(), typeName.getLocalPart());

        List<Property> properties = new ArrayList<>();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();

        // assume attributes from same namespace as typename

        Name attName = new NameImpl(typeName.getNamespaceURI(), "class");
        // Name name, Class<?> binding, boolean isAbstract, List<Filter> restrictions,
        // PropertyType superType, InternationalString description
        AttributeType p = new AttributeTypeImpl(attName, String.class, false, false, null, null, null);
        AttributeDescriptor pd = new AttributeDescriptorImpl(p, attName, 0, 0, false, null);

        propertyDescriptors.add(pd);
        properties.add(new AttributeImpl(classValue, pd, null));

        ComplexTypeImpl at =
                new ComplexTypeImpl(myType, propertyDescriptors, false, false, Collections.emptyList(), null, null);

        AttributeDescriptorImpl ai = new AttributeDescriptorImpl(at, myType, 0, 0, false, null);

        return new ComplexAttributeImpl(properties, ai, null);
    }

    public ComplexAttribute createUnrestrictedAttr(QName typeName, String contents) {
        Name unrestrictedType = new NameImpl(typeName.getNamespaceURI(), typeName.getLocalPart());

        List<Property> properties = new ArrayList<>();
        List<PropertyDescriptor> propertyDescriptors = new ArrayList<>();

        // create fake attribute simpleContent
        Name attName = new NameImpl(null, "simpleContent");
        AttributeType p = new AttributeTypeImpl(attName, String.class, false, false, null, null, null);
        AttributeDescriptor pd = new AttributeDescriptorImpl(p, attName, 0, 0, true, null);

        propertyDescriptors.add(pd);
        properties.add(new AttributeImpl(contents, pd, null));

        ComplexTypeImpl at = new ComplexTypeImpl(
                unrestrictedType,
                propertyDescriptors,
                false,
                false,
                Collections.emptyList(),
                XSSchema.ANYTYPE_TYPE,
                null);

        AttributeDescriptorImpl ai = new AttributeDescriptorImpl(at, unrestrictedType, 0, 0, false, null);

        return new ComplexAttributeImpl(properties, ai, null);
    }
}
