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

import java.util.HashSet;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Tests the ability of the datastore to cope with 3D data
 * 
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public abstract class JDBC3DTest extends JDBCTestSupport {
    
    protected static final String LINE3D = "line3d";

    protected static final String POLY3D = "poly3d";

    protected static final String POINT3D = "point3d";

    protected static final String ID = "id";

    protected static final String GEOM = "geom";

    protected static final String NAME = "name";

    protected static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    protected SimpleFeatureType poly3DType;

    protected SimpleFeatureType line3DType;
    
    protected CoordinateReferenceSystem epsg4326;

    protected abstract JDBC3DTestSetup createTestSetup();
    
    

    @Override
    protected void connect() throws Exception {
        super.connect();

        line3DType = DataUtilities.createType(dataStore.getNamespaceURI() + "." + tname(LINE3D),
                aname(ID) + ":0," + aname(GEOM) + ":LineString:srid=4326," + aname(NAME)
                        + ":String");
        line3DType.getGeometryDescriptor().getUserData().put(Hints.COORDINATE_DIMENSION, 3);
        poly3DType = DataUtilities.createType(dataStore.getNamespaceURI() + "." + tname(POLY3D),
                aname(ID) + ":0," + aname(GEOM) + ":Polygon:srid=4326," + aname(NAME) + ":String");
        poly3DType.getGeometryDescriptor().getUserData().put(Hints.COORDINATE_DIMENSION, 3);
        
        epsg4326 = CRS.decode("EPSG:4326");
    }

    
    protected Integer getNativeSRID() {
        return new Integer(4326);
    }
    
    public void testSchema() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname(LINE3D));
        CoordinateReferenceSystem crs = schema.getGeometryDescriptor()
                .getCoordinateReferenceSystem();
        assertEquals(new Integer(4326), CRS.lookupEpsgCode(crs, false));
        assertEquals(getNativeSRID(), schema.getGeometryDescriptor().getUserData().get(
                JDBCDataStore.JDBC_NATIVE_SRID));
    }

    public void testReadPoint() throws Exception {
        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(POINT3D)).getFeatures();
        SimpleFeatureIterator fr = fc.features();
        assertTrue(fr.hasNext());
        Point p = (Point) fr.next().getDefaultGeometry();
        assertTrue(new Coordinate(1, 1, 1).equals(p.getCoordinate()));
        fr.close();
    }

    public void testReadLine() throws Exception {
    	SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(LINE3D)).getFeatures();
        SimpleFeatureIterator fr = fc.features();
        assertTrue(fr.hasNext());
        LineString ls = (LineString) fr.next().getDefaultGeometry();
        // 1 1 0, 2 2 0, 4 2 1, 5 1 1
        assertEquals(4, ls.getCoordinates().length);
        assertTrue(new Coordinate(1, 1, 0).equals3D(ls.getCoordinateN(0)));
        assertTrue(new Coordinate(2, 2, 0).equals3D(ls.getCoordinateN(1)));
        assertTrue(new Coordinate(4, 2, 1).equals3D(ls.getCoordinateN(2)));
        assertTrue(new Coordinate(5, 1, 1).equals3D(ls.getCoordinateN(3)));
        fr.close();
    }

    public void testWriteLine() throws Exception {
        // build a 3d line
        GeometryFactory gf = new GeometryFactory();
        LineString ls = gf.createLineString(new Coordinate[] { new Coordinate(0, 0, 0),
                new Coordinate(1, 1, 1) });

        // build a feature around it
        SimpleFeature newFeature = SimpleFeatureBuilder.build(line3DType, new Object[] { 2, ls,
                "l3" }, null);

        // insert it
        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore
                .getFeatureSource(tname(LINE3D), Transaction.AUTO_COMMIT);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(newFeature));

        // retrieve it back
        SimpleFeatureIterator fi = fs.getFeatures(FF.id(new HashSet<FeatureId>(fids)))
                .features();
        assertTrue(fi.hasNext());
        SimpleFeature f = fi.next();
        assertTrue(ls.equals((Geometry) f.getDefaultGeometry()));
        fi.close();
    }

    /**
     * Creates the polygon schema and then inserts a 3D geometry into the
     * datastore and retrieves it back to make sure 3d data is really handled as
     * such
     * 
     * @throws Exception
     */
    public void testCreateSchemaAndInsert() throws Exception {
        dataStore.createSchema(poly3DType);
        SimpleFeatureType actualSchema = dataStore.getSchema(tname(POLY3D));
        assertFeatureTypesEqual(poly3DType, actualSchema);
        assertEquals(getNativeSRID(), actualSchema.getGeometryDescriptor().getUserData().get(
                JDBCDataStore.JDBC_NATIVE_SRID));

        // build a 3d polygon (ordinates in ccw order)
        GeometryFactory gf = new GeometryFactory();
        LinearRing shell = gf.createLinearRing(new Coordinate[] { new Coordinate(0, 0, 0),
                new Coordinate(1, 1, 1), new Coordinate(1, 0, 1), new Coordinate(0, 0, 0) });
        Polygon poly = gf.createPolygon(shell, null);

        // insert it
        FeatureWriter<SimpleFeatureType, SimpleFeature> fw = dataStore.getFeatureWriterAppend(
                tname(POLY3D), Transaction.AUTO_COMMIT);
        SimpleFeature f = fw.next();
        f.setAttribute(aname(ID), 0);
        f.setAttribute(aname(GEOM), poly);
        f.setAttribute(aname(NAME), "3dpolygon!");
        fw.write();
        fw.close();

        // read id back and compare
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = dataStore.getFeatureReader(
                new DefaultQuery(tname(POLY3D)), Transaction.AUTO_COMMIT);
        assertTrue(fr.hasNext());
        f = fr.next();
        // this unfortunately checks only the first 2d, but at the same time
        // a coordinate by coordinate check is not possible since the ring orientation
        // can be modified by the store
        assertTrue(poly.equals((Geometry) f.getDefaultGeometry()));
        
        fr.close();
    }

    /**
     * Make sure we can properly retrieve the bounds of 3d layers
     * 
     * @throws Exception
     */
    public void testBounds() throws Exception {
        ReferencedEnvelope env = dataStore.getFeatureSource(tname(LINE3D)).getBounds();

        // check we got the right 2d component
        Envelope expected = new Envelope(1, 5, 0, 4);
        assertEquals(expected, env);

        // check the srs the expected one
        assertEquals(epsg4326, env.getCoordinateReferenceSystem());
    }

    // disabled as the liter coordinate sequence has still not been updated to support 3d data
    public void testRendererBehaviour() throws Exception {
        // make sure the hints are supported
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(LINE3D));
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_COORDINATE_SEQUENCE_FACTORY));

        // setup a query that mimicks the streaming renderer behaviour
        DefaultQuery q = new DefaultQuery(tname(LINE3D));
        Hints hints = new Hints(Hints.JTS_COORDINATE_SEQUENCE_FACTORY,
                new LiteCoordinateSequenceFactory());
        q.setHints(hints);

        // check the srs you get is the expected one 
         FeatureCollection fc = fs.getFeatures(q);
         FeatureType fcSchema = fc.getSchema();
        assertEquals(epsg4326, fcSchema.getCoordinateReferenceSystem());
         assertEquals(epsg4326, fcSchema.getGeometryDescriptor().getCoordinateReferenceSystem());

        // build up the reference 2d line, the 3d one is (1 1 0, 2 2 0, 4 2 1, 5
        // 1 1)
        LineString expected = new GeometryFactory().createLineString(new Coordinate[] {
                new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(4, 2),
                new Coordinate(5, 1) });

        // check feature reader and the schema
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT);
        assertEquals(epsg4326, fr.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(epsg4326, fr.getFeatureType().getGeometryDescriptor()
                .getCoordinateReferenceSystem());
        assertTrue(fr.hasNext());
        SimpleFeature f = fr.next();
        assertTrue(expected.equals((Geometry) f.getDefaultGeometry()));
        fr.close();
    }

}
