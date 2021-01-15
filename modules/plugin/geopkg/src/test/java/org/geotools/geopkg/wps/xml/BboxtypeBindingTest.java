/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.wps.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.xsd.Binding;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

public class BboxtypeBindingTest extends GPKGTestSupport {
    @Test
    public void testType() {
        assertEquals(Envelope.class, binding(GPKG.bboxtype).getType());
    }

    @Test
    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.bboxtype).getExecutionMode());
    }

    @Test
    public void testParse() throws Exception {
        buildDocument("<bbox><minx>0</minx><maxx>100</maxx><miny>10</miny><maxy>50</maxy></bbox>");
        Object result = parse(GPKG.bboxtype);
        assertTrue(result instanceof Envelope);
        Envelope env = (Envelope) result;
        assertEquals(0.0, env.getMinX(), 0.0);
        assertEquals(100.0, env.getMaxX(), 0.0);
        assertEquals(10.0, env.getMinY(), 0.0);
        assertEquals(50.0, env.getMaxY(), 0.0);
    }
}
