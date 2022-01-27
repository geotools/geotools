/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import ucar.ma2.Array;
import ucar.ma2.DataType;

public final class NetCDFUtilitiesTest {

    @Test
    public void testGetArray() throws Exception {

        // Test 7 Dimensions
        int[] dimensions = {1, 2, 3, 4, 5, 6, 7};
        DataType varDataType = DataType.INT;
        Array testArray = NetCDFUtilities.getArray(dimensions, varDataType);
        assertNotNull("testArray should not be null", testArray);
        assertEquals("Unexpected DataType", varDataType, testArray.getDataType());
        assertEquals("Unexpected number of dimensions", 7, testArray.getRank());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetArrayTooManyDims() {
        int[] dimensions = {1, 2, 3, 4, 5, 6, 7, 8};
        DataType varDataType = DataType.INT;
        NetCDFUtilities.getArray(dimensions, varDataType);
    }

    @Test
    public void testUriCache() throws IOException {
        final File file = TestData.file(this, "axistime.nc");
        URI uri = file.toURI();
        NetCDFUtilities.FileFormat format1 = NetCDFUtilities.getFormat(uri);
        NetCDFUtilities.FileFormat format2 = NetCDFUtilities.getFormat(uri);
        Assert.assertSame(format1, format2);
    }

    @Test
    public void testGetFormatXXE() throws Exception {
        // Run this unit test with FINE logging enabled to confirm that the
        // XML parser did not attempt to read the file in the DTD.
        NetCDFUtilities.getFormat(TestData.url(this, "xxe.nc").toURI());
    }
}
