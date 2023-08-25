/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import java.util.Set;
import javax.sql.DataSource;
import org.geotools.TestData;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.IdentifiedObjectFinder;
import org.geotools.util.factory.Hints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HsqlDialectEpsgMediatorTest {
    private HsqlDialectEpsgMediator factory;
    private IdentifiedObjectFinder finder;

    @Before
    public void setUp() throws Exception {
        if (factory == null) {
            DataSource datasource = HsqlEpsgDatabase.createDataSource();
            Hints hints = new Hints(Hints.CACHE_POLICY, "default");
            factory = new HsqlDialectEpsgMediator(80, hints, datasource);
        }
        if (finder == null) {
            finder = factory.getIdentifiedObjectFinder(CoordinateReferenceSystem.class);
        }
    }

    @Test
    public void testCreation() throws Exception {
        Assert.assertNotNull(factory);
        CoordinateReferenceSystem epsg4326 = factory.createCoordinateReferenceSystem("EPSG:4326");
        CoordinateReferenceSystem code4326 = factory.createCoordinateReferenceSystem("4326");

        Assert.assertNotNull(epsg4326);
        Assert.assertEquals("4326 equals EPSG:4326", code4326, epsg4326);
        Assert.assertSame("4326 == EPSG:4326", code4326, epsg4326);
    }

    @Test
    public void testFunctionality() throws Exception {
        CoordinateReferenceSystem crs1 = factory.createCoordinateReferenceSystem("4326");
        CoordinateReferenceSystem crs2 = factory.createCoordinateReferenceSystem("3005");

        // reproject
        MathTransform transform = CRS.findMathTransform(crs1, crs2, true);
        Position pos = new DirectPosition2D(48.417, 123.35);
        transform.transform(pos, null);
    }

    @Test
    public void testAuthorityCodes() throws Exception {
        Set authorityCodes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        Assert.assertNotNull(authorityCodes);
        Assert.assertTrue(authorityCodes.size() > 3000);
    }

    @Test
    public void testFindWSG84() throws FactoryException {
        String wkt =
                "GEOGCS[\"WGS 84\",\n"
                        + "  DATUM[\"World Geodetic System 1984\",\n"
                        + "    SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]],\n"
                        + "  PRIMEM[\"Greenwich\", 0.0],\n"
                        + "  UNIT[\"degree\", 0.017453292519943295],\n"
                        + "  AXIS[\"Geodetic latitude\", NORTH],\n"
                        + "  AXIS[\"Geodetic longitude\", EAST]]";

        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);
        finder.setFullScanAllowed(false);

        Assert.assertNull(
                "Should not find without a full scan, because the WKT contains no identifier "
                        + "and the CRS name is ambiguous (more than one EPSG object have this "
                        + "name).",
                finder.find(crs));

        finder.setFullScanAllowed(true);
        IdentifiedObject find = finder.find(crs);

        Assert.assertNotNull("With full scan allowed, the CRS should be found.", find);

        Assert.assertTrue(
                "Should found an object equals (ignoring metadata) to the requested one.",
                CRS.equalsIgnoreMetadata(crs, find));
        ReferenceIdentifier found =
                AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority());
        // assertEquals("4326",found.getCode());
        Assert.assertNotNull(found);

        finder.setFullScanAllowed(false);
        String id = finder.findIdentifier(crs);
        // this is broken because, as we know from above, it is ambiguous, so it may not be
        // EPSG:4326 in the cache at all!
        // assertEquals("The CRS should still in the cache.","EPSG:4326", id);
        Assert.assertEquals(
                "The CRS should still in the cache.",
                found.getCodeSpace() + ':' + found.getCode(),
                id);
    }

    @Test
    public void testFindBeijing1954() throws FactoryException {
        if (!TestData.isExtensiveTest()) {
            return;
        }
        /*
         * The PROJCS below intentionally uses a name different from the one found in the
         * EPSG database, in order to force a full scan (otherwise the EPSG database would
         * find it by name, but we want to test the scan).
         */
        String wkt =
                "PROJCS[\"Beijing 1954\",\n"
                        + "   GEOGCS[\"Beijing 1954\",\n"
                        + "     DATUM[\"Beijing 1954\",\n"
                        + "       SPHEROID[\"Krassowsky 1940\", 6378245.0, 298.3]],\n"
                        + "     PRIMEM[\"Greenwich\", 0.0],\n"
                        + "     UNIT[\"degree\", 0.017453292519943295],\n"
                        + "     AXIS[\"Geodetic latitude\", NORTH],\n"
                        + "     AXIS[\"Geodetic longitude\", EAST]],\n"
                        + "   PROJECTION[\"Transverse Mercator\"],\n"
                        + "   PARAMETER[\"central_meridian\", 135.0],\n"
                        + "   PARAMETER[\"latitude_of_origin\", 0.0],\n"
                        + "   PARAMETER[\"scale_factor\", 1.0],\n"
                        + "   PARAMETER[\"false_easting\", 500000.0],\n"
                        + "   PARAMETER[\"false_northing\", 0.0],\n"
                        + "   UNIT[\"m\", 1.0],\n"
                        + "   AXIS[\"Northing\", NORTH],\n"
                        + "   AXIS[\"Easting\", EAST]]";
        CoordinateReferenceSystem crs = CRS.parseWKT(wkt);

        finder.setFullScanAllowed(false);
        Assert.assertNull("Should not find the CRS without a full scan.", finder.find(crs));

        finder.setFullScanAllowed(true);
        IdentifiedObject find = finder.find(crs);
        Assert.assertNotNull("With full scan allowed, the CRS should be found.", find);

        Assert.assertTrue(
                "Should found an object equals (ignoring metadata) to the requested one.",
                CRS.equalsIgnoreMetadata(crs, find));

        Assert.assertEquals(
                "2442",
                AbstractIdentifiedObject.getIdentifier(find, factory.getAuthority()).getCode());
        finder.setFullScanAllowed(false);
        String id = finder.findIdentifier(crs);
        Assert.assertEquals("The CRS should still in the cache.", "EPSG:2442", id);
    }
}
