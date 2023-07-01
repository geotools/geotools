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
package org.geotools.jdbc;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.junit.Test;

public abstract class JDBCInEncodingOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCTestSetup createTestSetup();

    @Test
    public void testSimpleIn() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Function function =
                ff.function(
                        "in", ff.property(aname("intProperty")), ff.literal("1"), ff.literal("2"));
        Filter filter = ff.equal(function, ff.literal("true"), false);

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        SimpleFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(getCaseInsensitiveSet("ft1.1", "ft1.2"), collectFeatureIds(fc));
    }

    /** Tests "in3" with 3 values that are the same */
    @Test
    public void testSimpleIn3() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Function function =
                ff.function(
                        "in3",
                        ff.property(aname("intProperty")),
                        ff.literal("1"),
                        ff.literal("1"),
                        ff.literal("1"));
        Filter filter = ff.equal(function, ff.literal("true"), false);

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        SimpleFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(getCaseInsensitiveSet("ft1.1"), collectFeatureIds(fc));
    }

    @Test
    public void testNotEqual() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Function function =
                ff.function(
                        "in", ff.property(aname("intProperty")), ff.literal("1"), ff.literal("2"));
        Filter filter = ff.notEqual(function, ff.literal("true"), false);

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        SimpleFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(getCaseInsensitiveSet("ft1.0"), collectFeatureIds(fc));
    }

    @Test
    public void testNegated() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Function function =
                ff.function(
                        "in", ff.property(aname("intProperty")), ff.literal("1"), ff.literal("2"));
        Filter filter = ff.not(ff.equal(function, ff.literal("true"), false));

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        SimpleFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(getCaseInsensitiveSet("ft1.0"), collectFeatureIds(fc));
    }

    @Test
    public void testGreater() throws IOException {
        FilterFactory ff = dataStore.getFilterFactory();
        Function function =
                ff.function(
                        "in", ff.property(aname("intProperty")), ff.literal("1"), ff.literal("2"));
        Filter filter = ff.greater(function, ff.literal("false"), false);

        SimpleFeatureSource fs = dataStore.getFeatureSource(tname("ft1"));
        SimpleFeatureCollection fc = fs.getFeatures(filter);
        assertEquals(getCaseInsensitiveSet("ft1.1", "ft1.2"), collectFeatureIds(fc));
    }

    public Set<String> getCaseInsensitiveSet(String... values) {
        Set<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        result.addAll(asList(values));

        return result;
    }

    protected Set<String> collectFeatureIds(SimpleFeatureCollection fc) {
        Set<String> identifiers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        try (SimpleFeatureIterator fi = fc.features()) {
            while (fi.hasNext()) {
                SimpleFeature sf = fi.next();
                identifiers.add(sf.getID());
            }
        }
        return identifiers;
    }
}
