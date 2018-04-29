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

package org.geotools.referencing.operation.projection;

import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Tests for {@link AzimuthalEquidistant}. */
public class AzimuthalEquidistantTest {

    /** Test that parameter values are correctly converted to WKT. */
    @Test
    public void toWKT() throws Exception {
        // @formatter:off
        CoordinateReferenceSystem crs =
                CRS.parseWKT(
                        "PROJCS[\"unnamed\", "
                                + "GEOGCS[\"unnamed ellipse\", "
                                + "DATUM[\"unknown\", SPHEROID[\"unnamed\",6370841.391468334,0]], "
                                + "PRIMEM[\"Greenwich\",0], "
                                + "UNIT[\"degree\",0.0174532925199433]], "
                                + "PROJECTION[\"Azimuthal_Equidistant\"], "
                                + "PARAMETER[\"latitude_of_center\",42.42], "
                                + "PARAMETER[\"longitude_of_center\",16.16], "
                                + "PARAMETER[\"false_easting\",100000], "
                                + "PARAMETER[\"false_northing\",200000],"
                                + "UNIT[\"metre\", 1, AUTHORITY[\"EPSG\",\"9001\"]]]");
        // @formatter:on
        String wkt = crs.toWKT();
        Assert.assertTrue(wkt.contains("PROJECTION[\"Azimuthal_Equidistant\"]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"latitude_of_center\", 42.42]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"longitude_of_center\", 16.16]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"false_easting\", 100000.0]"));
        Assert.assertTrue(wkt.contains("PARAMETER[\"false_northing\", 200000.0]"));
    }
}
