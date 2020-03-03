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
import org.junit.Test;
import si.uom.NonSI;
import si.uom.SI;
import systems.uom.common.USCustomary;
import tec.uom.se.function.MultiplyConverter;
import tec.uom.se.unit.TransformedUnit;

/**
 * Test conversions using the units declared in {@link Units}.
 *
 * @author Martin Desruisseaux (Geomatys)
 */
public class UnitsTest {
    /** Compares two values for equality. */
    private static <Q extends Quantity<Q>> void checkConversion(
            final double expected,
            final Unit<Q> unitExpected,
            final double actual,
            final Unit<Q> unitActual) {
        UnitConverter converter = unitActual.getConverterTo(unitExpected);
        assertEquals(expected, converter.convert(actual), 1E-6);
        converter = converter.inverse();
        assertEquals(actual, converter.convert(expected), 1E-6);
    }

    /** Checks the conversions using {@link Units#SEXAGESIMAL_DMS}. */
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
    private static Object serialize(final Object object)
            throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(buffer);
        out.writeObject(object);
        out.close();
        final ObjectInputStream in =
                new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
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
        assertEquals(SEXAGESIMAL_DMS, serialize(SEXAGESIMAL_DMS));
        assertEquals(DEGREE_MINUTE_SECOND, serialize(DEGREE_MINUTE_SECOND));
        assertEquals(PPM, serialize(PPM));
    }

    @Test
    public void testUnitsMatch() {
        Unit<Angle> degree =
                Units.autoCorrect(
                        new TransformedUnit<Angle>(
                                SI.RADIAN, new MultiplyConverter((Math.PI * 2.0) / 360.0)));
        assertEquals("autocorrection of degree definition from jsr275", NonSI.DEGREE_ANGLE, degree);
        assertTrue("jsr275 deegree definition", isDegreeAngle(degree));

        // UNIT["degree", 0.017453292519943295],
        degree =
                Units.autoCorrect(
                        new TransformedUnit<Angle>(
                                SI.RADIAN, new MultiplyConverter(0.017453292519943295)));
        assertEquals(
                "autocorrection of deegree definition from EsriLookupTest",
                NonSI.DEGREE_ANGLE,
                degree);
        assertTrue("deegree definition from EsriLookupTest", isDegreeAngle(degree));

        Unit<Length> feet =
                Units.autoCorrect(
                        new TransformedUnit<Length>(
                                SI.METRE, new MultiplyConverter(0.3048006096012192)));
        assertEquals(
                "autocorrection of US Survey definition from EsriLookupTest",
                USCustomary.FOOT_SURVEY,
                feet);
        assertTrue("survey foot definition from EsriLookupTest", isUSSurveyFoot(feet));
    }

    private static double US_SURVEY_FOOT_FACTORY = 1200.0 / 3937.0; // 0.3048006096
    private static final double US_SURVEY_FOOT_COMPARISON_EPSILON = 1.0e-10;

    public static final boolean isUSSurveyFoot(Unit<?> unit) {
        if (unit == null) {
            return false;
        } else if (USCustomary.FOOT_SURVEY.equals(unit)) {
            return true;
        } else if (unit.getSystemUnit() == SI.METRE && unit instanceof TransformedUnit<?>) {
            @SuppressWarnings("unchecked")
            TransformedUnit<Length> transformed = (TransformedUnit<Length>) unit;
            UnitConverter converter = transformed.getConverter();
            if (converter instanceof MultiplyConverter) {
                MultiplyConverter multiplyConverter = (MultiplyConverter) converter;
                double factor = multiplyConverter.getFactor();
                // 0.3048006096012192  // observed
                // 0.3048006096        // expected
                if (Math.abs(US_SURVEY_FOOT_FACTORY - factor) < US_SURVEY_FOOT_COMPARISON_EPSILON) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final double RADIAN_TO_DEGREE_RATIO = Math.PI / 180.0; // 0.017453292519943295
    private static final double DEEGREE_RATIO_COMPARISON_EPSILON = 1.0e-15;
    /**
     * Recognize representation of NonSI.DEEGREE_ANGLE to prevent unnecessary conversion.
     *
     * @return true if MultiplyConverter is close to PI/180.0
     */
    public static final boolean isDegreeAngle(Unit<?> unit) {
        if (unit == null) {
            return false;
        } else if (NonSI.DEGREE_ANGLE.equals(unit)) {
            return true;
        }
        if (unit.getSystemUnit() == SI.RADIAN && unit instanceof TransformedUnit<?>) {
            @SuppressWarnings("unchecked")
            TransformedUnit<Angle> transformed = (TransformedUnit<Angle>) unit;
            UnitConverter converter = transformed.getConverter();
            if (converter instanceof MultiplyConverter) {
                MultiplyConverter multiplyConverter = (MultiplyConverter) converter;
                double factor = multiplyConverter.getFactor();
                if (Math.abs(RADIAN_TO_DEGREE_RATIO - factor) < DEEGREE_RATIO_COMPARISON_EPSILON) {
                    return true;
                }
            }
        }
        return false;
    }
}
