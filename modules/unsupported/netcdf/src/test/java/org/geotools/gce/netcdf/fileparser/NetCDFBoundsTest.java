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

import java.awt.Rectangle;
import java.io.File;
import org.geotools.gce.netcdf.test.TestData;
import org.geotools.gce.netcdf.test.TestUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 *
 * @author Yancy
 */
public class NetCDFBoundsTest {
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
    public void testConvertBoundsToNeg180to180_Neg180to180() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = -180;
        float max = 180;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -180,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", 180,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_0to180() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 0;
        float max = 180;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", 0,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", 180,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_0to360() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 0;
        float max = 360;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -180,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", 180,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_180to360() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 180;
        float max = 360;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -180,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", 0,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_150to300() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 150;
        float max = 300;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -180,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", 180,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_200to300() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 200;
        float max = 300;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -160,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", -60,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_20to30() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 20;
        float max = 30;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", 20,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", 30,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_Neg20toNeg30() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = -20;
        float max = -30;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -20,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", -30,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_180to200() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = 180;
        float max = 200;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertEquals("Min longitude was converted incorrectly", -180,
                convertedLongs[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Max longitude was converted incorrectly", -160,
                convertedLongs[1], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testConvertBoundsToNeg180to180_Neg150to300() {
	classUnderTest = new NetCDFFileInspector();
        
        float min = -150;
        float max = 300;
        float[] convertedLongs = classUnderTest.convertBoundsToNeg180to180(min, max);
        assertNull("Longitude was converted incorrectly", convertedLongs);
    }

    @Test
    public void testGetBounds_PctidesChesBasin() {
	File testFile = new File(TestData.getPctidesChesBasin());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        float[] bounds = classUnderTest.getBounds();
        assertEquals("PCTIDES Ches_basin NetCDF's min longitude is wrong", -78.5,
                bounds[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("PCTIDES Ches_basin NetCDF's max longitude is wrong", -72,
                bounds[1], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("PCTIDES Ches_basin NetCDF's min latitude is wrong", 35,
                bounds[2], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("PCTIDES Ches_basin NetCDF's max latitude is wrong", 41,
                bounds[3], TestUtil.FLOATING_POINT_DEMON);
    }
    
    @Test
    public void testGetBounds_PctidesSanDiego() {
	File testFile = new File(TestData.getPctidesSanDiego());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        float[] bounds = classUnderTest.getBounds();
        assertEquals("PCTIDES SanDiego NetCDF's min longitude is wrong", -117.4,
                bounds[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("PCTIDES SanDiego NetCDF's max longitude is wrong", -117,
                bounds[1], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("PCTIDES SanDiego NetCDF's min latitude is wrong", 32.5,
                bounds[2], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("PCTIDES SanDiego NetCDF's max latitude is wrong", 32.87,
                bounds[3], TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetBounds_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        float[] bounds = classUnderTest.getBounds();
        assertEquals("Wam NetCDF's min longitude is wrong", -180,
                bounds[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Wam NetCDF's max longitude is wrong", 180,
                bounds[1], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Wam NetCDF's min latitude is wrong", -75,
                bounds[2], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("Wam NetCDF's max latitude is wrong", 65,
                bounds[3], TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetBounds_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        float[] bounds = classUnderTest.getBounds();
        assertEquals("SwanSantamon NetCDF's min longitude is wrong", -118.72499,
                bounds[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("SwanSantamon NetCDF's max longitude is wrong", -118.47499,
                bounds[1], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("SwanSantamon NetCDF's min latitude is wrong", 33.725,
                bounds[2], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("SwanSantamon NetCDF's max latitude is wrong", 33.9750005,
                bounds[3], TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetBounds_SwanSocal() {
	File testFile = new File(TestData.getSwanSocal());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        float[] bounds = classUnderTest.getBounds();
        assertEquals("SwanSocal NetCDF's min longitude is wrong", -125,
                bounds[0], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("SwanSocal NetCDF's max longitude is wrong", -116,
                bounds[1], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("SwanSocal NetCDF's min latitude is wrong", 32,
                bounds[2], TestUtil.FLOATING_POINT_DEMON);
        assertEquals("SwanSocal NetCDF's max latitude is wrong", 39,
                bounds[3], TestUtil.FLOATING_POINT_DEMON);
    }

    @Test
    public void testGetOriginalDim_PctidesChesBasin() {
	File testFile = new File(TestData.getPctidesChesBasin());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        Rectangle dim = classUnderTest.getOriginalDim();
        assertEquals("PctidesChesBasin NetCDF's dimension x is wrong", 0,
                dim.x);
        assertEquals("PctidesChesBasin NetCDF's dimension y is wrong", 0,
                dim.y);
        assertEquals("PctidesChesBasin NetCDF's dimension width is wrong", 14,
                dim.width);
        assertEquals("PctidesChesBasin NetCDF's dimension height is wrong", 13,
                dim.height);
    }

    @Test
    public void testGetOriginalDim_PctidesSanDiego() {
	File testFile = new File(TestData.getPctidesSanDiego());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        Rectangle dim = classUnderTest.getOriginalDim();
        assertEquals("PctidesSanDiego NetCDF's dimension x is wrong", 0,
                dim.x);
        assertEquals("PctidesSanDiego NetCDF's dimension y is wrong", 0,
                dim.y);
        assertEquals("PctidesSanDiego NetCDF's dimension width is wrong", 201,
                dim.width);
        assertEquals("PctidesSanDiego NetCDF's dimension height is wrong", 186,
                dim.height);
    }

    @Test
    public void testGetOriginalDim_Wam() {
	File testFile = new File(TestData.getWam());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        Rectangle dim = classUnderTest.getOriginalDim();
        assertEquals("Wam NetCDF's dimension x is wrong", 0,
                dim.x);
        assertEquals("Wam NetCDF's dimension y is wrong", 0,
                dim.y);
        assertEquals("Wam NetCDF's dimension width is wrong", 360,
                dim.width);
        assertEquals("Wam NetCDF's dimension height is wrong", 141,
                dim.height);
    }

    @Test
    public void testGetOriginalDim_SwanSantamon() {
	File testFile = new File(TestData.getSwanSantamon());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        Rectangle dim = classUnderTest.getOriginalDim();
        assertEquals("SwanSantamon NetCDF's dimension x is wrong", 0,
                dim.x);
        assertEquals("SwanSantamon NetCDF's dimension y is wrong", 0,
                dim.y);
        assertEquals("SwanSantamon NetCDF's dimension width is wrong", 151,
                dim.width);
        assertEquals("SwanSantamon NetCDF's dimension height is wrong", 151,
                dim.height);
    }

    @Test
    public void testGetOriginalDim_SwanSocal() {
	File testFile = new File(TestData.getSwanSocal());
        if (null == testFile || !testFile.exists()) {
            return;
        }
	classUnderTest = new NetCDFFileInspector(testFile);

        Rectangle dim = classUnderTest.getOriginalDim();
        assertEquals("SwanSocal NetCDF's dimension x is wrong", 0,
                dim.x);
        assertEquals("SwanSocal NetCDF's dimension y is wrong", 0,
                dim.y);
        assertEquals("SwanSocal NetCDF's dimension width is wrong", 91,
                dim.width);
        assertEquals("SwanSocal NetCDF's dimension height is wrong", 71,
                dim.height);
    }
}
