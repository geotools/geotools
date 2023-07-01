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
/** @author hans.yperman@vliz.be */
package org.geotools.data.mongodb;

import org.geotools.data.mongodb.geojson.GeoJSONMongoTestSetup;
import org.geotools.data.store.ContentEntry;
import org.geotools.feature.NameImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import org.geotools.api.filter.Filter;

public class FilterSplitterTest extends MongoTestSupport {

    public FilterSplitterTest() throws Exception {
        super(new GeoJSONMongoTestSetup());
    }

    @Test
    public void testProperty() throws Exception {
        checkSplitter("a=1", "a=1", "INCLUDE");
    }

    @Test
    public void testAndMongoOnly() throws Exception {
        checkSplitter("a=1 AND b=1", "a=1 AND b=1", "INCLUDE");
    }

    @Test
    public void testAndSplit() throws Exception {
        // FIXME: CQL.toFilter(before) has wrong date format
        checkSplitter(
                "a=1 AND b BEFORE 1980-09-03T00:00:00", "a=1", "b BEFORE 1980-09-03T00:00:00");
    }

    @Test
    public void testJSonSelect() throws Exception {
        // jsonSelect is only supported by mongo if the other side is a literal
        checkSplitter("jsonSelect('a')=1", "jsonSelect('a')=1", "INCLUDE");
        checkSplitter("1=jsonSelect('a')", "1=jsonSelect('a')", "INCLUDE");
        checkSplitter("jsonSelect('a')=a", "INCLUDE", "jsonSelect('a')=a");
        checkSplitter("a=jsonSelect('a')", "INCLUDE", "a=jsonSelect('a')");
    }

    @Test
    public void testLike() throws Exception {
        checkSplitter("a like '%'", "a like '%'", "INCLUDE");
        checkSplitter("(a+b) like '%'", "INCLUDE", "(a+b) like '%'");
    }

    /**
     * @param beforeSplit The input to the splitter
     * @param toMongo The part of the filter given to mongodb
     * @param toPostprocess The part of the filter done by postprocessing
     * @throws Exception
     */
    private void checkSplitter(String beforeSplit, String toMongo, String toPostprocess)
            throws Exception {
        // FIXME getCountInternal ignores the toPostprocess, is this correct?
        connect();
        ContentEntry entry = new ContentEntry(dataStore, new NameImpl("ft1"));
        MongoFeatureSource source = new MongoFeatureSource(entry, null, null);
        Filter[] split = source.splitFilter(ECQL.toFilter(beforeSplit));
        Assert.assertEquals(ECQL.toFilter(toMongo), split[0]);
        Assert.assertEquals(ECQL.toFilter(toPostprocess), split[1]);
    }
}
