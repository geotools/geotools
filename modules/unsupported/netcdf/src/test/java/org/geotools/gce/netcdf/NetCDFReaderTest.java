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
package org.geotools.gce.netcdf;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.geotools.gce.netcdf.test.TestData;
import org.geotools.gce.netcdf.test.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * unit tests for named class.
 */
public class NetCDFReaderTest {
    NetCDFReader classUnderTest;

    @Before
    public void setUp() throws Exception {
	classUnderTest = new NetCDFReader();
    }

    @After
    public void tearDown() throws Exception {
	classUnderTest = null;
    }

    @Test
    public void testConstructor() {
        File testFile = TestUtil.getTestFile(TestData.TEST_DATA_POSITIVE_LONGITUDE);
        NetCDFReader ncr = new NetCDFReader(testFile, null);
        assertEquals("Unexpected number of grid range dimensions.", 2, ncr.getOriginalGridRange().getDimension());
        assertEquals("Unexpected number of envelope dimensions.", 2, ncr.getOriginalEnvelope().getDimension());
    }
}
