/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 * Testing MaxVisitor
 *
 * @author Roar Brænden
 */
public class MaxVisitorTest {

    private static SimpleFeatureType SCHEMA;

    static {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("Test");
        builder.add("AInteger", Integer.class);
        builder.add("ALong", Long.class);
        SCHEMA = builder.buildFeatureType();
    }

    @Test(expected = IllegalStateException.class)
    public void visitedFlagSetWithoutValue() throws Exception {

        final MemoryFeatureCollection collection = new MemoryFeatureCollection(SCHEMA);
        collection.add(SimpleFeatureBuilder.build(SCHEMA, new Object[] {1, 0}, "1"));

        final MaxVisitor falseVisitor = new PreVisitedMaxVisitor("AInteger");
        try {
            collection.accepts(falseVisitor, null);
        } catch (Exception e) {
            while (e.getCause() != null) {
                e = (Exception) e.getCause();
            }
            throw e;
        }
    }

    /** Check's that maxValue is set to null, and not a presumably low number. */
    @Test
    public void resetCallWillNullify() throws Exception {
        final MemoryFeatureCollection collection = new MemoryFeatureCollection(SCHEMA);
        collection.add(SimpleFeatureBuilder.build(SCHEMA, new Object[] {0, Integer.MIN_VALUE - 1}, "1"));

        final MaxVisitor visitor = new MaxVisitor("ALong");
        collection.accepts(visitor, null);

        visitor.reset();

        collection.accepts(visitor, null);
        Assert.assertEquals(Long.valueOf(Integer.MIN_VALUE - 1), (Long) visitor.getMax());
    }

    /** A MaxVisitor that set's visitied without maxvalue being set. */
    private static class PreVisitedMaxVisitor extends MaxVisitor {
        PreVisitedMaxVisitor(String attribute) {
            super(attribute);
            this.visited = true;
        }
    }
}
