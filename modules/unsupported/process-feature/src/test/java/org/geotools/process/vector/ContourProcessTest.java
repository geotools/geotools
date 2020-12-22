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
package org.geotools.process.vector;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.FileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.util.ProgressListener;

/** @author ian */
public class ContourProcessTest {

    private FeatureCollection features;

    @Before
    public void setup() throws IOException {
        URL url = TestData.getResource(this, "heights-0.shp");
        ShapefileDataStoreFactory fac = new ShapefileDataStoreFactory();
        FileDataStore ds = fac.createDataStore(url);
        features = ds.getFeatureSource().getFeatures();
    }

    @Test
    public void testSimplePoints() {
        ReferencedEnvelope bounds =
                new ReferencedEnvelope(0, 30, 0, 30, DefaultGeographicCRS.WGS84);
        Coordinate[] data =
                new Coordinate[] {
                    new Coordinate(10, 10, 100),
                    new Coordinate(10, 20, 20),
                    new Coordinate(20, 10, 0),
                    new Coordinate(20, 20, 80)
                };
        SimpleFeatureCollection fc = ProcessTestUtilities.createPoints(data, bounds);
        ContourProcess cp = new ContourProcess();
        double[] levels = new double[] {};
        double interval = 10;
        Boolean simplify = Boolean.TRUE;
        Boolean smooth = Boolean.TRUE;
        ProgressListener progressListener = null;
        SimpleFeatureCollection results =
                cp.execute(fc, "value", levels, interval, simplify, smooth, progressListener);
    }

    @Test
    public void testLevels() {
        ContourProcess cp = new ContourProcess();
        double[] levels = new double[] {10, 20, 30};
        double interval = 0;
        Boolean simplify = Boolean.TRUE;
        Boolean smooth = Boolean.TRUE;
        ProgressListener progressListener = null;
        SimpleFeatureCollection results =
                cp.execute(
                        features,
                        "propertyVa",
                        levels,
                        interval,
                        simplify,
                        smooth,
                        progressListener);
    }
}
