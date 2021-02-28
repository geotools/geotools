/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.gce.arcgrid;

import java.io.File;
import java.io.StringWriter;
import java.util.logging.Logger;
import org.geotools.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Giannecchini */
public abstract class ArcGridBaseTestCase {

    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ArcGridBaseTestCase.class);

    protected File[] testFiles;

    @Before
    public void setUp() throws Exception {

        // check that it exisits
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        Assert.assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");

        testFiles =
                TestData.file(this, "arcgrid/")
                        .listFiles(pathname -> new ArcGridFormat().accepts(pathname));
    }

    @After
    public void tearDown() throws Exception {
        final File[] fileList = TestData.file(this, "").listFiles();
        for (File file : fileList) {
            if (file.isDirectory()) {
                file.delete();

                continue;
            }

            if (!file.getName().endsWith("zip")) {
                file.delete();
            }
        }
    }

    @Test
    public void testAll() throws Exception {
        final StringBuilder errors = new StringBuilder();
        for (File testFile : testFiles) {

            try {
                runMe(testFile);

            } catch (Exception e) {
                // e.printStackTrace();
                final StringWriter writer = new StringWriter();
                Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                errors.append("\nFile ")
                        .append(testFile.getAbsolutePath())
                        .append(" :\n")
                        .append(e.getMessage())
                        .append("\n")
                        .append(writer.toString());
            }
        }

        if (errors.length() > 0) {
            Assert.fail(errors.toString());
        }
    }

    public abstract void runMe(File file) throws Exception;
}
