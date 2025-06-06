/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.geotools.TestData;
import org.geotools.api.data.DataStore;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.process.vector.IntersectionFeatureCollection.IntersectionMode;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

public class IntersectionFeatureCollectionTest {
    private static final Logger logger =
            Logger.getLogger("org.geotools.process.feature.gs.VectoralZonalStatisticalProcessTest");
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
    private DataStore data;
    private SimpleFeatureCollection zonesCollection;
    private SimpleFeatureCollection featuresCollection;
    private SimpleFeatureCollection polylineCollection;
    private SimpleFeatureCollection multipointCollection;
    private IntersectionFeatureCollection process;
    private static final double WORLDAREA = 510072000.0d;
    private static final double COLORADOAREA = 269837.0d;

    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null);
        data = new PropertyDataStore(file);
        zonesCollection = data.getFeatureSource("zones").getFeatures();
        featuresCollection = data.getFeatureSource("features").getFeatures();
        polylineCollection = data.getFeatureSource("polyline").getFeatures();
        multipointCollection = data.getFeatureSource("multipoint").getFeatures();
        process = new IntersectionFeatureCollection();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOpModeFstCollection() throws Exception {
        logger.info("Running testExceptionOpModeFstCollection ...");
        ArrayList<String> toRemoveFst = new ArrayList<>();
        toRemoveFst.add("cat2");
        ArrayList<String> toRemoveSnd = new ArrayList<>();
        toRemoveSnd.add("cat");
        process.execute(
                polylineCollection,
                featuresCollection,
                toRemoveFst,
                toRemoveSnd,
                IntersectionMode.INTERSECTION,
                true,
                false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExceptionOpModeSndCollection() throws Exception {
        logger.info("Running testExceptionOpModeSndCollection ...");
        ArrayList<String> toRetainFst = new ArrayList<>();
        toRetainFst.add("cat2");
        ArrayList<String> toRetainSnd = new ArrayList<>();
        toRetainSnd.add("cat");
        process.execute(
                zonesCollection,
                polylineCollection,
                toRetainFst,
                toRetainSnd,
                IntersectionMode.INTERSECTION,
                true,
                false);
    }

    private Polygon createRectangularPolygonByCoords(
            double xMin, double xMax, double yMin, double yMax, CoordinateReferenceSystem sourceCRS) {
        GeometryFactory geomFactory = new GeometryFactory();

        // creates the  polygon
        Coordinate[] tempCoordinates = new Coordinate[5];
        tempCoordinates[0] = new Coordinate(xMin, yMin);
        tempCoordinates[1] = new Coordinate(xMax, yMin);
        tempCoordinates[2] = new Coordinate(xMax, yMax);
        tempCoordinates[3] = new Coordinate(xMin, yMax);
        tempCoordinates[4] = tempCoordinates[0];
        LinearRing linearRing = geomFactory.createLinearRing(tempCoordinates);
        Polygon polygon = geomFactory.createPolygon(linearRing, null);
        return polygon;
    }

    // this test verifies if the Illegal argument exception is thrown when a MultiPointCollection is
    // given as first collection
    @Test(expected = IllegalArgumentException.class)
    public void testProcessArguments1() throws IllegalArgumentException {
        process.execute(
                multipointCollection, featuresCollection, null, null, IntersectionMode.INTERSECTION, null, null);
    }

    // this test verifies if the Illegal argument exception is thrown when a MultiPointCollection is
    // given as second collection and area attributes are required
    @Test(expected = IllegalArgumentException.class)
    public void testProcessArguments2() throws IllegalArgumentException {
        process.execute(
                featuresCollection, multipointCollection, null, null, IntersectionMode.INTERSECTION, true, false);
    }

    @Test
    public void testGetIntersectionAreaRate() {

        logger.info("Running testGetIntersectionAreaRate ...");
        CoordinateReferenceSystem sourceCRS = org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

        // creates the world state polygon
        Polygon worldPolygon = createRectangularPolygonByCoords(-180, 180, -90, 90, sourceCRS);

        // creates the Colorado state polygon
        Polygon coloradoPolygon = createRectangularPolygonByCoords(-102, -109, 37, 41, sourceCRS);

        // calculates the estimated value
        double calculatedRate = IntersectionFeatureCollection.getIntersectionArea(
                worldPolygon, sourceCRS, coloradoPolygon, sourceCRS, true);

        // calculates the expected value
        double expectedRate = COLORADOAREA / WORLDAREA;

        // 0.01% error off the expected value
        assertEquals(0, (expectedRate - calculatedRate) / expectedRate, 0.01);
    }

    @Test
    public void testReturnedAttributes() throws Exception {
        logger.info("Running testReturnedAttributes ...");
        ArrayList<String> toRetainFst = new ArrayList<>();
        toRetainFst.add("str1");
        ArrayList<String> toRetainSnd = new ArrayList<>();
        toRetainSnd.add("str2");
        SimpleFeatureCollection output2 = process.execute(
                zonesCollection,
                featuresCollection,
                toRetainFst,
                toRetainSnd,
                IntersectionMode.INTERSECTION,
                true,
                true);

        // System.out.println("count " + output2.getSchema().getAttributeCount());
        assertNotNull(output2.getSchema().getDescriptor("the_geom"));
        assertNotNull(output2.getSchema().getDescriptor("zones_str1"));
        assertNotNull(output2.getSchema().getDescriptor("features_str2"));
        assertNotNull(output2.getSchema().getDescriptor("percentageA"));
        assertNotNull(output2.getSchema().getDescriptor("percentageB"));
        assertNotNull(output2.getSchema().getDescriptor("areaA"));
        assertNotNull(output2.getSchema().getDescriptor("areaB"));
        assertNotNull(output2.getSchema().getDescriptor("INTERSECTION_ID"));
        assertEquals(8, output2.getSchema().getAttributeCount());
        SimpleFeature sf = DataUtilities.first(output2);

        // test with both area and percentage attributes
        assertNotNull(sf.getAttribute("the_geom"));
        assertNotNull(sf.getAttribute("zones_str1"));
        assertNotNull(sf.getAttribute("features_str2"));
        assertNotNull(sf.getAttribute("percentageA"));
        assertNotNull(sf.getAttribute("percentageB"));
        assertNotNull(sf.getAttribute("areaA"));
        assertNotNull(sf.getAttribute("areaB"));
        assertEquals(8, sf.getAttributeCount());

        // test without area and percentageAttributes
        SimpleFeatureCollection output3 = process.execute(
                zonesCollection,
                featuresCollection,
                toRetainFst,
                toRetainSnd,
                IntersectionMode.INTERSECTION,
                false,
                false);
        SimpleFeature sf2 = DataUtilities.first(output3);
        assertNotNull(sf2.getAttribute("the_geom"));
        assertNotNull(sf2.getAttribute("zones_str1"));
        assertNotNull(sf2.getAttribute("features_str2"));
        assertEquals(4, sf2.getAttributeCount());
    }

    @Test
    public void testExecute() throws Exception {

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Polygon.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        DefaultFeatureCollection secondFeatures = new DefaultFeatureCollection(null, b.getFeatureType());
        Polygon[] firstArrayGeometry = new Polygon[1];
        Polygon[] secondArrayGeometry = new Polygon[1];
        for (int numFeatures = 0; numFeatures < 1; numFeatures++) {
            Coordinate[] array = new Coordinate[5];
            array[0] = new Coordinate(0, 0);
            array[1] = new Coordinate(1, 0);
            array[2] = new Coordinate(1, 1);
            array[3] = new Coordinate(0, 1);
            array[4] = new Coordinate(0, 0);
            LinearRing shell = gf.createLinearRing(new CoordinateArraySequence(array));
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            firstArrayGeometry[0] = gf.createPolygon(shell, null);
            features.add(b.buildFeature(numFeatures + ""));
        }
        for (int numFeatures = 0; numFeatures < 1; numFeatures++) {
            Coordinate[] array = new Coordinate[5];
            Coordinate centre = ((Polygon) features.features().next().getDefaultGeometry())
                    .getCentroid()
                    .getCoordinate();
            array[0] = new Coordinate(centre.x, centre.y);
            array[1] = new Coordinate(centre.x + 1, centre.y);
            array[2] = new Coordinate(centre.x + 1, centre.y + 1);
            array[3] = new Coordinate(centre.x, centre.y + 1);
            array[4] = new Coordinate(centre.x, centre.y);
            LinearRing shell = gf.createLinearRing(new CoordinateArraySequence(array));
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            secondArrayGeometry[0] = gf.createPolygon(shell, null);
            secondFeatures.add(b.buildFeature(numFeatures + ""));
        }

        SimpleFeatureCollection output3 = process.execute(features, secondFeatures, null, null, null, false, false);

        assertEquals(1, output3.size());

        GeometryCollection firstCollection = new GeometryCollection(firstArrayGeometry, new GeometryFactory());
        GeometryCollection secondCollection = new GeometryCollection(secondArrayGeometry, new GeometryFactory());
        try (SimpleFeatureIterator iterator = output3.features()) {
            for (int i = 0; i < firstCollection.getNumGeometries() && iterator.hasNext(); i++) {
                Geometry expected = firstCollection.getGeometryN(i).intersection(secondCollection.getGeometryN(i));
                SimpleFeature sf = iterator.next();
                // geometry.equals(geometry) behaves differently than geometry.equals(object)
                assertTrue(expected.equals((Geometry) sf.getDefaultGeometry()));
            }
        }
    }

    @Test
    public void testPointInPolygonReturnsPoint() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("featureType");
        tb.add("geometry", Polygon.class);
        tb.add("integer", Integer.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());

        DefaultFeatureCollection features = new DefaultFeatureCollection(null, b.getFeatureType());
        Polygon[] firstArrayGeometry = new Polygon[1];

        for (int numFeatures = 0; numFeatures < 1; numFeatures++) {
            Coordinate[] array = new Coordinate[5];
            array[0] = new Coordinate(0, 0);
            array[1] = new Coordinate(1, 0);
            array[2] = new Coordinate(1, 1);
            array[3] = new Coordinate(0, 1);
            array[4] = new Coordinate(0, 0);
            LinearRing shell = gf.createLinearRing(new CoordinateArraySequence(array));
            b.add(gf.createPolygon(shell, null));
            b.add(0);
            firstArrayGeometry[0] = gf.createPolygon(shell, null);
            features.add(b.buildFeature(numFeatures + ""));
        }

        SimpleFeatureTypeBuilder pointFeatureTypeBuilder = new SimpleFeatureTypeBuilder();
        pointFeatureTypeBuilder.setName("pointFeatureType");
        pointFeatureTypeBuilder.add("geometry", Point.class);
        pointFeatureTypeBuilder.add("integer", Integer.class);

        SimpleFeatureBuilder pointFeatureBuilder = new SimpleFeatureBuilder(pointFeatureTypeBuilder.buildFeatureType());
        DefaultFeatureCollection pointFeatureCollection =
                new DefaultFeatureCollection(null, pointFeatureBuilder.getFeatureType());
        Point point = gf.createPoint(new Coordinate(0.5, 0.5));
        pointFeatureCollection.add(pointFeatureBuilder.buildFeature("1", new Object[] {point}));

        SimpleFeatureCollection output3 =
                process.execute(features, pointFeatureCollection, null, null, null, false, false);

        assertEquals(1, output3.size());
        Point pointOut = (Point) output3.features().next().getDefaultGeometry();
        assertEquals(0.5, pointOut.getX(), 0.01);
        assertEquals(
                "GeometryTypeImpl the_geom<Point>",
                ((IntersectionFeatureCollection.IntersectedFeatureCollection) output3)
                        .geomType
                        .getType()
                        .toString());
    }
}
