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
package org.geotools.gce.netcdf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import org.geotools.gce.netcdf.NetCdfUtil;
import org.geotools.gce.netcdf.ParamInformation;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * class to hold test utility methods.
 */
public class TestUtil {

    /**
     * http://mindprod.com/jgloss/floatingpoint.html
     */
    public static final double FLOATING_POINT_DEMON = 0.00001;

    public static final Collection<String> TEST_NAMES_WILL_NOT_FIND = Arrays
	    .asList("zePlane!zePlane!");

    public static void testBoundsMinVsMax(float[] bounds) {
	assertTrue(
		"Max longitude not greater than or equal to min longitude.",
		bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE] >= bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE]);
	assertTrue(
		"Max latitude not greater than or equal to min latitude.",
		bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE] >= bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE]);
    }

    public static void testBoundsSpecificValues(float[] expectedBounds,
	    float[] actualBounds) {

	assertEquals("Unexpectd bounds.",
		expectedBounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE],
		actualBounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE],
		TestUtil.FLOATING_POINT_DEMON);
	assertEquals("Unexpectd bounds.",
		expectedBounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE],
		actualBounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE],
		TestUtil.FLOATING_POINT_DEMON);
	assertEquals("Unexpectd bounds.",
		expectedBounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE],
		actualBounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE],
		TestUtil.FLOATING_POINT_DEMON);
	assertEquals("Unexpectd bounds.",
		expectedBounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE],
		actualBounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE],
		TestUtil.FLOATING_POINT_DEMON);
    }

    /**
     * test that the Rectangle has the expected individual values.
     */
    public static void testRectangle(Rectangle rectangle, int upperLeftX,
	    int upperLeftY, int width, int height) {
	assertEquals("Upper left x is not as expected.", upperLeftX,
		rectangle.getX(), FLOATING_POINT_DEMON);
	assertEquals("Upper left y is not as expected.", upperLeftY,
		rectangle.getY(), FLOATING_POINT_DEMON);
	assertEquals("Width is not as expected.", width, rectangle.getWidth(),
		FLOATING_POINT_DEMON);
	assertEquals("Height is not as expected.", height,
		rectangle.getHeight(), FLOATING_POINT_DEMON);
    }

    /**
     * test that the GeneralEnvelope is consistent(max >= min on each dimension)
     * and has the expected individual values.
     */
    public static void testGeneralEnvelope(GeneralEnvelope generalEnvelope,
	    float[] targetBounds) {
	assertTrue("Dimension 0 max is not greater than or equal to min.",
		generalEnvelope.getMaximum(0) >= generalEnvelope.getMinimum(0));
	assertTrue("Dimension 1 max is not greater than or equal to min.",
		generalEnvelope.getMaximum(1) >= generalEnvelope.getMinimum(1));
	assertEquals("Dimension 0 min is not as expected.",
		targetBounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE],
		generalEnvelope.getMinimum(0), FLOATING_POINT_DEMON);
	assertEquals("Dimension 0 max is not as expected.",
		targetBounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE],
		generalEnvelope.getMaximum(0), FLOATING_POINT_DEMON);
	assertEquals("Dimension 1 min is not as expected.",
		targetBounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE],
		generalEnvelope.getMinimum(1), FLOATING_POINT_DEMON);
	assertEquals("Dimension 1 max is not as expected.",
		targetBounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE],
		generalEnvelope.getMaximum(1), FLOATING_POINT_DEMON);
    }

    /**
     * test that the GridEnvelope is consistent(max >= min on each dimension)
     * and has the expected individual values.
     * 
     * note that the range max values are 1 fewer than I expected, look at the
     * output methods, they subtract 1 (and are documented as inclusive?) so
     * can't pass in the original bounds as input.
     */
    public static void testGridEnvelope(GridEnvelope gridEnvelope, int minDim0,
	    int maxDim0, int minDim1, int maxDim1) {
	assertTrue("Dimension 0 max is not greater than or equal to min.",
		gridEnvelope.getHigh(0) >= gridEnvelope.getLow(0));
	assertTrue("Dimension 1 max is not greater than or equal to min.",
		gridEnvelope.getHigh(1) >= gridEnvelope.getLow(1));
	assertEquals("Dimension 0 min is not as expected.", minDim0,
		gridEnvelope.getLow(0));
	assertEquals("Dimension 0 max is not as expected.", maxDim0,
		gridEnvelope.getHigh(0));
	assertEquals("Dimension 1 min is not as expected.", minDim1,
		gridEnvelope.getLow(1));
	assertEquals("Dimension 1 max is not as expected.", maxDim1,
		gridEnvelope.getHigh(1));
    }

    public static CoordinateReferenceSystem getTestCrs() {
	return getTestCrs(TestData.TEST_DEFAULT_CRS_CODE);
    }

    public static CoordinateReferenceSystem getTestCrs(String code) {
	CoordinateReferenceSystem crs = null;
	try {
	    crs = CRS.decode(code);
	} catch (NoSuchAuthorityCodeException e) {
	    fail("Test setup problem - CRS lookup " + code + ".");
	} catch (FactoryException e) {
	    fail("Test setup problem - CRS lookup " + code + ".");
	}

	return crs;
    }

    public static NetcdfFile getTestNetCdfFile(String netCdfFileName) {
	NetcdfFile ncFile = null;
	try {
	    ncFile = NetcdfFile.open(netCdfFileName);
	} catch (IOException e) {
	    fail("Test setup problem - could not open " + netCdfFileName + ".");
	}

	return ncFile;
    }

    public static NetcdfFile getTestNetCdfFileFromDataset(String fileName) {
	NetcdfFile ncFile = null;
	try {
	    ncFile = NetcdfDataset.openFile(fileName, null);
	} catch (IOException e) {
	    fail("Test setup problem - could not open " + fileName + ".");
	}

	return ncFile;
    }

    public static NetcdfDataset getTestNetcdfDatasetFromDataset(String fileName) {
	NetcdfDataset ncd = null;
	try {
	    ncd = NetcdfDataset.openDataset(fileName);
	} catch (IOException e) {
	    fail("Test setup problem - could not open " + fileName + ".");
	}

	return ncd;
    }

    public static File getTestFile(String fileName) {
        File file = new File(fileName);
        assertTestFileExists(file);
        
        return file;
    }

    public static void assertTestFileExists(File file) {
        assertNotNull("Test setup problem - file is null for " + file + ".", file);
        assertTrue("Test setup problem - file does not exist for " + file + ".", file.exists());
    }
    
    public static Date getTestDate(String dateStringInNetCdfFileFormat) {
	Date date = null;

	try {
	    date = NetCdfUtil.NETCDF_DATE_FORMAT_1
		    .parse(dateStringInNetCdfFileFormat);
	} catch (ParseException e) {
	    fail("Test setup problem - Date from "
		    + dateStringInNetCdfFileFormat + ".");
	}

	return date;
    }

    public static Date getTestDateOutput(String dateStringInOutputFormat) {
	Date date = null;

	try {
	    date = NetCdfUtil.OUTPUT_DATE_FORMAT
		    .parse(dateStringInOutputFormat);
	} catch (ParseException e) {
	    fail("Test setup problem - Date from " + dateStringInOutputFormat
		    + ".");
	}

	return date;
    }

    /**
     * @param bounds
     *            input min lon, max lon, min lat, max lat
     * @param shape
     *            input time, depth, lat, lon
     */
    public static ParamInformation createTestParamInformation(float[] bounds,
	    int[] shape, Date analysisTime, Date time, Double elevation,
	    String parameter) {
	ParamInformation pi = new ParamInformation();

	GeneralEnvelope ge = new GeneralEnvelope(new double[] { 0, 0 },
		new double[] { 0, 0 });
	ge.setRange(0, bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LONGITUDE],
		bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LONGITUDE]);
	ge.setRange(1, bounds[NetCdfUtil.BOUNDS_INDEX_MIN_LATITUDE],
		bounds[NetCdfUtil.BOUNDS_INDEX_MAX_LATITUDE]);
	ge.setCoordinateReferenceSystem(TestUtil.getTestCrs());
	pi.setRequestedEnvelope(ge);

	pi.setDim(new GridEnvelope2D(new Rectangle(0, 0, shape[3], shape[2])));

	pi.setAnalysisTime(analysisTime);
	pi.setTime(time);
	pi.setElevation(elevation);
	pi.setParameter(parameter);

	return pi;
    }

    public static void printFloatArray(String testClassName,
	    String testMethodName, String arrayTitle, float[] array) {
	System.out.println(testClassName + " " + testMethodName + " "
		+ arrayTitle + ": " + TestUtil.floatArrayToString(array) + " ");
    }

    public static String floatArrayToString(float[] floatArray) {
	StringBuffer sb = new StringBuffer();
	for (float f : floatArray) {
	    if (sb.length() > 0) {
		sb.append(", ");
	    }
	    sb.append(f);
	}
	return sb.toString();
    }

}
