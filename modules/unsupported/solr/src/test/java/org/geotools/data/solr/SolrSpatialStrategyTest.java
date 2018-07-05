/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.text.NumberFormat;
import java.util.Locale;
import org.geotools.data.solr.SolrSpatialStrategy.BBoxStrategy;
import org.geotools.data.solr.SolrSpatialStrategy.DefaultStrategy;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;

public class SolrSpatialStrategyTest {

    @Test
    public void testCreate() throws Exception {

        assertTrue(
                SolrSpatialStrategy.createStrategy(newDescriptor(null)) instanceof DefaultStrategy);
        assertTrue(
                SolrSpatialStrategy.createStrategy(
                                newDescriptor(
                                        "org.apache.solr.schema.SpatialRecursivePrefixTreeFieldType"))
                        instanceof DefaultStrategy);
        assertTrue(
                SolrSpatialStrategy.createStrategy(
                                newDescriptor("org.apache.solr.schema.LatLonType"))
                        instanceof DefaultStrategy);
        assertTrue(
                SolrSpatialStrategy.createStrategy(
                                newDescriptor("org.apache.solr.schema.BBoxField"))
                        instanceof BBoxStrategy);
        assertTrue(
                SolrSpatialStrategy.createStrategy(
                                newDescriptor("org.apache.solr.spatial.pending.BBoxFieldType"))
                        instanceof BBoxStrategy);
    }

    @Test
    public void testDefaultStrategy() throws Exception {
        SolrSpatialStrategy ss = new DefaultStrategy();

        Geometry g = ss.decode("POINT (0 0)");
        assertNotNull(g);
        assertTrue(g instanceof Point);

        assertEquals("POINT (0 0)", ss.encode(g));
    }

    @Test
    public void testBBoxStrategy() throws Exception {
        SolrSpatialStrategy ss = new BBoxStrategy();

        Geometry g = ss.decode("ENVELOPE(1, 3, 4, 2)");
        assertNotNull(g);
        assertTrue(g instanceof Polygon);

        String[] bbox = ss.encode(g).split(",");
        bbox[0] = bbox[0].substring(bbox[0].indexOf("(") + 1, bbox[0].length());
        bbox[3] = bbox[3].substring(0, bbox[3].indexOf(")"));
        NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
        assertEquals(1.0, format.parse(bbox[0]).doubleValue(), 0.1);
        assertEquals(3.0, format.parse(bbox[1]).doubleValue(), 0.1);
        assertEquals(4.0, format.parse(bbox[2]).doubleValue(), 0.1);
        assertEquals(2.0, format.parse(bbox[3]).doubleValue(), 0.1);
    }

    GeometryDescriptor newDescriptor(String solrTypeValue) {
        FeatureTypeFactory ftf = new FeatureTypeFactoryImpl();
        GeometryType type =
                ftf.createGeometryType(
                        new NameImpl("fooType"),
                        Geometry.class,
                        null,
                        false,
                        false,
                        null,
                        null,
                        null);
        GeometryDescriptor att =
                ftf.createGeometryDescriptor(type, new NameImpl("foo"), 1, 1, true, null);

        att.getUserData().put(SolrFeatureSource.KEY_SOLR_TYPE, solrTypeValue);
        return att;
    }
}
