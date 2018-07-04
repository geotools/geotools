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

/** @author Sebastian Graca, ISPiK S.A. */
@RunWith(Parameterized.class)
public class AverageVisitorTest<T, R> extends VisitorTestCase<T, R> {
    public AverageVisitorTest(Class<T> valueClass, List<T> values, R expectedValue) {
        super(valueClass, values, expectedValue);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(
                new Object[] {Integer.class, Arrays.asList(-2, 0, 15), 13.0 / 3},
                new Object[] {Long.class, Arrays.asList(-2L, 0L, 15L), 13.0 / 3},
                new Object[] {Double.class, Arrays.asList(-2.1, 0.0, 15.2), 13.1 / 3},
                new Object[] {Float.class, Arrays.asList(-2.1f, 0.0f, 15.2f), 13.1f / 3});
    }

    @Override
    protected FeatureCalc createVisitor(int attributeTypeIndex, SimpleFeatureType type) {
        return new AverageVisitor(attributeTypeIndex, type);
    }
}
