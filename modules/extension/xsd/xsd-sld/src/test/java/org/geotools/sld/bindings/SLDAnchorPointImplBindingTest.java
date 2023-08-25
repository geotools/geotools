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
package org.geotools.sld.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.geotools.filter.Filters;
import org.geotools.styling.AnchorPointImpl;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SLDAnchorPointImplBindingTest extends SLDTestSupport {

    @Test
    public void testType() throws Exception {
        assertEquals(AnchorPointImpl.class, new SLDAnchorPointBinding(styleFactory).getType());
    }

    @Test
    public void testXY() throws Exception {
        SLDMockData.anchorPoint(document, document);

        AnchorPointImpl point = (AnchorPointImpl) parse();
        assertNotNull(point);

        assertEquals(Filters.asInt(point.getAnchorPointX()), 1);
        assertEquals(Filters.asInt(point.getAnchorPointY()), 2);
    }

    @Test
    public void testNoY() throws Exception {
        Element anchorPoint = SLDMockData.anchorPoint(document, document);
        NodeList anchorPointX =
                anchorPoint.getElementsByTagNameNS(SLD.NAMESPACE, SLD.ANCHORPOINTY.getLocalPart());
        anchorPoint.removeChild(anchorPointX.item(0));

        AnchorPointImpl point = (AnchorPointImpl) parse();
        assertNotNull(point);

        assertEquals(Filters.asInt(point.getAnchorPointX()), 1);
        assertNull(point.getAnchorPointY());
    }
}
