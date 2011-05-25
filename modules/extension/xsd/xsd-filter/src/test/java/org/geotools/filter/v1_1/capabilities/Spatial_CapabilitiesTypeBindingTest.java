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
import javax.xml.namespace.QName;
import org.opengis.filter.capability.SpatialCapabilities;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:Spatial_CapabilitiesType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="Spatial_CapabilitiesType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element name="GeometryOperands" type="ogc:GeometryOperandsType"/&gt;
 *          &lt;xsd:element name="SpatialOperators" type="ogc:SpatialOperatorsType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class Spatial_CapabilitiesTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(SpatialCapabilities.class, binding(OGC.Spatial_CapabilitiesType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Spatial_CapabilitiesType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.spatialCapabilities(document, document);

        SpatialCapabilities scalar = (SpatialCapabilities) parse(OGC.Spatial_CapabilitiesType);

        assertNotNull(scalar.getSpatialOperators());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.spatialCapabilities(),
                new QName(OGC.NAMESPACE, "SpatialCapabilities"), OGC.Spatial_CapabilitiesType);

        assertNotNull(getElementByQName(dom, new QName(OGC.NAMESPACE, "SpatialOperators")));
    }
}
