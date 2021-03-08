/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.store;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.FilteringSimpleFeatureCollection;
import org.geotools.feature.visitor.CountVisitor;
import org.geotools.feature.visitor.MaxVisitor;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.util.ProgressListener;

public class FilteringSimpleFeatureCollectionTest extends FeatureCollectionWrapperTestSupport {
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    FeatureVisitor lastVisitor = null;
    private ListFeatureCollection visitorCollection;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        SimpleFeatureType schema =
                DataUtilities.createType(
                        "BasicPolygons", "the_geom:MultiPolygon:srid=4326,ID:String,value:int");
        visitorCollection =
                new ListFeatureCollection(schema) {
                    @Override
                    public void accepts(FeatureVisitor visitor, ProgressListener progress)
                            throws java.io.IOException {
                        lastVisitor = visitor;
                    };

                    @Override
                    public SimpleFeatureCollection subCollection(Filter filter) {
                        if (filter == Filter.INCLUDE) {
                            return this;
                        } else {
                            return super.subCollection(filter);
                        }
                    }
                };
    }

    @Test
    public void testNext() {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        assertNotNull(collection.features().next());
    }

    @Test
    public void testCount() {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        assertEquals(1, collection.size());
    }

    @Test
    public void testVisitor() throws IOException {
        Filter filter = ff.equal(ff.property("someAtt"), ff.literal("1"), false);
        SimpleFeatureCollection collection = new FilteringSimpleFeatureCollection(delegate, filter);
        collection.accepts(
                feature -> assertEquals(1, feature.getProperty("someAtt").getValue()), null);
    }

    @Test
    public void testMaxVisitorDelegation() throws SchemaException, IOException {
        MaxVisitor visitor =
                new MaxVisitor(CommonFactoryFinder.getFilterFactory2().property("value"));
        assertOptimalVisit(visitor);
    }

    @Test
    public void testCountVisitorDelegation() throws SchemaException, IOException {
        FeatureVisitor visitor = new CountVisitor();
        assertOptimalVisit(visitor);
    }

    private void assertOptimalVisit(FeatureVisitor visitor) throws IOException {
        FilteringSimpleFeatureCollection retypedCollection =
                new FilteringSimpleFeatureCollection(visitorCollection, Filter.INCLUDE);
        retypedCollection.accepts(visitor, null);
        assertSame(lastVisitor, visitor);
    }
}
