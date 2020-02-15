/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.logging.Logger;
import junit.framework.TestCase;
import junit.framework.TestResult;
import org.xml.sax.InputSource;

/** @author <a href="mailto:joel@lggi.com">Joel Skelton</a> */
public class RunStoredTest extends TestCase {
    private static final Logger LOG =
            org.geotools.util.logging.Logging.getLogger(RunStoredTest.class);

    private static String TEST_DIRECTORY = "src/main/resources/org/geotools/test-data/xml/geometry";
    // TODO: use TestData.copy and acquire files from sample-data module

    private FilenameFilter xmlFilter =
            new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".xml");
                }
            };

    /** Load and run all test files. */
    public void testGeometriesFromXML() throws IOException {
        GeometryTestParser parser = new GeometryTestParser();
        File dir = new File(TEST_DIRECTORY);
        if (dir.isDirectory()) {
            for (File testFile : dir.listFiles(xmlFilter)) {
                LOG.info("Loading test description file:" + testFile);
                FileInputStream inputStream = new FileInputStream(testFile);
                InputSource inputSource = new InputSource(inputStream);
                GeometryTestContainer tests = parser.parseTestDefinition(inputSource);
                assertTrue(
                        "Failed test(s) in: " + testFile.getName(),
                        tests.runAllTestCases(new TestResult()));
            }
        }
    }
}
