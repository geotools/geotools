/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.grib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.geotools.TestData;
import org.junit.Assert;
import org.junit.Test;

/**
 * Simple {@link GribUtilities} tests
 */
public class GribUtilitiesTest extends Assert {

    @Test
    public void testDataDir() throws FileNotFoundException, IOException {

        File wrongDir = new File("wrongfolderfortests");
        final File testDir = TestData.file(this, ".").getCanonicalFile();
        System.setProperty("GRIB_CACHE_DIR", testDir.getAbsolutePath());
        boolean isValid = GribUtilities.isValid(testDir);
        assertTrue(isValid);

        isValid = GribUtilities.isValid(wrongDir);
        assertFalse(isValid);

        wrongDir = TestData.file(this, "sampleGrib.grb2").getCanonicalFile();
        isValid = GribUtilities.isValid(wrongDir);
        assertFalse(isValid);
    }
}
