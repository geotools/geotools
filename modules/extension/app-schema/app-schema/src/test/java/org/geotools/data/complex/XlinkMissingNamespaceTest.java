/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URL;
import org.geotools.data.complex.config.AppSchemaDataAccessConfigurator;
import org.geotools.data.complex.config.AppSchemaDataAccessDTO;
import org.geotools.data.complex.config.XMLConfigDigester;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Test;

/**
 * To test client properties of app-schema mapping file
 *
 * @author Jacqui Githaiga (Curtin University of Technology)
 */
public class XlinkMissingNamespaceTest extends AppSchemaTestSupport {
    /**
     * Illustrates that if xlink namespace has not been declared in the app-schema mapping file,the
     * client property is set as if for href (in the no-name namespace).
     *
     * <p>This test shows correct behaviour by throwing an exception reporting the undeclared
     * namespace.
     */
    @Test
    public void testGetClientProperties() throws IOException {
        try {
            XMLConfigDigester reader = new XMLConfigDigester();

            URL url = getClass().getResource("/test-data/MappedFeatureMissingNamespaceXlink.xml");
            AppSchemaDataAccessDTO config = reader.parse(url);
            AppSchemaDataAccessConfigurator.buildMappings(config);
            fail("No exception caught");

        } catch (Exception ex) {
            assertSame(java.io.IOException.class, ex.getClass());
        }
    }
}
