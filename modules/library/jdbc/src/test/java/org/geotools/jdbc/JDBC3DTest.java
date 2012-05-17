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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequence;
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
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
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
 * @author Martin Davis - OpenGeo
 * 
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
        assertEquals(getNativeSRID(),
                schema.getGeometryDescriptor().getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID));
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
        SimpleFeatureStore fs = (SimpleFeatureStore) dataStore.getFeatureSource(tname(LINE3D),
                Transaction.AUTO_COMMIT);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(newFeature));

        // retrieve it back
        SimpleFeatureIterator fi = fs.getFeatures(FF.id(new HashSet<FeatureId>(fids))).features();
        assertTrue(fi.hasNext());
        SimpleFeature f = fi.next();
        assertTrue(ls.equalsExact((Geometry) f.getDefaultGeometry()));
        fi.close();
    }

    public void testCreateSchemaAndInsertPolyTriangle() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell = gf.createLinearRing(csf.create(new double[] { 0, 0, 99, 1, 0, 33, 1, 1,
                66, 0, 0, 99 }, 3));
        Polygon poly = gf.createPolygon(shell, null);

        checkCreateSchemaAndInsert(poly);
    }

    public void testCreateSchemaAndInsertPolyRectangle() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell = gf.createLinearRing(csf.create(new double[] { 0, 0, 99, 1, 0, 33, 1, 1,
                66, 0, 1, 33, 0, 0, 99 }, 3));
        Polygon poly = gf.createPolygon(shell, null);

        checkCreateSchemaAndInsert(poly);
    }

    public void testCreateSchemaAndInsertPolyRectangleWithHole() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell = gf.createLinearRing(csf.create(new double[] { 0, 0, 99, 10, 0, 33, 10,
                10, 66, 0, 10, 66, 0, 0, 99 }, 3));
        LinearRing hole = gf.createLinearRing(csf.create(new double[] { 2, 2, 99, 3, 2, 44, 3, 3,
                99, 2, 3, 99, 2, 2, 99 }, 3));
        Polygon poly = gf.createPolygon(shell, new LinearRing[] { hole });

        checkCreateSchemaAndInsert(poly);
    }

    public void testCreateSchemaAndInsertPolyWithHoleCW() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell = gf.createLinearRing(csf.create(new double[] { 1, 1, 99, 10, 1, 33, 
                10, 10, 66,    1, 10, 66,       1, 1, 99 }, 3));
        LinearRing hole = gf.createLinearRing(csf.create(new double[] { 2, 2, 99, 8, 2, 44, 8, 8,
                99, 2, 8, 99, 2, 2, 99 }, 3));
        Polygon poly = gf.createPolygon(shell, new LinearRing[] { hole });

        checkCreateSchemaAndInsert(poly);
    }

    /**
     * Creates the polygon schema, inserts a 3D geometry into the datastore,
     * and retrieves it back to make sure 3d data is preserved.
     * 
     * @throws Exception
     */
    private void checkCreateSchemaAndInsert(Geometry poly) throws Exception {
        dataStore.createSchema(poly3DType);
        SimpleFeatureType actualSchema = dataStore.getSchema(tname(POLY3D));
        assertFeatureTypesEqual(poly3DType, actualSchema);
        assertEquals(
                getNativeSRID(),
                actualSchema.getGeometryDescriptor().getUserData()
                        .get(JDBCDataStore.JDBC_NATIVE_SRID));

        // insert the feature
        FeatureWriter<SimpleFeatureType, SimpleFeature> fw = dataStore.getFeatureWriterAppend(
                tname(POLY3D), Transaction.AUTO_COMMIT);
        SimpleFeature f = fw.next();
        f.setAttribute(aname(ID), 0);
        f.setAttribute(aname(GEOM), poly);
        f.setAttribute(aname(NAME), "3dpolygon!");
        fw.write();
        fw.close();

        // read feature back
        
        /**
         * Use a LiteCoordinateSequence, since this mimics GeoServer behaviour better,
         * and it exposes bugs in CoordinateSequence handling.
         */
        final Hints hints = new Hints();
        hints.put(Hints.JTS_COORDINATE_SEQUENCE_FACTORY, new LiteCoordinateSequenceFactory());
        Query query = new DefaultQuery(tname(POLY3D));
        query.setHints(hints);
        
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = dataStore.getFeatureReader(
                query, Transaction.AUTO_COMMIT);
        assertTrue(fr.hasNext());
        f = fr.next();

        /**
         * Check the geometries are topologically equal.
         * Check that the Z values are preserved
         */
        Geometry fgeom = (Geometry) f.getDefaultGeometry();
        assertTrue("2D topology does not match", poly.equalsTopo(fgeom));
        assertTrue("Z values do not match", hasMatchingZValues(poly, fgeom));
        fr.close();
    }

    /**
     * Tests whether two geometries have the same Z values for coordinates with identical 2D locations. Requires that each geometry is internally
     * location-consistent in Z; that is, if two coordinates are identical in location, then the Z values are equal. This should always be the case
     * for valid data.
     * 
     * @param g1
     * @param g2
     * @return true if the geometries are location-equal in Z
     */
    private static boolean hasMatchingZValues(Geometry g1, Geometry g2) {
        Coordinate[] pt1 = g1.getCoordinates();
        Map<Coordinate, Double> coordZMap = new HashMap<Coordinate, Double>();
        for (int i = 0; i < pt1.length; i++) {
            coordZMap.put(pt1[i], pt1[i].z);
        }

        Coordinate[] pt2 = g2.getCoordinates();

        for (int i2 = 0; i2 < pt2.length; i2++) {
            Coordinate p2 = pt2[i2];
            double z = coordZMap.get(p2);
            boolean isEqualZ = p2.z == z || (Double.isNaN(p2.z) && Double.isNaN(z));
            if (!isEqualZ)
                return false;
        }

        return true;
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
        FeatureReader<SimpleFeatureType, SimpleFeature> fr = dataStore.getFeatureReader(q,
                Transaction.AUTO_COMMIT);
        assertEquals(epsg4326, fr.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(epsg4326, fr.getFeatureType().getGeometryDescriptor()
                .getCoordinateReferenceSystem());
        assertTrue(fr.hasNext());
        SimpleFeature f = fr.next();
        assertTrue(expected.equalsExact((Geometry) f.getDefaultGeometry()));
        fr.close();
    }

}
