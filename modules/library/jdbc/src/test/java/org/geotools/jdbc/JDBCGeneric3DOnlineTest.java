/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope3D;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.spatial.BBOX3D;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests the ability of the datastore to cope with 3D data
 *
 * @author Andrea Aime - OpenGeo
 * @author Martin Davis - OpenGeo
 */
public abstract class JDBCGeneric3DOnlineTest extends JDBCTestSupport {

    protected static final String ID = "id";

    protected static final String GEOM = "geom";

    protected static final String NAME = "name";

    protected static final FilterFactory FF = CommonFactoryFinder.getFilterFactory(null);

    protected SimpleFeatureType poly3DType;

    protected SimpleFeatureType line3DType;

    protected CoordinateReferenceSystem crs;

    /** Returns the name of the feature type with 3d lines */
    protected abstract String getLine3d();

    /** Returns the name of the feature type with 3d points */
    protected abstract String getPoint3d();

    /** Returns the name of the feature type with 3d polygons */
    protected abstract String getPoly3d();

    @Override
    protected void connect() throws Exception {
        super.connect();

        line3DType =
                DataUtilities.createType(
                        dataStore.getNamespaceURI() + "." + tname(getLine3d()),
                        aname(ID)
                                + ":0,"
                                + aname(GEOM)
                                + ":LineString:srid="
                                + getEpsgCode()
                                + ","
                                + aname(NAME)
                                + ":String");
        line3DType.getGeometryDescriptor().getUserData().put(Hints.COORDINATE_DIMENSION, 3);
        poly3DType =
                DataUtilities.createType(
                        dataStore.getNamespaceURI() + "." + tname(getPoly3d()),
                        aname(ID)
                                + ":0,"
                                + aname(GEOM)
                                + ":Polygon:srid="
                                + getEpsgCode()
                                + ","
                                + aname(NAME)
                                + ":String");
        poly3DType.getGeometryDescriptor().getUserData().put(Hints.COORDINATE_DIMENSION, 3);

        crs = CRS.decode("EPSG:" + getEpsgCode());
    }

    protected Integer getNativeSRID() {
        return Integer.valueOf(getEpsgCode());
    }

    protected abstract int getEpsgCode();

    public void testSchema() throws Exception {
        SimpleFeatureType schema = dataStore.getSchema(tname(getLine3d()));
        CoordinateReferenceSystem crs =
                schema.getGeometryDescriptor().getCoordinateReferenceSystem();
        assertEquals(Integer.valueOf(getEpsgCode()), CRS.lookupEpsgCode(crs, false));
        assertEquals(
                getNativeSRID(),
                schema.getGeometryDescriptor().getUserData().get(JDBCDataStore.JDBC_NATIVE_SRID));
        assertEquals(
                3, schema.getGeometryDescriptor().getUserData().get(Hints.COORDINATE_DIMENSION));
    }

