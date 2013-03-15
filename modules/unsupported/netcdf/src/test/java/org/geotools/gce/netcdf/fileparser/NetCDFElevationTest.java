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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.SortedSet;

import org.geotools.gce.netcdf.test.TestData;
import org.geotools.gce.netcdf.test.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import ucar.nc2.NetcdfFile;

/**
 * 
 * @author Yancy
 */
public class NetCDFElevationTest {
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
    public void testGetElevations_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	SortedSet<Double> elevations = classUnderTest.getElevations();
	assertEquals("Wam NetCDF does not have exactly 1 elevation.", 1, elevations.size());
	Double firstElevation = elevations.first();
	assertEquals("Wam NetCDF's first elevation is not 0.0", 0.0, firstElevation,
		TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetElevationString_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	String elevations = classUnderTest.getElevationString();
	assertEquals("Wam NetCDF's elevation string is incorrect.", "0.0", elevations);
    }

    @Test
    public void testGetDefaultElevation_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	Double defaultElevation = classUnderTest.getDefaultElevation();
	assertEquals("Wam NetCDF's default elevation is not 0.0", 0.0, defaultElevation,
		TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetElevations_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	SortedSet<Double> elevations = classUnderTest.getElevations();
	assertEquals("SwanSantamon NetCDF does not have exactly 1 elevation.", 1, elevations.size());
	Double firstElevation = elevations.first();
	assertEquals("SwanSantamon NetCDF's first elevation is not 0.0", 0.0, firstElevation,
		TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetElevationString_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	String elevations = classUnderTest.getElevationString();
	assertEquals("SwanSantamon NetCDF's elevation string is incorrect.", "0.0", elevations);
    }

    @Test
    public void testGetDefaultElevation_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	Double defaultElevation = classUnderTest.getDefaultElevation();
	assertEquals("SwanSantamon NetCDF's default elevation is not 0.0", 0.0, defaultElevation,
		TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetElevations_SmallFile() {
	File testFile = new File(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	SortedSet<Double> elevations = classUnderTest.getElevations();
	assertEquals("SmallFile NetCDF does not have exactly 5 elevations.", 5, elevations.size());
	Double firstElevation = elevations.first();
	assertEquals("SmallFile NetCDF's first elevation is not 0.0", 0.0, firstElevation,
		TestUtil.FLOATING_POINT_DEMON);
	Double lastElevation = elevations.last();
	assertEquals("SmallFile NetCDF's last elevation is not 500.0", 500.0, lastElevation,
		TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetElevationString_SmallFile() {
	File testFile = new File(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	String elevations = classUnderTest.getElevationString();
	assertEquals("SmallFile NetCDF's elevation string is incorrect.",
		"0.0,50.0,100.0,250.0,500.0", elevations);
    }

    @Test
    public void testGetDefaultElevation_SmallFile() {
	File testFile = new File(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

	Double defaultElevation = classUnderTest.getDefaultElevation();
	assertEquals("SmallFile NetCDF's default elevation is not 0.0", 0.0, defaultElevation,
		TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetElevationLayerInNCFile() throws IOException {
	File testFile = new File(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);
	NetcdfFile ncfile = NetcdfFile.open(testFile.getAbsolutePath());

	int elevationIndex = classUnderTest.getElevationIndexInNCFile(ncfile, 0.0);
	assertEquals("SmallFile NetCDF's elevation index for 0.0 is not 0", 0, elevationIndex);
	elevationIndex = classUnderTest.getElevationIndexInNCFile(ncfile, 50.0);
	assertEquals("SmallFile NetCDF's elevation index for 50.0 is not 1", 1, elevationIndex);
	elevationIndex = classUnderTest.getElevationIndexInNCFile(ncfile, 500.0);
	assertEquals("SmallFile NetCDF's elevation index for 500.0 is not 4", 4, elevationIndex);

	// Check elevations that are not in the file
	elevationIndex = classUnderTest.getElevationIndexInNCFile(ncfile, -50.0);
	assertEquals("SmallFile NetCDF's elevation index for -50.0 is not -1", -1, elevationIndex);
	elevationIndex = classUnderTest.getElevationIndexInNCFile(ncfile, 300.0);
	assertEquals("SmallFile NetCDF's elevation index for 300.0 is not -1", -1, elevationIndex);
    }
}
