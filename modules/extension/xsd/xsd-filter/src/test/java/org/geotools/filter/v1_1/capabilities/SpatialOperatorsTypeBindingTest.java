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

import javax.xml.namespace.QName;
import org.geotools.filter.v1_1.OGC;
import org.geotools.xsd.Binding;
import org.opengis.filter.capability.SpatialOperators;
import org.w3c.dom.Document;

/**
 * Binding test case for http://www.opengis.net/ogc:SpatialOperatorsType.
 *
 * <p>
 *
 * <pre>
 *   <code>
 *  &lt;xsd:complexType name="SpatialOperatorsType"&gt;
 *      &lt;xsd:sequence&gt;
 *          &lt;xsd:element maxOccurs="unbounded" name="SpatialOperator" type="ogc:SpatialOperatorType"/&gt;
 *      &lt;/xsd:sequence&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *    </code>
 *   </pre>
 *
 * @generated
 */
public class SpatialOperatorsTypeBindingTest extends OGCTestSupport {
    public void testType() {
        assertEquals(SpatialOperators.class, binding(OGC.SpatialOperatorsType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.SpatialOperatorsType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.spatial(document, document);

        SpatialOperators spatial = (SpatialOperators) parse(OGC.SpatialOperatorsType);

        assertNotNull(spatial.getOperator("BBOX"));
        assertNotNull(spatial.getOperator("Equals"));
        assertNotNull(spatial.getOperator("Disjoint"));
        assertNotNull(spatial.getOperator("Intersect"));
        assertNotNull(spatial.getOperator("Touches"));
        assertNotNull(spatial.getOperator("Crosses"));
        assertNotNull(spatial.getOperator("Within"));
        assertNotNull(spatial.getOperator("Contains"));
        assertNotNull(spatial.getOperator("Overlaps"));
        assertNotNull(spatial.getOperator("Beyond"));
        assertNotNull(spatial.getOperator("DWithin"));
    }

    public void testEncode() throws Exception {
        Document dom =
                encode(
                        FilterMockData.spatial(),
                        new QName(OGC.NAMESPACE, "SpatialOperators"),
                        OGC.SpatialOperatorsType);

        assertEquals(
                11,
                getElementsByQName(dom, new QName(OGC.NAMESPACE, "SpatialOperator")).getLength());
    }
}
