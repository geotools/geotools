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
package org.geotools.validation.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.geotools.validation.dto.PlugInDTO;
import org.geotools.validation.dto.TestDTO;
import org.geotools.validation.dto.TestSuiteDTO;
import org.junit.Assert;
import org.junit.Test;

/**
 * XMLReaderTest purpose.
 *
 * <p>Description of XMLReaderTest ...
 *
 * <p>Capabilities:
 *
 * <ul>
 *   <li>Feature: description
 * </ul>
 *
 * <p>Example Use:
 *
 * <pre><code>
 * XMLReaderTest x = new XMLReaderTest(...);
 * </code></pre>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 * @version $Id$
 */
public class XMLReaderTest {

    protected FileReader features() throws FileNotFoundException {
        File file = new File("C:/Java/workspace/geoserver/conf/plugins/Constraint.xml");
        if (!file.exists()) {
            return null;
        }
        return new FileReader(file);
    }

    @Test
    public void testReadPlugIn() {
        try {

            FileReader fr = features();
            if (fr == null) return;

            PlugInDTO dto = XMLReader.readPlugIn(fr);
            Assert.assertNotNull("Error if null", dto);
            Assert.assertEquals("Name read", "Constraint", dto.getName());
            Assert.assertEquals(
                    "Description read",
                    "All features must pass the provided filter",
                    dto.getDescription());
            Assert.assertEquals(
                    "ClassName read",
                    "org.geoserver.validation.plugins.filter.OGCFilter",
                    dto.getClassName());
            Assert.assertNotNull("Should be one arg.", dto.getArgs());
            Assert.assertEquals("Should be one arg.", 1, dto.getArgs().size());
            Assert.assertTrue("Arg. name", dto.getArgs().containsKey("tempDirectory"));
            Assert.assertTrue(
                    "Arg. value : " + dto.getArgs().get("tempDirectory"),
                    dto.getArgs().containsValue(new URI("file:///c:/temp")));
        } catch (Exception e) {
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testReadTestSuite() {
        try {
            // set-up
            FileReader fr = features();
            if (fr == null) return;

            Map<String, PlugInDTO> m = new HashMap<>();
            PlugInDTO dto = XMLReader.readPlugIn(fr);
            m.put(dto.getName(), dto);
            fr = new FileReader("C:/Java/workspace/geoserver/conf/plugins/NameInList.xml");
            dto = XMLReader.readPlugIn(fr);
            m.put(dto.getName(), dto);

            fr = new FileReader("C:/Java/workspace/geoserver/conf/validation/RoadTestSuite.xml");

            TestSuiteDTO testsuite = XMLReader.readTestSuite("test", fr, m);
            Assert.assertEquals("TestSuite Name read", "RoadTestSuite", testsuite.getName());

            // multi line so cannot effectively test

            /*assertTrue("TestSuite Description read",("This test suite checks each road name to see \n"+
            "that they are of appropriate length and checks to \n"+
            "see if they are on the list of possible road names.\n"+
            "It also checks to see if any roads are contained in\n"+
            "a specified box.").equals(testsuite.getDescription()));*/
            TestDTO test = testsuite.getTests().get("NameLookup");

            Assert.assertNotNull("NameLookup does not exist as a test", test);

            // multi line so cannot effectively test
            // assertTrue("Test Description read","Checks to see if the road name is in the list of
            // possible names.".equals(test.getDescription()));
            Assert.assertNotNull("Should not be null", test.getPlugIn());
            Assert.assertEquals("Test plugInName read", "NameInList", test.getPlugIn().getName());

            Assert.assertNotNull("Should be one arg.", test.getArgs());
            Assert.assertEquals("Should be one arg.", 2, test.getArgs().size());
            Assert.assertTrue("Arg. name", test.getArgs().containsKey("LUTName"));

            // multi line so cannot effectively test
            // assertTrue("Arg. value :
            // "+test.getArgs().get("LUTName"),test.getArgs().containsValue("RoadNameLUT.xls"));
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }
}
