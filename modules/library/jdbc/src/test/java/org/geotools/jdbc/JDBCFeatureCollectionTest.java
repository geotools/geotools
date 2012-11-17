/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.Iterator;

import com.vividsolutions.jts.geom.Point;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.feature.FeatureIterator;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCFeatureCollectionTest extends JDBCTestSupport {
    SimpleFeatureCollection collection;
    JDBCFeatureStore source;

    protected void connect() throws Exception {
        super.connect();

        source = (JDBCFeatureStore) dataStore.getFeatureSource(tname("ft1"));
        collection = source.getFeatures(); 
    }

    public void testIterator() throws Exception {
        FeatureIterator<SimpleFeature> i = collection.features();
        assertNotNull(i);

        assertFeatureIterator(0, 3, i, new SimpleFeatureAssertion() {

            public int toIndex(SimpleFeature feature) {
                return ((Number) feature.getAttribute(aname("intProperty"))).intValue();
            }

            public void check(int index, SimpleFeature feature) {
                assertNotNull(feature);

                String fid = feature.getID();

                int id = Integer.parseInt(fid.substring(fid.indexOf('.') + 1));


                assertEquals(index, id);
            }
        });
    }

    public void testBounds() throws IOException {
        ReferencedEnvelope bounds = collection.getBounds();
        assertNotNull(bounds);

        assertEquals(0d, bounds.getMinX(), 0.1);
        assertEquals(0d, bounds.getMinY(), 0.1);
        assertEquals(2d, bounds.getMaxX(), 0.1);
        assertEquals(2d, bounds.getMaxY(), 0.1);
    }

    public void testSize() throws IOException {
        assertEquals(3, collection.size());
    }

    public void testSubCollection() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        Filter f = ff.equals(ff.property(aname("intProperty")), ff.literal(1));

        SimpleFeatureCollection sub = collection.subCollection(f);
        assertNotNull(sub);
        assertEquals(1, sub.size());
        
        ReferencedEnvelope exp = new ReferencedEnvelope(1, 1, 1, 1, CRS.decode("EPSG:4326")); 
        ReferencedEnvelope act = sub.getBounds();
        
        assertEquals(exp.getMinX(), act.getMinX(), 0.1);
        assertEquals(exp.getMinY(), act.getMinY(), 0.1);
        assertEquals(exp.getMaxX(), act.getMaxX(), 0.1);
        assertEquals(exp.getMaxY(), act.getMaxY(), 0.1);
    }

    public void testAdd() throws IOException {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(collection.getSchema());
        b.set(aname("intProperty"), new Integer(3));
        b.set(aname("doubleProperty"), new Double(3.3));
        b.set(aname("stringProperty"), "three");
        b.set(aname("geometry"), new GeometryFactory().createPoint(new Coordinate(3, 3)));

        SimpleFeature feature = b.buildFeature(null);
        assertEquals(3, collection.size());

        source.addFeatures( DataUtilities.collection( feature ));

        assertEquals(4, collection.size());

        SimpleFeatureIterator i = collection.features();
        try {
            boolean found = false;
    
            while (i.hasNext()) {
                SimpleFeature f = (SimpleFeature) i.next();
    
                if ("three".equals(f.getAttribute(aname("stringProperty")))) {
                    assertEquals(feature.getAttribute(aname("doubleProperty")),
                        f.getAttribute(aname("doubleProperty")));
                    assertEquals(feature.getAttribute(aname("stringProperty")),
                        f.getAttribute(aname("stringProperty")));
                    assertEquals(feature.getAttribute(aname("geometry")),
                        f.getAttribute(aname("geometry")));
                    found = true;
                }
            }
            assertTrue(found);
        }
        finally {
            i.close();
        }
    }

}
