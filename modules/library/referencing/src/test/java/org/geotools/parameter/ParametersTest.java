/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.parameter;

import java.awt.geom.AffineTransform;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import org.opengis.parameter.InvalidParameterCardinalityException;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.InvalidParameterTypeException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.datum.VerticalDatumType;
import org.opengis.referencing.operation.MathTransform;

import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.wkt.Formatter;

import org.junit.*;

import static org.junit.Assert.*;


/**
 * Tests the <code>org.geotools.parameter</code> package.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class ParametersTest {
    /**
     * Tests integer and floating point values in a wide range of values. Some on those
     * values are cached (e.g. 0, 90, 360) because frequently used. It should be transparent
     * to the user. Test also unit conversions (degrees to radians in this case).
     */
    @Test
    public void testSequence() {
        for (int i=-1000; i<=1000; i++) {
            assertEquals("new (Integer, ...)", i, Parameter.create("Integer", i          ).intValue());
            assertEquals("new (Double, ...)",  i, Parameter.create("Double",  i, null    ).doubleValue(), 0.0);
            assertEquals("new (Double, ...)",  i,  Parameter.create("Double",  i, Unit.ONE).doubleValue(), 0.0);
            assertEquals("new (Double, ...)",  Math.toRadians(i),
            		 Parameter.create("Double", i, NonSI.DEGREE_ANGLE).doubleValue(SI.RADIAN), 1E-6);
        }
    }

    /**
     * Creates a parameter bounded by some range of integer numbers, and tests values
     * inside and outside this range. Tests also the uses of values of the wrong type.
     */
    @Test
    public void testRangeIntegers() {
        Parameter<Integer> param;
        param = new Parameter(DefaultParameterDescriptor.create("Range", 15, -30, +40));
        assertEquals(   "intValue", 15, param.intValue());
        assertEquals("doubleValue", 15, param.doubleValue(), 0.0);
        param.setValue(12);
        assertEquals(   "intValue", 12, param.intValue());
        assertEquals("doubleValue", 12, param.doubleValue(), 0.0);
        try {
            param.setValue(50);
            fail("setValue(> max)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Range", exception.getParameterName());
        }
        try {
            param.setValue(-40);
            fail("setValue(< min)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Range", exception.getParameterName());
        }
        try {
            param.setValue(10.0);
            fail("setValue(double)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Range", exception.getParameterName());
        }
        assertEquals("Clone not equals: ", param, param.clone());
    }

    /**
     * Creates a parameter bounded by some range of floating point numbers, and tests values
     * inside and outside this range. Tests also the uses of values of the wrong types.
     */
    @Test
    public void testRangeDoubles() {
        Parameter<Double> param;
        param = new Parameter(DefaultParameterDescriptor.create("Range", 15.0, -30.0, +40.0, null));
        assertEquals(   "intValue", 15, param.intValue());
        assertEquals("doubleValue", 15, param.doubleValue(), 0.0);
        param.setValue(12.0);
        assertEquals(   "intValue", 12, param.intValue());
        assertEquals("doubleValue", 12, param.doubleValue(), 0.0);
        try {
            param.setValue(50.0);
            fail("setValue(> max)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Range", exception.getParameterName());
        }
        try {
            param.setValue(-40.0);
            fail("setValue(< min)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Range", exception.getParameterName());
        }
        try {
            param.setValue("12");
            fail("setValue(String)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Range", exception.getParameterName());
        }
        assertEquals("equals(clone)", param, param.clone());
    }

    /**
     * Tests parameter for a code list. Try to inserts invalid values. Try also to insert a
     * new code list. This operation should fails if the new code list is created after the
     * parameter.
     */
    @Test
    public void testCodeList() {
        Parameter<AxisDirection> param = Parameter.create("Test", AxisDirection.class,AxisDirection.DISPLAY_UP);
        ParameterDescriptor op = param.getDescriptor();
        assertEquals("Set<AxisDirection>",
                     new HashSet<AxisDirection>(Arrays.asList(AxisDirection.values())),
                     op.getValidValues());
        assertNull("defaultValue", op.getDefaultValue());
        param.setValue(AxisDirection.DOWN);
        try {
            param.setValue(VerticalDatumType.ELLIPSOIDAL);
            fail("setValue(VerticalDatumType)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        AxisDirection dummy = AxisDirection.valueOf("Dummy");
        try {
            param.setValue(dummy);
            fail("setValue(AxisDirection)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        param = Parameter.create("Test",AxisDirection.class, AxisDirection.DISPLAY_UP);
        param.setValue(dummy); // Should not fails.
        assertEquals("equals(clone)", param, param.clone());
    }

    /**
     * Test {@link DefaultParameterDescriptor} construction.
     */
    @Test
    public void testParameterDescriptor() {
        ParameterDescriptor<Double> dDescriptor;
        ParameterDescriptor<Integer> iDescriptor;
        ParameterValue<Double>      parameter;

        dDescriptor = DefaultParameterDescriptor.create("Test", 12, 4, 20, SI.METER);
        parameter  = dDescriptor.createValue();
        assertEquals("name",         "Test",       dDescriptor.getName().getCode());
        assertEquals("unit",         SI.METER,     dDescriptor.getUnit());
        assertEquals("class",        Double.class, dDescriptor.getValueClass());
        assertEquals("defaultValue", 12.0,         dDescriptor.getDefaultValue().doubleValue(), 0.0);
        assertEquals("minimum",       4.0,         dDescriptor.getMinimumValue());
        assertEquals("maximum",      20.0,         dDescriptor.getMaximumValue());
        assertEquals("value",        12,           parameter.intValue());
        assertEquals("unit",         SI.METER,     parameter.getUnit());
        for (int i=4; i<=20; i++) {
            parameter.setValue(i);
            assertEquals("value", Double.valueOf(i), parameter.getValue());
            assertEquals("unit",  SI.METER,          parameter.getUnit());
            assertEquals("value", i,                 parameter.doubleValue(SI.METER), 0);
        }
        try {
            parameter.setValue(3.0);
            fail("setValue(< min)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        try {
            parameter.setValue("12");
            fail("setValue(Sring)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        for (int i=400; i<=2000; i+=100) {
            parameter.setValue(i, SI.CENTI(SI.METER));
            assertEquals("value", Double.valueOf(i),  parameter.getValue());
            assertEquals("unit",  SI.CENTI(SI.METER), parameter.getUnit());
            assertEquals("value", i/100,              parameter.doubleValue(SI.METER), 0);
        }
        try {
        	iDescriptor = DefaultParameterDescriptor.create("Test", 3, 4, 20);
            fail("setValue(< min)");
        } catch (InvalidParameterValueException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        try {
        	iDescriptor = DefaultParameterDescriptor.create("Test", 12, 20, 4);
            fail("ParameterDescriptor(min > max)");
        } catch (IllegalArgumentException exception) {
            // This is the expected exception.
        }
    }

    /**
     * Test {@link Parameter} construction.
     */
    @Test
    public void testParameterValue() throws IOException, ClassNotFoundException {
        Parameter<?>           parameter;
        ParameterDescriptor<?> descriptor;
        Set<?>                 validValues;

        parameter  = Parameter.create("Test", 14);
        descriptor = parameter.getDescriptor();
        assertNull  ("unit",                         parameter.getUnit());
        assertEquals("intValue",     14,             parameter.intValue());
        assertEquals("doubleValue",  14,             parameter.doubleValue(), 0);
        assertEquals("type",         Integer.class, descriptor.getValueClass());
        assertEquals("name",         "Test",        descriptor.getName().getCode());
        assertEquals("defaultValue", 0,             descriptor.getDefaultValue());
        assertNull  ("minimum",                     descriptor.getMinimumValue());
        assertNull  ("maximum",                     descriptor.getMaximumValue());
        assertNull  ("unit",                        descriptor.getUnit());
        assertNull  ("validValues",                 descriptor.getValidValues());
        try {
            parameter.doubleValue(SI.METER);
            fail("doubleValue(METER)");
        } catch (IllegalStateException exception) {
            // This is the expected exception.
        }
        try {
            parameter.stringValue();
            fail("stringValue()");
        } catch (InvalidParameterTypeException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        serialize(parameter);

        parameter  = Parameter.create("Test", 3, SI.METER);
        descriptor = (ParameterDescriptor)       parameter.getDescriptor();
        assertEquals("intValue",       3,        parameter.intValue());
        assertEquals("doubleValue",    3,        parameter.doubleValue(), 0);
        assertEquals("doubleValue",  300,        parameter.doubleValue(SI.CENTI(SI.METER)), 0);
        assertEquals("name",         "Test",    descriptor.getName().getCode());
        assertEquals("unit",         SI.METER,  descriptor.getUnit());
        assertNull  ("defaultValue",            descriptor.getDefaultValue());
        assertNull  ("minimum",                 descriptor.getMinimumValue());
        assertNull  ("maximum",                 descriptor.getMaximumValue());
        assertNull  ("validValues",             descriptor.getValidValues());
        try {
            parameter.stringValue();
            fail("stringValue()");
        } catch (InvalidParameterTypeException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        serialize(parameter);

        parameter   = Parameter.create("Test",AxisDirection.class, AxisDirection.NORTH);
        descriptor  = (ParameterDescriptor) parameter.getDescriptor();
        validValues = descriptor.getValidValues();
        assertEquals("value",  AxisDirection.NORTH, parameter.getValue());
        assertEquals("name",   "Test",             descriptor.getName().getCode());
        assertNull  ("unit",                       descriptor.getUnit());
        assertNull  ("defaultValue",               descriptor.getDefaultValue());
        assertNull  ("minimum",                    descriptor.getMinimumValue());
        assertNull  ("maximum",                    descriptor.getMaximumValue());
        assertTrue  ("validValues", validValues.contains(AxisDirection.NORTH));
        assertTrue  ("validValues", validValues.contains(AxisDirection.SOUTH));
        assertTrue  ("validValues", validValues.contains(AxisDirection.DISPLAY_LEFT));
        assertTrue  ("validValues", validValues.contains(AxisDirection.PAST));
        assertEquals("validValues", new HashSet<AxisDirection>(Arrays.asList(AxisDirection.values())), validValues);
        try {
            parameter.doubleValue();
            fail("doubleValue should not be allowed on AxisDirection");
        } catch (InvalidParameterTypeException exception) {
            // This is the expected exception.
            assertEquals("Test", exception.getParameterName());
        }
        serialize(parameter);
    }

    /**
     * Test parameter values group.
     */
    @Test
    @SuppressWarnings("serial")
    public void testGroup() throws IOException {
        final ParameterWriter writer = new ParameterWriter(new StringWriter());
        final Integer ONE = 1;
        final ParameterDescriptor<Integer> p1, p2, p3, p4;
        p1 = new DefaultParameterDescriptor<Integer>(Collections.singletonMap("name", "1"), Integer.class, null, ONE, null, null, null, true);
        p2 = new DefaultParameterDescriptor<Integer>(Collections.singletonMap("name", "2"), Integer.class, null, ONE, null, null, null, true);
        p3 = new DefaultParameterDescriptor<Integer>(Collections.singletonMap("name", "3"), Integer.class, null, ONE, null, null, null, false);
        p4 = new DefaultParameterDescriptor<Integer>(Collections.singletonMap("name", "4"), Integer.class, null, ONE, null, null, null, false) {
            /**
             * We are cheating here: <code>maximumOccurs</code> should always be 1 for
             * <code>ParameterValue</code>. However, the Geotools implementation should
             * be robust enough to accept other values. We will test that.
             */
            @Override
            public int getMaximumOccurs() {
                return 2;
            }
        };

        final Parameter v1, v2, v3, v4, v1b, v2b, v3b, v4b;
        v1  = new Parameter<Integer>(p1); v1 .setValue( 10);
        v2  = new Parameter<Integer>(p2); v2 .setValue( 20);
        v3  = new Parameter<Integer>(p3); v3 .setValue( 30);
        v4  = new Parameter<Integer>(p4); v4 .setValue( 40);
        v1b = new Parameter<Integer>(p1); v1b.setValue(-10);
        v2b = new Parameter<Integer>(p2); v2b.setValue(-20);
        v3b = new Parameter<Integer>(p3); v3b.setValue(-30);
        v4b = new Parameter<Integer>(p4); v4b.setValue(-40);

        ParameterDescriptorGroup descriptor;
        ParameterGroup           group;
        Collection               content;
        Map<String,?>            properties;
        Parameter                automatic;

        /* --------------------------------------------- *
         * Case (v1, v2, v3) where:
         *    - v1   is mandatory
         *    - v2   is mandatory
         *    - v3   is optional
         * --------------------------------------------- */
        properties = Collections.singletonMap("name", "group");
        group      = new ParameterGroup(properties, new Parameter[] {v1, v2, v3});
        descriptor = group.getDescriptor();
        content    = descriptor.descriptors();
        writer.format(group); // Ensure there is no exception there.
        assertEquals("name", "group", descriptor.getName().getCode());
        assertEquals("descriptors", 3, content.size());
        assertTrue  ("contains(p1)",  content.contains(p1));
        assertTrue  ("contains(p2)",  content.contains(p2));
        assertTrue  ("contains(p3)",  content.contains(p3));
        assertFalse ("contains(p4)",  content.contains(p4));
        assertSame  ("descriptor(\"1\")",  p1, descriptor.descriptor("1"));
        assertSame  ("descriptor(\"2\")",  p2, descriptor.descriptor("2"));
        assertSame  ("descriptor(\"3\")",  p3, descriptor.descriptor("3"));

        // Checks default values
        content = group.values();
        assertEquals("values.size()",  3, content.size());
        assertTrue  ("contains(v1)",      content.contains(v1 ));
        assertTrue  ("contains(v2)",      content.contains(v2 ));
        assertTrue  ("contains(v3)",      content.contains(v3 ));
        assertFalse ("contains(v4)",      content.contains(v4 ));
        assertFalse ("contains(v1b)",     content.contains(v1b));
        assertFalse ("contains(v2b)",     content.contains(v2b));
        assertFalse ("contains(v3b)",     content.contains(v3b));
        assertSame  ("parameter(\"1\")",  v1, group.parameter("1"));
        assertSame  ("parameter(\"2\")",  v2, group.parameter("2"));
        assertSame  ("parameter(\"3\")",  v3, group.parameter("3"));
        assertEquals("parameter(\"1\")",  10, group.parameter("1").intValue());
        assertEquals("parameter(\"2\")",  20, group.parameter("2").intValue());
        assertEquals("parameter(\"3\")",  30, group.parameter("3").intValue());

        // Tests the replacement of some values
        assertFalse("remove(v1b)", content.remove(v1b));
        try {
            assertTrue(content.remove(v1));
            fail("v1 is a mandatory parameter; it should not be removeable.");
        } catch (InvalidParameterCardinalityException e) {
            // This is the expected exception.
            assertEquals("1", e.getParameterName());
            assertNotNull(e.getMessage());
        }
        try {
            assertTrue(content.add(v4));
            fail("v4 is not a parameter for this group.");
        } catch (InvalidParameterNameException e) {
            // This is the expected exception.
            assertEquals("4", e.getParameterName());
            assertNotNull(e.getMessage());
        }
        assertTrue  ("add(v1b)", content.add(v1b));
        assertTrue  ("add(v2b)", content.add(v2b));
        assertTrue  ("add(v3b)", content.add(v3b));
        assertFalse ("add(v1b)", content.add(v1b)); // Already present
        assertFalse ("add(v2b)", content.add(v2b)); // Already present
        assertFalse ("add(v3b)", content.add(v3b)); // Already present
        assertEquals("parameter(\"1b\")", -10, group.parameter("1").intValue());
        assertEquals("parameter(\"2b\")", -20, group.parameter("2").intValue());
        assertEquals("parameter(\"3b\")", -30, group.parameter("3").intValue());
        assertEquals("values.size()", 3, content.size());

        // Tests equality
        assertEquals("new", group, group=new ParameterGroup(descriptor, new Parameter[] {v1b, v2b, v3b}));

        /* --------------------------------------------- *
         * Case (v1, v2) where:
         *    - v1   is mandatory
         *    - v2   is mandatory
         *    - v3   is optional and initially omitted
         * --------------------------------------------- */
        group      = new ParameterGroup(descriptor, new Parameter[] {v1, v2});
        descriptor = group.getDescriptor();
        content    = group.values();
        automatic  = (Parameter) v3.getDescriptor().createValue();
        writer.format(group); // Ensure there is no exception there.
        assertEquals   ("values.size()", 2, content.size());
        assertTrue     ("contains(v1)",     content.contains(v1 ));
        assertTrue     ("contains(v2)",     content.contains(v2 ));
        assertFalse    ("contains(v3)",     content.contains(v3 ));
        assertFalse    ("contains(v4)",     content.contains(v4 ));
        assertFalse    ("contains(v1b)",    content.contains(v1b));
        assertFalse    ("contains(v2b)",    content.contains(v2b));
        assertFalse    ("contains(v3b)",    content.contains(v3b));
        assertSame     ("parameter(\"1\")", v1, group.parameter ("1"));
        assertSame     ("parameter(\"2\")", v2, group.parameter ("2"));
        assertFalse    ("contains(automatic)",  content.contains(automatic));
        assertNotEquals("parameter(\"3\")", v3, group.parameter ("3")); // Should have automatically created.
        assertTrue     ("contains(automatic)",  content.contains(automatic));
        try {
            assertNotNull(group.parameter("4"));
            fail("v4 parameter should not be allowed in this group.");
        } catch (ParameterNotFoundException e) {
            // This is the expected exception.
            assertEquals("4", e.getParameterName());
            assertNotNull(e.getMessage());
        }

        // Tests the replacement of some values
        assertFalse("remove(v1b)",  content.remove(v1b));       assertEquals("values.size()", 3, content.size());
        assertFalse("remove(v3)",   content.remove(v3));        assertEquals("values.size()", 3, content.size());
        assertTrue ("remove(auto)", content.remove(automatic)); assertEquals("values.size()", 2, content.size());
        try {
            assertTrue(content.remove(v1));
            fail("v1 is a mandatory parameter; it should not be removeable.");
        } catch (InvalidParameterCardinalityException e) {
            // This is the expected exception.
            assertEquals("1", e.getParameterName());
            assertNotNull(e.getMessage());
        }

        assertEquals("values.size()", 2, content.size());
        assertTrue  ("add(v1b)", content.add(v1b));
        assertTrue  ("add(v2b)", content.add(v2b));
        assertTrue  ("add(v3b)", content.add(v3b));
        assertFalse ("add(v1b)", content.add(v1b)); // Already present
        assertFalse ("add(v2b)", content.add(v2b)); // Already present
        assertFalse ("add(v3b)", content.add(v3b)); // Already present
        assertEquals("parameter(\"1b\")", -10, group.parameter("1").intValue());
        assertEquals("parameter(\"2b\")", -20, group.parameter("2").intValue());
        assertEquals("parameter(\"3b\")", -30, group.parameter("3").intValue());
        assertEquals("values.size()", 3, content.size());

        /* --------------------------------------------- *
         * Case (v1, v4, v3, v4b) where:
         *    - v1   is mandatory
         *    - v3   is optional
         *    - v4   is optional and can be included twice.
         * --------------------------------------------- */
        try {
            group = new ParameterGroup(properties, new Parameter[] {v1, v3, v4, v3b});
            fail("Adding two 'v3' value should not be allowed");
        } catch (InvalidParameterCardinalityException e) {
            // This is the expected exception.
            assertEquals("3", e.getParameterName());
            assertNotNull(e.getMessage());
        }
        group      = new ParameterGroup(properties, new Parameter[] {v1, v4, v3, v4b});
        descriptor = group.getDescriptor();
        content    = group.values();
        automatic  = (Parameter) v3.getDescriptor().createValue();
        writer.format(group); // Ensure there is no exception there.
        assertEquals   ("values.size()", 4, content.size());
        assertTrue     ("contains(v1)",     content.contains(v1 ));
        assertFalse    ("contains(v2)",     content.contains(v2 ));
        assertTrue     ("contains(v3)",     content.contains(v3 ));
        assertTrue     ("contains(v4)",     content.contains(v4 ));
        assertFalse    ("contains(v1b)",    content.contains(v1b));
        assertFalse    ("contains(v2b)",    content.contains(v2b));
        assertFalse    ("contains(v3b)",    content.contains(v3b));
        assertTrue     ("contains(v4b)",    content.contains(v4b));
        assertSame     ("parameter(\"1\")", v1, group.parameter ("1"));
        assertSame     ("parameter(\"3\")", v3, group.parameter ("3"));
        assertSame     ("parameter(\"4\")", v4, group.parameter ("4"));
        assertTrue     ("remove(v3)",       content.remove(v3));
        assertFalse    ("contains(automatic)", content.contains(automatic));
        assertNotEquals("parameter(\"3\")", v3, group.parameter ("3")); // Should have automatically created.
        assertTrue     ("contains(automatic)", content.contains(automatic));

        try {
            new ParameterGroup(descriptor, new Parameter[] {v4, v3});
            fail("Parameter 1 was mandatory.");
        } catch (InvalidParameterCardinalityException exception) {
            // This is the expected exception.
            assertEquals("1", exception.getParameterName());
        }
        try {
            new ParameterGroup(descriptor, new Parameter[] {v1, v4, v3, v3b});
            fail("Parameter 3 was not allowed to be inserted twice.");
        } catch (InvalidParameterCardinalityException exception) {
            // This is the expected exception.
            assertEquals("3", exception.getParameterName());
        }
        try {
            new ParameterGroup(descriptor, new Parameter[] {v1, v3, v1b});
            fail("Parameter 1 was not allowed to be inserted twice.");
        } catch (InvalidParameterCardinalityException exception) {
            // This is the expected exception.
            assertEquals("1", exception.getParameterName());
        }

        /* --------------------------------------------- *
         * Case (v1, v2) where:
         *    - v1   is mandatory
         *    - v2   is mandatory
         * --------------------------------------------- */
        group      = new ParameterGroup(properties, new Parameter[] {v1, v2});
        descriptor = group.getDescriptor();
        content    = descriptor.descriptors();
        writer.format(group); // Ensure there is no exception there.
        assertEquals("name", "group", descriptor.getName().getCode());
        assertEquals("descriptors.size()", 2, content.size());
        assertTrue  ("contains(p1)",          content.contains(p1));
        assertTrue  ("contains(p2)",          content.contains(p2));
        assertFalse ("contains(p3)",          content.contains(p3));
        assertSame  ("descriptor(\"1\")", p1, descriptor.descriptor("1"));
        assertSame  ("descriptor(\"2\")", p2, descriptor.descriptor("2"));
        try {
            assertSame("p3", p3, descriptor.descriptor("3"));
            fail("p3 should not exists.");
        } catch (ParameterNotFoundException e) {
            // This is the expected exception
            assertEquals("3", e.getParameterName());
        }

        content = group.values();
        assertEquals("values.size()", 2, content.size());
        assertTrue  ("contains(v1)",     content.contains(v1 ));
        assertTrue  ("contains(v2)",     content.contains(v2 ));
        assertFalse ("contains(v3)",     content.contains(v3 ));
        assertFalse ("contains(v1b)",    content.contains(v1b));
        assertFalse ("contains(v2b)",    content.contains(v2b));
        assertFalse ("contains(v3b)",    content.contains(v3b));
        assertSame  ("parameter(\"1\")", v1, group.parameter("1"));
        assertSame  ("parameter(\"2\")", v2, group.parameter("2"));
        try {
            assertSame("parameter(\"3\")", v3, group.parameter("3"));
            fail("v3 should not exists");
        } catch (ParameterNotFoundException e) {
            // This is the expected exception
            assertEquals("3", e.getParameterName());
        }

        /* --------------------------------------------- *
         * Case (v1, v3) where:
         *    - v1   is mandatory
         *    - v3   is optional
         * --------------------------------------------- */
        group      = new ParameterGroup(properties, new Parameter[] {v1, v3});
        descriptor = group.getDescriptor();
        content    = descriptor.descriptors();
        writer.format(group); // Ensure there is no exception there.
        assertEquals("name", "group", descriptor.getName().getCode());
        assertEquals("descriptors.size()", 2, content.size());
        assertTrue  ("contains(p1)",       content.contains(p1));
        assertFalse ("contains(p2)",       content.contains(p2));
        assertTrue  ("contains(p3)",       content.contains(p3));
        assertSame  ("descriptor(\"1\")",  p1, descriptor.descriptor("1"));
        assertSame  ("descriptor(\"3\")",  p3, descriptor.descriptor("3"));
        try {
            assertSame("descriptor(\"2\")", p2, descriptor.descriptor("2"));
            fail("p2 should not exists");
        } catch (ParameterNotFoundException e) {
            // This is the expected exception
            assertEquals("2", e.getParameterName());
        }

        content = group.values();
        assertEquals("values.size()", 2, content.size());
        assertTrue  ("contains(v1)",  content.contains(v1 ));
        assertFalse ("contains(v2)",  content.contains(v2 ));
        assertTrue  ("contains(v3)",  content.contains(v3 ));
        assertFalse ("contains(v1b)", content.contains(v1b));
        assertFalse ("contains(v2b)", content.contains(v2b));
        assertFalse ("contains(v3b)", content.contains(v3b));
        assertSame  ("parameter(\"1\")", v1, group.parameter("1"));
        assertSame  ("parameter(\"3\")", v3, group.parameter("3"));
        try {
            assertSame("parameter(\"2\")", v2, group.parameter("2"));
            fail("v2 should not exists");
        } catch (ParameterNotFoundException e) {
            // This is the expected exception
            assertEquals("2", e.getParameterName());
        }

        /* --------------------------------------------- *
         * Construction tests
         * --------------------------------------------- */
        group = new ParameterGroup(properties, new Parameter[] {v1, v2, v3, v4, v4b});
        writer.format(group); // Ensure there is no exception there.
        assertEquals("values.size()", 5, group.values().size());
        try {
            new ParameterGroup(properties, new Parameter[] {v1, v2, v3, v3b});
            fail("Parameter 3 was not allowed to be inserted twice.");
        } catch (InvalidParameterCardinalityException e) {
            // This is the expected exception.
            assertEquals("3", e.getParameterName());
        }
        try {
            new ParameterGroup(properties, new Parameter[] {v1, v3, v1b});
            fail("Parameter 1 was not allowed to be inserted twice.");
        } catch (InvalidParameterCardinalityException e) {
            // This is the expected exception.
            assertEquals("1", e.getParameterName());
        }
    }

    /**
     * Test WKT formatting of transforms backed by matrix.
     */
    @Test
    public void testMatrix() {
        final Formatter  formatter = new Formatter();
        final GeneralMatrix matrix = new GeneralMatrix(4);
        matrix.setElement(0,2,  4);
        matrix.setElement(1,0, -2);
        matrix.setElement(2,3,  7);
        MathTransform transform = ProjectiveTransform.create(matrix);
        assertFalse(transform instanceof AffineTransform);
        formatter.append(transform);
        assertEquals("PARAM_MT[\"Affine\", "          +
                     "PARAMETER[\"num_row\", 4], "    +
                     "PARAMETER[\"num_col\", 4], "    +
                     "PARAMETER[\"elt_0_2\", 4.0], "  +
                     "PARAMETER[\"elt_1_0\", -2.0], " +
                     "PARAMETER[\"elt_2_3\", 7.0]]", formatter.toString());
        matrix.setSize(3,3);
        transform = ProjectiveTransform.create(matrix);
        assertTrue(transform instanceof AffineTransform);
        formatter.clear();
        formatter.append(transform);
        assertEquals("PARAM_MT[\"Affine\", "          +
                     "PARAMETER[\"num_row\", 3], "    +
                     "PARAMETER[\"num_col\", 3], "    +
                     "PARAMETER[\"elt_0_2\", 4.0], "  +
                     "PARAMETER[\"elt_1_0\", -2.0]]", formatter.toString());
    }

    /**
     * Tests the storage of matrix parameters.
     */
    @Test
    public void testMatrixEdit() {
        final int size = 8;
        final Random random = new Random(47821365);
        final GeneralMatrix matrix = new GeneralMatrix(size);
        for (int j=0; j<size; j++) {
            for (int i=0; i<size; i++) {
                matrix.setElement(j, i, 200*random.nextDouble()-100);
            }
        }
        final MatrixParameterDescriptors descriptor =
                new MatrixParameterDescriptors(Collections.singletonMap("name", "Test"));
        for (int height=2; height<=size; height++) {
            for (int width=2; width<=size; width++) {
                MatrixParameters parameters = (MatrixParameters) descriptor.createValue();
                GeneralMatrix copy = matrix.clone();
                copy.setSize(height, width);
                parameters.setMatrix(copy);
                assertEquals("height", height, ((Parameter) parameters.parameter("num_row")).intValue());
                assertEquals("width",  width,  ((Parameter) parameters.parameter("num_col")).intValue());
                assertTrue  ("equals", copy.equals(parameters.getMatrix(), 0));
                assertEquals("equals", parameters, parameters.clone());
            }
        }
    }

    /**
     * Test the serialization of the given object.
     */
    private static void serialize(final Object object) throws IOException, ClassNotFoundException {
        final ByteArrayOutputStream out  = new ByteArrayOutputStream();
        final ObjectOutputStream    outs = new ObjectOutputStream(out);
        outs.writeObject(object);
        outs.close();

        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        final Object test = in.readObject();
        in.close();

        assertNotSame("Serialization", object, test);
        assertEquals ("Serialization", object, test);
        assertEquals ("Serialization", object.hashCode(), test.hashCode());
    }

    /**
     * Ensure that the specified objects are not equals.
     */
    private static void assertNotEquals(final String message, final Object o1, final Object o2) {
        assertNotNull(message, o1);
        assertNotNull(message, o2);
        assertNotSame(message, o1, o2);
        assertFalse  (message, o1.equals(o2));
    }
}
