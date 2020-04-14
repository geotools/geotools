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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;

public abstract class MongoFeatureSourceTest extends MongoTestSupport {

    protected MongoFeatureSourceTest(MongoTestSetup testSetup) {
        super(testSetup);
    }

    public void testBBOXFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        BBOX f = ff.bbox(ff.property("geometry"), 0.5, 0.5, 1.5, 1.5, "epsg:4326");

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");

        Query q = new Query("ft1", f);
        assertEquals(1, source.getCount(q));
        assertEquals(
                new ReferencedEnvelope(1d, 1d, 1d, 1d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 1);
        } finally {
            it.close();
        }
    }

    public void testEqualToFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsEqualTo f =
                ff.equals(ff.property("properties.stringProperty"), ff.literal("two"));

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", f);

        assertEquals(1, source.getCount(q));
        ReferencedEnvelope e = source.getBounds();
        assertEquals(
                new ReferencedEnvelope(2d, 0d, 2d, 0d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 0);
        } finally {
            it.close();
        }
    }

    public void testLikeFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsLike f = ff.like(ff.property("properties.stringProperty"), "on%", "%", "_", "\\");

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", f);

        assertEquals(1, source.getCount(q));
        assertEquals(
                new ReferencedEnvelope(1d, 1d, 1d, 1d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 1);
        } finally {
            it.close();
        }

        // check full string match
        f = ff.like(ff.property("properties.stringProperty"), "n%", "%", "_", "\\");

        source = dataStore.getFeatureSource("ft1");
        q = new Query("ft1", f);

        // no feature should match
        assertEquals(0, source.getCount(q));
    }

    public void testLikePostFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        // wrapping the property name in a function that is not declared as
        // supported in the filter capabilities (i.e. Concatenate) will make the
        // filter a post-filter
        PropertyIsLike f =
                ff.like(
                        ff.function(
                                "Concatenate",
                                ff.property("properties.stringProperty"),
                                ff.literal("test")),
                        "on%",
                        "%",
                        "_",
                        "\\");

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", f, new String[] {"geometry"});

        // filter should match just one feature
        assertEquals(1, source.getFeatures(q).size());
        assertEquals(
                new ReferencedEnvelope(1d, 1d, 1d, 1d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            SimpleFeature feature = it.next();
            assertFeature(feature, 1, false);
            // the stringProperty attribute should not be returned, since it was
            // used in the post-filter, but was not listed among the properties to fetch
            assertNull(feature.getAttribute("properties.stringProperty"));
        } finally {
            it.close();
        }
    }

    public void testDateGreaterComparison() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsGreaterThan gt =
                ff.greater(
                        ff.property("properties.dateProperty"),
                        ff.literal("2015-01-01T11:30:00.000Z"));

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", gt);

        assertEquals(2, source.getCount(q));
        assertEquals(
                new ReferencedEnvelope(0d, 2d, 0d, 2d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 0);
        } finally {
            it.close();
        }

        // test again passing Date object as literal
        gt =
                ff.greater(
                        ff.property("properties.dateProperty"),
                        ff.literal(MongoTestSetup.parseDate("2015-01-01T11:30:00.000Z")));
        q = new Query("ft1", gt);

        assertEquals(2, source.getCount(q));
        assertEquals(
                new ReferencedEnvelope(0d, 2d, 0d, 2d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));
        it = source.getFeatures(q).features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 0);
        } finally {
            it.close();
        }

        // test no-match filter
        gt =
                ff.greater(
                        ff.property("properties.dateProperty"),
                        ff.literal("2015-01-01T22:30:00.000Z"));
        q = new Query("ft1", gt);

        // no feature should match
        assertEquals(0, source.getCount(q));
    }

    public void testDateLessComparison() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsLessThan lt =
                ff.less(
                        ff.property("properties.dateProperty"),
                        ff.literal("2015-01-01T16:00:00.000Z"));

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", lt);

        assertEquals(1, source.getCount(q));
        assertEquals(
                new ReferencedEnvelope(0d, 2d, 0d, 2d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 0);
        } finally {
            it.close();
        }

        // test no-match filter
        lt =
                ff.less(
                        ff.property("properties.dateProperty"),
                        ff.literal("2015-01-01T00:00:00.000Z"));
        q = new Query("ft1", lt);

        // no feature should match
        assertEquals(0, source.getCount(q));
    }

    public void testDateBetweenComparison() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsBetween lt =
                ff.between(
                        ff.property("properties.dateProperty"),
                        ff.literal("2014-12-31T23:59:00.000Z"),
                        ff.literal("2015-01-01T00:01:00.000Z"));

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", lt);

        assertEquals(1, source.getCount(q));
        assertEquals(
                new ReferencedEnvelope(0d, 0d, 0d, 0d, DefaultGeographicCRS.WGS84),
                source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 0);
        } finally {
            it.close();
        }

        // test no-match filter
        lt =
                ff.between(
                        ff.property("properties.dateProperty"),
                        ff.literal("2014-12-31T23:59:00.000Z"),
                        ff.literal("2014-12-31T23:59:59.000Z"));
        q = new Query("ft1", lt);

        // no feature should match
        assertEquals(0, source.getCount(q));
    }

    public void testIsNullFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsNull isNull = ff.isNull(ff.literal("properties.nullableAttribute"));
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", isNull);
        assertEquals(2, source.getCount(q));
    }

    public void testOrPostFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsLike f1 =
                ff.like(ff.property("properties.stringProperty"), "on%", "%", "_", "\\");
        PropertyIsLike f2 =
                ff.like(ff.property("properties.stringProperty"), "no%", "%", "_", "\\");
        Or or = ff.or(f1, f2);
        checkBinaryLogicOperatorFilterSplitting(or);
    }

    public void testAndPostFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsLike f1 =
                ff.like(ff.property("properties.stringProperty"), "on%", "%", "_", "\\");
        PropertyIsLike f2 =
                ff.like(ff.property("properties.stringProperty"), "no%", "%", "_", "\\");
        And and = ff.and(f1, f2);
        checkBinaryLogicOperatorFilterSplitting(and);
    }

    public void testSingleSortBy() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        SortBy[] sorts =
                new SortBy[] {
                    ff.sort("properties.doubleProperty", SortOrder.DESCENDING),
                };

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        assertTrue(source.getQueryCapabilities().supportsSorting(sorts));
        Query q = new Query("ft1", Filter.INCLUDE);
        q.setSortBy(sorts);

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        List<Double> doubleValues = new ArrayList<>(3);
        while (it.hasNext()) {
            SimpleFeature feature = it.next();
            doubleValues.add((Double) feature.getAttribute("properties.doubleProperty"));
        }
        assertEquals(doubleValues.size(), 3);
        Double first = doubleValues.get(0);
        Double second = doubleValues.get(1);
        Double third = doubleValues.get(2);
        assertTrue(first > second);
        assertTrue(second > third);
    }

    public void testTwoSortBy() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        // sort before desc on string value a,b,b obtaining b,b,a
        // then asc on date 2015-01-01T00:00, 2015-01-01T16:30, 2015-01-01T21:30
        // obtaining second, third, one
        SortBy[] sorts =
                new SortBy[] {
                    ff.sort("properties.stringProperty2", SortOrder.DESCENDING),
                    ff.sort("properties.dateProperty", SortOrder.ASCENDING),
                };

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        assertTrue(source.getQueryCapabilities().supportsSorting(sorts));
        Query q = new Query("ft1", Filter.INCLUDE);
        q.setSortBy(sorts);

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        List<Date> dates = new ArrayList<>(3);
        List<String> stringAttributes = new ArrayList<>(3);
        while (it.hasNext()) {
            SimpleFeature feature = it.next();
            dates.add((Date) feature.getAttribute("properties.dateProperty"));
            stringAttributes.add((String) feature.getAttribute("properties.stringProperty2"));
        }
        assertEquals(stringAttributes.get(0), "b");
        assertEquals(stringAttributes.get(1), "b");
        assertEquals(stringAttributes.get(2), "a");
        assertEquals(dates.size(), 3);
        Date first = dates.get(0);
        Date second = dates.get(1);
        Date third = dates.get(2);
        assertTrue(first.before(second));
        assertTrue(second.after(third));
    }

    public void testTwoSortByWithNullableAttribute() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        // sort before on nullable so that second sort overcome
        SortBy[] sorts =
                new SortBy[] {
                    ff.sort("properties.nullableAttribute", SortOrder.DESCENDING),
                    ff.sort("properties.dateProperty", SortOrder.ASCENDING),
                };

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        assertTrue(source.getQueryCapabilities().supportsSorting(sorts));
        Query q = new Query("ft1", Filter.INCLUDE);
        q.setSortBy(sorts);

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        List<Date> dates = new ArrayList<>(3);
        while (it.hasNext()) {
            SimpleFeature feature = it.next();
            dates.add((Date) feature.getAttribute("properties.dateProperty"));
        }
        assertEquals(dates.size(), 3);
        Date first = dates.get(0);
        Date second = dates.get(1);
        Date third = dates.get(2);
        assertTrue(first.before(second));
        assertTrue(second.before(third));
    }

    private void checkBinaryLogicOperatorFilterSplitting(BinaryLogicOperator filter)
            throws Exception {
        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        assertTrue(source instanceof MongoFeatureStore);
        MongoFeatureStore mongoStore = (MongoFeatureStore) source;
        MongoFeatureSource mongoSource = mongoStore.delegate;
        Filter[] filters = mongoSource.splitFilter(filter);
        Filter preFilter = filters[0];
        assertTrue(preFilter instanceof BinaryLogicOperator);
        Filter postFilter = filters[1];
        assertTrue(postFilter instanceof IncludeFilter);
    }
}
