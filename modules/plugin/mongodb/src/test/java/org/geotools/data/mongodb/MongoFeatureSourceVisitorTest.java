/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.geotools.data.Query;
import org.geotools.data.mongodb.geojson.GeoJSONMongoTestSetup;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.IllegalFilterException;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;

public class MongoFeatureSourceVisitorTest extends MongoTestSupport {

    public MongoFeatureSourceVisitorTest() {
        super(new GeoJSONMongoTestSetup());
    }

    boolean visited = false;
    boolean valueSet = false;

    class TestMinVisitor extends MinVisitor {

        public TestMinVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }

        public void setValue(Object result) {
            super.setValue(result);
            valueSet = true;
        }
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        visited = false;
        valueSet = false;
    }

    @Test
    public void testMinVisitor() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property("properties.dateProperty");

        MinVisitor v = new TestMinVisitor(p);

        dataStore.getFeatureSource("ft1").accepts(Query.ALL, v, null);

        Date minDate =
                Date.from(
                        Instant.from(
                                DateTimeFormatter.ISO_DATE_TIME.parse("2015-01-01T00:00:00Z")));
        assertEquals(minDate, v.getResult().getValue());

        assertFalse(visited);
        assertTrue(valueSet);
    }

    class TestMaxVisitor extends MaxVisitor {

        public TestMaxVisitor(Expression expr) throws IllegalFilterException {
            super(expr);
        }

        public void visit(Feature feature) {
            super.visit(feature);
            visited = true;
        }

        public void visit(SimpleFeature feature) {
            super.visit(feature);
            visited = true;
        }

        public void setValue(Object result) {
            super.setValue(result);
            valueSet = true;
        }
    }

    @Test
    public void testMaxVisitor() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyName p = ff.property("properties.dateProperty");

        MaxVisitor v = new TestMaxVisitor(p);

        dataStore.getFeatureSource("ft1").accepts(Query.ALL, v, null);

        Date minDate =
                Date.from(
                        Instant.from(
                                DateTimeFormatter.ISO_DATE_TIME.parse("2015-01-01T21:30:00Z")));
        assertEquals(minDate, v.getResult().getValue());

        assertFalse(visited);
        assertTrue(valueSet);
    }
}
