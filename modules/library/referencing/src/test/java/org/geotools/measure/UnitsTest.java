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

import java.io.*;
import javax.measure.unit.Unit;
import javax.measure.quantity.Quantity;
import javax.measure.converter.UnitConverter;
import static javax.measure.unit.NonSI.DEGREE_ANGLE;
import static org.geotools.measure.Units.*;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Test conversions using the units declared in {@link Units}.
 *
 * @author Martin Desruisseaux (Geomatys)
 *
 * @source $URL$
 */
public class UnitsTest {
    /**
     * Compares two values for equality.
     */
    private static <Q extends Quantity> void checkConversion(
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
        checkConversion(10.00, DEGREE_ANGLE, 10.0000, SEXAGESIMAL_DMS);
        checkConversion(10.01, DEGREE_ANGLE, 10.0036, SEXAGESIMAL_DMS);
        checkConversion(10.50, DEGREE_ANGLE, 10.3000, SEXAGESIMAL_DMS);
        checkConversion(10.99, DEGREE_ANGLE, 10.5924, SEXAGESIMAL_DMS);
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
     *
     * @todo Disabled for now. Needs JSR-275 fix.
     */
    @Test
    @Ignore
    public void testSerialization() throws IOException, ClassNotFoundException {
        assertEquals(DEGREE_ANGLE,         serialize(DEGREE_ANGLE));
        assertEquals(SEXAGESIMAL_DMS,      serialize(SEXAGESIMAL_DMS));
        assertEquals(DEGREE_MINUTE_SECOND, serialize(DEGREE_MINUTE_SECOND));
        assertEquals(PPM,                  serialize(PPM));
    }
}
