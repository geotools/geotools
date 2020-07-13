/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2011, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.data.ComplexTestData;
import org.geotools.data.complex.feature.type.UniqueNameFeatureTypeFactoryImpl;
import org.geotools.data.complex.util.XPathUtil;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.xlink.XLINK;
import org.junit.Test;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * @author Gabriel Roldan (Axios Engineering)
 * @version $Id$
 * @since 2.4
 */
public class XPathUtilTest {

    @Test
    public void testSteps() throws Exception {
        FeatureType complexType =
                ComplexTestData.createExample01MultiValuedComplexProperty(
                        new UniqueNameFeatureTypeFactoryImpl());
        Name name = complexType.getName();
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(complexType, name, 0, Integer.MAX_VALUE, true, null);

        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("wq", name.getNamespaceURI());
        try {
            XPathUtil.steps(descriptor, null, namespaces);
            fail("passed null");
        } catch (NullPointerException e) {
        }

        List expected;
        String xpath;

        xpath = "/";
        assertEquals(1, XPathUtil.steps(descriptor, xpath, namespaces).size());
        XPathUtil.Step step =
                (XPathUtil.Step) XPathUtil.steps(descriptor, xpath, namespaces).get(0);
        QName rootQName = new QName(name.getNamespaceURI(), name.getLocalPart());
        assertEquals(rootQName, step.getName());

        expected = Collections.singletonList(new XPathUtil.Step(rootQName, 1));
        xpath = "wq_plus";
        assertEquals(expected, XPathUtil.steps(descriptor, xpath, namespaces));

        expected = Collections.singletonList(new XPathUtil.Step(rootQName, 1));
        xpath = "/wq_plus";
        assertEquals(expected, XPathUtil.steps(descriptor, xpath, namespaces));

        expected = Collections.singletonList(new XPathUtil.Step(rootQName, 1));
        xpath = "wq_plus/measurement/result/../../measurement/determinand_description/../..";
        assertEquals(expected, XPathUtil.steps(descriptor, xpath, namespaces));

        expected =
                Arrays.asList(
                        new XPathUtil.Step[] {
                            new XPathUtil.Step(
                                    new QName(rootQName.getNamespaceURI(), "measurement"), 2),
                            new XPathUtil.Step(new QName(rootQName.getNamespaceURI(), "result"), 1)
                        });

        xpath = "wq_plus/measurement/result/../../measurement[2]/result";
        assertEquals(expected, XPathUtil.steps(descriptor, xpath, namespaces));

        expected =
                Arrays.asList(
                        new XPathUtil.Step[] {
                            new XPathUtil.Step(
                                    new QName(rootQName.getNamespaceURI(), "measurement"), 1),
                            new XPathUtil.Step(new QName(rootQName.getNamespaceURI(), "result"), 1)
                        });
        xpath = "wq_plus/measurement/result/../result/.";
        assertEquals(expected, XPathUtil.steps(descriptor, xpath, namespaces));

        expected =
                Arrays.asList(
                        new XPathUtil.Step[] {
                            new XPathUtil.Step(
                                    new QName(rootQName.getNamespaceURI(), "measurement"), 5)
                        });
        xpath = "measurement/result/../../measurement[5]";
        assertEquals(expected, XPathUtil.steps(descriptor, xpath, namespaces));
    }

    /**
     * Tests a location path of the form <code>"foo/bar/@baz"</code> gets built as a {@link
     * StepList} of attribute names <code>"foo/bar/baz"</code> (i.e. no distinction between what's a
     * "property" and what's an (xml) "attribute".
     */
    @Test
    public void testStepsWithXmlAttribute() throws Exception {
        FeatureType complexType =
                ComplexTestData.createExample01MultiValuedComplexProperty(
                        new UniqueNameFeatureTypeFactoryImpl());
        Name name = complexType.getName();
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(complexType, name, 0, Integer.MAX_VALUE, true, null);
        QName rootQName = new QName(name.getNamespaceURI(), name.getLocalPart());

        NamespaceSupport namespaces = new NamespaceSupport();
        namespaces.declarePrefix("wq", name.getNamespaceURI());
        namespaces.declarePrefix("xlink", XLINK.NAMESPACE);

        StepList steps =
                XPathUtil.steps(descriptor, "wq_plus/measurement[2]/@xlink:href", namespaces);
        assertNotNull(steps);
        assertEquals(steps.toString(), 2, steps.size());

        XPathUtil.Step step1 =
                new XPathUtil.Step(new QName(rootQName.getNamespaceURI(), "measurement"), 2, false);
        XPathUtil.Step step2 = new XPathUtil.Step(XLINK.HREF, 1, true);

        assertEquals(step1, steps.get(0));
        assertEquals(step2, steps.get(1));
    }

    @Test
    public void testStepEquals() {
        XPathUtil.Step step1 = new XPathUtil.Step(XLINK.FROM, 1);

        XPathUtil.Step step2 = new XPathUtil.Step(XLINK.HREF, 1, false);
        XPathUtil.Step step3 = new XPathUtil.Step(XLINK.HREF, 1, false);

        XPathUtil.Step step4 = new XPathUtil.Step(XLINK.HREF, 1, true);
        XPathUtil.Step step5 = new XPathUtil.Step(XLINK.HREF, 2, false);

        assertFalse(step1.equals(null));
        assertFalse(step1.equals(new Object()));
        assertFalse(step1.equals(step2));

        assertTrue(step2.equals(step3));
        assertFalse(step2.equals(step4));
        assertFalse(step2.equals(step5));
    }

    /** Test that the {@link StepList} for the root element is properly formed. */
    @Test
    public void testRootElementSteps() {
        NamespaceSupport namespaces = new NamespaceSupport();

        try {
            XPathUtil.rootElementSteps(null, namespaces);
            fail("passed null");
        } catch (NullPointerException e) {
        }

        FeatureType complexType =
                ComplexTestData.createExample05NoNamespaceURI(
                        new UniqueNameFeatureTypeFactoryImpl());
        Name name = complexType.getName();
        AttributeDescriptor descriptor =
                new AttributeDescriptorImpl(complexType, name, 0, Integer.MAX_VALUE, true, null);

        try {
            XPathUtil.rootElementSteps(descriptor, namespaces);
        } catch (NullPointerException e) {
            fail("failed null");
        }

        assertEquals(1, XPathUtil.rootElementSteps(descriptor, namespaces).size());
        XPathUtil.Step step = XPathUtil.rootElementSteps(descriptor, namespaces).get(0);
        QName rootQName = new QName(name.getNamespaceURI(), name.getLocalPart(), "");
        assertEquals(rootQName, step.getName());

        complexType =
                ComplexTestData.createExample01MultiValuedComplexProperty(
                        new UniqueNameFeatureTypeFactoryImpl());
        name = complexType.getName();
        descriptor =
                new AttributeDescriptorImpl(complexType, name, 0, Integer.MAX_VALUE, true, null);

        String prefix = "wq";
        namespaces.declarePrefix(prefix, name.getNamespaceURI());

        try {
            XPathUtil.rootElementSteps(descriptor, namespaces);
        } catch (NullPointerException e) {
            fail("failed null");
        }

        assertEquals(1, XPathUtil.rootElementSteps(descriptor, namespaces).size());
        step = XPathUtil.rootElementSteps(descriptor, namespaces).get(0);
        rootQName = new QName(name.getNamespaceURI(), name.getLocalPart(), prefix);
        assertEquals(rootQName, step.getName());
    }
}
