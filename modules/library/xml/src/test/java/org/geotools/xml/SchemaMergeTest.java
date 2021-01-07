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
package org.geotools.xml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.test.TestData;
import org.geotools.xml.schema.Schema;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/** @author dzwiers */
public class SchemaMergeTest {

    protected SAXParser parser;

    /*
     * @see TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);
        parser = spf.newSAXParser();
    }

    @Test
    public void testMergeSchema() {
        // will load a doc that includes two schema docs which duplicate definitions

        File f;
        try {
            f = TestData.file(this, "merge.xsd");
            URI u = f.toURI();
            XSISAXHandler contentHandler = new XSISAXHandler(u);
            XSISAXHandler.setLogLevel(Level.WARNING);

            try {
                parser.parse(f, contentHandler);
            } catch (Exception e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                Assert.fail(e.toString());
            }

            try {
                Assert.assertNotNull("Schema missing", contentHandler.getSchema());
                // System.out.println(contentHandler.getSchema());

                Schema schema = contentHandler.getSchema();

                Assert.assertEquals(
                        "Should only have 2 elements, had " + schema.getElements().length,
                        2,
                        schema.getElements().length);
                Assert.assertEquals(
                        "Should only have 1 complexType, had " + schema.getComplexTypes().length,
                        1,
                        schema.getComplexTypes().length);

            } catch (Exception e) {
                java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
                Assert.fail(e.toString());
            }
        } catch (IOException e1) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e1);
            Assert.fail(e1.toString());
        }
    }
}
