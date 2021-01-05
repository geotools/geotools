/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.gtopo30;

import java.io.File;
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author Simone Giannecchini */
public abstract class GT30TestBase {

    protected GridCoverage2D gc;

    protected Logger logger =
            org.geotools.util.logging.Logging.getLogger(GT30ReaderWriterTest.class);

    protected File newDir;

    protected String fileName = "W002N52";

    @Test
    public abstract void test() throws Exception;

    /** Unpack the gtopo files from the supplied zip file. */
    protected void unpackGTOPO() throws Exception {
        // check that it exisits
        File file = TestData.file(this, fileName + ".zip");
        Assert.assertTrue(file.exists());

        // unzip it
        TestData.unzipFile(this, fileName + ".zip");
    }

    /**
     * Deleting all the file we created during tests. Since gtopo files are big we try to save space
     * on the disk!!!
     */
    protected void deleteAll(File file) {
        final File[] fileList = file.listFiles();
        for (File value : fileList) {
            if (value.isDirectory()) {
                deleteAll(value);
                value.delete();

                continue;
            }

            if (!value.getName().endsWith("zip") && !value.getName().startsWith("W002N52")) {
                value.delete();
            }
        }
    }

    @Before
    public void setUp() throws Exception {
        this.unpackGTOPO();
    }

    /*
     * @see TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
        deleteAll(TestData.file(this, ""));
    }
}
