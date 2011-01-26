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
package org.geotools.filter.v1_1.capabilities;

import org.w3c.dom.Document;
import org.opengis.filter.capability.FilterCapabilities;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:_Filter_Capabilities.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="_Filter_Capabilities"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="Spatial_Capabilities" type="ogc:Spatial_CapabilitiesType"/&gt;
 *          &lt;xsd:element name="Scalar_Capabilities" type="ogc:Scalar_CapabilitiesType"/&gt;
 *          &lt;xsd:element name="Id_Capabilities" type="ogc:Id_CapabilitiesType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class _Filter_CapabilitiesTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(FilterCapabilities.class, binding(OGC._Filter_Capabilities).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC._Filter_Capabilities).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.capabilities(document, document);

        FilterCapabilities caps = (FilterCapabilities) parse();

        assertEquals(FilterCapabilities.VERSION_110, caps.getVersion());
        assertNotNull(caps.getScalarCapabilities());
        assertNotNull(caps.getSpatialCapabilities());
        assertNotNull(caps.getIdCapabilities());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.capabilities(), OGC.Filter_Capabilities);

        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Spatial_Capabilities"));
        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Scalar_Capabilities"));
        assertNotNull(dom.getElementsByTagNameNS(OGC.NAMESPACE, "Id_Capabilities"));
    }
}
