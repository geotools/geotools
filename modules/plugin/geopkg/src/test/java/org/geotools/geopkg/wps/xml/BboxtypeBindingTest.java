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

import org.geotools.xml.Binding;
import org.locationtech.jts.geom.Envelope;

/**
 * Binding test case for http://www.opengis.net/gpkg:bboxtype.
 *
 * <p>
 *
 * <pre>
 *   <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="bboxtype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element name="minx" type="xs:decimal"/&gt;
 *        &lt;xs:element name="miny" type="xs:decimal"/&gt;
 *        &lt;xs:element name="maxx" type="xs:decimal"/&gt;
 *        &lt;xs:element name="maxy" type="xs:decimal"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt;
 *
 *    </code>
 *   </pre>
 *
 * @generated
 */
public class BboxtypeBindingTest extends GPKGTestSupport {

    public void testType() {
        assertEquals(Envelope.class, binding(GPKG.bboxtype).getType());
    }

    public void testExecutionMode() {
        assertEquals(Binding.OVERRIDE, binding(GPKG.bboxtype).getExecutionMode());
    }

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
