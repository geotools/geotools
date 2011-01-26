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

import org.geotools.filter.Filters;
import org.geotools.styling.PointPlacement;


public class SLDPointPlacementBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(PointPlacement.class, new SLDPointPlacementBinding(null).getType());
    }

    public void test() throws Exception {
        SLDMockData.pointPlacement(document, document);

        PointPlacement pp = (PointPlacement) parse();
        assertNotNull(pp);

        assertEquals(1, Filters.asInt(pp.getAnchorPoint().getAnchorPointX()));
        assertEquals(2, Filters.asInt(pp.getAnchorPoint().getAnchorPointY()));

        assertEquals(1, Filters.asInt(pp.getDisplacement().getDisplacementX()));
        assertEquals(2, Filters.asInt(pp.getDisplacement().getDisplacementY()));

        assertEquals(90d, Filters.asDouble(pp.getRotation()), 0d);
    }
}
