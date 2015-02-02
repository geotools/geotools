/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geotools.data.solr.SolrSpatialStrategy.BBoxStrategy;
import org.geotools.data.solr.SolrSpatialStrategy.DefaultStrategy;
import org.geotools.feature.NameImpl;
import org.geotools.feature.type.FeatureTypeFactoryImpl;
import org.junit.Test;
import org.opengis.feature.type.FeatureTypeFactory;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;

import java.text.NumberFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SolrSpatialStrategyTest {

    @Test
    public void testCreate() throws Exception {

        assertTrue(SolrSpatialStrategy.createStrategy(newDescriptor(null)) instanceof DefaultStrategy);
        assertTrue(SolrSpatialStrategy.createStrategy(newDescriptor(
            "org.apache.solr.schema.SpatialRecursivePrefixTreeFieldType")) instanceof DefaultStrategy);
        assertTrue(SolrSpatialStrategy.createStrategy(newDescriptor(
            "org.apache.solr.schema.LatLonType")) instanceof DefaultStrategy);
        assertTrue(SolrSpatialStrategy.createStrategy(newDescriptor(
            "org.apache.solr.schema.BBoxField")) instanceof BBoxStrategy);
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

        Geometry g = ss.decode("1 2 3 4");
        assertNotNull(g);
        assertTrue(g instanceof Polygon);

        String[] bbox = ss.encode(g).split("\\s+");
        NumberFormat format = NumberFormat.getInstance();
        assertEquals(1.0, format.parse(bbox[0]).doubleValue(), 0.1);
        assertEquals(2.0, format.parse(bbox[1]).doubleValue(), 0.1);
        assertEquals(3.0, format.parse(bbox[2]).doubleValue(), 0.1);
        assertEquals(4.0, format.parse(bbox[3]).doubleValue(), 0.1);
    }

    GeometryDescriptor newDescriptor(String solrTypeValue) {
        FeatureTypeFactory ftf = new FeatureTypeFactoryImpl();
        GeometryType type =
            ftf.createGeometryType(new NameImpl("fooType"), Geometry.class, null, false, false, null, null, null);
        GeometryDescriptor att = ftf
            .createGeometryDescriptor(type, new NameImpl("foo"), 1, 1, true, null);

        att.getUserData().put(SolrFeatureSource.KEY_SOLR_TYPE, solrTypeValue);
        return att;
    }
}
