/* Copyright (c) 2001 - 2013 OpenPlans - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geotools.gce.netcdf;

import org.geotools.gce.netcdf.NetCdfUtil;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.geotools.gce.netcdf.test.TestData;
import org.geotools.gce.netcdf.test.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * unit tests for named class.
 */
public class NetCdfUtilTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetFileVariableByNameFound() {
	NetcdfFile ncFile = TestUtil
		.getTestNetCdfFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LON_VARIABLE_NAMES);
	assertNotNull("Did not find expected variable.", variable);
    }

    @Test
    public void testGetFileVariableByNameNotFound() {
	NetcdfFile ncFile = TestUtil
		.getTestNetCdfFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		TestUtil.TEST_NAMES_WILL_NOT_FIND);
	assertNull("Found unexpected variable.", variable);
    }

    @Test
    public void testGetVariableAttributeByNameFound() {
	NetcdfFile ncFile = TestUtil
		.getTestNetCdfFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LON_VARIABLE_NAMES);
	Attribute attribute = NetCdfUtil.getVariableAttributeByName(variable,
		NetCdfUtil.TIME_UNIT_ATTRIBUTE_NAMES);
	assertNotNull("Did not find expected variable attribute.", attribute);
    }

    @Test
    public void testGetVariableAttributeByNameNotFound() {
	NetcdfFile ncFile = TestUtil
		.getTestNetCdfFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LON_VARIABLE_NAMES);
	Attribute attribute = NetCdfUtil.getVariableAttributeByName(variable,
		TestUtil.TEST_NAMES_WILL_NOT_FIND);
	assertNull("Found unexpected variable attribute.", attribute);
    }

    @Test
    public void testGetVariableDimensionIndexByNameFound() {
	NetcdfFile ncFile = TestUtil
		.getTestNetCdfFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LON_VARIABLE_NAMES);
	int dimensionIndex = NetCdfUtil.getVariableDimensionIndexByName(
		variable, NetCdfUtil.LON_VARIABLE_NAMES);
	assertTrue("Did not find expected variable.",
		dimensionIndex != NetCdfUtil.NOT_FOUND);
    }

    @Test
    public void testGetVariableDimensionIndexByNameNotFound() {
	NetcdfFile ncFile = TestUtil
		.getTestNetCdfFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
	Variable variable = NetCdfUtil.getFileVariableByName(ncFile,
		NetCdfUtil.LON_VARIABLE_NAMES);
	int dimensionIndex = NetCdfUtil.getVariableDimensionIndexByName(
		variable, TestUtil.TEST_NAMES_WILL_NOT_FIND);
	assertEquals("Found unexpected variable.", NetCdfUtil.NOT_FOUND,
		dimensionIndex);
    }
}
