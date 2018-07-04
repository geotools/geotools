/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import junit.framework.TestCase;
import org.geotools.util.URLs;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 * @source $URL$
 */
@SuppressWarnings("nls")
public class JGrassRegionTest extends TestCase {

    private static final double EPSI = 0.0001;

    public void testJGrassMapEnvironment() throws IOException {
        URL theUrl = this.getClass().getClassLoader().getResource("exampleWIND1");
        File regionFile = URLs.urlToFile(theUrl);
        JGrassRegion tmpRegion = new JGrassRegion(regionFile.getAbsolutePath());
        assertEquals(tmpRegion.getNorth(), 5052484.11852058);
        assertEquals(tmpRegion.getSouth(), 5041259.917339253);
        assertEquals(tmpRegion.getEast(), 679785.979783096);
        assertEquals(tmpRegion.getWest(), 668296.4273310362);
        assertEquals(tmpRegion.getNSResolution(), 4.999644178764807);
        assertEquals(tmpRegion.getWEResolution(), 4.999805244586515);
        assertEquals(tmpRegion.getRows(), 2245);
        assertEquals(tmpRegion.getCols(), 2298);

        theUrl = this.getClass().getClassLoader().getResource("exampleWINDLatLong");
        regionFile = URLs.urlToFile(theUrl);
        tmpRegion = new JGrassRegion(regionFile.getAbsolutePath());
        assertEquals(tmpRegion.getNorth(), -3.742082752222222);
        assertEquals(tmpRegion.getSouth(), -3.9554160855555556);
        assertEquals(tmpRegion.getEast(), 30.15125023476092);
        assertEquals(tmpRegion.getWest(), 29.840416916475846);
        assertEquals(tmpRegion.getNSResolution(), 8.333333333333335E-4);
        assertEquals(tmpRegion.getWEResolution(), 8.333332929894733E-4);
        assertEquals(tmpRegion.getRows(), 256);
        assertEquals(tmpRegion.getCols(), 373);

        theUrl = this.getClass().getClassLoader().getResource("exampleCELLHD1");
        File cellhdFile = URLs.urlToFile(theUrl);
        tmpRegion = new JGrassRegion(cellhdFile.getAbsolutePath());
        assertEquals(tmpRegion.getNorth(), 5052484.11852058);
        assertEquals(tmpRegion.getSouth(), 5041259.917339253);
        assertEquals(tmpRegion.getEast(), 679785.979783096);
        assertEquals(tmpRegion.getWest(), 668296.4273310362);
        assertEquals(tmpRegion.getNSResolution(), 4.999644178764807);
        assertEquals(tmpRegion.getWEResolution(), 4.999805244586515);
        assertEquals(tmpRegion.getRows(), 2245);
        assertEquals(tmpRegion.getCols(), 2298);

        theUrl = this.getClass().getClassLoader().getResource("exampleCELLHDLatLong");
        cellhdFile = URLs.urlToFile(theUrl);
        tmpRegion = new JGrassRegion(cellhdFile.getAbsolutePath());
        assertTrue(tmpRegion.getNorth() - 0.0004 < EPSI);
        assertTrue(tmpRegion.getSouth() - (-5.0004) < EPSI);
        assertTrue(tmpRegion.getEast() - 35.0004 < EPSI);
        assertTrue(tmpRegion.getWest() - 25 < EPSI);
        assertTrue(tmpRegion.getNSResolution() - 8.333333333333334E-4 < EPSI);
        assertTrue(tmpRegion.getWEResolution() - 8.333332929894731E-4 < EPSI);
        assertEquals(tmpRegion.getRows(), 6001);
        assertEquals(tmpRegion.getCols(), 12001);
    }
}
