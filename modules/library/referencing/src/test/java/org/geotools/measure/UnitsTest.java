/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import static org.geotools.measure.Units.DEGREE_MINUTE_SECOND;
import static org.geotools.measure.Units.PPM;
import static org.geotools.measure.Units.SEXAGESIMAL_DMS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static tec.uom.se.unit.Units.RADIAN;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;

import org.junit.Ignore;
import org.junit.Test;

import si.uom.NonSI;
import si.uom.SI;
import tec.uom.se.function.MultiplyConverter;
import tec.uom.se.function.PiMultiplierConverter;
import tec.uom.se.function.RationalConverter;
import tec.uom.se.unit.TransformedUnit;


/**
 * Test conversions using the units declared in {@link Units}.
 *
 * @author Martin Desruisseaux (Geomatys)
 *
 *
 *
 * @source $URL$
 */
public class UnitsTest {
    /**
     * Compares two values for equality.
     */
    private static <Q extends Quantity<Q>> void checkConversion(
            final double expected, final Unit<Q> unitExpected,
            final double actual,   final Unit<Q> unitActual)
    {
        UnitConverter converter = unitActual.getConverterTo(unitExpected);
        assertEquals(expected, converter.convert(actual), 1E-6);
        converter = converter.inverse();
        assertEquals(actual, converter.convert(expected), 1E-6);
    }

    /**
     * Checks the conversions using {@link Units#SEXAGESIMAL_DMS}.
     */
    @Test
    public void testSexagesimal() {
        checkConversion(10.00, NonSI.DEGREE_ANGLE, 10.0000, SEXAGESIMAL_DMS);
        checkConversion(10.01, NonSI.DEGREE_ANGLE, 10.0036, SEXAGESIMAL_DMS);
        checkConversion(10.50, NonSI.DEGREE_ANGLE, 10.3000, SEXAGESIMAL_DMS);
        checkConversion(10.99, NonSI.DEGREE_ANGLE, 10.5924, SEXAGESIMAL_DMS);
    }

    /**
     * Serialize and deserialize the given object.
     *
     * @param object The object to serialize.
     * @return The deserialized object.
     * @throws IOException Should never occurs.
     * @throws ClassNotFoundException Should never occurs.
     */
    private static Object serialize(final Object object) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(object);
        out.close();
        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        final Object read = in.readObject();
        in.close();
        return read;
    }

    /**
     * Tests serialization of units.
     *
     * @throws IOException Should never occurs.
     * @throws ClassNotFoundException Should never occurs.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        assertEquals(NonSI.DEGREE_ANGLE, serialize(NonSI.DEGREE_ANGLE));
        assertEquals(SEXAGESIMAL_DMS,      serialize(SEXAGESIMAL_DMS));
        assertEquals(DEGREE_MINUTE_SECOND, serialize(DEGREE_MINUTE_SECOND));
        assertEquals(PPM,                  serialize(PPM));
    }
    
    @Test
    public void testUnitsMatch() {
        Unit<Angle> degree = new TransformedUnit<Angle>(SI.RADIAN,
                new MultiplyConverter((Math.PI * 2.0) / 360.0));
        assertTrue("jsr275 deegree definition", Units.isDegreeAngle(degree));

        // UNIT["degree", 0.017453292519943295],
        degree = new TransformedUnit<Angle>(SI.RADIAN, new MultiplyConverter(0.017453292519943295));
        assertTrue("deegree definition from EsriLookupTest", Units.isDegreeAngle(degree));

        Unit<Length> feet = new TransformedUnit<Length>(SI.METRE,
                new MultiplyConverter(0.3048006096012192));
        assertTrue("survey foot definition from EsriLookupTest", Units.isUSSurveyFoot(feet));
    }
}
