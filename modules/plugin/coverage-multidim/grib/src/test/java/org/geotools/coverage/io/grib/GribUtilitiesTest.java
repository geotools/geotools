/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.junit.Test;
import ucar.unidata.util.StringUtil2;

/** Simple {@link GribUtilities} tests */
public class GribUtilitiesTest extends GribTest {

    @Test
    public void testCacheDir() throws FileNotFoundException, IOException {

        File wrongDir = new File("wrongfolderfortests");
        boolean isValid = GribUtilities.isValid(cacheDir);
        assertTrue(isValid);

        isValid = GribUtilities.isValid(wrongDir);
        assertFalse(isValid);

        final String name = "sample.grb2";
        final String index = name + ".gbx9";
        File sampleGrib = TestData.file(this, name).getCanonicalFile();
        GRIBFormat format = new GRIBFormat();

        AbstractGridCoverage2DReader reader = null;
        try {
            reader = format.getReader(sampleGrib);
            assertNotNull(reader);

            // Mimic the DiskCache2 makeCachePath behavior
            String cachePath = sampleGrib.getAbsolutePath();
            if (cachePath.startsWith("/")) {
                cachePath = cachePath.substring(1);
            }
            if (cachePath.endsWith("/")) {
                cachePath = cachePath.substring(0, cachePath.length() - 1);
            }
            cachePath = StringUtil2.remove(cachePath, ':');

            // Set the index file
            cachePath = cachePath.replace(name, index);

            final File sampleGribIndex = new File(cacheDir, cachePath);
            assertTrue(sampleGribIndex.exists());
        } finally {
            // Dispose
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Does nothing
                }
            }
        }
    }
}
