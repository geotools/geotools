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

import org.w3c.dom.Document;
import javax.xml.namespace.QName;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialOperators;
import org.geotools.xml.Binding;


public class Spatial_OperatorsTypeBindingTest extends FilterCapabilitiesTestSupport {
    public void testType() {
        assertEquals(SpatialOperators.class, binding(OGC.Spatial_OperatorsType).getType());
    }

    public void testExectionMode() {
        assertEquals(Binding.OVERRIDE, binding(OGC.Spatial_OperatorsType).getExecutionMode());
    }

    public void testParse() throws Exception {
        FilterMockData.spatial(document, document);

        SpatialOperators spatial = (SpatialOperators) parse(OGC.Spatial_OperatorsType);

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
        Document dom = encode(FilterMockData.spatial(),
                new QName(OGC.NAMESPACE, "Spatial_Operators"), OGC.Spatial_OperatorsType);

        assertNotNull(getElementByQName(dom, OGC.BBOX));
        assertNotNull(getElementByQName(dom, OGC.Equals));
        assertNotNull(getElementByQName(dom, OGC.Disjoint));
        assertNotNull(getElementByQName(dom, OGC.Intersect));
        assertNotNull(getElementByQName(dom, OGC.Touches));
        assertNotNull(getElementByQName(dom, OGC.Crosses));
        assertNotNull(getElementByQName(dom, OGC.Within));
        assertNotNull(getElementByQName(dom, OGC.Contains));
        assertNotNull(getElementByQName(dom, OGC.Overlaps));
        assertNotNull(getElementByQName(dom, OGC.Beyond));
        assertNotNull(getElementByQName(dom, OGC.DWithin));
    }
}
