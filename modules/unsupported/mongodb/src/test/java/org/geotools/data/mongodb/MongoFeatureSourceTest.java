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

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.spatial.BBOX;

public abstract class MongoFeatureSourceTest extends MongoTestSupport {

    protected MongoFeatureSourceTest(MongoTestSetup testSetup) {
        super(testSetup);
    }

    public void testBBOXFilter() throws Exception {
      FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
      BBOX f = ff.bbox(ff.property("geometry"),  0.5, 0.5, 1.5, 1.5, "epsg:4326");

      SimpleFeatureSource source = dataStore.getFeatureSource("ft1");

      Query q = new Query("ft1", f);
      assertEquals(1, source.getCount(q));
      assertEquals(new ReferencedEnvelope(1d,1d,1d,1d,DefaultGeographicCRS.WGS84), source.getBounds(q));

      SimpleFeatureCollection features = source.getFeatures(q);
      SimpleFeatureIterator it = features.features();
      try {
          assertTrue(it.hasNext());
          assertFeature(it.next(), 1);
      }
      finally {
          it.close();
      }
    }

    public void testEqualToFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsEqualTo f = ff.equals(ff.property("properties.stringProperty"), ff.literal("two"));

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", f);
        
        assertEquals(1, source.getCount(q));
        assertEquals(new ReferencedEnvelope(2d,2d,2d,2d,DefaultGeographicCRS.WGS84), source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 2);
        }
        finally {
            it.close();
        }
    }

    public void testLikeFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        PropertyIsLike f = ff.like(ff.property("properties.stringProperty"), "on%", "%", "_", "\\");

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", f);

        assertEquals(1, source.getCount(q));
        assertEquals(new ReferencedEnvelope(1d,1d,1d,1d,DefaultGeographicCRS.WGS84), source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            assertFeature(it.next(), 1);
        }
        finally {
            it.close();
        }
    }

    public void testLikePostFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        // wrapping the property name in a function that is not declared as
        // supported in the filter capabilities (i.e. Concatenate) will make the
        // filter a post-filter
        PropertyIsLike f = ff.like(ff.function("Concatenate",
                ff.property("properties.stringProperty"), ff.literal("test")),
                "on%", "%", "_", "\\");

        SimpleFeatureSource source = dataStore.getFeatureSource("ft1");
        Query q = new Query("ft1", f, new String[] { "geometry" });

        // filter should match just one feature
        assertEquals(1, source.getCount(q));
        assertEquals(new ReferencedEnvelope(1d,1d,1d,1d,DefaultGeographicCRS.WGS84), source.getBounds(q));

        SimpleFeatureCollection features = source.getFeatures(q);
        SimpleFeatureIterator it = features.features();
        try {
            assertTrue(it.hasNext());
            SimpleFeature feature =  it.next();
            assertFeature(feature, 1, false);
            // the stringProperty attribute should not be returned, since it was
            // used in the post-filter, but was not listed among the properties to fetch
            assertNull(feature.getAttribute("properties.stringProperty"));
        }
        finally {
            it.close();
        }
    }

}
