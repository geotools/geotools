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
import org.geotools.util.URLs;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JGrassRegionTest {

    private static final double EPSI = 0.0001;

    @Test
    public void testJGrassMapEnvironment() throws IOException {
        URL theUrl = this.getClass().getClassLoader().getResource("exampleWIND1");
        File regionFile = URLs.urlToFile(theUrl);
        JGrassRegion tmpRegion = new JGrassRegion(regionFile.getAbsolutePath());
        Assert.assertEquals(tmpRegion.getNorth(), 5052484.11852058, 0d);
        Assert.assertEquals(tmpRegion.getSouth(), 5041259.917339253, 0d);
        Assert.assertEquals(tmpRegion.getEast(), 679785.979783096, 0d);
        Assert.assertEquals(tmpRegion.getWest(), 668296.4273310362, 0d);
        Assert.assertEquals(tmpRegion.getNSResolution(), 4.999644178764807, 0d);
        Assert.assertEquals(tmpRegion.getWEResolution(), 4.999805244586515, 0d);
        Assert.assertEquals(tmpRegion.getRows(), 2245, 0d);
        Assert.assertEquals(tmpRegion.getCols(), 2298, 0d);

        theUrl = this.getClass().getClassLoader().getResource("exampleWINDLatLong");
        regionFile = URLs.urlToFile(theUrl);
        tmpRegion = new JGrassRegion(regionFile.getAbsolutePath());
        Assert.assertEquals(tmpRegion.getNorth(), -3.742082752222222, 0d);
        Assert.assertEquals(tmpRegion.getSouth(), -3.9554160855555556, 0d);
        Assert.assertEquals(tmpRegion.getEast(), 30.15125023476092, 0d);
        Assert.assertEquals(tmpRegion.getWest(), 29.840416916475846, 0d);
        Assert.assertEquals(tmpRegion.getNSResolution(), 8.333333333333335E-4, 0d);
        Assert.assertEquals(tmpRegion.getWEResolution(), 8.333332929894733E-4, 0d);
        Assert.assertEquals(tmpRegion.getRows(), 256);
        Assert.assertEquals(tmpRegion.getCols(), 373);

        theUrl = this.getClass().getClassLoader().getResource("exampleCELLHD1");
        File cellhdFile = URLs.urlToFile(theUrl);
        tmpRegion = new JGrassRegion(cellhdFile.getAbsolutePath());
        Assert.assertEquals(tmpRegion.getNorth(), 5052484.11852058, 0d);
        Assert.assertEquals(tmpRegion.getSouth(), 5041259.917339253, 0d);
        Assert.assertEquals(tmpRegion.getEast(), 679785.979783096, 0d);
        Assert.assertEquals(tmpRegion.getWest(), 668296.4273310362, 0d);
        Assert.assertEquals(tmpRegion.getNSResolution(), 4.999644178764807, 0d);
        Assert.assertEquals(tmpRegion.getWEResolution(), 4.999805244586515, 0d);
        Assert.assertEquals(tmpRegion.getRows(), 2245, 0d);
        Assert.assertEquals(tmpRegion.getCols(), 2298, 0d);

        theUrl = this.getClass().getClassLoader().getResource("exampleCELLHDLatLong");
        cellhdFile = URLs.urlToFile(theUrl);
        tmpRegion = new JGrassRegion(cellhdFile.getAbsolutePath());
        Assert.assertTrue(tmpRegion.getNorth() - 0.0004 < EPSI);
        Assert.assertTrue(tmpRegion.getSouth() - -5.0004 < EPSI);
        Assert.assertTrue(tmpRegion.getEast() - 35.0004 < EPSI);
        Assert.assertTrue(tmpRegion.getWest() - 25 < EPSI);
        Assert.assertTrue(tmpRegion.getNSResolution() - 8.333333333333334E-4 < EPSI);
        Assert.assertTrue(tmpRegion.getWEResolution() - 8.333332929894731E-4 < EPSI);
        Assert.assertEquals(tmpRegion.getRows(), 6001);
        Assert.assertEquals(tmpRegion.getCols(), 12001);
    }
}
