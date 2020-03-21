/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import junit.framework.TestCase;
import org.geotools.data.hana.metadata.Srs.Type;

/** @author Stefan Uhrig, SAP SE */
public class SrsTest extends TestCase {

    private static final String WGS84_WKT =
            "GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]]";
    private static final String WGS84_PROJ4 = "+proj=longlat +datum=WGS84 +no_defs";

    public void testValidGeographicSrs() {
        Srs srs =
                new Srs(
                        "WGS 84",
                        4326,
                        "EPSG",
                        4326,
                        WGS84_WKT,
                        WGS84_PROJ4,
                        "meter",
                        "degree",
                        Type.GEOGRAPHIC,
                        6378137.0,
                        null,
                        298.257223563,
                        -180.0,
                        180.0,
                        -90.0,
                        90.0);
        assertEquals("WGS 84", srs.getName());
        assertEquals(4326, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(4326, srs.getOrganizationId());
        assertEquals(WGS84_WKT, srs.getWkt());
        assertEquals(WGS84_PROJ4, srs.getProj4());
        assertEquals("meter", srs.getLinearUom());
        assertEquals("degree", srs.getAngularUom());
        assertEquals(Type.GEOGRAPHIC, srs.getType());
        assertEquals(6378137.0, srs.getMajorAxis());
        assertNull(srs.getMinorAxis());
        assertEquals(298.257223563, srs.getInverseFlattening());
        assertEquals(-180.0, srs.getMinX());
        assertEquals(180.0, srs.getMaxX());
        assertEquals(-90.0, srs.getMinY());
        assertEquals(90.0, srs.getMaxY());
    }

    public void testValidFlatSrs() {
        Srs srs =
                new Srs(
                        "WGS 84 (planar)",
                        1000004326,
                        "EPSG",
                        4326,
                        WGS84_WKT,
                        WGS84_PROJ4,
                        "meter",
                        "degree",
                        Type.GEOGRAPHIC,
                        6378137.0,
                        null,
                        298.257223563,
                        -180.0,
                        180.0,
                        -90.0,
                        90.0);
        assertEquals("WGS 84 (planar)", srs.getName());
        assertEquals(1000004326, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(4326, srs.getOrganizationId());
        assertEquals(WGS84_WKT, srs.getWkt());
        assertEquals(WGS84_PROJ4, srs.getProj4());
        assertEquals("meter", srs.getLinearUom());
        assertEquals("degree", srs.getAngularUom());
        assertEquals(Type.GEOGRAPHIC, srs.getType());
        assertEquals(6378137.0, srs.getMajorAxis());
        assertNull(srs.getMinorAxis());
        assertEquals(298.257223563, srs.getInverseFlattening());
        assertEquals(-180.0, srs.getMinX());
        assertEquals(180.0, srs.getMaxX());
        assertEquals(-90.0, srs.getMinY());
        assertEquals(90.0, srs.getMaxY());
    }

    public void testValidProjectedSrs() {
        String wkt =
                "PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Mercator_1SP\"],PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],EXTENSION[\"PROJ4\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"],AUTHORITY[\"EPSG\",\"3857\"]]";
        String proj4 =
                "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs";

        Srs srs =
                new Srs(
                        "WGS 84 / Pseudo-Mercator",
                        3857,
                        "EPSG",
                        3857,
                        wkt,
                        proj4,
                        "meter",
                        null,
                        Type.PROJECTED,
                        6378137.0,
                        null,
                        298.257223563,
                        -20037508.342789248,
                        20037508.342789248,
                        -20048966.104014635,
                        20048966.104014624);
        assertEquals("WGS 84 / Pseudo-Mercator", srs.getName());
        assertEquals(3857, srs.getSrid());
        assertEquals("EPSG", srs.getOrganization());
        assertEquals(3857, srs.getOrganizationId());
        assertEquals(wkt, srs.getWkt());
        assertEquals(proj4, srs.getProj4());
        assertEquals("meter", srs.getLinearUom());
        assertNull(srs.getAngularUom());
        assertEquals(Type.PROJECTED, srs.getType());
        assertEquals(6378137.0, srs.getMajorAxis());
        assertNull(srs.getMinorAxis());
        assertEquals(298.257223563, srs.getInverseFlattening());
        assertEquals(-20037508.342789248, srs.getMinX());
        assertEquals(20037508.342789248, srs.getMaxX());
        assertEquals(-20048966.104014635, srs.getMinY());
        assertEquals(20048966.104014624, srs.getMaxY());
    }

    public void testNoName() {
        try {
            new Srs(
                    null,
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
        try {
            new Srs(
                    "",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testNegativeSrid() {
        try {
            new Srs(
                    "WGS 84",
                    -1,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testNegativeOrganizationId() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    -1,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testNullType() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    null,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testMissingAngularUom() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    null,
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testMissingMajorAxis() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    null,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testMissingMinorAxisAndInvFlattening() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    null,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    public void testMinorAxisAndInvFlatteningBothGiven() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    6356752.31425,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testZeroAxesFlattening() {
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    0.0,
                    null,
                    298.257223563,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    0.0,
                    null,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            new Srs(
                    "WGS 84",
                    4326,
                    "EPSG",
                    4326,
                    WGS84_WKT,
                    WGS84_PROJ4,
                    "meter",
                    "degree",
                    Type.GEOGRAPHIC,
                    6378137.0,
                    null,
                    0.0,
                    -180.0,
                    180.0,
                    -90.0,
                    90.0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testProjectedWithFlatteningNoMajorAxis() {
        String wkt =
                "PROJCS[\"WGS 84 / Pseudo-Mercator\",GEOGCS[\"WGS 84\",DATUM[\"WGS_1984\",SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],AUTHORITY[\"EPSG\",\"6326\"]],PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],UNIT[\"degree\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9122\"]],AUTHORITY[\"EPSG\",\"4326\"]],PROJECTION[\"Mercator_1SP\"],PARAMETER[\"central_meridian\",0],PARAMETER[\"scale_factor\",1],PARAMETER[\"false_easting\",0],PARAMETER[\"false_northing\",0],UNIT[\"metre\",1,AUTHORITY[\"EPSG\",\"9001\"]],AXIS[\"X\",EAST],AXIS[\"Y\",NORTH],EXTENSION[\"PROJ4\",\"+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs\"],AUTHORITY[\"EPSG\",\"3857\"]]";
        String proj4 =
                "+proj=merc +a=6378137 +b=6378137 +lat_ts=0.0 +lon_0=0.0 +x_0=0.0 +y_0=0 +k=1.0 +units=m +nadgrids=@null +wktext +no_defs";

        try {
            new Srs(
                    "WGS 84 / Pseudo-Mercator",
                    3857,
                    "EPSG",
                    3857,
                    wkt,
                    proj4,
                    "meter",
                    null,
                    Type.PROJECTED,
                    null,
                    null,
                    298.257223563,
                    -20037508.342789248,
                    20037508.342789248,
                    -20048966.104014635,
                    20048966.104014624);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }
}
