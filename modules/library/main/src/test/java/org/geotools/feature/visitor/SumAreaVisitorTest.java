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
package org.geotools.feature.visitor;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;

@RunWith(Parameterized.class)
public class SumAreaVisitorTest<T> extends VisitorTestCase<T, T> {

    private static final GeometryFactory GF = new GeometryFactory();

    public SumAreaVisitorTest(Class<T> valueClass, List<T> values, T expectedValue) {
        super(valueClass, values, expectedValue);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {

        return Arrays.asList(
                new Object[] {
                    Polygon.class,
                    Arrays.asList(
                            polygon(new int[] {12, 6, 14, 8, 16, 6, 16, 4, 12, 4, 12, 6}),
                            polygon(new int[] {10, 3, 12, 5, 16, 6, 15, 4, 13, 4, 10, 3})),
                    18.5
                },
                new Object[] {
                    Polygon.class,
                    Arrays.asList(polygon(new int[] {12, 6, 14, 8, 16, 6, 16, 4, 12, 4, 12, 6})),
                    12.0
                });
    }

    public static Polygon polygon(int[] xy) {
        LinearRing shell = ring(xy);
        return GF.createPolygon(shell, null);
    }

    public static LinearRing ring(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return GF.createLinearRing(coords);
    }

    @Override
    protected FeatureCalc createVisitor(int attributeTypeIndex, SimpleFeatureType type) {
        return new SumAreaVisitor(attributeTypeIndex, type);
    }
}
