/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util.decorate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AbstractDecoratorTest {

    public static class MyClass {}

    public static class MyOtherClass {}

    MyClass sampleClass = new MyClass();

    MyDecorator decorator;

    @Before
    public void setUp() {
        decorator = new MyDecorator(sampleClass);
    }

    public static class MyDecorator extends AbstractDecorator<MyClass> {
        public MyDecorator(MyClass delegate) {
            super(delegate);
        }
    }

    @Test
    public void testIsWrapperFor() {
        assertTrue(decorator.isWrapperFor(MyClass.class));
        assertFalse(decorator.isWrapperFor(MyOtherClass.class));
    }

    @Test
    public void testUnwrap() {
        assertTrue(decorator.unwrap(MyClass.class) == sampleClass);
    }

    @Test
    public void testUnwrapWrongClass() {
        boolean error = false;
        try {
            decorator.unwrap(MyOtherClass.class);
        } catch (Exception e) {
            error = true;
        }
        assertTrue(error);
    }
}
