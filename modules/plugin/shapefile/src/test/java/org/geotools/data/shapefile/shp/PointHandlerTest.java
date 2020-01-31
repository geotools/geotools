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
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

/** @author ian */
public class PointHandlerTest {

    /**
     * Test method for {@link org.geotools.data.shapefile.shp.PointHandler#read(java.nio.ByteBuffer,
     * org.geotools.data.shapefile.shp.ShapeType, boolean)}.
     *
     * @throws IOException
     * @throws ShapefileException
     */
    @Test
    public void testReadMZ() throws ShapefileException, IOException {
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
    public void testReadM() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/mpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);

        assertEquals("wrong m", 20, geom.getCoordinate().getM(), 0.00001);
    }

    @Test
    public void testReadZ() throws ShapefileException, IOException {
        URL url = TestData.url(ShapefileDataStore.class, "mzvalues/zpoints.shp");
        ShapefileDataStore store = new ShapefileDataStore(url);
        SimpleFeature feature = DataUtilities.first(store.getFeatureSource().getFeatures());
        Geometry geom = (Geometry) feature.getDefaultGeometry();

        assertEquals("wrong x", 10, geom.getCoordinate().getX(), 0.00001);
        assertEquals("wrong y", 5, geom.getCoordinate().getY(), 0.00001);
        assertEquals("wrong z", 1, geom.getCoordinate().getZ(), 0.00001);
    }
    /**
     * Test method for {@link
     * org.geotools.data.shapefile.shp.PointHandler#write(java.nio.ByteBuffer, java.lang.Object)}.
     */
    @Test
    public void testWrite() {}
}
