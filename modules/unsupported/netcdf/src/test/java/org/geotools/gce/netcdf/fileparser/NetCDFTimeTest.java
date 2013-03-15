/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.netcdf.fileparser;

import java.io.File;
import java.util.SortedSet;
import org.geotools.gce.netcdf.test.TestData;
import org.geotools.gce.netcdf.test.TestUtil;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 *
 * @author Yancy
 */
public class NetCDFTimeTest {
    NetCDFFileInspector classUnderTest;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
	classUnderTest = null;
    }

    @Test
    public void testGetTimeMinimum_PctidesChesBasin() {
	File testFile = new File(TestData.getPctidesChesBasin());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMinimum();
        assertEquals("PctidesChesBasin NetCDF Time Minimum is incorrect", 
                "2012-10-25T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMaximum_PctidesChesBasin() {
	File testFile = new File(TestData.getPctidesChesBasin());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMaximum();
        assertEquals("PctidesChesBasin NetCDF Time Maximum is incorrect", 
                "2012-10-27T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMinimum_PctidesSanDiego() {
	File testFile = new File(TestData.getPctidesSanDiego());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMinimum();
        assertEquals("PctidesSanDiego NetCDF Time Minimum is incorrect", 
                "2012-10-25T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMaximum_PctidesSanDiego() {
	File testFile = new File(TestData.getPctidesSanDiego());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMaximum();
        assertEquals("PctidesSanDiego NetCDF Time Maximum is incorrect", 
                "2012-10-27T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMinimum_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMinimum();
        assertEquals("Wam NetCDF Time Minimum is incorrect", 
                "2012-10-25T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMaximum_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMaximum();
        assertEquals("Wam NetCDF Time Maximum is incorrect", 
                "2012-10-29T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMinimum_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMinimum();
        assertEquals("SwanSantamon NetCDF Time Minimum is incorrect", 
                "2012-10-25T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMaximum_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMaximum();
        assertEquals("SwanSantamon NetCDF Time Maximum is incorrect", 
                "2012-10-27T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMinimum_SwanSocal() {
	File testFile = new File(TestData.getSwanSocal());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMinimum();
        assertEquals("SwanSocal NetCDF Time Minimum is incorrect", 
                "2012-10-25T12:00:00.000Z", time);
    }

    @Test
    public void testGetTimeMaximum_SwanSocal() {
	File testFile = new File(TestData.getSwanSocal());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
        
        String time = classUnderTest.getTimeMaximum();
        assertEquals("SwanSocal NetCDF Time Maximum is incorrect", 
                "2012-10-27T12:00:00.000Z", time);
    }

}
