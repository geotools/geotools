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
package org.geotools.feature.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.opengis.feature.simple.SimpleFeatureType;

@RunWith(Parameterized.class)
public class MedianVisitorTest<T> extends VisitorTestCase<T, T> {
    public MedianVisitorTest(Class<T> valueClass, List<T> values, T expectedValue) {
        super(valueClass, values, expectedValue);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[] {Integer.class, Arrays.asList(34, 33, -2, 0, 15), 15},
                new Object[] {Long.class, Arrays.asList(34L, 33L, -2L, 0L, 15L), 15L},
                new Object[] {Double.class, Arrays.asList(34.1, 33.2, -2.1, 0.0, 15.2), 15.2},
                new Object[] {Float.class, Arrays.asList(34.1f, 33.2f, -2.1f, 0.0f, 15.2f), 15.2f});
    }

    @Override
    protected FeatureCalc createVisitor(int attributeTypeIndex, SimpleFeatureType type) {
        return new MedianVisitor(attributeTypeIndex, type);
    }
}
