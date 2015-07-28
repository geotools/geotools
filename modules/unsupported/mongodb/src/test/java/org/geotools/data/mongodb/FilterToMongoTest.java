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

import java.util.Date;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsGreaterThan;
import org.opengis.filter.PropertyIsLessThan;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.spatial.BBOX;

import com.mongodb.BasicDBObject;
import com.vividsolutions.jts.geom.Point;

public class FilterToMongoTest extends TestCase {

    static final String DATE_LITERAL = "2015-07-01T00:00:00.000+01:00";

    FilterFactory2 ff;
    FilterToMongo filterToMongo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ff = CommonFactoryFinder.getFilterFactory2();
        filterToMongo = new FilterToMongo(new GeoJSONMapper());

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("ftTest");
        tb.setCRS(DefaultGeographicCRS.WGS84);
        tb.add("geometry", Point.class);
        tb.add("dateProperty", Date.class);
        filterToMongo.setFeatureType(tb.buildFeatureType());
    }

    public void testEqualTo() throws Exception {
        PropertyIsEqualTo equalTo = ff.equals(ff.property("foo"), ff.literal("bar"));
        BasicDBObject obj = (BasicDBObject) equalTo.accept(filterToMongo, null);
        assertNotNull(obj);

        assertEquals(1, obj.keySet().size());
        assertEquals("bar", obj.get("properties.foo"));
    }

    public void testBBOX() throws Exception {
        BBOX bbox = ff.bbox("loc", 10d,10d,20d,20d, "epsg:4326");
        BasicDBObject obj = (BasicDBObject) bbox.accept(filterToMongo, null);
        
        assertNotNull(obj);
    }

    public void testLike() throws Exception {
        PropertyIsLike like = ff.like(ff.property("stringProperty"), "on%", "%", "_", "\\");
        BasicDBObject obj = (BasicDBObject) like.accept(filterToMongo, null);

        assertNotNull(obj);
    }

    public void testLikeUnsupported() throws Exception {
        PropertyIsLike likeLiteral = ff.like(ff.literal("once upon a time"), "on%", "%", "_", "\\");
        PropertyIsLike likeFunction = ff.like(
                ff.function("Concatenate", ff.property("stringProperty"),
                        ff.literal("test")), "on%", "%", "_", "\\");

        try {
            likeLiteral.accept(filterToMongo, null);
            fail("Expected UnsupportedOperationException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }

        try {
            likeFunction.accept(filterToMongo, null);
            fail("Expected UnsupportedOperationException not thrown");
        } catch (Exception e) {
            assertTrue(e instanceof UnsupportedOperationException);
        }
    }

    public void testDateGreaterComparison() {
        PropertyIsGreaterThan gt = ff.greater(ff.property("dateProperty"),
                ff.literal(DATE_LITERAL));
        BasicDBObject obj = (BasicDBObject) gt.accept(filterToMongo, null);

        assertNotNull(obj);
        BasicDBObject filter = (BasicDBObject) obj.get("properties.dateProperty");
        assertNotNull(filter);
        assertEquals(MongoTestSetup.parseDate(DATE_LITERAL), filter.get("$gt"));
    }

    public void testDateLessComparison() {
        PropertyIsLessThan lt = ff.less(ff.property("dateProperty"),
                ff.literal(DATE_LITERAL));
        BasicDBObject obj = (BasicDBObject) lt.accept(filterToMongo, null);

        assertNotNull(obj);
        BasicDBObject filter = (BasicDBObject) obj.get("properties.dateProperty");
        assertNotNull(filter);
        assertEquals(MongoTestSetup.parseDate(DATE_LITERAL), filter.get("$lt"));
    }

    public void testDateBetweenComparison() {
        final String LOWER_BOUND = DATE_LITERAL;
        final String UPPER_BOUND = "2015-07-31T00:00:00.000+01:00";

        PropertyIsBetween lt = ff.between(ff.property("dateProperty"),
                ff.literal(LOWER_BOUND),
                ff.literal(UPPER_BOUND));
        BasicDBObject obj = (BasicDBObject) lt.accept(filterToMongo, null);

        assertNotNull(obj);
        BasicDBObject filter = (BasicDBObject) obj.get("properties.dateProperty");
        assertNotNull(filter);
        assertEquals(MongoTestSetup.parseDate(LOWER_BOUND), filter.get("$gte"));
        assertEquals(MongoTestSetup.parseDate(UPPER_BOUND), filter.get("$lte"));
    }

}