    public void testReadPoint() throws Exception {
        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(getPoint3d())).getFeatures();
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            Point p = (Point) fr.next().getDefaultGeometry();
            assertTrue(new Coordinate(1, 1, 1).equals(p.getCoordinate()));
        }
    }

    public void testReadLine() throws Exception {
        SimpleFeatureCollection fc = dataStore.getFeatureSource(tname(getLine3d())).getFeatures();
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            LineString ls = (LineString) fr.next().getDefaultGeometry();
            // 1 1 0, 2 2 0, 4 2 1, 5 1 1
            assertEquals(4, ls.getCoordinates().length);
            assertTrue(new Coordinate(1, 1, 0).equals3D(ls.getCoordinateN(0)));
            assertTrue(new Coordinate(2, 2, 0).equals3D(ls.getCoordinateN(1)));
            assertTrue(new Coordinate(4, 2, 1).equals3D(ls.getCoordinateN(2)));
            assertTrue(new Coordinate(5, 1, 1).equals3D(ls.getCoordinateN(3)));
        }
    }

    public void testBBOX3DReadLine() throws Exception {
        BBOX3D bbox3d = FF.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 0, 1, crs));
        SimpleFeatureCollection fc =
                dataStore.getFeatureSource(tname(getLine3d())).getFeatures(bbox3d);
        try (SimpleFeatureIterator fr = fc.features()) {
            assertTrue(fr.hasNext());
            LineString ls = (LineString) fr.next().getDefaultGeometry();
            // 1 1 0, 2 2 0, 4 2 1, 5 1 1
            assertEquals(4, ls.getCoordinates().length);
            assertTrue(new Coordinate(1, 1, 0).equals3D(ls.getCoordinateN(0)));
            assertTrue(new Coordinate(2, 2, 0).equals3D(ls.getCoordinateN(1)));
            assertTrue(new Coordinate(4, 2, 1).equals3D(ls.getCoordinateN(2)));
            assertTrue(new Coordinate(5, 1, 1).equals3D(ls.getCoordinateN(3)));
        }
    }

    public void testBBOX3DOutsideLine() throws Exception {
        // a bbox 3d well outside the line footprint
        BBOX3D bbox3d = FF.bbox("", new ReferencedEnvelope3D(2, 3, 1, 2, 100, 101, crs));
        SimpleFeatureCollection fc =
                dataStore.getFeatureSource(tname(getLine3d())).getFeatures(bbox3d);
        assertEquals(0, fc.size());
    }

    public void testWriteLine() throws Exception {
        // build a 3d line
        GeometryFactory gf = new GeometryFactory();
        LineString ls =
                gf.createLineString(
                        new Coordinate[] {new Coordinate(0, 0, 0), new Coordinate(1, 1, 1)});

        // build a feature around it
        SimpleFeature newFeature =
                SimpleFeatureBuilder.build(line3DType, new Object[] {2, ls, "l3"}, null);

        // insert it
        SimpleFeatureStore fs =
                (SimpleFeatureStore)
                        dataStore.getFeatureSource(tname(getLine3d()), Transaction.AUTO_COMMIT);
        List<FeatureId> fids = fs.addFeatures(DataUtilities.collection(newFeature));

        // retrieve it back
        try (SimpleFeatureIterator fi =
                fs.getFeatures(FF.id(new HashSet<FeatureId>(fids))).features()) {
            assertTrue(fi.hasNext());
            SimpleFeature f = fi.next();
            assertTrue(ls.equalsExact((Geometry) f.getDefaultGeometry()));
        }
    }

    public void testCreateSchemaAndInsertPolyTriangle() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell =
                gf.createLinearRing(
                        csf.create(new double[] {0, 0, 99, 1, 0, 33, 1, 1, 66, 0, 0, 99}, 3));
        Polygon poly = gf.createPolygon(shell, null);

        checkCreateSchemaAndInsert(poly);
    }

    public void testCreateSchemaAndInsertPolyRectangle() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell =
                gf.createLinearRing(
                        csf.create(
                                new double[] {0, 0, 99, 1, 0, 33, 1, 1, 66, 0, 1, 33, 0, 0, 99},
                                3));
        Polygon poly = gf.createPolygon(shell, null);

        checkCreateSchemaAndInsert(poly);
    }

    public void testCreateSchemaAndInsertPolyRectangleWithHole() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell =
                gf.createLinearRing(
                        csf.create(
                                new double[] {0, 0, 99, 10, 0, 33, 10, 10, 66, 0, 10, 66, 0, 0, 99},
                                3));
        LinearRing hole =
                gf.createLinearRing(
                        csf.create(
                                new double[] {2, 2, 99, 3, 2, 44, 3, 3, 99, 2, 3, 99, 2, 2, 99},
                                3));
        Polygon poly = gf.createPolygon(shell, new LinearRing[] {hole});

        checkCreateSchemaAndInsert(poly);
    }

    public void testCreateSchemaAndInsertPolyWithHoleCW() throws Exception {
        LiteCoordinateSequenceFactory csf = new LiteCoordinateSequenceFactory();
        GeometryFactory gf = new GeometryFactory(csf);

        LinearRing shell =
                gf.createLinearRing(
                        csf.create(
                                new double[] {1, 1, 99, 10, 1, 33, 10, 10, 66, 1, 10, 66, 1, 1, 99},
                                3));
        LinearRing hole =
                gf.createLinearRing(
                        csf.create(
                                new double[] {2, 2, 99, 8, 2, 44, 8, 8, 99, 2, 8, 99, 2, 2, 99},
                                3));
        Polygon poly = gf.createPolygon(shell, new LinearRing[] {hole});

        checkCreateSchemaAndInsert(poly);
    }

    /**
     * Creates the polygon schema, inserts a 3D geometry into the datastore, and retrieves it back
     * to make sure 3d data is preserved.
     */
    private void checkCreateSchemaAndInsert(Geometry poly) throws Exception {
        dataStore.createSchema(poly3DType);
        SimpleFeatureType actualSchema = dataStore.getSchema(tname(getPoly3d()));
        assertFeatureTypesEqual(poly3DType, actualSchema);
        assertEquals(
                getNativeSRID(),
                actualSchema
                        .getGeometryDescriptor()
                        .getUserData()
                        .get(JDBCDataStore.JDBC_NATIVE_SRID));

        // insert the feature
        try (FeatureWriter<SimpleFeatureType, SimpleFeature> fw =
                dataStore.getFeatureWriterAppend(tname(getPoly3d()), Transaction.AUTO_COMMIT)) {
            SimpleFeature f = fw.next();
            f.setAttribute(aname(ID), 0);
            f.setAttribute(aname(GEOM), poly);
            f.setAttribute(aname(NAME), "3dpolygon!");
            fw.write();
        }

        // read feature back

        /**
         * Use a LiteCoordinateSequence, since this mimics GeoServer behaviour better, and it
         * exposes bugs in CoordinateSequence handling.
         */
        final Hints hints = new Hints();
        hints.put(Hints.JTS_COORDINATE_SEQUENCE_FACTORY, new LiteCoordinateSequenceFactory());
        Query query = new Query(tname(getPoly3d()));
        query.setHints(hints);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertTrue(fr.hasNext());
            SimpleFeature f = fr.next();

            /**
             * Check the geometries are topologically equal. Check that the Z values are preserved
             */
            Geometry fgeom = (Geometry) f.getDefaultGeometry();
            assertTrue("2D topology does not match", poly.equalsTopo(fgeom));
            assertTrue("Z values do not match", hasMatchingZValues(poly, fgeom));
        }
    }

    /**
     * Tests whether two geometries have the same Z values for coordinates with identical 2D
     * locations. Requires that each geometry is internally location-consistent in Z; that is, if
     * two coordinates are identical in location, then the Z values are equal. This should always be
     * the case for valid data.
     *
     * @return true if the geometries are location-equal in Z
     */
    private static boolean hasMatchingZValues(Geometry g1, Geometry g2) {
        Coordinate[] pt1 = g1.getCoordinates();
        Map<Coordinate, Double> coordZMap = new HashMap<Coordinate, Double>();
        for (int i = 0; i < pt1.length; i++) {
            coordZMap.put(pt1[i], pt1[i].getZ());
        }

        Coordinate[] pt2 = g2.getCoordinates();

        for (int i2 = 0; i2 < pt2.length; i2++) {
            Coordinate p2 = pt2[i2];
            double z = coordZMap.get(p2);
            boolean isEqualZ = p2.getZ() == z || (Double.isNaN(p2.getZ()) && Double.isNaN(z));
            if (!isEqualZ) return false;
        }

        return true;
    }

    /** Make sure we can properly retrieve the bounds of 3d layers */
    public void testBounds() throws Exception {
        ReferencedEnvelope env = dataStore.getFeatureSource(tname(getLine3d())).getBounds();

        // check we got the right 2d component
        Envelope expected = new Envelope(1, 5, 0, 4);
        assertEquals(expected, env);

        // check the srs the expected one
        assertEquals(CRS.getHorizontalCRS(crs), env.getCoordinateReferenceSystem());
    }

    // disabled as the liter coordinate sequence has still not been updated to support 3d data
    public void testRendererBehaviour() throws Exception {
        // make sure the hints are supported
        ContentFeatureSource fs = dataStore.getFeatureSource(tname(getLine3d()));
        assertTrue(fs.getSupportedHints().contains(Hints.JTS_COORDINATE_SEQUENCE_FACTORY));

        // setup a query that mimicks the streaming renderer behaviour
        Query q = new Query(tname(getLine3d()));
        Hints hints =
                new Hints(
                        Hints.JTS_COORDINATE_SEQUENCE_FACTORY, new LiteCoordinateSequenceFactory());
        q.setHints(hints);

        // check the srs you get is the expected one
        FeatureCollection fc = fs.getFeatures(q);
        FeatureType fcSchema = fc.getSchema();
        assertEquals(crs, fcSchema.getCoordinateReferenceSystem());
        assertEquals(crs, fcSchema.getGeometryDescriptor().getCoordinateReferenceSystem());

        // build up the reference 2d line, the 3d one is (1 1 0, 2 2 0, 4 2 1, 5
        // 1 1)
        LineString expected =
                new GeometryFactory()
                        .createLineString(
                                new Coordinate[] {
                                    new Coordinate(1, 1),
                                    new Coordinate(2, 2),
                                    new Coordinate(4, 2),
                                    new Coordinate(5, 1)
                                });

        // check feature reader and the schema
        try (FeatureReader<SimpleFeatureType, SimpleFeature> fr =
                dataStore.getFeatureReader(q, Transaction.AUTO_COMMIT)) {
            assertEquals(crs, fr.getFeatureType().getCoordinateReferenceSystem());
            assertEquals(
                    crs,
                    fr.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
            assertTrue(fr.hasNext());
            SimpleFeature f = fr.next();
            assertTrue(expected.equalsExact((Geometry) f.getDefaultGeometry()));
        }
    }
}
