/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import static org.junit.Assert.assertArrayEquals;

import java.util.Properties;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;

/** Tests if the CRS utility class is functioning correctly when using HSQL datastore. */
public class HSQLCRSTest extends AbstractCRSTest {

    @Override
    protected String getFixtureId() {
        return "hsql";
    }

    @Override
    protected Properties createOfflineFixture() {
        return new Properties();
    }

    @Override
    protected boolean supportsED50QuickScan() {
        return true;
    }

    @Test
    public void testMercatorProjectionTolerance()
            throws NoSuchAuthorityCodeException, FactoryException, MismatchedDimensionException, TransformException {
        CoordinateReferenceSystem targetCrs = CRS.decode("EPSG:25831");
        // System.out.println(targetCrs.getDomainOfValidity());
        MathTransform mathTransform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, targetCrs, true);
        Position2D position2D = new Position2D(DefaultGeographicCRS.WGS84, 0.1, 39);
        Position2D position2Dres = new Position2D();
        mathTransform.transform(position2D, position2Dres);
    }

    @Test
    public void testSouthPolarEastNorth() throws Exception {
        // force NE while decoding
        CoordinateReferenceSystem crsEN = CRS.decode("EPSG:32761", true);
        assertEquals(CRS.AxisOrder.EAST_NORTH, CRS.getAxisOrder(crsEN));
    }

    @Test
    public void testSouthPolarNorthEast() throws Exception {
        // leave native axis order, it should be recognized as north/east
        CoordinateReferenceSystem crsNE = CRS.decode("EPSG:32761", false);
        assertEquals(CRS.AxisOrder.NORTH_EAST, CRS.getAxisOrder(crsNE));
    }

    @Test
    public void testETRSPivot() throws Exception {
        CoordinateReferenceSystem source = CRS.decode("EPSG:31467");
        CoordinateReferenceSystem target = CRS.decode("EPSG:5683");
        MathTransform mtAccuracy = CRS.findMathTransform(source, target, true);

        double[] src = {3099840.7430828, 4949957.671010};
        double[] dst = new double[2];
        double[] projResult = {4949953.06, 3099840.28};

        // without pivoting the result is 150 meters away form the proj one,
        // with pivoting it's within 15 meters (proj favours large area operations over accurate ones)
        mtAccuracy.transform(src, 0, dst, 0, 1);
        assertArrayEquals(projResult, dst, 15);
    }
}
