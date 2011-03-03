/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex.filter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.geotools.data.ComplexTestData;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.geotools.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.gml3.GMLSchema;
import org.geotools.xlink.XLINK;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 * @source $URL:
 *         http://svn.geotools.org/geotools/branches/2.4.x/modules/unsupported/community-schemas/community-schema-ds/src/test/java/org/geotools/data/complex/filter/XPathTest.java $
 * @since 2.4
 */
public class XPathTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSteps() throws Exception {
        FeatureType complexType = ComplexTestData
                .createExample01MultiValuedComplexProperty(new UniqueNameFeatureTypeFactoryImpl());
        Name name = complexType.getName();
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(complexType, name, 0,
                Integer.MAX_VALUE, true, null);

        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("wq", name.getNamespaceURI());
        try {
            XPath.steps(descriptor, null, namespaces);
            fail("passed null");
        } catch (NullPointerException e) {
        }

        List expected;
        String xpath;

        xpath = "/";
        assertEquals(1, XPath.steps(descriptor, xpath, namespaces).size());
        XPath.Step step = (XPath.Step) XPath.steps(descriptor, xpath, namespaces).get(0);
        QName rootQName = new QName(name.getNamespaceURI(), name.getLocalPart());
        assertEquals(rootQName, step.getName());

        expected = Collections.singletonList(new XPath.Step(rootQName, 1));
        xpath = "wq_plus";
        assertEquals(expected, XPath.steps(descriptor, xpath, namespaces));

        expected = Collections.singletonList(new XPath.Step(rootQName, 1));
        xpath = "/wq_plus";
        assertEquals(expected, XPath.steps(descriptor, xpath, namespaces));

        expected = Collections.singletonList(new XPath.Step(rootQName, 1));
        xpath = "wq_plus/measurement/result/../../measurement/determinand_description/../..";
        assertEquals(expected, XPath.steps(descriptor, xpath, namespaces));

        expected = Arrays.asList(new XPath.Step[] {
                new XPath.Step(new QName(rootQName.getNamespaceURI(), "measurement"), 2),
                new XPath.Step(new QName(rootQName.getNamespaceURI(), "result"), 1) });

        xpath = "wq_plus/measurement/result/../../measurement[2]/result";
        assertEquals(expected, XPath.steps(descriptor, xpath, namespaces));

        expected = Arrays.asList(new XPath.Step[] {
                new XPath.Step(new QName(rootQName.getNamespaceURI(), "measurement"), 1),
                new XPath.Step(new QName(rootQName.getNamespaceURI(), "result"), 1) });
        xpath = "wq_plus/measurement/result/../result/.";
        assertEquals(expected, XPath.steps(descriptor, xpath, namespaces));

        expected = Arrays.asList(new XPath.Step[] { new XPath.Step(new QName(rootQName
                .getNamespaceURI(), "measurement"), 5) });
        xpath = "measurement/result/../../measurement[5]";
        assertEquals(expected, XPath.steps(descriptor, xpath, namespaces));
    }

    /**
     * Tests a location path of the form <code>"foo/bar/@baz"</code> gets built as a
     * {@link StepList} of attribute names <code>"foo/bar/baz"</code> (i.e. no distinction between
     * what's a "property" and what's an (xml) "attribute".
     * 
     * @throws Exception
     */
    public void testStepsWithXmlAttribute() throws Exception {
        FeatureType complexType = ComplexTestData
                .createExample01MultiValuedComplexProperty(new UniqueNameFeatureTypeFactoryImpl());
        Name name = complexType.getName();
        AttributeDescriptor descriptor = new AttributeDescriptorImpl(complexType, name, 0,
                Integer.MAX_VALUE, true, null);
        QName rootQName = new QName(name.getNamespaceURI(), name.getLocalPart());

        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("wq", name.getNamespaceURI());
        namespaces.declarePrefix("xlink", XLINK.NAMESPACE);

        StepList steps = XPath.steps(descriptor, "wq_plus/measurement[2]/@xlink:href", namespaces);
        assertNotNull(steps);
        assertEquals(steps.toString(), 2, steps.size());

        XPath.Step step1 = new XPath.Step(new QName(rootQName.getNamespaceURI(), "measurement"), 2,
                false);
        XPath.Step step2 = new XPath.Step(XLINK.HREF, 1, true);

        assertEquals(step1, steps.get(0));
        assertEquals(step2, steps.get(1));
    }

    public void testStepEquals() {
        XPath.Step step1 = new XPath.Step(XLINK.FROM, 1);

        XPath.Step step2 = new XPath.Step(XLINK.HREF, 1, false);
        XPath.Step step3 = new XPath.Step(XLINK.HREF, 1, false);

        XPath.Step step4 = new XPath.Step(XLINK.HREF, 1, true);
        XPath.Step step5 = new XPath.Step(XLINK.HREF, 2, false);

        assertFalse(step1.equals(null));
        assertFalse(step1.equals(new Object()));
        assertFalse(step1.equals(step2));

        assertTrue(step2.equals(step3));
        assertFalse(step2.equals(step4));
        assertFalse(step2.equals(step5));
    }

    /**
     * Test that some simple-content and non-simple-content types are correctly detected.
     */
    public void testIsSimpleContentType() {
        assertTrue(XPath.isSimpleContentType(GMLSchema.CODETYPE_TYPE));
        assertTrue(XPath.isSimpleContentType(GMLSchema.MEASURETYPE_TYPE));
        assertFalse(XPath.isSimpleContentType(GMLSchema.POINTTYPE_TYPE));
        assertFalse(XPath.isSimpleContentType(GMLSchema.POINTPROPERTYTYPE_TYPE));
        assertFalse(XPath.isSimpleContentType(GMLSchema.ABSTRACTFEATURETYPE_TYPE));
        assertFalse(XPath.isSimpleContentType(GMLSchema.ABSTRACTFEATURECOLLECTIONTYPE_TYPE));
    }

}
