/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2014, Open Source Geospatial Foundation (OSGeo)
 * (C) 2001-2014 TOPP - www.openplans.org.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.process.raster;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GridCoverageAngleCalcTest {

    private static final double TOLERANCE = 0.001d;

    @Test
    public void testLambertConic() throws Exception {
        //
        // Test some points within a custom Lambert Conformal Conic projection
        // used by the HRRR forecast model.
        //
        String wktString =
                "PROJCS[\"Lambert_Conformal_Conic\","
                        + "GEOGCS[\"GCS_unknown\",DATUM[\"D_unknown\","
                        + "SPHEROID[\"Sphere\",6367470,0]],PRIMEM[\"Greenwich\",0],"
                        + "UNIT[\"Degree\",0.017453292519943295]],"
                        + "PROJECTION[\"Lambert_Conformal_Conic_1SP\"],"
                        + "PARAMETER[\"latitude_of_origin\",38.5],"
                        + "PARAMETER[\"central_meridian\",-97.5],"
                        + "PARAMETER[\"scale_factor\",1],"
                        + "PARAMETER[\"false_easting\",0],"
                        + "PARAMETER[\"false_northing\",0],UNIT[\"m\",1.0]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wktString);
        GridConvergenceAngleCalc angleCalc = new GridConvergenceAngleCalc(crs);
        DirectPosition2D position =
                new DirectPosition2D(crs, 2626.018310546785 * 1000, -1118.3695068359375 * 1000);
        Assert.assertEquals(16.0573598047079d, angleCalc.getConvergenceAngle(position), TOLERANCE);
        position =
                new DirectPosition2D(crs, -1201.9818115234375 * 1000, -1172.3695068359375 * 1000);
        Assert.assertEquals(
                -7.461565880473206d, angleCalc.getConvergenceAngle(position), TOLERANCE);
    }

    @Test
    public void testEPSG3411() throws Exception {
        //
        // Test Oklahoma in EPSG:3411 Northern Hemis. Sea Ice Polar Stereo.
        //
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3411");
        GridConvergenceAngleCalc angleCalc = new GridConvergenceAngleCalc(crs);
        DirectPosition2D position = new DirectPosition2D(crs, -5050427.62537, -3831167.39071);
        Assert.assertEquals(
                -52.81667373163404d, angleCalc.getConvergenceAngle(position), TOLERANCE);
    }

    @Test
    public void testEPSG3031() throws Exception {
        //
        // Test Antarctic Polar Stereo EPSG:3031
        //
        //
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3031");
        GridConvergenceAngleCalc angleCalc = new GridConvergenceAngleCalc(crs);
        DirectPosition2D position = new DirectPosition2D(crs, 5450569.17764, -5333348.64467);
        Assert.assertEquals(
                -134.37722187798775d, angleCalc.getConvergenceAngle(position), TOLERANCE);
    }
}
