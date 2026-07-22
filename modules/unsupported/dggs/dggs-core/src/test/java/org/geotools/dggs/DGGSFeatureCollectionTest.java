/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.dggs.datastore.DGGSDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.visitor.Aggregate;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.GroupByVisitor;
import org.junit.Before;
import org.junit.Test;

public class DGGSFeatureCollectionTest {

    private static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    private SimpleFeatureType delegateSchema;
    private SimpleFeatureType dggsSchema;
    private DGGSFeatureCollection<String> collection;

    @Before
    public void setup() throws Exception {
        delegateSchema = DataUtilities.createType("delegated", "zone:String,category:String,value:Integer");
        dggsSchema = DataUtilities.createType(
                "dggs", "zone:String,category:String,value:Integer," + DGGSDataStore.GEOMETRY + ":Polygon");
        SimpleFeature[] features = {
            SimpleFeatureBuilder.build(delegateSchema, new Object[] {"A", "one", 10}, "fid.1"),
            SimpleFeatureBuilder.build(delegateSchema, new Object[] {"B", "two", 20}, "fid.2")
        };
        SimpleFeatureCollection delegate = DataUtilities.collection(features);
        collection = new DGGSFeatureCollection<>(delegate, dggsSchema, "zone", null);
    }

    @Test
    public void testGeometrylessCountVisitorIsDelegatedOnce() throws Exception {
        CountVisitor visitor = new CountVisitor();

        collection.accepts(visitor, null);

        assertEquals(2, visitor.getCount());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGeometrylessGroupByVisitorIsDelegated() throws Exception {
        GroupByVisitor visitor = new GroupByVisitor(
                Aggregate.SUM, FF.property("value"), Collections.singletonList(FF.property("category")), null);

        collection.accepts(visitor, null);

        Map<Object, Object> result = visitor.getResult().toMap();
        assertEquals(10, result.get(Collections.singletonList("one")));
        assertEquals(20, result.get(Collections.singletonList("two")));
    }
}
