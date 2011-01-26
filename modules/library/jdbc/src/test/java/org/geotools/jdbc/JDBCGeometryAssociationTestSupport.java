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

import java.util.Map;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Transaction;
import org.geotools.factory.Hints;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.identity.GmlObjectId;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;


public abstract class JDBCGeometryAssociationTestSupport extends JDBCTestSupport {
    protected void connect() throws Exception {
        super.connect();

        dataStore.setAssociations(true);
    }

    public void testGetFeatureNoAssociation() throws Exception {
        Hints hints = new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH, new Integer(1));

        DefaultQuery query = new DefaultQuery();
        query.setTypeName("ga");
        query.setHints(hints);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertTrue(reader.hasNext());

        SimpleFeature feature = (SimpleFeature) reader.next();
        assertNotNull(feature);

        assertEquals("ga.0", feature.getID());

        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertEquals(new Coordinate(0, 0), g.getCoordinate());

        Map ud = (Map) g.getUserData();
        assertEquals("0", ud.get("gml:id"));
        reader.close();
    }

    public void testGetFeatureWithAssociation() throws Exception {
        Hints hints = new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH, new Integer(1));

        DefaultQuery query = new DefaultQuery();
        query.setTypeName("ga");
        query.setHints(hints);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertTrue(reader.hasNext());
        reader.next();

        assertTrue(reader.hasNext());

        SimpleFeature feature = (SimpleFeature) reader.next();
        assertNotNull(feature);

        assertEquals("ga.1", feature.getID());

        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertEquals(new Coordinate(1, 1), g.getCoordinate());
        assertTrue(g.getUserData() instanceof Map);
        assertEquals("1", ((Map) g.getUserData()).get("gml:id"));
        
        reader.close();

        //test with zero dpeth
        query.setHints(new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH, new Integer(0)));
        reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);

        assertTrue(reader.hasNext());
        reader.next();

        assertTrue(reader.hasNext());

        feature = (SimpleFeature) reader.next();
        assertNotNull(feature);

        assertEquals("ga.1", feature.getID());

        g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g.isEmpty());
        //assertTrue( g instanceof NullGeometry );
        assertTrue(g.getUserData() instanceof Map);
        assertEquals("1", ((Map) g.getUserData()).get("gml:id"));
        
        reader.close();
    }

    public void testMultiGeometryAssociation() throws Exception {
        Hints hints = new Hints(Hints.ASSOCIATION_TRAVERSAL_DEPTH, new Integer(1));

        DefaultQuery query = new DefaultQuery();
        query.setTypeName("ga");
        query.setHints(hints);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertTrue(reader.hasNext());
        reader.next();
        assertTrue(reader.hasNext());
        reader.next();

        assertTrue(reader.hasNext());

        SimpleFeature feature = reader.next();

        Geometry g = (Geometry) feature.getDefaultGeometry();
        assertNotNull(g);
        assertTrue(g instanceof MultiPoint);

        MultiPoint mp = (MultiPoint) g;
        assertEquals("2", ((Map) mp.getUserData()).get("gml:id"));

        assertEquals(2, mp.getNumGeometries());

        Point p = (Point) mp.getGeometryN(0);
        assertEquals(new Coordinate(0, 0), p.getCoordinate());
        assertEquals("0", ((Map) p.getUserData()).get("gml:id"));

        p = (Point) mp.getGeometryN(1);
        assertEquals(new Coordinate(1, 1), p.getCoordinate());
        assertEquals("1", ((Map) p.getUserData()).get("gml:id"));
        
        reader.close();
    }
    
    public void testGetGmlObjectGeometry() throws Exception {
        GmlObjectId id = dataStore.getFilterFactory().gmlObjectId("0");
        Object o = dataStore.getGmlObject( id, null);
        
        assertNotNull( o );
        assertTrue( o instanceof Point );
        assertEquals( new Coordinate( 0 , 0 ), ((Point)o).getCoordinate() );
        
        id = dataStore.getFilterFactory().gmlObjectId( "ft1.0" );
        o = dataStore.getGmlObject( id, null);
        assertNotNull( o );
        assertTrue( o instanceof SimpleFeature );
    }
}
