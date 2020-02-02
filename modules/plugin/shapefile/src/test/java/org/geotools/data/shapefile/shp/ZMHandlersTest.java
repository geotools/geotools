/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.shp;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.test.TestData;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;

/** @author ian */
public class ZMHandlersTest {

    /**
     * Test method for {@link org.geotools.data.shapefile.shp.PointHandler#read(java.nio.ByteBuffer,
     * org.geotools.data.shapefile.shp.ShapeType, boolean)}.
     *
     * @throws IOException
     * @throws ShapefileException
     */
    @Test
    public void testReadMZPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);
        assertEquals("wrong z", 1, geom.getCoordinate().getZ(), 0.00001);
        assertEquals("wrong m", 20, geom.getCoordinate().getM(), 0.00001);
    }

    @Test
    public void testReadMPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);

        assertEquals("wrong m", 20, geom.getCoordinate().getM(), 0.00001);
    }

    @Test
    public void testReadZPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();

        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);
        assertEquals("wrong z", 1, geom.getCoordinate().getZ(), 0.00001);
    }

    @Test
    public void testReadMZMultiPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmmultipoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPoint geom = (MultiPoint) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 1208.5983, coordinate.getX(), 0.001);
        assertEquals("wrong y", 924.8555, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
    }

    @Test
    public void testReadZMultiPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmmultipoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPoint geom = (MultiPoint) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 1208.5983, coordinate.getX(), 0.001);
        assertEquals("wrong y", 924.8555, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
    }

    @Test
    public void testReadMMultiPoints() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zmmultipoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPoint geom = (MultiPoint) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 1208.5983, coordinate.getX(), 0.001);
        assertEquals("wrong y", 924.8555, coordinate.getY(), 0.001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
    }

    @Test
    public void testReadMZLine() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mzlines.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 589.4648, coordinate.getX(), 0.001);
        assertEquals("wrong y", 909.9963, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
    }

    @Test
    public void testReadMLine() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mlines.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 589.4648, coordinate.getX(), 0.001);
        assertEquals("wrong y", 909.9963, coordinate.getY(), 0.001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
    }

    @Test
    public void testReadZLine() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zlines.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiLineString geom = (MultiLineString) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 589.4648, coordinate.getX(), 0.001);
        assertEquals("wrong y", 909.9963, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
    }

    @Test
    public void testReadMZPolygon() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mzpolygons.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 96.944404, coordinate.getX(), 0.001);
        assertEquals("wrong y", 653.67509, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
    }

    @Test
    public void testReadMPolygon() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mpolygons.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 96.944404, coordinate.getX(), 0.001);
        assertEquals("wrong y", 653.67509, coordinate.getY(), 0.001);
        assertEquals("wrong m", 10, coordinate.getM(), 0.00001);
    }

    @Test
    public void testReadZPolygon() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zpolygons.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        MultiPolygon geom = (MultiPolygon) feature.getDefaultGeometry();
        Coordinate coordinate = geom.getCoordinates()[0];

        assertEquals("wrong x", 96.944404, coordinate.getX(), 0.001);
        assertEquals("wrong y", 653.67509, coordinate.getY(), 0.001);
        assertEquals("wrong z", 20, coordinate.getZ(), 0.00001);
    }
}
