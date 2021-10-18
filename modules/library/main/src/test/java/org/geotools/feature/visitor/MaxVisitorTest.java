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

import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;

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
        SCHEMA = builder.buildFeatureType();
    }

    @Test(expected = IllegalStateException.class)
    public void visitedFlagSetWithoutValue() throws Exception {

        final MemoryFeatureCollection collection = new MemoryFeatureCollection(SCHEMA);
        collection.add(SimpleFeatureBuilder.build(SCHEMA, new Object[] {1}, "1"));

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

    /** A MaxVisitor that set's visitied without maxvalue being set. */
    private class PreVisitedMaxVisitor extends MaxVisitor {
        PreVisitedMaxVisitor(String attribute) {
            super(attribute);
            this.visited = true;
        }
    }
}
