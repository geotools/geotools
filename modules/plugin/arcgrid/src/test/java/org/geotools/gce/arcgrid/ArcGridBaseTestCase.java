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
import java.io.FileFilter;
import java.io.StringWriter;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.geotools.TestData;

/** @author Giannecchini */
public abstract class ArcGridBaseTestCase extends TestCase {

    protected static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ArcGridBaseTestCase.class);

    protected File[] testFiles;

    public ArcGridBaseTestCase(String name) {

        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        // check that it exisits
        File file = TestData.copy(this, "arcgrid/arcgrid.zip");
        assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, "arcgrid/arcgrid.zip");

        testFiles =
                TestData.file(this, "arcgrid/")
                        .listFiles(
                                new FileFilter() {

                                    public boolean accept(File pathname) {

                                        return new ArcGridFormat().accepts(pathname);
                                    }
                                });
    }

    protected void tearDown() throws Exception {
        final File[] fileList = TestData.file(this, "").listFiles();
        final int length = fileList.length;
        for (File file : fileList) {
            if (file.isDirectory()) {
                file.delete();

                continue;
            }

            if (!file.getName().endsWith("zip")) {
                file.delete();
            }
        }
        super.tearDown();
    }

    public void testAll() throws Exception {

        final StringBuilder errors = new StringBuilder();
        final int length = testFiles.length;
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
            fail(errors.toString());
        }
    }

    public abstract void runMe(File file) throws Exception;
}
