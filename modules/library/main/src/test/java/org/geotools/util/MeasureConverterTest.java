/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import static org.junit.Assert.assertEquals;

import javax.measure.unit.NonSI;
import javax.measure.unit.SI;

import org.geotools.measure.Measure;
import org.junit.Test;

public class MeasureConverterTest {
    

    @Test
    public void testToMeasure() {
        assertEquals(new Measure(10, SI.METER), Converters.convert("10m", Measure.class));
        assertEquals(new Measure(0.3, NonSI.FOOT), Converters.convert(".3ft", Measure.class));
        assertEquals(new Measure(3e-10, NonSI.DEGREE_ANGLE), Converters.convert("3e-10\u00B0", Measure.class));
        assertEquals(new Measure(3, null), Converters.convert("3", Measure.class));
    }
    
    @Test
    public void testMeasureToString() {
        assertEquals("10m", Converters.convert(new Measure(10, SI.METER), String.class));
        assertEquals("0.3ft", Converters.convert(new Measure(0.3, NonSI.FOOT), String.class));
        assertEquals("3E-10\u00B0", Converters.convert(new Measure(3e-10, NonSI.DEGREE_ANGLE), String.class));
    }
    
    
}
