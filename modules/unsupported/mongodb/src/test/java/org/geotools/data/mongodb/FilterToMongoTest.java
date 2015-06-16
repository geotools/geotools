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

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.spatial.BBOX;

import com.mongodb.BasicDBObject;

public class FilterToMongoTest extends TestCase {

    FilterFactory2 ff;
    FilterToMongo filterToMongo;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ff = CommonFactoryFinder.getFilterFactory2();
        filterToMongo = new FilterToMongo(new GeoJSONMapper());
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

}
