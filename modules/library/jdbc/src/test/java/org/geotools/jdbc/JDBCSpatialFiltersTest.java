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

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

/**
 * Excercises spatial filters
 * 
 * @author Andrea Aime - OpenGeo
 *
 * @source $URL$
 */
public abstract class JDBCSpatialFiltersTest extends JDBCTestSupport {
    
    TestData td;

    protected void connect() throws Exception {
        super.connect();

        if (td == null) {
            td = new TestData(((JDBCDataStoreAPITestSetup) setup).getInitialPrimaryKeyValue());
            
            td.ROAD = tname( td.ROAD );
            td.ROAD_ID = aname( td.ROAD_ID );
            td.ROAD_GEOM = aname( td.ROAD_GEOM );
            td.ROAD_NAME = aname( td.ROAD_NAME );
            
            td.RIVER = tname( td.RIVER );
            td.RIVER_ID = aname( td.RIVER_ID );
            td.RIVER_GEOM = aname( td.RIVER_GEOM );
            td.RIVER_FLOW = aname( td.RIVER_FLOW );
            td.RIVER_RIVER = aname( td.RIVER_RIVER );
            
            td.build();
        }

        dataStore.setDatabaseSchema(null);
    }

    protected abstract JDBCDataStoreAPITestSetup createTestSetup();

    public void testBboxFilter() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match only "r2"
        BBOX bbox = ff.bbox(aname("geom"), 2, 3, 4, 5, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(bbox);
        checkSingleResult(features, "r2");
    }
    
    public void testBboxFilterDefault() throws Exception {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // should match only "r2"
        BBOX bbox = ff.bbox("", 2, 3, 4, 5, "EPSG:4326");
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(bbox);
        checkSingleResult(features, "r2");
    }

    public void testCrossesFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r2"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] { 2, 3, 4, 3 }, 2));
        Crosses cs = ff.crosses(ff.property(aname("geom")), ff.literal(ls));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(cs);
        checkSingleResult(features, "r2");
    }

    public void testIntersectsFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r1"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] { 2, 1, 2, 3 }, 2));
        Intersects is = ff.intersects(ff.property(aname("geom")), ff.literal(ls));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(is);
        checkSingleResult(features, "r1");
    }
    
    public void testIntersectsRingFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r1"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLinearRing(sf.create(new double[] { 2, 1, 2, 3, 0, 3, 2, 1}, 2));
        Intersects is = ff.intersects(ff.property(aname("geom")), ff.literal(ls));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(is);
        checkSingleResult(features, "r1");
    }
    
    public void testTouchesFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r1"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LineString ls = gf.createLineString(sf.create(new double[] { 1, 1, 1, 3 }, 2));
        Touches is = ff.touches(ff.property(aname("geom")), ff.literal(ls));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(is);
        checkSingleResult(features, "r1");
    }

    public void testContainsFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r2"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LinearRing shell = gf.createLinearRing(sf.create(new double[] { 2, -1, 2, 5, 4, 5, 4, -1,
                2, -1 }, 2));
        Polygon polygon = gf.createPolygon(shell, null);
        Contains cs = ff.contains(ff.literal(polygon), ff.property(aname("geom")));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(cs);
        checkSingleResult(features, "r2");
    }
    
    /**
     * Same as contains, with roles reversed
     * 
     * @throws Exception
     */
    public void testWithinFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r2"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LinearRing shell = gf.createLinearRing(sf.create(new double[] { 2, -1, 2, 5, 4, 5, 4, -1,
                2, -1 }, 2));
        Polygon polygon = gf.createPolygon(shell, null);
        Within wt = ff.within(ff.property(aname("geom")), ff.literal(polygon));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(wt);
        checkSingleResult(features, "r2");
    }

    public void testDisjointFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r2"
        GeometryFactory gf = new GeometryFactory();
        PackedCoordinateSequenceFactory sf = new PackedCoordinateSequenceFactory();
        LinearRing shell = gf.createLinearRing(sf.create(new double[] { 4, -1, 4, 5, 6, 5, 6, -1,
                4, -1 }, 2));
        Polygon polygon = gf.createPolygon(shell, null);
        Disjoint dj = ff.disjoint(ff.property(aname("geom")), ff.literal(polygon));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(dj);
        checkSingleResult(features, "r2");
    }

    public void testEqualsFilter() throws Exception {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        // should match only "r3"
        GeometryFactory gf = new GeometryFactory();
        Geometry g = gf.createGeometry((Geometry) td.roadFeatures[2].getDefaultGeometry());
        Equals cs = ff.equal(ff.literal(g), ff.property(aname("geom")));
        FeatureCollection features = dataStore.getFeatureSource(tname("road")).getFeatures(cs);
        checkSingleResult(features, "r3");
    }

    protected void checkSingleResult(FeatureCollection features, String name) {
        assertEquals(1, features.size());
        FeatureIterator fr = features.features();
        assertTrue(fr.hasNext());
        SimpleFeature f = (SimpleFeature) fr.next();
        assertNotNull(f);
        assertEquals(name, f.getAttribute(aname("name")));
        assertFalse(fr.hasNext());
        fr.close();
    }
}
