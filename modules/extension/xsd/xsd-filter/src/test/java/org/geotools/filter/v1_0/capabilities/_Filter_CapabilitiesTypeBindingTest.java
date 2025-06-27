/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v1_0.capabilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.geotools.api.filter.capability.FilterCapabilities;
import org.geotools.xsd.Binding;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.w3c.dom.Document;

public class _Filter_CapabilitiesTypeBindingTest extends FilterCapabilitiesTestSupport {

    @Test
    public void testType() {
        assertEquals(FilterCapabilities.class, binding(OGC._Filter_Capabilities).getType());
    }

    @Test
    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC._Filter_Capabilities).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        FilterMockData.capabilities(document, document);

        // print(document);

        FilterCapabilities caps = (FilterCapabilities) parse();

        assertEquals(FilterCapabilities.VERSION_100, caps.getVersion());
        assertNotNull(caps.getScalarCapabilities());
        assertNotNull(caps.getSpatialCapabilities());
    }

    @Test
    public void testParseWithParser() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"
                + "<Filter_Capabilities version=\"1.0.0\" xmlns=\"http://www.opengis.net/ogc\">"
                + "<Scalar_Capabilities>"
                + "<Logical_Operators/>"
                + "<Comparison_Operators>"
                + "<Simple_Comparisons/>"
                + "<Like/>"
                + "<Between/>"
                + "<NullCheck/>"
                + "</Comparison_Operators>"
                + "<Arithmetic_Operators>"
                + "<Simple_Arithmetic/>"
                + "<Functions>"
                + "<Function_Names>"
                + "<Function_Name nArgs=\"2\">foo</Function_Name>"
                + "<Function_Name nArgs=\"3\">bar</Function_Name>"
                + "</Function_Names>"
                + "</Functions>"
                + "</Arithmetic_Operators>"
                + "</Scalar_Capabilities>"
                + "<Spatial_Capabilities>"
                + "<Spatial_Operators>"
                + "<BBOX/>"
                + "<Equals/>"
                + "<Disjoint/>"
                + "<Intersect/>"
                + "<Touches/>"
                + "<Contains/>"
                + "<Crosses/>"
                + "<Within/>"
                + "<Overlaps/>"
                + "<Beyond/>"
                + "<DWithin/>"
                + "</Spatial_Operators>"
                + "</Spatial_Capabilities>"
                + "</Filter_Capabilities>";

        Parser parser = new Parser(createConfiguration());
        FilterCapabilities caps =
                (FilterCapabilities) parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        assertEquals(FilterCapabilities.VERSION_100, caps.getVersion());
        assertNotNull(caps.getScalarCapabilities());
        assertNotNull(caps.getSpatialCapabilities());
    }

    @Test
    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.capabilities(), OGC.Filter_Capabilities);

        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Spatial_Capabilities"));
        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Scalar_Capabilities"));
    }
}
