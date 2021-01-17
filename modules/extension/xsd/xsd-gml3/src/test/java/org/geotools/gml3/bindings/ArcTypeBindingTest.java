/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.geometry.jts.CircularString;
import org.geotools.gml3.GML3TestSupport;
import org.junit.Test;
import org.locationtech.jts.geom.LineString;

/** @author Erik van de Pol */
public class ArcTypeBindingTest extends GML3TestSupport {

    @Override
    protected boolean enableExtendedArcSurfaceSupport() {
        return true;
    }

    @Test
    public void testParse() throws Exception {
        GML3MockData.arcWithPosList(document, document);
        LineString lineString = (LineString) parse();
        assertTrue(lineString instanceof CircularString);
        CircularString cs = (CircularString) lineString;

        double[] controlPoints = cs.getControlPoints();
        assertEquals(1.0, controlPoints[0], 0d);
        assertEquals(1.0, controlPoints[1], 0d);
        assertEquals(2.0, controlPoints[2], 0d);
        assertEquals(2.0, controlPoints[3], 0d);
        assertEquals(3.0, controlPoints[4], 0d);
        assertEquals(1.0, controlPoints[5], 0d);
    }
}
