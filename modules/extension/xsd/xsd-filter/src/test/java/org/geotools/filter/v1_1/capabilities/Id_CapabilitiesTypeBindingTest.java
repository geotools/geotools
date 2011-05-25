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
import org.opengis.filter.capability.IdCapabilities;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/ogc:Id_CapabilitiesType.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;xsd:complexType name="Id_CapabilitiesType"&gt;
 *      &lt;xsd:choice maxOccurs="unbounded"&gt;
 *          &lt;xsd:element ref="ogc:EID"/&gt;
 *          &lt;xsd:element ref="ogc:FID"/&gt;
 *      &lt;/xsd:choice&gt;
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
public class Id_CapabilitiesTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(IdCapabilities.class, binding(OGC.Id_CapabilitiesType).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Id_CapabilitiesType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.idCapabilities(document, document);

        IdCapabilities id = (IdCapabilities) parse(OGC.Id_CapabilitiesType);

        assertTrue(id.hasEID());
        assertTrue(id.hasFID());
    }

    public void testEncode() throws Exception {
        Document dom = encode(FilterMockData.idCapabilities(),
                new QName(OGC.NAMESPACE, "IdCapabilities"), OGC.Id_CapabilitiesType);

        assertNotNull(getElementsByQName(dom, OGC.FID));
        assertNotNull(getElementsByQName(dom, OGC.EID));
    }
}
