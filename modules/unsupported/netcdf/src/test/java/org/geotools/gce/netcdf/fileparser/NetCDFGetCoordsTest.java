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
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import org.geotools.gce.netcdf.NetCdfUtil;
import org.geotools.gce.netcdf.index.IndexingStrategy;
import org.geotools.gce.netcdf.index.NearestNeighborIndexingStrategy;
import org.geotools.gce.netcdf.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import ucar.ma2.Array;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;

/**
 *
 * @author Yancy
 */
public class NetCDFGetCoordsTest {
    IndexingStrategy classUnderTest;

    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() throws Exception {
	classUnderTest = new NearestNeighborIndexingStrategy();
    }

    @After
    public void tearDown() throws Exception {
	classUnderTest = null;
    }
    
    @Test
    public void testGetCoordinateIndex_AllFiles() throws IOException {
        System.out.println("Testing NearestNeighbor.getCoordinateIndex for PctidesChesBasin.");
        testGetCoordinateIndex_WithFile(TestData.getPctidesChesBasin());
        
        System.out.println("Testing NearestNeighbor.getCoordinateIndex for PctidesSanDiego.");
        testGetCoordinateIndex_WithFile(TestData.getPctidesSanDiego());
        
        System.out.println("Testing NearestNeighbor.getCoordinateIndex for Wam.");
        testGetCoordinateIndex_WithFile(TestData.getWam());
        
        System.out.println("Testing NearestNeighbor.getCoordinateIndex for SwanSantamon.");
        testGetCoordinateIndex_WithFile(TestData.getSwanSantamon());
        
        System.out.println("Testing NearestNeighbor.getCoordinateIndex for SwanSocal.");
        testGetCoordinateIndex_WithFile(TestData.getSwanSocal());
    }
    
    
    public void testGetCoordinateIndex_WithFile(String filename)
            throws IOException {
        File testFile = new File(filename);
        if (null == testFile || !testFile.exists()) {
            return;
        }
        System.out.println("Testing longitudes.");
        Array lons = getLonArrayFromFile(filename);
        testGetCoordinateIndex_InitialPointWithArray(lons);
        for (int i = 1; i < lons.getSize() - 2; i++) {
            testGetCoordinateIndex_WithArrayAndIndex(lons, i);
        }
        testGetCoordinateIndex_LastPointWithArray(lons);

        System.out.println("Testing latitudes.");
        Array lats = getLatArrayFromFile(filename);
        testGetCoordinateIndex_InitialPointWithArray(lats);
        for (int i = 1; i < lats.getSize() - 2; i++) {
            testGetCoordinateIndex_WithArrayAndIndex(lats, i);
        }
        testGetCoordinateIndex_LastPointWithArray(lats);
    }

    /**
     * Test getting the index for coordinates near the first the coordinate in
     * the array.
     *
     * @param array
     * @throws IOException
     */
    public void testGetCoordinateIndex_InitialPointWithArray(Array array)
            throws IOException {

        double initialPoint = array.getDouble(0);
        double spacing = Math.abs(initialPoint - array.getDouble(1));

        double testPoint = initialPoint;

        testGetCoordinateIndex(array, testPoint, 0);

        testPoint = getTestPoint(initialPoint, spacing, -1);
        testGetCoordinateIndex(array, testPoint, -1);

        testPoint = getTestPoint(initialPoint, spacing, -1.5);
        testGetCoordinateIndex(array, testPoint, -1);

        // Because of the floating point error limitation in the double
        // primitive, we can't reliable test exactly halfway. So test slightly
        // before and after.
        testPoint = getTestPoint(initialPoint, spacing, -1.9);
        testGetCoordinateIndex(array, testPoint, -1);

        testPoint = getTestPoint(initialPoint, spacing, -2.1);
        testGetCoordinateIndex(array, testPoint, 0);

        testPoint = getTestPoint(initialPoint, spacing, -3);
        testGetCoordinateIndex(array, testPoint, 0);

        testPoint = getTestPoint(initialPoint, spacing, -4);
        testGetCoordinateIndex(array, testPoint, 0);
    }

