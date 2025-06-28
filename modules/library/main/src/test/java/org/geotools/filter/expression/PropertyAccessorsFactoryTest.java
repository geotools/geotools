/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.expression;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.util.factory.Hints;
import org.junit.Test;

public class PropertyAccessorsFactoryTest {

    /** Test to check that priority of {@link PropertyAccessorFactory} instances is respected. */
    @Test
    public void testPropertyAccessorsFactoryPriority() {
        List<PropertyAccessor> propertyAccessors =
                PropertyAccessors.findPropertyAccessors(new PriorityMockDataObject(), "", null, null);
        assertEquals(2, propertyAccessors.size());
        assertTrue(propertyAccessors.get(0) instanceof HighPriorityMockPropertyAccessor);
        assertTrue(propertyAccessors.get(1) instanceof LowPriorityMockPropertyAccessor);
    }

    // Below are stubs used for this test
    public static class LowPriorityMockPropertyAccessorFactory implements PropertyAccessorFactory {

        @Override
        public int getPriority() {
            return PropertyAccessorFactory.LOWEST_PRIORITY;
        }

        @Override
        public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
            if (!PriorityMockDataObject.class.equals(type)) {
                return null;
            }
            return new LowPriorityMockPropertyAccessor();
        }
    }

    public static class HighPriorityMockPropertyAccessorFactory implements PropertyAccessorFactory {

        @Override
        public int getPriority() {
            return PropertyAccessorFactory.HIGHEST_PRIORITY;
        }

        @Override
        public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target, Hints hints) {
            if (!PriorityMockDataObject.class.equals(type)) {
                return null;
            }
            return new HighPriorityMockPropertyAccessor();
        }
    }

    public static class PriorityMockPropertyAccessor implements PropertyAccessor {
        @Override
        public boolean canHandle(Object object, String xpath, Class target) {
            return object instanceof PriorityMockDataObject;
        }

        @Override
        public <T> T get(Object object, String xpath, Class<T> target) throws IllegalArgumentException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Object object, String xpath, Object value, Class target) throws IllegalArgumentException {
            throw new UnsupportedOperationException();
        }
    }

    public static class HighPriorityMockPropertyAccessor extends PriorityMockPropertyAccessor {}

    public static class LowPriorityMockPropertyAccessor extends PriorityMockPropertyAccessor {}

    public static class PriorityMockDataObject {}
}
