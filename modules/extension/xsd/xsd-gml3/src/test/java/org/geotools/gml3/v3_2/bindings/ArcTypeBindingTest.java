/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.v3_2.bindings;

import org.geotools.geometry.jts.CircularString;
import org.geotools.gml3.bindings.GML3MockData;
import org.geotools.gml3.v3_2.GML32TestSupport;
import org.locationtech.jts.geom.LineString;

/**
 * @author Erik van de Pol
 * @source $URL$
 */
public class ArcTypeBindingTest extends GML32TestSupport {

    public void testParse() throws Exception {
        GML3MockData.arcWithPosList(document, document);
        LineString lineString = (LineString) parse();
        assertTrue(lineString instanceof CircularString);
        CircularString cs = (CircularString) lineString;

        double[] controlPoints = cs.getControlPoints();
        assertEquals(1.0, controlPoints[0]);
        assertEquals(1.0, controlPoints[1]);
        assertEquals(2.0, controlPoints[2]);
        assertEquals(2.0, controlPoints[3]);
        assertEquals(3.0, controlPoints[4]);
        assertEquals(1.0, controlPoints[5]);
    }
}