    /**
     * Test getting the index for coordinates near a given coordinate in
     * the array.
     *
     * @param array
     * @throws IOException
     */
    public void testGetCoordinateIndex_WithArrayAndIndex(Array array, int index)
            throws IOException {

        double initialPoint = array.getDouble(index);
        double nextPoint = array.getDouble(index + 1);
        double spacing = getAbsoluteDifference(initialPoint, nextPoint);
        double testPoint = initialPoint;

        // Test the exact point
        testGetCoordinateIndex(array, testPoint, index);

        // Test less than the point
        testPoint = getTestPoint(initialPoint, spacing, -1);
        testGetCoordinateIndex(array, testPoint, index - 1);

        testPoint = getTestPoint(initialPoint, spacing, -1.5);
        testGetCoordinateIndex(array, testPoint, index - 1);

        // Because of the floating point error limitation in the double
        // primitive, we can't reliable test exactly halfway. So test slightly
        // before and after.
        testPoint = getTestPoint(initialPoint, spacing, -1.9);
        testGetCoordinateIndex(array, testPoint, index - 1);

        testPoint = getTestPoint(initialPoint, spacing, -2.1);
        testGetCoordinateIndex(array, testPoint, index);

        testPoint = getTestPoint(initialPoint, spacing, -3);
        testGetCoordinateIndex(array, testPoint, index);

        testPoint = getTestPoint(initialPoint, spacing, -4);
        testGetCoordinateIndex(array, testPoint, index);
        
        // Test greater than the point
        testPoint = getTestPoint(initialPoint, spacing, 1);
        testGetCoordinateIndex(array, testPoint, index + 1);

        testPoint = getTestPoint(initialPoint, spacing, 1.5);
        testGetCoordinateIndex(array, testPoint, index + 1);

        // Because of the floating point error limitation in the double
        // primitive, we can't reliable test exactly halfway. So test slightly
        // before and after.
        testPoint = getTestPoint(initialPoint, spacing, 1.9);
        testGetCoordinateIndex(array, testPoint, index + 1);

        testPoint = getTestPoint(initialPoint, spacing, 2.1);
        testGetCoordinateIndex(array, testPoint, index);

        testPoint = getTestPoint(initialPoint, spacing, 3);
        testGetCoordinateIndex(array, testPoint, index);

        testPoint = getTestPoint(initialPoint, spacing, 4);
        testGetCoordinateIndex(array, testPoint, index);
    }

    /**
     * Test getting the index for coordinates near the last the coordinate in
     * the array.
     *
     * @param array
     * @throws IOException
     */
    public void testGetCoordinateIndex_LastPointWithArray(Array array)
            throws IOException {

        int index = (int)array.getSize() - 1;
        double initialPoint = array.getDouble(index);
        double spacing = Math.abs(initialPoint - array.getDouble(index - 1));

        double testPoint = initialPoint;

        testGetCoordinateIndex(array, testPoint, index);

        testPoint = getTestPoint(initialPoint, spacing, 1);
        testGetCoordinateIndex(array, testPoint, -1);

        testPoint = getTestPoint(initialPoint, spacing, 1.5);
        testGetCoordinateIndex(array, testPoint, -1);

        // Because of the floating point error limitation in the double
        // primitive, we can't reliable test exactly halfway. So test slightly
        // before and after.
        testPoint = getTestPoint(initialPoint, spacing, 1.9);
        testGetCoordinateIndex(array, testPoint, -1);
        
        testPoint = getTestPoint(initialPoint, spacing, 2.1);
        testGetCoordinateIndex(array, testPoint, index);
        
        testPoint = getTestPoint(initialPoint, spacing, 3);
        testGetCoordinateIndex(array, testPoint, index);

        testPoint = getTestPoint(initialPoint, spacing, 4);
        testGetCoordinateIndex(array, testPoint, index);
    }

    public void testGetCoordinateIndex(Array array, double point,
            int expectedIndex) throws IOException {
        int actualIndex = classUnderTest.getCoordinateIndex(array, point);

        if (expectedIndex != -1 && actualIndex != -1) {
            assertEquals("Nearest Coordinate index is incorrect for " +
                    point + "\nValue of expected index is " +
                    array.getDouble(expectedIndex) +
                    "\nValue of actual index is " +
                    array.getDouble(actualIndex), expectedIndex, actualIndex);
        } else {
            assertEquals("Nearest Coordinate index is incorrect for " + point,
                    expectedIndex, actualIndex);
        }
    }

    /**
     * Reads the array of longitude coordinates from a NetCDF.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public Array getLonArrayFromFile(String filename) throws IOException {
        NetcdfFile ncFile = NetcdfDataset.openFile(filename, null);

        Variable lon = NetCdfUtil.getFileVariableByName(ncFile, 
                NetCdfUtil.LON_VARIABLE_NAMES);
        Array la = lon.read();
        ncFile.close();

        return la;
    }

    /**
     * Reads the array of latitude coordinates from a NetCDF.
     *
     * @param filename
     * @return
     * @throws IOException
     */
    public Array getLatArrayFromFile(String filename) throws IOException {
        NetcdfFile ncFile = NetcdfDataset.openFile(filename, null);

        Variable lat = NetCdfUtil.getFileVariableByName(ncFile, 
                NetCdfUtil.LAT_VARIABLE_NAMES);
        Array la = lat.read();
        ncFile.close();

        return la;
    }

    /*
     * Creates a testpoint that is a fraction of the resolution from the
     * given point.
     */
    public double getTestPoint(double initialPoint, double resolution,
            double factor) {
        return initialPoint + (resolution / factor);
    }

    public double getAbsoluteDifference(double a, double b) {
        return Math.abs(a - b);
    }
}
